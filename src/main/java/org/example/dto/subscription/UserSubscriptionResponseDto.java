package org.example.dto.subscription;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Информация об активном абонементе пользователя")
public class UserSubscriptionResponseDto {
    @Schema(description = "ID записи", example = "1")
    private Long id;
    @Schema(description = "ID пользователя", example = "1")
    private Long userId;
    @Schema(description = "ID купленного абонемента", example = "2")
    private Long subscriptionId;
    @Schema(description = "Название абонемента", example = "Месяц фристартов")
    private String subscriptionName;
    @Schema(description = "Дата окончания действия")
    private LocalDateTime endDate;
    @Schema(description = "Оставшиеся минуты", example = "60")
    private Integer remainingMinutes;
    @Schema(description = "Активен ли абонемент", example = "true")
    private Boolean isActive;
}