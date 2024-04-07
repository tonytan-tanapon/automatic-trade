package apitest;

import java.util.ArrayList;
import com.ib.controller.Bar;

public class IndyDonchain {
	ArrayList<Double> donchain = new ArrayList<Double>();
	ArrayList<Double> upper = new ArrayList<Double>();
	ArrayList<Double> lower = new ArrayList<Double>();
	ArrayList<Double> temp_up = new ArrayList<Double>();
	ArrayList<Double> temp_down = new ArrayList<Double>();

	ArrayList<Bar> data = null;

	public IndyDonchain(int period, ArrayList<Bar> bars) {
		setIndy(period, bars);
	}

	void setIndy(int period, ArrayList<Bar> bars) {
		int i = 0;
		for (Bar bar : bars) {
			
			double high = bar.high();
			double low = bar.low();
			
			if(i < period ) {
				upper.add(high);
				lower.add(low); 
				System.out.println("if " + i);
			}
			else {
				System.out.println("else " + i);
				double pre_high = highest(i,period, bars);
				if(high > pre_high) {
					
					upper.add(high);
				}
				else {
					upper.add(pre_high);
				}
//				
				double pre_low = lowest(i, period,bars);
				if(low < pre_low) {
					lower.add(low);
				}
				else {
					lower.add(pre_low);
				}
			}
			
			i++;

		}

	}
// 0 1 2 3 4 5 6 7 8 9 10 11 12
// 0 1 2 3 4 5 
//	 1 2 3 4 5 6 
//	   2 3 4 5 6 7
	double highest(int index, int period, ArrayList<Bar> bars) {
		int i = index - period;
		int end = period+i;
		double max = bars.get(i).high();
//		System.out.println("higest    "+i +" to "+ upper.size() +" or "+ end +" max = "+ max);
		
		for (; i <end; i++) {
			
			if (bars.get(i).high() > max) {
				max = bars.get(i).high();
			}
		}
	
		return max;
	}
	double lowest(int index, int period, ArrayList<Bar> bars) {
		int i = index - period;
		int end = period+i;
		double min = bars.get(i).low();
//		System.out.println("higest    "+i +" to "+ upper.size() +" or "+ end +" max = "+ max);
		
		for (; i <end; i++) {
			
			if (bars.get(i).low() < min) {
				min = bars.get(i).low();
			}
		}
	
		return min;
	}
	void setIndy2(int period, ArrayList<Bar> bars) {
		int i = 0;
		for (Bar bar : bars) {

			double high = bar.high();
			double low = bar.low();

			if (i < period - 1) { // before first bar
				temp_up.add(high);
				upper.add(0.0);

				temp_down.add(low);
				lower.add(0.0);
			} else if (i == period - 1) { // first bar
				temp_up.add(high);
				upper.add(highest(temp_up));

				temp_down.add(low);
				lower.add(highest(temp_down));

			} else { // other bars

				temp_up.add(high);
				upper.add(highest(temp_up));
				temp_up.remove(0);

				temp_down.add(low);
				lower.add(highest(temp_down));
				temp_down.remove(0);
			}

			i++;
		}

	}

	double highest(ArrayList<Double> upper) {

		double max = upper.get(0);
		for (int i = 1; i < upper.size(); i++) {

			if (upper.get(i) > max) {
				max = upper.get(i);
			}
		}
		return max;
	}

	double lowest(ArrayList<Double> lower) {

		double min = lower.get(0);
		for (int i = 1; i < lower.size(); i++) {

			if (lower.get(i) < min) {
				min = lower.get(i);
			}
		}
		return min;
	}

	public ArrayList<Double> getUpper() {

		return upper;
	}

	public ArrayList<Double> getLower() {

		return lower;
	}

}
