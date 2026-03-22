package com.taxi.demo.repository;

import com.taxi.demo.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByPassengerId(Long passengerId);

    List<Booking> findByStatus(String status);

    List<Booking> findByPassengerIdAndStatus(Long passengerId, String status);

    long countByPassengerId(Long passengerId);
}
