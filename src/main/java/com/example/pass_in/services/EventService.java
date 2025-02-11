package com.example.pass_in.services;

//Nos arquivos da pasta service armazenamos a logica para as funcionalidades dos dimain's

import com.example.pass_in.domain.event.Event;
import com.example.pass_in.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service //Indica ao spring que esta classe representa um componente de service
@RequiredArgsConstructor //Indica o spring para gerer o construtor apenas com os atributos que forem necessarios
public class EventService {

    private final EventRepository eventRepository;

    public void getEventDetail(String eventId){
        Event event = this.eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with Id: " + eventId)); //Porque o findById retorna um Optional tratamos a possibilidade de nao haver id com a excepcao
        return;
    }
}