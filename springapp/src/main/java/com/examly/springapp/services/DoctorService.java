package com.examly.springapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

import com.examly.springapp.entities.Doctor;
import com.examly.springapp.repositories.DoctorRepository;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    // Get all doctors with pagination
    public Page<Doctor> getAllDoctors(int page, int size) {
        return doctorRepository.findAll(PageRequest.of(page, size));
    }

    // Get doctor by ID
    public Doctor getDoctor(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }

    // Create or update a doctor
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // Delete doctor by ID
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    // Find doctors by specialization
    public List<Doctor> findDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }

    // Find doctors by experience
    public List<Doctor> findDoctorsByExperience(int years) {
        return doctorRepository.findByExperienceGreaterThan(years);
    }

    // Find doctors by name
    public List<Doctor> findDoctorsByName(String name) {
        return doctorRepository.findByNameContainingIgnoreCase(name);
    }

    // Count doctors by specialization
    public long countDoctorsBySpecialization(String specialization) {
        return doctorRepository.countBySpecialization(specialization);
    }
}