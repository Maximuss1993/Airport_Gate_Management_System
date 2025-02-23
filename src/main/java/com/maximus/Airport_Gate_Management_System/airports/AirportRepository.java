package com.maximus.Airport_Gate_Management_System.airports;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Integer> {

    List<Airport> findAllByLocation(String location);

    List<Airport> findAllByName(String name);

}
