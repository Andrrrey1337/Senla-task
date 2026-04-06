package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TariffUpdateDto;
import org.example.entity.Tariff;
import org.example.exception.BusinessException;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.TariffMapper;
import org.example.repository.TariffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TariffService {
    private final TariffRepository tariffRepository;
    private final TariffMapper tariffMapper;

    public Tariff createTariff(Tariff tariff) {
        if (tariffRepository.findByName(tariff.getName()).isPresent()) {
            throw new BusinessException("Тариф с названием '" + tariff.getName() + "' уже существует");
        }
        tariff = tariffRepository.create(tariff);

        log.info("Успешно создан новый тариф: ID={}, название='{}', цена={}",
                tariff.getId(), tariff.getName(), tariff.getPrice());

        return tariff;
    }

    @Transactional(readOnly = true)
    public Tariff findTariffById(Long id) {
        Tariff tariff = tariffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Тариф с ID " + id + " не найден"));

        log.info("Успешно найден тариф с ID: {}", id);
        return tariff;
    }

    @Transactional(readOnly = true)
    public Tariff findTariffByName(String name) {
        Tariff tariff = tariffRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Тариф с названием '" + name + "' не найден"));

        log.info("Успешно найден тариф с названием: '{}'", name);
        return tariff;
    }

    @Transactional(readOnly = true)
    public List<Tariff> findAllTariffs() {
        List<Tariff> tariffs = tariffRepository.findAll();

        log.info("Получен список всех тарифов. Количество записей: {}", tariffs.size());
        return tariffs;
    }

    public Tariff updateTariff(Long id, TariffUpdateDto tariffDto) {
        Tariff existTariff = findTariffById(id);

        if (tariffDto.getName() != null && !tariffDto.getName().equals(existTariff.getName())
                && tariffRepository.findByName(tariffDto.getName()).isPresent()) {
            throw new BusinessException("Тариф с названием '" + tariffDto.getName() + "' уже существует");
        }

        tariffMapper.updateEntity(tariffDto, existTariff);
        existTariff = tariffRepository.update(existTariff);
        log.info("Данные тарифа с ID {} успешно обновлены", existTariff.getId());

        return existTariff;
    }

    public void deleteTariffById(Long id) {
        findTariffById(id);
        tariffRepository.deleteById(id);
        log.info("Тариф с ID {} успешно удален", id);
    }
}
