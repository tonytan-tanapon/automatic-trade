package apitest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.Types.Right;
import com.ib.client.Types.SecType;
import com.ib.controller.ApiController.IContractDetailsHandler;

import autotrade.ContractATS;

public class A_OptionChainImp  extends A_OptionChain implements IContractDetailsHandler{
	HashMap<Double, Contract> callData ;
	HashMap<Double, Contract> putData;
	ArrayList<Double> strikeList ;
	List<ContractDetails> data;
	
	Contract contract = new Contract();

	
	double callATM = 0.0; 
	double putATM = 0.0;
	
	
	public A_OptionChainImp(A_Mediator med, String name) {
		super(med, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void send(String msg, Object data) {
		// TODO Auto-generated method stub
		System.out.println(this.name+": Sending Message="+msg);
		mediator.sendMessage(msg, this);
	}

	@Override
	public void receive(String msg) {
		// TODO Auto-generated method stub
		System.out.println(this.name+": Received Message:"+msg.toString());
		HashMap<String, String> map = new HashMap<String, String>();
		map = A_MSG.toHashMap(msg.toString());
		
		String symbol =map.get("symbol");
		String date = map.get("date");
		
//		System.out.println(symbol+" "+date);
		// call option chain
		ApiDemo.INSTANCE.controller().reqContractDetails(getOptionContract(symbol,date), this);
						
		
	}
	
	
	@Override
	public void reqOptionChain() {
		// TODO Auto-generated method stub
		
		
		
	}
	public Contract getOptionContract(String symbol, String date) {
		
		contract.symbol(symbol.toUpperCase() ); //
		contract.currency("USD".toUpperCase() );
		contract.secType(SecType.OPT);
		contract.exchange("SMART".toUpperCase() );
		contract.lastTradeDateOrContractMonth(date); // 
		return contract;
		
	}
	

	
	@Override
	public void contractDetails(List<ContractDetails> list) {
		// TODO Auto-generated method stub
//		System.out.println("    >>>> LIST >>>"+ list);
		
		callData = new HashMap();
		putData = new HashMap();
		data = list;
//		data.add(list);
		strikeList  = new ArrayList<Double>();
		// TODO Auto-generated method stub

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
		
//		System.out.println("strikeList>>>: "+strikeList);
		send("option ok", this);

	}
	
	@Override
	public Object getCallData() {
		// TODO Auto-generated method stub
		return callData;
	}

	@Override
	public Object getPutData() {
		// TODO Auto-generated method stub
		return putData;
	}


	@Override
	public Object strikeList() {
		// TODO Auto-generated method stub
		return strikeList;
	}
	

}


