package apitest;

import java.util.ArrayList;

import com.ib.controller.Bar;

public class IndyATR {

	
	ArrayList<Double> TR = new ArrayList<Double>();
	ArrayList<Double> ATR = new ArrayList<Double>();
	
	int period = 14;
	String sma_type = "sma"; // or rma
	public IndyATR(int period,ArrayList<Bar> bars,String sma_type) {
		this.period = period;
		setATR(bars);
	}
	public void setATR(ArrayList<Bar> bars) {
		for (int i = 0; i < bars.size(); i++) {

		double h = bars.get(i).high();
			double l = bars.get(i).low();
			if (i == 0) {
				double tr1 = h-l;
				TR.add(Indicator.floor(tr1));

			} else {
				double c1 = bars.get(i-1).close();
				double tr1 =  Math.abs(h-l);
				double tr2 = Math.abs(h-c1);
				double tr3 = Math.abs(c1-l);
				TR.add(Indicator.floor(Math.max(tr1,Math.max(tr2,tr3))));
			}			
		}			
	}
	
	
	public  ArrayList<Double> getTR() {
		return TR;
	}
	
	public ArrayList<Double> getATR() {
		switch(sma_type) {
		case "sma": 
			IndySMA s = new IndySMA(period,TR);
			ATR = s.getSMA();
			return ATR;
		case "rma": 
			IndyRMA s2 = new IndyRMA(period,TR);
			s2.setRMA(period, TR);
			ATR = s2.getRMA();
			return ATR;
		 default: 
			 return null;
		}
		
	}
	

}
