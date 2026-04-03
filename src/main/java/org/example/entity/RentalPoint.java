package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "rental_points")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rental_points_seq")
    @SequenceGenerator(name = "rental_points_seq", sequenceName = "rental_points_id_seq", allocationSize = 50)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private RentalPoint parent;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal longitude;

}
