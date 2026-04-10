package org.example.dto.scooterModel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "DTO для обновления параметров модели самоката")
public class ScooterModelUpdateDto {
    @Size(max = 50, message = "Название не должно превышать 50 символов")
    @Schema(description = "Новое название модели", example = "Xiaomi M365")
    private String name;

    @DecimalMin(value = "0.0", message = "Цена не может быть отрицательной")
    @Schema(description = "Новая цена за минуту", example = "5.50")
    private BigDecimal pricePerMinute;

    @PositiveOrZero(message = "Максимальная скорость не может быть отрицательной")
    @Schema(description = "Новая максимальная скорость", example = "25")
    private Integer maxSpeed;
}
