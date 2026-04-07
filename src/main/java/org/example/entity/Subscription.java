package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subscriptions_seq")
    @SequenceGenerator(name = "subscriptions_seq", sequenceName = "subscriptions_id_seq", allocationSize = 50)
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "duration_days", nullable = false)
    private Integer durationDays;

    @Column(name = "is_free_start", nullable = false)
    @Builder.Default
    private Boolean isFreeStart = false;

    @Column(name = "include_minutes", nullable = false)
    @Builder.Default
    private Integer includeMinutes = 0;
}
