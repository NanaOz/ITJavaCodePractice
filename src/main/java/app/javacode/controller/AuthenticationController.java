package app.javacode.controller;

import app.javacode.model.User;
import app.javacode.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> authenticateUser(
            @RequestParam String username,
            @RequestParam String password) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> {
                        logger.warn("User not found: {}", username);
                        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
                    });

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, createSessionCookie().toString())
                    .body(Map.of(
                            "user", Map.of(
                                    "email", user.getEmail(),
                                    "name", user.getName(),
                                    "role", user.getRole()
                            )
                    ));

        } catch (BadCredentialsException e) {
            logger.warn("Failed login attempt for username: {}", username);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

    private ResponseCookie createSessionCookie() {
        return ResponseCookie.from("SESSION", "session-id")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofHours(2))
                .sameSite("Lax")
                .build();
    }
}