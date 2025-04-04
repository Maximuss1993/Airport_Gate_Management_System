package com.maximus.Airport_Gate_Management_System.gates;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.maximus.Airport_Gate_Management_System.airports.Airport;
import com.maximus.Airport_Gate_Management_System.flights.Flight;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Gate {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(length = 50, nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  @JsonFormat(pattern = "HH:mm")
  private LocalTime openingTime;

  @Column(nullable = false)
  @JsonFormat(pattern = "HH:mm")
  private LocalTime closingTime;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "flight_id")
  private Flight flight;

  @ManyToOne
  @JoinColumn(name = "airport_id")
  @JsonBackReference
  private Airport airport;

}
