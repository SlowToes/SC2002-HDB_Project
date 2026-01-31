package project;


import java.time.LocalDate;
import java.util.List;
import java.util.Set;


public class ProjectValidationService implements IProjectValidationService {
   private static final Set<String> VALID_NEIGHBORHOODS = Set.of(
           "Yishun", "Boon Lay", "Tampines", "Jurong West", "Bedok"
   );

   @Override
   public void validateProjectNameUnique(Project project, List<Project> existingProjects) {
      existingProjects.stream()
              .filter(p -> p.getProjectName().equalsIgnoreCase(project.getProjectName()))
              .filter(p -> p.getNeighborhood().equalsIgnoreCase(project.getNeighborhood()))
              .filter(p -> !p.getApplicationOpeningDate().isAfter(project.getApplicationClosingDate()))
              .filter(p -> !p.getApplicationClosingDate().isBefore(project.getApplicationOpeningDate()))
              .findFirst()
              .ifPresent(p -> {
                 throw new IllegalArgumentException("Project name already exists in this neighborhood during overlapping dates");
              });
   }

   @Override
   public void validateApplicationDates(LocalDate openingDate, LocalDate closingDate) {
      if (openingDate.isAfter(closingDate)) {
         throw new IllegalArgumentException("Application opening date must be before closing date");
      }
      if (openingDate.isBefore(LocalDate.now())) {
         throw new IllegalArgumentException("Application opening date cannot be in the past");
      }
   }

   @Override
   public void validateFlatUnitsAndPrices(int twoRoomUnits, double twoRoomPrice,
                                          int threeRoomUnits, double threeRoomPrice) {
      if (twoRoomUnits < 0 || threeRoomUnits < 0) {
         throw new IllegalArgumentException("Flat units cannot be negative");
      }
      if (twoRoomPrice <= 0 || threeRoomPrice <= 0) {
         throw new IllegalArgumentException("Flat prices must be positive");
      }
   }

   @Override
   public void validateOfficerSlots(int slots) {
      if (slots < 0 || slots > 10) {
         throw new IllegalArgumentException("Officer slots must be between 0-10");
      }
   }

   @Override
   public void validateNeighborhood(String neighborhood) {
      if (!VALID_NEIGHBORHOODS.contains(neighborhood)) {
         throw new IllegalArgumentException("Invalid neighborhood specified");
      }
   }
}
