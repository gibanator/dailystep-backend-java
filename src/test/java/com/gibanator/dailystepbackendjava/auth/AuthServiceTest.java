package com.gibanator.dailystepbackendjava.auth;

import com.gibanator.dailystepbackendjava.auth.exceptions.EmailAlreadyExistsException;
import com.gibanator.dailystepbackendjava.user.UserEntity;
import com.gibanator.dailystepbackendjava.user.UserRepository;
import com.gibanator.dailystepbackendjava.user.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_shouldSaveUser_whenInputIsvalid(){
        RegisterRequest req = new RegisterRequest("test@mail.com", "19274Fjnmc_");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(false);
        when(passwordEncoder.encode("19274Fjnmc_")).thenReturn("encoded-password");

        UserEntity savedUserFromRepo = new UserEntity();
        savedUserFromRepo.setId(1L);
        savedUserFromRepo.setEmail("test@mail.com");
        savedUserFromRepo.setPasswordHash("encoded-password");

        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUserFromRepo);
        when(jwtService.generateToken(1L, "test@mail.com")).thenReturn("fake-jwt-token");

        authService.register(req);

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userCaptor.capture());
        UserEntity savedUser = userCaptor.getValue();
        assertEquals("test@mail.com", savedUser.getEmail());
        assertEquals("encoded-password", savedUser.getPasswordHash());
    }

    @Test
    void register_shouldThrow_whenEmailAlreadyExists(){
        RegisterRequest req = new RegisterRequest("test@mail.com", "19274Fjnmc_");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> authService.register(req));

        verify(userRepository, never()).save(any());
    }
}
