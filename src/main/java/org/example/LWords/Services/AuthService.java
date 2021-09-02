package org.example.LWords.Services;

import org.example.LWords.dto.Requests.AuthRequest;
import org.example.LWords.dto.Responses.JwtResponse;

public interface AuthService {
    JwtResponse authenticateUser(AuthRequest authRequest);
    Boolean createUser(AuthRequest authRequest);
}
