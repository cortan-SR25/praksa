package com.example.dcim.service;
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() { super("Korisničko ime ili lozinka nisu ispravni."); }
}
