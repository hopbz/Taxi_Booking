package com.taxi.mapper;

import com.taxi.dto.BookingHistoryItemDTO;
import com.taxi.entity.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingHistoryItemDTO toHistoryItemDTO(Booking booking) {
        return BookingHistoryItemDTO.builder()
                .id(booking.getId())
                .pickupLocation(booking.getPickupLocation())
                .dropoffLocation(booking.getDropoffLocation())
                .status(booking.getStatus() != null ? booking.getStatus().toString() : null)
                .distanceKm(booking.getDistanceKm())
                .totalFare(booking.getTotalPrice())
                .build();
    }
}
