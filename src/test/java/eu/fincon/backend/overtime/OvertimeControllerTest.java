package eu.fincon.backend.overtime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.fincon.backend.employee.EmployeeRequest;
import eu.fincon.backend.timeentry.TimeEntryRequest;

@SpringBootTest
@AutoConfigureMockMvc
class OvertimeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

	@Test
	void calculate_excludesAbsenceDaysAndCountsWeekendBookingsFully() throws Exception {
		Long employeeId = createEmployee("o.test", 40.0);

		LocalDate monday = LocalDate.of(2026, 8, 24);
		LocalDate tuesday = monday.plusDays(1);
		LocalDate wednesday = monday.plusDays(2);
		LocalDate saturday = monday.plusDays(5);
		LocalDate sunday = monday.plusDays(6);

		createTimeEntry(employeeId, monday, "8", "Customer project", false);
		createTimeEntry(employeeId, tuesday, "8", "Customer project", false);
		createTimeEntry(employeeId, wednesday, "8", "Vacation", true);
		createTimeEntry(employeeId, saturday, "4", "Weekend support", false);

		mockMvc.perform(get("/api/employees/{id}/overtime", employeeId)
						.param("from", monday.toString())
						.param("to", sunday.toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.targetHours").value(32.0))
				.andExpect(jsonPath("$.actualHours").value(20.0))
				.andExpect(jsonPath("$.overtimeHours").value(-12.0));
	}

	@Test
	void calculate_rejectsInvertedDateRange() throws Exception {
		Long employeeId = createEmployee("o.invers", 40.0);

		mockMvc.perform(get("/api/employees/{id}/overtime", employeeId)
						.param("from", "2026-08-30")
						.param("to", "2026-08-24"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void calculate_rejectsUnknownEmployee() throws Exception {
		mockMvc.perform(get("/api/employees/{id}/overtime", 999999)
						.param("from", "2026-08-24")
						.param("to", "2026-08-30"))
				.andExpect(status().isNotFound());
	}

	private Long createEmployee(String username, double weeklyTargetHours) throws Exception {
		EmployeeRequest request = new EmployeeRequest("Test", "Person", username, weeklyTargetHours);
		String response = mockMvc.perform(post("/api/employees")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andReturn().getResponse().getContentAsString();
		return objectMapper.readTree(response).get("id").asLong();
	}

	private void createTimeEntry(Long employeeId, LocalDate date, String hours, String task, boolean absence)
			throws Exception {
		TimeEntryRequest request = new TimeEntryRequest(date, new BigDecimal(hours), task, absence);
		mockMvc.perform(post("/api/employees/{employeeId}/time-entries", employeeId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated());
	}
}
