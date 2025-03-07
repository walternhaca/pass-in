package com.example.pass_in.services;

//Nos arquivos da pasta service armazenamos a logica para as funcionalidades dos dimain's

import com.example.pass_in.domain.attendee.Attendee;
import com.example.pass_in.domain.event.Event;
import com.example.pass_in.domain.event.exceptions.EventFullException;
import com.example.pass_in.domain.event.exceptions.EventNotFoundException;
import com.example.pass_in.dto.attendee.AttendeeIdDTO;
import com.example.pass_in.dto.attendee.AttendeeRequestDTO;
import com.example.pass_in.dto.event.EventIdDTO;
import com.example.pass_in.dto.event.EventRequestDTO;
import com.example.pass_in.dto.event.EventResponseDTO;
import com.example.pass_in.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service //Indica ao spring que esta classe representa um componente de service
@RequiredArgsConstructor //Indica o spring para gerer o construtor apenas com os atributos que forem necessarios
public class EventService {

    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    //Retorna os dados do evento
    public EventResponseDTO getEventDetail(String eventId){
        Event event = this.getEventById(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);
        return new EventResponseDTO(event, attendeeList.size());
    }

    //Faz criacao de eventos
    public EventIdDTO createEvent(EventRequestDTO eventDTO){
        Event newEvent = new Event();
        newEvent.setTitle(eventDTO.title());
        newEvent.setDetails(eventDTO.details());
        newEvent.setMaximumAttendees(eventDTO.maximumAttendees());
        newEvent.setSlug(this.createSlug(eventDTO.title()));

        this.eventRepository.save(newEvent);

        return new EventIdDTO(newEvent.getId());
    }

    public Event getEventById(String eventId){
        return this.eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with Id: " + eventId)); //Porque o findById retorna um Optional tratamos a possibilidade de nao haver id com a excepcao
    }

    //Controla insercao de participantes no evento
    public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendeeRequestDTO){
        //verfica se o participante ainda nao esta inscrito
        this.attendeeService.verifyAttendeeSubscription(attendeeRequestDTO.email(), eventId);
        Event event = this.getEventById(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);

        if(event.getMaximumAttendees() <= attendeeList.size()) throw new EventFullException("Event is full");

        Attendee newAttendee = new Attendee();
        newAttendee.setName(attendeeRequestDTO.name());
        newAttendee.setEmail(attendeeRequestDTO.email());
        newAttendee.setEvent(event);
        newAttendee.setCreatedAt(LocalDateTime.now());
        this.attendeeService.registerAttendee(newAttendee);

        return  new AttendeeIdDTO(newAttendee.getId());
    }


    //Metodo auxiliar para criacao de slug
    private String createSlug(String text){
        //A intrucao abaixo faz a decomposicao canonica
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD); // passamos string a normalizar e o tipo de normalizacao
        //A intrucao abaixo aplica pega na string decomposta e substitui as assentuacoes por strings vazias, em seguida remove tudo que nao for letra ou numero, depois substitui todos espacos em branco por ifen, e por fim deixa tudo maiusculo
        return normalized.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("\\s+", "-")
                .toLowerCase();
    }
}