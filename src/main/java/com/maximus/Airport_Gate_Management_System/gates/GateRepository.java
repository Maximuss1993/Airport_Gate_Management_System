package com.maximus.Airport_Gate_Management_System.gates;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface GateRepository extends JpaRepository<Gate, Integer> {

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

    @Modifying
    @Transactional
    @Query("update Gate gate set gate.flight = null where gate.id = :gateId")
    void parkOutFlightFromGate(Integer gateId);

}