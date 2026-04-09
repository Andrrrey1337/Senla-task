package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.rental.FinishRentalDto;
import org.example.dto.rental.RentalAdminResponseDto;
import org.example.dto.rental.RentalResponseDto;
import org.example.dto.rental.StartRentalDto;
import org.example.entity.Rental;
import org.example.mapper.RentalMapper;
import org.example.service.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;
    private final RentalMapper rentalMapper;

    @PostMapping("/start")
    public ResponseEntity<RentalResponseDto> startRental(@RequestBody StartRentalDto startRentalDto) {
        Rental rental = rentalService.startRental(startRentalDto);
        return new ResponseEntity<>(rentalMapper.toDto(rental), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/finish")
    public ResponseEntity<RentalResponseDto> finishRental(@PathVariable Long id, @RequestBody FinishRentalDto finishRentalDto) {
        Rental rental = rentalService.finishRental(id, finishRentalDto);
        return ResponseEntity.ok(rentalMapper.toDto(rental));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RentalResponseDto>> getUserRentals(@PathVariable Long userId) {
        List<Rental> rentals = rentalService.findRentalsByUserId(userId);
        return ResponseEntity.ok(rentalMapper.toDtos(rentals));
    }

    @GetMapping("/scooter/{scooterId}")
    public ResponseEntity<List<RentalAdminResponseDto>> getScooterRentals(@PathVariable Long scooterId) {
        List<Rental> rentals = rentalService.findRentalsByScooterId(scooterId);
        return ResponseEntity.ok(rentalMapper.toAdminDtos(rentals));
    }
}
