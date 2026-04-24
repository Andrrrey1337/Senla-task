package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.user.UserAdminUpdateDto;
import org.example.dto.user.UserResponseDto;
import org.example.dto.user.UserUpdateDto;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "Управление профилями пользователей и их балансом")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/username/{username}")
    @Operation(summary = "Получить пользователя по имени (админ)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username) { // админам
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @GetMapping("/me")
    @Operation(summary = "Получить мой профиль")
    public ResponseEntity<UserResponseDto> getMyProfile() { // только себе
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userMapper.toDto(userService.findById(currentUser.getId())));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID (админ/владелец)")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) { // админам или самому себе
        User user = userService.findById(id);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping("/{id}/balance")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Пополнить баланс")
    public ResponseEntity<UserResponseDto> addBalance(@PathVariable Long id, @RequestParam BigDecimal amount) {
        User user = userService.addBalance(id, amount);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping("/me/balance")
    @Operation(summary = "Пополнить мой баланс")
    public ResponseEntity<UserResponseDto> addMyBalance(@RequestParam BigDecimal amount) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.addBalance(currentUser.getId(), amount);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить личную информацию пользователя", description = "Изменение имени или пароля.")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id,@Valid @RequestBody UserUpdateDto userUpdateDto) {
        User user = userService.updateUser(id,userUpdateDto);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PatchMapping("/me")
    @Operation(summary = "Обновить мою информацию")
    public ResponseEntity<UserResponseDto> updateMe(@Valid @RequestBody UserUpdateDto dto) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.updateUser(currentUser.getId(), dto);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить права и статус пользователя (админ)", description = "Изменение роли или блокировка пользователя.")
    public ResponseEntity<UserResponseDto> updateAdminFields(@PathVariable Long id,@Valid @RequestBody UserAdminUpdateDto  userAdminUpdateDto) {
        User user = userService.updateAdminFields(id,userAdminUpdateDto);
        return ResponseEntity.ok(userMapper.toDto(user));
    }
}
