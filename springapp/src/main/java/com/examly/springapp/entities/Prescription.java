package com.examly.springapp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;

@Entity
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String medicalDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @JsonBackReference
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonBackReference
    private Patient patient;

    public Prescription() {}

    public Prescription(String medicalDetails, Doctor doctor, Patient patient) {
        this.medicalDetails = medicalDetails;
        this.doctor = doctor;
        this.patient = patient;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getMedicalDetails() { return medicalDetails; }

    public void setMedicalDetails(String medicalDetails) { this.medicalDetails = medicalDetails; }

    public Doctor getDoctor() { return doctor; }

    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public Patient getPatient() { return patient; }

    public void setPatient(Patient patient) { this.patient = patient; }
}
