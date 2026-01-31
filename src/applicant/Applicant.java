package applicant;

import enquiry.Enquiry;
import project.FlatType;
import user.MaritalStatus;
import user.User;
import user.UserFilterSettings;

import java.util.ArrayList;
import java.util.List;

public class Applicant extends User {
   private final List<Application> myApplications;
   private final List<Enquiry> enquiries;
   private FlatType bookedFlatType;

   public Applicant(String name, String nric, String password, int age, MaritalStatus maritalStatus) {
      super(name, nric, password, age, maritalStatus, new UserFilterSettings());
      this.myApplications = new ArrayList<>();
      this.bookedFlatType = null;
      this.enquiries = new ArrayList<>();
   }

   public List<Application> getMyApplications() {
      return myApplications;
   }

   public List<Enquiry> getEnquiries() {
      return enquiries;
   }

   public FlatType getBookedFlatType() {
      return bookedFlatType;
   }

   public void setBookedFlatType(FlatType bookedFlatType) {
      this.bookedFlatType = bookedFlatType;
   }

   @Override
   public int getAge() {
      return super.getAge();
   }
}