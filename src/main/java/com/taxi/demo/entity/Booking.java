package com.taxi.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// Booking có khóa chính id, map tới bảng bookings đã tạo từ buổi 5
@Entity
@Table(name = "bookings")
public class Booking {
    // Constructor không tham số (bắt buộc cho JPA)
    public Booking() {
    }

    // Constructor có tham số (tiện lợi)
    public Booking(Long passengerId, String pickupLocation, String dropoffLocation,
            BigDecimal totalPrice, Double distanceKm, String status) {
        this.passengerId = passengerId;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.totalPrice = totalPrice;
        this.distanceKm = distanceKm;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Khóa ngoại - dùng Long thay vì User entity để đơn giản
    @Column(name = "passenger_id")
    private Long passengerId;

    @Column(name = "driver_id")
    private Long driverId;

    @Column(name = "pickup_location", nullable = false)
    private String pickupLocation;

    @Column(name = "dropoff_location", nullable = false)
    private String dropoffLocation;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "distance_km")
    private Double distanceKm;

    @Column(name = "status", nullable = false)
    private String status; // PENDING, ACCEPTED, COMPLETED, CANCELED

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    // Getter và Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(String dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}