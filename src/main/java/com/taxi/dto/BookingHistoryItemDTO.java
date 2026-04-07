package com.taxi.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class BookingHistoryItemDTO {
    private Long id;
    private String pickupLocation;
    private String dropoffLocation;
    private String status;
    private Double distanceKm;
    private BigDecimal totalFare;
}
