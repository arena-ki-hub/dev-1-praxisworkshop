package eu.fincon.backend.employee;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

	private final EmployeeRepository employeeRepository;

	public EmployeeService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public List<Employee> findAll() {
		return employeeRepository.findAll();
	}

	public Employee findById(Long id) {
		return employeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException(id));
	}

	public Employee create(EmployeeRequest request) {
		if (employeeRepository.existsByUsername(request.username())) {
			throw new DuplicateUsernameException(request.username());
		}
		Employee employee = new Employee(request.firstName(), request.lastName(), request.username(),
				request.weeklyTargetHours());
		return employeeRepository.save(employee);
	}

	public Employee update(Long id, EmployeeRequest request) {
		Employee employee = findById(id);
		employee.setFirstName(request.firstName());
		employee.setLastName(request.lastName());
		employee.setWeeklyTargetHours(request.weeklyTargetHours());
		return employeeRepository.save(employee);
	}

	public void delete(Long id) {
		if (!employeeRepository.existsById(id)) {
			throw new EmployeeNotFoundException(id);
		}
		employeeRepository.deleteById(id);
	}
}
