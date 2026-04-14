package org.example.dto.promocode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Информация о промокоде")
public class PromoCodeResponseDto {
    @Schema(description = "ID промокода в базе", example = "1")
    private Long id;
    @Schema(description = "Текст промокода", example = "SUMMER2024")
    private String code;
    @Schema(description = "Процент скидки", example = "15")
    private Integer discount;
    @Schema(description = "Дата окончания действия")
    private LocalDateTime endDate;
    @Schema(description = "Активен ли промокод в данный момент", example = "true")
    private Boolean isActive;
}