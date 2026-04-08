package org.example.dto.user;

import lombok.*;
import org.example.entity.Role;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private Long id;
    private String username;
    private Role role;
    private Boolean isActive;
    private BigDecimal balance;
}
