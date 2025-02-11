package com.example.pass_in.dto.event;

//Este dto tem como objectivo a recepcao de dados do organizador do evento
public record EventRequestDTO (
        String title,
        String details,
        Integer maximumAttendees
){
}
