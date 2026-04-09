package org.example.dto.point;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RentalPointCreateDto {
    private String name;
    private Long parentId;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
