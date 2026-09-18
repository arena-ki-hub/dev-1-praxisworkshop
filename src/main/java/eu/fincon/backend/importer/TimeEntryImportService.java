package eu.fincon.backend.importer;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import eu.fincon.backend.employee.Employee;
import eu.fincon.backend.employee.EmployeeRepository;
import eu.fincon.backend.timeentry.TimeEntryRequest;
import eu.fincon.backend.timeentry.TimeEntryService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

/**
 * Erwartet ein festes Spaltenlayout: A=Datum (ISO yyyy-MM-dd), B=Username,
 * C=Stunden, D=Task, E=Abwesenheit (Ja/Nein). Zeile 1 ist die Kopfzeile.
 */
@Service
public class TimeEntryImportService {

	private static final int COLUMN_DATE = 0;
	private static final int COLUMN_USERNAME = 1;
	private static final int COLUMN_HOURS = 2;
	private static final int COLUMN_TASK = 3;
	private static final int COLUMN_ABSENCE = 4;

	private final EmployeeRepository employeeRepository;
	private final TimeEntryService timeEntryService;
	private final Validator validator;

	public TimeEntryImportService(EmployeeRepository employeeRepository, TimeEntryService timeEntryService,
			Validator validator) {
		this.employeeRepository = employeeRepository;
		this.timeEntryService = timeEntryService;
		this.validator = validator;
	}

	public ImportResult importFrom(MultipartFile file) throws IOException {
		int importedCount = 0;
		List<ImportRowError> errors = new ArrayList<>();
		DataFormatter formatter = new DataFormatter();

		try (InputStream inputStream = file.getInputStream(); XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
			Sheet sheet = workbook.getSheetAt(0);
			for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				int humanRowNumber = rowIndex + 1;
				if (row == null || isBlank(formatter, row, COLUMN_DATE)) {
					continue;
				}

				String username = formatter.formatCellValue(row.getCell(COLUMN_USERNAME)).trim();
				Employee employee = employeeRepository.findByUsername(username).orElse(null);
				if (employee == null) {
					errors.add(new ImportRowError(humanRowNumber, "Unbekannter Mitarbeiter: '%s'".formatted(username)));
					continue;
				}

				LocalDate date;
				try {
					date = LocalDate.parse(formatter.formatCellValue(row.getCell(COLUMN_DATE)).trim());
				} catch (DateTimeParseException ex) {
					errors.add(new ImportRowError(humanRowNumber, "Ungültiges Datum"));
					continue;
				}

				BigDecimal hours;
				try {
					hours = new BigDecimal(formatter.formatCellValue(row.getCell(COLUMN_HOURS)).trim().replace(",", "."));
				} catch (NumberFormatException ex) {
					errors.add(new ImportRowError(humanRowNumber, "Ungültige Stundenzahl"));
					continue;
				}

				String task = formatter.formatCellValue(row.getCell(COLUMN_TASK)).trim();
				boolean absence = isYes(formatter.formatCellValue(row.getCell(COLUMN_ABSENCE)));

				TimeEntryRequest request = new TimeEntryRequest(date, hours, task, absence);
				Set<ConstraintViolation<TimeEntryRequest>> violations = validator.validate(request);
				if (!violations.isEmpty()) {
					errors.add(new ImportRowError(humanRowNumber, describe(violations)));
					continue;
				}

				timeEntryService.create(employee.getId(), request);
				importedCount++;
			}
		}

		return new ImportResult(importedCount, errors);
	}

	private static boolean isBlank(DataFormatter formatter, Row row, int column) {
		return formatter.formatCellValue(row.getCell(column)).isBlank();
	}

	private static boolean isYes(String value) {
		String normalized = value.trim().toLowerCase();
		return normalized.equals("ja") || normalized.equals("yes") || normalized.equals("true");
	}

	private static String describe(Set<ConstraintViolation<TimeEntryRequest>> violations) {
		return violations.stream()
				.map(v -> v.getPropertyPath() + ": " + v.getMessage())
				.reduce((a, b) -> a + "; " + b)
				.orElse("Validierung fehlgeschlagen");
	}
}
