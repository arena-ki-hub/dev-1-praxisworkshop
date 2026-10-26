package eu.fincon.backend.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record EmployeeRequest(
		@NotBlank(message = "must not be empty") String firstName,
		@NotBlank(message = "must not be empty") String lastName,
		@NotBlank(message = "must not be empty") String username,
		@Positive(message = "must be greater than 0") double weeklyTargetHours) {
}
