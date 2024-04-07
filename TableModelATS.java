package apitest;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

class TableModelATS extends AbstractTableModel {
	private  ArrayList<Double> m_information = new ArrayList<>();
//	public InfomationRow infoRow = new InfomationRow();
//	public Object infoRow = new Object();
	public TableRow irow= new TableRow();
	private ArrayList<String> colName = new ArrayList<String>();
	private Vector<String[]> rows_data = new Vector<String[]>();
	
	public void setColumnsName(String[] col) {
		for(int i=0;i<col.length; i++) {
			colName.add(col[i]);
		}
	}
	

	public void clear() {
		rows_data.clear();
	}
	public void insertRow(InfomationRow infoRow ) {
		String[] rowData = new String[colName.size()];
		for (int i = 0; i < colName.size(); i++) {
			rowData[i] = infoRow.get(i);
		}
		rows_data.addElement(rowData);
		fireTableChanged(null);
	}
//	public void insertRow(InfomationRow infoRow ) {
//		String[] rowData = new String[colName.size()];
//		for (int i = 0; i < colName.size(); i++) {
//			rowData[i] = infoRow.get(i);
//		}
//		rows_data.addElement(rowData);
//		fireTableChanged(null);
//	}
	@Override
	public int getRowCount() {
		return rows_data == null ? 0 : rows_data.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return colName.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return rows_data.elementAt(rowIndex)[columnIndex];
	}
	
	@Override public String getColumnName(int col) {
		return colName.get(col);
	}
	
	public Object getValue(String name) {
		for(int i=0;i<colName.size();i++) {
			if(colName.get(i).equals(name)) {
				return rows_data.elementAt(0)[i]; 
			}
			
		}
		return null;
	}
}
