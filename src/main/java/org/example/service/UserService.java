package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.user.UserAdminUpdateDto;
import org.example.dto.user.UserUpdateDto;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.exception.BusinessException;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User registerUser(User user)  {
        String username = user.getUsername();
        if (userRepository.findByUsername(username).isPresent()) {
            throw new BusinessException("Пользователь с именем " + username + "' уже существует");
        }

        user.setRole(Role.USER);

        user = userRepository.create(user);
        log.info("Успешно зарегистрирован новый пользователь: ID={}, username={}", user.getId(), username);

        return user;
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с именем '" + username + " не найден"));

        log.info("Успешно выполнен поиск пользователя по username: {}", username);

        return user;
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с ID " + id + " не найден"));

        log.info("Успешно выполнен поиск пользователя по ID: {}", id);
        return user;
    }

    public User addBalance(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Сумма пополнения должна быть больше нуля");
        }
        User user = findById(userId);
        user.setBalance(user.getBalance().add(amount));

        log.info("Баланс пользователя с ID {} успешно пополнен на {}. Текущий баланс: {}", userId, amount, user.getBalance());

        return user;
    }

    public User updateUser(Long userId, UserUpdateDto userUpdateDto) {
        User user = findById(userId);

        if (userUpdateDto.getUsername() != null && !userUpdateDto.getUsername().equals(user.getUsername())
                && userRepository.findByUsername(userUpdateDto.getUsername()).isPresent()) {
            throw new BusinessException("Пользователь с таким именем '" + userUpdateDto.getUsername() + "' уже существует");
        }

        userMapper.updateEntity(userUpdateDto, user);

        log.info("Данные пользователя с ID {} успешно обновлены", user.getId());

        return user;
    }

    public User updateAdminFields(Long userId, UserAdminUpdateDto dto) {
        User user = findById(userId);

        userMapper.updateEntity(dto, user);

        log.info("Изменены права/статус пользователя с ID={}. Новая роль: {}, Активен: {}",
                user.getId(), user.getRole(), user.getIsActive());

        return user;
    }
}
