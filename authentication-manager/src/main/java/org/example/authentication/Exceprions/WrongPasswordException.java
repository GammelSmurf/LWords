package org.example.authentication.Exceprions;

public class WrongPasswordException extends RuntimeException{
    public WrongPasswordException() {
        super(String.format("Wrong password!"));
    }
}
