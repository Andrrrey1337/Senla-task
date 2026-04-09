package org.example.dto.rental;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalAdminResponseDto {
    private Long id;
    private Long userId;
    private String username;
    private Long scooterId;
    private String scooterSerialNumber;
    private Long tariffId;
    private String tariffName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal startLatitude;
    private BigDecimal startLongitude;
    private BigDecimal endLatitude;
    private BigDecimal endLongitude;
    private BigDecimal price;
    private Boolean isActive;
    private BigDecimal distance;
    private String promoCode;
}
