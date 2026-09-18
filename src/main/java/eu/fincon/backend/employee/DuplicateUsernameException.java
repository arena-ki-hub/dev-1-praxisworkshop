package eu.fincon.backend.employee;

import eu.fincon.backend.common.ConflictException;

public class DuplicateUsernameException extends ConflictException {

	public DuplicateUsernameException(String username) {
		super("Username '%s' ist bereits vergeben".formatted(username));
	}
}
