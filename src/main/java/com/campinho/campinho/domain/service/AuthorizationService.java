package com.campinho.campinho.domain.service;

import com.campinho.campinho.domain.entity.User;
import com.campinho.campinho.domain.enums.UserRole;
import com.campinho.campinho.domain.repository.UserRepository;
import com.campinho.campinho.domain.request.RegisterUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    // Cria um usuário comum
    public User createUser(RegisterUserRequest data) {
        if (userRepository.findByEmail(data.email()) != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este e-mail já está cadastrado em nosso sistema");

        // Criptografa a senha
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        var newUser = new User(data.name(), data.email(), encryptedPassword, UserRole.USER);
        return userRepository.save(newUser);
    }

    // Cria um usuário administrador
    public User createUserAdmin(RegisterUserRequest data) {
        if (userRepository.findByEmail(data.email()) != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este e-mail já está cadastrado em nosso sistema");

        // Criptografa a senha
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        var newUser = new User(data.name(), data.email(), encryptedPassword, UserRole.ADMIN);
        return userRepository.save(newUser);
    }
}
