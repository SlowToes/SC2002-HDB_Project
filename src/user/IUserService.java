package user;

//Purpose: This interface defines the methods that a user service should implement. (default methods are implemented here to avoid duplication: DRY principle)
public interface IUserService {

   User getUser();

   IPasswordValidationService getPasswordValidationService();

   default void validatePassword(String password) throws IllegalArgumentException {
      getPasswordValidationService().isPasswordMatch(getUser(), password);
   }

   default void changePassword(String oldPassword, String newPassword1, String newPassword2) {
      getPasswordValidationService().validateChangePassword(getUser(), oldPassword, newPassword1, newPassword2);
      getUser().setPassword(newPassword1);
   }
}