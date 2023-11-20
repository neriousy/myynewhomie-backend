package com.druzynav.exceptions.CharacteristicsException;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CharacteristicsAdvice {
    @ResponseBody
    @ExceptionHandler(CharacteristicsException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String EmailIsAlreadyInUseHandler(CharacteristicsException ex){
        return ex.getMessage();
    }
}
