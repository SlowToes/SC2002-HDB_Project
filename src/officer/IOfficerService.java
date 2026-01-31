package officer;

import applicant.Application;
import project.Project;
import user.IUserService;

public interface IOfficerService extends IUserService {
   // Officer Registration
   RegistrationForm createRegistrationForm(String projectId);

   void sendRegistrationRequest(RegistrationForm registrationForm);

   OfficerStatus getOfficerStatus();

   void setOfficerStatus(OfficerStatus status);

   RegistrationForm getCurrentRegistrationForm();

   void setCurrentRegistrationForm(RegistrationForm form);

   void addToMyRegistrations(RegistrationForm form);

   void removeRegistrationForm(RegistrationForm form);

   // Project Handling
   Project getCurrentProject();

   void setUser(Officer officer);

   void setOfficerCurrentProject(String officerName, Project currentProject);

   void bookFlat(Application application);


   // Application Management
//   List<Application> getApplicationsForCurrentProject();

   //   void updateFlatAvailability(FlatType flatType, int quantityChange);
//
//   void updateApplicantStatus(Application application, ApplicationStatus newStatus);
//
//   String generateBookingReceipt(Application application);
//
//   // Enquiry Handling
//   List<Enquiry> getEnquiriesForHandledProject();
//
//   void replyToEnquiry(Enquiry enquiry, String response);
//
//   // Account Management
}