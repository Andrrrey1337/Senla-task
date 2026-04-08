package org.example.dto.scooter;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ScooterCreateDto {
    private String serialNumber;
    private Long modelId;
    private Long rentalPointId;
    private Integer batteryLevel;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
