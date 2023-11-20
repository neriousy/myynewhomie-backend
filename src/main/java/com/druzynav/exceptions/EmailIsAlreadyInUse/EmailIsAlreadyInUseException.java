package com.druzynav.exceptions.EmailIsAlreadyInUse;

public class EmailIsAlreadyInUseException extends RuntimeException {
    public EmailIsAlreadyInUseException(String email) {
        super("Email " + email + " is already in use");
    }
}
