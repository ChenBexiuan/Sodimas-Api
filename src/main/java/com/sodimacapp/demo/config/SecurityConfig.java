package com.sodimacapp.demo.config; // Paquete donde se encuentra la configuración de seguridad

import com.sodimacapp.demo.security.JwtAuthenticationEntryPoint; // Importación del manejador de excepciones de autenticación
import com.sodimacapp.demo.security.JwtAuthenticationFilter; // Importación del filtro de autenticación JWT
import org.springframework.context.annotation.Bean; // Importación para definir beans
import org.springframework.context.annotation.Configuration; // Importación para la configuración de Spring
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager; // Importación del administrador de autenticación
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Importación para la configuración de autenticación
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Importación para habilitar seguridad a nivel de método
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Importación para la configuración de seguridad HTTP
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Importación para habilitar seguridad web
import org.springframework.security.config.http.SessionCreationPolicy; // Importación para la gestión de sesiones
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Importación del codificador de contraseñas BCrypt
import org.springframework.security.crypto.password.PasswordEncoder; // Importación para el codificador de contraseñas
import org.springframework.security.web.SecurityFilterChain; // Importación para la cadena de filtros de seguridad
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Importación para el filtro de autenticación de usuario y contraseña
import org.springframework.security.config.Customizer; // Importación necesaria para personalizar configuraciones

@Configuration // Indica que esta clase es una configuración de Spring
@EnableWebSecurity // Habilita la seguridad web de Spring
@EnableMethodSecurity // Habilita la seguridad a nivel de método (por ejemplo, usando @PreAuthorize)
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // Manejador de excepciones de autenticación
    private final JwtAuthenticationFilter jwtAuthenticationFilter; // Filtro de autenticación JWT

    // Constructor que inyecta las dependencias necesarias
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // Bean para el codificador de contraseñas (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Retorna una instancia de BCryptPasswordEncoder
    }

    // Bean para el AuthenticationManager, que se usará para autenticar usuarios
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // Retorna el AuthenticationManager configurado
    }

    // Configuración de la cadena de filtros de seguridad HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilita CSRF para APIs REST, ya que no se necesita en este contexto
            .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)) // Configura el manejador de excepciones de autenticación
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configura la gestión de sesiones como sin estado (para JWT)
            .authorizeHttpRequests(authorize -> authorize
                // Rutas públicas que no requieren autenticación
                .requestMatchers("/api/auth/**","/api/files/cvs/**").permitAll()
               
                // Permite a CUALQUIERA ver la lista de trabajos y los detalles de un trabajo específico.
                .requestMatchers(HttpMethod.GET, "/api/jobs", "/api/jobs/**").permitAll()
                
                // Cualquier otra petición (POST, PUT, DELETE en /jobs, etc.) debe ser autenticada
                .anyRequest().authenticated()
            )
            .cors(Customizer.withDefaults()); // Habilita CORS usando la configuración de WebMvcConfigurer

        // Añade el filtro JWT personalizado antes del filtro de autenticación de usuario y contraseña de Spring Security
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
      
        return http.build(); // Construye y retorna la configuración de seguridad
    }
}
