package project;

import helper.Color;
import user.User;
import user.UserFilterSettings;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserFilterManager {
   private final Scanner scanner;

   public UserFilterManager(Scanner scanner) {
      this.scanner = scanner;
   }

   public void manageFilters(User currentUser) {
      UserFilterSettings filters = currentUser.getFilterSettings();

      if (hasActiveFilters(filters)) {
         Color.println("Active filters are applied.", Color.YELLOW);
      }

      String filterChoice = "";
      while (true) {
         try {
            Color.println("\nWould you like to update/reset the filters? (yes/no/reset)", Color.CYAN);
            filterChoice = scanner.nextLine().trim().toLowerCase();

            if (filterChoice.equals("yes") || filterChoice.equals("no") || filterChoice.equals("reset")) {
               break;
            }
            else {
               Color.println("Please enter a valid option: yes / no / reset", Color.RED);
            }
         }
         catch (Exception e) {
            Color.println("An error occurred while reading input. Please try again.", Color.RED);
         }
      }

      if (filterChoice.equals("reset")) {
         filters.reset();
      }
      else if (filterChoice.equals("yes")) {
         updateFilters(filters);
      }
   }

   public List<Project> applyFilters(List<Project> projects, User currentUser) {
      Predicate<Project> combinedFilter = getProjectPredicate(currentUser.getFilterSettings());
      return projects.stream()
              .filter(combinedFilter)
              .collect(Collectors.toList());
   }

   private boolean hasActiveFilters(UserFilterSettings filters) {
      return filters.getProjectName() != null || filters.getNeighbourhood() != null ||
              filters.getFlatType() != null || filters.getDate() != null;
   }

   private void updateFilters(UserFilterSettings filters) {
      while (true) {
         try {
            Color.println("\nChoose a filter to set/update:", Color.CYAN);
            Color.println("1. Project Name", Color.YELLOW);
            Color.println("2. Neighbourhood", Color.YELLOW);
            Color.println("3. Flat Type", Color.YELLOW);
            Color.println("4. Date", Color.YELLOW);
            Color.println("5. Done", Color.YELLOW);
            Color.print("Enter your choice (1-5): ", Color.CYAN);

            String input = scanner.nextLine().trim();
            int choice = Integer.parseInt(input);

            switch (choice) {
               case 1 -> {
                  Color.print("Enter project name: ", Color.CYAN);
                  String name = scanner.nextLine().trim().toLowerCase();
                  filters.setProjectName(name.isEmpty() ? null : name);
               }
               case 2 -> {
                  Color.print("Enter neighbourhood: ", Color.CYAN);
                  String hood = scanner.nextLine().trim().toLowerCase();
                  filters.setNeighbourhood(hood.isEmpty() ? null : hood);
               }
               case 3 -> {
                  Color.print("Enter flat type (TWO_ROOM / THREE_ROOM): ", Color.CYAN);
                  String flatTypeInput = scanner.nextLine().trim().toUpperCase();
                  try {
                     filters.setFlatType(FlatType.valueOf(flatTypeInput));
                  }
                  catch (IllegalArgumentException e) {
                     Color.println("Invalid flat type. Please enter a valid option.", Color.RED);
                  }
               }
               case 4 -> {
                  Color.print("Enter date (yyyy-mm-dd): ", Color.CYAN);
                  String dateInput = scanner.nextLine().trim();
                  try {
                     filters.setDate(LocalDate.parse(dateInput));
                  }
                  catch (Exception e) {
                     Color.println("Invalid date format. Please use yyyy-mm-dd.", Color.RED);
                  }
               }
               case 5 -> {
                  return; // Exit the loop
               }
               default -> Color.println("Invalid choice. Please enter a number from 1 to 5.", Color.RED);
            }
         }
         catch (NumberFormatException e) {
            Color.println("Invalid input. Please enter a number between 1 and 5.", Color.RED);
         }
         catch (Exception e) {
            Color.println("An unexpected error occurred. Please try again.", Color.RED);
         }
      }
   }

   private Predicate<Project> getProjectPredicate(UserFilterSettings filters) {
      Predicate<Project> combinedFilter = p -> true;

      if (filters.getProjectName() != null) {
         combinedFilter = combinedFilter.and(p -> p.getProjectName().toLowerCase().contains(filters.getProjectName()));
      }

      if (filters.getNeighbourhood() != null) {
         combinedFilter = combinedFilter.and(p -> p.getNeighborhood().toLowerCase().contains(filters.getNeighbourhood()));
      }

      if (filters.getFlatType() != null) {
         combinedFilter = combinedFilter.and(p -> p.getAvailableFlats().getOrDefault(filters.getFlatType(), 0) > 0);
      }

      if (filters.getDate() != null) {
         combinedFilter = combinedFilter.and(p ->
                 !filters.getDate().isBefore(p.getApplicationOpeningDate()) &&
                         !filters.getDate().isAfter(p.getApplicationClosingDate()));
      }
      return combinedFilter;
   }
}
