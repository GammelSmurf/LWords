package org.example.LWords.Controllers;

import lombok.RequiredArgsConstructor;
import org.example.LWords.Entities.User;
import org.example.LWords.Services.UserService;
import org.example.LWords.dto.Requests.AuthRequest;
import org.example.LWords.Services.AuthService;
import org.example.LWords.dto.Responses.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody AuthRequest authRequest) throws Exception{
        try {
            return ResponseEntity.ok(authService.createUser(authRequest));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
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

    @GetMapping("/users")
    public Iterable<User> getUsers(){
        return userService.getUsers();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> removeUser(@PathVariable("id") long id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
