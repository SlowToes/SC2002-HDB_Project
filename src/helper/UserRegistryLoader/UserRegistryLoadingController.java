package helper.UserRegistryLoader;

import user.UserRegistry;

import java.io.IOException;

/**
 * Controller class responsible for initializing the User Registry by loading data
 * from various delimited files.
 */
public class UserRegistryLoadingController {
   //String managerFilePath, String applicantFilePath, String officerFilePath
   private final String managerFilePath = "./given/ManagerList.txt";
   private final String applicantFilePath = "./given/ApplicantList.txt";
   private final String officerFilePath = "./given/OfficerList.txt";
   private final UserRegistry userRegistry;
   private final UserRegistryLoaderService loaderService;


   public UserRegistryLoadingController() {
      this.userRegistry = new UserRegistry();
      this.loaderService = new UserRegistryLoaderService(this.userRegistry);
   }

   public UserRegistry initializeUserRegistry() throws IOException, Exception {
      System.out.println("Starting user registry initialization...");
      try {
         // Use the loader service to load data from all files into the registry
         loaderService.loadRegistry(managerFilePath, applicantFilePath, officerFilePath);
         System.out.println("User registry initialization complete.");
         return this.userRegistry; // Return the populated registry

      }
      catch (IOException e) {
         System.err.println("Failed to load user data from files: " + e.getMessage());
         throw e; // Re-throw the IOException
      }
      catch (Exception e) {
         System.err.println("An unexpected error occurred during initialization: " + e.getMessage());
         throw e; // Re-throw any other exceptions
      }
   }
}
