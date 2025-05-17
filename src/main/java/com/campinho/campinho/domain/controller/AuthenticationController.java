package com.campinho.campinho.domain.controller;

import com.campinho.campinho.domain.entity.User;
import com.campinho.campinho.domain.request.AuthenticationRequest;
import com.campinho.campinho.domain.request.RegisterUserRequest;
import com.campinho.campinho.domain.response.LoginResponse;
import com.campinho.campinho.domain.service.AuthorizationService;
import com.campinho.campinho.domain.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.net.URI;

@Controller
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid AuthenticationRequest data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        // Gera token de acesso
        String token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegisterUserRequest data) {
        User userRegistred = authorizationService.createUser(data);

        var uri = URI.create("/api/auth/register" + userRegistred.getId());
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/register-admin")
    public ResponseEntity<String> registerUserAdmin(@RequestBody @Valid RegisterUserRequest data) {
        User userRegistred = authorizationService.createUserAdmin(data);

        var uri = URI.create("/api/auth/register" + userRegistred.getId());
        return ResponseEntity.created(uri).build();
    }
}
