package eu.fincon.backend.employee;

import eu.fincon.backend.common.NotFoundException;

public class EmployeeNotFoundException extends NotFoundException {

	public EmployeeNotFoundException(Long id) {
		super("Mitarbeiter mit ID %d nicht gefunden".formatted(id));
	}
}
