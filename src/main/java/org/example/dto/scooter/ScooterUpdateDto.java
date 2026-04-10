package org.example.dto.scooter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.example.entity.ScooterStatus;

import java.math.BigDecimal;

@Setter
@Getter
@Schema(description = "DTO для обновления данных самоката")
public class ScooterUpdateDto {
    @Schema(description = "Новый ID точки проката", example = "1")
    private Long rentalPointId;

    @Min(value = 0, message = "Заряд батареи не может быть меньше 0")
    @Max(value = 100, message = "Заряд батареи не может быть больше 100")
    @Schema(description = "Новый уровень заряда батареи", example = "80")
    private Integer batteryLevel;

    @NotNull(message = "Широта обязательна")
    @DecimalMin(value = "-90.0", message = "Широта должна быть от -90 до 90")
    @DecimalMax(value = "90.0", message = "Широта должна быть от -90 до 90")
    @Schema(description = "Новая широта", example = "53.9006")
    private BigDecimal latitude;

    @NotNull(message = "Долгота обязательна")
    @DecimalMin(value = "-180.0", message = "Долгота должна быть от -180 до 180")
    @DecimalMax(value = "180.0", message = "Долгота должна быть от -180 до 180")
    @Schema(description = "Новая долгота", example = "27.5590")
    private BigDecimal longitude;

    @Schema(description = "Новый статус самоката", example = "AVAILABLE")
    private ScooterStatus scooterStatus;
}
