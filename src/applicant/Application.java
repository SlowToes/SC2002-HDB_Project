package applicant;

import project.FlatType;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Application implements Serializable {
   private final Integer id;
   private final String applicantName;
   private final String applicantNric;
   private final Integer projectId;
   private final String projectName;
   private final FlatType flatType;
   private final LocalDate dateApplied;
   private ApplicationStatus applicationStatus;
   private BookingStatus bookingStatus;
   private WithdrawalRequestStatus withdrawalRequestStatus;

   public Application(Integer id, String applicantName, String applicantNric, Integer projectId, String projectName, FlatType flatType) {
      this.id = id;
      this.applicantName = applicantName;
      this.applicantNric = applicantNric;
      this.projectId = projectId;
      this.projectName = projectName;
      this.flatType = flatType;
      this.applicationStatus = ApplicationStatus.PENDING;
      this.bookingStatus = BookingStatus.NOT_BOOKED;
      this.withdrawalRequestStatus = WithdrawalRequestStatus.NOT_REQUESTED;
      this.dateApplied = LocalDate.now();
   }

   public Integer getId() {
      return id;
   }

   public String getApplicantName() {
      return applicantName;
   }

   public String getApplicantNric() {
      return applicantNric;
   }

   public Integer getProjectId() {
      return projectId;
   }

   public String getProjectName() {
      return projectName;
   }

   public FlatType getFlatType() {
      return flatType;
   }

   public LocalDate getDateApplied() {
      return dateApplied;
   }

   public ApplicationStatus getApplicationStatus() {
      return applicationStatus;
   }

   public void setStatus(ApplicationStatus applicationStatus) {
      this.applicationStatus = applicationStatus;
   }

   public BookingStatus getBookingStatus() {
      return bookingStatus;
   }

   public void setBookingStatus(BookingStatus bookingStatus) {
      this.bookingStatus = bookingStatus;
   }

   public WithdrawalRequestStatus getWithdrawalRequestStatus() {
      return withdrawalRequestStatus;
   }

   public void setWithdrawalRequestStatus(WithdrawalRequestStatus withdrawalRequestStatus) {
      this.withdrawalRequestStatus = withdrawalRequestStatus;

   }


   @Override
   public String toString() {
      return "============== Application ==============\n" +
              "Application ID        : " + id + "\n" +
              "Applicant Name        : " + applicantName + "\n" +
              "Applicant NRIC         : " + applicantNric + "\n" +
              "Project ID            : " + projectId + "\n" +
              "Project Name          : " + projectName + "\n" +
              "Flat Type             : " + flatType + "\n" +
              "Application Status    : " + applicationStatus + "\n" +
              "Booking Status        : " + bookingStatus + "\n" +
              "Withdrawal Request Status : " + withdrawalRequestStatus + "\n" +
              "Date Applied         : " + dateApplied + "\n" +
              "==============================================";
   }

   //only this in this order : "Application ID", "Applicant Name", "NRIC", "Flat Type", "Project ID", "Project Name", "Status"
   public List<String> toList() {
      return List.of(
              id.toString(),
              applicantName,
              applicantNric,
              flatType.toString(),
              projectId.toString(),
              projectName,
              applicationStatus.toString()
      );
   }

}