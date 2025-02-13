package com.example.pass_in.repositories;

import com.example.pass_in.domain.attendee.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendeeRepository extends JpaRepository<Attendee, String> {

    List<Attendee> findByEventId(String eventId);
    //Usamos optional pois constar assim como nao do banco de dados
    Optional<Attendee> findByEventIdAndEmail(String email, String eventId);
}