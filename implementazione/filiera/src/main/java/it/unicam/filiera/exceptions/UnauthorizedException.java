package it.unicam.filiera.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) { super(message); }
}