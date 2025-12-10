Feature: Rechnungen und Kundentransaktionen einsehen
  Als Betreiber möchte ich Rechnungen und Kundentransaktionen einsehen können,
  damit ich finanzielle Abläufe nachvollziehen, Support leisten und Auswertungen
  für Buchhaltung und Analyse durchführen kann.

  Scenario: Rechnungen pro Kunde einsehen
    Given der Betreiber ist im Admin-Dashboard eingeloggt
    And der Betreiber befindet sich im Bereich "Rechnungen"
    When der Betreiber einen bestimmten Kunden auswählt
    Then werden alle zugehörigen Rechnungen des Kunden angezeigt

  Scenario: Ladevorgänge pro Kunde einsehen
    Given der Betreiber ist im Admin-Dashboard eingeloggt
    And der Betreiber befindet sich im Bereich "Ladevorgänge"
    When der Betreiber einen Kunden auswählt
    Then werden alle zugehörigen Ladevorgänge dieses Kunden angezeigt

  Scenario: Ladevorgänge pro Standort einsehen
    Given der Betreiber ist im Admin-Dashboard eingeloggt
    And der Betreiber befindet sich im Bereich "Ladevorgänge"
    When der Betreiber einen Standort auswählt
    Then werden alle Ladevorgänge angezeigt, die an diesem Standort durchgeführt wurden
