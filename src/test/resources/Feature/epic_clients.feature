Feature: Kundenverwaltung
  Als System möchte ich Kundenkonten anlegen und Guthaben verwalten,
  damit Kunden laden können.

  Scenario: Kunde registrieren und Konto prüfen
    Given es existiert kein Kunde mit email "max@example.com"
    When der Benutzer registriert sich mit name "Max Mustermann" und email "max@example.com"
    Then existiert ein Kunde mit email "max@example.com"
    And das Kundenkonto hat ein Guthaben von 0

  Scenario: Konto aufladen (TopUp)
    Given ein Kunde mit email "max@example.com" existiert und sein Konto hat Guthaben 0
    When der Kunde lädt sein Konto mit 10.00 auf
    Then hat das Konto ein Guthaben von 10.00
    And die Aufladehistorie enthält eine Aufladung über 10.00
