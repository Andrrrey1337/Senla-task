package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.scooterModel.ScooterModelCreateDto;
import org.example.dto.scooterModel.ScooterModelResponseDto;
import org.example.dto.scooterModel.ScooterModelUpdateDto;
import org.example.entity.ScooterModel;
import org.example.exception.BusinessException;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.ScooterModelMapper;
import org.example.repository.ScooterModelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface ScooterModelService {
    ScooterModelResponseDto createScooterModel(ScooterModelCreateDto dto);

    ScooterModel findScooterModelById(Long id);

    ScooterModelResponseDto getScooterModelDtoById(Long id);

    List<ScooterModelResponseDto> findAllScooterModels();

    ScooterModelResponseDto updateScooterModel(Long id, ScooterModelUpdateDto scooterModelUpdateDto);

    void deleteScooterModelById(Long id);

}
