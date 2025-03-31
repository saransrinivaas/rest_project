package com.examly.springapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.examly.springapp.entities.Doctor;
import com.examly.springapp.services.DoctorService;


import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    // Get all doctors (paginated)
    @GetMapping
    public Page<Doctor> getAllDoctors(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return doctorService.getAllDoctors(page, size);
    }

    // Get doctor by ID
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctor(id);
        return doctor != null ? ResponseEntity.ok(doctor) : ResponseEntity.notFound().build();
    }

    // Create or update doctor
    @PostMapping(consumes = "application/json", produces = "application/json")
    public Doctor createOrUpdateDoctor(@RequestBody Doctor doctor) {
        System.out.println("Received Doctor: " + doctor);
        return doctorService.saveDoctor(doctor);
    }
    

    // Delete doctor by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok().body("{\"message\": \"Doctor deleted successfully\"}");
    }

    // Find doctors by specialization
    @GetMapping("/specialization/{specialization}")
    public List<Doctor> getDoctorsBySpecialization(@PathVariable String specialization) {
        return doctorService.findDoctorsBySpecialization(specialization);
    }

    // Find doctors by experience
    @GetMapping("/experience/{years}")
    public List<Doctor> getDoctorsByExperience(@PathVariable int years) {
        return doctorService.findDoctorsByExperience(years);
    }

    // Find doctors by name
    @GetMapping("/search")
    public List<Doctor> getDoctorsByName(@RequestParam String name) {
        return doctorService.findDoctorsByName(name);
    }

    // Count doctors by specialization
    @GetMapping("/count/{specialization}")
    public long countDoctorsBySpecialization(@PathVariable String specialization) {
        return doctorService.countDoctorsBySpecialization(specialization);
    }
}
