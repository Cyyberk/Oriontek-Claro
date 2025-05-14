package com.oriontek.OriontekClaro.exceptionHandlers;

import lombok.Getter;

public class MyApiException extends RuntimeException {
    
    @Getter
    private final int status;
    
    @Getter
    private final String errorCode;

    public MyApiException(String message, int status, String errorCode){
        super(message); 
        this.status = status;
        this.errorCode = errorCode;
    }
}
