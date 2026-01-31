package UniqueID;


public interface IUniqueIdService {
   Integer generateUniqueId(IdType idType);

   void resetId(IdType idType);
}
