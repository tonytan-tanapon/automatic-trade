package apitest;

import com.ib.client.Contract;
import com.ib.client.Types.BarSize;
import com.ib.client.Types.SecType;

public class AutoTradeBuilder {
	// Set of variable
	// require
	 String symbol; 
	 SecType sectype;
	 double qty;
	 double traget;
	 double stop;
//	optional
	 String day = "5";
	 BarSize barsize = BarSize._15_mins;
	
	AutoTradeBuilder(String symbol, SecType sectype, double qty, double traget, double stop){
		this.symbol = symbol; 
		this.sectype = sectype;
		this.qty = qty;
		this.traget = traget;
		this.stop = stop; 
	}
	public AutoTradeBuilder setBarSize(BarSize barsize) {
		this.barsize = barsize;
		return this;
	}
	public AutoTradeBuilder setDay(String day) {
		this.day = day;
		return this;
	}
	
	AutoTradePanel build() {
		return new AutoTradePanel(this);
	}
	
	
}
