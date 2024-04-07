package apitest;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

public class InformationModel extends TableModelATS {
	InfomationRow infoRow= new InfomationRow();
	InformationModel() {
		String[] columnNames ={ "Symbol", "Account","signal","price","avgerage","position","P/L","traget","stop"};
//
		setColumnsName(columnNames);
		
	}
	
//	
//  static class InformationRow{
//		String symbol,account,signal,price,avgerage,position, PL,traget,stop;
//		
//		static public String[] getColumnName(){
//			String[] columnNames ={ "Symbol", "Account","signal","price","avgerage","position","P/L","traget","stop"};
//			return columnNames;
//		}		
//		 public String get(int i) {
//			switch(i) {
//			case 0: return symbol;
//			case 1: return account;
//			case 2: return signal;
//			case 3: return price;
//			case 4: return avgerage;
//			case 5: return position;
//			case 6: return PL;
//			case 7: return traget;
//			case 8: return stop;
//			
//			}
//			 return null;
//		}
//	}

}
