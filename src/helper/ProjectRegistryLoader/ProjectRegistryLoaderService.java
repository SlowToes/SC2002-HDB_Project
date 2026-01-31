package helper.ProjectRegistryLoader;


import UniqueID.IUniqueIdService;
import helper.loader.DelimitedTextDataLoader;
import project.Project;
import project.ProjectRegistry;

import java.io.IOException;
import java.util.List;

/**
 * Service to load Project data from a delimited file into a separate registry.
 */
public class ProjectRegistryLoaderService {

   private static final String TAB_DELIMITER = "\t";
   // Count the columns in the example row: 13
   // Project Name, Neighborhood, Type 1, Units 1, Price 1, Type 2, Units 2, Price 2,
   // Open Date, Close Date, Manager, Officer Slot, Officer List
   private static final int EXPECTED_COLUMNS = 13;
   private final ProjectRegistry projectRegistry;
   private final IUniqueIdService uniqueIdService;

   public ProjectRegistryLoaderService(ProjectRegistry projectRegistry, IUniqueIdService uniqueIdService) {
      this.projectRegistry = projectRegistry;
      this.uniqueIdService = uniqueIdService;
   }

   /**
    * Loads Project data from the specified file and populates the project registry.
    *
    * @param projectFilePath The path to the tab-delimited file containing Project data.
    * @throws IOException If an I/O error occurs during file reading.
    * @throws Exception   If any unexpected error occurs during the loading process or mapping.
    */
   public void loadRegistry(String projectFilePath) throws IOException, Exception {

      // Load Projects
      System.out.println("Loading Projects from: " + projectFilePath);
      DelimitedTextDataLoader<Project> projectLoader = new DelimitedTextDataLoader<>();

      // Instantiate the ProjectRowMapper class to handle row-to-object mapping and ID generation
      ProjectRowMapper projectRowMapper = new ProjectRowMapper(uniqueIdService);

      List<Project> projects = projectLoader.loadData(
              projectFilePath,
              TAB_DELIMITER,
              projectRowMapper, // Pass the mapper instance
              EXPECTED_COLUMNS
      );
      System.out.println("Loaded " + projects.size() + " Project records.");
      projects.forEach(projectRegistry::addProject); // Add loaded projects to the registry

      System.out.println("Project registry loading complete. Total projects in registry: " + projectRegistry.size());
   }

   // No main method here, this is a service class
}
