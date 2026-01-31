package officer;

import applicant.Application;
import applicant.ApplicationStatus;
import helper.Color;
import helper.TablePrinter;
import interfaces.Menu;
import project.IProjectService;
import project.Project;
import system.ServiceRegistry;
import system.SessionManager;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class OfficerMenu extends Menu {
   private final Scanner scanner;
   private final SessionManager sessionManager;
   private final OfficerController officerController;
   private final TablePrinter tablePrinter;

   public OfficerMenu(Scanner scanner, TablePrinter tablePrinter, SessionManager sessionManager,
                      OfficerController officerController) {
      super(scanner);
      this.scanner = scanner;
      this.sessionManager = sessionManager;
      this.officerController = officerController;
      this.tablePrinter = tablePrinter;
   }


   protected void display() {
      Color.print("========== HDB Officer Menu ==========\n" +
              "1. View All Projects\n" +
              "2. Register as Officer for Project\n" +
              "3. View Officer Registration Status\n" +
              "4. View Details of Handled Project\n" +
              "5. View and Reply to Project Enquiries\n" +
              "6. Manage Flat Selection/Booking for Applicants\n" +
              "7. Change Password\n" +
              "0. Logout\n" +
              "======================================\n" +
              "Please enter your choice:", Color.CYAN);
   }

   protected void handleInput() {
      String input = scanner.nextLine();
      switch (input) {
         case "1" -> handleViewAllProjects();
         case "2" -> handleRegisterAsOfficer();
         case "3" -> handleViewOfficerRegistrationStatus();
         case "4" -> handleViewHandledProjectDetails();
         case "5" -> handleReplyToProjectEnquiries();
         case "6" -> handleManageFlatSelection(); //
         case "7" -> handleChangePassword();
         case "0" -> {
            sessionManager.logout();
         }
         default -> Color.println("Invalid choice", Color.RED);
      }
   }

   @Override
   public void run() {
      while (sessionManager.isLoggedIn()) {
         display();
         handleInput();
      }
   }

   private void handleViewAllProjects() {
      try {
         List<List<String>> tableData = officerController.getAllProjectsTableData();
         if (tableData.isEmpty()) {
            Color.println("No projects found.", Color.RED);
            return;
         }
         Color.println("---  All Projects ---", Color.YELLOW);
         Integer COLUMN_WIDTH = 15;
         tablePrinter.printTable(COLUMN_WIDTH, tableData);
      }
      catch (Exception e) {
         Color.println("Error: " + e.getMessage(), Color.RED);
      }
   }

   private void handleApplyForBTOProject() {
      Color.println("Applying for BTO Project", Color.GREEN);
      // Implement logic to apply for a BTO project
   }

   private void handleRegisterAsOfficer() {
      if (officerController.getOfficerStatus() != OfficerStatus.INACTIVE) {
         Color.println("You have either already registered as an officer or your registration is pending.", Color.RED);
         return;
      }

      try {
         List<List<String>> table = officerController.getOfficerEligibleProjectsTableData();
         if (table == null) {
            Color.println("No projects found.", Color.RED);
            return;
         }

         Integer COLUMN_WIDTH = 15;
         TablePrinter tablePrinter = new TablePrinter();
         for (List<String> row : table) {
            tablePrinter.printRow(COLUMN_WIDTH, row);
         }

         Color.print("Enter the project name or ID you want to register for (0 to exit): ", Color.GREEN);
         String projectName = scanner.nextLine();
         if (projectName.equals("0")) {
            Color.println("Returning to Officer Menu.", Color.RED);
            return;
         }

         RegistrationForm form = officerController.CreateRegistrationForm(projectName);
         if (form == null) {
            return;
         }

         Color.print("Do you want to send the registration request? (y/n): ", Color.GREEN);
         String confirm = scanner.nextLine();
         if (confirm.equals("y")) {
            officerController.sendRegistrationRequest(form);
            Color.println("Registration Form sent successfully.", Color.CYAN);
         }
         else {
            Color.println("Registration Form is discarded. Returning to Officer Menu.", Color.RED);
         }

      }
      catch (IllegalArgumentException | IllegalStateException e) {
         Color.println("Registration Error: " + e.getMessage(), Color.RED);
      }
      catch (Exception e) {
         Color.println("Unexpected error occurred: " + e.getMessage(), Color.RED);
      }
   }


   private void handleViewOfficerRegistrationStatus() {
      OfficerStatus status = officerController.getOfficerStatus();

      Color.println("===============================================", Color.YELLOW);

      switch (status) {
         case ACTIVE -> {
            Color.println("You are currently an active officer", Color.GREEN);
            try {
               Project project = officerController.getCurrentProject();
               Color.println(project.toString(), Color.YELLOW);
            }
            catch (Exception e) {
               Color.println("Error in the System: You have no current registration form. " + e.getMessage(), Color.RED);
            }
         }

         case INACTIVE -> {
            Color.println("You are currently inactive", Color.RED);
            Color.println("===============================================", Color.YELLOW);
            try {
               RegistrationForm form = officerController.getCurrentRegistrationForm();
               Color.println(form.toString(), Color.YELLOW);
            }
            catch (Exception e) {
               Color.println("Error in the System: You have no current registration form", Color.RED);
            }
         }

         case PENDING -> {
            Color.println("You are currently pending for registration", Color.YELLOW);
            try {
               RegistrationForm form = officerController.getCurrentRegistrationForm();
               Color.println(form.toString(), Color.YELLOW);
            }
            catch (Exception e) {
               Color.println("Error in the System: You have no current registration form", Color.RED);
            }
         }
      }
   }

   private void handleViewHandledProjectDetails() {
      Color.println("Viewing Details of Handled Project", Color.GREEN);

      try {
         Project project = officerController.getCurrentProject();
         Color.println(project.toString(), Color.YELLOW);
      }
      catch (Exception e) {
         Color.println("Error in the System: You have no current registration form. :" + e.getMessage(), Color.RED);
      }

   }

   private void handleViewAllProjectEnquiries() {
      try {
         List<List<String>> tableData = officerController.getProjectEnquiriesTableData();
         if (tableData == null || tableData.isEmpty()) {
            Color.println("No project enquiries.", Color.RED);
            return;
         }
         Integer COLUMN_WIDTH = 15;
         Color.println("--- Viewing Project Enquiries ---", Color.YELLOW);
         tablePrinter.printTable(COLUMN_WIDTH, tableData);
      }
      catch (Exception e) {
         Color.println("Error viewing project enquiries.\nPossible Reason:" + e.getMessage(), Color.RED);
      }
   }

   private void handleReplyToProjectEnquiries() {
      try {
         handleViewAllProjectEnquiries();
         Color.print("Enter Enquiry ID to reply:", Color.GREEN);
         String enquiryId = scanner.nextLine();
         Color.print("Enter Reply:", Color.GREEN);
         String reply = scanner.nextLine();
         boolean success = officerController.replyToProjectEnquiry(enquiryId, reply);
         if (success) {
            Color.println("Reply sent successfully!", Color.GREEN);
         }
         else {
            Color.println("Failed to send reply. Enquiry may not exist or already processed. Check the Enquiry ID.", Color.RED);
         }
      }
      catch (Exception e) {
         Color.println("Error processing project enquiry: " + e.getMessage(), Color.RED);
      }

   }


   private void handleViewAndReplyToEnquiries() {
      Color.println("Viewing and Replying to Project Enquiries", Color.GREEN);
      // Implement logic to view and reply to project enquiries
   }

   private void handleManageFlatSelection() {
      try {

         IProjectService projectService = ServiceRegistry.get(IProjectService.class);
         IOfficerService officerService = ServiceRegistry.get(IOfficerService.class);
         SessionManager sessionManager = ServiceRegistry.get(SessionManager.class);
         Project current = officerService.getCurrentProject();
         if (current == null) {
            Color.println("You are not assigned to any project.", Color.RED);
            return;
         }

         List<Application> apps = projectService.getProjectById(current.getId())
                 .getApplications().stream()
                 .filter(a -> a.getApplicationStatus() == ApplicationStatus.SUCCESSFUL)
                 .toList();

         if (apps.isEmpty()) {
            Color.println("No approved applications to book.", Color.RED);
            return;
         }

         Color.println("--- Approved Applications ---", Color.YELLOW);
         for (Application a : apps) {
            Color.println("ID: " + a.getId() + " | " + a.getApplicantName() + " | " + a.getFlatType() + " | " + a.getBookingStatus(), Color.YELLOW);
         }

         //((ServiceRegistry.get(SessionManager.class).getUserByName(app.getApplicantName())))
         Color.print("Enter Application ID to book flat (0 to cancel): ", Color.CYAN);
         String input = scanner.nextLine().trim();

         if (input.equals("0")) {
            Color.println("Booking cancelled.", Color.YELLOW);
            return;
         }

         try {
            int selectedId = Integer.parseInt(input);
            Optional<Application> selectedApp = apps.stream()
                    .filter(a -> a.getId() == selectedId)
                    .findFirst();

            if (selectedApp.isEmpty()) {
               Color.println("Invalid Application ID.", Color.RED);
               return;
            }

            officerService.bookFlat(selectedApp.get());
            Color.println("Flat booked and receipt generated.", Color.GREEN);
         }
         catch (NumberFormatException e) {
            Color.println("Invalid input. Please enter a valid Application ID.", Color.RED);
         }
      }
      catch (Exception e) {
         Color.println("Error: " + e.getMessage(), Color.RED);
      }
   }

   private void handleChangePassword() {
      List<String> inputs = super.getInputsChangePassword();
      try {
         officerController.changePassword(inputs.get(0), inputs.get(1), inputs.get(2));
         Color.println("Password changed successfully.", Color.GREEN);
      }
      catch (IllegalArgumentException e) {
         Color.println("Password change error: " + e.getMessage(), Color.RED);
      }
   }
}
