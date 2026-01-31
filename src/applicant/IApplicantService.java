package applicant;

import project.Project;
import user.IUserService;

import java.util.List;
import java.util.function.Predicate;

public interface IApplicantService extends IUserService {

   void setUser(Applicant applicant);

   Predicate<Project> isEligibleForApplicant(Applicant applicant);

   void addApplicationToApplicant(Application application);

   List<Application> getApplicationsByApplicant(Applicant applicant);
}
