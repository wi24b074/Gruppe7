package org.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BillingManager {

    private final List<Invoice> invoices = new ArrayList<>();

    /**
     * Berechnet Kosten einer Session + bucht vom Guthaben ab.
     */
    public void chargeCustomerForSession(ChargingSession session) {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null");
        }

        Customer customer = session.getCustomer();
        CustomerAccount account = customer.getAccount();

        BigDecimal totalCost = session.getTotalCost();

        account.debit(totalCost);


        Invoice invoice = findOrCreateInvoice(customer);
        invoice.addSession(session);
    }

    /**
     * Holt existierende Rechnung oder erstellt eine neue.
     */
    private Invoice findOrCreateInvoice(Customer customer) {
        return invoices.stream()
                .filter(i -> i.getCustomer().equals(customer))
                .findFirst()
                .orElseGet(() -> {
                    Invoice newInvoice = new Invoice(
                            "INV-" + customer.getCustomerId(),
                            customer
                    );
                    invoices.add(newInvoice);
                    return newInvoice;
                });
    }

    /**
     * Gibt Rechnung mit allen Sessions zurück.
     */
    public Invoice getInvoiceForCustomer(Customer customer) {
        return invoices.stream()
                .filter(i -> i.getCustomer().equals(customer))
                .findFirst()
                .orElse(null);
    }

    public List<ChargingSession> getSessionsForLocation(Location location) {
        List<ChargingSession> result = new ArrayList<>();

        for (Invoice invoice : invoices) {
            for (ChargingSession session : invoice.getSessions()) {
                ChargingPoint cp = session.getChargingPoint();
                if (cp != null && cp.getLocation().equals(location)) {
                    result.add(session);
                }
            }
        }

        return result;
    }


}

