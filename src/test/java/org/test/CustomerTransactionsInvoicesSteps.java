package org.test;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.example.Customer;
import org.example.BillingManager;
import org.example.Invoice;
import org.example.CustomerAccount;
import org.example.ChargingPoint;
import org.example.ChargingSession;
import org.example.ChargingMode;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTransactionsInvoicesSteps {

    private final BillingManager billingManager = new BillingManager();
    private Customer selectedCustomer;
    private Invoice resultInvoice;

    @Given("der Betreiber ist im Admin-Dashboard eingeloggt")
    public void derBetreiberIstImAdminDashboardEingeloggt() {
        assertTrue(true);
    }

    @And("der Betreiber befindet sich im Bereich {string}")
    public void derBetreiberBefindetSichImBereich(String bereich) {
        assertEquals("Rechnungen", bereich);
    }

    @When("der Betreiber einen bestimmten Kunden auswählt")
    public void derBetreiberEinenBestimmtenKundenAuswaehlt() {
        selectedCustomer = new Customer("C-001", "kunde@mail.de", "Max Mustermann", "pw123");
        CustomerAccount account = new CustomerAccount("C-001");
        selectedCustomer.setAccount(account);

        ChargingPoint cp = new ChargingPoint("CP-1", ChargingMode.AC, null);
        ChargingSession session = new ChargingSession("1", selectedCustomer, cp);
        session.getTotalCost();

        billingManager.chargeCustomerForSession(session);

        resultInvoice = billingManager.getInvoiceForCustomer(selectedCustomer);
    }

    @Then("werden alle zugehörigen Rechnungen des Kunden angezeigt")
    public void werdenAlleZugehoerigenRechnungenDesKundenAngezeigt() {
        assertNotNull(resultInvoice);
        assertEquals(selectedCustomer, resultInvoice.getCustomer());
        assertFalse(resultInvoice.getSessions().isEmpty());
    }
}
