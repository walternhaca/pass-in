package com.example.pass_in.dto.event;

import com.example.pass_in.domain.event.Event;
import lombok.Getter;
import lombok.Setter;

@Getter
//Esta classe vai ser a resposta que vamos enviar ao nosso cliente
//Aqui optamos em classe e nao record pois se fez necessario um construtor diferente
public class EventResponseDTO {

    EventDetailDTO event;

    public EventResponseDTO (Event event, Integer numberOfAttendees){
        this.event = new EventDetailDTO(
                event.getId(),
                event.getTitle(),
                event.getDetails(),
                event.getSlug(),
                event.getMaximumAttendees(),
                numberOfAttendees);
    }
}
