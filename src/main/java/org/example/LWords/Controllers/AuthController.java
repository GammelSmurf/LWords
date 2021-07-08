package org.example.LWords.Controllers;

import lombok.RequiredArgsConstructor;
import org.example.LWords.Entities.AuthRequest;
import org.example.LWords.Security.JwtUtil;
import org.example.LWords.Security.MyUserDetails;
import org.example.LWords.Services.AuthService;
import org.example.LWords.Services.RecordService;
import org.example.LWords.dto.MessageResponse;
import org.example.LWords.repos.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.example.LWords.Entities.Record;

@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RecordRepository recordRepository;

    @GetMapping("/hello")
    public Iterable<Record> hello(){
        return recordRepository.findAll();
    }

    @PostMapping("/signin")
    public ResponseEntity<?> generateToken(@RequestBody AuthRequest authRequest) throws Exception{
        try {
            return ResponseEntity.ok(authService.authenticateUser(authRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
