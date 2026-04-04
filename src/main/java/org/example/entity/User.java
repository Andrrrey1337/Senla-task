package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_id_seq", allocationSize = 50)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false,  length = 50)
    private String password;

    @Column(name = "role", nullable = false, length = 50)
    @Enumerated(EnumType.STRING) // для конвертации в строку
    private Role role; // user or admin or manager

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "balance", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;
}
