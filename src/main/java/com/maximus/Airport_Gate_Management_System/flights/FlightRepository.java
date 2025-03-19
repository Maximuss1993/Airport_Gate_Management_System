package com.maximus.Airport_Gate_Management_System.flights;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {


    boolean existsByFlightNumber(String flightNumber);

}
