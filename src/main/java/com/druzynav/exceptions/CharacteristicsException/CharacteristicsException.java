package com.druzynav.exceptions.CharacteristicsException;



public class CharacteristicsException extends RuntimeException{
    public CharacteristicsException(){
        super("You have to fill out the characteristics form");
    }
}
