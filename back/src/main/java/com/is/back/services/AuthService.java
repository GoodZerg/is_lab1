package com.is.back.services;

import com.is.back.dto.UserDTO;
import com.is.back.dto.UserLoginDTO;
import com.is.back.dto.UserRegistrationDTO;
import com.is.back.entity.Users;
import com.is.back.exception.UserAlreadyExistsException;
import com.is.back.repositories.UserRepository;
import com.is.back.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Аутентифицирует пользователя и возвращает JWT-токен.
     *
     * @param loginRequest Запрос на аутентификацию (логин и пароль).
     * @return JWT-токен.
     */
    public String signIn(UserLoginDTO loginRequest) {
        // Аутентификация пользователя
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword())
        );

        // Генерация JWT-токена
        return jwtUtil.generateToken(
                    userService.convertToDTO(
                        userService.getUserByName(loginRequest.getUsername())
                    ));
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param registrationDTO Данные для регистрации (логин, пароль, роль).
     * @return Сообщение об успешной регистрации.
     * @throws UserAlreadyExistsException Если пользователь с таким именем уже существует.
     */
    public String signUp(UserRegistrationDTO registrationDTO) throws UserAlreadyExistsException {
        // Проверяем, существует ли пользователь с таким именем
        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User with username " + registrationDTO.getUsername() + " already exists");
        }

        Users user = new Users();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setRole(registrationDTO.getRole());

        Users savedUser = userRepository.save(user);


        return signIn(new UserLoginDTO(registrationDTO.getUsername(), registrationDTO.getPassword()));
    }
}
