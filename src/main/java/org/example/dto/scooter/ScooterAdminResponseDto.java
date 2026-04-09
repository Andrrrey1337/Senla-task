package org.example.dto.scooter;

import lombok.*;
import org.example.entity.ScooterStatus;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScooterAdminResponseDto {
    private Long id;
    private String serialNumber;
    private Long modelId;
    private String modelName;
    private Long rentalPointId;
    private Integer batteryLevel;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private ScooterStatus scooterStatus;
    private BigDecimal mileage;
}
