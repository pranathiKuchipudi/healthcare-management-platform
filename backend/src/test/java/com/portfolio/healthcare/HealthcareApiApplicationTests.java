package com.portfolio.healthcare;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.healthcare.appointment.AppointmentRepository;
import com.portfolio.healthcare.patient.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HealthcareApiApplicationTests {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired PatientRepository patientRepository;
    @Autowired AppointmentRepository appointmentRepository;

    @BeforeEach
    void cleanDatabase() {
        appointmentRepository.deleteAll();
        patientRepository.deleteAll();
    }

    @Test
    void createsAndRetrievesPatient() throws Exception {
        String patient = """
            {"firstName":"Asha","lastName":"Rao","dateOfBirth":"1995-04-12","email":"asha@example.test","phone":"9999999999"}
            """;

        String response = mockMvc.perform(post("/api/v1/patients")
                .contentType(MediaType.APPLICATION_JSON).content(patient))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.email").value("asha@example.test"))
            .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();
        mockMvc.perform(get("/api/v1/patients/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("Asha"));
    }

    @Test
    void rejectsInvalidPatientInput() throws Exception {
        String invalidPatient = """
            {"firstName":"","lastName":"Rao","dateOfBirth":"2099-01-01","email":"not-an-email","phone":"123"}
            """;

        mockMvc.perform(post("/api/v1/patients")
                .contentType(MediaType.APPLICATION_JSON).content(invalidPatient))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Request validation failed"))
            .andExpect(jsonPath("$.validationErrors.firstName").exists())
            .andExpect(jsonPath("$.validationErrors.email").exists());
    }

    @Test
    void preventsDuplicatePatientEmail() throws Exception {
        String patient = """
            {"firstName":"Asha","lastName":"Rao","dateOfBirth":"1995-04-12","email":"asha@example.test","phone":"9999999999"}
            """;
        mockMvc.perform(post("/api/v1/patients").contentType(MediaType.APPLICATION_JSON).content(patient))
            .andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/patients").contentType(MediaType.APPLICATION_JSON).content(patient))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.message").value("Email already exists"));
    }

    @Test
    void createsAppointmentForExistingPatient() throws Exception {
        String patient = """
            {"firstName":"Asha","lastName":"Rao","dateOfBirth":"1995-04-12","email":"asha@example.test","phone":"9999999999"}
            """;
        String response = mockMvc.perform(post("/api/v1/patients")
                .contentType(MediaType.APPLICATION_JSON).content(patient))
            .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long patientId = objectMapper.readTree(response).get("id").asLong();
        String appointment = """
            {"patientId":%d,"scheduledAt":"%s","reason":"Annual check-up"}
            """.formatted(patientId, LocalDateTime.now().plusDays(2).withNano(0));

        mockMvc.perform(post("/api/v1/appointments")
                .contentType(MediaType.APPLICATION_JSON).content(appointment))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.patientId").value(patientId))
            .andExpect(jsonPath("$.patientName").value("Asha Rao"))
            .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    @Test
    void returnsNotFoundForUnknownPatient() throws Exception {
        mockMvc.perform(get("/api/v1/patients/999999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Patient not found with id 999999"));
    }
}
