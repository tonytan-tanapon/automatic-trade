package apitest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.ib.client.Contract;
import com.ib.controller.Formats;


public class PositionModel extends AbstractTableModel{
	Map<PositionKey, PositionRow> m_map = new HashMap<>();
	List<PositionRow> m_list = new ArrayList<>();
	@Override
	public int getRowCount() {
		return m_map.size();
	}

	@Override 
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int col) {
		switch (col) {
		case 0:
			return "Account";
		case 1:
			return "Contract";
		case 2:
			return "Position";
		case 3:
			return "Avg Cost";
		default:
			return null;
		}
	}

	@Override
	public Object getValueAt(int rowIn, int col) {
		PositionRow row = m_list.get(rowIn);

		switch (col) {
		case 0:
			return row.m_account;
		case 1:
			return row.m_contract.description();
		case 2:
			return row.m_position;
		case 3:
			return Formats.fmt(row.m_avgCost);
		default:
			return null;
		}
	}

	static class PositionKey {
		String m_account;
		int m_conid;

		PositionKey( String account, int conid) {
			m_account = account;
			m_conid = conid;
		}
		
		@Override public int hashCode() {
			return m_account.hashCode() + m_conid;
		}
		
		@Override public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof PositionKey)) {
				return false;
			}
			PositionKey other = (PositionKey)obj;
			return m_account.equals( other.m_account) && m_conid == other.m_conid;
		}
	}

	static class PositionRow {
		String m_account;
		Contract m_contract;
		double m_position;
		double m_avgCost;

		void update(String account, Contract contract, double position, double avgCost) {
			m_account = account;
			m_contract = contract;
			m_position = position;
			m_avgCost = avgCost;
		}
	}

}
