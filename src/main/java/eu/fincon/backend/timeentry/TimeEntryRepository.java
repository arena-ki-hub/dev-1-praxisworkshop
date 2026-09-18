package eu.fincon.backend.timeentry;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeEntryRepository extends JpaRepository<TimeEntry, Long> {

	List<TimeEntry> findByEmployeeIdOrderByDateAsc(Long employeeId);

	List<TimeEntry> findByEmployeeIdAndDateBetweenOrderByDateAsc(Long employeeId, LocalDate from, LocalDate to);
}
