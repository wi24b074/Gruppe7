Feature: Rechnungen und Historie einsehen
  Als Kunde möchte ich meine Rechnungen, meine Ladehistorie und meine Transaktionen einsehen können,
  damit ich meine Kosten nachvollziehen und vollständige Transparenz über meine Ladeaktivitäten habe.

  Scenario: Kunde sieht seine vollständige Rechnung mit allen Ladevorgängen
    Given ein Kunde "max@example.com" hat mehrere Ladevorgänge abgeschlossen
    And es existiert eine Rechnung für diesen Kunden
    When der Kunde seine Rechnung einsehen möchte
    Then wird die vollständige Rechnung inklusive aller Ladevorgänge angezeigt

  Scenario: Kunde sieht seine vergangenen Ladevorgänge
    Given ein Kunde "max@example.com" hat vergangene Ladevorgänge
    When der Kunde seine Ladehistorie öffnet
    Then werden alle vergangenen Ladevorgänge chronologisch angezeigt

  Scenario: Kunde sieht, wie sich sein Guthaben nach jeder Nutzung verändert hat
    Given ein Kunde "max@example.com" hat mehrere Ladevorgänge durchgeführt
    And sein Guthaben hat sich mehrfach verändert
    When der Kunde seine Guthabenhistorie einsehen möchte
    Then sieht er alle Transaktionen inklusive Guthaben vor und nach jedem Vorgang
