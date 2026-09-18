package eu.fincon.backend.employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private double weeklyTargetHours;

	protected Employee() {
	}

	public Employee(String firstName, String lastName, String username, double weeklyTargetHours) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.weeklyTargetHours = weeklyTargetHours;
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public double getWeeklyTargetHours() {
		return weeklyTargetHours;
	}

	public void setWeeklyTargetHours(double weeklyTargetHours) {
		this.weeklyTargetHours = weeklyTargetHours;
	}
}
