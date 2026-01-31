package project;

import applicant.Application;
import enquiry.Enquiry;
import officer.RegistrationForm;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Project implements Serializable {
   @Serial
   private static final long serialVersionUID = 1L;
   // =================== Immutable Project Details =================== (never modified, at least not directly, only through the setter methods)
   private final Integer id;//automatically generated
   private final String manager;
   private final Map<FlatType, Double> flatPrices;
   private final Map<FlatType, Integer> availableFlats;
   private final Map<FlatType, Integer> remainingFlats;//Officer can modify
   private final List<String> officers;
   private final List<RegistrationForm> registrationForms = new ArrayList<>();
   private final List<Application> applications = new ArrayList<>();
   private final List<Enquiry> enquiries = new ArrayList<>();

   // =================== Mutable Project Metadata ===================
   private String projectName;
   private String neighborhood;
   private LocalDate applicationOpeningDate;
   private LocalDate applicationClosingDate;
   private boolean visibility = true;
   private Integer availableOfficerSlots;

   // Use this when Manager wants to create a new project
   public Project(int id, String projectName, String neighborhood, Integer twoRoomUnits, Double twoRoomPrice,
                  Integer threeRoomUnits, Double threeRoomPrice, LocalDate applicationOpeningDate,
                  LocalDate applicationClosingDate, String manager, Integer availableOfficerSlots,
                  List<String> officers) {
      this.id = id;
      this.projectName = projectName;
      this.neighborhood = neighborhood;
      this.flatPrices = Map.of(FlatType.TWO_ROOM, twoRoomPrice, FlatType.THREE_ROOM, threeRoomPrice);
      this.availableFlats = Map.of(FlatType.TWO_ROOM, twoRoomUnits, FlatType.THREE_ROOM, threeRoomUnits);
      this.remainingFlats = Map.of(FlatType.TWO_ROOM, twoRoomUnits, FlatType.THREE_ROOM, threeRoomUnits);
      this.applicationOpeningDate = applicationOpeningDate;
      this.applicationClosingDate = applicationClosingDate;
      this.manager = manager;
      this.availableOfficerSlots = availableOfficerSlots;
      this.officers = officers;
   }
   // =================== Immutable Project Details ===================

   public Integer getId() {
      return id;
   }

   public String getManager() {
      return manager;
   }

   public Map<FlatType, Double> getFlatPrices() {
      return flatPrices;
   }

   public Map<FlatType, Integer> getAvailableFlats() {
      return availableFlats;
   }

   public Map<FlatType, Integer> getRemainingFlats() {
      return remainingFlats;
   }

   public List<String> getOfficers() {
      return officers;
   }

   public List<RegistrationForm> getRegistrationForms() {
      return registrationForms;
   }

   public void addRegistrationForm(RegistrationForm registrationForm) {
      registrationForms.add(registrationForm);
   }

   public void removeRegistrationForm(RegistrationForm registrationForm) {
      registrationForms.remove(registrationForm);
   }

   public List<Application> getApplications() {
      return applications;
   }

   public List<Enquiry> getEnquiries() {
      return enquiries;
   }

   public String getProjectName() {
      return projectName;
   }

   public void setProjectName(String projectName) {
      this.projectName = projectName;
   }

   public String getNeighborhood() {
      return neighborhood;
   }

   public void setNeighborhood(String neighborhood) {
      this.neighborhood = neighborhood;
   }

   public LocalDate getApplicationOpeningDate() {
      return applicationOpeningDate;
   }

   public void setApplicationOpeningDate(LocalDate applicationOpeningDate) {
      this.applicationOpeningDate = applicationOpeningDate;
   }

   public LocalDate getApplicationClosingDate() {
      return applicationClosingDate;
   }

   public void setApplicationClosingDate(LocalDate applicationClosingDate) {
      this.applicationClosingDate = applicationClosingDate;
   }

   public boolean isVisibility() {
      return visibility;
   }

   public void setVisibility(boolean visibility) {
      this.visibility = visibility;
   }

   public Integer getAvailableOfficerSlots() {
      return availableOfficerSlots;
   }

   public void setAvailableOfficerSlots(Integer availableOfficerSlots) {
      this.availableOfficerSlots = availableOfficerSlots;
   }

   public Integer getTwoRoomUnits() {
      return availableFlats.get(FlatType.TWO_ROOM);
   }

   public Double getTwoRoomPrice() {
      return flatPrices.get(FlatType.TWO_ROOM);
   }

   public Integer getThreeRoomUnits() {
      return availableFlats.get(FlatType.THREE_ROOM);
   }

   public Double getThreeRoomPrice() {
      return flatPrices.get(FlatType.THREE_ROOM);
   }

   //Setters
   public void addEnquiry(Enquiry enquiry) {
      enquiries.add(enquiry);
   }

   public void removeEnquiry(Enquiry enquiry) {
      enquiries.remove(enquiry);
   }

   public void addApplication(Application application) {
      applications.add(application);
   }

   public void removeApplication(Application application) {
      applications.remove(application);
   }

   public void addOfficer(String officer) {
      officers.add(officer);
   }

   public void removeOfficer(String officer) {
      officers.remove(officer);
   }

// =================== Mutable Project Metadata ===================


// =================== Printable String Project Metadata ===================

   //"Project ID", "Project Name", "Neighbourhood", "Visibility", "Two Room Units", "Two Room Price", "Three Room Units", "Three Room Price", "Appln..Opening Date", "Appln..Closing Date", "Manager", "Officer Slots", "Officers"
   public List<String> toStringAsList() {
      return List.of(id.toString(), projectName, neighborhood, visibility ? "Visible" : "Hidden", getTwoRoomUnits().toString(), getTwoRoomPrice().toString(), getThreeRoomUnits().toString(), getThreeRoomPrice().toString(), applicationOpeningDate.toString(), applicationClosingDate.toString(), manager, availableOfficerSlots.toString(), officers.toString());
   }

   @Override // to print as a form like
   public String toString() {
      return "============== Project Details ==============\n" +
              "Project ID          : " + id + "\n" +
              "Project Name        : " + projectName + "\n" +
              "Neighbourhood       : " + neighborhood + "\n" +
              "Visibility          : " + (visibility ? "Visible" : "Hidden") + "\n" +
              "Two Room Units      : " + getTwoRoomUnits() + "\n" +
              "Two Room Price      : " + getTwoRoomPrice() + "\n" +
              "Three Room Units    : " + getThreeRoomUnits() + "\n" +
              "Three Room Price    : " + getThreeRoomPrice() + "\n" +
              "Opening Date        : " + applicationOpeningDate + "\n" +
              "Closing Date        : " + applicationClosingDate + "\n" +
              "Manager             : " + manager + "\n" +
              "Officer Slots       : " + availableOfficerSlots + "\n" +
              "Officers            : " + ((officers.isEmpty()) ? "N/A" : officers) + "\n" +
              "==============================================";
   }


   public void decrementRemainingFlat(FlatType flatType) {
      Integer currentCount = remainingFlats.get(flatType);
      if (currentCount == null || currentCount <= 0) {
         throw new IllegalStateException("No remaining " + flatType + " flats available");
      }
      remainingFlats.put(flatType, currentCount - 1);
   }

   /**
    * Decrements the number of remaining two-room flats by one.
    *
    * @throws IllegalStateException if there are no remaining two-room flats
    */
   public void decrementRemainingTwoRoomFlat() {
      decrementRemainingFlat(FlatType.TWO_ROOM);
   }

   /**
    * Decrements the number of remaining three-room flats by one.
    *
    * @throws IllegalStateException if there are no remaining three-room flats
    */
   public void decrementRemainingThreeRoomFlat() {
      decrementRemainingFlat(FlatType.THREE_ROOM);
   }


}


