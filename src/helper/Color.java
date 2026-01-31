//usage: color.print();

package helper;

public class Color {
   public static final String RESET = "\u001B[0m";
   public static final String BLACK = "\u001B[30m";
   public static final String RED = "\u001B[31m";
   public static final String GREEN = "\u001B[32m";
   public static final String YELLOW = "\u001B[33m";
   public static final String BLUE = "\u001B[34m";
   public static final String PURPLE = "\u001B[35m";
   public static final String CYAN = "\u001B[36m";
   public static final String WHITE = "\u001B[37m";

   public static void print(String message, String color) {
      System.out.print(color + message + RESET);
   }

   public static void println(String message, String color) {
      System.out.println(color + message + RESET);
   }

   public static void println(String message) {
      System.out.println(message);
   }

   public static void print(String message) {
      System.out.print(message);
   }

   public static void printf(String color, String format, Object... args) {
      System.out.printf(color + format + RESET, args);
   }
}
