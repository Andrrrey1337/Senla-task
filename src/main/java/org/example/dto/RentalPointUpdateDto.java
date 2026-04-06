package org.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RentalPointUpdateDto {
    private String name;
    private Long parentId;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
