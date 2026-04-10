package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.user.UserAdminUpdateDto;
import org.example.dto.user.UserCreateDto;
import org.example.dto.user.UserResponseDto;
import org.example.dto.user.UserUpdateDto;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "Управление профилями пользователей и их балансом")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @Operation(summary = "Регистрация пользователя", description = "Создает нового пользователя. По умолчанию назначается роль USER.")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        User user = userService.registerUser(userMapper.toEntity(userCreateDto));
        return new ResponseEntity<>(userMapper.toDto(user), HttpStatus.CREATED);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Получить пользователя по имени (админ/владелец)")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username) { // админам или самому себе
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID (админ/владелец)")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) { // админам или самому себе
        User user = userService.findById(id);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping("/{id}/balance")
    @Operation(summary = "Пополнить баланс")
    public ResponseEntity<UserResponseDto> addBalance(@PathVariable Long id, @RequestParam BigDecimal amount) {
        User user = userService.addBalance(id, amount);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить личную информацию пользователя", description = "Изменение имени или пароля.")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id,@Valid @RequestBody UserUpdateDto userUpdateDto) {
        User user = userService.updateUser(id,userUpdateDto);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Обновить права и статус пользователя (админ)", description = "Изменение роли или блокировка пользователя.")
    public ResponseEntity<UserResponseDto> updateAdminFields(@PathVariable Long id,@Valid @RequestBody UserAdminUpdateDto  userAdminUpdateDto) {
        User user = userService.updateAdminFields(id,userAdminUpdateDto);
        return ResponseEntity.ok(userMapper.toDto(user));
    }
}
