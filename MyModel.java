package apitest;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.table.AbstractTableModel;

class MyModel extends AbstractTableModel {

	String[] columnNames = new String[0];
	Vector<String[]> rows_data = new Vector<String[]>();

	MyModel(ResultSet results) {
		setResultSet(results);
	}

	public void setResultSet(ResultSet results) {
		try {
			ResultSetMetaData mdata = results.getMetaData();
			int columns = mdata.getColumnCount();
			columnNames = new String[columns];

			for (int i = 0; i < columns; i++) {
				columnNames[i] = mdata.getColumnLabel(i + 1);
			}

			rows_data.clear();
			String[] rowData;

			while (results.next()) {
				rowData = new String[columns];
				for (int i = 0; i < columns; i++)
					rowData[i] = results.getString(i + 1);
				rows_data.addElement(rowData);
			}
			fireTableChanged(null);

		} catch (SQLException se) {
			se.printStackTrace();
		}

	}

	public void insertEmptyRow() {
		String[] rowData = new String[columnNames.length];
		for (int i = 0; i < columnNames.length; i++)
			rowData[i] = "";
		rows_data.addElement(rowData);
		fireTableChanged(null);
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return rows_data == null ? 0 : rows_data.size();
	}

	public String getValueAt(int row, int column) {
		return rows_data.elementAt(row)[column];
	}

	public String getColumnName(int column) {
		return columnNames[column];
	}

	
	
}
