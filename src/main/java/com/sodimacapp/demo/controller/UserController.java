package com.sodimacapp.demo.controller;

import com.sodimacapp.demo.model.User;
import com.sodimacapp.demo.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Controlador REST para gestionar usuarios del sistema
@RequestMapping("/api/users") // Ruta base para los endpoints relacionados a usuarios
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Obtener todos los usuarios registrados
    @GetMapping
    @PreAuthorize("hasRole('HR')") // Acceso restringido al rol HR
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Obtener un usuario específico por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('HR')") // Acceso restringido al rol HR
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear un nuevo usuario administrativo
    @PostMapping
    @PreAuthorize("hasRole('HR')") // Acceso restringido al rol HR
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = userService.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    // Actualizar un usuario existente por ID
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('HR')") // Acceso restringido al rol HR
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return (updatedUser != null)
                ? ResponseEntity.ok(updatedUser)
                : ResponseEntity.notFound().build();
    }

    // Eliminar un usuario por ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HR')") // Acceso restringido al rol HR
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
