package eu.fincon.backend.overtime;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OvertimeResponse(
		Long employeeId,
		LocalDate from,
		LocalDate to,
		BigDecimal sollStunden,
		BigDecimal istStunden,
		BigDecimal ueberstunden) {
}
