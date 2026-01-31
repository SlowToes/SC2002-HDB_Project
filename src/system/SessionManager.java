package system;

import UniqueID.IUniqueIdService;
import helper.ProjectRegistryLoader.ProjectRegistryLoadingController;
import helper.ProjectRegistryLoader.ProjectStaffLinker;
import helper.UserRegistryLoader.UserRegistryLoadingController;
import project.ProjectRegistry;
import user.IPasswordValidationService;
import user.User;
import user.UserRegistry;

import java.io.*;

public class SessionManager implements Serializable {
   @Serial
   private static final long serialVersionUID = 1L;
   private static final String SAVE_FILE = "session.ser";
   private transient final IPasswordValidationService passwordValidationService;
   private transient final IUniqueIdService uniqueIdService;
   private ProjectRegistry projectRegistry;
   private UserRegistry userRegistry;
   private User currentUser = null;

   public SessionManager(Boolean loadFromTxt) {
      this.uniqueIdService = ServiceRegistry.get(IUniqueIdService.class);
      this.passwordValidationService = ServiceRegistry.get(IPasswordValidationService.class);

      boolean loadedFromSerialized = false;

      // Only try to load from serialized file if not explicitly told to load from txt
      if (!loadFromTxt) {
         SessionManager loadedSession = loadSession();
         if (loadedSession != null) {
            try {
               this.projectRegistry = loadedSession.projectRegistry;
               this.userRegistry = loadedSession.userRegistry;
               System.out.println("Session loaded from serialized file.");
               loadedFromSerialized = true;
            }
            catch (Exception e) {
               System.out.println("Error during deserialization processing: " + e.getMessage());
               // If we get an exception while accessing the loaded data, it's likely a class incompatibility
               loadedFromSerialized = false;
            }
         }
      }

      // If we couldn't load from serialized file or were explicitly told to load from txt
      if (!loadedFromSerialized) {
         this.projectRegistry = new ProjectRegistry();
         this.userRegistry = new UserRegistry();

         try {
            loadFromTxt();
            System.out.println("Session loaded from Txt file.");

            // After loading from txt, save the session to update the serialized file
            saveSession();
         }
         catch (Exception e) {
            System.out.println("Error: @ Loading Files " + e.getMessage());
            e.printStackTrace();
         }
      }
   }

   private SessionManager loadSession() {
      try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
         return (SessionManager) in.readObject();
      }
      catch (InvalidClassException e) {
         System.out.println("Class structure has changed, cannot load serialized data: " + e.getMessage());
         // Delete the incompatible serialized file
         File serFile = new File(SAVE_FILE);
         if (serFile.exists()) {
            serFile.delete();
            System.out.println("Deleted incompatible serialized file.");
         }
         return null;
      }
      catch (IOException | ClassNotFoundException e) {
         System.out.println("No saved session found or error loading session: " + e.getMessage());
         return null;
      }
   }

   public void saveSession() {
      try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
         out.writeObject(this);
         System.out.println("Session saved successfully.");
      }
      catch (IOException e) {
         System.out.println("Error saving session: " + e.getMessage());
         e.printStackTrace();
      }
   }

   public void loadFromTxt() {
      try {
         UserRegistryLoadingController userRegistryLoadingController = new UserRegistryLoadingController();
         this.userRegistry = userRegistryLoadingController.initializeUserRegistry();

         ProjectRegistryLoadingController projectRegistryLoadingController = new ProjectRegistryLoadingController(uniqueIdService);
         this.projectRegistry = projectRegistryLoadingController.initializeProjectRegistry();

         ProjectStaffLinker projectStaffLinker = new ProjectStaffLinker(this.projectRegistry, this.userRegistry);
         projectStaffLinker.linkProjectToOfficer();
         projectStaffLinker.linkProjectToManager();
      }
      catch (Exception e) {
         System.out.println("Error: @ Loading Files " + e.getMessage());
         e.printStackTrace();
      }
   }

   public void login(String nric, String password) {
      try {
         User u = userRegistry.getUserByNric(nric);

         if (u != null) {
            passwordValidationService.isPasswordMatch(u, password);
            this.currentUser = u;
         }
      }
      catch (IllegalArgumentException e) {
         throw new IllegalArgumentException(e);
      }
   }

   public User getCurrentUser() {
      return currentUser;
   }

   public boolean isLoggedIn() {
      return currentUser != null;
   }

   public void logout() {
      saveSession();
      this.currentUser = null;
   }

   public ProjectRegistry getProjectRegistry() {
      return projectRegistry;
   }

   public UserRegistry getUserRegistry() {
      return userRegistry;
   }

   public User getUserByNric(String nric) {
      return userRegistry.getUser(nric);
   }

   public User getUserByName(String name) {
      return userRegistry.getUser(name);
   }
}