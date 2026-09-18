package eu.fincon.backend.importer;

public record ImportRowError(int rowNumber, String reason) {
}
