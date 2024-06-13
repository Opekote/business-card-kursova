package com.arksena.business_card.controller;

import com.arksena.business_card.DTO.UserRegistrationDTO;
import com.arksena.business_card.enums.Role;
import com.arksena.business_card.model.User;
import com.arksena.business_card.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRegistrationControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder; // Mock PasswordEncoder

    @InjectMocks
    private UserRegistrationController userController;

    private UserRegistrationDTO userRegistrationDTO;

    @BeforeEach
    public void setUp() {
        userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setUsername("testuser");
        userRegistrationDTO.setEmail("testuser@example.com");
        userRegistrationDTO.setPassword("testpassword");

        // Configure passwordEncoder mock behavior
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encryptedPassword");
    }

    @Test
    public void testRegisterUser() {
        // Mocking saved user
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername(userRegistrationDTO.getUsername());
        savedUser.setEmail(userRegistrationDTO.getEmail());
        savedUser.setPasswordHash("encryptedPassword"); // Example encrypted password
        savedUser.setRole(Role.USER);

        // Mocking userService.saveUser to return savedUser
        when(userService.saveUser(any(User.class))).thenReturn(savedUser);

        // Invoking controller method
        ResponseEntity<User> responseEntity = userController.registerUser(userRegistrationDTO);

        // Verifying response status and body
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        User responseBody = responseEntity.getBody();
        assertEquals(savedUser.getId(), responseBody.getId());
        assertEquals(savedUser.getUsername(), responseBody.getUsername());
        assertEquals(savedUser.getEmail(), responseBody.getEmail());
        assertEquals(savedUser.getRole(), responseBody.getRole());

        verify(userService, times(1)).saveUser(any(User.class));
    }
}
