Feature: Standort- und Ladepunktverwaltung
  Als Betreiber möchte ich Standorte und Ladepunkte im System verwalten können,
  damit die Ladeinfrastruktur vollständig und korrekt abgebildet, erweitert und gewartet werden kann.

  Scenario: Neuer Standort wird angelegt
    Given es existiert kein Standort mit id "LOC-1"
    When der Betreiber legt einen neuen Standort mit id "LOC-1" und name "Hauptbahnhof" an
    Then existiert ein Standort mit id "LOC-1"
    And der Standort hat den Namen "Hauptbahnhof"

  Scenario: Ladepunktstatus sehen
    Given ein Standort "LOC-1" mit Ladepunkt "CP-1" existiert
    When der Betreiber fragt den Status von "CP-1" ab
    Then ist der Status von "CP-1" "IN_BETRIEB_FREI"

  Scenario: Ladepunkt bearbeiten
    Given ein Standort "LOC-1" mit Ladepunkt "CP-1" existiert
    When der Betreiber ändert den Modus von "CP-1" auf "DC"
    Then hat der Ladepunkt "CP-1" den Modus "DC"

  Scenario: Ladepunkt deaktivieren
    Given ein Standort "LOC-1" mit Ladepunkt "CP-1" existiert
    When der Betreiber setzt den Status von "CP-1" auf "AUSSER_BETRIEB"
    Then ist der Status von "CP-1" "AUSSER_BETRIEB"
