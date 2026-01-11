Feature: Nutzung und Auslastung einsehen
  Als Betreiber möchte ich die Nutzung und Auslastung aller Ladepunkte in Echtzeit einsehen können,
  damit ich den Betrieb überwachen, Ausfälle erkennen und die Effizienz der Standorte optimieren kann.

  Scenario: Status aller Ladepunkte in Echtzeit sehen
    Given folgende Ladepunkte existieren:
      | StandortId | LadepunktId | Status               |
      | LOC-1      | CP-1        | IN_BETRIEB_FREI      |
      | LOC-1      | CP-2        | IN_BETRIEB_BESETZT   |
      | LOC-2      | CP-3        | AUSSER_BETRIEB      |
    And der Betreiber befindet sich auf dem Dashboard
    When der Betreiber die Echtzeitansicht der Ladepunkte öffnet
    Then werden die aktuellen Statusinformationen aller Ladepunkte angezeigt
    And der Betreiber sieht Ladepunkte mit Status:
      | IN_BETRIEB_FREI    |
      | IN_BETRIEB_BESETZT |
      | AUSSER_BETRIEB    |

  Scenario: Gesamtübersicht über alle Standorte einsehen
    Given folgende Standorte mit Ladepunkten existieren:
      | StandortId | LadepunktId | Status          |
      | LOC-10     | CP-10       | IN_BETRIEB_FREI |
      | LOC-20     | CP-20       | AUSSER_BETRIEB |
    And der Betreiber befindet sich im Verwaltungsbereich
    When der Betreiber die Standortübersicht aufruft
    Then wird eine Liste aller Standorte angezeigt
    And zu jedem Standort werden Anzahl und Zustand der Ladepunkte dargestellt

  Scenario: Ladevorgänge pro Standort einsehen
    Given der Betreiber ist im Admin-Dashboard eingeloggt
    And folgende Ladevorgänge existieren:
      | StandortId | LadepunktId | SessionId |
      | LOC-100    | CP-100      | S-1       |
      | LOC-100    | CP-100      | S-2       |
    And der Betreiber befindet sich im Bereich "Ladevorgänge"
    When der Betreiber wählt den Standort "LOC-100"
    Then werden alle Ladevorgänge angezeigt, die an diesem Standort durchgeführt wurden

  Scenario: Keine Ladepunkte im System vorhanden
    Given es existieren keine Ladepunkte im System
    And der Betreiber befindet sich auf dem Dashboard
    When der Betreiber die Echtzeitansicht der Ladepunkte öffnet
    Then werden keine Ladepunkte angezeigt