package user;

import java.io.Serializable;

public class User implements Serializable {
   private final String name;
   private final String nric;
   private final int age;
   private final MaritalStatus maritalStatus;
   private String password;
   private UserFilterSettings filterSettings;

   public User() {
      this.name = null;
      this.nric = null;
      this.password = null;
      this.age = 0;
      this.maritalStatus = null;
      this.filterSettings = null;
   }

   public User(String name, String nric, String password, int age, MaritalStatus maritalStatus,
               UserFilterSettings filterSettings) {
      this.name = name;
      this.nric = nric;
      this.password = password;
      this.age = age;
      this.maritalStatus = maritalStatus;
      this.filterSettings = filterSettings;
   }

   public String getName() {
      return name;
   }

   public String getNric() {
      return nric;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public int getAge() {
      return age;
   }

   public MaritalStatus getMaritalStatus() {
      return maritalStatus;
   }

   public UserFilterSettings getFilterSettings() {
      return filterSettings;
   }

   public void setFilterSettings(UserFilterSettings filterSettings) {
      this.filterSettings = filterSettings;
   }


}







