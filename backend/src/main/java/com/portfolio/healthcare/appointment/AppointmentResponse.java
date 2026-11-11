package com.portfolio.healthcare.appointment;
import java.time.LocalDateTime;
public record AppointmentResponse(Long id,Long patientId,String patientName,LocalDateTime scheduledAt,String reason,AppointmentStatus status) {
 public static AppointmentResponse from(Appointment a){var p=a.getPatient();return new AppointmentResponse(a.getId(),p.getId(),p.getFirstName()+" "+p.getLastName(),a.getScheduledAt(),a.getReason(),a.getStatus());}
}
