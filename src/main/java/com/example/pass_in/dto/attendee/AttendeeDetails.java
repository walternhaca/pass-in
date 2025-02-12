package com.example.pass_in.dto.attendee;

import java.time.LocalDateTime;

public record AttendeeDetails(
        String id,
        String name,
        String email,
        LocalDateTime createdAt,
        LocalDateTime checkedInAt) {
}
