package com.sodimacapp.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtGenerator jwtGenerator;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtGenerator jwtGenerator, CustomUserDetailsService customUserDetailsService) {
        this.jwtGenerator = jwtGenerator;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. Obtener el token JWT de la cabecera de la petición
        String token = getJwtFromRequest(request);

        // 2. Validar el token y autenticar al usuario
        if (StringUtils.hasText(token) && jwtGenerator.validateToken(token)) {
            String username = jwtGenerator.getUsernameFromJwt(token);

            // Cargar los detalles del usuario
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // Crear un objeto de autenticación
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Establecer la autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // 3. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    // Método para extraer el JWT de la cabecera "Authorization"
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Eliminar el prefijo "Bearer "
        }
        return null;
    }
}
