package com.druzynav.exceptions.UserDoesNotExist;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserDoesNotExistAdvice {
    @ResponseBody
    @ExceptionHandler(UserDoesNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String EmailIsAlreadyInUseHandler(UserDoesNotExistException ex){
        return ex.getMessage();
    }
}
