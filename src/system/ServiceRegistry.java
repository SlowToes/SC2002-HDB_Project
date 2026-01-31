package system;

import java.util.HashMap;
import java.util.Map;

/**
 * A static service registry that provides centralized access to application services.
 * This reduces dependency injection boilerplate in the EntryPoint class.
 */
public class ServiceRegistry {
   private static final Map<Class<?>, Object> services = new HashMap<>();

   /**
    * Register a service implementation for a specific interface or class.
    *
    * @param <T>            The type of service
    * @param serviceType    The class or interface of the service
    * @param implementation The implementation instance
    */
   public static <T> void register(Class<T> serviceType, T implementation) {
      services.put(serviceType, implementation);
   }

   /**
    * Get a registered service by its interface or class type.
    *
    * @param <T>         The type of service
    * @param serviceType The class or interface of the service
    * @return The service implementation
    * @throws IllegalStateException if the requested service is not registered
    */
   @SuppressWarnings("unchecked")
   public static <T> T get(Class<T> serviceType) {
      Object service = services.get(serviceType);
      if (service == null) {
         throw new IllegalStateException("Service not registered: " + serviceType.getName());
      }
      return (T) service;
   }

   /**
    * Check if a service is registered.
    *
    * @param serviceType The class or interface of the service
    * @return true if the service is registered, false otherwise
    */
   public static boolean isRegistered(Class<?> serviceType) {
      return services.containsKey(serviceType);
   }

   /**
    * Clear all registered services.
    */
   public static void clear() {
      services.clear();
   }
}