package apitest;

import java.util.ArrayList;

public abstract class Strategy {
	HistoricalModel historicalModel;
	ArrayList<Double> open;
	ArrayList<Double> close;
	ArrayList<Double> high;
	ArrayList<Double> low;
	ArrayList<Long> volume;
	ArrayList<Double> list_signal;
	int signal = 0;
	public Strategy(HistoricalModel historicalModel) {
		this.historicalModel = historicalModel;
		// TODO Auto-generated constructor stub
		// 0 Date/Time, 1 Open, 2 High, 3 Low, 4 Close, 5 Volume, 6 WAP

		open = historicalModel.getColData("Open");
		close = historicalModel.getColData("Close");
		high = historicalModel.getColData("High");
		low = historicalModel.getColData("Low");
	

	}

	abstract public void setIndicator() ;
	public void setSignal(ArrayList<Double> list_signal) {
		this.list_signal = list_signal;
		historicalModel.addCol(list_signal,"Signal");
	}
	
	public int getSignal() {
		return signal;
	}
	
}
