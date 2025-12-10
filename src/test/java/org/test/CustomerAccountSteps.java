package org.test;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.example.Customer;
import org.example.CustomerManager;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerAccountSteps {

    private final CustomerManager customerManager = CustomerManager.getInstance();
    private Customer currentCustomer;
    private boolean registrationSuccess;
    private boolean loginSuccess;


    @Given("ein neuer Kunde befindet sich auf der Registrierungsseite")
    public void newCustomerOnRegistrationPage() {
    }

    @When("der Kunde seine Daten eingibt und die Registrierung bestätigt")
    public void customerRegisters() {
        String email = "newcustomer@example.com";
        String name = "Max Mustermann";
        String passwordHash = "password";

        currentCustomer = customerManager.registerCustomer(email, name,passwordHash);
        registrationSuccess = currentCustomer != null;
    }

    @Then(" wird ein neues Kundenkonto erstellt")
    public void newAccountCreated() {
        assertTrue(registrationSuccess);
        assertNotNull(currentCustomer);
    }



    @Given("ein registrierter Kunde befindet sich auf der Loginseite")
    public void registeredCustomerOnLoginPage() {
        String email = "registered@example.com";
        String name = "Lena Beispiel";
        String passwordHash = "password";

        currentCustomer = customerManager.findByEmail(email);
        if (currentCustomer == null) {
            currentCustomer = customerManager.registerCustomer(email, name, passwordHash);
        }
    }

    @When("der Kunde seine gültigen Zugangsdaten eingibt")
    public void customerEntersValidCredentials() {
        Customer loggedIn = customerManager.login(
                currentCustomer.getEmail(),
                currentCustomer.getPasswordHash()
        );

        loginSuccess = (loggedIn != null);
    }

    @Then("wird er erfolgreich eingeloggt")
    public void customerIsLoggedIn() {
        assertTrue(loginSuccess);
    }

    @Then("er erhält Zugriff auf sein Kundenkonto und die Ladefunktionen")
    public void customerGetsAccessAfterLogin() {
        Customer loggedIn = customerManager.login(currentCustomer.getEmail(), currentCustomer.getPasswordHash());
        assertEquals(currentCustomer.getEmail(), loggedIn.getEmail());
    }
}
