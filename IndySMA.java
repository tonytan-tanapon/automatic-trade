package apitest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class IndySMA extends ArrayList<Double>  {
	


	// queue used to store list so that we get the average
	private Queue<Double> Dataset = new LinkedList<Double>();

	private double sum;
	public IndySMA(int period, ArrayList<Double> data) {
		setSMA(period,data);
	}
	void setSMA(int period, ArrayList<Double> data) {
		for (int i = 0; i < data.size(); i++) {
			sum+=data.get(i);
			if (i < period - 1) {
				Dataset.add(data.get(i));
				add(0.0);				
			} else if (i == period - 1) {
				Dataset.add(data.get(i));
				add(sum/period);
			} else {
				Dataset.add(data.get(i));
				sum-=Dataset.remove();
				add(sum/period);
			}
		}
	}
	public ArrayList<Double> getSMA() {

		return this;
	}
	public  double getBar(int index) {
		return get(size()- index-1);
	}

}	


