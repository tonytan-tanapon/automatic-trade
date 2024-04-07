package apitest;

import javax.swing.table.DefaultTableModel;

public class BackTestResult extends DefaultTableModel {
	String cols[] = new String[] {"Date/time","Sig","buy","sell","PL","%"};
	
	BackTestResult(){
		for(String col: cols ) {
			addColumn(col);
		}
	}
	
	
}
