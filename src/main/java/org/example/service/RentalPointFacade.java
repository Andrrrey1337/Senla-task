package org.example.service;

import org.example.dto.point.RentalPointDataDto;

public interface RentalPointFacade {
    RentalPointDataDto getRentalPointDataById(Long id);
}
