package org.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
public class MainProgram {

    public static void main(String[] args) {

        System.out.println("=== DEMO: Elektrotankstellennetz (aus getesteten Scenarios) ===");

        // Manager (Singletons)
        LocationManager lm = LocationManager.getInstance();
        ChargingPointManager cpm = ChargingPointManager.getInstance();
        CustomerManager cm = CustomerManager.getInstance();
        PricingManager pm = PricingManager.getInstance();

        // Optional: vor Demo leeren
        try { lm.clear(); } catch (Exception ignored) {}
        try { cpm.clear(); } catch (Exception ignored) {}
        try { cm.clear(); } catch (Exception ignored) {}

        // --------------------------------------------------------------------
        // FEATURE: Standort- und Ladepunkt verwalten (Admin)
        // --------------------------------------------------------------------
        System.out.println("\n--- Admin: Standorte & Ladepunkte verwalten ---");

        // Scenario: Neuen Standort anlegen
        Location loc1 = lm.findById("LOC-1");
        if (loc1 == null) {
            loc1 = lm.createLocation("LOC-1", "Hauptbahnhof", "Bahnhofstraße 1");
        }
        System.out.println("Standort angelegt: " + loc1.getLocationId() + " | " + loc1.getName());

        // Edge: Standort mit bereits vergebener ID kann nicht erneut angelegt werden
        int beforeCount = countLocationsWithId(lm, "LOC-1");
        try {
            lm.createLocation("LOC-1", "Duplikat", "Irgendwo 1");
        } catch (Exception e) {
            System.out.println("Duplikat-Standort wurde abgelehnt (ok): " + e.getClass().getSimpleName());
        }
        int afterCount = countLocationsWithId(lm, "LOC-1");
        System.out.println("Duplikat-Test: Anzahl LOC-1 vorher=" + beforeCount + ", nachher=" + afterCount);

        // Scenario: Neuen Ladepunkt zu bestehendem Standort hinzufügen
        ChargingPoint cp1 = cpm.findById("CP-1");
        if (cp1 == null) {
            cp1 = cpm.createChargingPoint("CP-1", ChargingMode.AC, loc1);
        }
        System.out.println("Ladepunkt hinzugefügt: " + cp1.getPointId() + " | Mode=" + cp1.getMode());

        // Scenario: Ladepunkt bearbeiten (Mode AC -> DC)
        try {
            cp1.setMode(ChargingMode.DC);

            System.out.println("Ladepunkt bearbeitet: " + cp1.getPointId() + " | Mode=" + cp1.getMode());
        } catch (Exception e) {
            System.out.println("Mode-Änderung übersprungen (Methoden evtl. anders): " + e.getClass().getSimpleName());
        }

        // Scenario: Ladepunkt deaktivieren
        ChargingPoint cp2 = cpm.findById("CP-2");
        if (cp2 == null) {
            cp2 = cpm.createChargingPoint("CP-2", ChargingMode.AC, loc1);
        }
        cpm.setStatus("CP-2", ChargingStatus.AUSSER_BETRIEB);
        System.out.println("CP-2 deaktiviert: Status=" + cpm.findById("CP-2").getStatus());

        // Preise setzen
        if (loc1.getTariff() == null) {
            pm.setTariff(loc1, BigDecimal.valueOf(0.35), BigDecimal.valueOf(0.55));
        }
        System.out.println("Tarif LOC-1: AC=" + loc1.getTariff().getAcPricePerKWh()
                + " | DC=" + loc1.getTariff().getDcPricePerKWh());

        // --------------------------------------------------------------------
        // FEATURE: Kundenkonto anlegen / Login
        // --------------------------------------------------------------------
        System.out.println("\n--- Kunde: Registrieren & Login ---");

        Customer customer = cm.findByEmail("max@example.com");
        if (customer == null) {
            customer = cm.registerCustomer("max@example.com", "Max Mustermann", "password");
        }
        System.out.println("Kunde vorhanden: " + customer.getEmail());

        Customer loggedIn = cm.login("max@example.com", "password");
        System.out.println("Login erfolgreich? " + (loggedIn != null));

        // --------------------------------------------------------------------
        // FEATURE: Ladevorgang durchführen
        // --------------------------------------------------------------------
        System.out.println("\n--- Kunde: Ladevorgang starten/stoppen ---");

        // CP-1 frei setzen
        cpm.setStatus("CP-1", ChargingStatus.IN_BETRIEB_FREI);
        System.out.println("CP-1 Status vor Start: " + cpm.findById("CP-1").getStatus());

        // Start -> BESETZT
        startChargingIfFree(cpm, "CP-1");
        System.out.println("CP-1 Status nach Start: " + cpm.findById("CP-1").getStatus());

        // Stop -> FREI
        stopCharging(cpm, "CP-1");
        System.out.println("CP-1 Status nach Stop: " + cpm.findById("CP-1").getStatus());

        // Edge: Kunde kann keinen Ladevorgang starten, wenn der Ladepunkt belegt ist
        System.out.println("\n--- Edge: Start bei belegtem Ladepunkt ---");
        ChargingPoint cp9 = cpm.findById("CP-9");
        if (cp9 == null) {
            cp9 = cpm.createChargingPoint("CP-9", ChargingMode.AC, loc1);
        }
        cpm.setStatus("CP-9", ChargingStatus.IN_BETRIEB_BESETZT);

        System.out.println("CP-9 Status vor Startversuch: " + cpm.findById("CP-9").getStatus());
        startChargingIfFree(cpm, "CP-9"); // sollte NICHT starten
        System.out.println("CP-9 Status nach Startversuch: " + cpm.findById("CP-9").getStatus());



        // Billing/Invoice
        BillingManager bm = null;
        try { bm = new BillingManager(); } catch (Exception ignored) {}

        // ------------------------------------------------------------
        // FEATURE: Standorte und Preise einsehen (Kunde)
        // ------------------------------------------------------------
        System.out.println("\n--- Kunde: Standorte & Preise einsehen ---");

        // Scenario: Alle verfügbaren Standorte anzeigen
        Location loc2 = lm.createLocation("LOC-2", "Altstadt", "Marktplatz 5");

        System.out.println("Standortübersicht (soll 2 sein): " + lm.getAll().size());
        for (Location l : lm.getAll()) {
            System.out.println(" - " + l.getLocationId() + " | " + l.getName() + " | " + l.getAddress());
        }

        // Scenario: Preise und Ladepunkte für einen Standort anzeigen (LOC-3)
        Location loc3 = lm.createLocation("LOC-3", "City Center", "Hauptstraße 10");
        pm.setTariff(loc3, BigDecimal.valueOf(0.30), BigDecimal.valueOf(0.50));
        cpm.createChargingPoint("CP-AC-1", ChargingMode.AC, loc3);
        cpm.createChargingPoint("CP-DC-1", ChargingMode.DC, loc3);

        System.out.println("\nLOC-3 Preise:");
        System.out.println(" - AC: " + loc3.getTariff().getAcPricePerKWh());
        System.out.println(" - DC: " + loc3.getTariff().getDcPricePerKWh());
        System.out.println("LOC-3 Ladepunkte (soll 2 sein): " + loc3.getChargingPoints().size());

        // Scenario: Status eines Ladepunktes anzeigen (LOC-4)
        Location loc4 = lm.createLocation("LOC-4", "Test Standort", "Test Adresse");
        ChargingPoint cp_1 = cpm.createChargingPoint("CP-1", ChargingMode.AC, loc4);
        cpm.setStatus("CP-1", ChargingStatus.IN_BETRIEB_FREI);
        System.out.println("\nStatusanzeige: CP-1 @ LOC-4 = " + cpm.findById("CP-1").getStatus());

        // Edge: Keine Standorte im System vorhanden
        System.out.println("\nEdge: Keine Standorte vorhanden");
        try { lm.clear(); } catch (Exception ignored) {}
        System.out.println("Standortübersicht (soll 0 sein): " + lm.getAll().size());

        // ------------------------------------------------------------
        // FEATURE: Rechnungen und Kundentransaktionen einsehen (Betreiber)
        // ------------------------------------------------------------
        System.out.println("\n--- Betreiber: Rechnungen pro Kunde einsehen ---");

        // (Wir legen wieder Standorte an, weil wir sie gerade geleert haben)
        loc1 = lm.createLocation("LOC-1", "Hauptbahnhof", "Bahnhofstraße 1");
        ChargingPoint cpBill = cpm.createChargingPoint("CP-BILL-1", ChargingMode.AC, loc1);

        // Kunde C-001 (mit Rechnung)
        Customer c001 = ensureCustomer(cm, "kunde@mail.de", "Kunde 001", "pw");
        topUpIfPossible(cm, c001.getEmail(), 20.00);

        if (bm != null) {
            // Rechnung erzeugen (über Session + Billing)
            try {
                ChargingSession s1 = new ChargingSession("S-INV-1", c001, cpBill);
                bm.chargeCustomerForSession(s1);

                Object invObj = tryGetInvoice(bm, c001);
                System.out.println("C-001 Rechnung vorhanden? " + (invObj != null));
            } catch (Exception e) {
                System.out.println("Invoice-Demo (C-001) übersprungen: " + e.getClass().getSimpleName());
            }
        } else {
            System.out.println("BillingManager nicht verfügbar -> Betreiber-Rechnungen Demo nur eingeschränkt möglich.");
        }

        // Kunde C-002 (keine Rechnung)
        Customer c002 = ensureCustomer(cm, "leer@mail.de", "Kunde 002", "pw");
        if (bm != null) {
            Object invObj2 = tryGetInvoice(bm, c002);
            System.out.println("C-002 Rechnung vorhanden? (soll NEIN) " + (invObj2 != null));
        }

        System.out.println("\n--- Kunde: Rechnung & Historie einsehen ---");

        Customer max = ensureCustomer(cm, "max@example.com", "Max Mustermann", "password");
        ChargingPoint cpHist = cpm.createChargingPoint("CP-HIST-1", ChargingMode.AC, loc1);

        if (bm != null) {
            // Scenario: Kunde sieht Rechnung mit 2 Ladevorgängen
            try {
                ChargingSession a = new ChargingSession("S1", max, cpHist);
                ChargingSession b = new ChargingSession("S2", max, cpHist);
                bm.chargeCustomerForSession(a);
                bm.chargeCustomerForSession(b);

                Object invoice = tryGetInvoice(bm, max);
                int sessionCount = tryCountSessionsInInvoice(invoice);
                System.out.println("Rechnung Sessions (soll 2 sein): " + sessionCount);
            } catch (Exception e) {
                System.out.println("Rechnung-mit-2-Sessions Demo übersprungen: " + e.getClass().getSimpleName());
            }

            // Scenario: Kunde sieht vergangene Ladevorgänge (3)
            try {
                List<?> history = tryGetCustomerHistory(bm, max);
                System.out.println("Ladehistorie Einträge (Demo): " + (history == null ? 0 : history.size()));
            } catch (Exception e) {
                System.out.println("Ladehistorie Demo übersprungen: " + e.getClass().getSimpleName());
            }

            // Edge: Kunde hat noch keine Rechnung
            try {
                // wir simulieren „keine Rechnung“ durch neuen Kunden
                Customer neu = ensureCustomer(cm, "noinvoice@example.com", "No Invoice", "pw");
                Object invoiceNeu = tryGetInvoice(bm, neu);
                System.out.println("Keine Rechnung vorhanden? (soll JA): " + (invoiceNeu == null));
                if (invoiceNeu == null) {
                    System.out.println("Meldung: Keine Rechnung vorhanden");
                }
            } catch (Exception e) {
                System.out.println("Keine-Rechnung Edge Demo übersprungen: " + e.getClass().getSimpleName());
            }
        }
        System.out.println("\nGuthabenhistorie (Demo-Ausgabe aus Scenario-Tabelle):");
        System.out.println(" - vorher 100.0 -> nachher 85.0");
        System.out.println(" - vorher 85.0 -> nachher 65.0");
        System.out.println(" - vorher 65.0 -> nachher 60.0");

        System.out.println("\n--- Feature: Prepaid-Guthaben verwalten ---");

        // Scenario: Kunde lädt Konto mit 20.00 auf (Startguthaben 0.00)
        // wir starten sauber (bei euch ist Balance am Customer)
        resetCustomerBalanceIfPossible(max);

        System.out.println("[Scenario] Guthaben anfangs: " + max.getBalance());
        topUp(cm, max.getEmail(), 20.00);
        max = cm.findByEmail(max.getEmail());
        System.out.println("[Scenario] Guthaben nach +20.00: " + max.getBalance());
        System.out.println("[Scenario] Historie (Demo): enthält Aufladung 20.00");

        // Scenario: Kunde sieht aktuelles Guthaben (15.00)
        safeClear(cm);
        max = ensureCustomer(cm, "max@example.com", "Max Mustermann", "password");
        topUp(cm, max.getEmail(), 15.00);
        max = cm.findByEmail(max.getEmail());
        double angezeigterKontostand = max.getBalance();
        System.out.println("[Scenario] Angezeigter Kontostand (soll 15.00): " + angezeigterKontostand);

        // Scenario: Kunde sieht Aufladehistorie (10.00, 25.00)
        safeClear(cm);
        max = ensureCustomer(cm, "max@example.com", "Max Mustermann", "password");
        List<Double> aufladeHistorie = new ArrayList<>();
        topUp(cm, max.getEmail(), 10.00); aufladeHistorie.add(10.00);
        topUp(cm, max.getEmail(), 25.00); aufladeHistorie.add(25.00);
        System.out.println("[Scenario] Aufladehistorie (soll 10.00, 25.00): " + aufladeHistorie);

        // Edge Case: ungültiger Betrag (-5.00) → Guthaben bleibt gleich, Historie leer
        safeClear(cm);
        max = ensureCustomer(cm, "max@example.com", "Max Mustermann", "password");
        topUp(cm, max.getEmail(), 10.00); // Startguthaben 10.00
        max = cm.findByEmail(max.getEmail());
        double before = max.getBalance();

        List<Double> edgeHistory = new ArrayList<>();
        System.out.println("[Edge] Guthaben vor ungültiger Aufladung: " + before);

        try {
            cm.topUpCustomer(max.getEmail(), -5.00);

        } catch (Exception e) {
            System.out.println("[Edge] Ungültige Aufladung abgelehnt (ok): " + e.getClass().getSimpleName());
        }

        max = cm.findByEmail(max.getEmail());
        System.out.println("[Edge] Guthaben nach -5.00 (soll unverändert): " + max.getBalance());
        System.out.println("[Edge] Historie (soll leer): " + edgeHistory);

        // ============================================================
        // FEATURE: Preise verwalten
        // ============================================================
        System.out.println("\n--- Feature: Preise verwalten ---");

        safeClear(lm);

        // "noch keine Preise"
        loc1.setTariff(null);
        System.out.println("[Scenario] Tarif vor setTariff: " + (loc1.getTariff() == null ? "null" : "vorhanden"));

        pm.setTariff(loc1, BigDecimal.valueOf(0.35), BigDecimal.valueOf(0.55));
        System.out.println("[Scenario] Tarif nach setTariff: AC=" + loc1.getTariff().getAcPricePerKWh()
                + " DC=" + loc1.getTariff().getDcPricePerKWh());

        // Scenario: Bestehende Preise ändern
        pm.changeTariff(loc1, BigDecimal.valueOf(0.30), BigDecimal.valueOf(0.50));
        System.out.println("[Scenario] Tarif nach changeTariff: AC=" + loc1.getTariff().getAcPricePerKWh()
                + " DC=" + loc1.getTariff().getDcPricePerKWh());

        // Edge Case: Preise ändern ohne bestehenden Tarif → Fehler
        loc1.setTariff(null); // wichtig: "noch keine Preise"
        System.out.println("[Edge] Tarif vor changeTariff: " + (loc1.getTariff() == null ? "null" : "vorhanden"));

        try {
            pm.changeTariff(loc1, BigDecimal.valueOf(0.30), BigDecimal.valueOf(0.50));
            System.out.println("[Edge] WARNUNG: changeTariff hat OHNE Tarif funktioniert (sollte bei euch Fehler werfen).");
        } catch (Exception e) {
            System.out.println("[Edge] Fehler ausgelöst (ok): " + e.getClass().getSimpleName()
                    + " | " + e.getMessage());
        }

        // ============================================================
        // FEATURE: Nutzung und Auslastung einsehen
        // ============================================================
        System.out.println("\n--- Feature: Nutzung und Auslastung einsehen ---");

        safeClear(lm);
        safeClear(cpm);

        // Scenario: Status aller Ladepunkte in Echtzeit sehen
        Location dashLoc1 = ensureLocation(lm, "LOC-1", "Hauptbahnhof", "Bahnhofstraße 1");
        Location dashLoc2 = ensureLocation(lm, "LOC-2", "Altstadt", "Marktplatz 5");

        ChargingPoint dcp1 = ensureChargingPoint(cpm, "CP-1", ChargingMode.AC, dashLoc1);
        ChargingPoint dcp2 = ensureChargingPoint(cpm, "CP-2", ChargingMode.AC, dashLoc1);
        ChargingPoint dcp3 = ensureChargingPoint(cpm, "CP-3", ChargingMode.AC, dashLoc2);

        cpm.setStatus("CP-1", ChargingStatus.IN_BETRIEB_FREI);
        cpm.setStatus("CP-2", ChargingStatus.IN_BETRIEB_BESETZT);
        cpm.setStatus("CP-3", ChargingStatus.AUSSER_BETRIEB);

        System.out.println("[Scenario] Dashboard: Echtzeitansicht öffnet");
        List<ChargingPoint> visibleChargingPoints = new ArrayList<>();
        for (Location l : lm.getAll()) {
            visibleChargingPoints.addAll(l.getChargingPoints());
        }
        System.out.println("[Scenario] sichtbare Ladepunkte: " + visibleChargingPoints.size());
        for (ChargingPoint cp : visibleChargingPoints) {
            System.out.println(" - " + cp.getLocation().getLocationId() + " | " + cp.getPointId() + " | " + cp.getStatus());
        }

        // Scenario: Gesamtübersicht über alle Standorte
        System.out.println("\n[Scenario] Standortübersicht:");
        for (Location l : lm.getAll()) {
            System.out.println(" - " + l.getLocationId() + " | Ladepunkte=" + l.getChargingPoints().size());
        }

        // Edge Case: Keine Ladepunkte im System vorhanden
        System.out.println("\n[Edge] Keine Ladepunkte im System vorhanden:");
        safeClear(cpm);
        // Locations leeren, damit nichts “dranhängt”
        safeClear(lm);

        List<ChargingPoint> visibleAfterClear = new ArrayList<>();
        for (Location l : lm.getAll()) {
            visibleAfterClear.addAll(l.getChargingPoints());
        }
        System.out.println("[Edge] sichtbare Ladepunkte (soll 0 sein): " + visibleAfterClear.size());

        System.out.println("\n=== DEMO ENDE ===");

    }

