package com.WebBiblioteca.Exception;

import lombok.Getter;

@Getter
public class NoDataFoundException extends RuntimeException{
    private final String message;
    public NoDataFoundException(String message){
        super(message);
        this.message = message;
    }
}
