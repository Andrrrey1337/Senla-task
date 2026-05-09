package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.promocode.PromoCodeCreateDto;
import org.example.dto.promocode.PromoCodeResponseDto;
import org.example.dto.promocode.PromoCodeUpdateDto;
import org.example.entity.PromoCode;
import org.example.exception.BusinessException;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.PromoCodeMapper;
import org.example.repository.PromoCodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface PromoCodeService {
    PromoCodeResponseDto createPromoCode(PromoCodeCreateDto dto);

    PromoCode findEntityById(Long id);

    PromoCode findByCode(String code);

    PromoCodeResponseDto getDtoById(Long id);

    List<PromoCodeResponseDto> findAllPromoCodes();

    void deletePromoCode(Long id);

    PromoCodeResponseDto updatePromoCode(Long id, PromoCodeUpdateDto promoCodeUpdateDto);

}
