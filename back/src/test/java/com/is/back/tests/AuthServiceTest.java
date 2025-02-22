package com.is.back.tests;

import com.is.back.dto.UserLoginDTO;
import com.is.back.dto.UserRegistrationDTO;
import com.is.back.entity.Users;
import com.is.back.exception.UserAlreadyExistsException;
import com.is.back.repositories.UserRepository;
import com.is.back.services.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testSignIn() {
        // Создаем тестового пользователя
        Users user = new Users();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole("USER");
        userRepository.save(user);

        // Создаем запрос на вход
        UserLoginDTO loginRequest = new UserLoginDTO();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        // Аутентифицируем пользователя и получаем токен
        String token = authService.signIn(loginRequest);

        // Проверяем, что токен не пустой
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    public void testSignUp() {
        // Создаем запрос на регистрацию
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setUsername("newuser");
        registrationDTO.setPassword("password123");
        registrationDTO.setRole("USER");

        // Регистрируем пользователя
        String token = authService.signUp(registrationDTO);

        // Проверяем, что пользователь зарегистрирован
        assertNotNull(token);
        assertTrue(userRepository.findByUsername("newuser").isPresent());
    }

    @Test
    public void testSignUpWithExistingUsername() {
        // Создаем тестового пользователя
        Users user = new Users();
        user.setUsername("existinguser");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole("USER");
        userRepository.save(user);

        // Создаем запрос на регистрацию с уже существующим именем
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setUsername("existinguser");
        registrationDTO.setPassword("password123");
        registrationDTO.setRole("USER");

        // Проверяем, что выбрасывается исключение
        assertThrows(UserAlreadyExistsException.class, () -> authService.signUp(registrationDTO));
    }
}
