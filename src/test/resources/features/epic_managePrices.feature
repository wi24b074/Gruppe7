Feature: Preise verwalten
  Als Betreiber möchte ich AC- und DC-Preise pro Standort verwalten können,
  damit ich flexible und standortbezogene Tarife festlegen und anpassen kann.

  Scenario: Preise für AC/DC-Ladevorgänge pro Standort festlegen
    Given ein Standort mit der id "LOC-1" existiert
    And für den Standort "LOC-1" existieren noch keine Preise
    When der Betreiber setzt den AC-Preis auf 0.35 und den DC-Preis auf 0.55 für Standort "LOC-1"
    Then beträgt der AC-Preis des Standorts "LOC-1" 0.35
    And beträgt der DC-Preis des Standorts "LOC-1" 0.55

  Scenario: Bestehende Preise ändern
    Given ein Standort mit der id "LOC-1" existiert
    And der AC-Preis des Standorts "LOC-1" beträgt 0.35
    And der DC-Preis des Standorts "LOC-1" beträgt 0.55
    When der Betreiber ändert den AC-Preis auf 0.30 und den DC-Preis auf 0.50 für Standort "LOC-1"
    Then beträgt der AC-Preis des Standorts "LOC-1" 0.30
    And beträgt der DC-Preis des Standorts "LOC-1" 0.50