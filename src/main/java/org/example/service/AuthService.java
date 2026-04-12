package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.auth.JwtRequest;
import org.example.dto.auth.JwtResponse;
import org.example.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public JwtResponse login(JwtRequest jwtRequest) {
        // авторизация пользователя (благодаря бину AuthenticationManager пароль захешируется и сверится с тем что в бд)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        jwtRequest.getUsername(),
                        jwtRequest.getPassword()
                )
        );

        // получаем пользователя из бд
        final UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());

        // генерим для него токен
        String token = jwtService.generateToken(userDetails);

        log.info("Пользователь {} успешно вошел в систему", jwtRequest.getUsername());

        return new JwtResponse(token);
    }

}
