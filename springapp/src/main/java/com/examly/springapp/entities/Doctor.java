package com.examly.springapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false)
    private String contactInfo;

    @Column
    private String availability;

    private int experience;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
@JsonManagedReference
private List<Prescription> prescriptions;


@OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
@JsonManagedReference
private List<Report> reports;



    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Invoice> invoices;

    // Constructor with experience
    public Doctor(String name, String specialization, String contactInfo, String availability, int experience) {
        this.name = name;
        this.specialization = specialization;
        this.contactInfo = contactInfo;
        this.availability = availability;
        this.experience = experience;
    }

    // Constructor without experience
    public Doctor(Long id, String name, String specialization, String contactInfo, String availability) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.contactInfo = contactInfo;
        this.availability = availability;
    }
}
