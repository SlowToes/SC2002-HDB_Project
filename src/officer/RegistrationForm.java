package officer;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class RegistrationForm implements Serializable {
   private final Integer id;
   private final String officerName;
   private final String nric;
   private final Integer projectId;
   private final String projectName;
   private final LocalDate dateApplied;
   private RegistrationStatus status;

   public RegistrationForm(Integer id, String officerName, String nric, Integer projectId, String projectName) {
      this.id = id;
      this.officerName = officerName;
      this.nric = nric;
      this.projectId = projectId;
      this.projectName = projectName;
      this.dateApplied = LocalDate.now();
      this.status = RegistrationStatus.PENDING;
   }

   public Integer getId() {
      return id;
   }

   public String getOfficerName() {
      return officerName;
   }

   public Integer getProjectId() {
      return projectId;
   }

   public String getProjectName() {
      return projectName;
   }

   public LocalDate getDateApplied() {
      return dateApplied;
   }

   public String getNric() {
      return nric;
   }

   public RegistrationStatus getStatus() {
      return status;
   }

   public void setStatus(RegistrationStatus status) {
      this.status = status;
   }

   public Boolean isPending() {
      return status == RegistrationStatus.PENDING;
   }

   public Boolean isApproved() {
      return status == RegistrationStatus.APPROVED;
   }

   @Override
   public String toString() {
      return "============== Registration Form ==============\n" +
              "Registration ID     : " + id + "\n" +
              "Officer             : " + officerName + "\n" +
              "NRIC                : " + nric + "\n" +
              "Project ID          : " + projectId + "\n" +
              "Project Name        : " + projectName + "\n" +
              "Date Applied        : " + dateApplied + "\n" +
              "Status              : " + status + "\n" +
              "==============================================";
   }

   public List<String> toStringAsList() {
      return List.of(id.toString(), officerName, nric, dateApplied.toString(), status.toString(), projectId.toString(), projectName);
   }
}
