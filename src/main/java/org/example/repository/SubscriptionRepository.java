package org.example.repository;

import org.example.entity.Subscription;
import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionRepository extends AbstractDao<Subscription, Long> {
    public SubscriptionRepository() {
        super(Subscription.class);
    }

}
