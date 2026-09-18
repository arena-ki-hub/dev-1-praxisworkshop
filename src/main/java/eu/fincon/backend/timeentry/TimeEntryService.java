package eu.fincon.backend.timeentry;

import java.util.List;

import org.springframework.stereotype.Service;

import eu.fincon.backend.employee.Employee;
import eu.fincon.backend.employee.EmployeeService;

@Service
public class TimeEntryService {

	private final TimeEntryRepository timeEntryRepository;
	private final EmployeeService employeeService;

	public TimeEntryService(TimeEntryRepository timeEntryRepository, EmployeeService employeeService) {
		this.timeEntryRepository = timeEntryRepository;
		this.employeeService = employeeService;
	}

	public List<TimeEntry> findByEmployee(Long employeeId) {
		employeeService.findById(employeeId);
		return timeEntryRepository.findByEmployeeIdOrderByDateAsc(employeeId);
	}

	public TimeEntry findById(Long id) {
		return timeEntryRepository.findById(id)
				.orElseThrow(() -> new TimeEntryNotFoundException(id));
	}

	public TimeEntry create(Long employeeId, TimeEntryRequest request) {
		Employee employee = employeeService.findById(employeeId);
		TimeEntry timeEntry = new TimeEntry(employee, request.date(), request.hours(), request.taskDescription(),
				request.absence());
		return timeEntryRepository.save(timeEntry);
	}

	public TimeEntry update(Long id, TimeEntryRequest request) {
		TimeEntry timeEntry = findById(id);
		timeEntry.setDate(request.date());
		timeEntry.setHours(request.hours());
		timeEntry.setTaskDescription(request.taskDescription());
		timeEntry.setAbsence(request.absence());
		return timeEntryRepository.save(timeEntry);
	}

	public void delete(Long id) {
		if (!timeEntryRepository.existsById(id)) {
			throw new TimeEntryNotFoundException(id);
		}
		timeEntryRepository.deleteById(id);
	}
}
