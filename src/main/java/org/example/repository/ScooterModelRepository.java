package org.example.repository;

import org.example.entity.ScooterModel;
import org.springframework.stereotype.Repository;

@Repository
public class ScooterModelRepository extends AbstractDao<ScooterModel, Long> {
    public ScooterModelRepository() {
        super(ScooterModel.class);
    }
}
