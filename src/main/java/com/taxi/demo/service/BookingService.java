package com.taxi.demo.service;

import com.taxi.demo.entity.Booking;
import com.taxi.demo.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Booking createBooking(Long passengerId, String pickupLocation, String dropoffLocation) {
        Booking booking = new Booking();
        booking.setPassengerId(passengerId);
        booking.setPickupLocation(pickupLocation);
        booking.setDropoffLocation(dropoffLocation);
        booking.setStatus("PENDING");
        booking.setCreatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public List<Booking> getPendingBookingsByPassenger(Long passengerId) {
        return bookingRepository.findByPassengerIdAndStatus(passengerId, "PENDING");
    }

    public Booking acceptBooking(@NonNull Long bookingId, Long driverId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy booking với id = " + bookingId));

        booking.setDriverId(driverId);
        booking.setStatus("ACCEPTED");
        return bookingRepository.save(booking);
    }

    public long countBookingsByPassenger(Long passengerId) {
        return bookingRepository.countByPassengerId(passengerId);
    }
}