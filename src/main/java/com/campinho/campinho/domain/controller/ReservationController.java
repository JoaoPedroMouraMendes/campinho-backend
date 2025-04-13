package com.campinho.campinho.domain.controller;

import com.campinho.campinho.domain.entity.Reservation;
import com.campinho.campinho.domain.request.CreateReservationRequest;
import com.campinho.campinho.domain.request.UpdateReservationRequest;
import com.campinho.campinho.domain.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.type.NullType;
import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/api")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/reservation")
    public ResponseEntity<String> createReservation(@RequestBody @Valid CreateReservationRequest data) {
        var reservationId = reservationService.createReservation(data);
        var uri = URI.create("/api/reservation/" + reservationId);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable("reservationId") String reservationId) {
        var reservation = reservationService.getReservationById(reservationId);

        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/reservations/active")
    public ResponseEntity<List<Reservation>> getActiveReservations() {
        var activeReservations = reservationService.getActiveReservations();

        return ResponseEntity.ok(activeReservations);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        var allReservations = reservationService.getAllReservations();

        return ResponseEntity.ok(allReservations);
    }

    @PutMapping("/reservation/{reservationId}")
    public ResponseEntity<NullType> updateReservationById(@PathVariable("reservationId") String reservationId, @RequestBody @Valid UpdateReservationRequest newData) {
        reservationService.updateReservationById(reservationId, newData);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reservation/{reservationId}")
    public ResponseEntity<NullType> deleteReservationById(@PathVariable("reservationId") String reservationId) {
        reservationService.deleteReservationById(reservationId);

        return ResponseEntity.ok().build();
    }
}
