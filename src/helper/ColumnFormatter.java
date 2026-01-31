package helper;

public class ColumnFormatter {

   public static String formatColumn(Object data, int width) {
      String text = (data == null) ? "" : String.valueOf(data);

      if (text.length() > width) {
         return text.substring(0, width); // Trim to width
      }
      else {
         return String.format("%-" + width + "s", text); // Pad with spaces
      }
   }

   public static void setColumnWidth(int width) {
   }
}
