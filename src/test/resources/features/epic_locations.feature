Feature: Standortverwaltung
  Als Administrator möchte ich Standorte und ihre Ladepunkte verwalten,
  damit das Elektrotankstellennetz korrekt abgebildet wird.

  Scenario: Neue Standorte anlegen
    Given es existiert kein Standort mit id "LOC-1"
    When der Administrator legt einen neuen Standort mit id "LOC-1" und name "Hauptbahnhof" an
    Then existiert ein Standort mit id "LOC-1"
    And der Standort hat den Namen "Hauptbahnhof"

  Scenario: Neue Ladepunkte zu Standort hinzufügen
    Given ein Standort mit id "LOC-1" existiert
    When der Administrator fügt einen Ladepunkt mit id "CP-1" und Modus "AC" zu Standort "LOC-1" hinzu
    Then existiert ein Ladepunkt mit id "CP-1" an Standort "LOC-1"
    And der Ladepunkt hat den Modus "AC"
