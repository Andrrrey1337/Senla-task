package org.example.dto.tariff;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO с информацией о тарифе")
public class TariffResponseDto {
    @Schema(description = "ID тарифа", example = "1")
    private Long id;
    @Schema(description = "Название тарифа", example = "Стандартный")
    private String name;
    @Schema(description = "Описание тарифа", example = "Базовый тариф для всех пользователей")
    private String description;
    @Schema(description = "Стоимость старта аренды", example = "100.00")
    private BigDecimal price;
}
