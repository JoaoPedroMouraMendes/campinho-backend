package com.campinho.campinho.domain.repository;

import com.campinho.campinho.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> { }