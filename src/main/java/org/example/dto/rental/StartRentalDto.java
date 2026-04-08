package org.example.dto.rental;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartRentalDto {
    private Long userId;
    private Long scooterId;
    private Long tariffId;
    private String promoCode;
}
