Feature: Standorte und Preise einsehen
  Als Kunde möchte ich alle verfügbaren Standorte, Ladepunkte und die dazugehörigen Preise einsehen können,
  damit ich einen passenden Ladeort auswählen und die Kosten vorab einschätzen kann.

  Scenario: Alle verfügbaren Standorte anzeigen
    Given der Kunde befindet sich im Bereich "Standorte"
    When der Kunde die Standortübersicht öffnet
    Then werden alle verfügbaren Standorte mit Basisinformationen angezeigt

  Scenario: Preise und Ladepunkte für einen Standort anzeigen
    Given der Kunde befindet sich in der Standortübersicht
    When der Kunde einen Standort auswählt
    Then werden die aktuellen Preise für AC- und DC-Laden angezeigt
    And die verfügbaren Ladepunkte des Standorts werden angezeigt

  Scenario: Status eines Ladepunktes anzeigen
    Given der Kunde befindet sich auf der Detailseite eines Standorts
    When der Kunde einen Ladepunkt auswählt
    Then wird der aktuelle Status des Ladepunktes angezeigt (frei, belegt, außer Betrieb)
