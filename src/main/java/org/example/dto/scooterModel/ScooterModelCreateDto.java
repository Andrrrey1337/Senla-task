package org.example.dto.scooterModel;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ScooterModelCreateDto {
    private String name;
    private BigDecimal pricePerMinute;
    private Integer maxSpeed;
}
