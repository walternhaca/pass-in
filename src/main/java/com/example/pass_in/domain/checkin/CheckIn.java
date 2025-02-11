package com.example.pass_in.domain.checkin;

import com.example.pass_in.domain.attendee.Attendee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "check_ins")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name ="created_AT")
    private LocalDateTime createdAt;

    @OneToOne //Indica relacao "um-para-um" isto porque um participante so pode fazer um check-in
    @JoinColumn(name = "attendee_id", nullable = false)
    private Attendee attendee;
}