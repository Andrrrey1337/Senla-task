package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.entity.RentalPoint;
import org.example.entity.ScooterStatus;

import java.math.BigDecimal;

@Setter
@Getter
public class ScooterUpdateDto {
    private Long id;
    private RentalPoint rentalPoint;
    private Integer batteryLevel;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private ScooterStatus scooterStatus;
}
