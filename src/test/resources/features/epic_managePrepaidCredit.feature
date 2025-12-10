Feature: Prepaid-Guthaben verwalten
  Als Kunde möchte ich mein Prepaid-Guthaben verwalten können,
  damit ich mein Konto aufladen, mein aktuelles Guthaben einsehen
  und meine Aufladehistorie nachvollziehen kann.

  Scenario: Kunde lädt sein Konto mit einem bestimmten Betrag auf
    Given ein Kunde mit email "max@example.com" existiert und sein Konto hat Guthaben 0.00
    When der Kunde lädt sein Konto mit 20.00 auf
    Then hat das Konto ein Guthaben von 20.00
    And die Aufladehistorie enthält eine Aufladung über 20.00

  Scenario: Kunde sieht sein aktuelles Guthaben ein
    Given ein Kunde mit email "max@example.com" existiert und sein Konto hat Guthaben 15.00
    When der Kunde ruft sein Guthaben ab
    Then wird der angezeigte Kontostand 15.00 sein

  Scenario: Kunde sieht seine Aufladehistorie ein
    Given ein Kunde mit email "max@example.com" existiert
    And der Kunde hat folgende Aufladungen durchgeführt:
      | Betrag |
      | 10.00  |
      | 25.00  |
    When der Kunde seine Aufladehistorie abruft
    Then enthält die Aufladehistorie:
      | Betrag |
      | 10.00  |
      | 25.00  |
