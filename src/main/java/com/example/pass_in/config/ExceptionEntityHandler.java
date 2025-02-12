package com.example.pass_in.config;

import com.example.pass_in.domain.event.exceptions.EventNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice //Indica ao spring que esta classe captura os erros lancados pelo controller

//Esta classe pega todas excepcoes e trata
public class ExceptionEntityHandler {

    @ExceptionHandler(EventNotFoundException.class) //Indica ao spring que o metodo abaixo lida coma excepcao passada na anotacao
    public ResponseEntity handleEventNotFound(EventNotFoundException exception){
        return ResponseEntity.notFound().build();
    }
}
