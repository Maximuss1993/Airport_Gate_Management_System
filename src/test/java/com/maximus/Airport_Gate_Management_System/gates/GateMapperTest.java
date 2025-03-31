package com.maximus.Airport_Gate_Management_System.gates;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GateMapperTest {

  private GateMapper gateMapper;

  @BeforeEach
  void setUp() {
    gateMapper = GateMapper.INSTANCE;
  }

  @Test
  public void should_map_gate_dto_to_gate() {
    GateDto dto = new GateDto(
        "TestGateDto",
        LocalTime.of(1, 0),
        LocalTime.of(2, 0));
    Gate gate = gateMapper.toGate(dto);
    assertEquals(dto.name(), gate.getName());
    assertEquals(dto.openingTime(), gate.getOpeningTime());
    assertEquals(dto.closingTime(), gate.getClosingTime());
  }

  @Test
  public void should_map_gate_to_gate_response_dto() {
    Gate gate = Gate.builder()
        .name("TestGate")
        .openingTime(LocalTime.of(1, 0))
        .closingTime(LocalTime.of(3, 0))
        .build();
    GateResponseDto responseDto = gateMapper.toGateResponseDto(gate);
    assertEquals(gate.getName(), responseDto.name());
    assertEquals(gate.getOpeningTime(), responseDto.openingTime());
    assertEquals(gate.getClosingTime(), responseDto.closingTime());
  }

  @Test
  public void should_update_gate_from_dto() {
    var oldOpeningTime = LocalTime.of(1, 0);
    var oldClosingTime = LocalTime.of(2, 0);
    var newOpeningTime = LocalTime.of(12, 0);
    var newClosingTime = LocalTime.of(13, 0);
    Gate gate = Gate.builder()
        .name("TestGate")
        .openingTime(oldOpeningTime)
        .closingTime(oldClosingTime)
        .build();
    GateDto dto = new GateDto(
        "TestGateDto",
        newOpeningTime,
        newClosingTime);
    gateMapper.updateGateFromDto(dto, gate);
    assertEquals(gate.getName(), dto.name());
    assertEquals(gate.getOpeningTime(), dto.openingTime());
    assertEquals(gate.getClosingTime(), dto.closingTime());
  }

  @Test
  public void should_update_gate_from_dto_null_name() {
    var oldOpeningTime = LocalTime.of(1, 0);
    var oldClosingTime = LocalTime.of(2, 0);
    var newOpeningTime = LocalTime.of(12, 0);
    var newClosingTime = LocalTime.of(13, 0);
    Gate gate = Gate.builder()
        .name("TestGate")
        .openingTime(oldOpeningTime)
        .closingTime(oldClosingTime)
        .build();
    GateDto dto = new GateDto(
        null,
        newOpeningTime,
        newClosingTime);
    var nameBeforeUpdating = gate.getName();
    gateMapper.updateGateFromDto(dto, gate);
    var nameAfterUpdating = gate.getName();
    assertEquals(nameBeforeUpdating, nameAfterUpdating);
    assertEquals(gate.getOpeningTime(), dto.openingTime());
    assertEquals(gate.getClosingTime(), dto.closingTime());
  }

  @Test
  public void should_update_gate_from_dto_null_openingTime() {
    var oldOpeningTime = LocalTime.of(1, 0);
    var oldClosingTime = LocalTime.of(2, 0);
    var newClosingTime = LocalTime.of(13, 0);
    Gate gate = Gate.builder()
        .name("TestGate")
        .openingTime(oldOpeningTime)
        .closingTime(oldClosingTime)
        .build();
    GateDto dto = new GateDto(
        "TestGateDto",
        null,
        newClosingTime);
    var openingTimeBeforeUpdating = gate.getOpeningTime();
    gateMapper.updateGateFromDto(dto, gate);
    var openingTimeAfterUpdating = gate.getOpeningTime();
    assertEquals(gate.getName(), dto.name());
    assertEquals(openingTimeBeforeUpdating, openingTimeAfterUpdating);
    assertEquals(gate.getClosingTime(), dto.closingTime());
  }

  @Test
  public void should_update_gate_from_dto_null_closingTime() {
    var oldOpeningTime = LocalTime.of(1, 0);
    var oldClosingTime = LocalTime.of(2, 0);
    var newOpeningTime = LocalTime.of(12, 0);
    Gate gate = Gate.builder()
        .name("TestGate")
        .openingTime(oldOpeningTime)
        .closingTime(oldClosingTime)
        .build();
    GateDto dto = new GateDto(
        "TestGateDto",
        newOpeningTime,
        null);
    var closingTimeBeforeUpdating = gate.getClosingTime();
    gateMapper.updateGateFromDto(dto, gate);
    var closingTimeAfterUpdating = gate.getClosingTime();
    assertEquals(gate.getName(), dto.name());
    assertEquals(gate.getOpeningTime(), dto.openingTime());
    assertEquals(closingTimeBeforeUpdating, closingTimeAfterUpdating);
  }

  @Test
  public void should_throw_null_pointer_exception_updating_when_gate_is_null() {
    Gate gate = new Gate();
    assertThrows(NullPointerException.class,
        () -> gateMapper.updateGateFromDto(null, gate));
  }

  @Test
  public void should_throw_null_pointer_exception_updating_when_dto_is_null() {
    Gate gate = new Gate();
    assertThrows(NullPointerException.class,
        () -> gateMapper.updateGateFromDto(null, null));
  }

}