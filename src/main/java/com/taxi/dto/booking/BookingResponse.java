package com.taxi.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    private Long id;
    private Long passengerId;
    private Long driverId;
    private String pickupLocation;
    private String dropoffLocation;
    private BigDecimal totalPrice;
    private Double distanceKm;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}
