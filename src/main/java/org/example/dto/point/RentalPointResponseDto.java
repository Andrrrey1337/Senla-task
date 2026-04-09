package org.example.dto.point;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalPointResponseDto {
    private Long id;
    private String name;
    private Long parentId;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
