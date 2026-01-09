package org.test;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;

import org.example.ChargingPoint;
import org.example.ChargingPointManager;
import org.example.ChargingStatus;
import org.example.ChargingMode;
import org.example.Customer;
import org.example.CustomerAccount;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class ChargingProcessSteps {

    private final ChargingPointManager cpManager = ChargingPointManager.getInstance();

    private Customer customer;
    private CustomerAccount account;
    private ChargingPoint selectedChargingPoint;

    @Given("ein Kunde mit der E-Mail {string} existiert")
    public void ein_kunde_existiert(String email) {
        customer = new Customer("C-1", email, "Max Mustermann", "CUST-1");
        account = customer.getAccount();
        assertNotNull(account);
    }

    @Given("ein Kunde mit der E-Mail {string} existiert und hat ein Guthaben von {double}")
    public void kunde_existiert_mit_guthaben(String email, double balance) {
        ein_kunde_existiert(email);
        account.credit(BigDecimal.valueOf(balance));
    }

    @Given("ein Ladepunkt mit ID {string} ist frei")
    public void ladepunkt_ist_frei(String pointId) {
        ChargingPoint cp = cpManager.findById(pointId);
        if (cp == null) {
            cp = cpManager.createChargingPoint(pointId, ChargingMode.AC, null);
        }
        cp.setStatus(ChargingStatus.IN_BETRIEB_FREI);
    }

    @Given("folgende Ladepunkte bestehen:")
    public void folgende_ladepunkte_bestehen(DataTable table) {
        for (Map<String, String> row : table.asMaps()) {
            String id = row.get("Ladepunkt");
            String status = row.get("Status");

            ChargingPoint cp = cpManager.findById(id);
            if (cp == null) {
                cp = cpManager.createChargingPoint(id, ChargingMode.AC, null);
            }

            cp.setStatus(mapStatus(status));
        }
    }

    @When("der Kunde startet den Ladevorgang am Ladepunkt {string}")
    public void kunde_startet_ladevorgang(String pointId) {
        ChargingPoint cp = cpManager.findById(pointId);
        assertNotNull(cp);
        cp.setStatus(ChargingStatus.IN_BETRIEB_BESETZT);
    }

    @When("der Kunde stoppt den Ladevorgang am Ladepunkt {string}")
    public void kunde_stoppt_ladevorgang(String pointId) {
        ChargingPoint cp = cpManager.findById(pointId);
        assertNotNull(cp);
        cp.setStatus(ChargingStatus.IN_BETRIEB_FREI);
    }

    @When("der Kunde führt einen Ladevorgang am Ladepunkt {string} mit Kosten von {double} durch")
    public void ladevorgang_mit_kosten(String pointId, double kosten) {
        ChargingPoint cp = cpManager.findById(pointId);
        assertNotNull(cp);

        account.debit(BigDecimal.valueOf(kosten));
        cp.setStatus(ChargingStatus.IN_BETRIEB_BESETZT);
        cp.setStatus(ChargingStatus.IN_BETRIEB_FREI);
    }

    @When("der Kunde wählt den Ladepunkt {string} aus")
    public void kunde_waehlt_ladepunkt(String pointId) {
        selectedChargingPoint = cpManager.findById(pointId);
        assertNotNull(selectedChargingPoint);
    }

    @Then("hat der Ladepunkt {string} den Status {string}")
    public void ladepunkt_hat_status(String pointId, String status) {
        ChargingPoint cp = cpManager.findById(pointId);
        assertNotNull(cp);
        assertEquals(mapStatus(status), cp.getStatus());
    }

    @Then("beträgt das Guthaben des Kunden {double}")
    public void guthaben_betraegt(double expected) {
        assertEquals(
                0,
                account.getBalance().compareTo(BigDecimal.valueOf(expected))
        );
    }

    @Then("ist der ausgewählte Ladepunkt {string}")
    public void ausgewählter_ladepunkt(String pointId) {
        assertNotNull(selectedChargingPoint);
        assertEquals(pointId, selectedChargingPoint.getPointId());
    }

    private ChargingStatus mapStatus(String status) {
        return switch (status) {
            case "FREI" -> ChargingStatus.IN_BETRIEB_FREI;
            case "BELEGT" -> ChargingStatus.IN_BETRIEB_BESETZT;
            default -> ChargingStatus.valueOf(status);
        };
    }
}
