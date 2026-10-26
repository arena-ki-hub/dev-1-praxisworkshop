package eu.fincon.backend.timeentry;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record TimeEntryRequest(
		@NotNull(message = "must not be empty") @PastOrPresent(message = "must not be in the future") LocalDate date,
		@NotNull(message = "must not be empty") @DecimalMin(value = "0.0", inclusive = false, message = "must be greater than 0") @DecimalMax(value = "24.0", message = "must be at most 24") BigDecimal hours,
		@NotBlank(message = "must not be empty") String taskDescription,
		boolean absence) {
}
