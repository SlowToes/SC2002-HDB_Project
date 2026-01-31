package helper.UserRegistryLoader;

import user.MaritalStatus;

// Utility to parse common User fields from a String array row
public class UserFieldParser {

   // Private constructor to prevent instantiation
   private UserFieldParser() {
      throw new IllegalStateException("Utility class");
   }

   /**
    * Parses the common User fields from a String array representing a row.
    * Assumes the order: Name, NRIC, Age, Marital Status, Password.
    *
    * @param parts The String array from a split line.
    * @return An array of Objects containing the parsed Name (String), NRIC (String), Age (int), MaritalStatus, Password (String).
    * @throws Exception If parsing or conversion fails for any field.
    */
   public static Object[] parseUserFields(String[] parts) throws Exception {
      // Assuming parts array has already been checked for correct length (5) by the loader

      String name = parts[0].trim();
      String nric = parts[1].trim();
      String ageStr = parts[2].trim();
      String maritalStatusStr = parts[3].trim();
      String password = parts[4].trim();

      // Parse Age
      int age;
      if (ageStr.isEmpty()) {
         // Decide how to handle empty age: default to 0, or throw exception
         // Throwing is safer if age is required
         throw new IllegalArgumentException("Age cannot be empty");
      }
      try {
         age = Integer.parseInt(ageStr);
         if (age < 0) { // Basic age validation
            throw new IllegalArgumentException("Age cannot be negative: " + age);
         }
      }
      catch (NumberFormatException e) {
         throw new NumberFormatException("Invalid Age format: '" + ageStr + "'");
      }

      // Parse Marital Status
      MaritalStatus maritalStatus = null; // Allow null if status is optional/empty
      if (!maritalStatusStr.isEmpty()) {
         try {
            maritalStatus = MaritalStatus.fromString(maritalStatusStr); // Use helper method
         }
         catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Marital Status: '" + maritalStatusStr + "'");
         }
      }

      // Basic validation for required fields (can be moved to specific mappers if needed per type)
      if (name.isEmpty()) {
         throw new IllegalArgumentException("Name cannot be empty");
      }
      if (nric.isEmpty()) { // NRIC is already checked for non-empty in User constructor, but good to check early
         throw new IllegalArgumentException("NRIC cannot be empty");
      }
      // Password could potentially be empty depending on requirements

      // Return parsed fields as an Object array
      return new Object[]{name, nric, password, age, maritalStatus};
   }
}
