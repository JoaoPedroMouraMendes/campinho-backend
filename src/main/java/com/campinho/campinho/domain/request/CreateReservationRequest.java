package com.campinho.campinho.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateReservationRequest(
        @NotBlank(message = "reservedBy não pode estar em branco")
        String reservedBy,

        @NotNull(message = "startTime não pode ser nulo")
        LocalDateTime startTime,

        @NotNull(message = "endTime não pode ser nulo")
        LocalDateTime endTime
) { }
