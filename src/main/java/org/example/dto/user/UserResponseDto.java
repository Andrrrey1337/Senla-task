package org.example.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.example.entity.Role;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO с данными пользователя")
public class UserResponseDto {
    @Schema(description = "ID пользователя", example = "1")
    private Long id;
    @Schema(description = "Имя пользователя", example = "ivan_rider")
    private String username;
    @Schema(description = "Роль в системе", example = "USER")
    private Role role;
    @Schema(description = "Активен ли аккаунт", example = "true")
    private Boolean isActive;
    @Schema(description = "Текущий баланс", example = "100.00")
    private BigDecimal balance;
}
