package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.point.RentalPointCreateDto;
import org.example.dto.point.RentalPointDataDto;
import org.example.dto.point.RentalPointResponseDto;
import org.example.dto.point.RentalPointUpdateDto;
import org.example.dto.scooter.ScooterAdminResponseDto;
import org.example.entity.RentalPoint;
import org.example.entity.Scooter;
import org.example.entity.ScooterStatus;
import org.example.exception.BusinessException;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.RentalPointMapper;
import org.example.repository.RentalPointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public interface RentalPointService {
    RentalPointResponseDto createRentalPoint(RentalPointCreateDto dto);

    List<RentalPointResponseDto> createRentalPointsBatch(List<RentalPointCreateDto> dtos);

    RentalPointResponseDto updateRentalPoint(Long id, RentalPointUpdateDto dto);

    // определяет вес адреса
    int getAddressLevel(RentalPoint point);

    RentalPoint findRentalPointById(Long id);

    RentalPoint findRentalPointByName(String name);

    List<RentalPointResponseDto> findAllRentalPoints();

    Optional<RentalPoint> findNearestValidParkingPoint(BigDecimal latitude, BigDecimal longitude, double radius);

    void deleteById(Long id);

    RentalPointResponseDto getRentalPointDtoById(Long id);

    RentalPointResponseDto getRentalPointDtoByName(String name);

}
