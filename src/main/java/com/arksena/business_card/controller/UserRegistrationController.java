package com.arksena.business_card.controller;

import com.arksena.business_card.DTO.UserRegistrationDTO;
import com.arksena.business_card.enums.Role;
import com.arksena.business_card.model.User;
import com.arksena.business_card.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operations related to Cards")

public class UserRegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRegistrationDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword())); // Шифруємо пароль
        user.setRole(Role.USER); // Встановлюємо роль за замовчуванням як USER
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }
}


