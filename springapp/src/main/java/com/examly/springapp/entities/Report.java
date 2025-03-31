package com.examly.springapp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;

@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String reportName;

    @Column(nullable = false, length = 1000) // Ensure reports have limited length
    private String reportDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonBackReference
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @JsonBackReference
    private Doctor doctor;

    public Report() {}

    public Report(String reportName, String reportDetails, Patient patient, Doctor doctor) {
        this.reportName = reportName;
        this.reportDetails = reportDetails;
        this.patient = patient;
        this.doctor = doctor;
    }

    public Report(Long id, String reportName, String reportDetails, Patient patient) {
        this.id = id;
        this.reportName = reportName;
        this.reportDetails = reportDetails;
        this.patient = patient;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReportName() { return reportName; }
    public void setReportName(String reportName) { this.reportName = reportName; }

    public String getReportDetails() { return reportDetails; }
    public void setReportDetails(String reportDetails) { this.reportDetails = reportDetails; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

}