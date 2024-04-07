package apitest;

import java.util.ArrayList;

public class IndyOperate {
	
	ArrayList<Double> result = new ArrayList<Double>();
	public IndyOperate() {
		
	}
//	a  			b   new b
//	0			0	
//	1			1	0
//	2			2 	1
//	3			3 	2
//	4			4	3
//	5			5	4
//					5
	public ArrayList<Double>  compareAhigherB_shiftB(ArrayList<Double> a, ArrayList<Double> b, int shift_b) {
		result.clear();
		for(int i = 0 ;i<a.size() ; i++ ) {
			if(i >= shift_b) {
				// if current is higher than pass
				if(a.get(i) > b.get(i-1)) {
					result.add(1.0);
				}				
				else {
					result.add(0.0);
				}
			}
			else {
				result.add(0.0);
			}
		}
		
		return result;
	}
	public ArrayList<Double>  compareAlowerB_shiftB(ArrayList<Double> a, ArrayList<Double> b, int shift_b) {
		result.clear();
		for(int i = 0 ;i<a.size() ; i++ ) {
			if(i >= shift_b) {
				// if current is higher than pass
				if(a.get(i) < b.get(i-1)) {
					result.add(-1.0);
				}				
				else {
					result.add(0.0);
				}
			}
			else {
				result.add(0.0);
			}
		}
		
		return result;
		
	}
}
