package com.maximus.Airport_Gate_Management_System.airports;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.maximus.Airport_Gate_Management_System.gates.Gate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Airport {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(length = 50, nullable = false, unique = true)
  private String name;

  @Column(length = 50, nullable = false)
  private String location;

  @OneToMany(mappedBy = "airport",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<Gate> gates;

}