package user;

public class PasswordValidationService implements IPasswordValidationService {

   @Override
   public void isPasswordMatch(User user, String password) {
      if (!user.getPassword().equals(password)) {
         throw new IllegalArgumentException("Password is incorrect");
      }
   }

   @Override
   public void validateChangePassword(User user, String old, String newPassword1, String newPassword2) {
      if (!old.equals(user.getPassword())) {
         throw new IllegalArgumentException("Old password is incorrect");
      }
      if (!newPassword1.equals(newPassword2)) {
         throw new IllegalArgumentException("New passwords do not match");
      }
      if (newPassword1.length() < 8) {
         throw new IllegalArgumentException("New password must be at least 8 characters long...(It's not mentioned in Project Manual, but enforced by our system)  :)");
      }
   }
}
