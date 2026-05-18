package com.bus.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "buses")
public class Bus {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String plateNumber;
    private Integer capacity;
    private String busType;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;
}