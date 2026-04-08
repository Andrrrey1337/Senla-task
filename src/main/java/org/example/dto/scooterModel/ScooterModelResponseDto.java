package org.example.dto.scooterModel;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScooterModelResponseDto {
    private Long id;
    private String name;
    private BigDecimal pricePerMinute;
    private Integer maxSpeed;
}
