Feature: Ladevorgang durchführen
  Als Kunde möchte ich einen vollständigen Ladevorgang durchführen können,
  indem ich den Ladevorgang starte, stoppe und meine Ladehistorie einsehen kann,
  damit ich mein Fahrzeug flexibel laden und meine vergangenen Ladevorgänge nachvollziehen kann.

  Scenario: Kunde startet und stoppt einen Ladevorgang
    Given ein Kunde "max@example.com" existiert
    And ein freier Ladepunkt "CP-1" existiert
    When der Kunde den Ladevorgang am Ladepunkt "CP-1" startet
    Then ist der Ladepunkt "CP-1" im Status "BELEGT"
    When der Kunde den Ladevorgang am Ladepunkt "CP-1" stoppt
    Then ist der Ladepunkt "CP-1" im Status "FREI"

  Scenario: Guthaben wird beim Ladevorgang automatisch abgerechnet
    Given ein Kunde "max@example.com" existiert und hat Guthaben 20.00
    And ein freier Ladepunkt "CP-2" existiert
    When der Kunde einen Ladevorgang am Ladepunkt "CP-2" durchführt mit Verbrauch 5.00
    Then wird das Guthaben des Kunden auf 15.00 reduziert

  Scenario: Kunde wählt einen freien Ladepunkt aus
    Given mehrere Ladepunkte existieren
    And Ladepunkt "CP-3" ist frei
    When der Kunde einen Ladepunkt auswählt
    Then wird "CP-3" als ausgewählter Ladepunkt angezeigt

