package UniqueID;

public class UniqueIdService implements IUniqueIdService {
   private final UniqueId uniqueId;

   public UniqueIdService() {
      this.uniqueId = new UniqueId();
      uniqueId.loadFromPropertiesFile();
   }

   @Override
   public Integer generateUniqueId(IdType idType) {
      return switch (idType) {
         case APPLICATION_ID -> uniqueId.getApplicationId();
         case PROJECT_ID -> uniqueId.getProjectId();
         case ENQUIRY_ID -> uniqueId.getEnquiryId();
         case REGISTRATION_FORM_ID -> uniqueId.getRegistrationId();
         default -> throw new IllegalArgumentException("Invalid ID type");
      };
   }

   @Override
   public void resetId(IdType idType) {
      switch (idType) {
         case APPLICATION_ID -> uniqueId.resetApplicationId();
         case PROJECT_ID -> uniqueId.resetProjectId();
         case ENQUIRY_ID -> uniqueId.resetEnquiryId();
         case REGISTRATION_FORM_ID -> uniqueId.resetRegistrationId();
         default -> throw new IllegalArgumentException("Invalid ID type");
      }
   }

}
