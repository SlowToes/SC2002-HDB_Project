package helper.UserRegistryLoader;


import applicant.Applicant;
import helper.loader.RowMapper;
import manager.Manager;
import officer.Officer;
import user.MaritalStatus;

// Contains RowMapper implementations for specific actor types
public class ActorRowMappers {

   // RowMapper for Manager
   // Uses UserFieldParser for common fields and then creates a Manager object
   public static final RowMapper<Manager> MANAGER_MAPPER = (parts) -> {
      // Use the utility to parse common fields
      Object[] parsedFields = UserFieldParser.parseUserFields(parts);

      // Extract parsed fields (casting is safe if parseUserFields returns expected types)
      String name = (String) parsedFields[0];
      String nric = (String) parsedFields[1];
      String password = (String) parsedFields[2];
      int age = (int) parsedFields[3];
      MaritalStatus maritalStatus = (MaritalStatus) parsedFields[4];

      // Create and return the specific Manager object
      return new Manager(name, nric, password, age, maritalStatus);
   };
   // RowMapper for Applicant
   // Uses UserFieldParser for common fields and then creates an Applicant object
   public static final RowMapper<Applicant> APPLICANT_MAPPER = (parts) -> {
      // Use the utility to parse common fields
      Object[] parsedFields = UserFieldParser.parseUserFields(parts);

      // Extract parsed fields
      String name = (String) parsedFields[0];
      String nric = (String) parsedFields[1];
      String password = (String) parsedFields[2];
      int age = (int) parsedFields[3];
      MaritalStatus maritalStatus = (MaritalStatus) parsedFields[4];

      // Create and return the specific Applicant object
      return new Applicant(name, nric, password, age, maritalStatus);
   };
   // RowMapper for Officer
   // Uses UserFieldParser for common fields and then creates an Officer object
   public static final RowMapper<Officer> OFFICER_MAPPER = (parts) -> {
      // Use the utility to parse common fields
      Object[] parsedFields = UserFieldParser.parseUserFields(parts);

      // Extract parsed fields
      String name = (String) parsedFields[0];
      String nric = (String) parsedFields[1];
      String password = (String) parsedFields[2];
      int age = (int) parsedFields[3];
      MaritalStatus maritalStatus = (MaritalStatus) parsedFields[4];

      // Create and return the specific Officer object
      return new Officer(name, nric, password, age, maritalStatus);
   };

   // Private constructor to prevent instantiation
   private ActorRowMappers() {
      throw new IllegalStateException("Utility class");
   }

   // You could also create a generic User mapper if needed, but the requirement is
   // to create the *specific* type based on the file source.
    /*
    public static final RowMapper<User> USER_MAPPER = (parts) -> {
         Object[] parsedFields = UserFieldParser.parseUserFields(parts);
         String name = (String) parsedFields[0];
         String nric = (String) parsedFields[1];
         String password = (String) parsedFields[2];
         int age = (int) parsedFields[3];
         MaritalStatus maritalStatus = (MaritalStatus) parsedFields[4];
         // This mapper can only create the base User type, not the specific subclass
         return new User(name, nric, password, age, maritalStatus);
    };
    */
}
