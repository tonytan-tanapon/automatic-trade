package apitest;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.ib.controller.Bar;

public class MyHistorical {
	
	String[] columnNames = {"datetime","open","high","low","close","volume"};
	String[][] data;
	JTable historicalBar = new JTable(data,columnNames);
	int i=0;
	DefaultTableModel tableModel = new DefaultTableModel();
	JTable table = new JTable(tableModel);
	
	double close = 0.0;
	
	MyHistorical(){
		for(int i=0;i<columnNames.length ;i++) {
			tableModel.addColumn(columnNames[i]);
		}
		
	}
	public void setBar(Bar bar) {
		
		tableModel.insertRow(tableModel.getRowCount(), new Object[] { bar.timeStr(), bar.open(), bar.high(), bar.low(), bar.close(), bar.volume() });
		this.close = bar.close();
	}
	
	public void getLastRow() {

	
		for(int i=0;i<columnNames.length ;i++) {
		System.out.print(tableModel.getValueAt(tableModel.getRowCount()-1, i)+" ,, ");
		}
		System.out.println();
		System.out.println(tableModel.getRowCount());
	}
	
	public double getClose() {
//		System.out.println("TTT model =" +tableModel.getRowCount());
//		return Double.parseDouble(tableModel.getValueAt(tableModel.getRowCount()-1, 4).toString());
		return this.close;
	}
	public void resetTable() {
		if (tableModel.getRowCount() > 0) {
		    for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
		    	tableModel.removeRow(i);
		    }
		}
	}
	public int size() {
		return tableModel.getRowCount();
	}
}
