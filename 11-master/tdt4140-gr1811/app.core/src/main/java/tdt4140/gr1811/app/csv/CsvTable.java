package tdt4140.gr1811.app.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A table, meant to be saved to a CSV-file. All rows must have the same number of elements in them.
 * <p>
 * Example usage:
 * <pre>
 * CsvTable table = new CsvTable("First column", "Second column");
 * table.addRow("value 1", "value 2");
 * table.addRow("value one", "value two");
 * table.save(file);
 */
public class CsvTable {

	private final List<String> columnNames = new ArrayList<>();
	private final List<List<String>> rows = new ArrayList<>();

	/**
	 * Creates a {@link CsvTable}.
	 * 
	 * @param columns
	 *            the first row, ie. the header
	 */
	public CsvTable(String... columns) {
		for (int i = 0; i < columns.length; i++) {
			String columnName = columns[i];
			columnNames.add(columnName);
		}
	}

	/**
	 * Creates a {@link CsvTable}.
	 * 
	 * @param columns
	 *            the first row, ie. the header
	 */
	public CsvTable(List<String> columns) {
		columnNames.addAll(columns);
	}

	/**
	 * Adds element to the header of this row. The number of elements must match the
	 * columns given through the constructor, otherwise a
	 * {@link IllegalArgumentException} is thrown.
	 * 
	 * @param elements
	 *            values to add to a new row
	 */
	public void addRow(String... elements) {
		List<String> columns = Arrays.asList(elements);
		addRow(columns);
	}

	/**
	 * Adds element to the header of this row. The number of elements must match the
	 * columns given through the constructor, otherwise a
	 * {@link IllegalArgumentException} is thrown.
	 * <p>
	 * Makes an internal copy of the list. Changes to the list will not propagate to
	 * this row.
	 * 
	 * @param columns
	 *            list of values to add to a new row
	 */
	public void addRow(List<String> columns) {
		if (columns == null) {
			throw new IllegalArgumentException("list of columns was null");
		}
		if (columns.size() != columnNames.size()) {
			throw new IllegalArgumentException("number of columns must match list given through constructor");
		}
		// Create copy of arraylist to avoid reference to external list
		rows.add(new ArrayList<>(columns));
	}

	/**
	 * Saves the internal header and tables to a new Comma Separated Values file.
	 * The file must not be null, otherwise a {@link IllegalArgumentException} is
	 * thrown.
	 * <p>
	 * Values with commas will be encapsulated with quotes. Quotes will be escaped
	 * by using double quotes.
	 * 
	 * @param file
	 *            the file to write to, it will be overwritten
	 * @throws IOException
	 *             if an exception occurs while attempting to write to the file
	 */
	public void save(File file) throws IOException {
		if (file == null) {
			throw new IllegalArgumentException("File is null!");
		}
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
			String commaSeparatedColNames = listToRow(columnNames);
			bw.write(commaSeparatedColNames + "\n");
			for (List<String> row : rows) {
				String commaSeparatedValues = listToRow(row);
				bw.write(commaSeparatedValues + "\n");
			}
		}
	}

	/**
	 * Internal method that converts a list of values into a comma separated string.
	 * 
	 * @param columns
	 *            list of values
	 * @return a comma separated string
	 */
	private String listToRow(List<String> columns) {
		StringBuilder sb = new StringBuilder();
		for (String col : columns) {
			String escapedString = col.replaceAll("\"", "\"\"");
			if (col.indexOf(',') != -1) {
				escapedString = String.format("\"%s\"", col);
			}
			sb.append(escapedString);
			sb.append(',');
		}
		if (columns.size() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

}
