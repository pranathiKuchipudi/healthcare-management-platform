package com.portfolio.healthcare.patient;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record PatientRequest(@NotBlank @Size(max=80) String firstName,@NotBlank @Size(max=80) String lastName,
 @NotNull @Past LocalDate dateOfBirth,@NotBlank @Email @Size(max=160) String email,@Size(max=30) String phone) {}
