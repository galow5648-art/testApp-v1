package com.example.demo;

import com.example.demo.entity.Flight;
import com.example.demo.repository.FlightRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(FlightRepository flightRepository) {
		return args -> {
			if (flightRepository.count() == 0) {
				Flight f1 = new Flight();
				f1.setFlightNumber("SU-101");
				f1.setDeparture("Москва");


				f1.setDestination("Сочи");
				f1.setFlightDate(LocalDateTime.now().plusDays(1));
				f1.setTotalSeats(180);

				Flight f2 = new Flight();
				f2.setFlightNumber("SU-202");
				f2.setDeparture("Санкт-Петербург");
				f2.setDestination("Казань");
				f2.setFlightDate(LocalDateTime.now().plusDays(2));
				f2.setTotalSeats(150);

				flightRepository.save(f1);
				flightRepository.save(f2);
			}
		};
	}
}