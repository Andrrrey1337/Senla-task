package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_subscriptions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_subscriptions_seq")
    @SequenceGenerator(name = "user_subscriptions_seq", sequenceName = "user_subscriptions_id_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "remaining_minutes", nullable = false)
    private Integer remainingMinutes;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}