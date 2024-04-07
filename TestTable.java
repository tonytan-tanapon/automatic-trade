package apitest;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
public class TestTable {
   public static void main(String[] argv) throws Exception {
      DefaultTableModel tableModel = new DefaultTableModel();
      JTable table = new JTable(tableModel);
      String[] columnNames = {"datetime","open","high","low","close","volume"};
      tableModel.addColumn("datetime");
      tableModel.addColumn("open");
      tableModel.addColumn("high");
      tableModel.insertRow(tableModel.getRowCount(), new Object[] { "CSS" });
      tableModel.insertRow(tableModel.getRowCount(), new Object[] { "HTML5" });
      tableModel.insertRow(tableModel.getRowCount(), new Object[] { "JavaScript" });
      tableModel.insertRow(tableModel.getRowCount(), new Object[] { "jQuery" });
      tableModel.insertRow(tableModel.getRowCount(), new Object[] { "AngularJS" });
      // adding a new row
      tableModel.insertRow(tableModel.getRowCount(), new Object[] { "ExpressJS" });
      JFrame f = new JFrame();
      f.setSize(550, 350);
      f.add(new JScrollPane(table));
      f.setVisible(true);
   }
}
