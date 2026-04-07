package com.taxi.controller;

import com.taxi.dto.BookingHistoryItemDTO;
import com.taxi.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/history/{passengerId}")
    public ResponseEntity<List<BookingHistoryItemDTO>> getPassengerHistory(
            @PathVariable Long passengerId) {

        List<BookingHistoryItemDTO> history = bookingService.getHistoryForPassenger(passengerId);

        // Trả về HTTP 200 OK cùng với body là danh sách DTO
        return ResponseEntity.ok(history);
    }
}
