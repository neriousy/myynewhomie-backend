package com.druzynav.exceptions.EmailIsAlreadyInUse;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EmailIsAlreadyInUseAdvice {
    @ResponseBody
    @ExceptionHandler(EmailIsAlreadyInUseException.class)
    @ResponseStatus(HttpStatus.IM_USED)
    public String EmailIsAlreadyInUseHandler(EmailIsAlreadyInUseException ex){
        return ex.getMessage();
    }
}
