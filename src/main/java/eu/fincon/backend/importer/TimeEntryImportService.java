package eu.fincon.backend.importer;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import eu.fincon.backend.common.BadRequestException;
import eu.fincon.backend.employee.Employee;
import eu.fincon.backend.employee.EmployeeRepository;
import eu.fincon.backend.timeentry.TimeEntryRequest;
import eu.fincon.backend.timeentry.TimeEntryService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

/**
 * Liest Zeile 1 als Kopfzeile und ordnet die fachlichen Felder anhand des
 * {@link ColumnMapping} den Spaltenüberschriften zu (nicht anhand fester
 * Spaltenpositionen). So bleibt der Import auch für andere Export-Formate
 * nutzbar, solange die Spalten passend benannt sind bzw. ein passendes
 * Mapping mitgegeben wird.
 */
@Service
public class TimeEntryImportService {

	private final EmployeeRepository employeeRepository;
	private final TimeEntryService timeEntryService;
	private final Validator validator;
	private final ColumnMapping defaultColumnMapping;

	public TimeEntryImportService(EmployeeRepository employeeRepository, TimeEntryService timeEntryService,
			Validator validator, ColumnMapping defaultColumnMapping) {
		this.employeeRepository = employeeRepository;
		this.timeEntryService = timeEntryService;
		this.validator = validator;
		this.defaultColumnMapping = defaultColumnMapping;
	}

	public ImportResult importFrom(MultipartFile file, ColumnMapping requestedMapping) throws IOException {
		ColumnMapping mapping = requestedMapping != null ? requestedMapping : defaultColumnMapping;
		int importedCount = 0;
		List<ImportRowError> errors = new ArrayList<>();
		DataFormatter formatter = new DataFormatter();

		try (InputStream inputStream = file.getInputStream(); XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
			Sheet sheet = workbook.getSheetAt(0);
			Map<String, Integer> headerIndex = readHeaderIndex(sheet, formatter);

			int dateColumn = requireColumn(headerIndex, mapping.date());
			int usernameColumn = requireColumn(headerIndex, mapping.username());
			int hoursColumn = requireColumn(headerIndex, mapping.hours());
			int taskColumn = requireColumn(headerIndex, mapping.task());
			int absenceColumn = requireColumn(headerIndex, mapping.absence());

			for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				int humanRowNumber = rowIndex + 1;
				if (row == null || isBlank(formatter, row, dateColumn)) {
					continue;
				}

				String username = formatter.formatCellValue(row.getCell(usernameColumn)).trim();
				Employee employee = employeeRepository.findByUsername(username).orElse(null);
				if (employee == null) {
					errors.add(new ImportRowError(humanRowNumber, "Unbekannter Mitarbeiter: '%s'".formatted(username)));
					continue;
				}

				LocalDate date;
				try {
					date = LocalDate.parse(formatter.formatCellValue(row.getCell(dateColumn)).trim());
				} catch (DateTimeParseException ex) {
					errors.add(new ImportRowError(humanRowNumber, "Ungültiges Datum"));
					continue;
				}

				BigDecimal hours;
				try {
					hours = new BigDecimal(formatter.formatCellValue(row.getCell(hoursColumn)).trim().replace(",", "."));
				} catch (NumberFormatException ex) {
					errors.add(new ImportRowError(humanRowNumber, "Ungültige Stundenzahl"));
					continue;
				}

				String task = formatter.formatCellValue(row.getCell(taskColumn)).trim();
				boolean absence = isYes(formatter.formatCellValue(row.getCell(absenceColumn)));

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

	private static Map<String, Integer> readHeaderIndex(Sheet sheet, DataFormatter formatter) {
		Map<String, Integer> headerIndex = new LinkedHashMap<>();
		Row headerRow = sheet.getRow(0);
		if (headerRow == null) {
			return headerIndex;
		}
		for (Cell cell : headerRow) {
			String header = formatter.formatCellValue(cell).trim();
			if (!header.isBlank()) {
				headerIndex.put(header, cell.getColumnIndex());
			}
		}
		return headerIndex;
	}

	private static int requireColumn(Map<String, Integer> headerIndex, String columnName) {
		Integer index = headerIndex.get(columnName);
		if (index == null) {
			throw new BadRequestException("Pflichtspalte '%s' nicht in der Excel-Datei gefunden".formatted(columnName));
		}
		return index;
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
