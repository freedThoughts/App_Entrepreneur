package com.mitti.entrepreneur.exception;

public class InsufficientResource extends RuntimeException {
    public InsufficientResource(final String message){
        super(message);
    }
}
