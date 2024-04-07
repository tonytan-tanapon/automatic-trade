package apitest;

import java.util.ArrayList;

public class StrategyMA extends Strategy{
	
	
	public StrategyMA(HistoricalModel historicalModel) {
		super(historicalModel);
		// TODO Auto-generated constructor stub
		
		System.out.println(""+ close);
		
		
	}

	@Override
	public void setIndicator() {
		IndySMA sma1 = new IndySMA(5, close);
		historicalModel.addCol(sma1.getSMA(),"SMA1");
		
		IndySMA sma2 = new IndySMA(10, close);
		historicalModel.addCol(sma2.getSMA(),"SMA2");
		
		ArrayList<Double> list_signal = Indicator.cross(sma1.getSMA(), sma2.getSMA());
		setSignal(list_signal);
	}
	
}
