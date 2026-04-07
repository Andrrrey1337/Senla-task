package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "promo_codes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromoCode {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "promo_codes_seq")
    @SequenceGenerator(name = "promo_codes_seq", sequenceName = "promo_codes_id_seq", allocationSize = 50)
    private Long id;

    @Column(name = "code", length = 20, nullable = false, unique = true)
    private String code;

    @Column(name = "discount", nullable = false)
    private Integer discount;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
