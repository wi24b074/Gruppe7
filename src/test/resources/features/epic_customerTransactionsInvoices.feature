Feature: Rechnungen und Kundentransaktionen einsehen
  Als Betreiber möchte ich Rechnungen und Kundentransaktionen einsehen können,
  damit ich finanzielle Abläufe nachvollziehen, Support leisten und Auswertungen
  für Buchhaltung und Analyse durchführen kann.

  Scenario: Rechnungen pro Kunde einsehen
    Given der Betreiber ist in dem Admin-Dashboard eingeloggt
    And der Betreiber befindet sich in dem Bereich "Rechnungen"
    And ein Kunde mit ID "C-001" und E-Mail "kunde@mail.de" existiert
    And der Kunde hat mindestens eine Rechnung
    When der Betreiber den Kunden mit ID "C-001" auswählt
    Then werden alle zugehörigen Rechnungen des Kunden angezeigt

  Scenario: Kunde hat noch keine Rechnungen
    Given der Betreiber ist in dem Admin-Dashboard eingeloggt
    And der Betreiber befindet sich in dem Bereich "Rechnungen"
    And ein Kunde mit ID "C-002" und E-Mail "leer@mail.de" existiert
    And der Kunde hat keine Rechnungen
    When der Betreiber den Kunden mit ID "C-002" auswählt
    Then werden keine Rechnungen für den Kunden angezeigt
