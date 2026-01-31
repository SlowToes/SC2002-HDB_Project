package officer;

public interface IRegistrationValidationService {
   void validateRegistration(RegistrationForm form) throws IllegalArgumentException;
}