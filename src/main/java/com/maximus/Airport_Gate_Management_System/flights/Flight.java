package com.maximus.Airport_Gate_Management_System.flights;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String airline;

    @Column(nullable = false)
    private LocalDate arrivingDate;

    @Column(nullable = false)
    private LocalTime arrivingTime;

}