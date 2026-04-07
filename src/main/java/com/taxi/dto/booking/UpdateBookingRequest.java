package com.taxi.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBookingRequest {
    private String pickupLocation;
    private String dropoffLocation;
    private Double distanceKm;
    private BigDecimal totalPrice;
    private String status;
    private Long driverId;
}
