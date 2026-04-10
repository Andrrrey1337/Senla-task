package org.example.dto.scooterModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO с информацией о модели самоката")
public class ScooterModelResponseDto {
    @Schema(description = "ID модели", example = "1")
    private Long id;
    @Schema(description = "Название модели", example = "Xiaomi M365")
    private String name;
    @Schema(description = "Цена за минуту", example = "5.50")
    private BigDecimal pricePerMinute;
    @Schema(description = "Максимальная скорость (км/ч)", example = "25")
    private Integer maxSpeed;
}
