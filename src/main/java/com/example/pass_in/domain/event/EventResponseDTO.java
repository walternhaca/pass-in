package com.example.pass_in.domain.event;

import com.example.pass_in.dto.event.EventDetailDTO;

//Esta classe vai ser a resposta que vamos enviar ao nosso cliente
public class EventResponseDTO {

    EventDetailDTO event;

    public EventResponseDTO (Event event, Integer numberOfAttendees){
        this.event = new EventDetailDTO(event.getId(), event.getTitle(), event.getDetails(), event.getSlug(), event.getMaximumAttendees(), numberOfAttendees);
    }
}
