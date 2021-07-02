package org.example.LWords.Services;

import org.example.LWords.Entities.AuthRequest;
import org.example.LWords.dto.JwtResponse;

public interface AuthService {
    JwtResponse authenticateUser(AuthRequest authRequest);
}
