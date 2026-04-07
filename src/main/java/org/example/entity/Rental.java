package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rentals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rentals_seq")
    @SequenceGenerator(name = "rentals_seq", sequenceName = "rentals_id_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scooter_id", nullable = false)
    private Scooter scooter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_id", nullable = false)
    private Tariff tariff;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "start_lat", nullable = false, precision = 10, scale = 8)
    private BigDecimal startLatitude;

    @Column(name = "start_lon", nullable = false, precision = 10, scale = 8)
    private BigDecimal startLongitude;

    @Column(name = "end_lat", precision = 10, scale = 8)
    private BigDecimal endLatitude;

    @Column(name = "end_lon", precision = 10, scale = 8)
    private BigDecimal endLongitude;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "distance", precision = 10, scale = 2)
    private BigDecimal distance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promo_code_id")
    private PromoCode promoCode;
}
