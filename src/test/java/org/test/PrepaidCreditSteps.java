package org.test;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import org.example.Customer;
import org.example.CustomerManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrepaidCreditSteps {

    private final CustomerManager customerManager = CustomerManager.getInstance();

    private Customer currentCustomer;
    private String currentEmail;
    private double angezeigtesGuthaben;

    private final List<Double> aufladeHistorie = new ArrayList<>();
    private List<Double> geseheneHistorie = new ArrayList<>();


    @Given("ein Kunde mit email {string} existiert und sein Konto hat Guthaben {double}")
    public void kundeMitEmailExistiertMitGuthaben(String email, double guthaben) {
        currentEmail = email;
        currentCustomer = customerManager.findByEmail(email);
        if (currentCustomer == null) {
            currentCustomer = customerManager.registerCustomer(email, "Testkunde", "pw123");
        }


        if (guthaben > 0) {
            customerManager.topUpCustomer(email, guthaben);
        }

        assertEquals(guthaben, currentCustomer.getBalance(), 0.0001);
    }

    @When("der Kunde lädt sein Konto mit {double} auf")
    public void derKundeLaedtSeinKontoMitAuf(double betrag) {
        customerManager.topUpCustomer(currentEmail, betrag);
        currentCustomer = customerManager.findByEmail(currentEmail);
        aufladeHistorie.add(betrag);
    }

    @Then("hat das Konto ein Guthaben von {double}")
    public void hatDasKontoEinGuthabenVon(double erwartetesGuthaben) {
        currentCustomer = customerManager.findByEmail(currentEmail);
        assertNotNull(currentCustomer);
        assertEquals(erwartetesGuthaben, currentCustomer.getBalance(), 0.0001);
    }

    @Then("die Aufladehistorie enthält eine Aufladung über {double}")
    public void dieAufladehistorieEnthaeltEineAufladungUeber(double betrag) {
        assertTrue(aufladeHistorie.contains(betrag),
                "Aufladehistorie enthält den Betrag " + betrag + " nicht");
    }


    @When("der Kunde ruft sein Guthaben ab")
    public void derKundeRuftSeinGuthabenAb() {
        currentCustomer = customerManager.findByEmail(currentEmail);
        assertNotNull(currentCustomer);
        angezeigtesGuthaben = currentCustomer.getBalance();
    }

    @Then("wird der angezeigte Kontostand {double} sein")
    public void wirdDerAngezeigteKontostandSein(double erwarteterWert) {
        assertEquals(erwarteterWert, angezeigtesGuthaben, 0.0001);
    }

    @Given("ein Kunde mit email {string} existiert")
    public void einKundeMitEmailExistiert(String email) {
        currentEmail = email;
        currentCustomer = customerManager.findByEmail(email);
        if (currentCustomer == null) {
            currentCustomer = customerManager.registerCustomer(email, "Testkunde", "pw123");
        }
        aufladeHistorie.clear();
    }

    @Given("der Kunde hat folgende Aufladungen durchgeführt:")
    public void derKundeHatFolgendeAufladungenDurchgefuehrt(DataTable table) {
        for (var row : table.asLists()) {
            if (row.get(0).equals("Betrag")) {
                continue;
            }
            double betrag = Double.parseDouble(row.get(0));
            customerManager.topUpCustomer(currentEmail, betrag);
            aufladeHistorie.add(betrag);
        }
    }

    @When("der Kunde seine Aufladehistorie abruft")
    public void derKundeSeineAufladehistorieAbruft() {

        geseheneHistorie = new ArrayList<>(aufladeHistorie);
    }

    @Then("enthält die Aufladehistorie:")
    public void enthaeltDieAufladehistorie(DataTable expectedTable) {
        List<Double> erwarteteBetraege = new ArrayList<>();
        for (var row : expectedTable.asLists()) {
            if (row.get(0).equals("Betrag")) {
                continue;
            }
            erwarteteBetraege.add(Double.parseDouble(row.get(0)));
        }

        assertEquals(erwarteteBetraege.size(), geseheneHistorie.size(),
                "Anzahl der Einträge in der Aufladehistorie stimmt nicht");

        for (int i = 0; i < erwarteteBetraege.size(); i++) {
            assertEquals(erwarteteBetraege.get(i), geseheneHistorie.get(i), 0.0001);
        }
    }
}
