package com.gibanator.dailystepbackendjava.auth;

import com.gibanator.dailystepbackendjava.global.DefaultApiResponses;
import com.gibanator.dailystepbackendjava.user.dto.AuthResponse;
import com.gibanator.dailystepbackendjava.user.dto.LoginRequest;
import com.gibanator.dailystepbackendjava.user.dto.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register new user")
    @DefaultApiResponses
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req){
        return ResponseEntity.ok(authService.login(req));
    }
}
