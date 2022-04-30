package org.example.authenticationmanager.Exceprions;

public class WrongPasswordException extends RuntimeException{
    public WrongPasswordException() {
        super(String.format("Wrong password!"));
    }
}
