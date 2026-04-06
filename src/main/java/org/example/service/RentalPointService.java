package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.RentalPointDataDto;
import org.example.dto.RentalPointUpdateDto;
import org.example.entity.RentalPoint;
import org.example.entity.Scooter;
import org.example.entity.ScooterStatus;
import org.example.exception.BusinessException;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.RentalPointMapper;
import org.example.repository.RentalPointRepository;
import org.example.repository.ScooterModelRepository;
import org.example.repository.ScooterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RentalPointService {
    private final RentalPointRepository rentalPointRepository;
    private final ScooterRepository scooterRepository;
    private final RentalPointMapper rentalPointMapper;

    public RentalPoint createRentalPoint(RentalPoint rentalPoint) {
        if (rentalPointRepository.findRentalPointByName(rentalPoint.getName()).isPresent()) {
            throw new BusinessException("Точка проката с названием " +  rentalPoint.getName() + " уже существует");
        }
        rentalPoint = rentalPointRepository.create(rentalPoint);
        log.info("Успешно создана новая точка проката: ID={}, название='{}'", rentalPoint.getId(), rentalPoint.getName());

        return rentalPoint;
    }

    @Transactional(readOnly = true)
    public RentalPoint findRentalPointById(Long rentalPointId) {
        if (rentalPointId == null) {
            return  null; // для нашего маппера, если id будет null вернем null, не обращаясь к бд
        }

        RentalPoint rentalPoint = rentalPointRepository.findById(rentalPointId)
                .orElseThrow(() -> new ResourceNotFoundException("Точка проката с ID " + rentalPointId + " не найдена"));

        log.info("Успешно найдена точка проката с ID: {}", rentalPointId);
        return rentalPoint;
    }

    @Transactional(readOnly = true)
    public RentalPoint findRentalPointByName(String name) {
        RentalPoint rentalPoint = rentalPointRepository.findRentalPointByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Точка проката с названием " + name + " не найдена"));

        log.info("Успешно найдена точка проката с названием: {}", name);
        return rentalPoint;
    }

    @Transactional(readOnly = true)
    public List<RentalPoint> findAllRentalPoints() {
        List<RentalPoint> rentalPoints = rentalPointRepository.findAll();

        log.info("Получен список всех точек проката. Количество: {}", rentalPoints.size());
        return rentalPoints;
    }

    public RentalPoint updateRentalPoint(Long id, RentalPointUpdateDto  rentalPointUpdateDto) {
        RentalPoint rentalPoint = findRentalPointById(id);
        rentalPointMapper.updateEntity(rentalPointUpdateDto, rentalPoint);

        if (rentalPointUpdateDto.getParentId() != null) {
            RentalPoint parentPoint = findRentalPointById(rentalPointUpdateDto.getParentId());
            rentalPoint.setParent(parentPoint);
        }

        RentalPoint updatedPoint = rentalPointRepository.update(rentalPoint);

        log.info("Данные точки проката с ID {} успешно обновлены", updatedPoint.getId());

        return updatedPoint;
    }

    public void deleteById(Long id) {
        findRentalPointById(id);
        rentalPointRepository.deleteById(id);
        log.info("Точка проката с ID {} успешно удалена", id);
    }

    @Transactional(readOnly = true)
    public List<Scooter> findAllScootersAtRentalPoint(Long rentalPointId) {
        findRentalPointById(rentalPointId);

        List<Scooter> scooters = scooterRepository.findAllByRentalPoint(rentalPointId);

        log.info("Найдено {} самокатов на точке ID={}", scooters.size(), rentalPointId);

        return scooters;
    }

    @Transactional(readOnly = true)
    public RentalPointDataDto getRentalPointDataById(Long rentalPointId) {
        RentalPoint rentalPoint = findRentalPointById(rentalPointId);
        List<Scooter> scooters = findAllScootersAtRentalPoint(rentalPointId);

        long availableCount = scooters.stream()
                .filter(s -> s.getScooterStatus() == ScooterStatus.AVAILABLE)
                .count();

        long rentedCount = scooters.stream()
                .filter(s -> s.getScooterStatus() == ScooterStatus.RENTED)
                .count();

        Map<String, Long> availableModels = scooters.stream()
                .filter(s -> s.getScooterStatus() == ScooterStatus.AVAILABLE)
                .collect(Collectors.groupingBy(
                        scooter -> scooter.getScooterModel().getName(), // ключ
                        Collectors.counting() // значение
                ));

        RentalPointDataDto rentalPointDataDto = RentalPointDataDto.builder()
                .rentalPointId(rentalPointId)
                .rentalPointName(rentalPoint.getName())
                .totalScooters(scooters.size())
                .availableScooters(availableCount)
                .rentedScooters(rentedCount)
                .availableModelsSummary(availableModels)
                .build();

        log.info("Успешно сгенерирована сводка для точки ID={}", rentalPointId);

        return rentalPointDataDto;
    }
}
