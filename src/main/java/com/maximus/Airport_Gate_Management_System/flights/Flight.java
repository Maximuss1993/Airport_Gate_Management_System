package com.maximus.Airport_Gate_Management_System.flights;

import com.maximus.Airport_Gate_Management_System.gates.Gate;
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
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 50, nullable = false, unique = true)
    private String flightNumber;

    @Column(nullable = false)
    private LocalTime arrivingTime;

    @OneToOne
    @JoinColumn(name = "gate_id")
    private Gate gate;

}