package com.druzynav.exceptions.UserDoesNotExist;

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException() {
        super("User does not exisst");
    }
}
