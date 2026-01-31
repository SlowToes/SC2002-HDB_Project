package user;

import project.FlatType;

import java.io.Serializable;
import java.time.LocalDate;

public class UserFilterSettings implements Serializable {
   private String projectName;
   private String neighbourhood;
   private FlatType flatType;
   private LocalDate date;

   public UserFilterSettings() {
      this.projectName = null;
      this.neighbourhood = null;
      this.flatType = null;
      this.date = null;
   }

   public String getProjectName() {
      return projectName;
   }

   public void setProjectName(String projectName) {
      this.projectName = projectName;
   }

   public String getNeighbourhood() {
      return neighbourhood;
   }

   public void setNeighbourhood(String neighbourhood) {
      this.neighbourhood = neighbourhood;
   }

   public FlatType getFlatType() {
      return flatType;
   }

   public void setFlatType(FlatType flatType) {
      this.flatType = flatType;
   }

   public LocalDate getDate() {
      return date;
   }

   public void setDate(LocalDate date) {
      this.date = date;
   }


   public void reset() {
      projectName = null;
      neighbourhood = null;
      flatType = null;
      date = null;
   }
}
