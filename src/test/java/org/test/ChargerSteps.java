package org.test;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.example.ChargingPoint;
import org.example.ChargingPointManager;
import org.example.ChargingStatus;
import org.example.ChargingMode;
import org.example.Location;
import org.example.LocationManager;

import static org.junit.jupiter.api.Assertions.*;

public class ChargerSteps {

    private final ChargingPointManager cpManager = ChargingPointManager.getInstance();
    private final LocationManager locationManager = LocationManager.getInstance();

    @Given("ein Standort {string} mit Ladepunkt {string} existiert")
    public void siteWithPointExists(String siteId, String cpId) {
        Location loc = locationManager.findById(siteId);
        if (loc == null) {
            loc = locationManager.createLocation(siteId, siteId, "");
        }

        if (cpManager.findById(cpId) == null) {
            cpManager.createChargingPoint(cpId, ChargingMode.AC, loc);
        }
    }


    @Given("es existiert kein Standort mit id {string}")
    public void noLocationWithId(String siteId) {
        Location loc = locationManager.findById(siteId);
        if (loc != null) {

            assertNull(loc, "Standort mit id " + siteId + " existiert bereits");
        }
    }

    @When("der Betreiber legt einen neuen Standort mit id {string} und name {string} an")
    public void operatorCreatesLocation(String siteId, String name) {
        Location loc = locationManager.createLocation(siteId, name, "");
        assertNotNull(loc);
    }

    @Then("existiert ein Standort mit id {string}")
    public void locationExists(String siteId) {
        Location loc = locationManager.findById(siteId);
        assertNotNull(loc, "Standort mit id " + siteId + " existiert nicht");
    }

    @Then("der Standort hat den Namen {string}")
    public void locationHasName(String expectedName) {

        Location loc = locationManager.findById("LOC-1");
        assertNotNull(loc);
        assertEquals(expectedName, loc.getName());
    }


    @When("der Betreiber fragt den Status von {string} ab")
    public void queryStatus(String cpId) {
        ChargingPoint cp = cpManager.findById(cpId);
        assertNotNull(cp, "Ladepunkt " + cpId + " existiert nicht");
    }

    @Then("ist der Status von {string} {string}")
    public void assertStatus(String cpId, String expectedStatus) {
        ChargingPoint cp = cpManager.findById(cpId);
        assertNotNull(cp, "Ladepunkt " + cpId + " existiert nicht");
        assertEquals(ChargingStatus.valueOf(expectedStatus), cp.getStatus());
    }


    @When("der Betreiber ändert den Modus von {string} auf {string}")
    public void operatorChangesMode(String cpId, String mode) {
        ChargingPoint cp = cpManager.findById(cpId);
        assertNotNull(cp, "Ladepunkt " + cpId + " existiert nicht");
        cp.setMode(ChargingMode.valueOf(mode));
    }

    @Then("hat der Ladepunkt {string} den Modus {string}")
    public void assertChargingPointMode(String cpId, String expectedMode) {
        ChargingPoint cp = cpManager.findById(cpId);
        assertNotNull(cp, "Ladepunkt " + cpId + " existiert nicht");
        assertEquals(ChargingMode.valueOf(expectedMode), cp.getMode());
    }


    @When("der Betreiber setzt den Status von {string} auf {string}")
    public void setStatus(String cpId, String status) {
        ChargingPoint cp = cpManager.findById(cpId);
        assertNotNull(cp, "Ladepunkt " + cpId + " existiert nicht");
        cpManager.setStatus(cpId, ChargingStatus.valueOf(status));
    }
}
