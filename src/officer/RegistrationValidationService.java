package officer;

import project.Project;
import project.ProjectRegistry;

import java.time.LocalDate;

public class RegistrationValidationService implements IRegistrationValidationService {
   private final ProjectRegistry projectRegistry;

   public RegistrationValidationService(ProjectRegistry projectRegistry) {
      this.projectRegistry = projectRegistry;
   }

   @Override
   public void validateRegistration(RegistrationForm form) {
      Project targetProject = projectRegistry.getProjects().stream()
              .filter(p -> p.getId().equals(form.getProjectId()))
              .findFirst()
              .orElseThrow(() -> new IllegalArgumentException("Project does not exist"));
      validateNotAlreadyHandling(form.getOfficerName(), targetProject);
      validateNoExistingRegistration(form.getNric(), targetProject);
      validateOfficerSlots(targetProject);
      validateApplicationPeriod(targetProject);
   }

   private void validateApplicationPeriod(Project project) {
      LocalDate today = LocalDate.now();
      if (today.isBefore(project.getApplicationOpeningDate()) ||
              today.isAfter(project.getApplicationClosingDate())) {
         throw new IllegalArgumentException("Project is not accepting officer registrations");
      }
   }

   private void validateOfficerSlots(Project project) {
      if (project.getAvailableOfficerSlots() <= 0) {
         throw new IllegalArgumentException("No available officer slots for this project");
      }
   }

   private void validateNoExistingRegistration(String nric, Project newProject) {
      projectRegistry.getProjects().stream()
              .filter(p -> p.getOfficers().contains(nric))
              .filter(p -> datesOverlap(p, newProject))
              .findAny()
              .ifPresent(p -> {
                 throw new IllegalArgumentException("Existing officer registration in overlapping period");
              });
   }

   private boolean datesOverlap(Project existing, Project newProject) {
      return !existing.getApplicationClosingDate().isBefore(newProject.getApplicationOpeningDate()) &&
              !existing.getApplicationOpeningDate().isAfter(newProject.getApplicationClosingDate());
   }

   private void validateNotAlreadyHandling(String officer, Project project) {
      if (project.getOfficers().contains(officer)) {
         throw new IllegalArgumentException("Officer already assigned to this project");
      }
   }
}