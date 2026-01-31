package system;

import UniqueID.IUniqueIdService;
import UniqueID.UniqueIdService;
import applicant.*;
import enquiry.EnquiryService;
import helper.Color;
import helper.TablePrinter;
import interfaces.Menu;
import manager.*;
import officer.*;
import project.IProjectService;
import project.ProjectRegistry;
import project.ProjectService;
import project.UserFilterManager;
import user.IPasswordValidationService;
import user.PasswordValidationService;

import java.util.Scanner;

public class EntryPoint {
   public void start() throws Exception {
      boolean running = true; // Flag to control the main loop
      Scanner scanner = new Scanner(System.in);
      TablePrinter tablePrinter = new TablePrinter();
      Boolean loadFromTxt = false;

      // Initialize and register all services to ServiceRegistry
      initializeServices(loadFromTxt); // written in the initializeServices method below, to prevent DI boilerplate

      // Get required services from registry
      SessionManager sessionManager = ServiceRegistry.get(SessionManager.class);
      IApplicantService applicantService = ServiceRegistry.get(IApplicantService.class);
      IOfficerService officerService = ServiceRegistry.get(IOfficerService.class);
      IManagerService managerService = ServiceRegistry.get(IManagerService.class);
      OfficerController officerController = ServiceRegistry.get(OfficerController.class);
      ManagerController managerController = ServiceRegistry.get(ManagerController.class);
      ApplicantController applicantController = ServiceRegistry.get(ApplicantController.class);

      // Create login menu
      LoginMenu loginMenu = new LoginMenu(sessionManager, scanner);
      Color.println("Welcome to the BTO System!", Color.CYAN);

      //sessionManager.getProjectRegistry().createDummyProjects();

      while (running) {
         // Get current menu based on user state
         Menu currentMenu = null;

         if (sessionManager.getCurrentUser() == null) {
            // User is not logged in, show login menu
            currentMenu = loginMenu;
         }
         else {
            Object user = sessionManager.getCurrentUser();

            if (user instanceof Manager) {
               managerService.setUser((Manager) user);
               currentMenu = new ManagerMenu(scanner, tablePrinter, sessionManager, managerController);
            }
            else if (user instanceof Officer) {
               Color.println("Officer logged in.", Color.GREEN);
               Color.println("Choose to be Applicant or officer: (1/0)");

               // surround using try and catch, and then the user will input 1 or 2 to choose between applicant or officer. if he choose applicabnt tthen we will wrap current user as applicant and run the appklicant menu. if he choose officer then we will wrap current user as officer and run the officer menu.
               String input = scanner.nextLine();
               if (input.equals("1")) {
                  applicantService.setUser((Applicant) user);
                  currentMenu = new ApplicantMenu(scanner, tablePrinter, sessionManager, applicantController);
               }
               else {
                  officerService.setUser((Officer) user);
                  currentMenu = new OfficerMenu(scanner, tablePrinter, sessionManager, officerController);
               }
            }
            else if (user instanceof Applicant) {
               applicantService.setUser((Applicant) user);
               currentMenu = new ApplicantMenu(scanner, tablePrinter, sessionManager, applicantController);
            }
            else {
               Color.println("Unsupported user type. Logging out.", Color.RED);
               sessionManager.logout();
               currentMenu = loginMenu;
            }
         }

         // Display and handle the current menu
         currentMenu.run();

         // Check if user wants to exit
         if (sessionManager.getCurrentUser() == null) {
            Color.print("\n\nEnter 'exit' to quit or any key to continue:", Color.BLUE);
            String input = scanner.nextLine();
            if ("exit".equalsIgnoreCase(input)) {
               running = false;
            }
         }
      }
      Color.println("Thank you for using the BTO Housing Application System. Goodbye!", Color.CYAN);
      scanner.close();
   }

   /**
    * Initialize all services and register them in the ServiceRegistry
    */
   private void initializeServices(Boolean loadFromTxt) {
      // Create core services
      IUniqueIdService uniqueIdService = new UniqueIdService();
      IPasswordValidationService passwordValidationService = new PasswordValidationService();

      // Register core services
      ServiceRegistry.register(IUniqueIdService.class, uniqueIdService);
      ServiceRegistry.register(IPasswordValidationService.class, passwordValidationService);

      // Create and register session manager
      SessionManager sessionManager = new SessionManager(loadFromTxt);
      ServiceRegistry.register(SessionManager.class, sessionManager);

      // Get project registry from session manager
      ProjectRegistry projectRegistry = sessionManager.getProjectRegistry();

      // Create and register project service
      IProjectService projectService = new ProjectService(projectRegistry);
      ServiceRegistry.register(IProjectService.class, projectService);

      // Create and register officer service
      IApplicantService applicantService = new ApplicantService();
      ServiceRegistry.register(IApplicantService.class, applicantService);

      IOfficerService officerService = new OfficerService();
      ServiceRegistry.register(IOfficerService.class, officerService);

      // Create and register manager service
      IManagerService managerService = new ManagerService();
      ServiceRegistry.register(IManagerService.class, managerService);

      EnquiryService enquiryService = new EnquiryService();
      ServiceRegistry.register(EnquiryService.class, enquiryService);

      // Create and register controllers
      ApplicantController applicantController = new ApplicantController();
      ServiceRegistry.register(ApplicantController.class, applicantController);

      OfficerController officerController = new OfficerController();
      ServiceRegistry.register(OfficerController.class, officerController);

      ManagerController managerController = new ManagerController();
      ServiceRegistry.register(ManagerController.class, managerController);

      UserFilterManager userFilterManager = new UserFilterManager(new Scanner(System.in));
      ServiceRegistry.register(UserFilterManager.class, userFilterManager);

   }
}