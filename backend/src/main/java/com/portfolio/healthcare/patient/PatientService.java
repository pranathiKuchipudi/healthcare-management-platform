package com.portfolio.healthcare.patient;
import com.portfolio.healthcare.shared.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @Transactional
public class PatientService {
 private final PatientRepository repository;
 public PatientService(PatientRepository repository){this.repository=repository;}
 public PatientResponse create(PatientRequest r){
  if(repository.existsByEmailIgnoreCase(r.email())) throw new DuplicateResourceException("Email already exists");
  return PatientResponse.from(repository.save(new Patient(r.firstName().trim(),r.lastName().trim(),r.dateOfBirth(),r.email().trim().toLowerCase(),r.phone())));
 }
 @Transactional(readOnly=true) public List<PatientResponse> findAll(){return repository.findAll().stream().map(PatientResponse::from).toList();}
 @Transactional(readOnly=true) public PatientResponse findById(Long id){return PatientResponse.from(get(id));}
 public PatientResponse update(Long id,PatientRequest r){
  Patient p=get(id);
  repository.findByEmailIgnoreCase(r.email()).filter(x->!x.getId().equals(id)).ifPresent(x->{throw new DuplicateResourceException("Email already in use");});
  p.update(r.firstName().trim(),r.lastName().trim(),r.dateOfBirth(),r.email().trim().toLowerCase(),r.phone());
  return PatientResponse.from(repository.save(p));
 }
 public void delete(Long id){repository.delete(get(id));}
 private Patient get(Long id){return repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Patient",id));}
}
