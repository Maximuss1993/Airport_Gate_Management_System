package com.maximus.Airport_Gate_Management_System.gates;

import com.maximus.Airport_Gate_Management_System.flights.Flight;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface GateRepository extends JpaRepository<Gate, Integer> {

  Logger log = LoggerFactory.getLogger(GateRepository.class);

  @Query("select gate from Gate gate where " +
      "(gate.openingTime <= :localTime " +
      "and gate.closingTime >= :localTime) " +
      "or (gate.openingTime > gate.closingTime " +
      "and (gate.openingTime <= :localTime " +
      "or gate.closingTime >= :localTime)) " +
      "and gate.flight is null")
  List<Gate> findAllAvailableGates(
      @Param("localTime") LocalTime localTime);

  @Query("select gate from Gate gate where " +
      "(gate.openingTime <= :localTime " +
      "and gate.closingTime >= :localTime) " +
      "or (gate.openingTime > gate.closingTime " +
      "and (gate.openingTime <= :localTime " +
      "       or gate.closingTime >= :localTime)) " +
      "and gate.flight is null")
  Page<Gate> findFirstAvailableGate(
      @Param("localTime") LocalTime localTime,
      Pageable pageable);

  @Transactional
  default void parkOutFlightFromGate(Integer gateId) {
    Gate gate = findById(gateId).orElseThrow(() ->
        new EntityNotFoundException("Gate not found, ID: " + gateId)
    );
    Flight flight = gate.getFlight();
    if (flight == null) {
      log.debug("Gate ID {} does not have a flight associated.", gateId);
      return;
    }
    gate.setFlight(null);
    flight.setGate(null);
    save(gate);
    log.trace("Flight ID {} successfully removed from Gate ID {}",
        flight.getId(), gateId);
  }

}