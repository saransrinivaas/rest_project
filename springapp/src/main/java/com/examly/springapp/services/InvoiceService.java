package com.examly.springapp.services;


import com.examly.springapp.entities.Invoice;
import com.examly.springapp.repositories.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }

    public Invoice createInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public Invoice updateInvoice(Long id, Invoice updatedInvoice) {
        return invoiceRepository.findById(id).map(invoice -> {
            invoice.setInvoiceNumber(updatedInvoice.getInvoiceNumber());
            invoice.setCustomerName(updatedInvoice.getCustomerName());
            invoice.setTotalAmount(updatedInvoice.getTotalAmount());
            invoice.setInvoiceDate(updatedInvoice.getInvoiceDate());
            return invoiceRepository.save(invoice);
        }).orElse(null);
    }

    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }
}
