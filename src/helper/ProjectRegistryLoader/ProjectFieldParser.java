package helper.ProjectRegistryLoader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// Utility to parse Project specific fields from a String array row
// It parses the 12 data columns from the tab-delimited file format.
public class ProjectFieldParser {

   // Date formatter for "M/d/yyyy" format
   private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");

   // Private constructor to prevent instantiation
   private ProjectFieldParser() {
      throw new IllegalStateException("Utility class");
   }

   /**
    * Parses the 12 data fields for a Project from a String array representing a row.
    * Assumes the order based on the example:
    * Project Name, Neighborhood, Type 1, Units 1, Price 1, Type 2, Units 2, Price 2,
    * Open Date, Close Date, Manager, Officer Slot, Officer List.
    *
    * @param parts The String array from a split line (expected length 13).
    * @return An array of Objects containing the parsed fields in the order required by the Project constructor
    * (excluding the 'id' which is generated):
    * [0] projectName (String), [1] neighbourhood (String), [2] twoRoomUnits (Integer), [3] twoRoomPrice (Double),
    * [4] threeRoomUnits (Integer), [5] threeRoomPrice (Double), [6] applicationOpeningDate (LocalDate),
    * [7] applicationClosingDate (LocalDate), [8] manager (String), [9] availableOfficerSlots (Integer),
    * [10] officers (List<String>).
    * @throws Exception If parsing or conversion fails for any field.
    */
   public static Object[] parseProjectFields(String[] parts) throws Exception {
      // Assumes parts array has already been checked for correct length (13) by the loader

      // Extract raw strings (trimming simple whitespace)
      String projectNameStr = parts[0].trim();
      String neighbourhoodStr = parts[1].trim();
      // parts[2] is Type 1 ("2-Room"), not mapped directly
      String twoRoomUnitsStr = parts[3].trim();
      String twoRoomPriceStr = parts[4].trim();
      // parts[5] is Type 2 ("3-Room"), not mapped directly
      String threeRoomUnitsStr = parts[6].trim();
      String threeRoomPriceStr = parts[7].trim();
      String openingDateStr = parts[8].trim();
      String closingDateStr = parts[9].trim();
      String managerStr = parts[10].trim();
      String officerSlotsStr = parts[11].trim();
      String officersListStr = parts[12].trim(); // This needs special parsing

      // --- Parse and Convert Fields ---

      // Project Name (String) - Basic validation already in Project constructor
      String projectName = projectNameStr;

      // Neighbourhood (String) - Basic validation already in Project constructor
      String neighbourhood = neighbourhoodStr;

      // Two Room Units (Integer)
      Integer twoRoomUnits = null; // Allow null if empty or invalid
      if (!twoRoomUnitsStr.isEmpty()) {
         try {
            twoRoomUnits = Integer.parseInt(twoRoomUnitsStr);
            if (twoRoomUnits < 0) {
               throw new IllegalArgumentException("Units cannot be negative");
            }
         }
         catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Two Room Units format: '" + twoRoomUnitsStr + "'");
         }
      }

