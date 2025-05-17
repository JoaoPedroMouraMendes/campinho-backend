package com.campinho.campinho.domain.request;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(
    @NotBlank(message = "email não pode estar em branco")
    String email,
    @NotBlank(message = "password não pode estar em branco")
    String password
) { }
