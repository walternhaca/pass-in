package com.example.pass_in.domain.attendee.exceptions;

public class AttendeeAlreadyExistException extends RuntimeException {

    public AttendeeAlreadyExistException(String message){
        super(message);
    }
}
