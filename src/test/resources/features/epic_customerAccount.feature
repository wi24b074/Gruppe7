Feature: Kundenkonto anlegen
  Als Kunde möchte ich ein persönliches Kundenkonto anlegen und mich
  einloggen können, damit ich Zugang zu allen Ladefunktionen
  erhalte und meine Informationen aktuell halten kann.

  Scenario: Neuer Kunde registriert sich erfolgreich
    Given ein neuer Kunde befindet sich auf der Registrierungsseite
    When der Kunde seine Daten eingibt und die Registrierung bestätigt
    Then wird ein neues Kundenkonto erstellt
    And der Kunde erhält Zugriff auf seine persönlichen Kontofunktionen

  Scenario: Registrierter Kunde loggt sich ein
    Given ein registrierter Kunde befindet sich auf der Loginseite
    When der Kunde seine gültigen Zugangsdaten eingibt
    Then wird er erfolgreich eingeloggt
    And er erhält Zugriff auf sein Kundenkonto und die Ladefunktionen