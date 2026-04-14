package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.promocode.PromoCodeCreateDto;
import org.example.dto.promocode.PromoCodeResponseDto;
import org.example.dto.promocode.PromoCodeUpdateDto;
import org.example.entity.PromoCode;
import org.example.mapper.PromoCodeMapper;
import org.example.service.PromoCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promocodes")
@RequiredArgsConstructor
@Tag(name = "Админ: Промокоды", description = "Управление системой скидок")
@PreAuthorize("hasRole('ADMIN')")
public class PromoCodeController {
    private final PromoCodeService promoCodeService;
    private final PromoCodeMapper promoCodeMapper;

    @PostMapping()
    @Operation(summary = "Создать новый промокод")
    public ResponseEntity<PromoCodeResponseDto> create(@Valid @RequestBody PromoCodeCreateDto promoCodeCreateDto) {
        PromoCode promoCode = promoCodeService.createPromoCode(promoCodeMapper.toEntity(promoCodeCreateDto));
        return new ResponseEntity<>(promoCodeMapper.toDto(promoCode), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Получить все промокоды")
    public List<PromoCodeResponseDto> getAll() {
        return promoCodeMapper.toDtos(promoCodeService.findAllPromoCodes());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить промокод по его id")
    public PromoCodeResponseDto getPromoCodeById(@PathVariable Long id) {
        return promoCodeMapper.toDto(promoCodeService.findPromoCodeById(id));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить промокод")
    public ResponseEntity<PromoCodeResponseDto> update(@PathVariable Long id, @Valid @RequestBody PromoCodeUpdateDto dto) {
        PromoCode entity = promoCodeService.updatePromoCode(id, dto);
        return ResponseEntity.ok(promoCodeMapper.toDto(entity));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить промокод")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        promoCodeService.deletePromoCode(id);
        return ResponseEntity.noContent().build();
    }
}
