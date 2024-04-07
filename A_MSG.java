package apitest;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;

import com.ib.client.Types.Right;

public class A_MSG {
	final static String None="None";
	final static String Start="Start";
	final static String Stop= "Stop";
	final static String Tick = "Tick";
	final static String Historical = "Historical";
	final static String Posotion = "Positon";
	final static String Account = "Account";
	final static String Init = "Init";
	final static String Req="Req";
	private static A_Position position;
	private static A_Tick tick;
	private static A_Gui gui;

	public static HashMap<String, String> toHashMap(String s) {
		HashMap<String, String> myMap = new HashMap<String, String>();
		s= s.replace("{", "");
		s= s.replace("}", "");
		String[] pairs = s.split(",");
		for (int i=0;i<pairs.length;i++) {
		    String pair = pairs[i];
		    String[] keyValue = pair.split("=");
		    myMap.put(keyValue[0].strip(), keyValue[1]);

		}
		return  myMap;
	}
	
	public static double getATM(double stockPrice, ArrayList<Double> strikeList, Right right){
//		System.out.println("GET ATM chain !!!!!!!!!!!" +strikeList.size());
		
		for(int i=0;i< strikeList.size();i++ ) {
//			System.out.println(stockPrice+" "+strikeList.get(i));
			if (stockPrice == strikeList.get(i)) {
				return(strikeList.get(i));
			}
			if(right == Right.Call) {
				if (stockPrice < strikeList.get(i)) {
					return strikeList.get(i-1);
				}
			}
			else {
				if (stockPrice < strikeList.get(i)) {
					return strikeList.get(i);
				}
			}
		}
		return 0.0;	
	}
	
	

}
//public enum A_MSG {
//	None, 
//	Start,
//	Stop, 
//	Tick, 
//	Historical, 
//	Posotion,
//	Account,
//	Init,
//
//}
