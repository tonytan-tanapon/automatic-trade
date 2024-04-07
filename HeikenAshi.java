package apitest;


import java.util.ArrayList;

import com.ib.controller.Bar;


//HAO = (HAO-1 + HAC-1) / 2
//* 
//* HAC = (O + H + L + C) / 4
//* 
//* HAH = Highest(H, HAO, HAC)
//* 
//* HAL = Lowest(L, HAO, HAC
public class HeikenAshi {
	private final ArrayList<Double> open = new ArrayList<Double>();
	private final ArrayList<Double> high = new ArrayList<Double>();
	private final ArrayList<Double> low = new ArrayList<Double>();
	private final ArrayList<Double> close = new ArrayList<Double>();
	
	private final ArrayList<Double> HAO = new ArrayList<Double>();
	private final ArrayList<Double> HAC = new ArrayList<Double>();
	private final ArrayList<Double> HAH = new ArrayList<Double>();
	private final ArrayList<Double> HAL = new ArrayList<Double>();
	
	HeikenAshi(){
		
	}
	public ArrayList<String> toStringArray(ArrayList<Double> data){
		ArrayList<String> out = new ArrayList<String>();
		for(double d: data) {
			out.add(""+d);
		}
		return out;
	}
	public ArrayList<String> HAO() {
	
		return toStringArray(HAO);
	}
	public ArrayList<String> HAC() {
		return toStringArray(HAC);
	}
	public ArrayList<String> HAH() {
		return toStringArray(HAH);
	}
	public ArrayList<String> HAL() {
		return toStringArray(HAL);
	}
	public void addData(Bar bar) {
		open.add(bar.open());
		high.add(bar.high());
		low.add(bar.low());
		close.add(bar.close());
		
		HAC.add((open.get(open.size()-1)+high.get(open.size()-1)+low.get(open.size()-1)+close.get(open.size()-1))/4);

		if(HAO.size() == 0) {
			HAO.add((open.get(open.size()-1)+close.get(open.size()-1))/2);
			HAH.add(high.get(high.size()-1));
			HAL.add(low.get(low.size()-1));
		}
		else {			

			HAO.add((HAO.get(HAO.size()-1) +HAC.get(HAC.size()-1)) / 2);			
			HAH.add(Math.max(  Math.max(  high.get(open.size()-1)  ,HAO.get(open.size()-1)  ),   HAC.get(open.size()-1)))  ;
			HAL.add(Math.min(  Math.min(  low.get(open.size()-1)   ,HAO.get(open.size()-1)  ),    HAC.get(open.size()-1)))  ;
//			
		}
		
	}

	public double getHAO() {
//		System.out.println("HAO: "+ HAO + " size " + HAO.size() );
//		return 0;
		return HAO.get(HAO.size()-1);
	}
	public double getHAC() {
//		return 0;
		return HAC.get(HAC.size()-1);
	}
	public double getHAH() {
//		return 0;
		return HAH.get(HAH.size()-1);
	}
	public double getHAL() {
//		return 0;
		return HAL.get(HAL.size()-1);
	}

}
