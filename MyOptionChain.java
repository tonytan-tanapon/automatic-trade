package apitest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.Types.Right;
import com.ib.client.Types.SecType;

public class MyOptionChain {
	List<ContractDetails> list;
	HashMap<Double, Contract> callData ;
	HashMap<Double, Contract> putData;
	double callATM = 0.0; 
	double putATM = 0.0;
//	double stockPrice;
	public void setOptionChain(List<ContractDetails> list, double stockPrice) {
		
		System.out.println("SET option chain");
		this.list = list;
		
		callData = new HashMap();
		putData = new HashMap();
		
		ArrayList<Double> strikeList  = new ArrayList<Double>();
		// TODO Auto-generated method stub
//		System.out.println("size = "+list.size());
		for (ContractDetails data : list) {
			Contract contract = data.contract();
			if (contract.right() == Right.Put) {
				putData.put(contract.strike(), contract);
				strikeList.add(contract.strike());
			}
			else { 
				callData.put(contract.strike(), contract);
			}
		}
		Collections.sort(strikeList);   

//		System.out.println(strikeList);
		callATM = getATM(stockPrice,strikeList,Right.Call);
		putATM = getATM(stockPrice,strikeList,Right.Put);
		System.out.println(stockPrice+ " call ATM " +callATM);
		System.out.println(stockPrice+ " put ATM "  +putATM);
		
//		inputOptionCall.setText(""+callATM);
//		inputOptionPut.setText(""+putATM);
//		inputlastTradeDate.setText(lastTradeDate);
		
	}
//	public 
	public double getATM(double stockPrice, ArrayList<Double> strikeList, Right right){
		System.out.println("GET ATM chain !!!!!!!!!!!");
		for(int i=0;i< strikeList.size();i++ ) {
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
	
	public Contract getCallATMContract() {
		return callData.get(callATM);
	}
	
	public Contract getPutATMContract() {
		return callData.get(putATM);
	}
	
}
