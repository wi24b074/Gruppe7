Feature: Standorte und Preise einsehen
  Als Kunde möchte ich alle verfügbaren Standorte, Ladepunkte und die dazugehörigen Preise einsehen können,
  damit ich einen passenden Ladeort auswählen und die Kosten vorab einschätzen kann.

  Scenario: Alle verfügbaren Standorte anzeigen
    Given folgende Standorte existieren:
      | locationId | name          | address           |
      | LOC-1      | Hauptbahnhof  | Bahnhofstraße 1   |
      | LOC-2      | Altstadt      | Marktplatz 5      |
    When der Kunde die Standortübersicht öffnet
    Then werden 2 Standorte angezeigt

  Scenario: Preise und Ladepunkte für einen Standort anzeigen
    Given ein Standort existiert mit:
      | locationId | name        | address        | acPrice | dcPrice |
      | LOC-3      | City Center | Hauptstraße 10 | 0.30    | 0.50    |
    And folgende Ladepunkte existieren für Standort "LOC-3":
      | pointId   | mode |
      | CP-AC-1  | AC   |
      | CP-DC-1  | DC   |
    When der Kunde den Standort "LOC-3" auswählt
    Then werden die Preise 0.30 für AC und 0.50 für DC angezeigt
    And es werden 2 Ladepunkte angezeigt

  Scenario: Status eines Ladepunktes anzeigen
    Given ein Standort "LOC-4" existiert
    And ein Ladepunkt "CP-1" mit Status "FREI" existiert am Standort "LOC-4"
    When der Kunde den Ladepunkt "CP-1" auswählt
    Then wird der Status "FREI" angezeigt

  Scenario: Keine Standorte im System vorhanden
    Given es existieren keine Standorte im System
    When der Kunde die Standortübersicht öffnet
    Then werden 0 Standorte angezeigt