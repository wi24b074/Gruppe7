package org.test;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.example.*;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
public class CustomerAccountSteps {

    private final CustomerManager customerManager = CustomerManager.getInstance();
    private final ChargingPointManager chargingPointManager = ChargingPointManager.getInstance();
    private Customer currentCustomer;
    private Customer loggedInCustomer;

    private ChargingStatus mapStatus(String status) {
        return switch (status) {
            case "FREI" -> ChargingStatus.IN_BETRIEB_FREI;
            case "BELEGT" -> ChargingStatus.IN_BETRIEB_BESETZT;
            default -> ChargingStatus.valueOf(status);
        };
    }

    @Before
    public void resetState() {
        customerManager.clear();
    }


    @Given("ein neuer Kunde befindet sich auf der Registrierungsseite")
    public void newCustomerOnRegistrationPage() {
        assertTrue(true);
    }

    @When("der Kunde registriert sich mit E-Mail {string}, Name {string} und Passwort {string}")
    public void customer_registers(String email, String name, String password) {
        currentCustomer = customerManager.registerCustomer(email, name, password);
    }

    @Then("wird ein Kundenkonto für die E-Mail {string} erstellt")
    public void customer_account_created(String email) {
        assertNotNull(currentCustomer);
        assertEquals(email, currentCustomer.getEmail());
    }

    @Given("ein registrierter Kunde mit E-Mail {string}, Name {string} und Passwort {string} existiert")
    public void registered_customer_exists(String email, String name, String password) {
        currentCustomer = customerManager.findByEmail(email);
        if (currentCustomer == null) {
            currentCustomer = customerManager.registerCustomer(email, name, password);
        }
        assertNotNull(currentCustomer);
    }

    @Given("der Kunde befindet sich auf der Loginseite")
    public void customer_on_login_page() {
        assertTrue(true);
    }

    @When("der Kunde meldet sich mit E-Mail {string} und Passwort {string} an")
    public void customer_logs_in(String email, String password) {
        loggedInCustomer = customerManager.login(email, password);
    }

    @Then("wird der Kunde erfolgreich eingeloggt")
    public void customer_logged_in_successfully() {
        assertNotNull(loggedInCustomer);
    }

    @Then("der Kunde hat Zugriff auf sein Kundenkonto")
    public void customer_has_access_to_account() {
        assertEquals(currentCustomer.getEmail(), loggedInCustomer.getEmail());
    }

    @And("ein Ladepunkt mit ID {string} am Standort {string} hat den Status {string}")
    public void einLadepunktMitIDAmStandortHatDenStatus(String pointId, String locId, String status) {
        Location loc = LocationManager.getInstance().findById(locId);
        if (loc == null) {
            loc = LocationManager.getInstance().createLocation(locId, "Test Location", "Test Adresse");
        }

        ChargingPoint cp = chargingPointManager.findById(pointId);
        if (cp == null) {
            cp = chargingPointManager.createChargingPoint(pointId, ChargingMode.AC, loc);
        }
        cp.setStatus(mapStatus(status));
    }
}