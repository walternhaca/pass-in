package com.example.pass_in.controllers;

import com.example.pass_in.dto.attendee.AttendeeIdDTO;
import com.example.pass_in.dto.attendee.AttendeeRequestDTO;
import com.example.pass_in.dto.attendee.AttendeesListResponseDTO;
import com.example.pass_in.dto.event.EventIdDTO;
import com.example.pass_in.dto.event.EventRequestDTO;
import com.example.pass_in.dto.event.EventResponseDTO;
import com.example.pass_in.services.AttendeeService;
import com.example.pass_in.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final AttendeeService attendeeService;

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String id){
        EventResponseDTO event = this.eventService.getEventDetail(id);
        return ResponseEntity.ok().body(event);
    }

    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        EventIdDTO eventIdDTO = this.eventService.createEvent(body);

        /*A uri no parametro eh injetada pelo spring,eh util pois eh um padrao
        do status created mandar a uri onde o usuario pode resgatar a informacao
        por esta razao a instrucao abaixo inicializa a uri com dados referentes ao evento criado pelo usuario
        */

        var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(eventIdDTO.eventId()).toUri();
        return ResponseEntity.created(uri).body(eventIdDTO);
    }

    //Regista participante
    @PostMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeeIdDTO> registerParticipant(@PathVariable String eventId, @RequestBody AttendeeRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        AttendeeIdDTO attendeeIdDTO = this.eventService.registerAttendeeOnEvent(eventId, body);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/badge").buildAndExpand(attendeeIdDTO.attendeeId()).toUri();
        return ResponseEntity.created(uri).body(attendeeIdDTO);
    }


    @GetMapping("attendees/{id}")
    public ResponseEntity<AttendeesListResponseDTO> getEventAttendees(@PathVariable String id){
        AttendeesListResponseDTO attendeesListResponse = this.attendeeService.getEventsAttendee(id);
        return ResponseEntity.ok().body(attendeesListResponse);
    }
}
