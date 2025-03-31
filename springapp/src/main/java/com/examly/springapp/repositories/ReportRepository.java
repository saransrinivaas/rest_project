package com.examly.springapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examly.springapp.entities.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

}
