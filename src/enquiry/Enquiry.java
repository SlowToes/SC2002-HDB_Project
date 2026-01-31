package enquiry;

import system.ServiceRegistry;
import system.SessionManager;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Enquiry implements Serializable {
   private final Integer id;
   private final String projectName;
   private final Integer projectId;
   private final String applicantName;
   private final String applicantNric;
   private final LocalDate dateEnquired;
   private String enquiry;
   private String reply;
   private String respondent;
   private LocalDate dateReplied;

   protected Enquiry(Integer id, String projectName, Integer projectId, String applicantName, String applicantNric,
                     LocalDate dateEnquired) {
      this.id = id;
      this.projectName = projectName;
      this.projectId = projectId;
      this.applicantName = applicantName;
      this.applicantNric = applicantNric;
      this.dateEnquired = dateEnquired;
   }

   public Integer getId() {
      return id;
   }

   public String getProjectName() {
      return projectName;
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

   public LocalDate getDateEnquired() {
      return dateEnquired;
   }

   public String getEnquiry() {
      return enquiry;
   }

   //package-private
   protected void setEnquiry(String enquiry) {
      this.enquiry = enquiry;
   }


   public String getReply() {
      return reply;
   }

   public void setReply(String reply) {
      this.reply = reply;
      setRespondent(ServiceRegistry.get(SessionManager.class).getCurrentUser().getName());
      setDateReplied(LocalDate.now());
   }


   public void setRespondent(String respondent) {
      this.respondent = respondent;
   }

   public LocalDate getDateReplied() {
      return dateReplied;
   }

   public void setDateReplied(LocalDate dateReplied) {
      this.dateReplied = dateReplied;
   }

   @Override
   public String toString() {
      return "============== Enquiry Details ==============\n" +
              "Enquiry ID          : " + id + "\n" +
              "Project             : " + projectName + "\n" +
              "Applicant           : " + applicantName + "\n" +
              "Date Enquired       : " + dateEnquired + "\n" +
              "Enquiry             : " + enquiry + "\n" +
              "Reply               : " + (reply != null ? reply : "N/A") + "\n" +
              "Respondent          : " + (respondent != null ? respondent : "N/A") + "\n" +
              "Date Replied        : " + (dateReplied != null ? dateReplied : "Pending") + "\n" +
              "==============================================";
   }

   public List<String> toStringList() {
      return List.of(
              String.valueOf(id),
              projectName,
              applicantName,
              dateEnquired.toString(),
              enquiry,
              reply != null ? reply : "N/A",
              respondent != null ? respondent : "Unanswered",
              dateReplied != null ? dateReplied.toString() : "Pending"
      );
   }
}