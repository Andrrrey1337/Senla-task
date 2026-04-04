package org.example.repository;

import org.example.entity.RentalPoint;
import org.springframework.stereotype.Repository;

@Repository
public class RentalPointRepository extends AbstractDao<RentalPoint, Long> {
    public RentalPointRepository() {
        super(RentalPoint.class);
    }
}
