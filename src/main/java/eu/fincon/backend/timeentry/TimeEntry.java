package eu.fincon.backend.timeentry;

import java.math.BigDecimal;
import java.time.LocalDate;

import eu.fincon.backend.employee.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "time_entry")
public class TimeEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;

	@Column(nullable = false)
	private LocalDate date;

	@Column(nullable = false, precision = 4, scale = 2)
	private BigDecimal hours;

	@Column(nullable = false)
	private String taskDescription;

	@Column(nullable = false)
	private boolean absence;

	protected TimeEntry() {
	}

	public TimeEntry(Employee employee, LocalDate date, BigDecimal hours, String taskDescription, boolean absence) {
		this.employee = employee;
		this.date = date;
		this.hours = hours;
		this.taskDescription = taskDescription;
		this.absence = absence;
	}

	public Long getId() {
		return id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public BigDecimal getHours() {
		return hours;
	}

	public void setHours(BigDecimal hours) {
		this.hours = hours;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public boolean isAbsence() {
		return absence;
	}

	public void setAbsence(boolean absence) {
		this.absence = absence;
	}
}
