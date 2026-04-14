package org.example.dto.promocode;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "DTO для создания нового промокода")
public class PromoCodeCreateDto {

    @NotBlank(message = "Код не может быть пустым")
    @Size(max = 20, message = "Код не должен превышать 20 символов")
    @Schema(description = "Уникальный текст промокода", example = "SUMMER2024")
    private String code;

    @NotNull(message = "Скидка обязательна")
    @Min(value = 1, message = "Скидка должна быть минимум 1%")
    @Max(value = 100, message = "Скидка не может превышать 100%")
    @Schema(description = "Размер скидки в процентах", example = "15")
    private Integer discount;

    @Schema(description = "Дата окончания действия промокода (может быть пустой)")
    private LocalDateTime endDate;
}