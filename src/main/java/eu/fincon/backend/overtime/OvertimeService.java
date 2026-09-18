package eu.fincon.backend.overtime;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import eu.fincon.backend.common.BadRequestException;
import eu.fincon.backend.employee.Employee;
import eu.fincon.backend.employee.EmployeeService;
import eu.fincon.backend.timeentry.TimeEntry;
import eu.fincon.backend.timeentry.TimeEntryRepository;

/**
 * Regel: Sa/So haben 0 Soll-Stunden, Werktage {@code weeklyTargetHours / 5}.
 * Tage mit einer Abwesenheits-Buchung (Urlaub etc.) werden komplett aus der
 * Berechnung ausgeschlossen (weder Soll- noch Ist-Stunden).
 */
@Service
public class OvertimeService {

	private static final int DAYS_PER_WORK_WEEK = 5;

	private final EmployeeService employeeService;
	private final TimeEntryRepository timeEntryRepository;

	public OvertimeService(EmployeeService employeeService, TimeEntryRepository timeEntryRepository) {
		this.employeeService = employeeService;
		this.timeEntryRepository = timeEntryRepository;
	}

	public OvertimeResponse calculate(Long employeeId, LocalDate from, LocalDate to) {
		if (from.isAfter(to)) {
			throw new BadRequestException("'from' darf nicht nach 'to' liegen");
		}
		Employee employee = employeeService.findById(employeeId);
		BigDecimal dailyTargetHours = BigDecimal.valueOf(employee.getWeeklyTargetHours())
				.divide(BigDecimal.valueOf(DAYS_PER_WORK_WEEK), 4, RoundingMode.HALF_UP);

		List<TimeEntry> entries = timeEntryRepository.findByEmployeeIdAndDateBetweenOrderByDateAsc(employeeId, from, to);
		Map<LocalDate, List<TimeEntry>> entriesByDate = entries.stream()
				.collect(Collectors.groupingBy(TimeEntry::getDate));

		BigDecimal sollStunden = BigDecimal.ZERO;
		BigDecimal istStunden = BigDecimal.ZERO;

		for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
			List<TimeEntry> dayEntries = entriesByDate.getOrDefault(date, List.of());
			boolean isAbsenceDay = dayEntries.stream().anyMatch(TimeEntry::isAbsence);

			if (!isAbsenceDay) {
				sollStunden = sollStunden.add(dailyTargetHours);
			}

			BigDecimal dayHours = dayEntries.stream()
					.map(TimeEntry::getHours)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			istStunden = istStunden.add(dayHours);
		}

		BigDecimal ueberstunden = istStunden.subtract(sollStunden);
		return new OvertimeResponse(employeeId, from, to, sollStunden, istStunden, ueberstunden);
	}
}
