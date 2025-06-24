package com.sodimacapp.demo.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // Este método se activa cuando un usuario no autenticado intenta acceder a un recurso protegido
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // Envía un error 401 (Unauthorized) con un mensaje de error
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}