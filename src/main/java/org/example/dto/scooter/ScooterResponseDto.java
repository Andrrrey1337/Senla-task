package org.example.dto.scooter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO с основной информацией о самокате для пользователей")
public class ScooterResponseDto {
    @Schema(description = "ID самоката", example = "1")
    private Long id;
    @Schema(description = "Серийный номер", example = "SN12345678")
    private String serialNumber;
    @Schema(description = "ID модели", example = "1")
    private Long modelId;
    @Schema(description = "Название модели", example = "Xiaomi M365")
    private String modelName;
    @Schema(description = "ID точки проката", example = "1")
    private Long rentalPointId;
    @Schema(description = "Уровень заряда батареи", example = "80")
    private Integer batteryLevel;
    @Schema(description = "Широта", example = "53.9006")
    private BigDecimal latitude;
    @Schema(description = "Долгота", example = "27.5590")
    private BigDecimal longitude;
}
