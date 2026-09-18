package eu.fincon.backend.importer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class TimeEntryImportControllerTest {

	private static final Path SAMPLE_FILE = Path.of("samples", "zeiterfassung-import-beispiel.xlsx");

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void importTimeEntries_importsKnownEmployeesAndSkipsUnknownUsername() throws Exception {
		byte[] content = Files.readAllBytes(SAMPLE_FILE);
		MockMultipartFile file = new MockMultipartFile("file", "zeiterfassung-import-beispiel.xlsx",
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", content);

		String response = mockMvc.perform(multipart("/api/time-entries/import").file(file))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		ImportResult result = objectMapper.readValue(response, ImportResult.class);

		assertThat(result.importedCount()).isEqualTo(9);
		assertThat(result.skippedRows()).hasSize(1);
		assertThat(result.skippedRows().getFirst().reason()).contains("x.unbekannt");
	}
}
