package project;

import UniqueID.IUniqueIdService;
import UniqueID.IdType;
import interfaces.Filterable;
import system.ServiceRegistry;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// This class is used to store all projects in a list
public class ProjectRegistry implements Serializable, Filterable<Project> {
   @Serial
   private static final long serialVersionUID = 1L;
   private static final String filePath = "./data/projectRegistry.dat";
   private final List<Project> projects;

   public ProjectRegistry() {
      this.projects = new ArrayList<>();
   }


   // We will always use this because we will always be loading projects from csv.
   public ProjectRegistry(List<Project> projects) {
      this.projects = projects;
   }

   public void createDummyProjects() {
      // Clear existing projects to avoid duplicates when called multiple times
      projects.clear();

      // Create dummy projects with realistic data
      for (int i = 1; i <= 10; i++) {
         // Generate project data
         String projectName = "Project " + i;
         String neighbourhood = "Neighborhood " + (char) ('A' + (i % 5));
         int twoRoomUnits = 50 + (i * 10);
         double twoRoomPrice = 200000 + (i * 25000);
         int threeRoomUnits = 30 + (i * 5);
         double threeRoomPrice = 300000 + (i * 30000);

         // Set application dates (opening date is today, closing date is 30 days later)
         LocalDate today = LocalDate.now();
         LocalDate applicationOpeningDate = today;
         LocalDate applicationClosingDate = today.plusDays(30);

         // Manager and officer slots
         String managerNric = "gg";
         int officerSlots = 2 + (i % 6);

         // Create project with the generated data
         Project project = new Project(
                 ServiceRegistry.get(IUniqueIdService.class).generateUniqueId(IdType.PROJECT_ID), // projectId
                 projectName,
                 neighbourhood,
                 twoRoomUnits,
                 twoRoomPrice,
                 threeRoomUnits,
                 threeRoomPrice,
                 applicationOpeningDate,
                 applicationClosingDate,
                 managerNric,
                 officerSlots,
                 new ArrayList<>() // empty officer list initially
         );
         project.setVisibility(true);
         projects.add(project);
      }
      save();
      System.out.println("Dummy projects created and saved.");
   }

   public ProjectRegistry load() {
      try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
         return (ProjectRegistry) ois.readObject();
      }
      catch (IOException | ClassNotFoundException e) {
         System.err.println("Error loading ProjectRegistry: " + e.getMessage());
         e.printStackTrace();
         return new ProjectRegistry();
      }
   }


   // Only for other service classes to view and filter, cannot make any changes to the original list.
   // Cannot modify the original list structure (add/remove projects).
   // Please use add or remove methods explicitly if manager wants to add or remove projects.
   public List<Project> getProjects() {
      return List.copyOf(projects);
   }

   public void addProject(Project project) {
      projects.add(project);
      save();
   }

   public void removeProject(Project project) {
      projects.remove(project);
   }

   public void save() {
      try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
         oos.writeObject(this);
         System.out.println("ProjectRegistry saved successfully to " + filePath);
      }
      catch (IOException e) {
         System.err.println("Error saving ProjectRegistry: " + e.getMessage());
         e.printStackTrace();
      }
   }

   @Override
   public List<Project> filter(Predicate<Project> predicate) {
      return projects.stream()
              .filter(predicate)
              .collect(Collectors
                      .toList());
   }

   public Integer size() {
      return projects.size();
   }
}

//TODO : validate current user is manager