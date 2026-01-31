package manager;

import helper.Color;
import helper.TablePrinter;
import interfaces.Menu;
import officer.RegistrationStatus;
import system.SessionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ManagerMenu extends Menu {
   private final Scanner scanner;
   private final TablePrinter tablePrinter;
   private final SessionManager sessionManager;
   private final ManagerController managerController;

   public ManagerMenu(Scanner scanner, TablePrinter tablePrinter, SessionManager sessionManager, ManagerController managerController) {
      super(scanner);
      this.scanner = scanner;
      this.tablePrinter = tablePrinter;
      this.sessionManager = sessionManager;
      this.managerController = managerController;
   }

   /**
    * Displays the manager menu options.
    */
   protected void display() {
      Color.print("========== HDB Manager Menu ==========\n" +
              "1. Create New BTO Project\n" +
              "2. Edit Existing BTO Project\n" +
              "3. Delete BTO Project\n" +
              "4. Toggle Project Visibility\n" +
              "5. View All Projects\n" +
              "6. Filter My Projects\n" +
              "7. View Current Project\n" +
              "8. View Pending Officer Registrations\n" +
              "9. Approve/Reject Officer Registrations\n" +
              "10. View Applicant Applications\n" +
              "11. Approve/Reject Applicant Applications\n" +
              "12. Approve/Reject Withdrawal Requests\n" +
              "13. View All Project Enquiries\n" +
              "14. Reply to Project Enquiries\n" +
              "15. Generate Applicant/Booking Reports\n" +
              "16. Change Password\n" +
              "0. Logout\n" +
              "======================================\n" +
              "Please enter your choice:", Color.CYAN);
   }

   /**
    * Handles user input and routes to appropriate handler methods.
    */
   protected void handleInput() {
      String input = scanner.nextLine();

      switch (input) {
         case "1" -> {
            handleCreateProject();
         }
         case "2" -> {
            handleEditProject();
         }
         case "3" -> {
            handleDeleteProject();
         }
         case "4" -> {
            handleToggleVisibility();
         }
         case "5" -> {
            handleViewAllProjects();
         }
         case "6" -> {
            handleFilterMyProjects();
         }
         case "7" -> {
            handleViewCurrentProject();
         }
         case "8" -> {
            handleViewPendingOfficerRegistrations();
         }
         case "9" -> {
            handleApproveRejectOfficerRegistrations();
         }
         case "10" -> {
            handleViewApplicantApplications();
         }
         case "11" -> {
            handleApproveRejectApplicantApplications();
         }
         case "12" -> {
            handleApproveRejectWithdrawalRequests();
         }
         case "13" -> {
            handleViewAllProjectEnquiries();
         }
         case "14" -> {
            handleReplyToProjectEnquiries();
         }
         case "15" -> {
            handleGenerateApplicantBookingReports();
         }
         case "16" -> {
            handleChangePassword();
         }
         case "0" -> {
            sessionManager.logout();
         }
         default -> {
            Color.println("Invalid choice", Color.RED);
         }
      }
   }

   /**
    * Main method to run the manager menu loop while user is logged in.
    */
   @Override
   public void run() {
      while (sessionManager.isLoggedIn()) {
         display();
         handleInput();
      }
   }

   /**
    * Handles the creation of a new BTO project by collecting required information from the user.
    */
   private void handleCreateProject() {
      try {
         Color.print("Enter Project Name:", Color.GREEN);
         String name = scanner.nextLine();

         Color.print("Enter Project Neighbourhood:", Color.GREEN);
         String neighbourhood = scanner.nextLine();

         Color.print("Enter Two Room Flat Count:", Color.GREEN);
         int twoRoomFlatCount = Integer.parseInt(scanner.nextLine());

         Color.print("Enter Two Room Flat Price:", Color.GREEN);
         double twoRoomPrice = Double.parseDouble(scanner.nextLine());

         Color.print("Enter Three Room Flat Count:", Color.GREEN);
         int threeRoomFlatCount = Integer.parseInt(scanner.nextLine());

         Color.print("Enter Three Room Flat Price:", Color.GREEN);
         double threeRoomPrice = Double.parseDouble(scanner.nextLine());

         Color.print("Enter Project Application Opening Date (yyyy-MM-dd):", Color.GREEN);
         LocalDate applicationOpeningDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);

         Color.print("Enter Project Application Closing Date (yyyy-MM-dd):", Color.GREEN);
         LocalDate applicationClosingDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);


         Color.print("Enter Number of Officer Slots Available:", Color.GREEN);
         int officerSlots = Integer.parseInt(scanner.nextLine());

         try {
            managerController.createProject(name, neighbourhood, twoRoomFlatCount, twoRoomPrice, threeRoomFlatCount, threeRoomPrice,
                    applicationOpeningDate, applicationClosingDate, sessionManager.getCurrentUser().getName(), officerSlots, new ArrayList<>());
            Color.println("Project created successfully!", Color.GREEN);
         }
         catch (IllegalArgumentException e) {
            Color.println("Project Creation Failed, An error occurred: " + e.getMessage(), Color.RED);
         }
      }
      catch (NumberFormatException e) {
         Color.println("Invalid number format. Please try again.", Color.RED);
      }
      catch (DateTimeParseException e) {
         Color.println("Invalid date format. Please use yyyy-MM-dd format.", Color.RED);
      }
      catch (Exception e) {
         Color.println("Project Creation Failed. An error occurred." + e.getMessage(), Color.RED);
      }
   }

   /**
    * Handles the deletion of an existing BTO project.
    */
   private void handleDeleteProject() {
      Color.print("Enter Project ID to delete:", Color.GREEN);
      int projectId = Integer.parseInt(scanner.nextLine());

      if (managerController.deleteProject(projectId)) {
         Color.println("Project deleted successfully!", Color.GREEN);
      }
      else {
         Color.println("Project deletion failed.", Color.RED);
      }
   }

   /**
    * Displays all BTO projects in a tabular format.
    * this is updated to use table printer
    */
   private void handleViewAllProjects() {

      try {
         List<List<String>> tableData = managerController.getAllProjectsTableData();
         if (tableData.isEmpty()) {
            Color.println("No projects found.", Color.RED);
            return;
         }
         Color.println("---  All Projects ---", Color.YELLOW);
         Integer COLUMN_WIDTH = 15;
         tablePrinter.printTable(COLUMN_WIDTH, tableData); //old way
      }
      catch (Exception e) {
         Color.println("Error: " + e.getMessage(), Color.RED);
      }
   }

   /**
    * Displays projects created by the current manager.
    */
   private void handleFilterMyProjects() {

      try {
         List<List<String>> tableData = managerController.getMyProjectsTableData();
         if (tableData.isEmpty()) {
            Color.println("No projects found.", Color.RED);
            Color.println("You have not created any projects yet.", Color.RED);
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

   /**
    * Displays details of the currently selected project.
    */
   private void handleViewCurrentProject() {
      try {
         Color.println(managerController.getCurrentProjectData(), Color.YELLOW);
      }
      catch (Exception e) {
         Color.println("Error viewing current project: " + e.getMessage(), Color.RED);
      }
   }

   /**
    * Toggles the visibility of a project to make it available or unavailable to applicants.
    */
   private void handleToggleVisibility() {
      try {
         Color.println("My Projects:", Color.GREEN);

         handleFilterMyProjects();
         Color.print("Enter Project ID to toggle visibility:", Color.GREEN);
         Integer projectId = Integer.parseInt(scanner.nextLine());
         if (projectId == null) {
            Color.println("Invalid Project ID.", Color.RED);
            return;
         }
         if (managerController.toggleVisibility(projectId)) {
            Color.println("Visibility toggled successfully!", Color.GREEN);
         }
         else {
            Color.println("Visibility toggle failed.", Color.RED);
         }
      }
      catch (Exception e) {
         Color.println("Error: " + e.getMessage(), Color.RED);
      }

   }

   /**
    * Handles editing of an existing BTO project's properties.
    */
   private void handleEditProject() {
      Color.println("My Projects:", Color.GREEN);
      managerController.getMyProjectsTableData();//prints my projects on screen
      Color.print("Enter Project ID to edit (0 to exit):", Color.GREEN);
      Integer projectName = Integer.parseInt(scanner.nextLine());
      if (projectName.equals(0)) {
         Color.println("Returning to Manager Menu.", Color.RED);
         return;
      }

      if (projectName == null) {
         Color.println("Invalid Project ID.", Color.RED);
         return;
      }

      while (true) {
         Color.print("Editing Options:\n 1. Name, 2. Neighbourhood, " +
                 "3. Application Opening Date, 4. Application Closing Dates, " +
                 "5. Visibility, 6. Exit\n" +
                 "Enter your choice:", Color.GREEN);
         String editOption = scanner.nextLine();
         switch (editOption) {
            case "1" -> {
               Color.print("Enter New Project Name:", Color.GREEN);
               String newName = scanner.nextLine();
               managerController.editProject(projectName, "1", newName);
            }
            case "2" -> {
               Color.print("Enter New Project Neighbourhood:", Color.GREEN);
               String newNeighbourhood = scanner.nextLine();
               managerController.editProject(projectName, "2", newNeighbourhood);
            }
            case "3" -> {
               Color.print("Enter New Application Opening Date (yyyy-MM-dd):", Color.GREEN);
               LocalDate newApplicationOpeningDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
               managerController.editProject(projectName, "3", newApplicationOpeningDate);
            }
            case "4" -> {
               Color.print("Enter New Application Closing Date (yyyy-MM-dd):", Color.GREEN);
               LocalDate newApplicationClosingDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
               managerController.editProject(projectName, "4", newApplicationClosingDate);
            }
            case "5" -> {
               Color.print("Enter New Visibility (true/false):", Color.GREEN);
               boolean newVisibility = Boolean.parseBoolean(scanner.nextLine());
               managerController.editProject(projectName, "5", newVisibility);
            }
            case "6" -> {
               Color.println("Exiting Edit Menu.", Color.BLUE);
               return;
            }
            default -> {
               Color.println("Invalid option. Edit cancelled.", Color.RED);
            }
         }
      }

   }

   /**
    * Displays pending officer registration requests.
    *
    * @return Boolean indicating if there are pending registrations
    */
   private Boolean handleViewPendingOfficerRegistrations() {
      try {
         List<List<String>> tableData = managerController.getPendingRegistrationTableData();
         if (tableData == null || tableData.isEmpty()) {
            Color.println("No pending officer registrations.", Color.RED);
            return false;
         }
         Integer COLUMN_WIDTH = 15;
         Color.println("--- Viewing Pending Officer Registrations ---", Color.YELLOW);
         tablePrinter.printTable(COLUMN_WIDTH, tableData);
         return true;
      }
      catch (Exception e) {
         Color.println("Error viewing pending officer registrations.\nPossible Reason:" + e.getMessage(), Color.RED);
      }
      return false;
   }

   /**
    * Handles approving or rejecting officer registration requests.
    */
   private void handleApproveRejectOfficerRegistrations() {
      try {
         if (handleViewPendingOfficerRegistrations()) {

            Color.print("Enter Registration ID or Officer Name to approve/reject:", Color.GREEN);
            String identifier = scanner.nextLine();

            Color.print("Enter 'A' to approve or 'R' to reject:", Color.GREEN);
            String action = scanner.nextLine().toUpperCase();

            if (action.equals("A") || action.equals("R")) {

               RegistrationStatus isApproved = action.equals("A") ? RegistrationStatus.APPROVED : RegistrationStatus.REJECTED;

               // Let the controller handle the type checking and processing
               boolean success = managerController.processOfficerRegistration(identifier, isApproved);

               if (success) {
                  Color.println("Registration " + isApproved + " successfully!", Color.GREEN);
               }
               else {
                  Color.println("Failed to process registration. Registration may not exist or already processed. Check the registration ID or Officer Name.", Color.RED);
               }
            }
            else {
               Color.println("Invalid action. Please enter 'A' or 'R'.", Color.RED);
            }
         }

      }
      catch (Exception e) {
         Color.println("Error processing registration: " + e.getMessage(), Color.RED);
      }
   }

   /**
    * Displays all applicant applications in a tabular format.
    */
   private void handleViewApplicantApplications() {

      try {
         List<List<String>> tableData = managerController.getApplicantApplicationsTableData();
         if (tableData == null || tableData.isEmpty()) {
            Color.println("No applicant applications.", Color.RED);
            return;
         }
         Integer COLUMN_WIDTH = 15;
         Color.println("--- Viewing Applicant Applications ---", Color.YELLOW);
         tablePrinter.printTable(COLUMN_WIDTH, tableData);

      }
      catch (Exception e) {
         Color.println("Error viewing applicant applications.\nPossible Reason:" + e.getMessage(), Color.RED);
      }
   }

   /**
    * Handles approving or rejecting applicant applications.
    */
   private void handleApproveRejectApplicantApplications() {
      // TODO: Implement this method to handle approving or rejecting applicant applications
      // You can use the managerController to fetch the data and handle the approval/rejection process
      // You can use the Color class for console output formatting
      // You can use the scanner for user input
      // You can use the tablePrinter for printing the table
      try {
         handleViewApplicantApplications();
         Color.print("Enter Application ID to approve or reject:", Color.GREEN);
         String applicationId = scanner.nextLine();
         Color.print("Enter 'A' to approve or 'R' to reject:", Color.GREEN);
         String action = scanner.nextLine().toUpperCase();


         if (action.equals("A") || action.equals("R")) {
            boolean isApproved = action.equals("A");
            boolean success = managerController.approveOrRejectApplicantApplication(applicationId, isApproved);
            if (success) {
               Color.println("Application " + (isApproved ? "approved" : "rejected") + " successfully!", Color.GREEN);
            }
            else {
               Color.println("Failed to process application. Application may not exist or already processed. Check the Application ID.", Color.RED);
            }
         }
         else {
            Color.println("Invalid action. Please enter 'A' or 'R'.", Color.RED);
         }
      }
      catch (Exception e) {
         Color.println("Error processing applicant application: " + e.getMessage(), Color.RED);
      }

   }

   /**
    * Handles changing the manager's password.
    */
   public void handleChangePassword() {
      List<String> inputs = super.getInputsChangePassword();
      try {
         managerController.handlePasswordChange(inputs.get(0), inputs.get(1), inputs.get(2));
         Color.println("Password changed successfully.", Color.GREEN);
      }
      catch (IllegalArgumentException e) {
         Color.println("Password change error: " + e.getMessage(), Color.RED);
      }
      catch (Exception e) {
         Color.println("An unexpected error occurred during password change: " + e.getMessage(), Color.RED);
      }
   }

   /**
    * Displays all project enquiries in a tabular format.
    */
   private void handleViewAllProjectEnquiries() {
      try {
         List<List<String>> tableData = managerController.getProjectEnquiriesTableData();
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

   /**
    * Handles replying to project enquiries.
    */
   private void handleReplyToProjectEnquiries() {
      try {
         handleViewAllProjectEnquiries();
         Color.print("Enter Enquiry ID to reply:", Color.GREEN);
         String enquiryId = scanner.nextLine();
         Color.print("Enter Reply:", Color.GREEN);
         String reply = scanner.nextLine();
         boolean success = managerController.replyToProjectEnquiry(enquiryId, reply);
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

   /**
    * Handles approving or rejecting withdrawal requests.
    */
   private void handleApproveRejectWithdrawalRequests() {
      try {
         handleViewAllWithdrawalRequests();
         Color.print("Enter Withdrawal Request ID to approve or reject:", Color.GREEN);
         String withdrawalRequestId = scanner.nextLine();
         Color.print("Enter 'A' to approve or 'R' to reject:", Color.GREEN);
         String action = scanner.nextLine().toUpperCase();
         if (action.equals("A") || action.equals("R")) {
            boolean isApproved = action.equals("A");
            boolean success = managerController.approveOrRejectWithdrawalRequest(withdrawalRequestId, isApproved);
            if (success) {
               Color.println("Withdrawal request " + (isApproved ? "approved" : "rejected") + " successfully!", Color.GREEN);
            }
            else {
               Color.println("Failed to process withdrawal request. Request may not exist or already processed. Check the Withdrawal Request ID.", Color.RED);
            }
         }
         else {
            Color.println("Invalid action. Please enter 'A' or 'R'.", Color.RED);
         }
      }
      catch (Exception e) {
         Color.println("Error processing withdrawal request: " + e.getMessage(), Color.RED);
      }
   }

   /**
    * Displays all withdrawal requests in a tabular format.
    */
   private void handleViewAllWithdrawalRequests() {
      try {
         List<List<String>> tableData = managerController.getWithdrawalRequestsTableData();
         if (tableData == null || tableData.isEmpty()) {
            Color.println("No withdrawal requests.", Color.RED);
            return;
         }
         Integer COLUMN_WIDTH = 15;
         Color.println("--- Viewing Withdrawal Requests ---", Color.YELLOW);
         tablePrinter.printTable(COLUMN_WIDTH, tableData);
      }
      catch (Exception e) {
         Color.println("Error viewing withdrawal requests.\nPossible Reason:" + e.getMessage(), Color.RED);
      }
   }

   /**
    * Handles generating and displaying applicant and booking reports with various filtering options.
    */
   private void handleGenerateApplicantBookingReports() {
      try {
         Color.println("=== Generate Applicant/Booking Reports ===", Color.YELLOW);
         Color.println("Filter Options:", Color.CYAN);
         Color.println("1. All Applicants", Color.CYAN);
         Color.println("2. Filter by Flat Type", Color.CYAN);
         Color.println("3. Filter by Project", Color.CYAN);
         Color.println("4. Filter by Marital Status", Color.CYAN);
         Color.println("5. Filter by Age Range", Color.CYAN);
         Color.println("0. Back to Menu", Color.CYAN);
         Color.print("Enter your choice: ", Color.GREEN);

         String choice = scanner.nextLine();

         switch (choice) {
            case "1" -> {
               List<List<String>> reportData = managerController.generateApplicantReport(null);
               displayReportData(reportData, "All Applicants Report");
            }
            case "2" -> {
               Color.println("Flat Types: 1. Two Room, 2. Three Room", Color.CYAN);
               Color.print("Enter flat type (1 or 2): ", Color.GREEN);
               String flatType = scanner.nextLine();
               if (flatType.equals("1") || flatType.equals("2")) {
                  List<List<String>> reportData = managerController.generateApplicantReport(
                          Map.of("filterType", "flatType", "value", flatType.equals("1") ? "TWO_ROOM" : "THREE_ROOM"));
                  displayReportData(reportData, "Applicants Report - Filtered by Flat Type");
               }
               else {
                  Color.println("Invalid flat type selection.", Color.RED);
               }
            }
            case "3" -> {
               Color.println("Available Projects:", Color.CYAN);
               managerController.getAllProjectsTableData(); // Display projects for reference
               Color.print("Enter project ID: ", Color.GREEN);
               String projectId = scanner.nextLine();
               List<List<String>> reportData = managerController.generateApplicantReport(
                       Map.of("filterType", "project", "value", projectId));
               displayReportData(reportData, "Applicants Report - Filtered by Project");
            }
            case "4" -> {
               Color.println("Marital Status: 1. Single, 2. Married", Color.CYAN);
               Color.print("Enter marital status (1 or 2): ", Color.GREEN);
               String maritalStatus = scanner.nextLine();
               if (maritalStatus.equals("1") || maritalStatus.equals("2")) {
                  List<List<String>> reportData = managerController.generateApplicantReport(
                          Map.of("filterType", "maritalStatus", "value", maritalStatus.equals("1") ? "SINGLE" : "MARRIED"));
                  displayReportData(reportData, "Applicants Report - Filtered by Marital Status");
               }
               else {
                  Color.println("Invalid marital status selection.", Color.RED);
               }
            }
            case "5" -> {
               Color.print("Enter minimum age: ", Color.GREEN);
               String minAge = scanner.nextLine();
               Color.print("Enter maximum age: ", Color.GREEN);
               String maxAge = scanner.nextLine();
               try {
                  int min = Integer.parseInt(minAge);
                  int max = Integer.parseInt(maxAge);
                  if (min > max) {
                     Color.println("Minimum age cannot be greater than maximum age.", Color.RED);
                     return;
                  }
                  List<List<String>> reportData = managerController.generateApplicantReport(
                          Map.of("filterType", "ageRange", "minAge", minAge, "maxAge", maxAge));
                  displayReportData(reportData, "Applicants Report - Filtered by Age Range (" + minAge + "-" + maxAge + ")");
               }
               catch (NumberFormatException e) {
                  Color.println("Invalid age input. Please enter numeric values.", Color.RED);
               }
            }
            case "0" -> {
               Color.println("Returning to main menu.", Color.BLUE);
            }
            default -> {
               Color.println("Invalid choice.", Color.RED);
            }
         }
      }
      catch (Exception e) {
         Color.println("Error generating report: " + e.getMessage(), Color.RED);
      }
   }

   /**
    * Displays report data in a tabular format.
    *
    * @param reportData The report data to display
    * @param title      The title of the report
    */
   private void displayReportData(List<List<String>> reportData, String title) {
      if (reportData == null || reportData.isEmpty()) {
         Color.println("No data available for the selected filter criteria.", Color.RED);
         return;
      }

      Color.println("--- " + title + " ---", Color.YELLOW);
      Integer COLUMN_WIDTH = 15;
      tablePrinter.printTable(COLUMN_WIDTH, reportData);
   }

}//Menu ends here