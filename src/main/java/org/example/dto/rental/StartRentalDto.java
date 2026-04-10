package org.example.dto.rental;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO для начала новой аренды")
public class StartRentalDto {
    @NotNull(message = "ID пользователя обязателен")
    @Schema(description = "ID пользователя, арендующего самокат", example = "1")
    private Long userId;

    @NotNull(message = "ID самоката обязателен")
    @Schema(description = "ID самоката", example = "1")
    private Long scooterId;
    @NotNull(message = "ID тарифа обязателен")
    @Schema(description = "ID выбранного тарифа", example = "1")
    private Long tariffId;

    @Size(max = 20, message = "Промокод не может быть длиннее 20 символов")
    @Schema(description = "Промокод (если есть)", example = "SUMMER2024")
    private String promoCode;
}
