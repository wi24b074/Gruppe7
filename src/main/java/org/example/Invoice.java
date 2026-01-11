package org.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Invoice {

    private final String invoiceId;
    private final Customer customer;
    private final List<ChargingSession> sessions = new ArrayList<>();

    public Invoice(String invoiceId, Customer customer) {
        this.invoiceId = invoiceId;
        this.customer = customer;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<ChargingSession> getSessions() {
        return List.copyOf(sessions);
    }


    public void addSession(ChargingSession session) {
        if (session != null) {
            sessions.add(session);
        }
    }


    public BigDecimal getTotalAmount() {
        return sessions.stream()
                .map(ChargingSession::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
