package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.rental.FinishRentalDto;
import org.example.dto.rental.StartRentalDto;
import org.example.entity.*;

import org.example.exception.BusinessException;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    private final PromoCodeRepository promoCodeRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;

    private static final double EARTH_RADIUS_KM = 6371.0;

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

        PromoCode promoCode = null;
        if (rentalDto.getPromoCode() != null && !rentalDto.getPromoCode().isBlank()) {
            promoCode = promoCodeRepository.findByCode(rentalDto.getPromoCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Промокод '" + rentalDto.getPromoCode() + "' не найден"));

            if (!promoCode.getIsActive() || (promoCode.getEndDate() != null && promoCode.getEndDate().isAfter(LocalDateTime.now()))) {
                throw new BusinessException("Промокод недействителен или истек");
            }
        }

        scooter.setScooterStatus(ScooterStatus.RENTED);

        Rental rental = Rental.builder()
                .user(user)
                .scooter(scooter)
                .tariff(tariff)
                .promoCode(promoCode)
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

        // обновляем поездку
        rental.setEndLatitude(finishRentalDto.getEndLatitude());
        rental.setEndLongitude(finishRentalDto.getEndLongitude());
        rental.setEndTime(LocalDateTime.now());
        rental.setIsActive(false);


        //считаем данные о поездке
        BigDecimal durationMinutes = BigDecimal.valueOf(Duration.between(rental.getStartTime(), rental.getEndTime()).toMinutes());
        if (durationMinutes.compareTo(BigDecimal.ZERO) <= 0) {
            durationMinutes = BigDecimal.ONE;
        }
        BigDecimal pricePerMinute = rental.getScooter().getScooterModel().getPricePerMinute();
        BigDecimal tariffPrice = rental.getTariff().getPrice(); // стомость старта

        // Оработка подписок
        Optional<UserSubscription> userSubscriptionOptional = userSubscriptionRepository.findActiveByUserId(rental.getUser().getId());
        if (userSubscriptionOptional.isPresent()) {
            UserSubscription  userSubscription = userSubscriptionOptional.get();

            if (userSubscription.getSubscription().getIsFreeStart()) {
                tariffPrice = BigDecimal.ZERO; // бесплатный старт
            }

            int availableMinutes = userSubscription.getRemainingMinutes();
            if (availableMinutes > 0) {
                int tripMin = durationMinutes.intValue();

                if (availableMinutes >= tripMin) { // если минут в абонементе осталось больше чем была поездка
                    userSubscription.setRemainingMinutes(availableMinutes - tripMin);
                    durationMinutes = BigDecimal.ZERO;
                } else { // если оставшихся в абонементе минут не хватает на всю поездку
                    durationMinutes = BigDecimal.valueOf(tripMin - availableMinutes);
                    userSubscription.setRemainingMinutes(0);
                }
            }
        }

        BigDecimal totalPrice = pricePerMinute.multiply(durationMinutes).add(tariffPrice);

        // расчет промиков
        if (rental.getPromoCode() != null) {
            int discount = rental.getPromoCode().getDiscount();
            BigDecimal discountMultiplier = BigDecimal.valueOf(discount).divide(BigDecimal.valueOf(100));
            BigDecimal discountPrice = totalPrice.multiply(discountMultiplier);
            totalPrice = totalPrice.subtract(discountPrice);
        }

        rental.setPrice(totalPrice);

        // считаем расстояние
        BigDecimal mileage = getDistance(rental.getStartLatitude(), rental.getStartLongitude(), rental.getEndLatitude(), rental.getEndLongitude());
        rental.setDistance(mileage);

        // обновляем баланс
        User user = rental.getUser();
        user.setBalance(user.getBalance().subtract(totalPrice));

        // обновляем данные самоката
        Scooter scooter = rental.getScooter();
        scooter.setScooterStatus(ScooterStatus.AVAILABLE);
        scooter.setLongitude(finishRentalDto.getEndLongitude());
        scooter.setLatitude(finishRentalDto.getEndLatitude());

        if (scooter.getMileage() == null) {
            scooter.setMileage(BigDecimal.ZERO);
        }

        scooter.setMileage(scooter.getMileage().add(mileage));

        log.info("Поездка ID={} успешно завершена. Списано: {}. Длительность: {} мин. Дистанция: {} км",
                rental.getId(), totalPrice, durationMinutes, mileage);

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

    private BigDecimal getDistance(BigDecimal startLat, BigDecimal startLon, BigDecimal endLat, BigDecimal endLon) {
        if (startLat ==  null || startLon == null || endLat == null || endLon == null) {
            return BigDecimal.ZERO;
        }

        double lat1 = startLat.doubleValue();
        double lon1 = startLon.doubleValue();
        double lat2 = endLat.doubleValue();
        double lon2 = endLon.doubleValue();

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS_KM * c;

        // округляем до 2 знаков после запятой
        return BigDecimal.valueOf(distance).setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
