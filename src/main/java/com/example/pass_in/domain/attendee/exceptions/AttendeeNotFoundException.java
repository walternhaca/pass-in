package com.example.pass_in.domain.attendee.exceptions;

public class AttendeeNotFoundException extends RuntimeException {

    public AttendeeNotFoundException(String message){
        super(message);
    }
}
