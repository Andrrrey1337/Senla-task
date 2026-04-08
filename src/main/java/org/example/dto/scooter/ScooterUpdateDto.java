package org.example.dto.scooter;

import lombok.Getter;
import lombok.Setter;
import org.example.entity.ScooterStatus;

import java.math.BigDecimal;

@Setter
@Getter
public class ScooterUpdateDto {
    private Long rentalPointId;
    private Integer batteryLevel;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private ScooterStatus scooterStatus;
}
