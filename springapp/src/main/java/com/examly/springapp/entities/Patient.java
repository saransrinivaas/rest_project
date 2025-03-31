package com.examly.springapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String contactInfo;

    @Column
    private String diagnosis;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
@JsonManagedReference
private List<Prescription> prescriptions;


@OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
@JsonManagedReference
private List<Report> reports;


    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Invoice> invoices = new ArrayList<>();

    public Patient(String name, String contactInfo, String diagnosis) {
        this.name = name;
        this.contactInfo = contactInfo;
        this.diagnosis = diagnosis;
    }

    public Patient(Long id, String name, String contactInfo, String diagnosis) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.diagnosis = diagnosis;
    }

    public Patient(Long id, String name, String contactInfo, String diagnosis, List<Appointment> appointments) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.diagnosis = diagnosis;
        this.appointments = appointments != null ? appointments : new ArrayList<>();
    }
}