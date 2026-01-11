Feature: Kundenkonto anlegen
  Als Kunde möchte ich ein persönliches Kundenkonto anlegen und mich einloggen können,
  damit ich Zugang zu allen Ladefunktionen erhalte und meine Informationen aktuell halten kann.

  Scenario: Neuer Kunde registriert sich erfolgreich
    Given ein neuer Kunde befindet sich auf der Registrierungsseite
    When der Kunde registriert sich mit E-Mail "newcustomer@example.com", Name "Max Mustermann" und Passwort "password"
    Then wird ein Kundenkonto für die E-Mail "newcustomer@example.com" erstellt

  Scenario: Registrierter Kunde loggt sich ein
    Given ein registrierter Kunde mit E-Mail "registered@example.com", Name "Lena Beispiel" und Passwort "password" existiert
    And der Kunde befindet sich auf der Loginseite
    When der Kunde meldet sich mit E-Mail "registered@example.com" und Passwort "password" an
    Then wird der Kunde erfolgreich eingeloggt
    And der Kunde hat Zugriff auf sein Kundenkonto

  Scenario: Kunde kann keinen Ladevorgang an einem belegten Ladepunkt starten
    Given ein Standort mit ID "LOC-1" existiert
    And ein Ladepunkt mit ID "CP-9" am Standort "LOC-1" hat den Status "BELEGT"
    When der Kunde versucht den Ladevorgang am Ladepunkt "CP-9" zu starten
    Then hat der Ladepunkt "CP-9" den Status "BELEGT"
