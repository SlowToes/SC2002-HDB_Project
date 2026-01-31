package project;

import java.time.LocalDate;
import java.util.List;

public interface IProjectValidationService {

   void validateProjectNameUnique(Project project, List<Project> existingProjects);

   void validateApplicationDates(LocalDate openingDate, LocalDate closingDate);

   void validateFlatUnitsAndPrices(int twoRoomUnits, double twoRoomPrice,
                                   int threeRoomUnits, double threeRoomPrice);

   void validateOfficerSlots(int slots);

   void validateNeighborhood(String neighborhood);
}

