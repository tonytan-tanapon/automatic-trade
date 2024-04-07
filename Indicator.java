package apitest;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;

public class Indicator {
	private static final Format FMT2 = new DecimalFormat("#,##0.00");

	public static double floor(double d) {
//		DecimalFormat df = new DecimalFormat(".##");
//		return Double.parseDouble(df.format(d));
		return Math.floor(d * 100.0) / 100.0;

	}

	public static double floor(String d) {
//		DecimalFormat df = new DecimalFormat(".##");
//		return Double.parseDouble(df.format(d));
		return Math.floor(Double.parseDouble(d) * 100.0) / 100.0;

	}

	/** Format with two decimals. */
	public static String fmt(double v) {
		return v == Double.MAX_VALUE ? null : FMT2.format(v);
	}

	/** Format with two decimals. */
	public static String fmt(String v) {
		return Double.parseDouble(v) == Double.MAX_VALUE ? null : FMT2.format(v);
	}

	public static ArrayList<Double> cross(ArrayList<Double> indy1, ArrayList<Double> indy2) {
		ArrayList<Double> signal = new ArrayList<Double>();

		for (int i = 0; i < indy1.size(); i++) {
			if (i == 0) {
				signal.add(0.0);
			}

			else if (indy1.get(i - 1) < indy2.get(i - 1) && indy1.get(i) > indy2.get(i)) {
				signal.add(1.0); // cross up
			} else if (indy1.get(i - 1) > indy2.get(i - 1) && indy1.get(i) < indy2.get(i)) {
				signal.add(-1.0); // cross down
			} else {
				signal.add(0.0);
			}

		}

		return signal;

	}

	public static ArrayList<Double> UpperOrLower(ArrayList<Double> sma, ArrayList<Double> fixOpen) {
		// TODO Auto-generated method stub
		ArrayList<Double> signal = new ArrayList<Double>();
		for (int i = 0; i < sma.size(); i++) {
			if (sma.get(i) > fixOpen.get(i)) {
				signal.add(1.0);
			} else {
				signal.add(-1.0);
			}
		}
		return signal;
	}

	public static ArrayList<Double> And(ArrayList<Double> sig1, ArrayList<Double> sig2) {

		ArrayList<Double> signal = new ArrayList<Double>();
		for (int i = 0; i < sig1.size(); i++) {
//			
			if (sig1.get(i) == 1 && sig2.get(i) == 1) {
				signal.add(1.0);

			} else if (sig1.get(i) == -1 && sig2.get(i) == -1) {
				signal.add(-1.0);

			} else {
				signal.add(0.0);
			}
		}

		return signal;
	}

	public static ArrayList<Double> moreThan(ArrayList<Double> bar1, ArrayList<Double> bar2) {
		// TODO Auto-generated method stub
		ArrayList<Double> out = new ArrayList<Double>();
		for (int i = 0; i < bar1.size(); i++) {
			if (bar1.get(i) > bar2.get(i)) {
				out.add(1.0);
			} else {
				out.add(0.0);
			}
		}

		return out;
	}
}
