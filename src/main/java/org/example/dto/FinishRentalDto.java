package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FinishRentalDto {
    private BigDecimal endLatitude;
    private BigDecimal endLongitude;
}
