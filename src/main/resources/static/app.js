// Базовый URL backend'а
const API_BASE = "https://testapp-v1.onrender.com/api";

const api = axios.create({ baseURL: API_BASE });

// Автоматически подставляем JWT-токен в заголовок
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Интерцептор ответов: если токен невалиден или истёк (401), автоматически разлогиниваем
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      alert("Сессия истекла. Пожалуйста, войдите снова.");
      logout();
    }
    return Promise.reject(error);
  }
);

let currentTab = "login";

// Вспомогательная функция для защиты от XSS-атак
function escapeHtml(str) {
  if (!str) return "";
  return String(str)
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#039;");
}

function switchTab(tab) {
  currentTab = tab;
  document.getElementById("tab-login").className = tab === "login" ? "" : "secondary";
  document.getElementById("tab-register").className = tab === "register" ? "" : "secondary";
  document.getElementById("submit-btn").textContent = tab === "login" ? "Войти" : "Зарегистрироваться";
  setMsg("");
}

function setMsg(text, type = "") {
  const el = document.getElementById("msg");
  el.textContent = text;
  el.className = type;
}

async function submitAuth() {
  const emailInput = document.getElementById("email");
  const passwordInput = document.getElementById("password");
  const submitBtn = document.getElementById("submit-btn");

  const email = emailInput.value.trim();
  const password = passwordInput.value;

  if (!email || !password) {
    setMsg("Заполните email и пароль", "error");
    return;
  }

  submitBtn.disabled = true;
  setMsg("Загрузка...", "muted");

  try {
    const endpoint = currentTab === "login" ? "/auth/login" : "/auth/register";
    const res = await api.post(endpoint, { email, password });

    localStorage.setItem("token", res.data.token);
    localStorage.setItem("email", res.data.email);

    emailInput.value = "";
    passwordInput.value = "";
    setMsg("");
    showApp();
  } catch (err) {
    const message = err.response?.data?.message || "Ошибка авторизации";
    setMsg(message, "error");
  } finally {
    submitBtn.disabled = false;
  }
}

function logout() {
  localStorage.removeItem("token");
  localStorage.removeItem("email");

  const authSec = document.getElementById("auth-section");
  const appSec = document.getElementById("app-section");

  if (authSec) authSec.classList.remove("hidden");
  if (appSec) appSec.style.display = "none";
  setMsg("");
}

function showApp() {
  document.getElementById("auth-section").classList.add("hidden");
  document.getElementById("app-section").style.display = "block";
  document.getElementById("current-email").textContent = localStorage.getItem("email") || "";

  // Загружаем данные параллельно для ускорения
  Promise.all([loadFlights(), loadMyTickets()]);
}

async function loadFlights() {
  const container = document.getElementById("flights-list");
  container.innerHTML = '<p class="muted">Загрузка рейсов...</p>';

  try {
    const res = await api.get("/flights");
    if (!res.data || res.data.length === 0) {
      container.innerHTML = '<p class="muted">Рейсов пока нет.</p>';
      return;
    }

    container.innerHTML = res.data.map(f => `
      <div class="flight">
        <span><b>${escapeHtml(f.flightNumber)}</b>: ${escapeHtml(f.departure)} → ${escapeHtml(f.destination)}</span>
        <button onclick="buyTicket(${f.id})">Купить билет</button>
      </div>
    `).join("");
  } catch (err) {
    container.innerHTML = '<p class="muted error">Не удалось загрузить рейсы</p>';
  }
}

async function buyTicket(flightId) {
  const seatNumber = prompt("Номер места (например, 12A):", "12A");
  if (!seatNumber || !seatNumber.trim()) return;

  try {
    await api.post("/tickets/buy", {
      flightId,
      seatNumber: seatNumber.trim(),
      price: 5000
    });
    alert("Билет успешно забронирован!");
    await loadMyTickets();
  } catch (err) {
    alert(err.response?.data?.message || "Не удалось купить билет");
  }
}

async function loadMyTickets() {
  const container = document.getElementById("tickets-list");
  container.innerHTML = '<p class="muted">Загрузка билетов...</p>';

  try {
    const res = await api.get("/tickets/my");
    if (!res.data || res.data.length === 0) {
      container.innerHTML = '<p class="muted">У вас пока нет билетов</p>';
      return;
    }

    container.innerHTML = res.data.map(t => {
      const status = escapeHtml(t.status || 'RESERVED');
      return `
        <div class="ticket">
          <span><b>${escapeHtml(t.flightNumber)}</b> (${escapeHtml(t.departure)} → ${escapeHtml(t.destination)}), место ${escapeHtml(t.seatNumber)}</span>
          <span class="status ${status}">${status}</span>
          ${status !== "PAID" ? `<button onclick="payTicket(${t.id})">Оплатить</button>` : ""}
        </div>
      `;
    }).join("");
  } catch (err) {
    container.innerHTML = '<p class="muted error">Не удалось загрузить билеты</p>';
  }
}

async function payTicket(ticketId) {
  try {
    await api.post("/payments/pay", { ticketId });
    alert("Оплата прошла успешно!");
    await loadMyTickets();
  } catch (err) {
    alert(err.response?.data?.message || "Не удалось оплатить билет");
  }
}

// Автоматическая проверка авторизации при первом запуске
document.addEventListener("DOMContentLoaded", () => {
  if (localStorage.getItem("token")) {
    showApp();
  }
});