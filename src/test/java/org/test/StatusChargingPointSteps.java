package org.test;

import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import org.example.*;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class StatusChargingPointSteps {

    private final LocationManager locationManager = LocationManager.getInstance();
    private final ChargingPointManager cpManager = ChargingPointManager.getInstance();
    private final BillingManager billingManager = new BillingManager();

    private List<ChargingPoint> visibleChargingPoints;
    private List<Location> visibleLocations;
    private List<ChargingSession> sessionsForLocation;
    private Location selectedLocation;


    @Given("folgende Ladepunkte existieren:")
    public void folgendeLadepunkteExistieren(DataTable table) {
        for (Map<String, String> row : table.asMaps()) {
            Location loc = locationManager.findById(row.get("StandortId"));
            if (loc == null) {
                loc = locationManager.createLocation(
                        row.get("StandortId"),
                        row.get("StandortId"),
                        ""
                );
            }

            ChargingPoint cp = cpManager.createChargingPoint(
                    row.get("LadepunktId"),
                    ChargingMode.AC,
                    loc
            );

            cp.setStatus(ChargingStatus.valueOf(row.get("Status")));
        }
    }

    @Given("folgende Standorte mit Ladepunkten existieren:")
    public void folgendeStandorteExistieren(DataTable table) {
        folgendeLadepunkteExistieren(table);
    }

    @Given("folgende Ladevorgänge existieren:")
    public void folgendeLadevorgaengeExistieren(DataTable table) {
        for (Map<String, String> row : table.asMaps()) {
            Location loc = locationManager.findById(row.get("StandortId"));
            if (loc == null) {
                loc = locationManager.createLocation(
                        row.get("StandortId"),
                        row.get("StandortId"),
                        ""
                );
            }

            ChargingPoint cp = cpManager.findById(row.get("LadepunktId"));
            if (cp == null) {
                cp = cpManager.createChargingPoint(
                        row.get("LadepunktId"),
                        ChargingMode.AC,
                        loc
                );
            }

            Customer customer = new Customer(
                    "C-" + row.get("SessionId"),
                    "test@example.com",
                    "Test",
                    "PW"
            );

            ChargingSession session = new ChargingSession(
                    row.get("SessionId"),
                    customer,
                    cp
            );

            billingManager.chargeCustomerForSession(session);
        }
    }

    @Given("der Betreiber befindet sich auf dem Dashboard")
    public void betreiberAufDashboard() {
    }

    @Given("der Betreiber befindet sich im Verwaltungsbereich")
    public void betreiberImVerwaltungsbereich() {
    }

    @Given("der Betreiber ist im Admin-Dashboard eingeloggt")
    public void betreiberEingeloggt() {
    }

    @Given("der Betreiber befindet sich im Bereich {string}")
    public void betreiberImBereich(String bereich) {
        assertNotNull(bereich);
    }

    @When("der Betreiber die Echtzeitansicht der Ladepunkte öffnet")
    public void oeffnetEchtzeitansicht() {
        visibleChargingPoints = new ArrayList<>();
        for (Location loc : locationManager.getAll()) {
            visibleChargingPoints.addAll(loc.getChargingPoints());
        }
    }

    @When("der Betreiber die Standortübersicht aufruft")
    public void oeffnetStandortuebersicht() {
        visibleLocations = locationManager.getAll();
    }

    @When("der Betreiber wählt den Standort {string}")
    public void waehltStandort(String locationId) {
        selectedLocation = locationManager.findById(locationId);
        assertNotNull(selectedLocation);
        sessionsForLocation = billingManager.getSessionsForLocation(selectedLocation);
    }

    @Then("werden die aktuellen Statusinformationen aller Ladepunkte angezeigt")
    public void statusinformationenAngezeigt() {
        assertNotNull(visibleChargingPoints);
        assertFalse(visibleChargingPoints.isEmpty());
    }

    @Then("der Betreiber sieht Ladepunkte mit Status:")
    public void betreiberSiehtStatus(DataTable table) {
        Set<ChargingStatus> expected = new HashSet<>();
        for (String status : table.asList()) {
            expected.add(ChargingStatus.valueOf(status));
        }

        boolean found = visibleChargingPoints.stream()
                .anyMatch(cp -> expected.contains(cp.getStatus()));

        assertTrue(found);
    }

    @Then("wird eine Liste aller Standorte angezeigt")
    public void listeAllerStandorte() {
        assertNotNull(visibleLocations);
        assertFalse(visibleLocations.isEmpty());
    }

    @Then("zu jedem Standort werden Anzahl und Zustand der Ladepunkte dargestellt")
    public void anzahlUndZustand() {
        for (Location loc : visibleLocations) {
            assertNotNull(loc.getChargingPoints());
            for (ChargingPoint cp : loc.getChargingPoints()) {
                assertNotNull(cp.getStatus());
            }
        }
    }

    @Then("werden alle Ladevorgänge angezeigt, die an diesem Standort durchgeführt wurden")
    public void ladevorgaengeFuerStandort() {
        assertNotNull(sessionsForLocation);
        assertFalse(sessionsForLocation.isEmpty());
        for (ChargingSession s : sessionsForLocation) {
            assertEquals(selectedLocation, s.getChargingPoint().getLocation());
        }
    }

    @Given("es existieren keine Ladepunkte im System")
    public void keineLadepunkteImSystem() {
        locationManager.clear();
    }

    @Then("werden keine Ladepunkte angezeigt")
    public void werdenKeineLadepunkteAngezeigt() {
        assertNotNull(visibleChargingPoints);
        assertTrue(visibleChargingPoints.isEmpty());
    }
}
