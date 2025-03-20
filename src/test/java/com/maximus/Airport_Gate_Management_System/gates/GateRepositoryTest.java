package com.maximus.Airport_Gate_Management_System.gates;

import com.maximus.Airport_Gate_Management_System.flights.Flight;
import com.maximus.Airport_Gate_Management_System.flights.FlightRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class GateRepositoryTest {

    @Autowired
    private GateRepository gateRepository;

    @Autowired
    private FlightRepository flightRepository;

    @BeforeEach
    public void setUp() {
        gateRepository.deleteAll();
    }

    @Test
    public void should_save_gate_to_repository() {

        Gate gate = Gate.builder()
                .name("TestGate")
                .openingTime(LocalTime.of(1,0))
                .closingTime(LocalTime.of(13, 0))
                .build();

        Gate savedGate = gateRepository.save(gate);

        assertNotNull(savedGate);
        assertEquals(gate.getName(), savedGate.getName());
        assertEquals(gate.getOpeningTime(), savedGate.getOpeningTime());
        assertEquals(gate.getClosingTime(), savedGate.getClosingTime());
    }

    @Test
    public void should_find_all_gates() {

        int NUM_OF_GATES = 20;

        for (int i = 0; i < NUM_OF_GATES; i++) {
            Gate gate = Gate.builder()
                    .name("TestGate #" + i)
                    .openingTime(LocalTime.of(1,0))
                    .closingTime(LocalTime.of(2, 0))
                    .build();
            gateRepository.save(gate);
        }
        assertEquals(NUM_OF_GATES, gateRepository.findAll().size());
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void should_find_gate_by_id() {

        Gate gate = Gate.builder()
                .name("TestGate1")
                .openingTime(LocalTime.of(1, 0))
                .closingTime(LocalTime.of(2, 0))
                .build();

        gateRepository.save(gate);
        Gate foundGate = gateRepository.findById(gate.getId()).get();
        assertNotNull(foundGate);
    }

    @Test
    public void should_find_available_gate() {

        Gate gate1 = Gate.builder()
                .name("TestGate1")
                .openingTime(LocalTime.of(1, 0))
                .closingTime(LocalTime.of(3, 0))
                .build();
        gateRepository.save(gate1);

        Gate gate2 = Gate.builder()
                .name("TestGate2")
                .openingTime(LocalTime.of(10, 0))
                .closingTime(LocalTime.of(12, 0))
                .build();
        gateRepository.save(gate2);

        Gate gate3 = Gate.builder()
                .name("TestGate3")
                .openingTime(LocalTime.of(15, 0))
                .closingTime(LocalTime.of(3, 0))
                .build();
        gateRepository.save(gate3);

        var targetTime = LocalTime.of(2, 0);
        List<Gate> availableGates = gateRepository
                .findAllAvailableGates(targetTime);

        assertNotNull(availableGates);
        assertEquals(2, availableGates.size());
    }

    @Test
    public void should_find_first_available_gate() {

        Gate gate1 = Gate.builder()
                .name("TestGate1")
                .openingTime(LocalTime.of(1, 0))
                .closingTime(LocalTime.of(3, 0))
                .build();
        gateRepository.save(gate1);

        Gate gate2 = Gate.builder()
                .name("TestGate2")
                .openingTime(LocalTime.of(10, 0))
                .closingTime(LocalTime.of(12, 0))
                .build();
        gateRepository.save(gate2);

        var targetTime = LocalTime.of(2, 0);

        Pageable pageable = PageRequest.of(0, 1);
        Page<Gate> gatePage = gateRepository
                .findFirstAvailableGate(targetTime, pageable);

        Gate firstAvailableGate = gatePage.getContent().get(0);
        assertNotNull(firstAvailableGate);
    }

    @Test
    public void should_delete_gate() {

        Gate gate = Gate.builder()
                .name("TestGate1")
                .openingTime(LocalTime.of(1, 0))
                .closingTime(LocalTime.of(3, 0))
                .build();

        gateRepository.save(gate);
        gateRepository.deleteById(gate.getId());
        Optional<Gate> returnedGate = gateRepository.findById(gate.getId());

        assertTrue(returnedGate.isEmpty());
    }

    @Test
    @Transactional
    public void should_park_out_flight_from_the_gate() {
        Flight flight = Flight.builder()
                .flightNumber("F#123")
                .arrivingTime(LocalTime.of(1, 0))
                .build();
        flightRepository.save(flight);
        flightRepository.flush();

        Gate gate = Gate.builder()
                .name("TestGate1")
                .openingTime(LocalTime.of(1, 0))
                .closingTime(LocalTime.of(3, 0))
                .flight(flight)
                .build();
        gateRepository.save(gate);
        gateRepository.flush();

        var gateId = gate.getId();

        Gate savedGate = gateRepository.findById(gateId).orElseThrow(() ->
                new EntityNotFoundException("Gate not found, ID:" + gateId));

        assertEquals(savedGate.getFlight(), flight);

        gateRepository.parkOutFlightFromGate(gateId);
        gateRepository.flush();

        Gate updatedGate = gateRepository.findById(gateId).orElseThrow(() ->
                new EntityNotFoundException("Gate not found, ID:" + gateId));

        assertNull(updatedGate.getFlight(),
                "Flight should be null after parkOutFlightFromGate");

        Flight updatedFlight = flightRepository
                .findById(flight.getId()).orElseThrow(() ->
                        new EntityNotFoundException(
                                "Flight not found, ID:" + flight.getId()));
        assertNull(updatedFlight.getGate(),
                "Flight's gate should be null after parkOut");
    }
}