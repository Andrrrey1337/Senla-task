package org.example.repository;

import org.example.entity.Scooter;
import org.example.entity.ScooterStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ScooterRepository extends AbstractDao<Scooter, Long> {
    public ScooterRepository() {
        super(Scooter.class);
    }

    public Optional<Scooter> findBySerialNumber(String serialNumber) {
        return entityManager.createQuery("SELECT s FROM Scooter s WHERE s.serialNumber = :serialNumber", Scooter.class)
                .setParameter("serialNumber", serialNumber)
                .getResultStream()
                .findFirst();
    }

    public List<Scooter> findAvailableByRentalPoint(Long rentalPointId, Integer batteryLevel) {

        return entityManager.createQuery(
                     "SELECT s FROM Scooter s " +
                        "WHERE s.rentalPoint.id = :rentalPointId " +
                        "AND s.scooterStatus = :status " +
                        "AND s.batteryLevel >= :batteryLevel", Scooter.class)
                .setParameter("rentalPointId", rentalPointId)
                .setParameter("status", ScooterStatus.AVAILABLE)
                .setParameter("batteryLevel", batteryLevel)
                .getResultList();
    }
}
