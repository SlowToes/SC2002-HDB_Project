package helper.ProjectRegistryLoader;

import UniqueID.IUniqueIdService;
import UniqueID.IdType;
import helper.loader.RowMapper;
import project.Project;

import java.time.LocalDate;
import java.util.List;

// RowMapper implementation for Project
// Uses ProjectFieldParser for data fields and generates the 'id'.
public class ProjectRowMapper implements RowMapper<Project> { // Implement the interface

   // Counter for generating sequential IDs for Project objects
   private final IUniqueIdService uniqueIdService;

   // No-argument constructor is fine
   public ProjectRowMapper(IUniqueIdService uniqueIdService) {
      this.uniqueIdService = uniqueIdService;
   }

   /**
    * Maps a String array (representing a row from the Project file) to a Project object.
    * Generates a sequential ID for each project.
    *
    * @param parts The array of strings obtained by splitting a line from the file (expected length 13).
    * @return A Project object.
    * @throws Exception If parsing or conversion fails for any field in the row.
    */
   @Override // Implementing the RowMapper interface method
   public Project mapRow(String[] parts) throws Exception {
      // Generate a unique ID for this project based on the load order
      //uniqueIdService.resetId(IdType.PROJECT_ID);
      int generatedId = uniqueIdService.generateUniqueId(IdType.PROJECT_ID); // Starts from 1 for the first row

      // Use the utility to parse the 12 data fields from the parts array
      // This method throws exceptions if parsing/conversion fails for specific fields.
      Object[] parsedFields = ProjectFieldParser.parseProjectFields(parts);

      // Extract parsed fields from the Object array
      // Casting is safe because ProjectFieldParser returns objects in the expected order and types
      String projectName = (String) parsedFields[0];
      String neighbourhood = (String) parsedFields[1];
      Integer twoRoomUnits = (Integer) parsedFields[2];
      Double twoRoomPrice = (Double) parsedFields[3];
      Integer threeRoomUnits = (Integer) parsedFields[4];
      Double threeRoomPrice = (Double) parsedFields[5];
      LocalDate applicationOpeningDate = (LocalDate) parsedFields[6];
      LocalDate applicationClosingDate = (LocalDate) parsedFields[7];
      String manager = (String) parsedFields[8];
      Integer availableOfficerSlots = (Integer) parsedFields[9];
      @SuppressWarnings("unchecked") // Casting Object to List<String>
      List<String> officers = (List<String>) parsedFields[10];

      // Create and return the Project object using the generated ID and parsed fields
      return new Project(
              generatedId,
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
      );
   }
}
