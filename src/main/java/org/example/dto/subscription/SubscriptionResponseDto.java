package org.example.dto.subscription;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Информация о типе абонемента")
public class SubscriptionResponseDto {
    @Schema(description = "ID абонемента", example = "1")
    private Long id;
    @Schema(description = "Название тарифа абонемента", example = "Месяц бесплатных стартов")
    private String name;
    @Schema(description = "Цена покупки", example = "499.00")
    private BigDecimal price;
    @Schema(description = "Срок действия (дни)", example = "30")
    private Integer durationDays;
    @Schema(description = "Включенные минуты", example = "60")
    private Integer includeMinutes;
    @Schema(description = "Бесплатный старт включен", example = "true")
    private Boolean isFreeStart;
}