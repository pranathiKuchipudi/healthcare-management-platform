package com.portfolio.healthcare.appointment;
import com.portfolio.healthcare.patient.Patient;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="appointments")
public class Appointment {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="patient_id",nullable=false) private Patient patient;
 @Column(nullable=false) private LocalDateTime scheduledAt;
 @Column(nullable=false,length=300) private String reason;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private AppointmentStatus status;
 protected Appointment(){}
 public Appointment(Patient patient,LocalDateTime scheduledAt,String reason){this.patient=patient;this.scheduledAt=scheduledAt;this.reason=reason;this.status=AppointmentStatus.SCHEDULED;}
 public Long getId(){return id;} public Patient getPatient(){return patient;} public LocalDateTime getScheduledAt(){return scheduledAt;}
 public String getReason(){return reason;} public AppointmentStatus getStatus(){return status;}
}
