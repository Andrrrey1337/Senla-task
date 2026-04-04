package org.example.repository;

import org.example.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository extends AbstractDao<User,Long>{
    public UserRepository() {
        super(User.class);
    }

    public Optional<User> findByUsername(String username) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultStream().findFirst();
    }
}
