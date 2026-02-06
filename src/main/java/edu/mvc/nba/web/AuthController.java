package edu.mvc.nba.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.mvc.nba.dto.AuthResponse;
import edu.mvc.nba.dto.LoginRequest;
import edu.mvc.nba.dto.RegisterRequest;
import edu.mvc.nba.service.AuthService;
import jakarta.validation.Valid;

// @RestController: Indicates this class handles REST requests and returns JSON responses.
// @RequestMapping: Sets the base URL path "/api/auth" for all endpoints in this controller.
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/{id}")

    /**
     * Objective: Endpoint to register a new user.
     *
     * Input: request - JSON body with username and password.
     * Output: ResponseEntity containing the JWT token.
     */
    // @PostMapping("/register"): Maps POST requests to /api/auth/register.
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            // @RequestBody: Deserializes JSON request body into the Java Record.
            // @Valid: Applies validation constraints defined in the DTO.
            @RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Objective: Endpoint to authenticate an existing user.
     *
     * Input: request - JSON body with username and password.
     * Output: ResponseEntity containing the JWT token.
     */
    // @PostMapping("/login"): Maps POST requests to /api/auth/login.
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
