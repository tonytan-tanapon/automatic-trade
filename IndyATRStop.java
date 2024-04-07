package apitest;

import java.util.ArrayList;

import com.ib.controller.Bar;

public class IndyATRStop {
	ArrayList<Bar> bars;
	ArrayList<Double> ATR = new ArrayList<Double>();
	ArrayList<Double> ATRlongStop = new ArrayList<Double>(); /// price is below close
	ArrayList<Double> ATRshortStop = new ArrayList<Double>(); /// price is top close
	ArrayList<Double> ATRstop = new ArrayList<Double>();
//	Series price = new Series();
	ArrayList<Double> trend = new ArrayList<Double>();
	ArrayList<Double> a = new ArrayList<Double>();
	ArrayList<Double> cPos = new ArrayList<Double>();
	ArrayList<Double> signal = new ArrayList<Double>(); // buy 1, sell -1, do not thing 0
	ArrayList<Double> longStop = new ArrayList<Double>();
	ArrayList<Double> shortStop = new ArrayList<Double>();
	double multiple = 1;
	
	IndyATRStop(int period, ArrayList<Bar> bars, String ma){
		IndyATR s = new IndyATR(period,bars,ma);
		ATR = s.getATR();
		int i = 0;
		for (Bar bar : bars) {

			double high = bar.high();
			double low = bar.low();
			double close = bar.close();
			double HL2 = (high + low) / 2; // 5+4/2 =4.5


//			System.out.println( HL2 + " "+ longStop + " " + shortStop  );
			if (i < period - 1) { // before first bar
				signal.add(0.0);
				trend.add(-1.0);
				ATRlongStop.add(-1.0);
				ATRshortStop.add(-1.0);

				longStop.add(-1.0);
				shortStop.add(-1.0);
			} else if (i == period - 1) { // first bar
				signal.add(0.0);
				trend.add(1.0);

				longStop.add(HL2 - ATR.get(i)*multiple);
				shortStop.add(HL2 + ATR.get(i)*multiple);

				ATRlongStop.add(longStop.get(i));
				ATRshortStop.add(shortStop.get(i));

			} else { // other bars
				longStop.add(HL2 - ATR.get(i)*multiple);
				shortStop.add(HL2 + ATR.get(i)*multiple);

				if (trend.get(i - 1) == 1) {
					if (longStop.get(i) > ATRlongStop.get(i - 1)) {
						ATRlongStop.add(longStop.get(i));
					} else {
						ATRlongStop.add(ATRlongStop.get(i - 1));
					}
					ATRshortStop.add(shortStop.get(i));
				} else {
					if (shortStop.get(i) < ATRshortStop.get(i - 1)) {
						ATRshortStop.add(shortStop.get(i));
					} else {
						ATRshortStop.add(ATRshortStop.get(i - 1));
					}

					ATRlongStop.add(longStop.get(i));
				}
//				trend.add(i-1);
//				
				if(trend.get(i-1) == 1 && close < ATRlongStop.get(i)) {
					trend.add(0.0);
				}
				else if(trend.get(i-1) == 0 && close >ATRshortStop.get(i)) {
					trend.add(1.0);
				}
				else {
					trend.add(trend.get(i-1));
				}

//				/// signal 
				if(trend.get(i) == 1 && trend.get(i-1) == 0) {
					signal.add(1.0);
				}else if(trend.get(i) == 0 && trend.get(i-1) == 1) {
					signal.add(-1.0);
				}
				else {
					
						signal.add(0.0);
				
				}

			}

			if(trend.get(i) ==1 ) {
				ATRstop.add(ATRlongStop.get(i));
			}
			else {
				ATRstop.add(ATRshortStop.get(i));
			}

			i++;
		}
	}
	
	public void setIndy(int period, ArrayList<Bar> bars) {
		IndyATR s = new IndyATR(period,bars,"rma");
//		ATR = s.getATR(period, "rma");
//		System.out.println(ATR.toStringArray());
		int i = 0;
		for (Bar bar : bars) {

			double high = bar.high();
			double low = bar.low();
			double close = bar.close();
			double HL2 = (high + low) / 2; // 5+4/2 =4.5


//			System.out.println( HL2 + " "+ longStop + " " + shortStop  );
			if (i < period - 1) { // before first bar
				signal.add(0.0);
				trend.add(-1.0);
				ATRlongStop.add(-1.0);
				ATRshortStop.add(-1.0);

				longStop.add(-1.0);
				shortStop.add(-1.0);
			} else if (i == period - 1) { // first bar
				signal.add(0.0);
				trend.add(1.0);

				longStop.add(HL2 - ATR.get(i)*multiple);
				shortStop.add(HL2 + ATR.get(i)*multiple);

				ATRlongStop.add(longStop.get(i));
				ATRshortStop.add(shortStop.get(i));

			} else { // other bars
				longStop.add(HL2 - ATR.get(i)*multiple);
				shortStop.add(HL2 + ATR.get(i)*multiple);

				if (trend.get(i - 1) == 1) {
					if (longStop.get(i) > ATRlongStop.get(i - 1)) {
						ATRlongStop.add(longStop.get(i));
					} else {
						ATRlongStop.add(ATRlongStop.get(i - 1));
					}
					ATRshortStop.add(shortStop.get(i));
				} else {
					if (shortStop.get(i) < ATRshortStop.get(i - 1)) {
						ATRshortStop.add(shortStop.get(i));
					} else {
						ATRshortStop.add(ATRshortStop.get(i - 1));
					}

					ATRlongStop.add(longStop.get(i));
				}
//				trend.add(i-1);
//				
				if(trend.get(i-1) == 1 && close < ATRlongStop.get(i)) {
					trend.add(0.0);
				}
				else if(trend.get(i-1) == 0 && close >ATRshortStop.get(i)) {
					trend.add(1.0);
				}
				else {
					trend.add(trend.get(i-1));
				}

//				/// signal 
				if(trend.get(i) == 1 && trend.get(i-1) == 0) {
					signal.add(1.0);
				}else if(trend.get(i) == 0 && trend.get(i-1) == 1) {
					signal.add(-1.0);
				}
				else {
					
						signal.add(0.0);
				
				}

			}

			if(trend.get(i) ==1 ) {
				ATRstop.add(ATRlongStop.get(i));
			}
			else {
				ATRstop.add(ATRshortStop.get(i));
			}

			i++;
		}
//		System.out.println("trend "+trend.size());
//		System.out.println("ATRstop "+ATRstop.size());
//		System.out.println("signal "+signal.size());
//		System.out.println("ATRlongStop "+ATRlongStop.size());
//		System.out.println("ATRshortStop "+ATRshortStop.size());
//		

	}

	public ArrayList<Double> getTrend() {
		return trend;
	}

	public ArrayList<Double> getATRStop() {
		return ATRstop;
	}

	public ArrayList<Double> getSignal() {
		return signal;
	}

	public ArrayList<Double> getLongStop() {
		return ATRlongStop;
	}

	public ArrayList<Double> getShortgStop() {
		return ATRshortStop;
	}

	public ArrayList<Double> getlongSP() {
		return longStop;
	}

	public ArrayList<Double> getshortSP() {
		return shortStop;
	}

}
