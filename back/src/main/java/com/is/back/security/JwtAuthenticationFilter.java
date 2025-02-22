package com.is.back.security;

import com.is.back.security.JwtUtil;
import com.is.back.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Извлекаем токен из заголовка Authorization
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            // Если токен отсутствует, пропускаем запрос дальше
            filterChain.doFilter(request, response);
            return;
        }

        // Убираем префикс "Bearer " из заголовка
        String token = header.replace("Bearer ", "");

        try {
            // Парсим токен и извлекаем claims
            Claims claims = jwtUtil.parseToken(token);
            // Извлекаем имя пользователя и роли из токена
            String username = claims.getSubject();
            UserDetails userDetails = userService.getUserDetailsByName(username);

            // Создаем объект аутентификации
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // Устанавливаем аутентификацию в контекст SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            // Если токен невалидный, очищаем контекст безопасности
            SecurityContextHolder.clearContext();
        }

        // Продолжаем цепочку фильтров
        filterChain.doFilter(request, response);
    }
}
