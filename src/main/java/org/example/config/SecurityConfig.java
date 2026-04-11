package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter  jwtAuthenticationFilter;

    // шифровщик паролей
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // главный фильтр
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // отключаем csrf так как у нас нет сессий
                .csrf(AbstractHttpConfigurer::disable)
                // без сессий, будем jwt токен
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // эндпоинты для всех и даже не зарегистрированных
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll() // регистрация пользователя
                        .requestMatchers(HttpMethod.GET, "/api/tariffs/**").permitAll() // просмотр тарифов
                        .requestMatchers(HttpMethod.GET, "/api/scooter-models/**").permitAll() // просмотр моделей
                        .requestMatchers(HttpMethod.GET, "/api/points/**").permitAll() // просмотр точек
                        .requestMatchers(HttpMethod.POST, "/api/scooters/available").permitAll() // получить доступные самокаты
                        .requestMatchers(HttpMethod.POST, "/api/scooters/number/**").permitAll() // самокат по номеру
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // документация swagger
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
