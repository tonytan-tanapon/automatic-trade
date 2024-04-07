package apitest;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class PanelData extends JPanel {
	JScrollPane scrollPane;
	PanelData(AbstractTableModel data ){
		JTable table = new JTable(data);
		scrollPane= new JScrollPane(table);

//		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  
//		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  
		add(scrollPane);
		
	}
	public void setScrollPane(AbstractTableModel data) {
		JTable table = new JTable(data);
		scrollPane= new JScrollPane(table);
	}
	JScrollPane getScrollPane() {
		return scrollPane;
	}
	public void setScrollToButtom() {

		scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
//		scrollPane.getVerticalScrollBar().setValue(scrollPane.getHorizontalScrollBar().getMaximum());

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
//		scrollPane.getVerticalScrollBar().setValue(scrollPane.getHorizontalScrollBar().getMaximum());
	}
	
}
