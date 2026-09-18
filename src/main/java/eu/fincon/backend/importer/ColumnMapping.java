package eu.fincon.backend.importer;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Ordnet die fachlichen Felder des Imports den Spaltenüberschriften der
 * Excel-Datei zu. Der Default kommt aus {@code application.yaml}
 * (`import.excel.column-mapping.*`) und kann pro Request über den
 * optionalen `mapping`-Teil des Multipart-Requests überschrieben werden.
 */
@ConfigurationProperties(prefix = "import.excel.column-mapping")
public record ColumnMapping(String date, String username, String hours, String task, String absence) {
}
