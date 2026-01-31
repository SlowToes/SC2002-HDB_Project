package helper.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic loader for delimited text files with a header row.
 * Reads the file, splits lines, skips the header, and uses a RowMapper
 * to convert each data row into an object of type T.
 *
 * @param <T> The type of objects to load.
 */
public class DelimitedTextDataLoader<T> {

   /**
    * Loads data from a delimited text file and maps each row to an object of type T.
    *
    * @param filePath        The path to the delimited text file.
    * @param delimiter       The delimiter character(s) used in the file (e.g., "\t" for tab, "," for comma, "\\|" for pipe).
    * @param rowMapper       A RowMapper functional interface implementation that defines how to convert a String[] (split row) to an object of type T.
    * @param expectedColumns The expected number of columns in each data row (excluding the header).
    * @return A List of objects of type T loaded from the file.
    * @throws IOException If an I/O error occurs while reading the file.
    */
   public List<T> loadData(String filePath, String delimiter, RowMapper<T> rowMapper, int expectedColumns) throws IOException {
      List<T> loadedObjects = new ArrayList<>();

      // Use try-with-resources to ensure the BufferedReader is closed
      try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
         String line;
         boolean isHeader = true; // Flag to skip the header row
         int lineNumber = 0; // To track line number for error reporting

         while ((line = br.readLine()) != null) {
            lineNumber++; // Increment line number for current line

            if (isHeader) {
               isHeader = false;
               continue; // Skip the header line
            }

            // Skip empty or blank lines
            if (line.trim().isEmpty()) {
               continue;
            }

            // Split the line by the specified delimiter
            // Using -1 ensures trailing empty strings are included
            String[] parts = line.split(delimiter, -1);

            // Check if we have the expected number of columns
            if (parts.length != expectedColumns) {
               System.err.println("Skipping line " + lineNumber + " in file '" + filePath + "' due to incorrect number of columns (" + parts.length + "): " + line);
               continue; // Skip this row
            }

            try {
               // Use the provided rowMapper to convert the parts array to an object of type T
               T obj = rowMapper.mapRow(parts);
               if (obj != null) { // rowMapper might return null if it decides to skip a row internally
                  loadedObjects.add(obj);
               }
            }
            catch (Exception e) {
               // Catch exceptions thrown by the rowMapper (e.g., NumberFormatException, IllegalArgumentException)
               System.err.println("Skipping line " + lineNumber + " in file '" + filePath + "' due to data mapping error: " + line + " - " + e.getMessage());
               // e.printStackTrace(); // Uncomment for detailed debugging
            }
         }

      } // BufferedReader and FileReader are automatically closed by try-with-resources

      return loadedObjects;
   }

   // No main method here
}
