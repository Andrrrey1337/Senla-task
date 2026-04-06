package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.entity.Role;

@Getter
@Setter
public class UserUpdateDto {
    private String username;
    private String password;
    private Role role;
    private Boolean isActive;

}
