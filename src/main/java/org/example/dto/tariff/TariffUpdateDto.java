package org.example.dto.tariff;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "DTO для обновления параметров тарифа")
public class TariffUpdateDto {
    @Size(max = 20, message = "Название не должно превышать 20 символов")
    @Schema(description = "Новое название тарифа", example = "Стандартный")
    private String name;

    @Size(max = 255, message = "Описание не должно превышать 255 символов")
    @Schema(description = "Новое описание тарифа", example = "Базовый тариф для всех пользователей")
    private String description;

    @DecimalMin(value = "0.0", message = "Цена не может быть отрицательной")
    @Schema(description = "Новая стоимость старта", example = "100.00")
    private BigDecimal price;
}
