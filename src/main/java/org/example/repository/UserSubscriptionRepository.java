package org.example.repository;

import org.example.entity.UserSubscription;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserSubscriptionRepository extends AbstractDao<UserSubscription, Long> {
    public UserSubscriptionRepository() {
        super(UserSubscription.class);
    }

    public Optional<UserSubscription> findActiveByUserId(Long id) {
        return entityManager.createQuery("SELECT us FROM UserSubscription us " +
                        "WHERE us.user.id=:id " +
                        "AND us.isActive = true", UserSubscription.class)
                .setParameter("id", id)
                .getResultStream().findFirst();
    }

    public List<UserSubscription> findAllByUserId(Long userId) {
        return entityManager.createQuery("SELECT us FROM UserSubscription us WHERE us.user.id = :userId ORDER BY us.id DESC", UserSubscription.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
