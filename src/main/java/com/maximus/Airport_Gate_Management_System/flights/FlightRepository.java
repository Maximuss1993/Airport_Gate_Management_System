package com.maximus.Airport_Gate_Management_System.flights;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {

    List<Flight> findAllByArrivingDate(LocalDate date);

    List<Flight> findAllByArrivingTime(LocalTime time);

}
