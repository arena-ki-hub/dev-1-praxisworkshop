package eu.fincon.backend.timeentry;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TimeEntryResponse(
		Long id,
		Long employeeId,
		LocalDate date,
		BigDecimal hours,
		String taskDescription,
		boolean absence) {

	public static TimeEntryResponse from(TimeEntry timeEntry) {
		return new TimeEntryResponse(
				timeEntry.getId(),
				timeEntry.getEmployee().getId(),
				timeEntry.getDate(),
				timeEntry.getHours(),
				timeEntry.getTaskDescription(),
				timeEntry.isAbsence());
	}
}
