package org.example.dto.point;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "DTO для создания новой точки проката")
public class RentalPointCreateDto {
    @NotBlank(message = "Название точки проката не может быть пустым")
    @Size(max = 50, message = "Название не должно превышать 50 символов")
    @Schema(description = "Название точки проката", example = "Центральный парк")
    private String name;

    @Schema(description = "ID родительской точки проката (для иерархии)", example = "1")
    private Long parentId;

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
