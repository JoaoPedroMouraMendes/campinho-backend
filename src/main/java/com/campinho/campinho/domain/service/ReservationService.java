package com.campinho.campinho.domain.service;

import com.campinho.campinho.domain.request.CreateReservationRequest;
import com.campinho.campinho.domain.entity.Reservation;
import com.campinho.campinho.domain.repository.ReservationRepository;
import com.campinho.campinho.domain.request.UpdateReservationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public UUID createReservation(CreateReservationRequest data) {

        // Validação do horário
        if(!isValidReservationTime(data.startTime(), data.endTime()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O horário de inicio deve ser antes do horário de termino");

        // Validação da disponibilidade
        if(findReservationInRange(data.startTime(), data.endTime(), reservationRepository.findReservationsNotExpired(LocalDateTime.now())) != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O horário desta reserva está indisponível");

        var newReservation = new Reservation(data);

        var reservationSaved = reservationRepository.save(newReservation);

        return reservationSaved.getId();
    }

    public Reservation getReservationById(String reservationId) {
        try {
            var optionalReservation = reservationRepository.findById(UUID.fromString(reservationId));

            if (optionalReservation.isEmpty())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva não encontrada");

            return optionalReservation.get();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id inválido");
        }
    }

    public List<Reservation> getActiveReservations() {
        return reservationRepository.findReservationsNotExpired(LocalDateTime.now());
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public void updateReservationById(String reservationId, UpdateReservationRequest newData) {
        var reservation = getReservationById(reservationId);

        // Validação de horário e de disponibilidade
        if (newData.startTime() != null || newData.endTime() != null) {
            LocalDateTime newStartTime = newData.startTime() != null ? newData.startTime() : reservation.getStartTime();
            LocalDateTime newEndTime = newData.endTime() != null ? newData.endTime() : reservation.getEndTime();

            if (!isValidReservationTime(newStartTime, newEndTime))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O horário de inicio deve ser antes do horário de termino");

            var activeReservation = findActiveReservationInRange(newStartTime, newEndTime);
            if (activeReservation != null && activeReservation.getId() != reservation.getId())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O horário desta reserva está indisponível");

            // Altera dados de horário
            reservation.setStartTime(newStartTime);
            reservation.setEndTime(newEndTime);
        }

        // Alteração do nome de quem reservou
        if (newData.reservedBy() != null) reservation.setReservedBy(newData.reservedBy());

        reservationRepository.save(reservation);
    }

    public void deleteReservationById(String reservationId) {
        var reservation = getReservationById(reservationId);

        reservationRepository.delete(reservation);
    }

    private Reservation findReservationInRange(LocalDateTime startTime, LocalDateTime endTime, List<Reservation> reservations) {
        for(Reservation reservation : reservations)
            if (reservation.getEndTime().isAfter(startTime) && reservation.getStartTime().isBefore(endTime))
                return reservation;

        return null;
    }

    private Reservation findActiveReservationInRange(LocalDateTime startTime, LocalDateTime endTime) {
        return findReservationInRange(startTime, endTime, reservationRepository.findReservationsNotExpired(LocalDateTime.now()));
    }

    private boolean isValidReservationTime(LocalDateTime startTime, LocalDateTime endTime) {
        return startTime.isBefore(endTime);
    }
}
