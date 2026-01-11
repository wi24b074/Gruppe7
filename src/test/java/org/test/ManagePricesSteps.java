package org.test;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.example.Location;
import org.example.LocationManager;
import org.example.PricingManager;
import org.example.Tariff;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ManagePricesSteps {

    private final LocationManager locationManager = LocationManager.getInstance();
    private final PricingManager pricingManager = PricingManager.getInstance();
    private Exception lastException;
    private Location currentLocation;

    @Before
    public void resetState() {
        locationManager.clear();
        lastException = null;
        currentLocation = null;
    }


    @Given("ein Standort mit der id {string} existiert")
    public void einStandortMitIdExistiert(String locId) {
        currentLocation = locationManager.findById(locId);
        if (currentLocation == null) {
            currentLocation = locationManager.createLocation(locId, locId, "");
        }
        assertNotNull(currentLocation);
    }

    @Given("für den Standort {string} existieren noch keine Preise")
    public void fuerDenStandortExistierenNochKeinePreise(String locId) {
        Location loc = locationManager.findById(locId);
        if (loc == null) {
            loc = locationManager.createLocation(locId, locId, "");
        }
        currentLocation = loc;
        assertNull(loc.getTariff(), "Für den Standort existiert bereits ein Tarif");
    }

    @When("der Betreiber setzt den AC-Preis auf {double} und den DC-Preis auf {double} für Standort {string}")
    public void derBetreiberSetztPreiseFuerStandort(double ac, double dc, String locId) {
        Location loc = locationManager.findById(locId);
        assertNotNull(loc, "Standort " + locId + " existiert nicht");

        pricingManager.setTariff(
                loc,
                BigDecimal.valueOf(ac),
                BigDecimal.valueOf(dc)
        );
    }

    @Then("beträgt der AC-Preis des Standorts {string} {double}")
    public void betraegtDerAcPreisDesStandorts(String locId, double expectedAc) {
        Location loc = locationManager.findById(locId);
        assertNotNull(loc, "Standort " + locId + " existiert nicht");

        Tariff tariff = pricingManager.getTariffForLocation(loc);
        assertNotNull(tariff, "Für den Standort ist kein Tarif gesetzt");

        assertEquals(
                0,
                tariff.getAcPricePerKWh().compareTo(BigDecimal.valueOf(expectedAc)),
                "AC-Preis stimmt nicht"
        );
    }

    @Then("beträgt der DC-Preis des Standorts {string} {double}")
    public void betraegtDerDcPreisDesStandorts(String locId, double expectedDc) {
        Location loc = locationManager.findById(locId);
        assertNotNull(loc, "Standort " + locId + " existiert nicht");

        Tariff tariff = pricingManager.getTariffForLocation(loc);
        assertNotNull(tariff, "Für den Standort ist kein Tarif gesetzt");

        assertEquals(
                0,
                tariff.getDcPricePerKWh().compareTo(BigDecimal.valueOf(expectedDc)),
                "DC-Preis stimmt nicht"
        );

    }

    @Then("wird ein Fehler ausgelöst")
    public void werdenKeinePreiseGespeichert() {
        assertNotNull(lastException, "Es wurde kein Fehler ausgelöst");
        assertInstanceOf(IllegalStateException.class, lastException);
    }


    @Given("der AC-Preis des Standorts {string} beträgt {double}")
    public void derAcPreisDesStandortsBetraegt(String locId, double ac) {
        Location loc = locationManager.findById(locId);
        if (loc == null) {
            loc = locationManager.createLocation(locId, locId, "");
        }
        currentLocation = loc;

        Tariff tariff = pricingManager.getTariffForLocation(loc);
        if (tariff == null) {
            pricingManager.setTariff(loc, BigDecimal.valueOf(ac), BigDecimal.ZERO);
        } else {
            pricingManager.changeTariff(loc, BigDecimal.valueOf(ac), tariff.getDcPricePerKWh());
        }

        Tariff updated = pricingManager.getTariffForLocation(loc);
        assertEquals(0, updated.getAcPricePerKWh().compareTo(BigDecimal.valueOf(ac)));
    }

    @Given("der DC-Preis des Standorts {string} beträgt {double}")
    public void derDcPreisDesStandortsBetraegt(String locId, double dc) {
        Location loc = locationManager.findById(locId);
        if (loc == null) {
            loc = locationManager.createLocation(locId, locId, "");
        }
        currentLocation = loc;

        Tariff tariff = pricingManager.getTariffForLocation(loc);
        if (tariff == null) {
            pricingManager.setTariff(loc, BigDecimal.ZERO, BigDecimal.valueOf(dc));
        } else {
            pricingManager.changeTariff(loc, tariff.getAcPricePerKWh(), BigDecimal.valueOf(dc));
        }

        Tariff updated = pricingManager.getTariffForLocation(loc);
        assertEquals(0, updated.getDcPricePerKWh().compareTo(BigDecimal.valueOf(dc)));
    }

    @When("der Betreiber ändert den AC-Preis auf {double} und den DC-Preis auf {double} für Standort {string}")
    public void derBetreiberAendertDiePreiseFuerStandort(double newAc, double newDc, String locId) {
        Location loc = locationManager.findById(locId);
        assertNotNull(loc, "Standort " + locId + " existiert nicht");
        currentLocation = loc;

        try {
            pricingManager.changeTariff(loc, BigDecimal.valueOf(newAc), BigDecimal.valueOf(newDc));
        } catch (Exception e) {
            lastException = e;
        }
    }
}
