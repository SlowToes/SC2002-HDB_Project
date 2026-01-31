package helper.UserRegistryLoader;


import applicant.Applicant;
import helper.loader.DelimitedTextDataLoader;
import manager.Manager;
import officer.Officer;
import user.UserRegistry;

import java.io.IOException;
import java.util.List;

/**
 * Service to load user data from multiple delimited files into a single registry.
 */
public class UserRegistryLoaderService {

   private static final String TAB_DELIMITER = "\t";
   private static final int EXPECTED_COLUMNS = 5; // Name, NRIC, Age, Marital Status, Password
   private final UserRegistry userRegistry;

   public UserRegistryLoaderService(UserRegistry userRegistry) {
      this.userRegistry = userRegistry;
   }

   /**
    * Loads data from the specified files for each actor type and populates the user registry.
    *
    * @param managerFilePath   The path to the tab-delimited file containing Manager data.
    * @param applicantFilePath The path to the tab-delimited file containing Applicant data.
    * @param officerFilePath   The path to the tab-delimited file containing Officer data.
    * @throws IOException If an I/O error occurs during file reading.
    */
   public void loadRegistry(String managerFilePath, String applicantFilePath, String officerFilePath) throws IOException {

      // Load Managers
      System.out.println("Loading Managers from: " + managerFilePath);
      DelimitedTextDataLoader<Manager> managerLoader = new DelimitedTextDataLoader<>();
      List<Manager> managers = managerLoader.loadData(
              managerFilePath,
              TAB_DELIMITER,
              ActorRowMappers.MANAGER_MAPPER, // Use the Manager specific mapper
              EXPECTED_COLUMNS
      );
      System.out.println("Loaded " + managers.size() + " Manager records.");
      managers.forEach(userRegistry::addUser); // Add loaded managers to the registry

      System.out.println("\nLoading Applicants from: " + applicantFilePath);
      // Load Applicants
      DelimitedTextDataLoader<Applicant> applicantLoader = new DelimitedTextDataLoader<>();
      List<Applicant> applicants = applicantLoader.loadData(
              applicantFilePath,
              TAB_DELIMITER,
              ActorRowMappers.APPLICANT_MAPPER, // Use the Applicant specific mapper
              EXPECTED_COLUMNS
      );
      System.out.println("Loaded " + applicants.size() + " Applicant records.");
      applicants.forEach(userRegistry::addUser); // Add loaded applicants to the registry

      System.out.println("\nLoading Officers from: " + officerFilePath);
      // Load Officers
      DelimitedTextDataLoader<Officer> officerLoader = new DelimitedTextDataLoader<>();
      List<Officer> officers = officerLoader.loadData(
              officerFilePath,
              TAB_DELIMITER,
              ActorRowMappers.OFFICER_MAPPER, // Use the Officer specific mapper
              EXPECTED_COLUMNS
      );
      System.out.println("Loaded " + officers.size() + " Officer records.");
      officers.forEach(userRegistry::addUser); // Add loaded officers to the registry

      System.out.println("\nRegistry loading complete. Total users in registry: " + userRegistry.size());
   }

   // No main method here, this is a service class
}
