package org.example.dto.scooterModel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "DTO для создания новой модели самоката")
public class ScooterModelCreateDto {
    @NotBlank(message = "Название модели не может быть пустым")
    @Size(max = 50, message = "Название не должно превышать 50 символов")
    @Schema(description = "Название модели", example = "Xiaomi M365")
    private String name;

    @NotNull(message = "Цена за минуту обязательна")
    @DecimalMin(value = "0.0", message = "Цена не может быть отрицательной")
    @Schema(description = "Цена аренды за одну минуту", example = "5.50")
    private BigDecimal pricePerMinute;

    @PositiveOrZero(message = "Максимальная скорость не может быть отрицательной")
    @Schema(description = "Максимальная скорость (км/ч)", example = "25")
    private Integer maxSpeed;
}
