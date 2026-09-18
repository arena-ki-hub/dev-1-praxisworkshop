package eu.fincon.backend.timeentry;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record TimeEntryRequest(
		@NotNull(message = "darf nicht leer sein") @PastOrPresent(message = "darf nicht in der Zukunft liegen") LocalDate date,
		@NotNull(message = "darf nicht leer sein") @DecimalMin(value = "0.0", inclusive = false, message = "muss größer als 0 sein") @DecimalMax(value = "24.0", message = "darf höchstens 24 sein") BigDecimal hours,
		@NotBlank(message = "darf nicht leer sein") String taskDescription,
		boolean absence) {
}
