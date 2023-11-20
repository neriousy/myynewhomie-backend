package com.druzynav.exceptions.UserDisabled;


import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserDisabledAdvice {
    @ResponseBody
    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(HttpStatus.LOCKED)
    public String EmailIsTakenHandler(){
        return "Email is not confirmed";
    }
}
