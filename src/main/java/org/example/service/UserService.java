package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.user.UserAdminUpdateDto;
import org.example.dto.user.UserCreateDto;
import org.example.dto.user.UserResponseDto;
import org.example.dto.user.UserUpdateDto;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.exception.BusinessException;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

public interface UserService {
    UserResponseDto registerUser(UserCreateDto dto);

    UserResponseDto findByUsername(String username);

    User findEntityById(Long id);

    UserResponseDto getDtoById(Long id);

    UserResponseDto addBalance(Long userId, BigDecimal amount);

    UserResponseDto updateUser(Long userId, UserUpdateDto userUpdateDto);

    void update(User user);

    UserResponseDto updateAdminFields(Long userId, UserAdminUpdateDto dto);

}
