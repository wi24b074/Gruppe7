package org.test;

import io.cucumber.java.en.*;
import org.example.*;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTransactionsInvoicesSteps {

    private final BillingManager billingManager = new BillingManager();
    private Customer selectedCustomer;
    private Invoice resultInvoice;

    @Given("der Betreiber ist in dem Admin-Dashboard eingeloggt")
    public void derBetreiberIstImAdminDashboardEingeloggt() {
        assertTrue(true);
    }

    @And("der Betreiber befindet sich in dem Bereich {string}")
    public void derBetreiberBefindetSichImBereich(String bereich) {
        assertEquals("Rechnungen", bereich);
    }

    @And("ein Kunde mit ID {string} und E-Mail {string} existiert")
    public void einKundeExistiert(String customerId, String email) {
        selectedCustomer = new Customer(customerId, email, "Max Mustermann", "pw123");
        CustomerAccount account = new CustomerAccount(customerId);
        selectedCustomer.setAccount(account);
    }

    @And("der Kunde hat mindestens eine Rechnung")
    public void derKundeHatMindestensEineRechnung() {
        ChargingPoint cp = new ChargingPoint("CP-1", ChargingMode.AC, null);
        ChargingSession session = new ChargingSession("S-1", selectedCustomer, cp);
        billingManager.chargeCustomerForSession(session);
    }

    @When("der Betreiber den Kunden mit ID {string} auswählt")
    public void derBetreiberDenKundenAuswaehlt(String customerId) {
        assertEquals(customerId, selectedCustomer.getCustomerId());
        resultInvoice = billingManager.getInvoiceForCustomer(selectedCustomer);
    }

    @Then("werden alle zugehörigen Rechnungen des Kunden angezeigt")
    public void werdenAlleZugehoerigenRechnungenDesKundenAngezeigt() {
        assertNotNull(resultInvoice);
        assertEquals(selectedCustomer, resultInvoice.getCustomer());
        assertFalse(resultInvoice.getSessions().isEmpty());
    }
}
