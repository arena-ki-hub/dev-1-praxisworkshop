package eu.fincon.backend.timeentry;

import eu.fincon.backend.common.NotFoundException;

public class TimeEntryNotFoundException extends NotFoundException {

	public TimeEntryNotFoundException(Long id) {
		super("Time entry with ID %d not found".formatted(id));
	}
}
