package com.examly.springapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.examly.springapp.entities.Prescription;
import com.examly.springapp.services.PrescriptionService;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private static final Logger logger = LoggerFactory.getLogger(PrescriptionController.class);

    @Autowired
    private PrescriptionService prescriptionService;

    @GetMapping
    public ResponseEntity<Page<Prescription>> getAllPrescriptions(Pageable pageable) {
        logger.info("Fetching all prescriptions with pagination");
        return ResponseEntity.ok(prescriptionService.getAllPrescriptions(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable Long id) {
        logger.info("Fetching prescription with ID: {}", id);
        Optional<Prescription> prescription = prescriptionService.getPrescriptionById(id);
        return prescription.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Prescription not found with ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ResponseEntity<Prescription> createPrescription(@RequestBody Prescription prescription) {
        logger.info("Creating new prescription");
        return ResponseEntity.ok(prescriptionService.createPrescription(prescription));
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePrescription(@PathVariable Long id) {
        logger.info("Deleting prescription with ID: {}", id);
        try {
            prescriptionService.deletePrescription(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.warn("Prescription not found for deletion with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
