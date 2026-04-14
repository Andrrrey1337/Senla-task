package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.promocode.PromoCodeUpdateDto;
import org.example.entity.PromoCode;
import org.example.exception.BusinessException;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.PromoCodeMapper;
import org.example.repository.PromoCodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;
    private final PromoCodeMapper promoCodeMapper;

    public PromoCode createPromoCode(PromoCode promoCode){
        if (promoCodeRepository.findByCode(promoCode.getCode()).isPresent()){
            throw new BusinessException("Промокод " + promoCode.getCode() + " уже существует");
        }
        log.info("Создан новый промокод: {}", promoCode.getCode());
        return promoCodeRepository.create(promoCode);
    }

    public PromoCode findPromoCodeById(Long id){
        PromoCode promoCode = promoCodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Промокод с ID " + id + " не найден"));

        log.info("Успешно найден промокод с ID: {}", id);
        return promoCode;
    }

    public List<PromoCode> findAllPromoCodes() {
        List<PromoCode> promoCodes = promoCodeRepository.findAll();
        log.info("Получен список всех промокодов. Количество: {}", promoCodes.size());
        return promoCodes;
    }

    public void deletePromoCode(Long id){
        findPromoCodeById(id);
        promoCodeRepository.deleteById(id);
        log.info("Промокод с ID {} успешно удален", id);
    }

    public PromoCode updatePromoCode(Long id, PromoCodeUpdateDto promoCodeUpdateDto){
        PromoCode promoCode = findPromoCodeById(id);

        if (promoCodeUpdateDto.getCode() != null && !promoCodeUpdateDto.getCode().equals(promoCode.getCode())
                && promoCodeRepository.findByCode(promoCodeUpdateDto.getCode()).isPresent()) {
            throw new BusinessException("Промокод '" + promoCodeUpdateDto.getCode() + "' уже существует");
        }

        promoCodeMapper.updatePromoCode(promoCodeUpdateDto, promoCode);
        log.info("Данные промокода с ID {} успешно обновлены", id);

        return promoCode;
    }




}
