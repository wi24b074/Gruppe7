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

        assertTrue(true);
    }

    @When("er einen neuen Standort mit allen notwendigen Daten anlegt")
    public void admin_legt_neuen_standort_an() {
        // Testdaten (ID/Name/Adresse kannst du natürlich anpassen)
        currentLocation = locationManager.createLocation("LOC-1", "Hauptbahnhof", "Bahnhofstraße 1");
        assertNotNull(currentLocation);
    }

    @Then("wird der Standort erfolgreich im System gespeichert")
    public void standort_wird_erfolgreich_gespeichert() {
        Location loc = locationManager.findById(currentLocation.getLocationId());
        assertNotNull(loc, "Standort wurde nicht im System gefunden");
    }



    @Given("ein Standort existiert im System")
    public void ein_standort_existiert_im_system() {
        currentLocation = locationManager.findById("LOC-1");
        if (currentLocation == null) {
            currentLocation = locationManager.createLocation("LOC-1", "Hauptbahnhof", "Bahnhofstraße 1");
        }
    }

    @When("der Administrator einen neuen Ladepunkt mit den technischen Details hinzufügt")
    public void admin_fuegt_neuen_ladepunkt_hinzu() {
        currentPoint = cpManager.createChargingPoint("CP-1", ChargingMode.AC, currentLocation);
        assertNotNull(currentPoint);
    }

    @Then("ist der neue Ladepunkt dem Standort zugeordnet und im System sichtbar")
    public void neuer_ladepunkt_ist_zugeordnet_und_sichtbar() {
        ChargingPoint cp = cpManager.findById("CP-1");
        assertNotNull(cp, "Ladepunkt CP-1 nicht gefunden");
        assertNotNull(cp.getLocation(), "Ladepunkt hat keinen Standort");
        assertEquals(currentLocation, cp.getLocation(), "Ladepunkt ist nicht dem erwarteten Standort zugeordnet");
    }



    @Given("ein bestehender Ladepunkt ist im System vorhanden")
    public void ein_bestehender_ladepunkt_ist_im_system_vorhanden() {
        currentLocation = locationManager.findById("LOC-1");
        if (currentLocation == null) {
            currentLocation = locationManager.createLocation("LOC-1", "Hauptbahnhof", "Bahnhofstraße 1");
        }

        currentPoint = cpManager.findById("CP-1");
        if (currentPoint == null) {
            currentPoint = cpManager.createChargingPoint("CP-1", ChargingMode.AC, currentLocation);
        }
    }

    @When("der Administrator Änderungen an diesem Ladepunkt vornimmt")
    public void admin_nimmt_aenderungen_am_ladepunkt_vor() {
        cpManager.updateChargingPoint(currentPoint.getPointId(), ChargingMode.DC, currentLocation);
    }

    @Then("sind die aktualisierten Daten im System gespeichert")
    public void aktualisierte_daten_sind_gespeichert() {
        ChargingPoint cp = cpManager.findById(currentPoint.getPointId());
        assertNotNull(cp);
        assertEquals(ChargingMode.DC, cp.getMode(), "Ladepunkt-Modus wurde nicht aktualisiert");
    }



    @Given("ein Ladepunkt ist im System aktiv")
    public void ein_ladepunkt_ist_im_system_aktiv() {
        currentLocation = locationManager.findById("LOC-1");
        if (currentLocation == null) {
            currentLocation = locationManager.createLocation("LOC-1", "Hauptbahnhof", "Bahnhofstraße 1");
        }

        currentPoint = cpManager.findById("CP-2");
        if (currentPoint == null) {
            currentPoint = cpManager.createChargingPoint("CP-2", ChargingMode.AC, currentLocation);
        }

        cpManager.setStatus(currentPoint.getPointId(), ChargingStatus.IN_BETRIEB_FREI);
        assertEquals(ChargingStatus.IN_BETRIEB_FREI,
                cpManager.findById(currentPoint.getPointId()).getStatus());
    }

    @When("der Administrator den Ladepunkt deaktiviert")
    public void admin_deaktiviert_den_ladepunkt() {
        cpManager.deactivateChargingPoint(currentPoint.getPointId());
    }

    @Then("erscheint der Ladepunkt als deaktiviert und ist nicht mehr nutzbar")
    public void ladepunkt_ist_deaktiviert_und_nicht_nutzbar() {
        ChargingPoint cp = cpManager.findById(currentPoint.getPointId());
        assertNotNull(cp);
        assertEquals(ChargingStatus.AUSSER_BETRIEB, cp.getStatus());
    }
}
