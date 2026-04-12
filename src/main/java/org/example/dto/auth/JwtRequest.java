package org.example.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Запрос на авторизацию")
public class JwtRequest {
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Schema(description = "Логин пользователя", example = "ivan_rider")
    private String username;

    @NotBlank(message = "Пароль не может быть пустым")
    @Schema(description = "Пароль", example = "secretPass123")
    private String password;
}