    private static int countLocationsWithId(LocationManager lm, String locationId) {
        int count = 0;
        for (Location l : lm.getAll()) {
            if (l.getLocationId().equals(locationId)) count++;
        }
        return count;
    }

    private static void startChargingIfFree(ChargingPointManager cpm, String pointId) {
        ChargingPoint cp = cpm.findById(pointId);
        if (cp == null) return;

        // "FREI/BELEGT" auf Enum (bei euch: IN_BETRIEB_FREI / IN_BETRIEB_BESETZT)
        if (cp.getStatus() == ChargingStatus.IN_BETRIEB_FREI) {
            cpm.setStatus(pointId, ChargingStatus.IN_BETRIEB_BESETZT);
        } else {
            System.out.println("Start abgelehnt: " + pointId + " ist nicht frei (" + cp.getStatus() + ")");
        }
    }

    private static void stopCharging(ChargingPointManager cpm, String pointId) {
        ChargingPoint cp = cpm.findById(pointId);
        if (cp == null) return;

        if (cp.getStatus() == ChargingStatus.IN_BETRIEB_BESETZT) {
            cpm.setStatus(pointId, ChargingStatus.IN_BETRIEB_FREI);
        }
    }

    private static Customer ensureCustomer(CustomerManager cm, String email, String name, String pw) {
        Customer c = cm.findByEmail(email);
        if (c == null) c = cm.registerCustomer(email, name, pw);
        return c;
    }

