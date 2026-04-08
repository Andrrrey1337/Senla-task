package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.scooterModel.ScooterModelCreateDto;
import org.example.dto.scooterModel.ScooterModelResponseDto;
import org.example.dto.scooterModel.ScooterModelUpdateDto;
import org.example.entity.ScooterModel;
import org.example.mapper.ScooterModelMapper;
import org.example.service.ScooterModelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scooter-models")
@RequiredArgsConstructor
public class ScooterModelController {
    private final ScooterModelService scooterModelService;
    private final ScooterModelMapper scooterModelMapper;

    @PostMapping
    public ResponseEntity<ScooterModelResponseDto> createScooterModel(@RequestBody ScooterModelCreateDto scooterModelCreateDto) {
        ScooterModel scooterModel = scooterModelService.createScooterModel(scooterModelMapper.toEntity(scooterModelCreateDto));
        return new ResponseEntity<>(scooterModelMapper.toDto(scooterModel), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScooterModelResponseDto> getScooterModelById(@PathVariable Long id) {
        ScooterModel scooterModel = scooterModelService.findScooterModelById(id);
        return ResponseEntity.ok(scooterModelMapper.toDto(scooterModel));
    }

    @GetMapping
    public ResponseEntity<List<ScooterModelResponseDto>> getAllScooterModels() {
        List<ScooterModel> scooterModels = scooterModelService.findAllScooterModel();
        return ResponseEntity.ok(scooterModelMapper.toDtos(scooterModels));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ScooterModelResponseDto> updateScooterModel(@PathVariable Long id, @RequestBody ScooterModelUpdateDto scooterModelUpdateDto) {
        ScooterModel scooterModel = scooterModelService.updateScooterModel(id, scooterModelUpdateDto);
        return ResponseEntity.ok(scooterModelMapper.toDto(scooterModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScooterModel(@PathVariable Long id) {
        scooterModelService.deleteScooterModelById(id);
        return ResponseEntity.noContent().build();
    }
}
