package org.test;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.example.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StatusChargingPointSteps {

    private final LocationManager locationManager = LocationManager.getInstance();
    private final ChargingPointManager cpManager = ChargingPointManager.getInstance();
    private final BillingManager billingManager = new BillingManager();

    private List<ChargingPoint> allPoints = new ArrayList<>();
    private List<Location> allLocations = new ArrayList<>();
    private List<ChargingSession> sessionsForLocation = new ArrayList<>();
    private Location selectedLocation;



    @Given("der Betreiber befindet sich auf dem Dashboard")
    public void derBetreiberBefindetSichAufDemDashboard() {
        if (locationManager.getAll().isEmpty()) {
            Location loc = locationManager.createLocation("LOC-DASH-1", "Dashboard-Standort", "Adresse 1");
            cpManager.createChargingPoint("CP-D-1", ChargingMode.AC, loc);
            cpManager.createChargingPoint("CP-D-2", ChargingMode.DC, loc);
            cpManager.setStatus("CP-D-1", ChargingStatus.IN_BETRIEB_FREI);
            cpManager.setStatus("CP-D-2", ChargingStatus.IN_BETRIEB_BESETZT);
        }
    }

    @When("der Betreiber die Echtzeitansicht der Ladepunkte öffnet")
    public void derBetreiberDieEchtzeitansichtDerLadepunkteOeffnet() {
        allPoints = new ArrayList<>();
        for (Location loc : locationManager.getAll()) {
            allPoints.addAll(loc.getChargingPoints());
        }
    }

    @Then("werden die aktuellen Statusinformationen aller Ladepunkte angezeigt")
    public void werdenDieAktuellenStatusinformationenAllerLadepunkteAngezeigt() {
        assertNotNull(allPoints);
        assertFalse(allPoints.isEmpty(), "Es wurden keine Ladepunkte gefunden");

        for (ChargingPoint cp : allPoints) {
            assertNotNull(cp.getStatus(), "Ladepunkt " + cp.getPointId() + " hat keinen Status");
        }
    }

    @Then("der Betreiber sieht, welche Ladepunkte belegt, frei oder gestört sind")
    public void derBetreiberSiehtWelcheLadepunkteBelegtFreiOderGestoertSind() {
        boolean hatFrei = false;
        boolean hatBelegt = false;
        boolean hatGestoert = false;

        for (ChargingPoint cp : allPoints) {
            if (cp.getStatus() == ChargingStatus.IN_BETRIEB_FREI) {
                hatFrei = true;
            } else if (cp.getStatus() == ChargingStatus.IN_BETRIEB_BESETZT) {
                hatBelegt = true;
            } else if (cp.getStatus() == ChargingStatus.AUSSER_BETRIEB) {
                hatGestoert = true;
            }
        }

        assertTrue(hatFrei || hatBelegt || hatGestoert,
                "Es existiert kein Ladepunkt mit den erwarteten Statuswerten");
    }



    @Given("der Betreiber befindet sich im Verwaltungsbereich")
    public void derBetreiberBefindetSichImVerwaltungsbereich() {
        if (locationManager.getAll().isEmpty()) {
            Location loc1 = locationManager.createLocation("LOC-OV-1", "City West", "Adresse 2");
            Location loc2 = locationManager.createLocation("LOC-OV-2", "City Ost", "Adresse 3");

            cpManager.createChargingPoint("CP-OV-1", ChargingMode.AC, loc1);
            cpManager.createChargingPoint("CP-OV-2", ChargingMode.DC, loc2);
            cpManager.setStatus("CP-OV-1", ChargingStatus.IN_BETRIEB_FREI);
            cpManager.setStatus("CP-OV-2", ChargingStatus.AUSSER_BETRIEB);
        }
    }

    @When("der Betreiber die Standortübersicht aufruft")
    public void derBetreiberDieStandortuebersichtAufruft() {
        allLocations = locationManager.getAll();
    }

    @Then("wird eine Liste aller Standorte angezeigt")
    public void wirdEineListeAllerStandorteAngezeigt() {
        assertNotNull(allLocations);
        assertFalse(allLocations.isEmpty(), "Es gibt keine Standorte im System");
    }

    @Then("zu jedem Standort werden Anzahl und Zustand der Ladepunkte dargestellt")
    public void zuJedemStandortWerdenAnzahlUndZustandDerLadepunkteDargestellt() {
        for (Location loc : allLocations) {
            List<ChargingPoint> cps = loc.getChargingPoints();
            assertNotNull(cps, "Standort " + loc.getLocationId() + " hat null-Liste an Ladepunkten");
            for (ChargingPoint cp : cps) {
                assertNotNull(cp.getStatus(), "Ladepunkt " + cp.getPointId() + " hat keinen Status");
            }
        }
    }



    @Given("der Betreiber ist im Admin-Dashboard eingeloggt")
    public void derBetreiberIstImAdminDashboardEingeloggt() {
        Location loc = locationManager.findById("LOC-STAT-1");
        if (loc == null) {
            loc = locationManager.createLocation("LOC-STAT-1", "Statistik-Standort", "Adresse 4");
        }
        selectedLocation = loc;

        ChargingPoint cp = cpManager.findById("CP-STAT-1");
        if (cp == null) {
            cp = cpManager.createChargingPoint("CP-STAT-1", ChargingMode.AC, loc);
        }

        Customer customer = new Customer("C-STAT-1", "stats@example.com", "Stat Kunde", "CUST-STAT-1");

        ChargingSession s1 = new ChargingSession("1", customer, cp);
        ChargingSession s2 = new ChargingSession("2", customer, cp);

        billingManager.chargeCustomerForSession(s1);
        billingManager.chargeCustomerForSession(s2);
    }

    @Given("der Betreiber befindet sich im Bereich {string}")
    public void derBetreiberBefindetSichImBereich(String bereich) {
        assertEquals("Ladevorgänge", bereich);
    }

    @When("der Betreiber einen Standort auswählt")
    public void derBetreiberEinenStandortAuswaehlt() {
        assertNotNull(selectedLocation, "Kein Standort für die Auswertung gesetzt");
        sessionsForLocation = billingManager.getSessionsForLocation(selectedLocation);
    }

    @Then("werden alle Ladevorgänge angezeigt, die an diesem Standort durchgeführt wurden")
    public void werdenAlleLadevorgaengeAnDiesemStandortAngezeigt() {
        assertNotNull(sessionsForLocation, "Session-Liste ist null");
        assertFalse(sessionsForLocation.isEmpty(), "Es wurden keine Ladevorgänge für den Standort gefunden");

        for (ChargingSession s : sessionsForLocation) {
            assertNotNull(s.getChargingPoint());
            assertEquals(selectedLocation, s.getChargingPoint().getLocation(),
                    "Ladevorgang gehört nicht zum ausgewählten Standort");
        }
    }
}