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

    private Location currentLocation;
    private ChargingPoint currentPoint;

    @Given("ein Administrator befindet sich im Verwaltungsbereich")
    public void admin_im_verwaltungsbereich() {
        // rein fachlicher Kontext
        assertTrue(true);
    }

    @When("er einen neuen Standort mit id {string}, name {string} und adresse {string} anlegt")
    public void admin_legt_neuen_standort_an(String id, String name, String address) {
        currentLocation = locationManager.createLocation(id, name, address);
        assertNotNull(currentLocation);
    }

    @Then("existiert ein Standort mit id {string} und name {string}")
    public void standort_existiert(String id, String expectedName) {
        Location loc = locationManager.findById(id);
        assertNotNull(loc);
        assertEquals(expectedName, loc.getName());
    }

    @Given("ein Standort mit id {string} existiert")
    public void standort_existiert(String id) {
        currentLocation = locationManager.findById(id);
        if (currentLocation == null) {
            currentLocation = locationManager.createLocation(id, id, "");
        }
    }

    @When("der Administrator fügt einen Ladepunkt mit id {string} und Modus {string} zum Standort {string} hinzu")
    public void ladepunkt_hinzufuegen(String cpId, String mode, String locationId) {
        Location loc = locationManager.findById(locationId);
        assertNotNull(loc);
        currentPoint = cpManager.createChargingPoint(cpId, ChargingMode.valueOf(mode), loc);
    }

    @Then("ist der Ladepunkt {string} dem Standort {string} zugeordnet")
    public void ladepunkt_zugeordnet(String cpId, String locationId) {
        ChargingPoint cp = cpManager.findById(cpId);
        assertNotNull(cp);
        assertEquals(locationId, cp.getLocation().getLocationId());
    }

    @Given("ein Ladepunkt {string} mit Modus {string} existiert am Standort {string}")
    public void ladepunkt_existiert(String cpId, String mode, String locationId) {
        Location loc = locationManager.findById(locationId);
        if (loc == null) {
            loc = locationManager.createLocation(locationId, locationId, "");
        }
        currentPoint = cpManager.findById(cpId);
        if (currentPoint == null) {
            currentPoint = cpManager.createChargingPoint(cpId, ChargingMode.valueOf(mode), loc);
        }
    }

    @When("der Administrator ändert den Modus des Ladepunkts {string} auf {string}")
    public void ladepunkt_modus_aendern(String cpId, String newMode) {
        cpManager.updateChargingPoint(cpId, ChargingMode.valueOf(newMode), null);
    }

    @Then("hat der Ladepunkt {string} den Modus {string}")
    public void ladepunkt_hat_modus(String cpId, String expectedMode) {
        ChargingPoint cp = cpManager.findById(cpId);
        assertEquals(ChargingMode.valueOf(expectedMode), cp.getMode());
    }

    @Given("ein aktiver Ladepunkt {string} existiert am Standort {string}")
    public void aktiver_ladepunkt_existiert(String cpId, String locationId) {
        Location loc = locationManager.findById(locationId);
        if (loc == null) {
            loc = locationManager.createLocation(locationId, locationId, "");
        }
        currentPoint = cpManager.findById(cpId);
        if (currentPoint == null) {
            currentPoint = cpManager.createChargingPoint(cpId, ChargingMode.AC, loc);
        }
        cpManager.setStatus(cpId, ChargingStatus.IN_BETRIEB_FREI);
    }

    @When("der Administrator deaktiviert den Ladepunkt {string}")
    public void ladepunkt_deaktivieren(String cpId) {
        cpManager.deactivateChargingPoint(cpId);
    }

    @Then("ist der Status des Ladepunkt {string} {string}")
    public void ladepunkt_hat_status(String cpId, String expectedStatus) {
        ChargingPoint cp = cpManager.findById(cpId);
        assertEquals(ChargingStatus.valueOf(expectedStatus), cp.getStatus());
    }

    @When("der Administrator versucht einen neuen Standort mit id {string}, name {string}, adresse {string} anzulegen")
    public void derAdministratorVersuchtEinenNeuenStandortMitIdNameAdresseAnzulegen(String locId, String name, String address) {
        try {
            locationManager.createLocation(locId, name, address);
        } catch (Exception ignored) {
        }
    }

    @Then("bleibt die Anzahl der Standorte mit id {string} gleich {string}")
    public void anzahl_standorte_mit_id_bleibt_gleich(String locId, String expectedCount) {
        long count = locationManager.getAll().stream()
                .filter(l -> l.getLocationId().equals(locId))
                .count();
        assertEquals(Long.parseLong(expectedCount), count);
    }


}
