package com.campinho.campinho.domain.entity;

import com.campinho.campinho.domain.request.CreateReservationRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank(message = "reservedBy não pode estar em branco")
    @Size(max = 30, message = "reservedBy pode ter no máximo 30 caracteres")
    private String reservedBy;

    @NotNull(message = "startTime não pode ser nulo")
    private LocalDateTime startTime;

    @NotNull(message = "endTime não pode ser nulo")
    private LocalDateTime endTime;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // Construtor que converte de CreateReservationRequest para Reservation
    public Reservation(CreateReservationRequest data) {
        this.reservedBy = data.reservedBy();
        this.startTime = data.startTime();
        this.endTime = data.endTime();
    }
}
