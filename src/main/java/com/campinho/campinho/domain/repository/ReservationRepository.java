package com.campinho.campinho.domain.repository;

import com.campinho.campinho.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    @Query("SELECT r FROM Reservation r WHERE r.endTime >= :now ORDER BY r.startTime ASC")
    List<Reservation> findReservationsNotExpired(@Param("now") LocalDateTime now);
}