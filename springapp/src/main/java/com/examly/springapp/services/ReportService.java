package com.examly.springapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.examly.springapp.entities.Report;
import com.examly.springapp.repositories.ReportRepository;



@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public Page<Report> getAllReports(Pageable pageable) {
        return reportRepository.findAll(pageable);
    }

    public Report getReport(Long id) {
        return reportRepository.findById(id).orElse(null);
    }

    public Report createReport(Report report) {
        return reportRepository.save(report);
    }

    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
}