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
@Schema(description = "Детальная информация об аренде (для админа)")
public class RentalAdminResponseDto {
    @Schema(description = "ID аренды", example = "1")
    private Long id;
    @Schema(description = "ID пользователя", example = "1")
    private Long userId;
    @Schema(description = "Имя пользователя", example = "ivan_rider")
    private String username;
    @Schema(description = "ID самоката", example = "1")
    private Long scooterId;
    @Schema(description = "Серийный номер самоката", example = "SN12345678")
    private String scooterSerialNumber;
    @Schema(description = "ID тарифа", example = "1")
    private Long tariffId;
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
    @Schema(description = "Активна ли аренда в данный момент", example = "true")
    private Boolean isActive;
    @Schema(description = "Пройденная дистанция (км)", example = "5.5")
    private BigDecimal distance;
    @Schema(description = "Использованный промокод", example = "SUMMER2024")
    private String promoCode;
}
