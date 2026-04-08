package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.scooter.ScooterCreateDto;
import org.example.dto.scooter.ScooterResponseDto;
import org.example.dto.scooter.ScooterUpdateDto;
import org.example.entity.Scooter;
import org.example.mapper.ScooterMapper;
import org.example.service.ScooterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scooters")
@RequiredArgsConstructor
public class ScooterController {
    private final ScooterService scooterService;
    private final ScooterMapper scooterMapper;

    @PostMapping
    public ResponseEntity<ScooterResponseDto> createScooter(@RequestBody ScooterCreateDto scooterCreateDto) {
        Scooter scooter = scooterService.createScooter(scooterMapper.toEntity(scooterCreateDto));
        return new  ResponseEntity<>(scooterMapper.toDto(scooter), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScooterResponseDto> getScooterById(@PathVariable Long id) {
        Scooter scooter= scooterService.findScooterById(id);
        return ResponseEntity.ok(scooterMapper.toDto(scooter));
    }

    @GetMapping("/number/{number}")
    public ResponseEntity<ScooterResponseDto> getScooterByNumber(@PathVariable String number) {
        Scooter scooter = scooterService.findScooterBySerialNumber(number);
        return ResponseEntity.ok(scooterMapper.toDto(scooter));
    }

    @GetMapping("/available")
    public ResponseEntity<List<ScooterResponseDto>> getAvailableScooters(
            @RequestParam("pointId") Long id,
            @RequestParam(value = "minBattery", defaultValue = "20") Integer minBatteryLevel) {
        List<Scooter> scooters = scooterService.findAvailableScooters(id, minBatteryLevel);
        return ResponseEntity.ok(scooterMapper.toDtos(scooters));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScooter(@PathVariable Long id) {
        scooterService.deleteScooterById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ScooterResponseDto> updateScooter(@PathVariable Long id, @RequestBody ScooterUpdateDto scooterUpdateDto) {
        Scooter scooter = scooterService.updateScooter(id,  scooterUpdateDto);
        return ResponseEntity.ok(scooterMapper.toDto(scooter));
    }
}
