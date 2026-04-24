package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.scooter.ScooterCreateDto;
import org.example.dto.scooter.ScooterUpdateDto;
import org.example.entity.Scooter;
import org.example.exception.BusinessException;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.ScooterMapper;
import org.example.repository.RentalPointRepository;
import org.example.repository.ScooterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ScooterService {
    private final ScooterRepository scooterRepository;
    private final ScooterMapper scooterMapper;
    private final RentalPointRepository rentalPointRepository;

    public Scooter createScooter(ScooterCreateDto scooter) {
        String serialNumber = scooter.getSerialNumber();
        if (scooterRepository.findBySerialNumber(serialNumber).isPresent()) {
            throw new BusinessException("Самокат с серийным номером " + serialNumber + " уже существует в базе");
        }

        Scooter newScooter = scooterMapper.toEntity(scooter);

        if (scooter.getRentalPointId() != null) {
            newScooter.setRentalPoint(rentalPointRepository.findById(scooter.getRentalPointId())
                    .orElseThrow(() -> new ResourceNotFoundException("Точка проката не найдена")));
        }

        Scooter savedScooter = scooterRepository.create(newScooter);

        log.info("Успешно зарегистрирован новый самокат: SN={}, ID={}", serialNumber, savedScooter.getId());
        return savedScooter;
    }

    @Transactional(readOnly = true)
    public Scooter findScooterById(Long id) {
        Scooter scooter = scooterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Самокат с id " + id + " не найден"));

        log.info("Успешно выполнен поиск самоката по ID: {}", id);
        return scooter;
    }

    @Transactional(readOnly = true)
    public Scooter findScooterBySerialNumber(String serialNumber) {
        Scooter scooter = scooterRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Самокат с серийным номером " + serialNumber + " не найден"));

        log.info("Успешно выполнен поиск самоката по серийному номеру: {}", serialNumber);
        return scooter;
    }

    @Transactional(readOnly = true)
    public List<Scooter> findAvailableScooters(Long rentalPointId, Integer minBatteryLevel) {
        List<Scooter> scooters = scooterRepository.findAvailableByRentalPoint(rentalPointId, minBatteryLevel);

        log.info("Найден(о) {} свободных самокатов на точке {} с мин. зарядом {}", scooters.size(), rentalPointId, minBatteryLevel);
        return scooters;
    }

    public void deleteScooterById(Long scooterId) {
        findScooterById(scooterId);
        scooterRepository.deleteById(scooterId);
        log.info("Самокат с ID {} успешно удален из базы", scooterId);
    }

    public Scooter updateScooter(Long id, ScooterUpdateDto scooterDto) {
        Scooter scooter = findScooterById(id);

        scooterMapper.updateEntity(scooterDto, scooter);

        log.info("Данные самоката с ID {} успешно обновлены", scooter.getId());
        return scooter;
    }
}
