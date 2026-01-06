Feature: Rechnungen und Historie einsehen
  Als Kunde möchte ich meine Rechnungen, meine Ladehistorie und meine Transaktionen einsehen können,
  damit ich meine Kosten nachvollziehen und vollständige Transparenz über meine Ladeaktivitäten habe.

  Scenario: Kunde sieht seine vollständige Rechnung mit allen Ladevorgängen
    Given ein Kunde mit E-Mail "max@example.com" existiert
    And der Kunde hat folgende abgeschlossene Ladevorgänge:
      | sessionId |
      | S1        |
      | S2        |
    And es existiert eine Rechnung für den Kunden
    When der Kunde seine Rechnung einsehen möchte
    Then wird die Rechnung mit 2 Ladevorgängen angezeigt

  Scenario: Kunde sieht seine vergangenen Ladevorgänge
    Given ein Kunde mit E-Mail "max@example.com" existiert
    And der Kunde hat folgende vergangene Ladevorgänge:
      | sessionId |
      | S1        |
      | S2        |
      | S3        |
    When der Kunde seine Ladehistorie öffnet
    Then werden 3 vergangene Ladevorgänge angezeigt

  Scenario: Kunde sieht seine Guthabenhistorie
    Given ein Kunde mit E-Mail "max@example.com" existiert
    And der Kunde hat folgende Guthabenänderungen:
      | vorher | nachher |
      | 100.0  | 85.0    |
      | 85.0   | 65.0    |
      | 65.0   | 60.0    |
    When der Kunde seine Guthabenhistorie einsehen möchte
    Then werden alle Guthabenänderungen angezeigt