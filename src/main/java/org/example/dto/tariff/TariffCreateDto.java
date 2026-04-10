package org.example.dto.tariff;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "DTO для создания нового тарифа")
public class TariffCreateDto {
    @NotBlank(message = "Название тарифа не может быть пустым")
    @Size(max = 20, message = "Название не должно превышать 20 символов")
    @Schema(description = "Название тарифа", example = "Стандартный")
    private String name;

    @Size(max = 255, message = "Описание не должно превышать 255 символов")
    @Schema(description = "Описание тарифа", example = "Базовый тариф для всех пользователей")
    private String description;

    @NotNull(message = "Цена тарифа обязательна")
    @DecimalMin(value = "0.0", message = "Цена не может быть отрицательной")
    @Schema(description = "Стоимость начала аренды", example = "100.00")
    private BigDecimal price;
}
