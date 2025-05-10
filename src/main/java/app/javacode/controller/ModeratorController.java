package app.javacode.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/moderator")
public class ModeratorController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<String> moderatorDashboard() {
        return ResponseEntity.ok("Moderator Dashboard");
    }
}
