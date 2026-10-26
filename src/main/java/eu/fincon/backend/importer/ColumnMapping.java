package eu.fincon.backend.importer;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Maps the business fields of the import to the column headers of the
 * Excel file. The default comes from {@code application.yaml}
 * (`import.excel.column-mapping.*`) and can be overridden per request via the
 * optional `mapping` part of the multipart request.
 */
@ConfigurationProperties(prefix = "import.excel.column-mapping")
public record ColumnMapping(String date, String username, String hours, String task, String absence) {
}
