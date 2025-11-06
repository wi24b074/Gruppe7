Feature: Manage Locations and Charging Points
  As an administrator
  I want to manage locations and charging points
  in order to maintain a complete overview and control of the charging system.

  Background:
    Given a new ChargingSystem

  Scenario: Create New Location
    When the administrator creates a new location with the name "Vienna Central"
    Then the location "Vienna Central" is saved in the system

  Scenario: Add Charging Points to a Location
    Given an existing location "Vienna Central"
    When the administrator adds a new charging point with the ID "CP-01"
    Then the charging point "CP-01" is linked to the location "Vienna Central"

  Scenario: Edit or Deactivate Charging Points
    Given an existing charging point "CP-01"
    When the administrator edits or deactivates the charging point
    Then the status of the charging point is updated accordingly in the system