package com.example.pass_in.services;

import com.example.pass_in.domain.attendee.Attendee;
import com.example.pass_in.domain.checkin.CheckIn;
import com.example.pass_in.dto.attendee.AttendeeDetails;
import com.example.pass_in.dto.attendee.AttendeesListResponseDTO;
import com.example.pass_in.repositories.AttendeeRepository;
import com.example.pass_in.repositories.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
