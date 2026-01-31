package user;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages a collection of User objects.
 * This class is designed to be instantiated multiple times (not a singleton).
 * It supports serialization for saving and loading the registry state.
 */
public class UserRegistry implements Serializable {
   @Serial
   private static final long serialVersionUID = 1L;
   // Note: filePath is static, implying a fixed location for saving/loading
   // state for instances of this class when using the provided save/load methods.
   // For a truly non-singleton design where different registries could save
   // to different files, the filePath should be an instance variable or
   // passed to the save/load methods. Keeping it static for now to match
   // the original structure, but making save/load instance methods where appropriate.
   private static final String filePath = "./data/userRegistry.dat";

   private final Map<String, User> users = new HashMap<>();
   private final Map<String, User> usersByNric = new HashMap<>();

   /**
    * Constructs an empty UserRegistry.
    */
   public UserRegistry() {
      // The controller or application logic will decide when and how to load data.
   }

   /**
    * Loads a UserRegistry instance from the predefined file path.
    * This is a static method that returns a new UserRegistry object loaded from the file.
    *
    * @return A new UserRegistry instance loaded from the file, or a new empty instance if loading fails.
    */
   public UserRegistry load() {
      try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
         Object obj = ois.readObject();
         if (obj instanceof UserRegistry) {
            System.out.println("UserRegistry loaded successfully from " + filePath + ".::::" + ((UserRegistry) obj).getUsers().size());
            return (UserRegistry) obj;
         }
      }
      catch (IOException | ClassNotFoundException e) {
         System.err.println("Error loading UserRegistry: " + e.getMessage());
         // Return a new empty registry if loading fails
      }
      System.out.println("Returning new empty UserRegistry.");
      return new UserRegistry(); // Return a new empty instance if load fails
   }

   /**
    * Saves the current state of this UserRegistry instance to the predefined file path.
    * Note: This is an instance method, saving the state of 'this' object.
    */
   public void save() {
      try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
         oos.writeObject(this);
         System.out.println("UserRegistry saved successfully to " + filePath);
      }
      catch (IOException e) {
         System.err.println("Error saving UserRegistry: " + e.getMessage());
         e.printStackTrace();
      }
   }

   public void clearFromFile() {

      try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
         oos.writeObject(new UserRegistry());
         System.out.println("UserRegistry Cleared successfully to " + filePath);
      }
      catch (IOException e) {
         System.err.println("Error saving UserRegistry: " + e.getMessage());
         e.printStackTrace();
      }
   }


   /**
    * Adds a user to the registry.
    *
    * @param user The User object to add.
    */
   public void addUser(User user) {
      if (user != null && user.getName() != null) {
         users.put(user.getName(), user);
         usersByNric.put(user.getNric(), user);
      }
      save();
   }

   /**
    * Removes a user from the registry based on their NRIC.
    *
    * @param user The User object to remove. The removal is based on the user's NRIC.
    */
   public void removeUser(User user) {
      if (user != null && user.getNric() != null) {
         users.remove(user.getNric());
         usersByNric.remove(user.getNric());
      }
      save();
   }

   /**
    * Retrieves a user from the registry based on their NRIC.
    *
    * @param name The Name of the user to retrieve.
    * @return The User object associated with the NRIC, or null if not found.
    */
   public User getUser(String name) {
      return users.get(name);
   }

   /**
    * Retrieves a user from the registry based on their NRIC.
    *
    * @param nric The Name of the user to retrieve.
    * @return The User object associated with the NRIC, or null if not found.
    */
   public User getUserByNric(String nric) {
      return usersByNric.get(nric);
   }

   /**
    * Returns an unmodifiable view of the users map.
    *
    * @return An unmodifiable map of users.
    */
   public Map<String, User> getUsers() {
      // Returning a copy or an unmodifiable map is good practice
      // to prevent external modification of the internal map.
      return Map.copyOf(users); // Requires Java 10+
      // For older Java versions, use:
      // return Collections.unmodifiableMap(users);
   }

   /**
    * Gets the total number of users in the registry.
    *
    * @return The size of the registry.
    */
   public int size() {
      return users.size();
   }
}