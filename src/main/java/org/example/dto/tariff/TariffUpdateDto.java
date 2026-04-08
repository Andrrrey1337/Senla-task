package org.example.dto.tariff;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TariffUpdateDto {
    private String name;
    private String description;
    private BigDecimal price;
}
