package org.example.repository;

import org.example.entity.RentalPoint;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RentalPointRepository extends AbstractDao<RentalPoint, Long> {
    public RentalPointRepository() {
        super(RentalPoint.class);
    }

    public Optional<RentalPoint> findRentalPointByName(String name) {
        return entityManager.createQuery("SELECT p FROM RentalPoint p WHERE p.name = :name", RentalPoint.class)
                .setParameter("name", name)
                .getResultStream().findFirst();
    }
}
