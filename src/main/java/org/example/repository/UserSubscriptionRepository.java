package org.example.repository;

import org.example.entity.UserSubscription;
import org.springframework.stereotype.Repository;

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
}
