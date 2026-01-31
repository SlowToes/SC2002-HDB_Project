package applicant;

import enquiry.Enquiry;
import enquiry.EnquiryService;
import helper.Color;
import helper.TablePrinter;
import interfaces.Menu;
import project.FlatType;
import project.Project;
import project.UserFilterManager;
import system.ServiceRegistry;
import system.SessionManager;
import user.User;

import java.util.*;

public class ApplicantMenu extends Menu {
   private final TablePrinter tablePrinter;
   private final SessionManager sessionManager;
   private final ApplicantController applicantController;
   private final UserFilterManager userFilterManager;
   private final Scanner scanner;
   private final EnquiryService enquiryService = ServiceRegistry.get(EnquiryService.class);

   //scanner, tablePrinter, sessionManager, applicantController
   public ApplicantMenu(Scanner scanner, TablePrinter tablePrinter, SessionManager sessionManager,
                        ApplicantController applicantController) {
      super(scanner);
      this.scanner = scanner;
      this.tablePrinter = tablePrinter;
      this.sessionManager = sessionManager;
      this.applicantController = applicantController;
      this.userFilterManager = ServiceRegistry.get(UserFilterManager.class);
   }

   @Override
   public void display() {
      Color.print("""
                      ================================ Applicant Menu =================================
               1. View Available Projects     |     2. Apply for BTO Project     |     3. View My Application Status
               4. Request Withdrawal          |     5. Submit Project Enquiry    |     6. View My Enquiries
               7. Edit My Enquiry             |     8. Delete My Enquiry         |     9. Change Password
               0. Logout
              =======================================================================================
              Please enter your choice:""", Color.CYAN);
   }

   @Override
   public void handleInput() {
      // Keep asking until valid input
      try {
         String input = scanner.nextLine().trim();

         // Check if input is a single digit (0-9)
         if (!input.matches("[0-9]")) {
            Color.println("Error: Please enter a digit (0-9)", Color.RED);
         }

         // Process valid input
         switch (input) {
            case "1" -> handleViewAvailableProjects();
            case "2" -> handleApplyForBTOProject();
            case "3" -> handleViewApplicationStatus();
            case "4" -> handleRequestWithdrawal();
            case "5" -> handleSubmitEnquiry();
            case "6" -> handleViewMyEnquiries();
            case "7" -> handleEditEnquiry();
            case "8" -> handleDeleteEnquiry();
            case "9" -> handleChangePassword();
            case "0" -> {
               sessionManager.logout();
               return;  // Exit the loop after logout
            }
            // Add a default case for unexpected valid digits if needed
            default -> Color.println("Invalid choice. Please enter a number between 0 and 9.", Color.RED);
         }

      }
      catch (NoSuchElementException e) {
         Color.println("Error: Input stream closed", Color.RED);
         return;
      }
      catch (IllegalStateException e) {
         Color.println("Error: Scanner is closed", Color.RED);
         return;
      }

   }

   public void handleViewAvailableProjects() {
      try {
         List<Project> allProjects = applicantController.getEligibleProjects();
         if (allProjects.isEmpty()) {
            Color.println("No projects found.", Color.RED);
            return;
         }

         User currentUser = sessionManager.getCurrentUser();
         userFilterManager.manageFilters(currentUser);
         List<Project> filteredProjects = userFilterManager.applyFilters(allProjects, currentUser);

         if (filteredProjects.isEmpty()) {
            Color.println("No projects match your filter.", Color.RED);
         }
         else {
            Color.println("--- Filtered Projects ---", Color.YELLOW);
            printProjectsTable(filteredProjects);
         }
      }
      catch (Exception e) {
         Color.println("Error: " + e.getMessage(), Color.RED);
      }
   }

