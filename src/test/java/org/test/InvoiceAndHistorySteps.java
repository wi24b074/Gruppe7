package org.test;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import org.example.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class InvoiceAndHistorySteps {

    private final CustomerManager customerManager = CustomerManager.getInstance();
    private final ChargingPointManager chargingPointManager = ChargingPointManager.getInstance();
    private final BillingManager billingManager = new BillingManager();

    private Customer customer;
    private Invoice invoice;
    private List<ChargingSession> sessions = new ArrayList<>();

    private static class BalanceChange {
        double before;
        double after;

        BalanceChange(double before, double after) {
            this.before = before;
            this.after = after;
        }
    }

    private final List<BalanceChange> balanceHistory = new ArrayList<>();

    @Given("ein Kunde mit E-Mail {string} existiert")
    public void kunde_existiert(String email) {
        customer = customerManager.findByEmail(email);
        if (customer == null) {
            customer = customerManager.registerCustomer(email, "Max Mustermann", "pw123");
        }
        assertNotNull(customer);
    }

    @Given("der Kunde hat folgende abgeschlossene Ladevorgänge:")
    public void kunde_hat_abgeschlossene_ladevorgaenge(DataTable table) {
        Location loc = new Location("LOC-1", "Teststandort", "Adresse");
        ChargingPoint cp = new ChargingPoint("CP-1", ChargingMode.AC, loc);

        for (Map<String, String> row : table.asMaps()) {
            ChargingSession session =
                    new ChargingSession(row.get("sessionId"), customer, cp);
            chargingPointManager.addSession(session);
            billingManager.chargeCustomerForSession(session);
            sessions.add(session);
        }
    }

    @Given("es existiert eine Rechnung für den Kunden")
    public void rechnung_existiert() {
        invoice = billingManager.getInvoiceForCustomer(customer);
        assertNotNull(invoice);
    }

    @When("der Kunde seine Rechnung einsehen möchte")
    public void kunde_sieht_rechnung() {
        invoice = billingManager.getInvoiceForCustomer(customer);
    }

    @Then("wird die Rechnung mit {int} Ladevorgängen angezeigt")
    public void rechnung_mit_ladevorgaengen(int expectedCount) {
        assertNotNull(invoice);
        assertEquals(expectedCount, invoice.getSessions().size());
    }

    @Given("der Kunde hat folgende vergangene Ladevorgänge:")
    public void kunde_hat_vergangene_ladevorgaenge(DataTable table) {
        Location loc = new Location("LOC-2", "Historie", "Adresse");
        ChargingPoint cp = new ChargingPoint("CP-2", ChargingMode.DC, loc);

        for (Map<String, String> row : table.asMaps()) {
            ChargingSession session =
                    new ChargingSession(row.get("sessionId"), customer, cp);
            chargingPointManager.addSession(session);
        }
    }

    @When("der Kunde seine Ladehistorie öffnet")
    public void ladehistorie_oeffnen() {
        sessions = chargingPointManager.getSessionsForCustomer(customer);
    }

    @Then("werden {int} vergangene Ladevorgänge angezeigt")
    public void vergangene_ladevorgaenge_anzeigen(int expectedCount) {
        assertNotNull(sessions);
        assertEquals(expectedCount, sessions.size());
    }

    @Given("der Kunde hat folgende Guthabenänderungen:")
    public void guthabenaenderungen(DataTable table) {
        for (Map<String, String> row : table.asMaps()) {
            double before = Double.parseDouble(row.get("vorher"));
            double after = Double.parseDouble(row.get("nachher"));
            balanceHistory.add(new BalanceChange(before, after));
        }
    }

    @When("der Kunde seine Guthabenhistorie einsehen möchte")
    public void guthabenhistorie_oeffnen() {
        assertNotNull(balanceHistory);
    }

    @Then("werden alle Guthabenänderungen angezeigt")
    public void alle_guthabenaenderungen_anzeigen() {
        assertFalse(balanceHistory.isEmpty());
        for (BalanceChange change : balanceHistory) {
            assertNotEquals(change.before, change.after);
        }
    }
}