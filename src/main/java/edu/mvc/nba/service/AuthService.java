package edu.mvc.nba.service;

import edu.mvc.nba.dto.AuthResponse;
import edu.mvc.nba.dto.LoginRequest;
import edu.mvc.nba.dto.RegisterRequest;
import edu.mvc.nba.model.Role;
import edu.mvc.nba.model.User;
import edu.mvc.nba.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// @Service: Marks this class as a business logic component in the Spring context.
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, 
                       PasswordEncoder passwordEncoder, 
                       JwtService jwtService, 
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Objective: Registers a new user, encrypts the password, saves to DB, and returns a JWT.
     *
     * Input: request - DTO containing raw username and password.
     * Output: AuthResponse - The generated JWT token for immediate access.
     */
    // @Transactional: Ensures the save operation is atomic.
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // 1. Build User entity with encoded password and default role
        User user = new User(
            request.username(), 
            passwordEncoder.encode(request.password()),
            Role.USER
        );
        
        // 2. Persist to Database
        userRepository.save(user);
        
        // 3. Generate Token
        String jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }

    /**
     * Objective: Authenticates credentials using Spring Security and generates a JWT.
     *
     * Input: request - DTO containing username and password.
     * Output: AuthResponse - The generated JWT token if credentials are valid.
     */
    public AuthResponse login(LoginRequest request) {
        // 1. Delegate authentication to Spring Security Manager
        // This will throw AuthenticationException if password/user is wrong
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        
        // 2. Fetch User to generate token (Authenticated at this point)
        User user = userRepository.findByUsername(request.username())
            .orElseThrow(() -> new RuntimeException("User not found despite authentication"));
            
        // 3. Generate Token
        String jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }
}