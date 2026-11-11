package com.portfolio.healthcare.appointment;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
public record AppointmentRequest(@NotNull Long patientId,@NotNull @FutureOrPresent LocalDateTime scheduledAt,@NotBlank @Size(max=300) String reason) {}
