package helper.loader;

// A functional interface to map a String array (representing a row) to an object of type T
@FunctionalInterface
public interface RowMapper<T> {
   /**
    * Maps a String array (representing a row from a delimited file) to an object of type T.
    * Implementations should handle parsing, type conversion, and validation for the specific type T.
    *
    * @param parts The array of strings obtained by splitting a line from the file.
    * @return An object of type T, or null if the row should be skipped by the mapper's internal logic
    * (though throwing exceptions is often preferred for explicit error reporting).
    * @throws Exception If any error occurs during mapping (e.g., invalid format, missing required data).
    *                   The data loader will catch and report these exceptions.
    */
   T mapRow(String[] parts) throws Exception;
}
