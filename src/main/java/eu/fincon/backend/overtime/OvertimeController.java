package eu.fincon.backend.overtime;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OvertimeController {

	private final OvertimeService overtimeService;

	public OvertimeController(OvertimeService overtimeService) {
		this.overtimeService = overtimeService;
	}

	@GetMapping("/api/employees/{id}/overtime")
	public OvertimeResponse calculate(
			@PathVariable Long id,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
		return overtimeService.calculate(id, from, to);
	}
}
