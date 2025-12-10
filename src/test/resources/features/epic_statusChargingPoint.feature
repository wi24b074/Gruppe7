Feature: Nutzung und Auslastung einsehen
  Als Betreiber möchte ich die Nutzung und Auslastung aller Ladepunkte in Echtzeit einsehen können,
  damit ich den Betrieb überwachen, Ausfälle erkennen und die Effizienz der Standorte optimieren kann.

  Scenario: Status aller Ladepunkte in Echtzeit sehen
    Given der Betreiber befindet sich auf dem Dashboard
    When der Betreiber die Echtzeitansicht der Ladepunkte öffnet
    Then werden die aktuellen Statusinformationen aller Ladepunkte angezeigt
    And der Betreiber sieht, welche Ladepunkte belegt, frei oder gestört sind

  Scenario: Gesamtübersicht über alle Standorte einsehen
    Given der Betreiber befindet sich im Verwaltungsbereich
    When der Betreiber die Standortübersicht aufruft
    Then wird eine Liste aller Standorte angezeigt
    And zu jedem Standort werden Anzahl und Zustand der Ladepunkte dargestellt