      // Two Room Price (Double)
      Double twoRoomPrice = null; // Allow null if empty or invalid
      if (!twoRoomPriceStr.isEmpty()) {
         try {
            twoRoomPrice = Double.parseDouble(twoRoomPriceStr);
            if (twoRoomPrice < 0) {
               throw new IllegalArgumentException("Price cannot be negative");
            }
         }
         catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Two Room Price format: '" + twoRoomPriceStr + "'");
         }
      }

      // Three Room Units (Integer)
      Integer threeRoomUnits = null; // Allow null if empty or invalid
      if (!threeRoomUnitsStr.isEmpty()) {
         try {
            threeRoomUnits = Integer.parseInt(threeRoomUnitsStr);
            if (threeRoomUnits < 0) {
               throw new IllegalArgumentException("Units cannot be negative");
            }
         }
         catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Three Room Units format: '" + threeRoomUnitsStr + "'");
         }
      }

      // Three Room Price (Double)
      Double threeRoomPrice = null; // Allow null if empty or invalid
      if (!threeRoomPriceStr.isEmpty()) {
         try {
            threeRoomPrice = Double.parseDouble(threeRoomPriceStr);
            if (threeRoomPrice < 0) {
               throw new IllegalArgumentException("Price cannot be negative");
            }
         }
         catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Three Room Price format: '" + threeRoomPriceStr + "'");
         }
      }

      // Application Opening Date (LocalDate)
      LocalDate applicationOpeningDate = null; // Allow null if empty or invalid
      if (!openingDateStr.isEmpty()) {
         try {
            applicationOpeningDate = LocalDate.parse(openingDateStr, DATE_FORMATTER);
         }
         catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid Opening Date format: '" + openingDateStr + "' (Expected M/d/yyyy)");
         }
      }

      // Application Closing Date (LocalDate)
      LocalDate applicationClosingDate = null; // Allow null if empty or invalid
      if (!closingDateStr.isEmpty()) {
         try {
            applicationClosingDate = LocalDate.parse(closingDateStr, DATE_FORMATTER);
         }
         catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid Closing Date format: '" + closingDateStr + "' (Expected M/d/yyyy)");
         }
      }

      // Manager (String) - Trimmed above
      String manager = managerStr;

      // Available Officer Slots (Integer)
      Integer availableOfficerSlots = null; // Allow null if empty or invalid
      if (!officerSlotsStr.isEmpty()) {
         try {
            availableOfficerSlots = Integer.parseInt(officerSlotsStr);
            if (availableOfficerSlots < 0) {
               throw new IllegalArgumentException("Officer Slots cannot be negative");
            }
         }
         catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Officer Slots format: '" + officerSlotsStr + "'");
         }
      }

      // Officers List (List<String>) - Requires special parsing
      List<String> officers = null; // Allow null if empty or invalid
      if (!officersListStr.isEmpty()) {
         String contentToSplit = officersListStr;

         // Check and remove surrounding quotes if present
         if (contentToSplit.startsWith("\"") && contentToSplit.endsWith("\"")) {
            // Remove the outer quotes
            contentToSplit = contentToSplit.substring(1, contentToSplit.length() - 1);
         }

         // Handle escaped quotes within the content (e.g., "" -> ") - Not explicitly in example, but common in CSV/quoted fields
         // contentToSplit = contentToSplit.replace("\"\"", "\""); // Uncomment if escaped quotes are possible

         // Split by comma and trim each name
         if (!contentToSplit.isEmpty()) {
            officers = Arrays.stream(contentToSplit.split(","))
                    .map(String::trim) // Trim each individual name
                    .filter(name -> !name.isEmpty()) // Filter out empty names resulting from consecutive commas or trailing commas
                    .collect(Collectors.toList());
            if (officers.isEmpty() && !contentToSplit.isEmpty()) {
               // If splitting resulted in an empty list but the content wasn't empty,
               // it might be a single empty quoted string like "" - handle as empty list
               officers = java.util.Collections.emptyList();
            }
            else if (officers.isEmpty() && contentToSplit.isEmpty()) {
               // If content was empty after quote removal (e.g. just ""), result is empty list
               officers = java.util.Collections.emptyList();
            }
         }
         else {
            // If the string was empty after trimming (e.g., just whitespace or empty field), result is empty list
            officers = java.util.Collections.emptyList();
         }
      }
      else {
         // If the original string was empty, result is null or empty list. Let's use null for consistency with other nullable fields.
         officers = null;
      }


      // Return parsed fields as an Object array in the order needed by the Project constructor
      // Note: The 'id' is NOT parsed here, it will be generated by the mapper/loader.
      return new Object[]{
              projectName,
              neighbourhood,
              twoRoomUnits,
              twoRoomPrice,
              threeRoomUnits,
              threeRoomPrice,
              applicationOpeningDate,
              applicationClosingDate,
              manager,
              availableOfficerSlots,
              officers
      };
   }
}
