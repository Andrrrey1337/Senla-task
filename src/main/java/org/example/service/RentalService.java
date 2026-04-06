package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.FinishRentalDto;
import org.example.dto.StartRentalDto;
import org.example.entity.*;

import org.example.exception.BusinessException;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.RentalRepository;
import org.example.repository.ScooterRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RentalService {
    private final UserService userService;
    private final ScooterService scooterService;
    private final TariffService tariffService;
    private final RentalRepository rentalRepository;
    private final ScooterRepository scooterRepository;
    private final UserRepository userRepository;

    public Rental startRental(StartRentalDto rentalDto) {
        User user = userService.findById(rentalDto.getUserId());

        if (rentalRepository.findActiveRentalByUserId(user.getId()).isPresent()) {
            throw new BusinessException("У пользователя уже есть активная поездка");
        }

        if (user.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Недостаточно средств на балансе для начала поездки");
        }

        Scooter scooter = scooterService.findScooterById(rentalDto.getScooterId());

        if (scooter.getScooterStatus() == ScooterStatus.RENTED) {
            throw new BusinessException("Самокат уже занят");
        }

        Tariff tariff = tariffService.findTariffById(rentalDto.getTariffId());

        scooter.setScooterStatus(ScooterStatus.RENTED);
        scooterRepository.update(scooter);

        Rental rental = Rental.builder()
                .user(user)
                .scooter(scooter)
                .tariff(tariff)
                .startTime(LocalDateTime.now())
                .startLatitude(scooter.getLatitude())
                .startLongitude(scooter.getLongitude())
                .isActive(true)
                .build();

        rental = rentalRepository.create(rental);
        log.info("Успешно начата поездка ID={} для пользователя {}", rental.getId(), user.getUsername());

        return rental;
    }

    public Rental finishRental(Long id, FinishRentalDto finishRentalDto) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Поездка с ID " + id + " не найдена"));

        if (!rental.getIsActive()) {
            throw new BusinessException("Эта поездка уже была завершена ранее");
        }

        rental.setEndLatitude(finishRentalDto.getEndLatitude());
        rental.setEndLongitude(finishRentalDto.getEndLongitude());
        rental.setEndTime(LocalDateTime.now());
        rental.setIsActive(false);

        BigDecimal durationMinutes = BigDecimal.valueOf(Duration.between(rental.getStartTime(), rental.getEndTime()).toMinutes());
        if (durationMinutes.compareTo(BigDecimal.ZERO) <= 0) {
            durationMinutes = BigDecimal.ONE;
        }

        BigDecimal pricePerMinute = rental.getScooter().getScooterModel().getPricePerMinute();
        BigDecimal tariffPrice = rental.getTariff().getPrice();

        BigDecimal totalPrice = pricePerMinute.multiply(durationMinutes).add(tariffPrice);

        rental.setPrice(totalPrice);
        rental =  rentalRepository.update(rental);

        User user = rental.getUser();
        user.setBalance(user.getBalance().subtract(totalPrice));
        user = userRepository.update(user);

        Scooter scooter = rental.getScooter();
        scooter.setScooterStatus(ScooterStatus.AVAILABLE);
        scooter.setLongitude(finishRentalDto.getEndLongitude());
        scooter.setLatitude(finishRentalDto.getEndLatitude());
        scooterRepository.update(scooter);

        log.info("Поездка ID={} успешно завершена. Списано: {}. Длительность: {} мин.",
                rental.getId(), totalPrice, durationMinutes);

        return rental;
    }

    public List<Rental> findRentalsByUserId(Long userId) {
        userService.findById(userId);
        List<Rental> rentals = rentalRepository.findAllByUserId(userId);

        log.info("Получена история поездок для пользователя с ID={}. Количество записей: {}", userId, rentals.size());

        return rentals;
    }

    public List<Rental> findRentalsByScooterId(Long scooterId) {
        scooterRepository.findById(scooterId);
        List<Rental> rentals = rentalRepository.findByScooterId(scooterId);

        log.info("Получена история аренды для самоката с ID={}. Количество записей: {}", scooterId, rentals.size());

        return rentals;
    }
}
