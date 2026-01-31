package interfaces;

import helper.Color;

import java.util.List;
import java.util.Scanner;

public abstract class Menu {

   private final Scanner scanner;

   protected Menu(Scanner scanner) {
      this.scanner = scanner;
   }

   protected abstract void display();

   protected abstract void handleInput();

   public abstract void run();

   //Still handling UI related activities only. SRP not violated. Open/Closed Principle and only viewable to
   // extending classes.
   protected List<String> getInputsChangePassword() {
      Color.println("===========Changing Password==========", Color.CYAN);
      //get old password and new passwords confirm password
      Color.print("Enter your old password: ", Color.GREEN);
      String oldPassword = scanner.nextLine();
      Color.print("Enter your new password: ", Color.GREEN);
      String newPassword = scanner.nextLine();
      Color.print("Confirm your new password: ", Color.GREEN);
      String confirmPassword = scanner.nextLine();
      Color.println("======================================", Color.CYAN);
      return List.of(oldPassword, newPassword, confirmPassword);
   }
}
