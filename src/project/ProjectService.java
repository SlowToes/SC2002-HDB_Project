package project;

import UniqueID.IUniqueIdService;
import UniqueID.IdType;
import applicant.Application;
import enquiry.Enquiry;
import officer.IRegistrationValidationService;
import officer.RegistrationForm;
import officer.RegistrationValidationService;
import system.ServiceRegistry;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

public class ProjectService implements IProjectService {
   private final ProjectRegistry projectRegistry;
   private final IUniqueIdService uniqueIdService;
   private final IProjectValidationService projectValidationService;
   private final IRegistrationValidationService registrationValidationService;

   public ProjectService(ProjectRegistry projectRegistry) {
      this.projectRegistry = projectRegistry;
      this.uniqueIdService = ServiceRegistry.get(IUniqueIdService.class);
      this.projectValidationService = new ProjectValidationService();
      this.registrationValidationService = new RegistrationValidationService(projectRegistry);
   }

   @Override
   public Project getProjectById(Integer projectId) {
      return projectRegistry.getProjects().stream()
              .filter(p -> p.getId().equals(projectId))
              .findFirst()
              .orElse(null);
   }

   @Override
   public Project getProjectByName(String projectName) {
      return projectRegistry.getProjects().stream()
              .filter(p -> p.getProjectName().equals(projectName))
              .findFirst()
              .orElse(null);
   }

   @Override
   public List<Project> getAllProjects() {
      return projectRegistry.getProjects();
   }


   // --- Project Management ---
   @Override
   public Project createProject(String projectName, String neighbourhood, Integer twoRoomUnits, Double twoRoomPrice,
                                Integer threeRoomUnits, Double threeRoomPrice, LocalDate applicationOpeningDate,
                                LocalDate applicationClosingDate, String manager, Integer availableOfficerSlots,
                                List<String> officers) {
      Project project = new Project(uniqueIdService.generateUniqueId(IdType.PROJECT_ID), projectName, neighbourhood,
              twoRoomUnits, twoRoomPrice, threeRoomUnits, threeRoomPrice, applicationOpeningDate, applicationClosingDate, manager, availableOfficerSlots, officers);
      validateNewProject(project);//throws exception if project is invalid and propagates to caller
      addProjectToRegistry(project);
      return project;
   }

   @Override
   public void deleteProject(Project project) {
      projectRegistry.removeProject(project);
   }

   @Override
   public String returnNameIfProjectExists(String projectName) {
      //check existence of project by name and id
      String returnName = projectRegistry.getProjects().stream()
              .filter(p -> p.getProjectName().equals(projectName))
              .findFirst()
              .map(Project::getProjectName)
              .orElse(null);
      //check by id
      if (returnName == null) {
         returnName = projectRegistry.getProjects().stream()
                 .filter(p -> p.getId().equals(Integer.parseInt(projectName)))
                 .findFirst()
                 .map(Project::getProjectName)
                 .orElse(null);
      }
      return returnName;
   }

   @Override
   public void addProjectToRegistry(Project project) {
      projectRegistry.addProject(project);
   }

   @Override
   public void removeProjectFromRegistry(Project project) {
      projectRegistry.removeProject(project);
   }

   @Override
   public List<Project> getFilteredProjects(Predicate<Project> predicate) {
      return projectRegistry.filter(predicate);
   }

   // --- Validation ---
   @Override
   public void validateNewProject(Project project) throws IllegalArgumentException {
      projectValidationService.validateProjectNameUnique(project, getAllProjects());
      projectValidationService.validateApplicationDates(project.getApplicationOpeningDate(),
              project.getApplicationClosingDate());
      projectValidationService.validateNeighborhood(project.getNeighborhood());
      projectValidationService.validateFlatUnitsAndPrices(project.getTwoRoomUnits(), project.getTwoRoomPrice(),
              project.getThreeRoomUnits(), project.getThreeRoomPrice());
      projectValidationService.validateOfficerSlots(project.getAvailableOfficerSlots());
   }

   @Override
   public void addRegistrationToProject(RegistrationForm form) throws IllegalArgumentException {
      registrationValidationService.validateRegistration(form);
      Project project = getProjectById(form.getProjectId());
      project.addRegistrationForm(form);
   }

   @Override
   public void addEnquiryToProject(Enquiry enquiry) {
      Project project = getProjectById(enquiry.getProjectId());
      project.addEnquiry(enquiry);
   }

   @Override
   public void removeEnquiryFromProject(Enquiry enquiry) {
      Project project = getProjectById(enquiry.getProjectId());
      project.removeEnquiry(enquiry);
   }

   @Override
   public List<List<String>> getEnquiriesFrom(String projectId) {
      return projectRegistry.getProjects().stream()
              .filter(p -> p.getId().equals(Integer.parseInt(projectId)))
              .flatMap(p -> p.getEnquiries().stream().map(Enquiry::toStringList))
              .toList();
   }

   @Override
   public List<List<String>> getAllEnquiriesFromAllProjects() {
      return projectRegistry.getProjects().stream()
              .flatMap(p -> p.getEnquiries().stream())
              .map(Enquiry::toStringList)
              .toList();
   }

   @Override
   public void addApplicationToProject(Application application) {
      Project project = getProjectById(application.getProjectId());
      System.out.println("Adding application to project: " + project.getProjectName());
      project.addApplication(application);
   }

   @Override
   public void updateProject(Project project, FlatType flatType) {
      Project existingProject = getProjectById(project.getId());
      System.out.println("Updating project: " + existingProject.getProjectName());
      //existingProject.decrementRemainingFlat(flatType);
      System.out.println("Remaining flats after decrement: " + existingProject.getRemainingFlats());
   }


}