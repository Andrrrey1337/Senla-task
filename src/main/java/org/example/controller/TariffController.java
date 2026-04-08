package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.tariff.TariffCreateDto;
import org.example.dto.tariff.TariffResponseDto;
import org.example.dto.tariff.TariffUpdateDto;
import org.example.entity.Tariff;
import org.example.mapper.TariffMapper;
import org.example.service.TariffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tariffs")
@RequiredArgsConstructor
public class TariffController {
    private final TariffService tariffService;
    private final TariffMapper tariffMapper;

    @PostMapping
    public ResponseEntity<TariffResponseDto> createTariff(@RequestBody TariffCreateDto tariffCreateDto) {
        Tariff tariffEntity = tariffService.createTariff(tariffMapper.toEntity(tariffCreateDto));
        return new ResponseEntity<>(tariffMapper.toDto(tariffEntity), HttpStatus.CREATED); // 201 статус
    }

    @GetMapping("/{id}")
    public ResponseEntity<TariffResponseDto> getTariffById(@PathVariable Long id) {
        Tariff tariff = tariffService.findTariffById(id);
        return ResponseEntity.ok(tariffMapper.toDto(tariff));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<TariffResponseDto> getTariffByName(@PathVariable String name) {
        Tariff tariff = tariffService.findTariffByName(name);
        return ResponseEntity.ok(tariffMapper.toDto(tariff));
    }

    @GetMapping
    public ResponseEntity<List<TariffResponseDto>> getAllTariffs() {
        List<Tariff> tariffs = tariffService.findAllTariffs();
        return ResponseEntity.ok(tariffMapper.toDtos(tariffs));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TariffResponseDto> updateTariff(@PathVariable Long id, @RequestBody TariffUpdateDto tariffUpdateDto) {
        Tariff tariff = tariffService.updateTariff(id, tariffUpdateDto);
        return ResponseEntity.ok(tariffMapper.toDto(tariff));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTariff(@PathVariable Long id) {
        tariffService.deleteTariffById(id);
        return ResponseEntity.noContent().build(); // 204 статус
    }
}
