package interfaces;

import enquiry.Enquiry;

import java.util.List;

public interface StaffService {
   // View details of a project
   //ImmutableProjectView getCurrentProjectDetails();

   // View enquiries related to a project
   List<Enquiry> getCurrentProjectEnquiries();


   // Reply to an enquiry
   //void replyToEnquiry(String enquiryId, String response);

   // View list of projects (scope may differ in implementation)
   // void getProjectRegistry();
}
