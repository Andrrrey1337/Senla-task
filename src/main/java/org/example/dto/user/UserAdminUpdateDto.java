package org.example.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.example.entity.Role;

@Getter
@Setter
public class UserAdminUpdateDto {
    private Role role;
    private Boolean isActive;
}
