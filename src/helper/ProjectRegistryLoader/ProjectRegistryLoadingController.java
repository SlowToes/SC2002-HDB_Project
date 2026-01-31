package helper.ProjectRegistryLoader;


import UniqueID.IUniqueIdService;
import project.ProjectRegistry;

import java.io.IOException;

/**
 * Controller class responsible for initializing the Project Registry by loading data
 * from a delimited file.
 */
public class ProjectRegistryLoadingController {

   private final ProjectRegistry projectRegistry;
   private final ProjectRegistryLoaderService loaderService;

   /**
    * Constructs a ProjectRegistryLoadingController.
    * Initializes the ProjectRegistry and the ProjectRegistryLoaderService.
    */
   public ProjectRegistryLoadingController(IUniqueIdService uniqueIdService) {
      this.projectRegistry = new ProjectRegistry();
      this.loaderService = new ProjectRegistryLoaderService(this.projectRegistry, uniqueIdService);
   }

   public ProjectRegistry initializeProjectRegistry() throws IOException, Exception {
      System.out.println("Starting project registry initialization...");
      try {
         // Use the loader service to load data from the file into the registry
         String projectFilePath = "./given/ProjectList.txt";
         loaderService.loadRegistry(projectFilePath);
         System.out.println("Project registry initialization complete.");
         return this.projectRegistry; // Return the populated registry

      }
      catch (IOException e) {
         System.err.println("Failed to load project data from file: " + e.getMessage());
         throw e; // Re-throw the IOException
      }
      catch (Exception e) {
         System.err.println("An unexpected error occurred during project initialization: " + e.getMessage());
         throw e; // Re-throw any other exceptions
      }
   }

   // You can add getter methods for the registry if needed, e.g.:
   // public ProjectRegistry getProjectRegistry() {
   //     return projectRegistry;
   // }

   // This class would be instantiated and its initializeProjectRegistry method called
   // from another part of your application.
}
