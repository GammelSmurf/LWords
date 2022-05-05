package org.example.authentication.Services;

import org.example.authentication.dto.AuthRequest;
import org.example.authentication.dto.JwtResponse;

public interface AuthService {
    JwtResponse authenticateUser(AuthRequest authRequest);

    Boolean createUser(AuthRequest authRequest);
}
