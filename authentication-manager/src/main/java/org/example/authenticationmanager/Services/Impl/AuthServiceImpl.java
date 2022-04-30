package org.example.authenticationmanager.Services.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authenticationmanager.Entities.ERole;
import org.example.authenticationmanager.Entities.Role;
import org.example.authenticationmanager.Entities.User;
import org.example.authenticationmanager.Exceprions.UserNotFoundException;
import org.example.authenticationmanager.Exceprions.WrongPasswordException;
import org.example.authenticationmanager.dto.AuthRequest;
import org.example.authenticationmanager.dto.JwtResponse;
import org.example.authenticationmanager.Security.JwtUtil;
import org.example.authenticationmanager.Services.AuthService;
import org.example.authenticationmanager.repos.RoleRepository;
import org.example.authenticationmanager.repos.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    final private JwtUtil jwtUtil;
    final private UserRepository userRepository;
    final private RoleRepository roleRepository;

    @Override
    public JwtResponse authenticateUser(AuthRequest authRequest) {
        User user = validateInputCredentials(authRequest);

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .map(Enum::name)
                .collect(Collectors.toList());

        String jwt = jwtUtil.generateJwtToken(user);
        return new JwtResponse(
                user.getId(),
                jwt, user.getUsername(), roles, user.getProgressLength(), user.getTranslationCount());
    }

    @Override
    public Boolean createUser(AuthRequest authRequest) {
        if(userRepository.existsByUsername(authRequest.getUserName()))
            return false;
        else{
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String password = encoder.encode(authRequest.getPassword());
            User user = new User(authRequest.getUserName(), password, LocalDateTime.now());
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName(ERole.USER));
            user.setRoles(roles);
            userRepository.save(user);

            return true;
        }
    }

    private User validateInputCredentials(AuthRequest authRequest){
        User user = userRepository.getUserByUsername(authRequest.getUserName());
        if(user == null){
            throw new UserNotFoundException(authRequest.getUserName());
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(authRequest.getPassword(), user.getPassword())){
            throw new WrongPasswordException();
        }
        return user;
    }
}
