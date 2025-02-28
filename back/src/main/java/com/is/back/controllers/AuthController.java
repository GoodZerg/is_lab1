package com.is.back.controllers;

import com.is.back.dto.UserLoginDTO;
import com.is.back.dto.UserRegistrationDTO;
import com.is.back.dto.UserLoginResponseDTO;
import com.is.back.exception.UserAlreadyExistsException;
import com.is.back.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Обрабатывает запрос на вход и возвращает JWT-токен.
     *
     * @param loginRequest Запрос на вход (логин и пароль).
     * @return JWT-токен.
     */
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> signIn(@RequestBody UserLoginDTO loginRequest) {
        UserLoginResponseDTO response = authService.signIn(loginRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Обрабатывает запрос на регистрацию нового пользователя.
     *
     * @param registrationDTO Данные для регистрации (логин, пароль, роль).
     * @return Сообщение об успешной регистрации.
     */
    @PostMapping("/register")
    public ResponseEntity<UserLoginResponseDTO> signUp(@RequestBody UserRegistrationDTO registrationDTO) {
        try {
            UserLoginResponseDTO response = authService.signUp(registrationDTO);
            return ResponseEntity.ok(response);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }
}
