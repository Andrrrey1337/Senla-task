package org.example.dto.subscription;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "DTO для обновления абонемента")
public class SubscriptionUpdateDto {

    @Size(max = 50, message = "Название не должно превышать 50 символов")
    @Schema(description = "Новое название абонемента", example = "Премиум Месяц")
    private String name;

    @DecimalMin(value = "0.0", message = "Цена не может быть отрицательной")
    @Schema(description = "Новая стоимость", example = "499.00")
    private BigDecimal price;

    @Min(value = 1, message = "Длительность должна быть минимум 1 день")
    @Schema(description = "Новая длительность", example = "30")
    private Integer durationDays;

    @Min(value = 0, message = "Минуты не могут быть отрицательными")
    @Schema(description = "Новый пакет минут", example = "100")
    private Integer includeMinutes;

    @Schema(description = "Включен ли бесплатный старт", example = "true")
    private Boolean isFreeStart;
}