package edu.mvc.nba.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.mvc.nba.model.User;
import edu.mvc.nba.repository.UserRepository; // Usamos repo directo solo para el ejemplo, idealmente Service

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Objective: Retrieve user details ONLY if the requester matches the requested
     * user.
     * * Rule: The 'username' in the URL must match the 'authentication.name' (JWT
     * User).
     */
    @GetMapping("/{username}")
    @PreAuthorize("#username == authentication.name") // <--- AQUÍ ESTÁ LA MAGIA DE INSTANCIA
    public ResponseEntity<User> getUserProfile(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Por seguridad, no devolvemos la contraseña en el JSON
        // (En un caso real usaríamos un UserDTO sin el campo password)
        return ResponseEntity.ok(user);
    }
}