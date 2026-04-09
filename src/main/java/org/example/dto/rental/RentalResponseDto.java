package org.example.dto.rental;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalResponseDto {
    private Long id;
    private String username;
    private String scooterSerialNumber;
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
