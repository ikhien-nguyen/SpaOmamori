package com.spa.paymentservice.repository;

import com.spa.paymentservice.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    List<Invoice> findByCustomerId(String customerId);

    Optional<Invoice> findByAppointmentId(String appointmentId);

    boolean existsByAppointmentId(String appointmentId);

    // Dung khi VNPay goi IPN ve, chi co vnp_TxnRef de xac dinh dung hoa don nao.
    Optional<Invoice> findByVnpTxnRef(String vnpTxnRef);
}
