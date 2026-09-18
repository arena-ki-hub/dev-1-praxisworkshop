# Übung: Schritt 6 — Konfigurierbares Spalten-Mapping für den Excel-Import

**Lernziel:** Code Review vor einem Refactoring — schau dir dazu den Skill `code-review` bzw. `simplify` an.

**Ausgangslage:** Der Excel-Import aus Schritt 3 geht von einem festen Spaltenlayout aus. In der Praxis exportieren unterschiedliche Zeiterfassungs-Tools (oder unterschiedliche Konfigurationen desselben Tools) aber unterschiedliche Spaltenreihenfolgen und -namen — das reale Export-Format des Unternehmens hat z.B. deutlich mehr und anders benannte Spalten als unsere Beispieldatei.

**Aufgabe:** Baue den Import so um, dass die Zuordnung der Spalten zu den fachlichen Feldern (Datum, Username, Stunden, Task, Abwesenheit) konfigurierbar ist, statt fest im Code zu stehen — z.B. über die Spaltenüberschriften (Kopfzeile) statt feste Spaltenpositionen, mit einem Default-Mapping aus der Konfiguration und der Möglichkeit, es pro Import-Aufruf zu überschreiben.

**Empfohlener Workflow:** Bevor du umbaust: Lass dir von Claude Code zunächst ein Code-Review des bestehenden `TimeEntryImportService` geben (Skill `code-review` oder `simplify`) — welche Stellen sind fest an das aktuelle Spaltenlayout gekoppelt? Erst danach den Umbau planen, mit `/grilling` hinterfragen (z.B.: Was passiert, wenn eine im Mapping referenzierte Spalte in der Datei fehlt? Muss die Beispieldatei weiter funktionieren?) und umsetzen.

**Akzeptanzkriterien:**
- Der bestehende Import mit der Beispieldatei und dem Default-Mapping funktioniert weiterhin unverändert.
- Eine Datei mit abweichenden, aber vollständigen Spaltenüberschriften lässt sich importieren, wenn ein passendes Mapping mitgegeben wird.
- Fehlt eine im Mapping referenzierte Pflichtspalte in der Datei, liefert der Import einen klaren Fehler (HTTP 400) statt eines unklaren Fehlers oder falscher Daten.
- `./mvnw test` läuft grün, inkl. eines Tests mit abweichendem Spalten-Mapping.

Das ist der letzte Schritt dieser Übung — glückwunsch!

