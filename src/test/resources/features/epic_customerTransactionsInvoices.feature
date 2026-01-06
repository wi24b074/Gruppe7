Feature: Rechnungen und Kundentransaktionen einsehen
  Als Betreiber möchte ich Rechnungen und Kundentransaktionen einsehen können,
  damit ich finanzielle Abläufe nachvollziehen, Support leisten und Auswertungen
  für Buchhaltung und Analyse durchführen kann.

  Scenario: Rechnungen pro Kunde einsehen
    Given der Betreiber ist im Admin-Dashboard eingeloggt
    And der Betreiber befindet sich im Bereich "Rechnungen"
    And ein Kunde mit ID "C-001" und E-Mail "kunde@mail.de" existiert
    And der Kunde hat mindestens eine Rechnung
    When der Betreiber den Kunden mit ID "C-001" auswählt
    Then werden alle zugehörigen Rechnungen des Kunden angezeigt