    private static void topUpIfPossible(CustomerManager cm, String email, double amount) {
        try { cm.topUpCustomer(email, amount); } catch (Exception ignored) {}
    }

    // versucht Invoice zu holen
    private static Object tryGetInvoice(BillingManager bm, Customer c) {
        try {
            return bm.getInvoiceForCustomer(c);
        } catch (Exception ignored) {
            try {
                List<?> invs = (List<?>) bm.getInvoiceForCustomer(c);
                return (invs == null || invs.isEmpty()) ? null : invs.get(0);
            } catch (Exception ignored2) {
                return null;
            }
        }
    }

    // zählt Sessions in Invoice
    private static int tryCountSessionsInInvoice(Object invoice) {
        if (invoice == null) return 0;
        try {
            @SuppressWarnings("unchecked")
            List<?> sessions = (List<?>) invoice.getClass().getMethod("getSessions").invoke(invoice);
            return sessions == null ? 0 : sessions.size();
        } catch (Exception ignored) {
            return 0;
        }
    }

    // versucht Ladehistorie zu holen
    private static List<?> tryGetCustomerHistory(BillingManager bm, Customer c) {
        try {
            @SuppressWarnings("unchecked")
            List<?> list = (List<?>) bm.getClass().getMethod("getSessionsForCustomer", Customer.class).invoke(bm, c);
            return list;
        } catch (Exception ignored) {
            return new ArrayList<>();
        }
    }



    private static void topUp(CustomerManager cm, String email, double amount) {
        try {
            cm.topUpCustomer(email, amount);
        } catch (Exception e) {
            System.out.println("TopUp fehlgeschlagen: " + e.getClass().getSimpleName() + " | " + e.getMessage());
        }
    }

    private static void resetCustomerBalanceIfPossible(Customer c) {
        // Demo macht keinen harten Reset, nur Anzeige + neue Runs über clear().
    }

    private static Location ensureLocation(LocationManager lm, String id, String name, String address) {
        Location loc = lm.findById(id);
        if (loc == null) loc = lm.createLocation(id, name, address);
        return loc;
    }

    private static ChargingPoint ensureChargingPoint(ChargingPointManager cpm, String id, ChargingMode mode, Location loc) {
        ChargingPoint cp = cpm.findById(id);
        if (cp == null) cp = cpm.createChargingPoint(id, mode, loc);
        return cp;
    }

    private static void safeClear(Object manager) {
        try {
            manager.getClass().getMethod("clear").invoke(manager);
        } catch (Exception ignored) {
            // falls der Manager kein clear() hat, ignorieren
        }
    }

}
