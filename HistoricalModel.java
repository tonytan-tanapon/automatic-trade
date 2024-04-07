package apitest;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.table.AbstractTableModel;

import com.ib.controller.Bar;

public class HistoricalModel extends AbstractTableModel {
//	private Map<Long, Bar> m_map = new TreeMap<>();
	public ArrayList<Bar> m_bars = new ArrayList<Bar>();
	public ArrayList<ArrayList<Double>> barAdj = new ArrayList<ArrayList<Double>>();

	String[] list_name = { "Date/Time", "Open", "High", "Low", "Close", "Volume", "WAP"};
	ArrayList<String> col_name = new ArrayList<String>();

	public void clear() {
		m_bars.clear();
		barAdj.clear();
		col_name.clear();

		setDefualColume();

	}

	public void addBar(Bar bar) {

		m_bars.add(bar);
//		m_map.put(bar.time(), bar);
//		m_rows.clear();		
//		m_rows.addAll(m_map.values());
		

	}

	public void setBarFX(Bar bar) {
		addBar(bar);
		
	}

	public void setBarStock(Bar bar) {

		String datetime = bar.formattedTime();
		String time = datetime.split(" ")[1];
		String hh = time.split(":")[0];
		String mm = time.split(":")[1];
		String ss = time.split(":")[2];

		if (Double.parseDouble(hh) == 20 && Double.parseDouble(mm) >= 30) { // start at 20.30
			addBar(bar);
		} else if (Double.parseDouble(hh) > 20) {
			addBar(bar);
		} else if (Double.parseDouble(hh) == 2 && Double.parseDouble(mm) <= 45) {
			addBar(bar);
		} else if (Double.parseDouble(hh) < 2) {
			addBar(bar);
		}
		
	}

	public void addCol(ArrayList<Double> data, String string) {
		// TODO Auto-generated method stub

		col_name.add(string);
		barAdj.add(data);

		fireTableStructureChanged();
		fireTableDataChanged();

	}

	public void setDefualColume() {
		for (String name : list_name)
			col_name.add(name);
	}

	public String getLastRowData() {
		String raw_data = "";

		if (m_bars.size() > 0) {
			raw_data = m_bars.get(m_bars.size() - 1).toString();
		} else {
		}

		return raw_data;

	}

	// checked return only close column
	public ArrayList<Double> getColData(int col) {
		ArrayList<Double> out = new ArrayList<Double>();
		if (col != -1) {
			for (int i = 0; i < m_bars.size(); i++) {
				out.add((Double) getValueAt(i, col));
			}
		}
		return out;
	}

	// checked return only close column
	public ArrayList<Double> getColData(String colName) {
		
		int col = getColumIndex(colName);
		return getColData(col);
	}

	// checked return only close column
	public int getColumIndex(String colName) {
	
		for (int i = 0; i < col_name.size(); i++) {
			if (this.col_name.get(i).toString().equals(colName)) {
				return i;
			}
		}
		System.out.println("index Not Found ");
		return -1;
	}

	@Override
	public int getRowCount() {
		return m_bars.size();
	}

	@Override
	public int getColumnCount() {
		return col_name.size();
	}

	/// found!!!!!
	@Override
	public String getColumnName(int col) {
		String out = "";
		try {
			out = col_name.get(col);
		} catch (Exception ex) {
			System.out.println("error ====>" + col_name + " " + col);
		}
		return out;
	}
	public void getRowSignal() {
		
	}

	@Override
	public Object getValueAt(int rowIn, int col) {

		Bar row = m_bars.get(rowIn);
		switch (col) {
		case 0:
			return row.formattedTime();
		case 1:
			return row.open();
		case 2:
			return row.high();
		case 3:
			return row.low();
		case 4:
			return row.close();
		case 5:
			return row.volume();
		case 6:
			return row.wap();
		}
		try {
			Object out = barAdj.get(col - list_name.length).get(rowIn);
			return out;
		} catch (Exception e) {
			System.out.println("error >>>> rowIn " + rowIn + " " + col);
			return null;
		}
	}

}
