package apitest;

import java.util.Vector;

public class InfomationRow {
	String symbol,account,signal,price,avgerage,position, PL,traget,stop;
	
	
	public String get(int i) {
		switch(i) {
		case 0: return symbol;
		case 1: return account;
		case 2: return signal;
		case 3: return price;
		case 4: return avgerage;
		case 5: return position;
		case 6: return PL;
		case 7: return traget;
		case 8: return stop;
		
		}
		 return null;
	}
}
