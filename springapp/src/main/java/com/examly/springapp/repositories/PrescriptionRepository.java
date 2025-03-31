package com.examly.springapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.examly.springapp.entities.Prescription;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

}
