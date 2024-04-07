package apitest;

import com.ib.client.IApiEnum;

public enum MyStatus {
	None, BuyCall, SellCall, BuyPut, SellPut;

}

//
//public enum Right implements IApiEnum {
//	None, Put, Call;
//
//	public static Right get( String apiString) {
//		if (apiString != null && apiString.length() > 0) {
//			switch( apiString.charAt( 0) ) {
//				case 'P' : return Put;
//				case 'C' : return Call;
//			}
//		}
//		return None;
//	}
//
//	@Override public String getApiString() {
//		return this == None ? "" : String.valueOf( toString().charAt( 0) );
//	}
//}