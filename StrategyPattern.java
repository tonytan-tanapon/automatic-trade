package apitest;

import java.util.ArrayList;

import com.ib.client.Bar;

public class StrategyPattern {
	ArrayList<Bar> bars = new ArrayList<Bar>();
//	option
	ArrayList<Indicator> indicators =new ArrayList<Indicator>();
	StrategyPattern(ArrayList<Bar> bars ){
		this.bars =bars;
		
	}
}
