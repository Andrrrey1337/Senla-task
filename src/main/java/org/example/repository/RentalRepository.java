package org.example.repository;

import org.example.entity.Rental;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RentalRepository extends AbstractDao<Rental, Long> {
    public RentalRepository() {
        super(Rental.class);
    }

    public Optional<Rental> findActiveRentalByUserId(Long userId) {
        return entityManager.createQuery("SELECT r FROM Rental r " +
                "WHERE r.user.id = :userId " +
                "AND r.isActive = :isActive", Rental.class)
                .setParameter("userId", userId)
                .setParameter("isActive", true)
                .getResultStream().findFirst();
    }

    public List<Rental> findByScooterId(Long scooterId) {
        return entityManager.createQuery("SELECT r FROM Rental r WHERE r.scooter.id = :scooterId", Rental.class)
                .setParameter("scooterId", scooterId)
                .getResultList();
    }

    public List<Rental> findAllByUserId(Long userId) {
        return entityManager.createQuery("SELECT r FROM Rental r " +
                        "WHERE r.user.id = :userId ", Rental.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
