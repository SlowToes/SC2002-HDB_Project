package manager;

import project.Project;
import user.MaritalStatus;
import user.User;
import user.UserFilterSettings;

/**
 * Represents a Manager user who can manage housing projects.
 */
public class Manager extends User {

   /**
    * The project currently being managed by this manager.
    */
   Project currentProject;

   /**
    * Constructs a Manager with the specified personal details.
    *
    * @param name          The name of the manager
    * @param nric          The NRIC of the manager
    * @param password      The password for login
    * @param age           The age of the manager
    * @param maritalStatus The marital status of the manager
    */
   public Manager(String name,
                  String nric,
                  String password,
                  int age,
                  MaritalStatus maritalStatus) {
      super(name, nric, password, age, maritalStatus, new UserFilterSettings());
      this.currentProject = null;
   }

   /**
    * Gets the current project assigned to the manager.
    *
    * @return The current project
    */
   public Project getCurrentProject() {
      return currentProject;
   }

   /**
    * Sets the current project for the manager.
    *
    * @param currentProject The project to assign
    */
   public void setCurrentProject(Project currentProject) {
      this.currentProject = currentProject;
   }

}
