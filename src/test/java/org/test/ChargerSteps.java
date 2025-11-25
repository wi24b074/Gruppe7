package org.test;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import org.example.ChargingPointManager;
import org.example.ChargingStatus;

import static org.junit.jupiter.api.Assertions.*;


public class ChargerSteps {

    private final ChargingPointManager cpManager = ChargingPointManager.getInstance();

    @Given("ein Standort {string} mit Ladepunkt {string} existiert")
    public void siteWithPointExists(String siteId, String cpId){
        org.example.Location loc = org.example.LocationManager.getInstance().findById(siteId);
        if(loc == null){
            loc = org.example.LocationManager.getInstance().createLocation(siteId, siteId, "");
        }
        if(cpManager.findById(cpId) == null){
            cpManager.createChargingPoint(cpId, org.example.ChargingMode.AC, loc);
        }
    }

    @When("der Administrator fragt den Status von {string} ab")
    public void queryStatus(String cpId){
        org.example.ChargingPoint cp = cpManager.findById(cpId);
        assertNotNull(cp);
    }

    @Then("ist der Status von {string} {string}")
    public void assertStatus(String cpId, String expectedStatus){
        org.example.ChargingPoint cp = cpManager.findById(cpId);
        assertEquals(ChargingStatus.valueOf(expectedStatus), cp.getStatus());
    }

    @When("der Administrator setzt den Status von {string} auf {string}")
    public void setStatus(String cpId, String status){
        cpManager.setStatus(cpId, ChargingStatus.valueOf(status));
    }
}
