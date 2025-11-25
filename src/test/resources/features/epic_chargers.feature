Feature: Ladepunktverwaltung
  Als Administrator möchte ich Ladepunkte verwalten und ihren Status sehen,
  damit ich den Betrieb überwachen kann.

  Scenario: Ladepunktstatus sehen
    Given ein Standort "LOC-1" mit Ladepunkt "CP-1" existiert
    When der Administrator fragt den Status von "CP-1" ab
    Then ist der Status von "CP-1" "IN_BETRIEB_FREI"

  Scenario: Ladepunkt deaktivieren
    Given ein Standort "LOC-1" mit Ladepunkt "CP-1" existiert
    When der Administrator setzt den Status von "CP-1" auf "AUSSER_BETRIEB"
    Then ist der Status von "CP-1" "AUSSER_BETRIEB"
