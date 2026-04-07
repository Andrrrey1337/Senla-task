package org.example.repository;

import org.example.entity.PromoCode;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PromoCodeRepository extends AbstractDao<PromoCode, Long> {
    public PromoCodeRepository() {
        super(PromoCode.class);
    }

    public Optional<PromoCode> findByCode(String code){
        return entityManager.createQuery("SELECT p FROM PromoCode p WHERE p.code = :code", PromoCode.class)
                .setParameter("code", code)
                .getResultStream()
                .findFirst();
    }
}
