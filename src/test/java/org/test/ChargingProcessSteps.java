package org.test;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.example.ChargingPoint;
import org.example.ChargingPointManager;
import org.example.ChargingStatus;
import org.example.ChargingMode;
import org.example.Customer;
import org.example.CustomerAccount;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


public class ChargingProcessSteps {

    private final ChargingPointManager cpManager = ChargingPointManager.getInstance();

    private Customer customer;
    private CustomerAccount account;
    private ChargingPoint selectedPoint;



    private ChargingPoint ensureFreeChargingPoint(String cpId) {
        ChargingPoint cp = cpManager.findById(cpId);
        if (cp == null) {

            cp = cpManager.createChargingPoint(cpId, ChargingMode.AC, null);
        }
        cp.setStatus(ChargingStatus.IN_BETRIEB_FREI);
        return cp;
    }


    private ChargingStatus mapStatus(String statusText) {
        switch (statusText) {
            case "BELEGT":
                return ChargingStatus.IN_BETRIEB_BESETZT;
            case "FREI":
                return ChargingStatus.IN_BETRIEB_FREI;
            default:

                return ChargingStatus.valueOf(statusText);
        }
    }


    @Given("ein Kunde {string} existiert")
    public void kunde_existiert(String email) {
        customer = new Customer("C-1", email, "Testkunde_1@gmail.com", "CUST-1");
        account = customer.getAccount();
        assertNotNull(account);
    }

    @Given("ein freier Ladepunkt {string} existiert")
    public void freier_ladepunkt_existiert(String cpId) {
        ensureFreeChargingPoint(cpId);
    }

    @When("der Kunde den Ladevorgang am Ladepunkt {string} startet")
    public void kunde_startet_ladevorgang(String cpId) {
        ChargingPoint cp = cpManager.findById(cpId);
        assertNotNull(cp, "Ladepunkt " + cpId + " existiert nicht");
        cp.setStatus(ChargingStatus.IN_BETRIEB_BESETZT);
    }

    @Then("ist der Ladepunkt {string} im Status {string}")
    public void ist_ladepunkt_im_status(String cpId, String statusText) {
        ChargingPoint cp = cpManager.findById(cpId);
        assertNotNull(cp, "Ladepunkt " + cpId + " existiert nicht");
        ChargingStatus expected = mapStatus(statusText);
        assertEquals(expected, cp.getStatus());
    }

    @When("der Kunde den Ladevorgang am Ladepunkt {string} stoppt")
    public void kunde_stoppt_ladevorgang(String cpId) {
        ChargingPoint cp = cpManager.findById(cpId);
        assertNotNull(cp, "Ladepunkt " + cpId + " existiert nicht");
        cp.setStatus(ChargingStatus.IN_BETRIEB_FREI);
    }

    @Given("ein Kunde {string} existiert und hat Guthaben {double}")
    public void kunde_existiert_mit_guthaben(String email, double balance) {
        customer = new Customer("C-2", email, "Testkunde@gmail.com", "CUST-2");
        account = customer.getAccount();
        assertNotNull(account);
        account.credit(BigDecimal.valueOf(balance));
    }

    @When("der Kunde einen Ladevorgang am Ladepunkt {string} durchführt mit Verbrauch {double}")
    public void kunde_fuehrt_ladevorgang_durch(String cpId, double verbrauch) {
        ChargingPoint cp = ensureFreeChargingPoint(cpId);
        BigDecimal kosten = BigDecimal.valueOf(verbrauch);
        account.debit(kosten);
        cp.setStatus(ChargingStatus.IN_BETRIEB_BESETZT);
        cp.setStatus(ChargingStatus.IN_BETRIEB_FREI);
    }

    @Then("wird das Guthaben des Kunden auf {double} reduziert")
    public void guthaben_reduziert(double expectedBalance) {
        assertNotNull(account, "Kein Kundenkonto gefunden");
        assertEquals(0, account.getBalance()
                        .compareTo(BigDecimal.valueOf(expectedBalance)),
                "Guthaben stimmt nicht");
    }



    @Given("mehrere Ladepunkte existieren")
    public void mehrere_ladepunkte_existieren() {
        ensureFreeChargingPoint("CP-1");
        ensureFreeChargingPoint("CP-2");
        ensureFreeChargingPoint("CP-3");
    }

    @Given("Ladepunkt {string} ist frei")
    public void ladepunkt_ist_frei(String cpId) {
        ChargingPoint cp = ensureFreeChargingPoint(cpId);
        assertEquals(ChargingStatus.IN_BETRIEB_FREI, cp.getStatus());
    }

    @When("der Kunde einen Ladepunkt auswählt")
    public void kunde_waehlt_einen_ladepunkt_aus() {
        selectedPoint = cpManager.findById("CP-3");
        assertNotNull(selectedPoint, "Ladepunkt CP-3 existiert nicht");
    }

    @Then("wird {string} als ausgewählter Ladepunkt angezeigt")
    public void ausgewaehlter_ladepunkt_angezeigt(String expectedId) {
        assertNotNull(selectedPoint, "Es wurde kein Ladepunkt ausgewählt");
        assertEquals(expectedId, selectedPoint.getPointId());
    }
}
