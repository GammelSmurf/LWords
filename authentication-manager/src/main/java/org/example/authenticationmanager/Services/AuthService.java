package org.example.authenticationmanager.Services;

import org.example.authenticationmanager.dto.AuthRequest;
import org.example.authenticationmanager.dto.JwtResponse;

public interface AuthService {
    JwtResponse authenticateUser(AuthRequest authRequest);
    Boolean createUser(AuthRequest authRequest);
}
