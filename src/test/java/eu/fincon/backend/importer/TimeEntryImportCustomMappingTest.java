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
 * Zeigt, dass der Import nicht mehr an ein festes Spaltenlayout gebunden ist:
 * Diese Datei hat abweichende Spaltenüberschriften und wird nur importiert,
 * weil ein passendes {@link ColumnMapping} mitgegeben wird.
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

		ColumnMapping mapping = new ColumnMapping("Buchungsdatum", "Mitarbeiter", "Std", "Taetigkeit", "Urlaub");
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

		ColumnMapping mapping = new ColumnMapping("Existiert nicht", "Mitarbeiter", "Std", "Taetigkeit", "Urlaub");
		MockMultipartHttpServletRequestBuilder request = multipart("/api/time-entries/import")
				.file(file)
				.param("mapping", objectMapper.writeValueAsString(mapping));

		mockMvc.perform(request).andExpect(status().isBadRequest());
	}

	private static byte[] buildWorkbookWithCustomHeaders() throws Exception {
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			XSSFSheet sheet = workbook.createSheet("Import");
			XSSFRow header = sheet.createRow(0);
			header.createCell(0).setCellValue("Buchungsdatum");
			header.createCell(1).setCellValue("Mitarbeiter");
			header.createCell(2).setCellValue("Std");
			header.createCell(3).setCellValue("Taetigkeit");
			header.createCell(4).setCellValue("Urlaub");

			XSSFRow dataRow = sheet.createRow(1);
			dataRow.createCell(0).setCellValue("2026-08-31");
			dataRow.createCell(1).setCellValue("a.schmidt");
			dataRow.createCell(2).setCellValue("8");
			dataRow.createCell(3).setCellValue("Kundenprojekt");
			dataRow.createCell(4).setCellValue("Nein");

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			workbook.write(out);
			return out.toByteArray();
		}
	}
}
