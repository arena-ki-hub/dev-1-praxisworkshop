package eu.fincon.backend.importer;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.fincon.backend.common.BadRequestException;

@RestController
public class TimeEntryImportController {

	private final TimeEntryImportService timeEntryImportService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public TimeEntryImportController(TimeEntryImportService timeEntryImportService) {
		this.timeEntryImportService = timeEntryImportService;
	}

	/**
	 * @param mapping optional JSON column mapping, e.g.
	 *                {@code {"date":"Date","username":"User","hours":"Hrs","task":"Activity","absence":"Vacation"}}.
	 *                If the parameter is missing, the default mapping from
	 *                {@code application.yaml} (`import.excel.column-mapping`) applies.
	 */
	@PostMapping("/api/time-entries/import")
	public ImportResult importTimeEntries(
			@RequestParam("file") MultipartFile file,
			@RequestParam(value = "mapping", required = false) String mapping) throws IOException {
		return timeEntryImportService.importFrom(file, parseMapping(mapping));
	}

	private ColumnMapping parseMapping(String mapping) {
		if (mapping == null || mapping.isBlank()) {
			return null;
		}
		try {
			return objectMapper.readValue(mapping, ColumnMapping.class);
		} catch (JsonProcessingException ex) {
			throw new BadRequestException("Invalid column mapping: " + ex.getOriginalMessage());
		}
	}
}
