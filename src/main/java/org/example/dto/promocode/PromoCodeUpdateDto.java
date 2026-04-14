package org.example.dto.promocode;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "DTO для обновления промокода")
public class PromoCodeUpdateDto {

    @Size(max = 20, message = "Код не должен превышать 20 символов")
    @Schema(description = "Новый код", example = "AUTUMN2024")
    private String code;

    @Min(value = 1, message = "Скидка должна быть минимум 1%")
    @Max(value = 100, message = "Скидка не может превышать 100%")
    @Schema(description = "Новый размер скидки", example = "20")
    private Integer discount;

    @Schema(description = "Новая дата окончания действия")
    private LocalDateTime endDate;

    @Schema(description = "Включен или отключен", example = "true")
    private Boolean isActive;
}