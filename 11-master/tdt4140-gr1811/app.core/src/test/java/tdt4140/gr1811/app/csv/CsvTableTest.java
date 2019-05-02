package tdt4140.gr1811.app.csv;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CsvTableTest {

	@Test
	public void valuesShouldBeCommaSeparated() throws Exception {
		List<String> headersInput = Arrays.asList("one", "two");
		String headersExpected = "one,two";

		List<String> row1 = Arrays.asList("1", "2");
		String row1Expected = "1,2";

		Path tempFile = Files.createTempFile(null, null);
		CsvTable csvTable = new CsvTable(headersInput);
		csvTable.addRow(row1);

		csvTable.save(tempFile.toFile());

		// read result
		try (BufferedReader br = new BufferedReader(new FileReader(tempFile.toFile()))) {
			assertEquals(headersExpected, br.readLine());
			assertEquals(row1Expected, br.readLine());
		}
	}

	@Test
	public void testEscapeQuotesAndCommas() throws Exception {
		Path tempFile = Files.createTempFile(null, null);
		String columnName = "This is a column";
		String quotes = "\"";
		String escapedQuotes = "\"\"";
		String comma = ",";
		String escapedComma = "\",\"";
		CsvTable csvTable = new CsvTable(columnName);
		csvTable.addRow(quotes);
		csvTable.addRow(comma);

		csvTable.save(tempFile.toFile());

		// read result
		try (BufferedReader br = new BufferedReader(new FileReader(tempFile.toFile()))) {
			assertEquals(columnName, br.readLine());
			assertEquals(escapedQuotes, br.readLine());
			assertEquals(escapedComma, br.readLine());
		}
	}

	@Test
	public void testListMethods() throws Exception {
		Path tempFile = Files.createTempFile(null, null);
		String columnName = "This is a column";
		String quotes = "\"";
		String escapedQuotes = "\"\"";
		String comma = ",";
		String escapedComma = "\",\"";
		CsvTable csvTable = new CsvTable(Arrays.asList(columnName));
		csvTable.addRow(Arrays.asList(quotes));
		csvTable.addRow(Arrays.asList(comma));

		csvTable.save(tempFile.toFile());

		// read result
		try (BufferedReader br = new BufferedReader(new FileReader(tempFile.toFile()))) {
			assertEquals(columnName, br.readLine());
			assertEquals(escapedQuotes, br.readLine());
			assertEquals(escapedComma, br.readLine());
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullAsNewRowShouldThrow() {
		CsvTable csv = new CsvTable("Header");
		csv.addRow((List<String>) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void rowsMustHaveSameSizeAsColumnNames() {
		CsvTable csv = new CsvTable("One", "Two", "Three", "Four");
		csv.addRow("1", "2");
	}

	@Test(expected = IllegalArgumentException.class)
	public void savingThrowsOnNullInput() throws Exception {
		CsvTable csv = new CsvTable("One", "Two");
		csv.addRow("1", "2");
		csv.addRow("1", "2");
		csv.addRow("1", "2");
		csv.save(null);
	}
}