   private void printProjectsTable(List<Project> projects) {
      Integer COLUMN_WIDTH = 15;
      List<List<String>> tableData = new ArrayList<>();
      tableData.add(List.of(
              "Project ID", "Project Name", "Neighbourhood", "Visibility",
              "Two Room Units", "Two Room Price", "Three Room Units", "Three Room Price",
              "Opening Date", "Closing Date"
      ));
      for (Project p : projects) {
         // Use the existing toStringAsList method from Project
         // Make sure toStringAsList() in Project.java includes all these columns in the correct order
         // and handles visibility correctly (e.g., "Visible"/"Hidden").
         // Also ensure prices are formatted if needed (e.g., String.format("$%.2f", price))
         // The current toStringAsList seems okay based on the context provided.
         tableData.add(p.toStringAsList().subList(0, 10)); // Take only the first 10 columns for this view
      }
      tablePrinter.printTable(COLUMN_WIDTH, tableData);
   }

   public void handleApplyForBTOProject() {
      try {
         if (applicantController.hasCurrentApplication()) {
            Color.println("You already have an active application or booking and cannot apply for another.", Color.RED);
            return;
         }

         List<Project> eligibleProjects = applicantController.getEligibleProjects();
         if (eligibleProjects.isEmpty()) {
            Color.println("No eligible projects available for application at the moment.", Color.YELLOW);
            return;
         }

         Color.println("--- Eligible Projects for Application ---", Color.YELLOW);
         printProjectsTable(eligibleProjects); // Display projects they can apply for

         Project selectedProject; // Declare here to store the chosen project

         // Loop until a valid project is selected or user cancels
         while (true) {
            Color.print("Enter the project ID you want to apply for (0 to cancel): ", Color.CYAN);
            String input = scanner.nextLine().trim();
            if (input.equals("0")) {
               Color.println("Application cancelled. Returning to Applicant Menu.", Color.YELLOW);
               return;
            }

            // Call the controller method to validate input against eligible projects
            Optional<Project> foundProjectOpt = applicantController.validateUserApplicationInput(eligibleProjects, input);

            // Check if the Optional contains a project
            if (foundProjectOpt.isPresent()) {
               selectedProject = foundProjectOpt.get(); // Assign the found project
               break; // Exit the loop, valid project found
            }
            else {
               // Project not found in the eligible list by ID
               Color.println("Error: Project with ID '" + input + "' not found in the eligible list or input is invalid. Please try again.", Color.RED);
               // Loop continues to ask for input again
            }
         }

         // --- Project selected, now choose flat type ---
         Color.println("Selected Project: " + selectedProject.getProjectName(), Color.GREEN); // Confirm selection

         FlatType selectedFlatType;

         // Use remainingFlats map from the *selectedProject* for checking availability
         int twoRoomUnits = selectedProject.getRemainingFlats().getOrDefault(FlatType.TWO_ROOM, 0);
         int threeRoomUnits = selectedProject.getRemainingFlats().getOrDefault(FlatType.THREE_ROOM, 0);

         // Check if *any* units are available before proceeding
         if (twoRoomUnits <= 0 && threeRoomUnits <= 0) {
            Color.println("Sorry, no flats (TWO_ROOM or THREE_ROOM) are currently available in project '" + selectedProject.getProjectName() + "'.", Color.RED);
            return;
         }

         if (twoRoomUnits > 0 && threeRoomUnits > 0) {
            // Loop until valid flat type is chosen
            label:
            while (true) {
               Color.println("Project '" + selectedProject.getProjectName() + "' has both TWO-ROOM (" + twoRoomUnits + " left) and THREE-ROOM (" + threeRoomUnits + " left) flats available.", Color.CYAN);
               Color.print("Enter your choice (1 = TWO_ROOM, 2 = THREE_ROOM, 0 to cancel): ", Color.CYAN);
               String flatChoiceInput = scanner.nextLine().trim();
               switch (flatChoiceInput) {
                  case "1":
                     selectedFlatType = FlatType.TWO_ROOM;
                     break label;
                  case "2":
                     selectedFlatType = FlatType.THREE_ROOM;
                     break label;
                  case "0":
                     Color.println("Application cancelled.", Color.YELLOW);
                     return;
                  default:
                     Color.println("Invalid choice. Please enter 1, 2, or 0.", Color.RED);
                     break;
               }
            }
         }
         else if (twoRoomUnits > 0) {
            Color.println("Only TWO-ROOM flats (" + twoRoomUnits + " left) are available for project '" + selectedProject.getProjectName() + "'.", Color.YELLOW);
            selectedFlatType = FlatType.TWO_ROOM;
            Color.print("Proceed with applying for a TWO-ROOM flat? (Y/N): ", Color.CYAN);
            if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
               Color.println("Application cancelled.", Color.YELLOW);
               return;
            }
         }
         else { // Only threeRoomUnits > 0
            Color.println("Only THREE-ROOM flats (" + threeRoomUnits + " left) are available for project '" + selectedProject.getProjectName() + "'.", Color.YELLOW);
            selectedFlatType = FlatType.THREE_ROOM;
            Color.print("Proceed with applying for a THREE-ROOM flat? (Y/N): ", Color.CYAN);
            if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
               Color.println("Application cancelled.", Color.YELLOW);
               return;
            }
         }

