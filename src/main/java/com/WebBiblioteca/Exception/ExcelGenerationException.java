package com.WebBiblioteca.Exception;

import lombok.Getter;

@Getter
public class ExcelGenerationException extends RuntimeException{
    private final String resourceName;
    public ExcelGenerationException(String resourceName){
        super(String.format("Erro al crear el documento de excel %s",resourceName));
        this.resourceName = resourceName;
    }
}
