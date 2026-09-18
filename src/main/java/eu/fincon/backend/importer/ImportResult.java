package eu.fincon.backend.importer;

import java.util.List;

public record ImportResult(int importedCount, List<ImportRowError> skippedRows) {
}
