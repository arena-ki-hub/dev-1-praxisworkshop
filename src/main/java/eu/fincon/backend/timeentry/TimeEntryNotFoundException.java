package eu.fincon.backend.timeentry;

import eu.fincon.backend.common.NotFoundException;

public class TimeEntryNotFoundException extends NotFoundException {

	public TimeEntryNotFoundException(Long id) {
		super("Zeiteintrag mit ID %d nicht gefunden".formatted(id));
	}
}
