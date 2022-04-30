package org.example.authenticationmanager.Controllers;

import lombok.RequiredArgsConstructor;
import org.example.authenticationmanager.Services.AuthService;
import org.example.authenticationmanager.dto.AuthRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Object> createUser(@RequestBody AuthRequest authRequest) {
        try {
            return ResponseEntity.ok(authService.createUser(authRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> generateToken(@RequestBody AuthRequest authRequest) {
        try {
            return ResponseEntity.ok(authService.authenticateUser(authRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
