package org.example.dto.tariff;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TariffResponseDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
}
