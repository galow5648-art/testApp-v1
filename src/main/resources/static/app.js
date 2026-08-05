const API_BASE = "https://testapp-v1.onrender.com/api";

const api = axios.create({ baseURL: API_BASE });

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

let currentTab = "login";

function switchTab(tab) {
  currentTab = tab;
  document.getElementById("tab-login").classList.toggle("active", tab === "login");
  document.getElementById("tab-register").classList.toggle("active", tab === "register");
  document.getElementById("submit-btn").textContent = tab === "login" ? "Войти" : "Зарегистрироваться";
  setMsg("");
}

function setMsg(text, type = "") {
  const el = document.getElementById("msg");
  el.textContent = text;
  el.className = type;
}

async function submitAuth() {
  const email = document.getElementById("email").value.trim();
  const password = document.getElementById("password").value;

  if (!email || !password) {
    setMsg("Заполните email и пароль", "error");
    return;
  }

  try {
    const endpoint = currentTab === "login" ? "/auth/login" : "/auth/register";
    const res = await api.post(endpoint, { email, password });

    localStorage.setItem("token", res.data.token);
    localStorage.setItem("email", res.data.email);

    setMsg("");
    showApp();
  } catch (err) {
    const message = err.response?.data?.message || "Ошибка авторизации";
    setMsg(message, "error");
  }
}

function logout() {
  localStorage.removeItem("token");
  localStorage.removeItem("email");
  document.getElementById("auth-section").style.display = "block";
  document.getElementById("app-section").style.display = "none";
}

function showApp() {
  document.getElementById("auth-section").style.display = "none";
  document.getElementById("app-section").style.display = "block";
  document.getElementById("current-email").textContent = localStorage.getItem("email");
  loadFlights();
  loadMyTickets();
}

async function loadFlights() {
  const container = document.getElementById("flights-list");
  container.innerHTML = '<p class="muted-note">Загрузка...</p>';

  try {
    const res = await api.get("/flights");
    if (res.data.length === 0) {
      container.innerHTML = '<p class="muted-note">Рейсов пока нет. Добавьте их через API (роль ADMIN).</p>';
      return;
    }

    container.innerHTML = res.data.map(f => `
      <div class="flight-row">
        <div class="flight-info">
          <span class="flight-code">${f.flightNumber}</span>
          <span class="flight-route">${f.departure} → ${f.destination}</span>
        </div>
        <button class="btn-buy" onclick="buyTicket(${f.id})">Купить билет</button>
      </div>
    `).join("");
  } catch (err) {
    container.innerHTML = '<p class="muted-note">Не удалось загрузить рейсы</p>';
  }
}

async function buyTicket(flightId) {
  const seatNumber = prompt("Номер места (например, 12A):", "12A");
  if (!seatNumber) return;

  try {
    await api.post("/tickets/buy", {
      flightId,
      seatNumber,
      price: 5000
    });
    await loadMyTickets();
  } catch (err) {
    alert(err.response?.data?.message || "Не удалось купить билет");
  }
}

async function loadMyTickets() {
  const container = document.getElementById("tickets-list");
  container.innerHTML = '<p class="muted-note">Загрузка...</p>';

  try {
    const res = await api.get("/tickets/my");
    if (res.data.length === 0) {
      container.innerHTML = '<p class="muted-note">У вас пока нет билетов</p>';
      return;
    }

    container.innerHTML = res.data.map(t => `
      <div class="ticket">
        <div class="ticket-body">
          <span class="ticket-route">${t.flightNumber} · ${t.departure} → ${t.destination}</span>
          <span class="ticket-meta">МЕСТО ${t.seatNumber}</span>
        </div>
        <div class="ticket-side">
          <span class="status-pill ${t.status}">${t.status}</span>
          ${t.status !== "PAID" ? `<button class="btn-pay" onclick="payTicket(${t.id})">Оплатить</button>` : ""}
        </div>
      </div>
    `).join("");
  } catch (err) {
    container.innerHTML = '<p class="muted-note">Не удалось загрузить билеты</p>';
  }
}

async function payTicket(ticketId) {
  try {
    await api.post("/payments/pay", { ticketId });
    await loadMyTickets();
  } catch (err) {
    alert(err.response?.data?.message || "Не удалось оплатить билет");
  }
}

if (localStorage.getItem("token")) {
  showApp();
}