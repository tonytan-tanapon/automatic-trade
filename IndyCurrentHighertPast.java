package apitest;

import java.util.ArrayList;

import com.ib.controller.Bar;

public class IndyCurrentHighertPast {

	ArrayList<Double> signal = new ArrayList<Double>();
	public IndyCurrentHighertPast(ArrayList<Double> current, ArrayList<Double> past) {
		// TODO Auto-generated constructor stub
		
//		current   past   new past
//		0			0	
//		1			1	0
//		2			2 	1
//		3			3 	2
//		4			4	3
//		5			5	4
//						5
		
		for(int i = 0 ;i<current.size() ; i++ ) {
			if(i >= 1) {
				// if current is higher than pass
				if(current.get(i) > past.get(i-1)) {
					signal.add(1.0);
				}
				else if(current.get(i) < past.get(i-1)) 
				{
					signal.add(-1.0);
				}
				else {
					signal.add(0.0);
				}
			}
			else {
				signal.add(0.0);
			}
		}
		
	}
	public ArrayList<Double> getSignal(){
		return signal;
	}

}
