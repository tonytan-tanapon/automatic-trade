package apitest;

import java.util.ArrayList;
import java.util.Arrays;

import com.ib.controller.Bar;

public class StrategySMA {

	double signal = 0;
	public StrategySMA(HistoricalModel data) {
		// TODO Auto-generated constructor stub
		
		ArrayList<Double> close = data.getColData("Close");
		IndySMA _sma21 = new IndySMA(21, close);
		ArrayList<Double> sma21 = _sma21.getSMA();
//		data.addCol(sma21, "SMA21");
//		System.out.println("sma1 => "+ sma21);

		IndyFixOpen _fopen = new IndyFixOpen("D",data.m_bars);
		ArrayList<Double> fopen = _fopen.getFixOpen();
//		data.addCol(fopen, "fixOpen");
//		System.out.println(fopen);
		
//		IndyDonchain donchain = new IndyDonchain(10, data.m_bars); 
//		System.out.println("donchain => "+ donchain.getLower());
		
		IndyATRStop _atrstop = new IndyATRStop(10,data.m_bars,"rma");
//		ArrayList<Double> atrstop = _atrstop.getATRStop();
//		ArrayList<Double> atrstop = _atrstop.getSignal();
//		System.out.println("atrstop "+_atrstop.getATRStop());
		System.out.println("signal"+_atrstop.getSignal());
//		System.out.println("trend"+_atrstop.getTrend());
//		data.addCol(_atrstop.getATRStop(), "ATRSTOP");
//		data.addCol(_atrstop.getSignal(), "sig");
//		data.addCol(_atrstop.getTrend(), "trend");
		
		ArrayList<Double> conditionArea = Indicator.UpperOrLower(_sma21.getSMA(), _fopen.getFixOpen());
		ArrayList<Double> conditionATRchange = Indicator.And(conditionArea, _atrstop.getSignal());
		System.out.println("sig "+conditionATRchange);
		System.out.println();
//		data.addCol(conditionArea, "UpperLower");
		data.addCol(conditionATRchange, "Signal");
		
		// for return signal
		signal = conditionATRchange.get(conditionATRchange.size()-2);

	}

	public double getSignal() {
		return signal;
	}
}
