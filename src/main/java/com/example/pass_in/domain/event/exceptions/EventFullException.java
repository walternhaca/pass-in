package com.example.pass_in.domain.event.exceptions;

public class EventFullException extends RuntimeException{

    public EventFullException(String message){
        super(message);
    }
}
