package org.example.controller;

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
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody UserCreateDto userCreateDto) {
        User user = userService.registerUser(userMapper.toEntity(userCreateDto));
        return new ResponseEntity<>(userMapper.toDto(user), HttpStatus.CREATED);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping("/{id}/balance")
    public ResponseEntity<UserResponseDto> addBalance(@PathVariable Long id, @RequestParam BigDecimal amount) {
        User user = userService.addBalance(id, amount);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDto) {
        User user = userService.updateUser(id,userUpdateDto);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponseDto> updateAdminFields(@PathVariable Long id, @RequestParam UserAdminUpdateDto  userAdminUpdateDto) {
        User user = userService.updateAdminFields(id,userAdminUpdateDto);
        return ResponseEntity.ok(userMapper.toDto(user));
    }
}
