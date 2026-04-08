package com.pet.jiraliketracker.security;


import com.pet.jiraliketracker.dto.AppErrorDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException ex) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // 2. Вказуємо, що це JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 3. Формуємо DTO
        AppErrorDTO error = new AppErrorDTO(
                403,
                ex.getMessage() != null ? ex.getMessage() : "Forbidden",
                LocalDateTime.now()
        );

        // 4. Перетворюємо об'єкт в JSON та пишемо в response
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}