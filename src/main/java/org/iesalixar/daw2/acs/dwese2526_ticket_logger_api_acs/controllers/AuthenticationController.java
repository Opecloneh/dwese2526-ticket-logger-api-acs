package org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.AuthRequestDTO;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.AuthResponseDTO;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDTO> authenticate (@Valid @RequestBody AuthRequestDTO authRequest) {
        // 1) Autenticacion (si fallla, Spring lanza AuthenticationException y lo gestiona el ApiExceptionHandler)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );
        // 2) Username autenticado (normalmente el mismo que el enviado en el login)
        String username = authentication.getName();
        // 3) Roles/authorities del usuario autenticado
        List<String> roles = authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList());
        // 4) Generacion del JWT con subject + roles (claims)
        String token = jwtUtil.generateToken(username, roles);
        // 5) Respuesta OK con token
        return ResponseEntity.ok(new AuthResponseDTO(token, "Authentication successful"));
    }
}
