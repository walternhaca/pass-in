package com.example.pass_in.domain.attendee;

import com.example.pass_in.domain.event.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "attendees")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Attendee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @ManyToOne //Indica relacionamento "muitos para um" varios participantes podem estar relacionados a um evento
    @JoinColumn(name = "event_id", nullable = false ) //Indica a coluna desta tabela do qual queremos juntar
    private Event event;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}