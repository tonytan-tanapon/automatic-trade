package apitest;

import java.util.ArrayList;

class StrategyTrade {
	ArrayList<Double> signal ;
	public StrategyTrade(HistoricalModel data) {
		
		IndyFixOpen yOpen = new IndyFixOpen("D",data.m_bars);
		IndyFixClose yClose = new IndyFixClose("D",data.m_bars);
		IndyFixOpen tOpen = new IndyFixOpen("D",data.m_bars);
		
		
		
		ArrayList<Double> close = new ArrayList<Double>();
		close = data.getColData(4); // close; 
		IndySMA sma21 = new IndySMA(21,close);
		
		ArrayList<Double> con1 = Indicator.moreThan(tOpen, yOpen);
		ArrayList<Double> con2 = Indicator.moreThan(tOpen, yClose);
		ArrayList<Double> trend  = Indicator.And(con1,con2);
		
		ArrayList<Double> con3 = Indicator.cross(close, sma21.getSMA());
		
		signal = Indicator.And(con3,trend);
	
	}
	
	public ArrayList<Double> getSignal(){
		return signal;
	}

}
