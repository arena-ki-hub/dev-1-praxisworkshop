package eu.fincon.backend.importer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayOutputStream;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Shows that the import is no longer tied to a fixed column layout:
 * This file has different column headers and is only imported
 * because a matching {@link ColumnMapping} is supplied.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TimeEntryImportCustomMappingTest {

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void importTimeEntries_usesRequestedMappingForCustomHeaders() throws Exception {
		byte[] content = buildWorkbookWithCustomHeaders();
		MockMultipartFile file = new MockMultipartFile("file", "custom.xlsx",
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", content);

		ColumnMapping mapping = new ColumnMapping("BookingDate", "Employee", "Hrs", "Activity", "Vacation");
		MockMultipartHttpServletRequestBuilder request = multipart("/api/time-entries/import")
				.file(file)
				.param("mapping", objectMapper.writeValueAsString(mapping));

		String response = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		ImportResult result = objectMapper.readValue(response, ImportResult.class);
		assertThat(result.importedCount()).isEqualTo(1);
		assertThat(result.skippedRows()).isEmpty();
	}

	@Test
	void importTimeEntries_rejectsMappingWithMissingColumn() throws Exception {
		byte[] content = buildWorkbookWithCustomHeaders();
		MockMultipartFile file = new MockMultipartFile("file", "custom.xlsx",
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", content);

		ColumnMapping mapping = new ColumnMapping("Does not exist", "Employee", "Hrs", "Activity", "Vacation");
		MockMultipartHttpServletRequestBuilder request = multipart("/api/time-entries/import")
				.file(file)
				.param("mapping", objectMapper.writeValueAsString(mapping));

		mockMvc.perform(request).andExpect(status().isBadRequest());
	}

	private static byte[] buildWorkbookWithCustomHeaders() throws Exception {
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			XSSFSheet sheet = workbook.createSheet("Import");
			XSSFRow header = sheet.createRow(0);
			header.createCell(0).setCellValue("BookingDate");
			header.createCell(1).setCellValue("Employee");
			header.createCell(2).setCellValue("Hrs");
			header.createCell(3).setCellValue("Activity");
			header.createCell(4).setCellValue("Vacation");

			XSSFRow dataRow = sheet.createRow(1);
			dataRow.createCell(0).setCellValue("2026-08-31");
			dataRow.createCell(1).setCellValue("a.schmidt");
			dataRow.createCell(2).setCellValue("8");
			dataRow.createCell(3).setCellValue("Customer project");
			dataRow.createCell(4).setCellValue("No");

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			workbook.write(out);
			return out.toByteArray();
		}
	}
}
