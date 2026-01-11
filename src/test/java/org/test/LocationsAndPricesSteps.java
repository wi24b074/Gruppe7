package org.test;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import org.example.Location;
import org.example.LocationManager;
import org.example.PricingManager;
import org.example.Tariff;
import org.example.ChargingPointManager;
import org.example.ChargingPoint;
import org.example.ChargingMode;
import org.example.ChargingStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LocationsAndPricesSteps {

    private final LocationManager locationManager = LocationManager.getInstance();
    private final ChargingPointManager chargingPointManager = ChargingPointManager.getInstance();
    private final PricingManager pricingManager = PricingManager.getInstance();

    private List<Location> visibleLocations;
    private Location selectedLocation;
    private ChargingPoint selectedChargingPoint;

    private ChargingStatus mapStatus(String status) {
        return switch (status) {
            case "FREI" -> ChargingStatus.IN_BETRIEB_FREI;
            case "BELEGT" -> ChargingStatus.IN_BETRIEB_BESETZT;
            case "AUSSER_BETRIEB" -> ChargingStatus.AUSSER_BETRIEB;
            default -> ChargingStatus.valueOf(status);
        };
    }

    @Given("folgende Standorte existieren:")
    public void folgendeStandorteExistieren(DataTable table) {
        for (Map<String, String> row : table.asMaps()) {
            locationManager.createLocation(
                    row.get("locationId"),
                    row.get("name"),
                    row.get("address")
            );
        }
    }

    @Given("ein Standort existiert mit:")
    public void einStandortExistiertMit(DataTable table) {
        Map<String, String> row = table.asMaps().get(0);

        Location loc = locationManager.createLocation(
                row.get("locationId"),
                row.get("name"),
                row.get("address")
        );

        pricingManager.setTariff(
                loc,
                new BigDecimal(row.get("acPrice")),
                new BigDecimal(row.get("dcPrice"))
        );
    }

    @Given("folgende Ladepunkte existieren für Standort {string}:")
    public void folgendeLadepunkteExistieren(String locationId, DataTable table) {
        Location loc = locationManager.findById(locationId);
        assertNotNull(loc);

        for (Map<String, String> row : table.asMaps()) {
            chargingPointManager.createChargingPoint(
                    row.get("pointId"),
                    ChargingMode.valueOf(row.get("mode")),
                    loc
            );
        }
    }

    @Given("ein Standort {string} existiert")
    public void einStandortExistiert(String locationId) {
        locationManager.createLocation(locationId, "Test Standort", "Test Adresse");
    }

    @Given("ein Ladepunkt {string} mit Status {string} existiert am Standort {string}")
    public void ladepunktMitStatusExistiert(String pointId, String status, String locationId) {
        Location loc = locationManager.findById(locationId);
        ChargingPoint cp = chargingPointManager.createChargingPoint(pointId, ChargingMode.AC, loc);
        chargingPointManager.setStatus(pointId, mapStatus(status));
    }


    @When("der Kunde die Standortübersicht öffnet")
    public void derKundeOeffnetStandortuebersicht() {
        visibleLocations = locationManager.getAll();
    }

    @When("der Kunde den Standort {string} auswählt")
    public void derKundeWaehltStandort(String locationId) {
        selectedLocation = locationManager.findById(locationId);
        assertNotNull(selectedLocation);
    }

    @When("der Kunde den Ladepunkt {string} auswählt")
    public void derKundeWaehltLadepunkt(String pointId) {
        selectedChargingPoint = chargingPointManager.findById(pointId);
        assertNotNull(selectedChargingPoint);
    }


    @Then("werden {int} Standorte angezeigt")
    public void werdenStandorteAngezeigt(int expected) {
        assertEquals(expected, visibleLocations.size());
    }

    @Then("werden die Preise {double} für AC und {double} für DC angezeigt")
    public void werdenPreiseAngezeigt(double ac, double dc) {
        Tariff tariff = selectedLocation.getTariff();
        assertEquals(0, tariff.getAcPricePerKWh().compareTo(BigDecimal.valueOf(ac)));
        assertEquals(0, tariff.getDcPricePerKWh().compareTo(BigDecimal.valueOf(dc)));
    }

    @Then("es werden {int} Ladepunkte angezeigt")
    public void ladepunkteWerdenAngezeigt(int count) {
        assertEquals(count, selectedLocation.getChargingPoints().size());
    }

    @Then("wird der Status {string} angezeigt")
    public void statusWirdAngezeigt(String status) {
        assertEquals(mapStatus(status), selectedChargingPoint.getStatus());
    }
}
