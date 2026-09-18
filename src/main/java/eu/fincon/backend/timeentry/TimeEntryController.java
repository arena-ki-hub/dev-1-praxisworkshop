package eu.fincon.backend.timeentry;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class TimeEntryController {

	private final TimeEntryService timeEntryService;

	public TimeEntryController(TimeEntryService timeEntryService) {
		this.timeEntryService = timeEntryService;
	}

	@GetMapping("/api/employees/{employeeId}/time-entries")
	public List<TimeEntryResponse> findByEmployee(@PathVariable Long employeeId) {
		return timeEntryService.findByEmployee(employeeId).stream().map(TimeEntryResponse::from).toList();
	}

	@PostMapping("/api/employees/{employeeId}/time-entries")
	@ResponseStatus(HttpStatus.CREATED)
	public TimeEntryResponse create(@PathVariable Long employeeId, @Valid @RequestBody TimeEntryRequest request) {
		return TimeEntryResponse.from(timeEntryService.create(employeeId, request));
	}

	@GetMapping("/api/time-entries/{id}")
	public TimeEntryResponse findById(@PathVariable Long id) {
		return TimeEntryResponse.from(timeEntryService.findById(id));
	}

	@PutMapping("/api/time-entries/{id}")
	public TimeEntryResponse update(@PathVariable Long id, @Valid @RequestBody TimeEntryRequest request) {
		return TimeEntryResponse.from(timeEntryService.update(id, request));
	}

	@DeleteMapping("/api/time-entries/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		timeEntryService.delete(id);
	}
}
