package com.example.pass_in.services;

//Nos arquivos da pasta service armazenamos a logica para as funcionalidades dos dimain's

import com.example.pass_in.domain.attendee.Attendee;
import com.example.pass_in.domain.event.Event;
import com.example.pass_in.dto.event.EventIdDTO;
import com.example.pass_in.dto.event.EventRequestDTO;
import com.example.pass_in.dto.event.EventResponseDTO;
import com.example.pass_in.repositories.AttendeeRepository;
import com.example.pass_in.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

@Service //Indica ao spring que esta classe representa um componente de service
@RequiredArgsConstructor //Indica o spring para gerer o construtor apenas com os atributos que forem necessarios
public class EventService {

    private final EventRepository eventRepository;
    private final AttendeeRepository attendeeRepository;

    //Retorna os dados do evento
    public EventResponseDTO getEventDetail(String eventId){
        Event event = this.eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with Id: " + eventId)); //Porque o findById retorna um Optional tratamos a possibilidade de nao haver id com a excepcao
        List<Attendee> attendeeList = this.attendeeRepository.findByEventId(eventId);
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