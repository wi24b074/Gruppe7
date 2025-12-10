package org.test;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

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

import static org.junit.jupiter.api.Assertions.*;

public class LocationsAndPricesSteps {

    private final LocationManager locationManager = LocationManager.getInstance();
    private final ChargingPointManager chargingPointManager = ChargingPointManager.getInstance();
    private final PricingManager pricingManager = PricingManager.getInstance();

    private List<Location> sichtbareStandorte;
    private Location ausgewaehlterStandort;
    private ChargingPoint ausgewaehlterLadepunkt;



    @Given("der Kunde befindet sich im Bereich {string}")
    public void derKundeBefindetSichImBereich(String bereich) {
        if (locationManager.getAll().isEmpty()) {
            locationManager.createLocation("LOC-1", "Hauptbahnhof", "Bahnhofstraße 1");
            locationManager.createLocation("LOC-2", "Altstadt", "Marktplatz 5");
        }
    }

    @When("der Kunde die Standortübersicht öffnet")
    public void derKundeDieStandortuebersichtOeffnet() {
        sichtbareStandorte = locationManager.getAll();
    }

    @Then("werden alle verfügbaren Standorte mit Basisinformationen angezeigt")
    public void werdenAlleVerfuegbarenStandorteMitBasisinformationenAngezeigt() {
        assertNotNull(sichtbareStandorte, "Es wurden keine Standorte geladen");
        assertFalse(sichtbareStandorte.isEmpty(), "Es sind keine Standorte im System");

        for (Location loc : sichtbareStandorte) {
            assertNotNull(loc.getLocationId(), "Standort hat keine ID");
            assertNotNull(loc.getName(), "Standort hat keinen Namen");
        }
    }


    @Given("der Kunde befindet sich in der Standortübersicht")
    public void derKundeBefindetSichInDerStandortuebersicht() {
        Location loc = locationManager.findById("LOC-PRICE-1");
        if (loc == null) {
            loc = locationManager.createLocation("LOC-PRICE-1", "City Center", "Hauptstraße 10");
        }

        pricingManager.setTariff(
                loc,
                new BigDecimal("0.30"), // AC
                new BigDecimal("0.50")  // DC
        );

        if (loc.getChargingPoints().isEmpty()) {
            chargingPointManager.createChargingPoint("CP-AC-1", ChargingMode.AC, loc);
            chargingPointManager.createChargingPoint("CP-DC-1", ChargingMode.DC, loc);
        }

        sichtbareStandorte = locationManager.getAll();
    }

    @When("der Kunde einen Standort auswählt")
    public void derKundeEinenStandortAuswaehlt() {
        ausgewaehlterStandort = locationManager.findById("LOC-PRICE-1");
        if (ausgewaehlterStandort == null && !sichtbareStandorte.isEmpty()) {
            ausgewaehlterStandort = sichtbareStandorte.get(0);
        }
        assertNotNull(ausgewaehlterStandort, "Es konnte kein Standort ausgewählt werden");
    }

    @Then("werden die aktuellen Preise für AC- und DC-Laden angezeigt")
    public void werdenDieAktuellenPreiseFuerACUndDCLadenAngezeigt() {
        Tariff tariff = ausgewaehlterStandort.getTariff();
        assertNotNull(tariff, "Für den Standort ist kein Tarif gesetzt");

        assertNotNull(tariff.getAcPricePerKWh(), "AC-Preis fehlt");
        assertNotNull(tariff.getDcPricePerKWh(), "DC-Preis fehlt");
    }

    @Then("die verfügbaren Ladepunkte des Standorts werden angezeigt")
    public void dieVerfuegbarenLadepunkteDesStandortsWerdenAngezeigt() {
        List<ChargingPoint> points = ausgewaehlterStandort.getChargingPoints();
        assertNotNull(points, "Ladepunktliste ist null");
        assertFalse(points.isEmpty(), "Der Standort hat keine Ladepunkte");
    }



    @Given("der Kunde befindet sich auf der Detailseite eines Standorts")
    public void derKundeBefindetSichAufDerDetailseiteEinesStandorts() {
        Location loc = locationManager.findById("LOC-DETAIL-1");
        if (loc == null) {
            loc = locationManager.createLocation("LOC-DETAIL-1", "Uni Campus", "Campusweg 1");
        }

        ChargingPoint cp = chargingPointManager.findById("CP-DETAIL-1");
        if (cp == null) {
            cp = chargingPointManager.createChargingPoint("CP-DETAIL-1", ChargingMode.AC, loc);
        }

        chargingPointManager.setStatus("CP-DETAIL-1", ChargingStatus.IN_BETRIEB_FREI);
    }

    @When("der Kunde einen Ladepunkt auswählt")
    public void derKundeEinenLadepunktAuswaehlt() {
        ausgewaehlterLadepunkt = chargingPointManager.findById("CP-DETAIL-1");
        assertNotNull(ausgewaehlterLadepunkt, "Ausgewählter Ladepunkt existiert nicht");
    }

    @Then("wird der aktuelle Status des Ladepunktes angezeigt \\(frei, belegt, außer Betrieb)")
    public void wirdDerAktuelleStatusDesLadepunktesAngezeigt() {
        assertNotNull(ausgewaehlterLadepunkt.getStatus(), "Status des Ladepunktes ist null");
        assertTrue(
                ausgewaehlterLadepunkt.getStatus() == ChargingStatus.IN_BETRIEB_FREI
                        || ausgewaehlterLadepunkt.getStatus() == ChargingStatus.IN_BETRIEB_BESETZT
                        || ausgewaehlterLadepunkt.getStatus() == ChargingStatus.AUSSER_BETRIEB
        );
    }
}
