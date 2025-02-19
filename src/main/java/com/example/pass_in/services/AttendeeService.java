package com.example.pass_in.services;

import com.example.pass_in.domain.attendee.Attendee;
import com.example.pass_in.domain.attendee.exceptions.AttendeeAlreadyExistException;
import com.example.pass_in.domain.attendee.exceptions.AttendeeNotFoundException;
import com.example.pass_in.domain.checkin.CheckIn;
import com.example.pass_in.dto.attendee.AttendeeBadgeResponseDTO;
import com.example.pass_in.dto.attendee.AttendeeDetails;
import com.example.pass_in.dto.attendee.AttendeesListResponseDTO;
import com.example.pass_in.dto.attendee.AttendeeBadgeDTO;
import com.example.pass_in.repositories.AttendeeRepository;
import com.example.pass_in.repositories.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository; //o final torna esses atributos como obrigatorios no construtor
    private final CheckInRepository checkInRepository;

    public List<Attendee> getAllAttendeesFromEvent(String eventId){
        return  this.attendeeRepository.findByEventId(eventId);
    }
    //Cria attendeeDetailDTO e salva no attendeListResponseDTO
    public AttendeesListResponseDTO getEventsAttendee(String eventId){
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

        //O map abaixo passa por cada participante e consulta o checkin
        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkInRepository.findByAttendeeId(attendee.getId()); //Optional pois pode ou nao existir o checkin para aquele participante
            //Esta instrucao faz o mesmo que a linha abaixo: LocalDateTime checkedInAt = checkIn.isPresent() ? checkIn.get().getCreatedAt() : null;
            LocalDateTime checkedInAt = checkIn.<LocalDateTime>map(CheckIn::getCreatedAt).orElse(null);
            return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt);
        }).toList();

        return new AttendeesListResponseDTO(attendeeDetailsList);
    }

    //verfica se o participante ainda nao esta inscrito no evento
    public void verifyAttendeeSubscription(String email, String eventId){
        Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(email, eventId);
        //Se o participante estiver registado o programa vai parar a execucao do metodo e lancar uma excepcao
        if(isAttendeeRegistered.isPresent()) throw new AttendeeAlreadyExistException("Attendee is already registered");
    }

    //Regista um Participante no banco de dados
    public Attendee registerAttendee(Attendee newAttendee){
        this.attendeeRepository.save(newAttendee);
        return newAttendee;
    }

    //Exibi cracha
    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder ){
        Attendee attendee = this.attendeeRepository.findById(attendeeId).orElseThrow(
                () -> new AttendeeNotFoundException("Attendee not found with ID: " + attendeeId) //Lanca a excepcao pois o id passado pode nao existir
        );

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri().toString();

        AttendeeBadgeDTO badge = new AttendeeBadgeDTO(attendee.getId(), attendee.getEmail(), uri, attendee.getEvent().getId());
        return new AttendeeBadgeResponseDTO(badge);
    }

}
