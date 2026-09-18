package eu.fincon.backend.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record EmployeeRequest(
		@NotBlank(message = "darf nicht leer sein") String firstName,
		@NotBlank(message = "darf nicht leer sein") String lastName,
		@NotBlank(message = "darf nicht leer sein") String username,
		@Positive(message = "muss größer als 0 sein") double weeklyTargetHours) {
}
