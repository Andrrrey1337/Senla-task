package org.example.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class RentalPointDataDto {
    private Long rentalPointId;
    private String rentalPointName;
    private long totalScooters;
    private long availableScooters;
    private long rentedScooters;
    private Map<String, Long> availableModelsSummary; // модели - кол-во
}
