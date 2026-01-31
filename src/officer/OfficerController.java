package officer;

import enquiry.Enquiry;
import helper.Color;
import project.IProjectService;
import project.Project;
import project.UserFilterManager;
import system.ServiceRegistry;
import system.SessionManager;
import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OfficerController {

   private final IOfficerService officerService;
   private final IProjectService projectService;

   public OfficerController() {
      this.officerService = ServiceRegistry.get(IOfficerService.class);
      this.projectService = ServiceRegistry.get(IProjectService.class);
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

   public List<List<String>> getOfficerEligibleProjectsTableData() throws Exception {
      if (officerService.getOfficerStatus() != OfficerStatus.INACTIVE) {
         throw new IllegalStateException("You are already registered as an officer for the project.");
      }

      List<Project> projects = projectService.getFilteredProjects(project ->
              project.getOfficers().size() < project.getAvailableOfficerSlots()
      );

      if (projects == null || projects.isEmpty()) {
         return null;
      }

      List<String> headerRow = List.of("Project ID", "Project Name", "Neighbourhood", "Visibility",
              "Two Room Units", "Two Room Price", "Three Room Units",
              "Three Room Price", "Appln..Opening Date", "Appln..Closing Date",
              "Manager", "Officer Slots", "Officers");

      List<List<String>> tableData = new ArrayList<>();
      tableData.add(headerRow);

      for (Project project : projects) {
         List<String> row = List.of(
                 project.getId().toString(),
                 project.getProjectName(),
                 project.getNeighborhood(),
                 project.isVisibility() ? "Visible" : "Hidden",
                 project.getTwoRoomUnits().toString(),
                 project.getTwoRoomPrice().toString(),
                 project.getThreeRoomUnits().toString(),
                 project.getThreeRoomPrice().toString(),
                 project.getApplicationOpeningDate().toString(),
                 project.getApplicationClosingDate().toString(),
                 project.getManager(),
                 project.getAvailableOfficerSlots().toString(),
                 project.getOfficers().toString()
         );
         tableData.add(row);
      }

      return tableData;
   }


   public RegistrationForm CreateRegistrationForm(String project) {
      // TODO : check if officer is already an applicant for the project

      String projectStr = null;
      projectStr = projectService.returnNameIfProjectExists(project.trim());
      if (projectStr == null) {
         Color.println("Form not Created. Project not found or Error Creating Form. Try Again", Color.RED);
         return null;
      }
      RegistrationForm registrationForm = officerService.createRegistrationForm(projectStr);
      Color.println(registrationForm.toString(), Color.YELLOW);
      return registrationForm;
   }

   public void sendRegistrationRequest(RegistrationForm form) throws IllegalArgumentException {
      officerService.sendRegistrationRequest(form);
      officerService.setOfficerStatus(OfficerStatus.PENDING);
      //Color.println(" Registration Request Sent and set officer status", Color.GREEN);
      officerService.setCurrentRegistrationForm(form);
      officerService.addToMyRegistrations(form);
   }

   public OfficerStatus getOfficerStatus() {
      return officerService.getOfficerStatus();
   }

   public Project getCurrentProject() throws Exception {
      return officerService.getCurrentProject();
   }

   public RegistrationForm getCurrentRegistrationForm() throws Exception {
      return officerService.getCurrentRegistrationForm();
   }

   public void changePassword(String oldPassword, String newPassword, String confirmPassword) {
      officerService.changePassword(oldPassword, newPassword, confirmPassword);
   }

   public List<List<String>> getProjectEnquiriesTableData() {
      List<Enquiry> enquiries = officerService.getCurrentProject().getEnquiries();
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
      Project project = officerService.getCurrentProject();
      Enquiry enquiry = project.getEnquiries().stream().filter(x -> x.getId().equals(Integer.parseInt(enquiryId))).findFirst().get();
      if (enquiry == null) {
         return false;
      }

      if (enquiry.getReply() == null || enquiry.getReply().isEmpty()) {
         enquiry.setReply(reply);
         Color.println("Enquiry replied successfully.", Color.GREEN);
         return true;
      }
      return false;
   }
}

