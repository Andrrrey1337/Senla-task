package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.scooterModel.ScooterModelUpdateDto;
import org.example.entity.ScooterModel;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.ScooterModelMapper;
import org.example.repository.ScooterModelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ScooterModelService {
    private final ScooterModelRepository scooterModelRepository;
    private final ScooterModelMapper scooterModelMapper;

    public ScooterModel createScooterModel(ScooterModel scooterModel) {
        ScooterModel scooterModelNew = scooterModelRepository.create(scooterModel);

        log.info("Успешно добавлена новая модель самоката: ID={}, название='{}'",
                scooterModelNew.getId(), scooterModelNew.getName());

        return scooterModelNew;
    }

    @Transactional(readOnly = true)
    public ScooterModel findScooterModelById(Long id) {
        ScooterModel model = scooterModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Модель самоката с ID " + id + " не найдена"));

        log.info("Успешно найдена модель самоката с ID: {}", id);

        return model;
    }

    @Transactional(readOnly = true)
    public List<ScooterModel> findAllScooterModel() {
        List<ScooterModel>  scooterModels = scooterModelRepository.findAll();

        log.info("Получен список всех моделей самокатов. Количество записей: {}", scooterModels.size());

        return scooterModels;
    }

    public ScooterModel updateScooterModel(Long id, ScooterModelUpdateDto scooterModelUpdateDto) {
        ScooterModel scooterModel = findScooterModelById(id);
        scooterModelMapper.updateEntity(scooterModelUpdateDto, scooterModel);

        log.info("Данные модели самоката с ID {} успешно обновлены", scooterModel.getId());

        return scooterModel;
    }

    public void deleteScooterModelById(Long id) {
        findScooterModelById(id);
        scooterModelRepository.deleteById(id);
        log.info("Модель самоката с ID {} успешно удалена", id);
    }
}
