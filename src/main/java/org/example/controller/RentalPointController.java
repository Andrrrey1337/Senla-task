package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.point.RentalPointCreateDto;
import org.example.dto.point.RentalPointDataDto;
import org.example.dto.point.RentalPointResponseDto;
import org.example.dto.point.RentalPointUpdateDto;
import org.example.dto.scooter.ScooterAdminResponseDto;
import org.example.entity.RentalPoint;
import org.example.entity.Scooter;
import org.example.mapper.RentalPointMapper;
import org.example.mapper.ScooterMapper;
import org.example.service.RentalPointService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class RentalPointController {
    private final RentalPointService rentalPointService;
    private final RentalPointMapper rentalPointMapper;
    private final ScooterMapper scooterMapper;

    @PostMapping
    public ResponseEntity<RentalPointResponseDto> createRentalPoint(@RequestBody RentalPointCreateDto rentalPointCreateDto) {
        RentalPoint rentalPoint = rentalPointService.createRentalPoint(rentalPointCreateDto);
        return new ResponseEntity<>(rentalPointMapper.toDto(rentalPoint), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalPointResponseDto> getRentalPointById(@PathVariable Long id) {
        RentalPoint rentalPoint = rentalPointService.findRentalPointById(id);
        return ResponseEntity.ok(rentalPointMapper.toDto(rentalPoint));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<RentalPointResponseDto> getRentalPointByName(@PathVariable String name) {
        RentalPoint rentalPoint = rentalPointService.findRentalPointByName(name);
        return ResponseEntity.ok(rentalPointMapper.toDto(rentalPoint));
    }

    @GetMapping
    public ResponseEntity<List<RentalPointResponseDto>> getAllRentalPoints() {
        List<RentalPoint> rentalPoints = rentalPointService.findAllRentalPoints();
        return ResponseEntity.ok(rentalPointMapper.toDtos(rentalPoints));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RentalPointResponseDto> updateRentalPoint(@PathVariable Long id, @RequestBody RentalPointUpdateDto rentalPointUpdateDto) {
        RentalPoint rentalPoint = rentalPointService.updateRentalPoint(id, rentalPointUpdateDto);
        return ResponseEntity.ok(rentalPointMapper.toDto(rentalPoint));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRentalPoint(@PathVariable Long id) {
        rentalPointService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/scooters/{id}")
    public ResponseEntity<List<ScooterAdminResponseDto>> getScootersAtPointById(@PathVariable Long id) {
        List<Scooter> scooters = rentalPointService.findAllScootersAtRentalPoint(id);
        return ResponseEntity.ok(scooterMapper.toAdminDtos(scooters));
    }

    @GetMapping("/data/{id}")
    public ResponseEntity<RentalPointDataDto> getRentalPointDataById(@PathVariable Long id) { // только для админов
        return ResponseEntity.ok(rentalPointService.getRentalPointDataById(id));
    }
}
