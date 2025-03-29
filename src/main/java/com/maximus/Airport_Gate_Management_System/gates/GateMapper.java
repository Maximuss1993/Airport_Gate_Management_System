package com.maximus.Airport_Gate_Management_System.gates;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface GateMapper {

  GateMapper INSTANCE = Mappers.getMapper(GateMapper.class);

  Gate toGate(GateDto dto);

  GateResponseDto toGateResponseDto(@Valid Gate gate);

  default void updateGateFromDto(@NotNull GateDto dto, @NotNull Gate gate) {
    if (dto.name() != null)
      gate.setName(dto.name());
    if (dto.openingTime() != null)
      gate.setOpeningTime(dto.openingTime());
    if (dto.closingTime() != null)
      gate.setClosingTime(dto.closingTime());
  }
}