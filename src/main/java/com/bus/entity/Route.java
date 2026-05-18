package com.bus.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "routes")
public class Route {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String startLocation;
    private String endLocation;
    private Double distanceKm;
    private Double basePrice;
}