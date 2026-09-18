package eu.fincon.backend.timeentry;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

@SpringBootTest
@AutoConfigureMockMvc
class TimeEntryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

	@Test
	void findByEmployee_returnsSeededEntries() throws Exception {
		mockMvc.perform(get("/api/employees/1/time-entries"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", greaterThanOrEqualTo(3)));
	}

	@Test
	void create_persistsEntryForEmployee() throws Exception {
		TimeEntryRequest request = new TimeEntryRequest(LocalDate.now(), new BigDecimal("6.5"), "Testaufgabe", false);

		mockMvc.perform(post("/api/employees/1/time-entries")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.employeeId").value(1))
				.andExpect(jsonPath("$.hours").value(6.5));
	}

	@Test
	void create_rejectsUnknownEmployee() throws Exception {
		TimeEntryRequest request = new TimeEntryRequest(LocalDate.now(), new BigDecimal("6.5"), "Testaufgabe", false);

		mockMvc.perform(post("/api/employees/999999/time-entries")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isNotFound());
	}

	@Test
	void create_rejectsHoursAboveTwentyFour() throws Exception {
		TimeEntryRequest request = new TimeEntryRequest(LocalDate.now(), new BigDecimal("25"), "Zu viel", false);

		mockMvc.perform(post("/api/employees/1/time-entries")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void create_rejectsFutureDate() throws Exception {
		TimeEntryRequest request = new TimeEntryRequest(LocalDate.now().plusDays(1), new BigDecimal("8"), "Zukunft",
				false);

		mockMvc.perform(post("/api/employees/1/time-entries")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void update_changesExistingEntry() throws Exception {
		TimeEntryRequest createRequest = new TimeEntryRequest(LocalDate.now(), new BigDecimal("4"), "Original",
				false);
		String response = mockMvc.perform(post("/api/employees/1/time-entries")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(createRequest)))
				.andReturn().getResponse().getContentAsString();
		Long id = objectMapper.readTree(response).get("id").asLong();

		TimeEntryRequest updateRequest = new TimeEntryRequest(LocalDate.now(), new BigDecimal("5"), "Geaendert",
				false);
		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put(
						"/api/time-entries/" + id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.taskDescription").value("Geaendert"));
	}

	@Test
	void delete_removesEntry() throws Exception {
		TimeEntryRequest createRequest = new TimeEntryRequest(LocalDate.now(), new BigDecimal("2"), "Zum Loeschen",
				false);
		String response = mockMvc.perform(post("/api/employees/1/time-entries")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(createRequest)))
				.andReturn().getResponse().getContentAsString();
		Long id = objectMapper.readTree(response).get("id").asLong();

		mockMvc.perform(delete("/api/time-entries/" + id))
				.andExpect(status().isNoContent());
		mockMvc.perform(get("/api/time-entries/" + id))
				.andExpect(status().isNotFound());
	}
}
