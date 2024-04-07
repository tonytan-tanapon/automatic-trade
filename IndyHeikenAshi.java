package apitest;

import java.util.ArrayList;

import com.ib.controller.Bar;

public class IndyHeikenAshi {
	private final ArrayList<Double> open = new ArrayList<Double>();
	private final ArrayList<Double> high = new ArrayList<Double>();
	private final ArrayList<Double> low = new ArrayList<Double>();
	private final ArrayList<Double> close = new ArrayList<Double>();

	private final ArrayList<Double> HAO = new ArrayList<Double>();
	private final ArrayList<Double> HAC = new ArrayList<Double>();
	private final ArrayList<Double> HAH = new ArrayList<Double>();
	private final ArrayList<Double> HAL = new ArrayList<Double>();

	IndyHeikenAshi(ArrayList<Bar> bars) {
		setHeiken(bars);
	}

	public void setHeiken(ArrayList<Bar> bars) {
		for (Bar bar : bars) {
			addData(bar);
		}

	}
	public void addData(Bar bar) {
		open.add(bar.open());
		high.add(bar.high());
		low.add(bar.low());
		close.add(bar.close());

		HAC.add(Indicator.floor((open.get(open.size() - 1) + high.get(open.size() - 1) + low.get(open.size() - 1)
				+ close.get(open.size() - 1)) / 4));

		if (HAO.size() == 0) {
			HAO.add(Indicator.floor((open.get(open.size() - 1) + close.get(open.size() - 1)) / 2));
			HAH.add(Indicator.floor(high.get(high.size() - 1)));
			HAL.add(Indicator.floor(low.get(low.size() - 1)));
		} else {

			HAO.add(Indicator.floor((HAO.get(HAO.size() - 1) + HAC.get(HAC.size() - 1)) / 2));
			HAH.add(Indicator.floor(
					Math.max(Math.max(high.get(open.size() - 1), HAO.get(open.size() - 1)), HAC.get(open.size() - 1))));
			HAL.add(Indicator.floor(
					Math.min(Math.min(low.get(open.size() - 1), HAO.get(open.size() - 1)), HAC.get(open.size() - 1))));
		}
	}

	public ArrayList<Double> HAO() {
		return HAO;
	}

	public ArrayList<Double> HAC() {
		return HAC;
	}

	public ArrayList<Double> HAH() {
		return HAH;
	}

	public ArrayList<Double> HAL() {
		return HAL;
	}
}


//public ArrayList<String> toStringArray(ArrayList<Double> data) {
//	ArrayList<String> out = new ArrayList<String>();
//	for (double d : data) {
//		out.add("" + d);
//	}
//	return out;
//}


