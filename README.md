# Schritt 6 — Konfigurierbares Spalten-Mapping für den Excel-Import

## Lernziel

Code Review vor einem Refactoring sowie Subagents, um den eigenen Kontext schlank zu halten.

## Die Methode dieses Schritts

Schau dir dazu den Skill `code-review` bzw. `simplify` an.

**Subagents:** Ein Subagent ist eine eigene Claude-Instanz mit eigenem, frischem Kontextfenster. Claude gibt ihm eine Teilaufgabe, er liest selbstständig die nötigen Dateien und liefert nur eine Zusammenfassung zurück. Die Dateien, die er gelesen hat, landen also nicht in deinem Hauptkontext.

## Ausgangslage

Der Excel-Import aus Schritt 3 geht von einem festen Spaltenlayout aus. In der Praxis exportieren unterschiedliche Zeiterfassungs-Tools (oder unterschiedliche Konfigurationen desselben Tools) aber unterschiedliche Spaltenreihenfolgen und -namen — das reale Export-Format des Unternehmens hat z.B. deutlich mehr und anders benannte Spalten als unsere Beispieldatei.

## Aufgabe

Baue den Import so um, dass die Zuordnung der Spalten zu den fachlichen Feldern (Datum, Username, Stunden, Task, Abwesenheit) konfigurierbar ist, statt fest im Code zu stehen — z.B. über die Spaltenüberschriften (Kopfzeile) statt feste Spaltenpositionen, mit einem Default-Mapping aus der Konfiguration und der Möglichkeit, es pro Import-Aufruf zu überschreiben.

## Empfohlener Workflow

Bevor du umbaust: Lass dir von Claude Code zunächst ein Code-Review des bestehenden `TimeEntryImportService` geben — welche Stellen sind fest an das aktuelle Spaltenlayout gekoppelt? Macht das Review als Experiment mit eurem Nachbarn. Beide starten mit `/clear` und notieren sich den Kontext-% aus der Statuszeile.

Person A reviewt direkt:

```bash
Reviewe den TimeEntryImportService: Welche Stellen sind fest an das aktuelle Spaltenlayout gekoppelt?
```

Person B reviewt per Subagent:

```bash
Lass einen Subagent den TimeEntryImportService reviewen: Welche Stellen sind fest an das aktuelle Spaltenlayout gekoppelt? Gib mir nur das Ergebnis zurück.
```

> **Aufgabe:** Vergleicht Kontext-% und Tokens in der Statuszeile und schaut euch mit `/context` an, was im Kontext liegt. Wie unterscheiden sich die Ergebnisse des Reviews? In diesem kleinen Projekt ist der Unterschied noch überschaubar, in echten Projekten mit vielen Dateien fällt er stark ins Gewicht.

Alternativ kannst du für das Review auch die Skills `code-review` oder `simplify` nutzen. Erst danach den Umbau planen, mit `/grilling` hinterfragen (z.B.: Was passiert, wenn eine im Mapping referenzierte Spalte in der Datei fehlt? Muss die Beispieldatei weiter funktionieren?) und umsetzen.

## Feature selbst testen (Pflicht)

Nach einem Refactoring ist der wichtigste Test der, dass das alte Verhalten unverändert funktioniert — und zwar in der laufenden Anwendung, nicht nur in der Testsuite.

1. **App starten** — in einem zweiten Terminal, oder von Claude im Hintergrund starten lassen:

   ```bash
   ./mvnw spring-boot:run
   ```

2. **Swagger UI öffnen** — <http://localhost:8080/swagger-ui.html>. Im Dev Container ist Port 8080 weitergeleitet (siehe Reiter „Ports").

3. **Default-Mapping (Regression)** — `POST /api/time-entries/import` mit `samples/zeiterfassung-import-beispiel.xlsx` ohne eigenes Mapping → unverändert 9 importierte Zeilen und eine übersprungene Zeile (`x.unbekannt`).

4. **Eigenes Mapping** — Lass dir von Claude eine zweite Excel-Datei mit anderen Spaltennamen und anderer Reihenfolge erzeugen (z.B. `Tag`, `Mitarbeiter`, `Dauer`, `Tätigkeit`, `Urlaub`), importiere sie mit passendem Mapping und prüfe über `GET /api/employees/{employeeId}/time-entries`, dass die Einträge wirklich angekommen sind.

5. **Fehlerfall** — Dieselbe Datei mit einem Mapping importieren, das eine nicht vorhandene Spalte referenziert → klare 400-Antwort, die die fehlende Spalte benennt, statt stillschweigend falscher Daten.

6. **Wenn etwas nicht stimmt** — gib Claude die konkrete Anfrage, das verwendete Mapping und die Antwort aus der Swagger UI statt nur „geht nicht".

## Akzeptanzkriterien

- Der bestehende Import mit der Beispieldatei und dem Default-Mapping funktioniert weiterhin unverändert.
- Eine Datei mit abweichenden, aber vollständigen Spaltenüberschriften lässt sich importieren, wenn ein passendes Mapping mitgegeben wird.
- Fehlt eine im Mapping referenzierte Pflichtspalte in der Datei, liefert der Import einen klaren Fehler (HTTP 400) statt eines unklaren Fehlers oder falscher Daten.
- `./mvnw test` läuft grün, inkl. eines Tests mit abweichendem Spalten-Mapping.
- Du hast beide Importe (Default-Mapping und eigenes Mapping) sowie den Fehlerfall selbst in der laufenden Anwendung ausprobiert.

Das ist der letzte Pflichtschritt dieser Übung — glückwunsch! Die vollständige Referenzlösung findest du in `step-7`, eine optionale Zusatzübung (eigenen Skill bauen) in `step-8`.
