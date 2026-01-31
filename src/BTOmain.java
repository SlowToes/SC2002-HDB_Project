import system.EntryPoint;

public class BTOmain {
   public static void main(String[] args) {
      EntryPoint entryPoint = new EntryPoint();
      try {
         entryPoint.start();
      }
      catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
}