package eu.fincon.backend.employee;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void findAll_returnsSeededEmployees() throws Exception {
		mockMvc.perform(get("/api/employees"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", greaterThanOrEqualTo(3)));
	}

	@Test
	void create_persistsNewEmployee() throws Exception {
		EmployeeRequest request = new EmployeeRequest("Erika", "Muster", "e.muster", 35.0);

		mockMvc.perform(post("/api/employees")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.username").value("e.muster"));
	}

	@Test
	void create_rejectsDuplicateUsername() throws Exception {
		EmployeeRequest request = new EmployeeRequest("Doppel", "Gaenger", "a.schmidt", 40.0);

		mockMvc.perform(post("/api/employees")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isConflict());
	}

	@Test
	void create_rejectsInvalidPayload() throws Exception {
		EmployeeRequest request = new EmployeeRequest("", "Muster", "leer", 0.0);

		mockMvc.perform(post("/api/employees")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void findById_returnsNotFoundForUnknownId() throws Exception {
		mockMvc.perform(get("/api/employees/999999"))
				.andExpect(status().isNotFound());
	}

	@Test
	void delete_removesEmployee() throws Exception {
		EmployeeRequest request = new EmployeeRequest("Lisa", "Weber", "l.weber", 20.0);
		String response = mockMvc.perform(post("/api/employees")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andReturn().getResponse().getContentAsString();
		Long id = objectMapper.readTree(response).get("id").asLong();

		mockMvc.perform(delete("/api/employees/" + id))
				.andExpect(status().isNoContent());
		mockMvc.perform(get("/api/employees/" + id))
				.andExpect(status().isNotFound());
	}
}
