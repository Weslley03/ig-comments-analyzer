package com.ig.comments.web.scraping.controller;

import com.ig.comments.web.scraping.dto.AuthResponseDTO;
import com.ig.comments.web.scraping.dto.LoginRequestDTO;
import com.ig.comments.web.scraping.dto.RegisterRequestDTO;
import com.ig.comments.web.scraping.dto.ResponseApiDTO;
import com.ig.comments.web.scraping.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseApiDTO<AuthResponseDTO>> register(@Valid @RequestBody RegisterRequestDTO dto) {
        AuthResponseDTO authResponseDTO = authService.register(dto);
        return ResponseEntity.ok(new ResponseApiDTO<AuthResponseDTO>(
            HttpStatus.CREATED.value(),
            "cadastro do usuário efetuado com sucesso!",
                authResponseDTO
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseApiDTO<AuthResponseDTO>> login(@Valid @RequestBody LoginRequestDTO dto) {
        AuthResponseDTO authResponseDTO = authService.login(dto);
        return ResponseEntity.ok(new ResponseApiDTO<AuthResponseDTO>(
            HttpStatus.OK.value(),
            "usuário autenticado com sucesso!",
            authResponseDTO
        ));
    }
}
