package system;

import helper.Color;
import interfaces.Menu;
import manager.Manager;
import officer.Officer;

import java.util.Scanner;

public class LoginMenu extends Menu {

   private final SessionManager sessionManager;
   private final Scanner scanner;

   public LoginMenu(SessionManager sessionManager, Scanner scanner) {
      super(scanner);
      this.sessionManager = sessionManager;
      this.scanner = scanner;
   }

   @Override
   public void run() {
      display();
      handleInput();
   }

   protected void display() {
      Color.println("Login with Singpass to continue...", Color.BLUE);
   }

   protected void handleInput() {
      try {
         Color.print("Enter your NRIC:", Color.BLUE);
         String nric = scanner.nextLine();
         Color.print("Enter your password:", Color.BLUE);
         String password = scanner.nextLine();
         sessionManager.login(nric, password);
      }
      catch (IllegalArgumentException e) {
         Color.println("Login Error: " + e.getMessage(), Color.RED);
      } finally {
         if (sessionManager.isLoggedIn()) {
            Color.println("Login successful!", Color.GREEN);
            String type = null;
            if (sessionManager.getCurrentUser() instanceof Manager) {
               type = "Manager";
            }
            else if (sessionManager.getCurrentUser() instanceof Officer) {
               type = "Officer";
            }
            else {
               type = "Applicant";
            }
            Color.println("Welcome Back (" + type + ") : " + sessionManager.getCurrentUser().getName() + "!", Color.YELLOW);
         }
         else {
            Color.println("Login failed. Please check your credentials.", Color.RED);
         }
      }
   }
}

//give me try catch finally block for the above code



