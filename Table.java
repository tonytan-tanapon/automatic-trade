package apitest;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

public class Table extends JTable {
	private int m_n;

	public Table(AbstractTableModel model) {
		this( model, 1);
	}

	public Table(AbstractTableModel model, int n) {
		super( model);
		m_n = n;
	}

	@Override public TableCellRenderer getCellRenderer(int row, int col) {
		TableCellRenderer rend = super.getCellRenderer(row, col);
		((JLabel)rend).setHorizontalAlignment( col < m_n ? SwingConstants.LEFT : SwingConstants.RIGHT);
		return rend;
	}
}