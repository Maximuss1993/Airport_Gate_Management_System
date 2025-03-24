package com.maximus.Airport_Gate_Management_System;

import com.github.javafaker.Faker;
import com.maximus.Airport_Gate_Management_System.airports.Airport;
import com.maximus.Airport_Gate_Management_System.airports.AirportRepository;
import com.maximus.Airport_Gate_Management_System.flights.Flight;
import com.maximus.Airport_Gate_Management_System.flights.FlightRepository;
import com.maximus.Airport_Gate_Management_System.gates.Gate;
import com.maximus.Airport_Gate_Management_System.gates.GateRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.LocalTime;

@SpringBootApplication
public class AirportGateManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirportGateManagementSystemApplication.class, args);
	}

	@Profile("dev")
	@Bean
	public CommandLineRunner commandLineRunner(
			AirportRepository airportRepository,
			GateRepository gateRepository,
			FlightRepository flightRepository
	) {
		return args -> {
			final int NUM_OF_GATES = 20;
			final int NUM_OF_FLIGHTS = 30;

			Airport airport = Airport.builder()
					.name("Nikola Tesla")
					.location("Belgrade")
					.build();
			airportRepository.save(airport);

			for (int i = 1; i <= NUM_OF_GATES; i++) {
				Faker faker = new Faker();

				var oHours = faker.number().numberBetween(0, 24);
				var oMinutes = faker.number().numberBetween(0, 59);
				LocalTime openingTime = LocalTime.of(oHours, oMinutes);

				var cHours = faker.number().numberBetween(0, 24);
				var cMinutes = faker.number().numberBetween(0, 59);
				LocalTime closingTime = LocalTime.of(cHours, cMinutes);

				Gate gate = Gate.builder()
						.name("Gate #" + i)
						.openingTime(openingTime)
						.closingTime(closingTime)
						.airport(airport)
						.build();
				gateRepository.save(gate);
			}

			for (int i = 1; i <= NUM_OF_FLIGHTS; i++) {
				Faker faker = new Faker();
				var aHours = faker.number().numberBetween(0, 24);
				var aMinutes = faker.number().numberBetween(0, 59);
				LocalTime arrivingTime = LocalTime.of(aHours, aMinutes);

				var flNumber = "F#" + faker.number().numberBetween(100, 999);
				while (flightRepository.existsByFlightNumber(flNumber)) {
					flNumber = "F#" + faker.number().numberBetween(100, 999);
				}

				Flight flight = Flight.builder()
						.flightNumber(flNumber)
						.arrivingTime(arrivingTime)
						.build();
				flightRepository.save(flight);
			}
		};
	}
}


