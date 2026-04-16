package org.example.service;

import org.example.dto.scooterModel.ScooterModelUpdateDto;
import org.example.entity.ScooterModel;
import org.example.exception.BusinessException;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.ScooterModelMapper;
import org.example.repository.ScooterModelRepository;
import org.example.service.ScooterModelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScooterModelServiceTest {

    @Mock private ScooterModelRepository scooterModelRepository;
    @Mock private ScooterModelMapper scooterModelMapper;

    @InjectMocks
    private ScooterModelService scooterModelService;

    private ScooterModel scooterModel;
    private Long id = 1L;
    private String name = "Model X";

    @BeforeEach
    void setUp() {
        scooterModel = new ScooterModel();
        scooterModel.setId(id);
        scooterModel.setName(name);
    }

    @Test
    @DisplayName("createScooterModel - Успех")
    void createScooterModel_Success() {
        when(scooterModelRepository.findByName(name)).thenReturn(Optional.empty());
        when(scooterModelRepository.create(any(ScooterModel.class))).thenReturn(scooterModel);

        ScooterModel result = scooterModelService.createScooterModel(scooterModel);

        assertNotNull(result);
        assertEquals(name, result.getName());
    }

    @Test
    @DisplayName("createScooterModel - Уже существует")
    void createScooterModel_AlreadyExists_ThrowsBusinessException() {
        when(scooterModelRepository.findByName(name)).thenReturn(Optional.of(scooterModel));
        assertThrows(BusinessException.class, () -> scooterModelService.createScooterModel(scooterModel));
    }

    @Test
    @DisplayName("findScooterModelById - Успех")
    void findScooterModelById_Success() {
        when(scooterModelRepository.findById(id)).thenReturn(Optional.of(scooterModel));
        ScooterModel result = scooterModelService.findScooterModelById(id);
        assertEquals(id, result.getId());
    }

    @Test
    @DisplayName("findScooterModelById - Не найдена")
    void findScooterModelById_NotFound_ThrowsResourceNotFoundException() {
        when(scooterModelRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> scooterModelService.findScooterModelById(id));
    }

    @Test
    @DisplayName("findAllScooterModel - Успех")
    void findAllScooterModel_Success() {
        when(scooterModelRepository.findAll()).thenReturn(Collections.singletonList(scooterModel));
        List<ScooterModel> result = scooterModelService.findAllScooterModel();
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("updateScooterModel - Успех")
    void updateScooterModel_Success() {
        ScooterModelUpdateDto updateDto = new ScooterModelUpdateDto();
        when(scooterModelRepository.findById(id)).thenReturn(Optional.of(scooterModel));

        scooterModelService.updateScooterModel(id, updateDto);

        verify(scooterModelMapper).updateEntity(updateDto, scooterModel);
    }

    @Test
    @DisplayName("deleteScooterModelById - Успех")
    void deleteScooterModelById_Success() {
        when(scooterModelRepository.findById(id)).thenReturn(Optional.of(scooterModel));
        scooterModelService.deleteScooterModelById(id);
        verify(scooterModelRepository).deleteById(id);
    }
}
