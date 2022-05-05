package org.example.authentication.Security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.example.authentication.Entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    @Value("${LWords.app.jwtSecret}")
    private String jwtSecret;

    @Value("${LWords.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(User user) {
        return Jwts.builder()
                .setSubject((user.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}
