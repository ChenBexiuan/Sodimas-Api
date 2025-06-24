package com.sodimacapp.demo.controller; // Paquete donde se encuentra el controlador de autenticación

import com.sodimacapp.demo.dto.AuthResponseDTO; // Importación del DTO para la respuesta de autenticación
import com.sodimacapp.demo.dto.LoginDto; // Importación del DTO para el inicio de sesión
import com.sodimacapp.demo.dto.RegisterDto; // Importación del DTO para el registro
import com.sodimacapp.demo.model.User; // Importación del modelo de usuario
import com.sodimacapp.demo.repository.UserRepository; // Importación del repositorio de usuarios
import com.sodimacapp.demo.security.JwtGenerator; // Importación del generador de tokens JWT
import org.springframework.http.HttpStatus; // Importación para los códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Importación para la entidad de respuesta HTTP
import org.springframework.security.authentication.AuthenticationManager; // Importación para el administrador de autenticación
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Importación para el token de autenticación
import org.springframework.security.core.Authentication; // Importación para la autenticación del usuario
import org.springframework.security.core.context.SecurityContextHolder; // Importación para el contexto de seguridad
import org.springframework.security.crypto.password.PasswordEncoder; // Importación para el codificador de contraseñas
import org.springframework.web.bind.annotation.PostMapping; // Importación para la anotación de mapeo POST
import org.springframework.web.bind.annotation.RequestBody; // Importación para el cuerpo de la solicitud
import org.springframework.web.bind.annotation.RequestMapping; // Importación para el mapeo de la ruta
import org.springframework.web.bind.annotation.RestController; // Importación para el controlador REST

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/auth") // Mapeo de la ruta base para la autenticación
public class AuthController {

    private final AuthenticationManager authenticationManager; // Administrador de autenticación
    private final UserRepository userRepository; // Repositorio para acceder a los datos de usuario
    private final PasswordEncoder passwordEncoder; // Codificador de contraseñas
    private final JwtGenerator jwtGenerator; // Generador de tokens JWT

    // Constructor que inyecta las dependencias necesarias
    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    // Endpoint para registrar un nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        // Verificamos si el email ya está registrado
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return new ResponseEntity<>("Email ya registrado!", HttpStatus.BAD_REQUEST); // Retorna 400 si el email ya existe
        }

        // Creamos un nuevo usuario
        User user = new User();
        user.setUsername(registerDto.getName()); // Usar 'name' como username
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword())); // Codificamos la contraseña
        user.setRole(registerDto.getRole() != null ? registerDto.getRole() : "candidate"); // Rol por defecto si no se especifica

        userRepository.save(user); // Guardamos el nuevo usuario en la base de datos

        return new ResponseEntity<>("Usuario registrado exitosamente!", HttpStatus.CREATED); // Retorna 201 si se registra correctamente
    }

    // Endpoint para iniciar sesión
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDto loginDto) {
        // Autenticamos al usuario usando el administrador de autenticación
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()));

        // Establecemos la autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication); // Generamos el token JWT

        // Buscamos el usuario en la base de datos
        User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Retornamos la respuesta de autenticación con el token y los datos del usuario
        return new ResponseEntity<>(new AuthResponseDTO(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        ), HttpStatus.OK); // Retorna 200 si el inicio de sesión es exitoso
    }
}
