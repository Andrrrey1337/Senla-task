package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tariffs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tariffs_seq")
    @SequenceGenerator(name = "tariffs_seq", sequenceName = "tariffs_id_seq", allocationSize = 50)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 20)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal price = BigDecimal.ZERO;
}
