package com.gibanator.dailystepbackendjava.auth;

import com.gibanator.dailystepbackendjava.auth.exceptions.EmailAlreadyExistsException;
import com.gibanator.dailystepbackendjava.auth.exceptions.InvalidCredentialsException;
import com.gibanator.dailystepbackendjava.user.UserEntity;
import com.gibanator.dailystepbackendjava.user.UserRepository;
import com.gibanator.dailystepbackendjava.user.dto.AuthResponse;
import com.gibanator.dailystepbackendjava.user.dto.LoginRequest;
import com.gibanator.dailystepbackendjava.user.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest req){
        if (userRepository.existsByEmail(req.email())){
            throw new EmailAlreadyExistsException("Account with this email already exists.");
        }
        UserEntity user = new UserEntity();
        user.setEmail(req.email());
        user.setPasswordHash(passwordEncoder.encode(req.password()));

        UserEntity saved = userRepository.save(user);

        String token = jwtService.generateToken(saved.getId(), saved.getEmail());

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest req){
        UserEntity user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new InvalidCredentialsException("Email or password is wrong."));
        boolean matches = passwordEncoder.matches(req.password(), user.getPasswordHash());
        if (!matches) {
            throw new InvalidCredentialsException("Email or password is wrong.");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail());
        return new AuthResponse(token);
    }
}
