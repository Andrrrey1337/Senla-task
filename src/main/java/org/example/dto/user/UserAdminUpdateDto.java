package org.example.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.example.entity.Role;

@Getter
@Setter
@Schema(description = "DTO для управления пользователем администратором")
public class UserAdminUpdateDto {
    @Schema(description = "Новая роль пользователя", example = "USER")
    private Role role;
    @Schema(description = "Статус активности (блокировка)", example = "true")
    private Boolean isActive;
}
