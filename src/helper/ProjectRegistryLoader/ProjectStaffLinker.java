package helper.ProjectRegistryLoader;

import manager.Manager;
import officer.Officer;
import officer.OfficerStatus;
import project.ProjectRegistry;
import user.User;
import user.UserRegistry;

import java.time.LocalDate;
import java.util.List;

public class ProjectStaffLinker {
   private final ProjectRegistry projectRegistry;
   private final UserRegistry userRegistry;

   public ProjectStaffLinker(ProjectRegistry projectRegistry, UserRegistry userRegistry) {
      this.projectRegistry = projectRegistry;
      this.userRegistry = userRegistry;
   }

   public void linkProjectToOfficer() {
      projectRegistry.getProjects().forEach(project -> {
         if (isProjectActive(project)) {
            List<String> officers = project.getOfficers();
            for (String officerId : officers) {
               User user = userRegistry.getUser(officerId);
               if (user instanceof Officer) {
                  Officer officer = (Officer) user;
                  officer.setCurrentProject(project);
                  officer.setOfficerStatus(OfficerStatus.ACTIVE);
                  System.out.println(officer.getName() + " (Officer) is linked to " + project.getProjectName());
               }
               else {
                  System.out.println("Warning: User " + officerId + " is not an Officer. Cannot link to project " + project.getProjectName());
               }
            }
         }
      });
   }

   public void linkProjectToManager() {
      projectRegistry.getProjects().forEach(project -> {
         if (isProjectActive(project)) {
            String managerId = project.getManager();
            User user = userRegistry.getUser(managerId);

            if (user == null) {
               System.out.println("Error: Manager with ID " + managerId + " not found. Project " + project.getProjectName() + " not linked.");
               return;
            }

            if (!(user instanceof Manager)) {
               System.out.println("Error: User " + managerId + " is not a Manager. Project " + project.getProjectName() + " not linked.");
               return;
            }

            Manager manager = (Manager) user;
            manager.setCurrentProject(project);
            System.out.println(manager.getName() + " (Manager) is linked to " + project.getProjectName());
         }
         else {
            System.out.println("Project " + project.getProjectName() + " is not open for application, not linked to any manager");
         }
      });
   }

   private boolean isProjectActive(project.Project project) {
      LocalDate now = LocalDate.now();
      LocalDate openingDate = project.getApplicationOpeningDate();
      LocalDate closingDate = project.getApplicationClosingDate();

      return (now.isEqual(openingDate) || now.isAfter(openingDate)) &&
              (now.isEqual(closingDate) || now.isBefore(closingDate));
   }
}