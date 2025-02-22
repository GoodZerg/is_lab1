package com.is.back.services;

import com.is.back.dto.UserDTO;
import com.is.back.dto.UserRegistrationDTO;
import com.is.back.entity.Users;
import com.is.back.exception.NotFoundException;
import com.is.back.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // Получить всех пользователей
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Получить пользователя по ID
    @Transactional(readOnly = true)
    public Users getUserById(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        return user;
    }

    @Transactional(readOnly = true)
    public Users getUserByName(String username) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));
        return user;
    }

    @Transactional(readOnly = true)
    public UserDetails getUserDetailsByName(String username) {
        UserDetailsService user = (UserDetailsService) userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));
        return user.loadUserByUsername(username);
    }

    // Зарегистрировать нового пользователя

    // Удалить пользователя
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    // Преобразовать сущность User в UserDTO
    @Transactional
    public UserDTO convertToDTO(Users user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setRole(user.getRole());
        return userDTO;
    }
}