Feature: Ladevorgang durchführen
  Als Kunde möchte ich einen vollständigen Ladevorgang durchführen können,
  indem ich den Ladevorgang starte, stoppe und meine Ladehistorie einsehen kann,
  damit ich mein Fahrzeug flexibel laden und meine vergangenen Ladevorgänge
  nachvollziehen kann.

  Scenario: Kunde startet und stoppt einen Ladevorgang
    Given ein Standort mit ID "LOC-1" existiert
    And ein Kunde mit der E-Mail "max@example.com" existiert
    And ein Ladepunkt mit ID "CP-1" am Standort "LOC-1" ist frei
    When der Kunde startet den Ladevorgang am Ladepunkt "CP-1"
    Then hat der Ladepunkt "CP-1" den Status "BELEGT"
    When der Kunde stoppt den Ladevorgang am Ladepunkt "CP-1"
    Then hat der Ladepunkt "CP-1" den Status "FREI"



  Scenario: Guthaben wird beim Ladevorgang automatisch abgerechnet
    Given ein Kunde mit der E-Mail "max@example.com" existiert und hat ein Guthaben von 20.00
    And ein Ladepunkt mit ID "CP-2" ist frei
    When der Kunde führt einen Ladevorgang am Ladepunkt "CP-2" mit Kosten von 5.00 durch
    Then beträgt das Guthaben des Kunden 15.00

  Scenario: Kunde wählt einen freien Ladepunkt aus
    Given ein Standort mit ID "LOC-1" existiert
    And folgende Ladepunkte am Standort "LOC-1" bestehen:
      | Ladepunkt | Status |
      | CP-1      | FREI   |
      | CP-2      | BELEGT |
      | CP-3      | FREI   |
    When der Kunde wählt den Ladepunkt "CP-3" aus
    Then ist der ausgewählte Ladepunkt "CP-3"

