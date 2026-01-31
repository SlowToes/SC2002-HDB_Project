package manager;

import applicant.ApplicationStatus;
import applicant.WithdrawalRequestStatus;
import enquiry.Enquiry;
import officer.RegistrationForm;
import officer.RegistrationStatus;
import project.FlatType;
import project.Project;
import user.IUserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Interface defining the operations that can be performed by a Manager.
 */
public interface IManagerService extends IUserService {

   /**
    * Sets the current manager user.
    *
    * @param manager The manager to set
    */
   void setUser(Manager manager);

   /**
    * Creates a new housing project.
    *
    * @param projectName            The name of the project
    * @param neighbourhood          The neighbourhood location of the project
    * @param twoRoomUnits           Number of two-room units
    * @param twoRoomPrice           Price for a two-room unit
    * @param threeRoomUnits         Number of three-room units
    * @param threeRoomPrice         Price for a three-room unit
    * @param applicationOpeningDate The opening date for applications
    * @param applicationClosingDate The closing date for applications
    * @param manager                The name of the manager creating the project
    * @param availableOfficerSlots  Number of available officer slots
    * @param officers               List of officers to assign
    */
   void createProject(String projectName, String neighbourhood, Integer twoRoomUnits, Double twoRoomPrice,
                      Integer threeRoomUnits, Double threeRoomPrice, LocalDate applicationOpeningDate,
                      LocalDate applicationClosingDate, String manager, Integer availableOfficerSlots,
                      List<String> officers);

   /**
    * Updates an existing project.
    *
    * @param project        The project to update
    * @param name           New name of the project
    * @param neighbourhood  Updated neighbourhood location
    * @param availableFlats Updated available flat counts by type
    * @param openingDate    Updated application opening date
    * @param closingDate    Updated application closing date
    * @param visibility     Visibility status of the project
    */
   void updateProject(Project project, String name, String neighbourhood, Map<FlatType, Integer> availableFlats, LocalDate openingDate, LocalDate closingDate, boolean visibility);

   /**
    * Deletes a project by its ID.
    *
    * @param projectId The ID of the project to delete
    */
   void deleteProject(Integer projectId);

   /**
    * Retrieves all projects created by the current manager.
    *
    * @return List of projects managed by the current manager
    */
   List<Project> getMyProjects();

   /**
    * Retrieves the list of pending officer registrations.
    *
    * @return List of pending registration forms
    * @throws Exception If fetching fails
    */
   List<RegistrationForm> getPendingOfficerRegistrations() throws Exception;

   /**
    * Gets the currently selected project.
    *
    * @return The current project
    */
   Project getCurrentProject();

   /**
    * Sets the current project to work on.
    *
    * @param project The project to set as current
    */
   void setCurrentProject(Project project);

   /**
    * Displays all project-related enquiries.
    */
   void viewAllEnquiries();

   /**
    * Sets the registration status of an officer by their identifier.
    *
    * @param identifier The unique identifier of the officer
    * @param status     The new registration status to apply
    * @return Result message indicating success or failure
    * @throws Exception If update fails
    */
   String setRegistrationStatus(String identifier, RegistrationStatus status) throws Exception;

   /**
    * Adds an officer to the list of assigned officers.
    *
    * @param officerStr The officer identifier or name
    */
   void addToOfficersList(String officerStr);

   /**
    * Updates the application status of a user.
    *
    * @param applicationId The ID of the application
    * @param status        The new status to apply
    * @throws Exception If update fails
    */
   void updateApplicationStatus(String applicationId, ApplicationStatus status) throws Exception;

   /**
    * Retrieves enquiries related to the current project.
    *
    * @return List of project enquiries
    */
   List<Enquiry> getProjectEnquiries();

   /**
    * Sends a reply to a specific project enquiry.
    *
    * @param enquiryId The ID of the enquiry
    * @param reply     The reply message
    * @return true if reply was sent successfully, false otherwise
    */
   boolean replyToProjectEnquiry(String enquiryId, String reply);

   /**
    * Updates the status of a withdrawal request.
    *
    * @param withdrawalRequestId The withdrawal request ID
    * @param status              The new status to set
    * @throws Exception If there is an error updating the withdrawal request status
    */
   void updateWithdrawalRequestStatus(String withdrawalRequestId, WithdrawalRequestStatus status) throws Exception;
}
