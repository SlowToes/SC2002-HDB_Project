package UniqueID;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class UniqueId {
   private final String filename = "./data/uniqueIdState.properties";
   private int nextNewProjectId;
   private int nextNewApplicationId;
   private int nextNewRegistrationId;
   private int nextEnquiryId;

   public UniqueId() {
      nextNewProjectId = 0;
      nextNewApplicationId = 0;
      nextNewRegistrationId = 0;
      nextEnquiryId = 0;
   }

   public int getProjectId() {
      //System.out.println("Project ID: " + nextNewProjectId);
      nextNewProjectId++;
      saveToPropertiesFile();
      return nextNewProjectId;
   }

   public int getApplicationId() {
      //System.out.println("Application ID: " + nextNewApplicationId);
      nextNewApplicationId++;
      saveToPropertiesFile();
      return nextNewApplicationId;
   }

   public int getRegistrationId() {
      //System.out.println("Registration ID: " + nextNewRegistrationId);
      nextNewRegistrationId++;
      saveToPropertiesFile();
      return nextNewRegistrationId;
   }

   public int getEnquiryId() {
      //System.out.println("Enquiry ID: " + nextEnquiryId);
      nextEnquiryId++;
      saveToPropertiesFile();
      return nextEnquiryId;
   }

   public void resetProjectId() {
      nextNewProjectId = 0;
      saveToPropertiesFile();
   }

   public void resetApplicationId() {
      nextNewApplicationId = 0;
      saveToPropertiesFile();
   }

   public void resetRegistrationId() {
      nextNewRegistrationId = 0;
      saveToPropertiesFile();
   }

   public void resetEnquiryId() {
      nextEnquiryId = 0;
      saveToPropertiesFile();
   }


   public void loadFromPropertiesFile() {
      Properties props = new Properties();
      try (FileInputStream fis = new FileInputStream(filename)) {
         props.load(fis);
         nextNewProjectId = Integer.parseInt(props.getProperty("nextNewProjectId", "1"));
         nextNewApplicationId = Integer.parseInt(props.getProperty("nextNewApplicationId", "1"));
         nextNewRegistrationId = Integer.parseInt(props.getProperty("nextNewRegistrationId", "1"));
         nextEnquiryId = Integer.parseInt(props.getProperty("nextEnquiryId", "1"));
      }
      catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void saveToPropertiesFile() {
      Properties props = new Properties();
      props.setProperty("nextNewProjectId", String.valueOf(nextNewProjectId));
      props.setProperty("nextNewApplicationId", String.valueOf(nextNewApplicationId));
      props.setProperty("nextNewRegistrationId", String.valueOf(nextNewRegistrationId));
      props.setProperty("nextEnquiryId", String.valueOf(nextEnquiryId));

      try (FileOutputStream fos = new FileOutputStream(filename)) {
         props.store(fos, "Unique ID State");
      }
      catch (IOException e) {
         e.printStackTrace();
      }
   }

/* // TODO: usage example:
    public static void main(String[] args) {
        UniqueId uniqueId = new UniqueId();
        uniqueId.loadFromPropertiesFile("./data/uniqueIdState.properties");

        System.out.println(uniqueId.getNextProjectId());
        System.out.println(uniqueId.getNextApplicationId());

        uniqueId.saveToPropertiesFile("./data/uniqueIdState.properties");
        uniqueId.loadFromPropertiesFile("./data/uniqueIdState.properties");

        System.out.println(uniqueId.getNextProjectId());
        System.out.println(uniqueId.getNextApplicationId());

        uniqueId.saveToPropertiesFile("./data/uniqueIdState.properties");
        uniqueId.loadFromPropertiesFile("./data/uniqueIdState.properties");

        System.out.println(uniqueId.getNextProjectId());
        System.out.println(uniqueId.getNextApplicationId());

        uniqueId.saveToPropertiesFile("./data/uniqueIdState.properties");

    }*/
}
