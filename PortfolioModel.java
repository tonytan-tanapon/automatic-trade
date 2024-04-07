package apitest;

import static com.ib.controller.Formats.fmt0;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.ib.client.Types.SecType;
import com.ib.controller.Position;

public class PortfolioModel extends AbstractTableModel {
	private Map<Integer,Position> m_portfolioMap = new HashMap<>();
	private List<Integer> m_positions = new ArrayList<>(); // must store key because Position is overwritten
	
	void clear() {
		m_positions.clear();
		m_portfolioMap.clear();
	}
	
	Position getPosition( int i) {
		return m_portfolioMap.get( m_positions.get( i) );
	}

	public void update( Position position) {
		// skip fake FX positions
		if (position.contract().secType() == SecType.CASH) {
			return;
		}

		if (!m_portfolioMap.containsKey( position.conid() ) &&
				Double.parseDouble(""+position.position() )!= 0) {
			m_positions.add( position.conid() );
		}
		m_portfolioMap.put( position.conid(), position);
		fireTableDataChanged();
	}

	@Override public int getRowCount() {
		return m_positions.size();
	}

	@Override public int getColumnCount() {
		return 7;
	}
	
	@Override public String getColumnName(int col) {
		switch( col) {
			case 0: return "Description";
			case 1: return "Position";
			case 2: return "Price";
			case 3: return "Value";
			case 4: return "Avg Cost";
			case 5: return "Unreal Pnl";
			case 6: return "Real Pnl";
			default: return null;
		}
	}

	@Override public Object getValueAt(int row, int col) {
		Position pos = getPosition( row);
		switch( col) {
			case 0: return pos.contract().description();
			case 1: return pos.position();
			case 2: return pos.marketPrice();
			case 3: return format( "" + pos.marketValue(), null);
			case 4: return pos.averageCost();
			case 5: return pos.unrealPnl();
			case 6: return pos.realPnl();
			default: return null;
		}
	}
	static String format(String val, String currency) {
		if (val == null || val.length() == 0) {
			return null;
		}

		try {
			double dub = Double.parseDouble( val);
			val = fmt0( dub);
		} catch (Exception ignored) {
		}
		
		return currency != null && currency.length() > 0
			? val + " " + currency : val;
	}

}
