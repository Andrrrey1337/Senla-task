package org.example.dto.rental;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Информация об аренде для пользователя")
public class RentalResponseDto {
    @Schema(description = "ID аренды", example = "1")
    private Long id;
    @Schema(description = "Имя пользователя", example = "ivan_rider")
    private String username;
    @Schema(description = "Серийный номер самоката", example = "SN12345678")
    private String scooterSerialNumber;
    @Schema(description = "Название тарифа", example = "Стандартный")
    private String tariffName;
    @Schema(description = "Время начала аренды")
    private LocalDateTime startTime;
    @Schema(description = "Время завершения аренды")
    private LocalDateTime endTime;
    @Schema(description = "Широта старта", example = "53.9006")
    private BigDecimal startLatitude;
    @Schema(description = "Долгота старта", example = "27.5590")
    private BigDecimal startLongitude;
    @Schema(description = "Широта финиша", example = "53.9006")
    private BigDecimal endLatitude;
    @Schema(description = "Долгота финиша", example = "27.5590")
    private BigDecimal endLongitude;
    @Schema(description = "Итоговая стоимость", example = "100.00")
    private BigDecimal price;
    @Schema(description = "Статус (активна/завершена)", example = "true")
    private Boolean isActive;
    @Schema(description = "Дистанция (км)", example = "5.5")
    private BigDecimal distance;
    @Schema(description = "Примененный промокод", example = "SUMMER2024")
    private String promoCode;
}
