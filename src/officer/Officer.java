package officer;

import applicant.Applicant;
import project.Project;
import user.MaritalStatus;

import java.util.ArrayList;
import java.util.List;

public class Officer extends Applicant {
   private final List<RegistrationForm> myRegistrationForms;
   private Project currentProject;
   private RegistrationForm currentRegistrationForm;
   private OfficerStatus officerStatus;

   public Officer(String name, String nric, String password, int age, MaritalStatus maritalStatus) {
      super(name, nric, password, age, maritalStatus);
      this.currentProject = null;
      this.currentRegistrationForm = null;
      this.officerStatus = OfficerStatus.INACTIVE;
      this.myRegistrationForms = new ArrayList<>();
   }

   public RegistrationForm getCurrentRegistrationForm() {
      return currentRegistrationForm;
   }

   public void setCurrentRegistrationForm(RegistrationForm currentRegistrationForm) {
      this.currentRegistrationForm = currentRegistrationForm;
   }

   public OfficerStatus getOfficerStatus() {
      return officerStatus;
   }

   public void setOfficerStatus(OfficerStatus officerStatus) {
      this.officerStatus = officerStatus;
   }

   public List<RegistrationForm> getMyRegistrationForms() {//Read-only as a whole
      return myRegistrationForms;
   }

   public void addRegistrationForm(RegistrationForm registrationForm) {
      myRegistrationForms.add(registrationForm);
   }

   public void removeRegistrationForm(RegistrationForm registrationForm) {
      myRegistrationForms.remove(registrationForm);
   }

   public Project getCurrentProject() {
      return currentProject;
   }

   public void setCurrentProject(Project currentProject) {
      this.currentProject = currentProject;
   }

   @Override
   public String toString() {
      return "========== Officer Information ==========\n" +
              "Name           : " + super.getName() + "\n" +
              "NRIC           : " + super.getNric() + "\n" +
              "Password       : " + super.getPassword() + "\n" +
              "Age            : " + super.getAge() + "\n" +
              "Marital Status : " + super.getMaritalStatus() + "\n" +
              "Officer Status : " + officerStatus + "\n" +
              "Current Project: " + (currentProject != null ? currentProject.getProjectName() : "") + "\n" +
              "=========================================";
   }
}
