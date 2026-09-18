package eu.fincon.backend.importer;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TimeEntryImportController {

	private final TimeEntryImportService timeEntryImportService;

	public TimeEntryImportController(TimeEntryImportService timeEntryImportService) {
		this.timeEntryImportService = timeEntryImportService;
	}

	@PostMapping("/api/time-entries/import")
	public ImportResult importTimeEntries(@RequestParam("file") MultipartFile file) throws IOException {
		return timeEntryImportService.importFrom(file);
	}
}
