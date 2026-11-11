package com.portfolio.healthcare.appointment;
import com.portfolio.healthcare.patient.PatientRepository;
import com.portfolio.healthcare.shared.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @Transactional
public class AppointmentService {
 private final AppointmentRepository appointments; private final PatientRepository patients;
 public AppointmentService(AppointmentRepository a,PatientRepository p){this.appointments=a;this.patients=p;}
 public AppointmentResponse create(AppointmentRequest r){var p=patients.findById(r.patientId()).orElseThrow(()->new ResourceNotFoundException("Patient",r.patientId()));return AppointmentResponse.from(appointments.save(new Appointment(p,r.scheduledAt(),r.reason().trim())));}
 @Transactional(readOnly=true) public List<AppointmentResponse> findAll(){return appointments.findAll().stream().map(AppointmentResponse::from).toList();}
 @Transactional(readOnly=true) public AppointmentResponse findById(Long id){var a=appointments.findById(id).orElseThrow(()->new ResourceNotFoundException("Appointment",id));return AppointmentResponse.from(a);}
}
