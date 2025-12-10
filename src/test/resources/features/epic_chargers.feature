Feature: Standort- und Ladepunkt verwalten
  Als Administrator möchte ich Standorte und Ladepunkte im System verwalten können,
  damit ich die Ladeinfrastruktur vollständig und korrekt abbilden, erweitern und warten kann.

  Scenario: Neuen Standort anlegen
    Given ein Administrator befindet sich im Verwaltungsbereich
    When er einen neuen Standort mit allen notwendigen Daten anlegt
    Then wird der Standort erfolgreich im System gespeichert

  Scenario: Neuen Ladepunkt zu bestehendem Standort hinzufügen
    Given ein Standort existiert im System
    When der Administrator einen neuen Ladepunkt mit den technischen Details hinzufügt
    Then ist der neue Ladepunkt dem Standort zugeordnet und im System sichtbar

  Scenario: Ladepunkt bearbeiten
    Given ein bestehender Ladepunkt ist im System vorhanden
    When der Administrator Änderungen an diesem Ladepunkt vornimmt
    Then sind die aktualisierten Daten im System gespeichert

  Scenario: Ladepunkt deaktivieren
    Given ein Ladepunkt ist im System aktiv
    When der Administrator den Ladepunkt deaktiviert
    Then erscheint der Ladepunkt als deaktiviert und ist nicht mehr nutzbar
