package com.campinho.campinho.domain.request;

import java.time.LocalDateTime;

public record UpdateReservationRequest(
        String reservedBy,
        LocalDateTime startTime,
        LocalDateTime endTime
) {}
