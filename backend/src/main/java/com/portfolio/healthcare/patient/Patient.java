package com.portfolio.healthcare.patient;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity @Table(name="patients", uniqueConstraints=@UniqueConstraint(name="uk_patient_email", columnNames="email"))
public class Patient {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=80) private String firstName;
 @Column(nullable=false,length=80) private String lastName;
 @Column(nullable=false) private LocalDate dateOfBirth;
 @Column(nullable=false,length=160) private String email;
 @Column(length=30) private String phone;
 protected Patient() {}
 public Patient(String f,String l,LocalDate dob,String email,String phone){this.firstName=f;this.lastName=l;this.dateOfBirth=dob;this.email=email;this.phone=phone;}
 public Long getId(){return id;} public String getFirstName(){return firstName;} public String getLastName(){return lastName;}
 public LocalDate getDateOfBirth(){return dateOfBirth;} public String getEmail(){return email;} public String getPhone(){return phone;}
 public void update(String f,String l,LocalDate dob,String email,String phone){this.firstName=f;this.lastName=l;this.dateOfBirth=dob;this.email=email;this.phone=phone;}
}
