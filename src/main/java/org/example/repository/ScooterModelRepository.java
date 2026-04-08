package org.example.repository;

import org.example.entity.ScooterModel;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ScooterModelRepository extends AbstractDao<ScooterModel, Long> {
    public ScooterModelRepository() {
        super(ScooterModel.class);
    }

    public Optional<ScooterModel> findByName(String name) {
        return entityManager.createQuery("SELECT sm FROM ScooterModel sm WHERE sm.name = :name", ScooterModel.class)
                .setParameter("name", name)
                .getResultStream().findFirst();
    }
}
