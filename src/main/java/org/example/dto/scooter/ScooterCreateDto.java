package org.example.dto.scooter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "DTO для добавления нового самоката")
public class ScooterCreateDto {
    @NotBlank(message = "Серийный номер обязателен")
    @Size(max = 20, message = "Серийный номер не должен превышать 20 символов")
    @Schema(description = "Серийный номер", example = "SN12345678")
    private String serialNumber;

    @NotNull(message = "ID модели обязателен")
    @Schema(description = "ID модели самоката", example = "1")
    private Long modelId;

    @Schema(description = "ID точки проката, на которой находится самокат", example = "1")
    private Long rentalPointId;

    @Min(value = 0, message = "Заряд батареи не может быть меньше 0")
    @Max(value = 100, message = "Заряд батареи не может быть больше 100")
    @Schema(description = "Уровень заряда батареи (0-100)", example = "80")
    private Integer batteryLevel;

    @NotNull(message = "Широта обязательна")
    @DecimalMin(value = "-90.0", message = "Широта должна быть от -90 до 90")
    @DecimalMax(value = "90.0", message = "Широта должна быть от -90 до 90")
    @Schema(description = "Широта", example = "53.9006")
    private BigDecimal latitude;

    @NotNull(message = "Долгота обязательна")
    @DecimalMin(value = "-180.0", message = "Долгота должна быть от -180 до 180")
    @DecimalMax(value = "180.0", message = "Долгота должна быть от -180 до 180")
    @Schema(description = "Долгота", example = "27.5590")
    private BigDecimal longitude;
}
