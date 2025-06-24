package com.sodimacapp.demo.service;

import com.sodimacapp.demo.model.User;
import com.sodimacapp.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que gestiona la lógica relacionada con los usuarios.
 * Encargado de registrar, buscar, actualizar y eliminar usuarios.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Se usa para encriptar contraseñas

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param userRepository Repositorio de usuarios
     * @param passwordEncoder Codificador de contraseñas (bcrypt)
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Devuelve una lista con todos los usuarios registrados.
     * 
     * @return Lista de usuarios
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Busca un usuario por su ID.
     * 
     * @param id ID del usuario
     * @return Optional con el usuario si existe
     */
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    /**
     * Crea un nuevo usuario.
     * Antes de guardar, la contraseña es encriptada usando PasswordEncoder.
     * 
     * @param user Usuario a registrar
     * @return Usuario guardado en la base de datos
     */
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encripta la contraseña
        return userRepository.save(user);
    }

    /**
     * Actualiza los datos de un usuario existente.
     * NOTA: No se actualiza la contraseña aquí por seguridad.
     * 
     * @param id ID del usuario a actualizar
     * @param userDetails Nuevos datos del usuario
     * @return Usuario actualizado, o null si no se encuentra
     */
    public User updateUser(Integer id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setRole(userDetails.getRole());
            // Si deseas permitir editar más campos, puedes descomentar los siguientes:
            // user.setPhone(userDetails.getPhone());
            // user.setDepartment(userDetails.getDepartment());
            return userRepository.save(user);
        }).orElse(null);
    }

    /**
     * Elimina un usuario por su ID.
     * 
     * @param id ID del usuario a eliminar
     */
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    /**
     * Busca un usuario por su correo electrónico.
     * 
     * @param email Correo electrónico a buscar
     * @return Optional con el usuario si se encuentra
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
