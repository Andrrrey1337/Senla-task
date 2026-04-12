package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.example.dto.tariff.TariffCreateDto;
import org.example.dto.tariff.TariffResponseDto;
import org.example.dto.tariff.TariffUpdateDto;
import org.example.entity.Tariff;
import org.example.mapper.TariffMapper;
import org.example.service.TariffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tariffs")
@RequiredArgsConstructor
@Tag(name = "Тарифы", description = "Управление тарифами на аренду (стоимость старта)")
public class TariffController {
    private final TariffService tariffService;
    private final TariffMapper tariffMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Создать новый тариф")
    public ResponseEntity<TariffResponseDto> createTariff(@Valid @RequestBody TariffCreateDto tariffCreateDto) {
        Tariff tariffEntity = tariffService.createTariff(tariffMapper.toEntity(tariffCreateDto));
        return new ResponseEntity<>(tariffMapper.toDto(tariffEntity), HttpStatus.CREATED); // 201 статус
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить тариф по ID")
    public ResponseEntity<TariffResponseDto> getTariffById(@PathVariable Long id) {
        Tariff tariff = tariffService.findTariffById(id);
        return ResponseEntity.ok(tariffMapper.toDto(tariff));
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Найти тариф по названию")
    public ResponseEntity<TariffResponseDto> getTariffByName(@PathVariable String name) {
        Tariff tariff = tariffService.findTariffByName(name);
        return ResponseEntity.ok(tariffMapper.toDto(tariff));
    }

    @GetMapping
    @Operation(summary = "Получить список всех тарифов")
    public ResponseEntity<List<TariffResponseDto>> getAllTariffs() {
        List<Tariff> tariffs = tariffService.findAllTariffs();
        return ResponseEntity.ok(tariffMapper.toDtos(tariffs));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить параметры тарифа")
    public ResponseEntity<TariffResponseDto> updateTariff(@PathVariable Long id, @Valid @RequestBody TariffUpdateDto tariffUpdateDto) {
        Tariff tariff = tariffService.updateTariff(id, tariffUpdateDto);
        return ResponseEntity.ok(tariffMapper.toDto(tariff));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удалить тариф")
    public ResponseEntity<Void> deleteTariff(@PathVariable Long id) {
        tariffService.deleteTariffById(id);
        return ResponseEntity.noContent().build(); // 204 статус
    }
}
