package com.example.pass_in.dto.event;
//DTO sao objectos utilizados para tranferir dados

//O Record eh um objecto simplificado usado para a tranferencia ou representacao de dados
public record EventDetailDTO(
        String id,
        String title,
        String details,
        String slug,
        Integer maximumAttendees,
        Integer attendeesAmount) {

}