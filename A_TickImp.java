package apitest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.HistoricalTick;
import com.ib.client.HistoricalTickBidAsk;
import com.ib.client.HistoricalTickLast;
import com.ib.client.TickAttribBidAsk;
import com.ib.client.TickAttribLast;
import com.ib.controller.ApiController.ITickByTickDataHandler;

import autotrade.ContractATS;

public class A_TickImp extends A_Tick implements ITickByTickDataHandler {
//	HashMap<Long, List<Object>> data = new HashMap<Long, List<Object>>();
	HashMap<String,String> data = new HashMap<String, String>();
	double bid = 0.0;
	double ask = 0.0;
	
	String symbol = "AAPL";
//	ArrayList<Double> price = new ArrayList<Double>();
	public A_TickImp(A_Mediator med, String name) {
		super(med, name);
	}
	
	@Override
	public void send(String msg, A_Tick tick) {
		// TODO Auto-generated method stub
//		System.out.println(this.name+": Sending Message="+msg);
		mediator.sendMessage( "tick", this);
	}

	@Override
	public void receive(String msg) {
		// TODO Auto-generated method stub
		if(msg.equals(A_MSG.Stop)) {
			System.out.println("Stop Tick");
			ApiDemo.INSTANCE.controller().cancelTickByTickData(this);
		}
		else {
//			System.out.println(this.name+": Received Message:"+msg);
			HashMap<String,String> cmd = A_MSG.toHashMap(msg);
			
			if(cmd.get("cmd").equals(A_MSG.Tick)) {
				this.symbol=cmd.get("symbol");
				reqTick(createStockContrat(this.symbol));
			}
		}
	}
	public Contract  createStockContrat(String Symbol) {
		Contract contract = ContractATS.getContractStock(Symbol);
		return contract;
	}
	
	@Override
	public void reqTick(Contract contract) {
		
		// TODO Auto-generated method stub
		data.clear();
		ApiDemo.INSTANCE.controller().reqTickByTickData(contract, "BidAsk", 0, false, this);
		
	}
	
	@Override
	public void tickByTickAllLast(int reqId, int tickType, long time, double price, Decimal size,
			TickAttribLast tickAttribLast, String exchange, String specialConditions) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void tickByTickBidAsk(int reqId, long time, double bidPrice, double askPrice, Decimal bidSize,
			Decimal askSize, TickAttribBidAsk tickAttribBidAsk) {
		// TODO Auto-generated method stub
//		System.out.println("bidPrice " +bidPrice);
		
		ArrayList<Object> a = new ArrayList<Object>();
		a.add(bidSize);
		a.add(askSize);
//		data.put(time, a);
		bid = bidPrice;
		ask = askPrice;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
		String datetime = form.format(cal.getTime());
		data.put("time", datetime);
		data.put("bid",""+bidPrice);
		data.put("ask",""+askPrice);
		send("tick",this );
	}
	@Override
	public void tickByTickMidPoint(int reqId, long time, double midPoint) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void tickByTickHistoricalTickAllLast(int reqId, List<HistoricalTickLast> ticks) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void tickByTickHistoricalTickBidAsk(int reqId, List<HistoricalTickBidAsk> ticks) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void tickByTickHistoricalTick(int reqId, List<HistoricalTick> ticks) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public HashMap<String,String>  getData() {
		// TODO Auto-generated method stub
		return data;
	}
	


}
