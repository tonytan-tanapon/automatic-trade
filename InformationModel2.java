package apitest;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

public class InformationModel2 extends TableModelATS {


	public  ArrayList<Double> m_information = new ArrayList<>();
	InfomationRow infoRow = new InfomationRow();
	String[] columnNames = { "Symbol", "Account","signal","price","avgerage","position","P/L","traget","stop"};
	Vector<String[]> rows_data = new Vector<String[]>();
	
	public void clear() {
		rows_data.clear();
	}
	public void insertRow(InfomationRow infoRow ) {
		String[] rowData = new String[columnNames.length];
		for (int i = 0; i < columnNames.length; i++) {
			rowData[i] = infoRow.get(i);
		}
		rows_data.addElement(rowData);
		fireTableChanged(null);
	}
	@Override
	public int getRowCount() {
		return rows_data == null ? 0 : rows_data.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return rows_data.elementAt(rowIndex)[columnIndex];
	}
	
	@Override public String getColumnName(int col) {
		return columnNames[col];
	}
	
	
}
