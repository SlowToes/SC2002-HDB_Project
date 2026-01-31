package user;

public enum MaritalStatus {
   SINGLE,
   MARRIED;

   // Helper to parse string case-insensitively
   public static MaritalStatus fromString(String status) {
      if (status == null || status.trim().isEmpty()) {
         return null; // Or throw an exception, depending on requirements
      }
      return MaritalStatus.valueOf(status.trim().toUpperCase());
   }
}
