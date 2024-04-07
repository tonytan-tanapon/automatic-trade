package apitest;

import java.util.ArrayList;

import com.ib.controller.Bar;

public class IndyFixClose extends ArrayList<Double>{
	
	public IndyFixClose(String period, ArrayList<Bar> bars) {
		String datetime = bars.get(0).formattedTime().toString();
		String dateSplite[] = datetime.split(" ");
		
		String opentime = dateSplite[1];
		
		double previous =0.0;
		for(int i=0; i < bars.size(); i++) {
			String date = dateSplite[0];
			String time = dateSplite[1];
			datetime = bars.get(i).formattedTime().toString();
			dateSplite = datetime.split(" ");
			date = dateSplite[0];
			time = dateSplite[1];
		
			if(time.equals(opentime)){
				previous = bars.get(i).close();
			}		
			add(previous);
			
		}
		
		
	}
	
	ArrayList<Double> getFixOpen(){
		return this;
	}
}
