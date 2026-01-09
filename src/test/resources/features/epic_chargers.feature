Feature: Standort- und Ladepunkt verwalten
  Als Administrator möchte ich Standorte und Ladepunkte im System verwalten können,
  damit ich die Ladeinfrastruktur vollständig und korrekt abbilden, erweitern und warten kann.

  Scenario: Neuen Standort anlegen
    Given ein Administrator befindet sich im Verwaltungsbereich
    When er einen neuen Standort mit id "LOC-1", name "Hauptbahnhof" und adresse "Bahnhofstraße 1" anlegt
    Then existiert ein Standort mit id "LOC-1" und name "Hauptbahnhof"

  Scenario: Neuen Ladepunkt zu bestehendem Standort hinzufügen
    Given ein Standort mit id "LOC-1" existiert
    When der Administrator fügt einen Ladepunkt mit id "CP-1" und Modus "AC" zum Standort "LOC-1" hinzu
    Then ist der Ladepunkt "CP-1" dem Standort "LOC-1" zugeordnet

  Scenario: Ladepunkt bearbeiten
    Given ein Ladepunkt "CP-1" mit Modus "AC" existiert am Standort "LOC-1"
    When der Administrator ändert den Modus des Ladepunkts "CP-1" auf "DC"
    Then hat der Ladepunkt "CP-1" den Modus "DC"

  Scenario: Ladepunkt deaktivieren
    Given ein aktiver Ladepunkt "CP-2" existiert am Standort "LOC-1"
    When der Administrator deaktiviert den Ladepunkt "CP-2"
    Then ist der Status des Ladepunkt "CP-2" "AUSSER_BETRIEB"
