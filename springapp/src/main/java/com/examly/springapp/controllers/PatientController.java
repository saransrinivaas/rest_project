package com.examly.springapp.controllers;

import com.examly.springapp.entities.Patient;
import com.examly.springapp.services.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/patients")
@CrossOrigin(origins = "*") // Allows requests from any origin
public class PatientController {

    @Autowired
    private PatientService patientService;

    // 🔹 Fetch All Patients (Paginated)
    @GetMapping
    public ResponseEntity<Page<Patient>> getAllPatients() {
        Page<Patient> patients = patientService.getAllPatients(0, 10);
        return ResponseEntity.ok(patients);
    }

    // 🔹 Fetch Patient by ID
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // 🔹 Create a New Patient
    @PostMapping(consumes = "application/json", produces = "application/json")
public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
    Patient savedPatient = patientService.createOrUpdatePatient(patient);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
}



    // 🔹 Update an Existing Patient
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @Valid @RequestBody Patient patient) {
        patient.setId(id);
        Patient updatedPatient = patientService.createOrUpdatePatient(patient);
        return ResponseEntity.ok(updatedPatient);
    }

    // 🔹 Delete a Patient
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Patient deleted successfully");
        return ResponseEntity.ok(response);
    }
}