package apitest;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.ib.client.Contract;
import com.ib.client.Decimal;

public class MyPosition {
	String[] columnNames = {"Contract","pos","avgCost"};
	String[][] data;
	JTable historicalBar = new JTable(data,columnNames);
	int i=0;
	DefaultTableModel tableModel = new DefaultTableModel();
//	JTable table = new JTable(tableModel);
	Contract contract = new Contract();
	MyPosition(){
		for(int i=0;i<columnNames.length ;i++) {
			tableModel.addColumn(columnNames[i]);
		}
		
	}
	public void setPosition(String account, Contract contract, Decimal pos, double avgCost) {
		
		if(Double.parseDouble(""+pos) > 0.0 ) {
		System.out.println("YESSSSSSSSSSSSSSSSSSSs");
			tableModel.insertRow(tableModel.getRowCount(), new Object[] { contract,pos,avgCost });
			this.contract = contract;
		}
		else {
			System.out.println("NOOOOOOOOOOOOOOOOO");
		}
		
	}
	public void getLastRow() {

		
		for(int i=0;i<columnNames.length ;i++) {
		System.out.print(tableModel.getValueAt(tableModel.getRowCount()-1, i)+" ,, ");
		}
		System.out.println();
		System.out.println(tableModel.getRowCount());
	}
	
	public double getPosition() {
		if(tableModel.getRowCount() == 0) {
			return 0;
		}
		return Double.parseDouble(tableModel.getValueAt(tableModel.getRowCount()-1, 1).toString());
	}
	public Contract getPositionContract() {
		this.contract.exchange("SMART");
		return this.contract;
	}
	public void resetTable() {
		System.out.println("REST START + "  +tableModel.getRowCount());
		if (tableModel.getRowCount() > 0) {
		    for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
		    	System.out.println("iiiiiiiii = "+ i);
		    	tableModel.removeRow(i);
		    }
		}
		System.out.println("REST SSTOP");
	}
	public int size() {
		return tableModel.getRowCount();
	}

}
