package com.taxi.service;

import com.taxi.dto.BookingHistoryItemDTO;
import com.taxi.entity.Booking;
import com.taxi.mapper.BookingMapper;
import com.taxi.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingMapper bookingMapper;

    public List<BookingHistoryItemDTO> getHistoryForPassenger(Long passengerId) {
        // 1. Lấy danh sách Booking entity từ DB
        List<Booking> bookings = bookingRepository.findByPassengerId(passengerId);

        // 2. Map sang DTO list sử dụng Java Stream và Mapper
        return bookings.stream()
                .map(bookingMapper::toHistoryItemDTO) // Gọi method map cho từng phần tử
                .collect(Collectors.toList());
    }
}
