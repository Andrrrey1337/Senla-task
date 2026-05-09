package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.tariff.TariffCreateDto;
import org.example.dto.tariff.TariffResponseDto;
import org.example.dto.tariff.TariffUpdateDto;
import org.example.entity.Tariff;
import org.example.exception.BusinessException;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.TariffMapper;
import org.example.repository.TariffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface TariffService {
    TariffResponseDto createTariff(TariffCreateDto dto);

    Tariff findTariffById(Long id);

    Tariff findTariffByName(String name);

    List<TariffResponseDto> findAllTariffs();

    TariffResponseDto updateTariff(Long id, TariffUpdateDto tariffDto);

    TariffResponseDto getTariffDtoById(Long id);

    TariffResponseDto getTariffDtoByName(String name);

    void deleteTariffById(Long id);

}