         // Apply
         applicantController.applyForProject(selectedProject.getId(), selectedFlatType);
         Color.println("Application submitted successfully for a " + selectedFlatType + " flat in project '" + selectedProject.getProjectName() + "'!", Color.GREEN);

      }
      catch (IllegalArgumentException e) { // Catch specific errors from controller/logic
         Color.println("Application Error: " + e.getMessage(), Color.RED);
      }
      catch (Exception e) { // Catch broader unexpected errors
         Color.println("An unexpected error occurred during application: " + e.getMessage(), Color.RED);
         e.printStackTrace(); // Print stack trace for debugging unexpected errors
      }
   }


   public void handleViewApplicationStatus() {
      try {
         List<Application> myApplications = applicantController.getMyApplications();

         if (myApplications.isEmpty()) {
            Color.println("You have not submitted any applications.", Color.RED);
            return;
         }

         List<List<String>> tableData = new ArrayList<>();
         // Add more relevant columns if needed, e.g., Application ID, Flat Type, Booking Status
         tableData.add(List.of("App ID", "Project Name", "Flat Type", "Date Applied", "App Status", "Booking Status", "Withdrawal"));

         for (Application app : myApplications) {
            tableData.add(List.of(
                    String.valueOf(app.getId()),
                    app.getProjectName(),
                    app.getFlatType().toString(),
                    app.getDateApplied().toString(),
                    app.getApplicationStatus().toString(),
                    app.getBookingStatus().toString(),
                    app.getWithdrawalRequestStatus().toString()
            ));
         }

         Integer COLUMN_WIDTH = 18; // Adjust width if needed
         Color.println("--- My Applications ---", Color.YELLOW);
         tablePrinter.printTable(COLUMN_WIDTH, tableData);

      }
      catch (Exception e) {
         Color.println("Error retrieving application status: " + e.getMessage(), Color.RED);
      }
   }

   public void handleRequestWithdrawal() {
      try {
         List<Application> myApplications = applicantController.getMyApplications();
         List<Application> withdrawableApplications = myApplications.stream()
                 .filter(app -> app.getApplicationStatus() == ApplicationStatus.PENDING ||
                         app.getApplicationStatus() == ApplicationStatus.SUCCESSFUL ||
                         app.getBookingStatus() == BookingStatus.BOOKED ||
                         app.getBookingStatus() == BookingStatus.PENDING) // Only pending or successful can be withdrawn
                 .filter(app -> app.getWithdrawalRequestStatus() == WithdrawalRequestStatus.NOT_REQUESTED) // Not already pending withdrawal
                 .toList();


         if (withdrawableApplications.isEmpty()) {
            Color.println("You do not have any application eligible for withdrawal.", Color.RED);
            return;
         }

         // this should be the case
         Application applicationToWithdraw = withdrawableApplications.getFirst();
         Color.println("--- Selected Application ---", Color.YELLOW);

         // Show selected application details
         Color.println("Application ID    : " + applicationToWithdraw.getId(), Color.CYAN);
         Color.println("Project Name      : " + applicationToWithdraw.getProjectName(), Color.CYAN);
         Color.println("Flat Type         : " + applicationToWithdraw.getFlatType(), Color.CYAN);
         Color.println("Date Applied      : " + applicationToWithdraw.getDateApplied(), Color.CYAN);
         Color.println("Application Status: " + applicationToWithdraw.getApplicationStatus(), Color.CYAN);
         Color.println("-------------------------------------------", Color.YELLOW);

         // Ask for confirmation
         Color.print("Are you sure you want to request withdrawal for this application? (Y/N): ", Color.CYAN);
         String confirm = scanner.nextLine().trim().toUpperCase();

         if (confirm.equals("Y")) {
            applicantController.requestWithdrawal(applicationToWithdraw.getId());
            Color.println("Withdrawal request submitted for Application ID: " + applicationToWithdraw.getId(), Color.GREEN);
         }
         else {
            Color.println("Withdrawal cancelled.", Color.YELLOW);
         }

      }
      catch (NumberFormatException e) {
         Color.println("Invalid input. Please enter a number.", Color.RED);
      }
      catch (IllegalArgumentException e) {
         Color.println("Error: " + e.getMessage(), Color.RED);
      }
      catch (Exception e) {
         Color.println("Unexpected error during withdrawal request: " + e.getMessage(), Color.RED);
      }
   }

   public void handleSubmitEnquiry() {
      try {
         // Get projects available for enquiry (could be all visible, or just eligible)
         // Using eligible projects for consistency with apply logic
         List<Project> availableProjects = applicantController.getEligibleProjects();
         if (availableProjects.isEmpty()) {
            Color.println("No projects currently available to enquire about.", Color.YELLOW);
            return;
         }

         Color.println("--- Projects Available for Enquiry ---", Color.YELLOW);
         printProjectsTable(availableProjects); // Display projects with IDs

         Project selectedProject; // Declare here

         // Loop until a valid project ID is entered or user cancels
         while (true) {
            Color.print("Enter the Project ID to enquire about (0 to cancel): ", Color.CYAN);
            String input = scanner.nextLine().trim();
            if (input.equals("0")) {
               Color.println("Enquiry cancelled.", Color.YELLOW);
               return;
            }

            Optional<Project> foundProjectOpt;
            try {
               int projectId = Integer.parseInt(input);
               // Search within the availableProjects list by ID
               foundProjectOpt = availableProjects.stream()
                       .filter(p -> p.getId().equals(projectId))
                       .findFirst();
            }
            catch (NumberFormatException e) {
               // Input was not a valid integer
               foundProjectOpt = Optional.empty();
            }

            // Check if the Optional contains a project
            if (foundProjectOpt.isPresent()) {
               selectedProject = foundProjectOpt.get(); // Assign the found project
               break; // Exit the loop, valid project found
            }
            else {
               // Project ID not found in the list or input was not a valid number
               Color.println("Error: Project with ID '" + input + "' not found in the list or input is invalid. Please enter a valid Project ID.", Color.RED);
               // Loop continues
            }
         }

         // --- Project selected, now get enquiry text ---
         Color.println("Selected Project: " + selectedProject.getProjectName(), Color.GREEN);
         Color.print("Enter your enquiry for this project (max 200 chars): ", Color.CYAN);
         String enquiryText = scanner.nextLine().trim();

         // Basic validation for enquiry text
         if (enquiryText.isEmpty()) {
            Color.println("Enquiry text cannot be empty.", Color.RED);
            return; // Or loop back to ask again
         }
         if (enquiryText.length() > 200) { // Example length limit
            Color.println("Enquiry text is too long (max 200 characters). Please try again.", Color.RED);
            return; // Or loop back to ask again
         }

         // Submit the enquiry via the controller
         applicantController.submitEnquiry(selectedProject.getId(), enquiryText);
         Color.println("Enquiry submitted successfully for project '" + selectedProject.getProjectName() + "'.", Color.GREEN);

      }
      catch (
              IllegalArgumentException e) { // Catch specific errors from controller (e.g., project not found by ID in service)
         Color.println("Error submitting enquiry: " + e.getMessage(), Color.RED);
      }
      catch (Exception e) { // Catch broader unexpected errors
         Color.println("An unexpected error occurred while submitting enquiry: " + e.getMessage(), Color.RED);
         e.printStackTrace(); // For debugging
      }
   }

   public void handleViewMyEnquiries() {
      try {
         List<List<String>> enquiryData = applicantController.getMyEnquiriesAsTableData();

         if (enquiryData == null || enquiryData.size() <= 1) { // Check size > 1 because index 0 is header
            Color.println("No enquiries found.", Color.RED);
            return;
         }

         Color.println("--- My Enquiries ---", Color.YELLOW);
         Integer COLUMN_WIDTH = 20; // Adjust width as needed
         tablePrinter.printTable(COLUMN_WIDTH, enquiryData);
      }
      catch (IllegalStateException e) { // Catch specific exception from controller if user isn't applicant
         Color.println("Error: " + e.getMessage(), Color.RED);
      }
      catch (Exception e) {
         Color.println("Error retrieving enquiries: " + e.getMessage(), Color.RED);
      }
   }


   public void handleEditEnquiry() {
      try {
         List<Enquiry> enquiries = applicantController.getMyEnquiries();
         // Filter directly here for better user experience
         List<Enquiry> editableEnquiries = enquiries.stream()
                 // Allow editing if reply is null or just whitespace
                 .filter(e -> e.getReply() == null || e.getReply().isBlank())
                 .toList();

         if (editableEnquiries.isEmpty()) {
            Color.println("You have no enquiries that can be edited (either none exist or they have already been replied to).", Color.YELLOW);
            return;
         }

         // Use helper method to select enquiry
         Enquiry selectedEnquiry = selectEnquiryFromList(editableEnquiries, "edit");
         if (selectedEnquiry == null) {
            // User cancelled selection in helper method
            Color.println("Selected Enquiry cannot be edited.", Color.YELLOW);
            return;
         }

         /*// Get new enquiry text
         String newEnquiryText = getValidatedEnquiryInput(selectedEnquiry.getEnquiry());
         if (newEnquiryText == null) {
            // User cancelled or input was invalid/unchanged
            return;
         }*/


         // Get new enquiry text
         Color.print("Enter new enquiry text (max 200 chars): ", Color.CYAN);
         String newEnquiryText = scanner.nextLine().trim();
         if (newEnquiryText.isEmpty()) {
            Color.println("Enquiry text cannot be empty.", Color.RED);
            return;
         }
         if (newEnquiryText.length() > 200) { // Example length limit
            Color.println("Enquiry text is too long (max 200 characters). Please try again.", Color.RED);
            return;
         }

         // Confirmation step
         Color.print("Confirm replacing the old enquiry with the new text? (Y/N): ", Color.CYAN);
         if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
            Color.println("Edit cancelled.", Color.YELLOW);
            return;
         }

         // Call controller to perform the edit
         applicantController.editEnquiry(selectedEnquiry, newEnquiryText);
         Color.println("Enquiry ID " + selectedEnquiry.getId() + " updated successfully.", Color.GREEN);

      }
      catch (
              IllegalArgumentException e) { // Catch errors from controller (e.g., enquiry not found, already replied)
         Color.println("Error editing enquiry: " + e.getMessage(), Color.RED);
      }
      catch (Exception e) { // Catch unexpected errors
         Color.println("Unexpected error during enquiry edit: " + e.getMessage(), Color.RED);
         e.printStackTrace(); // For debugging
      }
   }

   //private methods
   private Enquiry selectEnquiryFromList(List<Enquiry> enquiries, String action) {
      Color.println("--- Your Enquiries Available to " + action.toUpperCase() + " ---", Color.YELLOW);

      List<List<String>> tableData = new ArrayList<>();
      for (Enquiry enquiry : enquiries) {
         tableData.add(Arrays.asList(
                 String.valueOf(enquiry.getId()),
                 enquiry.getProjectName(),
                 enquiry.getEnquiry(),
                 enquiry.getReply()
         ));
         tableData.add(0, List.of("Enquiry ID", "Project", "Enquiry Text", "Reply"));

         Integer COLUMN_WIDTH = 20; // Adjust width as needed
         tablePrinter.printTable(COLUMN_WIDTH, tableData);
         Color.print("Enter the Enquiry ID to " + action + " (or 'C' to cancel): ", Color.CYAN);
         String input = scanner.nextLine().trim();
         if (input.equalsIgnoreCase("C")) {
            return null; // User cancelled
         }
         try {
            int enquiryId = Integer.parseInt(input);
            return enquiries.stream()
                    .filter(e -> e.getId() == enquiryId)
                    .findFirst()
                    .orElse(null); // Return null if enquiry not found
         }
         catch (NumberFormatException e) {
            Color.println("Invalid input. Please enter a valid Enquiry ID.", Color.RED);
         }
      }
      return null;
   }

   public void handleDeleteEnquiry() {
      try {
         List<Enquiry> myEnquiries = applicantController.getMyEnquiries();
         // Filter directly here
         List<Enquiry> deletableEnquiries = myEnquiries.stream()
                 .filter(e -> e.getReply() == null || e.getReply().isBlank()) // Only allow deleting if no reply yet
                 .toList();

         if (deletableEnquiries.isEmpty()) {
            Color.println("You have no enquiries that can be deleted (either none exist or they have been replied to).", Color.YELLOW);
            return;
         }

         // Use helper to select
         Enquiry selectedEnquiry = selectEnquiryFromList(deletableEnquiries, "delete");
         if (selectedEnquiry == null) {
            // User cancelled selection
            return;
         }

         // Confirmation
         Color.print("Are you sure you want to permanently delete enquiry ID :" + selectedEnquiry.getId() + "  (y)  ",
                 Color.YELLOW);
         String confirm = scanner.nextLine().trim();
         if (!confirm.equalsIgnoreCase("y")) {
            Color.println("Deletion cancelled.", Color.YELLOW);
            return;
         }

         // Call controller
         enquiryService.deleteEnquiry(selectedEnquiry);

      }
      catch (IllegalArgumentException e) { // Catch errors from controller
         Color.println("Error deleting enquiry: " + e.getMessage(), Color.RED);
      }
      catch (Exception e) { // Catch unexpected errors
         Color.println("Unexpected error during enquiry deletion: " + e.getMessage(), Color.RED);
         e.printStackTrace(); // For debugging
      }
   }

   public void handleChangePassword() {
      List<String> inputs = super.getInputsChangePassword();
      try {
         applicantController.handlePasswordChange(inputs.get(0), inputs.get(1), inputs.get(2));
         Color.println("Password changed successfully.", Color.GREEN);
      }
      catch (IllegalArgumentException e) {
         Color.println("Password change error: " + e.getMessage(), Color.RED);
      }
      catch (Exception e) {
         Color.println("An unexpected error occurred during password change: " + e.getMessage(), Color.RED);
      }
   }

   @Override
   public void run() {
      while (sessionManager.isLoggedIn()) {
         display();
         handleInput();
      }
      Color.println("Logging out...", Color.YELLOW); // Optional logout message
   }
}