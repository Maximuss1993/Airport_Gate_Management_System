package com.maximus.Airport_Gate_Management_System.gates;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GateRepository extends JpaRepository<Gate, Integer> {

    List<Gate> findAllByAvailable(boolean available);

}