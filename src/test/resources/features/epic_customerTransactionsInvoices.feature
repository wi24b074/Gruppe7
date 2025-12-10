Feature: Rechnungen und Kundentransaktionen einsehen
  Als Betreiber möchte ich Rechnungen und Kundentransaktionen einsehen können,
  damit ich finanzielle Abläufe nachvollziehen, Support leisten und Auswertungen
  für Buchhaltung und Analyse durchführen kann.

  Scenario: Rechnungen pro Kunde einsehen
    Given der Betreiber ist im Admin-Dashboard eingeloggt
    And der Betreiber befindet sich im Bereich "Rechnungen"
    When der Betreiber einen bestimmten Kunden auswählt
    Then werden alle zugehörigen Rechnungen des Kunden angezeigt

