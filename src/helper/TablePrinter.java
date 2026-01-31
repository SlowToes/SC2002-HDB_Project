package helper;

import java.util.List;

import static helper.ColumnFormatter.formatColumn;

public class TablePrinter {
   public void printRow(Integer COLUMN_WIDTH, List<String> rowData) {
      StringBuilder row = new StringBuilder("| ");
      for (String str : rowData) {
         row.append(formatColumn(str, COLUMN_WIDTH)).append(" | ");
      }
      Color.println(row.toString(), Color.YELLOW);
   }

   public void printTable(Integer COLUMN_WIDTH, List<List<String>> tableData) {
      if (tableData.isEmpty()) {
         Color.println("No data to display.", Color.RED);
         return;
      }
      tableData.forEach(row -> printRow(COLUMN_WIDTH, row));
   }
}
