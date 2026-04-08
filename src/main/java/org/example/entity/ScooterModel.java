package org.example.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "scooter_models")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScooterModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scooter_models_seq")
    @SequenceGenerator(name = "scooter_models_seq", sequenceName = "scooter_models_id_seq", allocationSize = 50)
    private Long id;

    @Column(name = "name", nullable = false, length = 50, unique = true)
    private String name;

    @Column(name = "price_per_minute", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerMinute;

    @Column(name = "max_speed")
    private Integer maxSpeed;
}
