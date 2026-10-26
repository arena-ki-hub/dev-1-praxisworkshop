package eu.fincon.backend.employee;

import eu.fincon.backend.common.NotFoundException;

public class EmployeeNotFoundException extends NotFoundException {

	public EmployeeNotFoundException(Long id) {
		super("Employee with ID %d not found".formatted(id));
	}
}
