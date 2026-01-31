//package applicant;
//
//import java.util.Comparator;
//import java.util.List;
//
//import enquiry.Enquiry;
//import enums.*;
//import helper.Color;
//import UniqueID.UniqueId;
//import project.ProjectRegistry;
//import project.ProjectService;
//import user.User;
//import project.Project;
//
//public class ApplicantService {
//
//    public ApplicantService() {
//    }
//
//    public List<Project> getEligibleProjects(ProjectService projectService, ProjectRegistry projectRegistry, User applicant) {
//        return projectService.getEligibleProjects(projectRegistry, applicant);
//    }
//
//    public Application createApplication(UniqueId uniqueId, Applicant applicant, Project project, FlatType flatType) {
//        boolean hasCurrentApplications = applicant.getApplicationList().stream()
//                .anyMatch(application ->
//                        application.getApplicationStatus() == ApplicationStatus.PENDING ||
//                        application.getApplicationStatus() == ApplicationStatus.SUCCESSFUL ||
//                        application.getBookingStatus() == BookingStatus.PENDING ||
//                        application.getBookingStatus() == BookingStatus.BOOKED);
//
//        if (hasCurrentApplications) {
//            System.out.println("You have already applied for a project.");
//            return null;
//        }
//
//        return new Application(uniqueId.getNextApplicationId(), applicant, project, flatType);
//    }
//
//    public void submitApplication(ProjectService projectService, Application application) {
//        projectService.addApplicationToProject(application);
//        Color.println("Application submitted successfully.", Color.GREEN);
//
//    }
//
//    public void setApplicationStatus(Application application, ApplicationStatus status) {
//        application.setApplicationStatus(status);
//    }
//
//    public List<Application> getAllApplications(Applicant applicant) {
//        return applicant.getApplicationList();
//    }
//
//    public Application getCurrentApplication(Applicant applicant) {
//        List<Application> applications = applicant.getApplicationList();
//
//        return applications.stream()
//                .max(Comparator.comparing(Application::getDateApplied))
//                .orElse(null);
//    }
//
//    public void withdrawApplication(Application application) {
//        if (application == null) {
//            System.out.println("No application to withdraw.");
//            return;
//        }
//
//        application.setWithdrawalRequestStatus(WithdrawalRequestStatus.PENDING);
//        System.out.println("Withdrawal request submitted.");
//    }
//
//    public void sendBookingRequest(Application application) {
//        if (application == null) {
//            System.out.println("No application to request booking for.");
//            return;
//        }
//
//        if (application.getApplicationStatus() != ApplicationStatus.SUCCESSFUL) {
//            System.out.println("Only successful applicants can request a booking.");
//            return;
//        }
//
//        if (application.getBookingStatus() != BookingStatus.NOT_BOOKED) {
//            System.out.println("Booking request already submitted or processed.");
//            return;
//        }
//
//        application.setBookingStatus(BookingStatus.PENDING);
//        System.out.println("Booking request submitted.");
//    }
//
//    public void updateBookingStatus(Application application, BookingStatus status) {
//        if (application == null) {
//            System.out.println("Invalid application.");
//            return;
//        }
//
//        if (application.getBookingStatus() != BookingStatus.PENDING) {
//            System.out.println("Booking is not pending. Current status: " + application.getBookingStatus());
//        }
//    }
//
//    //validate current applicant
//    public boolean validateApplicant(Applicant applicant, Application application){
//        return applicant.equals(application.getApplicant());
//    }
//    public boolean validateApplicant(Applicant applicant, Enquiry enquiry){
//        return applicant.equals(enquiry.getApplicant());
//    }
//}
