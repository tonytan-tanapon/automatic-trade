package apitest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class IndyRMA {
	ArrayList<Double> rma = new ArrayList<Double>();
	ArrayList<Double> data = null;

	// queue used to store list so that we get the average
	private double sum = 0;

	public IndyRMA(int period, ArrayList<Double> data) {
		setRMA(period,data);
	}
	void setRMA(int period, ArrayList<Double> data) {
		for (int i = 0; i < data.size(); i++) {
			if (i < period - 1) {
				sum += data.get(i);
				rma.add(0.0);
			} else if (i == period - 1) {
				sum += data.get(i);
				rma.add(sum / (i + 1));
			} else {
				rma.add(((rma.get(i - 1) * (period - 1)) + data.get(i)) / period);
			}
		}
	}

	public ArrayList<Double> getRMA() {
		return rma;
	}

	public double getBar(int index) {
		return rma.get(rma.size() - index - 1);
	}

}
