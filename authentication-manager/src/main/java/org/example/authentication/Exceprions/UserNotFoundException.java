package org.example.authentication.Exceprions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String username) {
        super(String.format("There is no account with username: %s", username));
    }
}
