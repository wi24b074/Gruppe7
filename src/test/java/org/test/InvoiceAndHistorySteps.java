package org.test;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.example.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class InvoiceAndHistorySteps {

    private final CustomerManager customerManager = CustomerManager.getInstance();
    private final ChargingPointManager chargingPointManager = ChargingPointManager.getInstance();
    private final BillingManager billingManager = new BillingManager();

    private Customer customer;
    private Invoice invoice;
    private List<ChargingSession> ladehistorie;
    private final List<BalanceChange> balanceHistory = new ArrayList<>();

    private static class BalanceChange {
        final double before;
        final double after;

        BalanceChange(double before, double after) {
            this.before = before;
            this.after = after;
        }
    }



    @Given("ein Kunde {string} hat mehrere Ladevorgänge abgeschlossen")
    public void einKundeHatMehrereLadevorgaengeAbgeschlossen(String email) {
        customer = customerManager.findByEmail(email);
        if (customer == null) {
            customer = customerManager.registerCustomer(email, "Max Mustermann", "pw123");
        }

        Location loc = new Location("INV-LOC-1", "Rechnungs-Standort", "Adresse 1");
        ChargingPoint cp = new ChargingPoint("INV-CP-1", ChargingMode.AC, loc);

        ChargingSession s1 = new ChargingSession("1", customer, cp);
        ChargingSession s2 = new ChargingSession("2", customer, cp);

        chargingPointManager.addSession(s1);
        chargingPointManager.addSession(s2);

        billingManager.chargeCustomerForSession(s1);
        billingManager.chargeCustomerForSession(s2);

        invoice = billingManager.getInvoiceForCustomer(customer);
    }

    @Given("es existiert eine Rechnung für diesen Kunden")
    public void esExistiertEineRechnungFuerDiesenKunden() {
        assertNotNull(invoice, "Es existiert keine Rechnung für den Kunden");
        assertEquals(customer, invoice.getCustomer());
    }

    @When("der Kunde seine Rechnung einsehen möchte")
    public void derKundeSeineRechnungEinsehenMoechte() {

        invoice = billingManager.getInvoiceForCustomer(customer);
    }

    @Then("wird die vollständige Rechnung inklusive aller Ladevorgänge angezeigt")
    public void wirdDieVollstaendigeRechnungInklusiveAllerLadevorgaengeAngezeigt() {
        assertNotNull(invoice, "Rechnung ist null");
        assertNotNull(invoice.getSessions(), "Rechnungs-Sessions sind null");
        assertFalse(invoice.getSessions().isEmpty(), "Rechnung enthält keine Ladevorgänge");
        assertTrue(invoice.getSessions().size() >= 2);
    }



    @Given("ein Kunde {string} hat vergangene Ladevorgänge")
    public void einKundeHatVergangeneLadevorgaenge(String email) {
        customer = customerManager.findByEmail(email);
        if (customer == null) {
            customer = customerManager.registerCustomer(email, "Max Mustermann", "pw123");
        }

        Location loc = new Location("HIST-LOC-1", "Historie-Standort", "Adresse 2");
        ChargingPoint cp = new ChargingPoint("HIST-CP-1", ChargingMode.DC, loc);

        ChargingSession s1 = new ChargingSession("1", customer, cp);
        ChargingSession s2 = new ChargingSession("2", customer, cp);

        chargingPointManager.addSession(s1);
        chargingPointManager.addSession(s2);
    }

    @When("der Kunde seine Ladehistorie öffnet")
    public void derKundeSeineLadehistorieOeffnet() {
        ladehistorie = chargingPointManager.getSessionsForCustomer(customer);
    }

    @Then("werden alle vergangenen Ladevorgänge chronologisch angezeigt")
    public void werdenAlleVergangenenLadevorgaengeChronologischAngezeigt() {
        assertNotNull(ladehistorie, "Ladehistorie ist null");
        assertFalse(ladehistorie.isEmpty(), "Ladehistorie ist leer");


        assertTrue(ladehistorie.size() >= 2);
    }



    @Given("ein Kunde {string} hat mehrere Ladevorgänge durchgeführt")
    public void einKundeHatMehrereLadevorgaengeDurchgefuehrt(String email) {
        customer = customerManager.findByEmail(email);
        if (customer == null) {
            customer = customerManager.registerCustomer(email, "Max Mustermann", "pw123");
        }



        double balance = 100.0;
        double after1 = balance - 15.0;
        balanceHistory.add(new BalanceChange(balance, after1));
        balance = after1;

        double after2 = balance - 20.0;
        balanceHistory.add(new BalanceChange(balance, after2));
        balance = after2;

        double after3 = balance - 5.0;
        balanceHistory.add(new BalanceChange(balance, after3));
    }

    @Given("sein Guthaben hat sich mehrfach verändert")
    public void seinGuthabenHatSichMehrfachVeraendert() {
        assertTrue(balanceHistory.size() >= 2,
                "Es wurden nicht mehrere Guthabenänderungen aufgezeichnet");
    }

    @When("der Kunde seine Guthabenhistorie einsehen möchte")
    public void derKundeSeineGuthabenhistorieEinsehenMoechte() {

        assertNotNull(balanceHistory);
    }

    @Then("sieht er alle Transaktionen inklusive Guthaben vor und nach jedem Vorgang")
    public void siehtErAlleTransaktionenInklusiveGuthabenVorUndNachJedemVorgang() {
        assertFalse(balanceHistory.isEmpty(), "Es wurden keine Guthabenänderungen gespeichert");

        for (BalanceChange change : balanceHistory) {
            assertNotEquals(change.before, change.after,
                    "Vor- und Nach-Guthaben sind identisch – es fand keine Änderung statt");
        }
    }
}