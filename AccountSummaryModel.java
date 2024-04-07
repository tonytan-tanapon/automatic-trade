package apitest;

import static com.ib.controller.Formats.fmt0;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.ib.client.Bar;
import com.ib.client.Contract;
import com.ib.client.Types.Right;
import com.ib.client.Types.SecType;
import com.ib.controller.Formats;
import com.ib.controller.AccountSummaryTag;
import com.ib.controller.ApiController.IAccountSummaryHandler;
import com.ib.controller.ApiController.IPositionHandler;

import apitest.PositionsPanel.PositionKey;
import apitest.PositionsPanel.PositionRow;

public class AccountSummaryModel extends AbstractTableModel implements IAccountSummaryHandler {

	List<SummaryRow> m_rows = new ArrayList<>();
	Map<String, SummaryRow> m_map = new HashMap<>();
	boolean m_complete;
	AutoTradePanel autoTradePanel;
	double accountValue = 0;

	AccountSummaryModel(AutoTradePanel autoTradePanel) {
		this.autoTradePanel = autoTradePanel;
	}
	AccountSummaryModel() {
		
	}
	public void reqAccount() {
		
		ApiDemo.INSTANCE.controller().reqAccountSummary("All", AccountSummaryTag.values(), this);
	}

	@Override
	public void accountSummary(String account, AccountSummaryTag tag, String value, String currency) {
		// TODO Auto-generated method stub
		SummaryRow row = m_map.get(account);
		if (row == null) {
			row = new SummaryRow();
			m_map.put(account, row);
			m_rows.add(row);
		}
		row.update(account, tag, value);

		if (m_complete) {
			fireTableDataChanged();
		}
		System.out.println(account + " " + tag + " " + value + " " + currency);

		//////////// get position by symbol
		if (tag.toString().equals("NetLiquidation")) {

			accountValue = Double.parseDouble(value);
		}

	}

	@Override
	public void accountSummaryEnd() {
		// TODO Auto-generated method stub
		fireTableDataChanged();
		m_complete = true;
		System.out.println("Corrected Account. ");

		//// here
		
		

		System.out.println("\n");
		ApiDemo.INSTANCE.controller().cancelAccountSummary(this);

	}

	@Override
	public int getRowCount() {
		return m_rows.size();
	}

	@Override
	public int getColumnCount() {
		return AccountSummaryTag.values().length + 1; // add one for Account column

	}

	@Override
	public String getColumnName(int col) {
		if (col == 0) {
			return "Account";
		}
		return AccountSummaryTag.values()[col - 1].toString();
	}

	@Override
	public Object getValueAt(int rowIn, int col) {
		SummaryRow row = m_rows.get(rowIn);

		if (col == 0) {
			return row.m_account;
		}

		AccountSummaryTag tag = AccountSummaryTag.values()[col - 1];
		String val = row.m_map.get(tag);

		switch (tag) {
		case Cushion:
			return fmtPct(val);
		case LookAheadNextChange:
			return fmtTime(val);
		default:
			return format(val, null);
		}
	}

	String fmtPct(String val) {
		return val == null || val.length() == 0 ? null : Formats.fmtPct(Double.parseDouble(val));
	}

	String fmtTime(String val) {
		return val == null || val.length() == 0 || val.equals("0") ? null : Formats.fmtDate(Long.parseLong(val) * 1000);
	}

	static String format(String val, String currency) {
		if (val == null || val.length() == 0) {
			return null;
		}

		try {
			double dub = Double.parseDouble(val);
			val = fmt0(dub);
		} catch (Exception ignored) {
		}

		return currency != null && currency.length() > 0 ? val + " " + currency : val;
	}

	private static class SummaryRow {
		String m_account;
		Map<AccountSummaryTag, String> m_map = new HashMap<>();

		public void update(String account, AccountSummaryTag tag, String value) {
			m_account = account;
			m_map.put(tag, value);
		}
	}

}
