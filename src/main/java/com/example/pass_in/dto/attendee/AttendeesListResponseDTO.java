package com.example.pass_in.dto.attendee;

import java.util.List;

public record AttendeesListResponseDTO (
        List<AttendeeDetails> attendees) {
}
