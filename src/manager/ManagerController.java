package manager;

import applicant.Application;
import applicant.ApplicationStatus;
import applicant.WithdrawalRequestStatus;
import enquiry.Enquiry;
import helper.Color;
import officer.RegistrationForm;
import officer.RegistrationStatus;
import project.FlatType;
import project.IProjectService;
import project.Project;
import project.UserFilterManager;
import system.ServiceRegistry;
import system.SessionManager;
import user.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ManagerController {
   private final IManagerService managerService;
   private final IProjectService projectService;

   public ManagerController() {
      this.managerService = ServiceRegistry.get(IManagerService.class);
      this.projectService = ServiceRegistry.get(IProjectService.class);
   }

   public void createProject(String projectName, String neighbourhood, Integer twoRoomUnits, Double twoRoomPrice,
                             Integer threeRoomUnits, Double threeRoomPrice, LocalDate applicationOpeningDate,
                             LocalDate applicationClosingDate, String manager, Integer availableOfficerSlots,
                             List<String> officers) throws IllegalArgumentException {

      managerService.createProject(projectName, neighbourhood,
              twoRoomUnits, twoRoomPrice, threeRoomUnits, threeRoomPrice, applicationOpeningDate, applicationClosingDate, manager, availableOfficerSlots, officers);

   }


   public boolean deleteProject(Integer projectId) {
      try {
         managerService.deleteProject(projectId);
         return true;
      }
      catch (Exception e) {
         return false;
      }
   }

   public List<List<String>> getAllProjectsTableData() throws Exception {
      List<Project> projects = projectService.getAllProjects();
      if (projects == null || projects.isEmpty()) {
         return null;
      }
      else {
         UserFilterManager userFilterManager = new UserFilterManager(new Scanner(System.in));

         User currentUser = ServiceRegistry.get(SessionManager.class).getCurrentUser();
         userFilterManager.manageFilters(currentUser);
         List<Project> filteredProjects = userFilterManager.applyFilters(projects, currentUser);

         if (filteredProjects.isEmpty()) {
            //Color.println("No projects match your filter.", Color.RED);
            throw new Exception("No projects match your filter.");
         }
         else {
            Color.println("--- Filtered Projects ---", Color.YELLOW);
         }


         List<String> headerRow = List.of("Project ID", "Project Name", "Neighbourhood", "Visibility", "Two Room Units", "Two Room Price", "Three Room Units", "Three Room Price", "Appln..Opening Date", "Appln..Closing Date", "Manager", "Officer Slots", "Officers");
         List<List<String>> tableData = new ArrayList<>();
         tableData.add(headerRow);
         for (Project p : filteredProjects) {
            List<String> fromEachProject = p.toStringAsList();
            tableData.add(fromEachProject);
         }
         return tableData;
      }
   }

   public List<List<String>> getMyProjectsTableData() {

      List<Project> myProjects = managerService.getMyProjects();

      if (myProjects == null || myProjects.isEmpty()) {
         return null;
      }
      else {
         List<String> headerRow = List.of("Project ID", "Project Name", "Neighbourhood", "Visibility", "Two Room Units", "Two Room Price", "Three Room Units", "Three Room Price", "Appln..Opening Date", "Appln..Closing Date", "Manager", "Officer Slots", "Officers");
         List<List<String>> tableData = new ArrayList<>();
         tableData.add(headerRow);
         for (Project p : myProjects) {
            List<String> fromRegistration = p.toStringAsList();
            tableData.add(fromRegistration);
         }
         return tableData;
      }
   }


   public Project getProjectById(Integer projectId) {
      return projectService.getProjectById(projectId);
   }

   public boolean toggleVisibility(Integer projectId) {
      Project project = projectService.getProjectById(projectId);
      project.setVisibility(!project.isVisibility());
      return true;
   }

   public void editProject(Integer projectId, String option, Object T) {
      Project project = projectService.getProjectById(projectId);
      switch (option) {
         case "1" -> {
            project.setProjectName((String) T);
         }
         case "2" -> {
            project.setNeighborhood((String) T);
         }
         case "3" -> {
            project.setApplicationOpeningDate((LocalDate) T);
         }
         case "4" -> {
            project.setApplicationClosingDate((LocalDate) T);
         }
         case "5" -> {
            project.setVisibility((boolean) T);
         }
         default -> {
            System.out.println("Invalid option");
         }
      }
   }

   public List<List<String>> getPendingRegistrationTableData() throws Exception {

      try {
         List<RegistrationForm> pendingRegistrations = managerService.getPendingOfficerRegistrations();
         if (pendingRegistrations.isEmpty()) {
            return null;
         }
         List<String> headerRow = List.of("ID", "Officer", "NRIC", "Date Applied", "Status", "Project ID", "Project Name");
         List<List<String>> tableData = new ArrayList<>();
         tableData.add(headerRow.subList(0, 4));
         for (RegistrationForm form : pendingRegistrations) {
            List<String> fromRegistration = form.toStringAsList().subList(0, 4);
            tableData.add(fromRegistration);
         }
         return tableData;
      }
      catch (Exception e) {
         throw new Exception("No pending registrations found");
      }
   }

   public Boolean processOfficerRegistration(String identifier, RegistrationStatus status) throws Exception {
      try {

         String officerStr = managerService.setRegistrationStatus(identifier, status);
         if (officerStr != null) {
            managerService.addToOfficersList(officerStr);

            return true;
         }
      }
      catch (Exception e) {
         throw e;
      }
      return false;
   }

   public String getCurrentProjectData() {
      return managerService.getCurrentProject() != null ? managerService.getCurrentProject().toString() : null;
   }

   public List<List<String>> getApplicantApplicationsTableData() throws Exception {
      Project project = managerService.getCurrentProject();
      if (project == null) {
         throw new Exception("No project is under your management");
      }
      List<List<String>> tableData = new ArrayList<>();
      List<String> headerRow = List.of("Application ID", "Applicant Name", "NRIC", "Flat Type", "Project ID", "Project Name", "Status");
      tableData.add(headerRow);
      List<Application> applications = project.getApplications();
      for (Application application : applications) {
         List<String> fromApplication = application.toList();
         tableData.add(fromApplication);
      }
      return tableData;
   }

   public Boolean approveOrRejectApplicantApplication(String applicationId, boolean isApproved) throws Exception {
      ApplicationStatus status = (isApproved) ? ApplicationStatus.SUCCESSFUL : ApplicationStatus.REJECTED;
      managerService.updateApplicationStatus(applicationId, status);
      return true;
   }

   public void handlePasswordChange(String oldPass, String newPass1, String newPass2) {
      managerService.changePassword(oldPass, newPass1, newPass2); // Uses IUserService default
   }

   public List<List<String>> getProjectEnquiriesTableData() {
      // TODO: Implement this method


      List<Enquiry> enquiries = managerService.getProjectEnquiries();
      if (enquiries.isEmpty()) {
         return null;
      }
      List<String> headerRow = List.of("Enquiry ID", "Project ID", "Project Name", "Officer ID", "Officer Name", "Message", "Date");
      List<List<String>> tableData = new ArrayList<>();
      tableData.add(headerRow);
      for (Enquiry enquiry : enquiries) {
         List<String> fromEnquiry = enquiry.toStringList();
         tableData.add(fromEnquiry);
      }
      return tableData;
   }

   public boolean replyToProjectEnquiry(String enquiryId, String reply) {
      return managerService.replyToProjectEnquiry(enquiryId, reply);
   }

   public List<List<String>> getWithdrawalRequestsTableData() throws Exception {
      List<Application> wr = managerService.getCurrentProject().getApplications().stream()
              .filter(a -> a.getWithdrawalRequestStatus().equals(WithdrawalRequestStatus.PENDING))
              .toList();
      List<List<String>> tableData = new ArrayList<>();
      List<String> headerRow = List.of("Application ID", "Applicant Name", "NRIC", "Flat Type", "Project ID", "Project Name", "Status");
      tableData.add(headerRow);
      for (Application application : wr) {
         List<String> fromApplication = application.toList();
         tableData.add(fromApplication);
      }
      return tableData;
   }

   public boolean approveOrRejectWithdrawalRequest(String withdrawalRequestId, boolean isApproved) throws Exception {
      WithdrawalRequestStatus status = (isApproved) ? WithdrawalRequestStatus.APPROVED : WithdrawalRequestStatus.REJECTED;
      managerService.updateWithdrawalRequestStatus(withdrawalRequestId, status);
      return true;
   }


   public List<List<String>> generateApplicantReport(Map<String, String> filters) throws Exception {
      Project currentProject = managerService.getCurrentProject();
      if (currentProject == null) {
         throw new Exception("No project is currently selected");
      }

      List<Application> applications = currentProject.getApplications();
      if (applications == null || applications.isEmpty()) {
         return null;
      }

      // Filter applications based on criteria
      List<Application> filteredApplications = applications;

      if (filters != null) {
         String filterType = filters.get("filterType");

         if (filterType != null) {
            switch (filterType) {
               case "flatType" -> {
                  String flatTypeValue = filters.get("value");
                  FlatType flatType = FlatType.valueOf(flatTypeValue);
                  filteredApplications = applications.stream()
                          .filter(app -> app.getFlatType().equals(flatType))
                          .collect(Collectors.toList());
               }
               case "project" -> {
                  // If filtering by project and we're already in a specific project context,
                  // we can either keep all applications or check if the project ID matches
                  String projectId = filters.get("value");
                  if (!currentProject.getId().toString().equals(projectId)) {
                     // If requested project is different from current project, return empty
                     return null;
                  }
               }
               case "maritalStatus" -> {
                  String maritalStatusValue = filters.get("value");
                  filteredApplications = applications.stream()
                          .filter(app -> (ServiceRegistry.get(SessionManager.class).getUserByName(app.getApplicantName())).getMaritalStatus().toString().equals(maritalStatusValue))
                          .collect(Collectors.toList());
               }
               case "ageRange" -> {
                  int minAge = Integer.parseInt(filters.get("minAge"));
                  int maxAge = Integer.parseInt(filters.get("maxAge"));
                  filteredApplications = applications.stream()
                          .filter(app -> {
                             int age = ((ServiceRegistry.get(SessionManager.class).getUserByName(app.getApplicantName()))).getAge();
                             return age >= minAge && age <= maxAge;
                          })
                          .collect(Collectors.toList());
               }
            }
         }
      }

      // Create report data
      List<List<String>> reportData = new ArrayList<>();
      List<String> headerRow = List.of(
              "Application ID",
              "Applicant Name",
              "NRIC",
              "Age",
              "Marital Status",
              "Flat Type",
              "Project ID",
              "Project Name",
              "Status"
      );
      reportData.add(headerRow);

      for (Application app : filteredApplications) {
         List<String> row = new ArrayList<>();
         row.add(app.getId().toString());
         row.add(app.getApplicantName());
         row.add(app.getApplicantNric());
         row.add(String.valueOf(((ServiceRegistry.get(SessionManager.class).getUserByName(app.getApplicantName()))).getAge()));
         row.add(((ServiceRegistry.get(SessionManager.class).getUserByName(app.getApplicantName()))).getMaritalStatus().toString());
         row.add(app.getFlatType().toString());
         row.add(currentProject.getId().toString());
         row.add(currentProject.getProjectName());
         row.add(app.getApplicationStatus().toString());
         reportData.add(row);
      }

      return reportData;
   }

}