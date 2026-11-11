package com.portfolio.healthcare.patient;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/v1/patients")
public class PatientController {
 private final PatientService service;
 public PatientController(PatientService service){this.service=service;}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public PatientResponse create(@Valid @RequestBody PatientRequest r){return service.create(r);}
 @GetMapping public List<PatientResponse> findAll(){return service.findAll();}
 @GetMapping("/{id}") public PatientResponse findById(@PathVariable Long id){return service.findById(id);}
 @PutMapping("/{id}") public PatientResponse update(@PathVariable Long id,@Valid @RequestBody PatientRequest r){return service.update(id,r);}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){service.delete(id);}
}
