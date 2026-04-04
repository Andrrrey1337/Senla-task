package org.example.repository;

import org.example.entity.Tariff;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TariffRepository extends AbstractDao<Tariff, Long> {
    public TariffRepository() {
        super(Tariff.class);
    }

    public Optional<Tariff> findByName(String name) {
        return entityManager.createQuery("SELECT t FROM Tariff t WHERE t.name = :name",  Tariff.class)
                .setParameter("name", name)
                .getResultStream().findFirst();
    }
}
