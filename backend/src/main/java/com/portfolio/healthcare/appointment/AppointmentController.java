package com.portfolio.healthcare.appointment;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/v1/appointments")
public class AppointmentController {
 private final AppointmentService service;
 public AppointmentController(AppointmentService service){this.service=service;}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public AppointmentResponse create(@Valid @RequestBody AppointmentRequest r){return service.create(r);}
 @GetMapping public List<AppointmentResponse> findAll(){return service.findAll();}
 @GetMapping("/{id}") public AppointmentResponse findById(@PathVariable Long id){return service.findById(id);}
}
