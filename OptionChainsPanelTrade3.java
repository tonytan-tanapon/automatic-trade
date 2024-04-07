/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package apitest;

import static com.ib.controller.Formats.fmtNz;
import static com.ib.controller.Formats.fmtPct;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.Decimal;
import com.ib.client.HistoricalTick;
import com.ib.client.HistoricalTickBidAsk;
import com.ib.client.HistoricalTickLast;
import com.ib.client.MarketDataType;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.ib.client.TickAttrib;
import com.ib.client.TickAttribBidAsk;
import com.ib.client.TickAttribLast;
import com.ib.client.TickType;
import com.ib.client.Types.BarSize;
import com.ib.client.Types.DurationUnit;
import com.ib.client.Types.Right;
import com.ib.client.Types.SecType;
import com.ib.client.Types.WhatToShow;
import com.ib.controller.ApiController.IAccountHandler;
import com.ib.controller.ApiController.IAccountSummaryHandler;
import com.ib.controller.ApiController.IContractDetailsHandler;
import com.ib.controller.ApiController.IHistoricalDataHandler;
import com.ib.controller.ApiController.ILiveOrderHandler;
import com.ib.controller.ApiController.IOptHandler;
import com.ib.controller.ApiController.IPositionHandler;
import com.ib.controller.ApiController.IRealTimeBarHandler;
import com.ib.controller.ApiController.ITickByTickDataHandler;
import com.ib.controller.ApiController.TopMktDataAdapter;
import com.ib.controller.AccountSummaryTag;
import com.ib.controller.Bar;
import com.ib.controller.Position;

import apidemo.util.HtmlButton;
import apidemo.util.NewTabbedPanel;
import apidemo.util.NewTabbedPanel.NewTabPanel;
import apidemo.util.TCombo;
import apidemo.util.UpperField;
import apidemo.util.Util;
import apidemo.util.VerticalPanel;
import autotrade.ContractATS;

class OptionChainsPanelTrade3 extends JPanel implements IContractDetailsHandler, IPositionHandler, IRealTimeBarHandler, 
IHistoricalDataHandler, IAccountSummaryHandler, ILiveOrderHandler, ITickByTickDataHandler{
	InputText inputSignal = new InputText("Signal","0.0");
	InputText inputPos = new InputText("PosN","0.0");
	InputText inputLivePos = new InputText("Live pos","0.0");
//	InputText inputQty = new InputText("Qty","20000.0");
	InputText inputQty = new InputText("Qty","10.0");
	
	InputText inputBuyingPower = new InputText("Buy Power","0.0");
	InputText inputNetLiquidation = new InputText("NetLiquidation","0.0");
	
	Contract stockContract = new Contract();
	Contract optionContract = new Contract();
	String currentDateTime = "";
	double currentPosition = 0; 
	Bar lastBar; 
	
	MyOption myOption = new MyOption("AAPL");
	
//	boolean init = true;
	
	
	double stockPrice = 173.82;
	double stockBid = 173.82;
	double stockAsk = 173.82;
	double callATM = 0.0; 
	double putATM = 0.0;
	String lastTradeDate = "20240328";
	
	double buyingPower = 0.0; 
	double netLiquidation = 0.0;
	String symbol = "AAPL";
	
	InputText inputSymbol = new InputText("Symbol", symbol); 
	InputText inputPrice = new InputText("Price", "0"); 
	InputText inputOptionCall = new InputText("Call ATM","0");
	InputText inputOptionPut = new InputText("Put ATM","0");
	InputText inputContract = new InputText("Current contract", "Contract");
	InputText inputlastTradeDate = new InputText("Last Trade Date", "DDMMYYY", 10);
	
	InputText inputLiveContract = new InputText("Live Contract", "Live contract",20);
	InputText inputLiveOrder = new InputText("Live Order", "Live order");
	
	double position = 0.0; 
	double buyingPoswer = 0.0;
	double netLiq = 0.0; 
	
	int signal = 0;
	Contract optContractCall;
	Contract optContractPut;
	Contract optContract = new Contract();
	HashMap<Double, Contract> callData ;
	HashMap<Double, Contract> putData;
	Contract liveContract ;
	OptionChainsPanelTrade3(){
		
		setLayout(new GridLayout(0, 3));
		JPanel column1 = new JPanel(new GridLayout(10, 1));
		column1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JButton btnInit = new JButton("Init");
		JButton btnGo = new JButton("Go");
		JButton btnStop = new JButton("Stop");
		
		
		column1.add(btnInit);
		column1.add(btnGo);
		column1.add(btnStop);
		
		this.add(column1);
		
		JPanel column2 = new JPanel(new GridLayout(10, 1));
		column2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		column2.add(inputSymbol);
		column2.add(inputPrice);
		column2.add(inputOptionCall);
		column2.add(inputOptionPut);
		column2.add(inputlastTradeDate);
		
		column2.add(inputBuyingPower);
		column2.add(inputNetLiquidation);
		this.add(column2);
		
		
		JPanel column3 = new JPanel(new GridLayout(10, 1));
		column3.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		column3.add(inputContract);
		column3.add(inputSignal);
		column3.add(inputPos);
		column3.add(inputLivePos);
		column3.add(inputQty);
		
		column3.add(inputLiveContract);
		column3.add(inputLiveOrder);
		
		this.add(column3);
		
		btnInit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				initFunction();
			}
		});
		btnGo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				goFunction();
			}
		});
		
		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				stopFunction();
			}
		});
	}
	
	public void initFunction() {
		initStockContract();
		processAccountSummary();

//		processPosition();
//		 request Live order 

//		
		
		// request option chains
//		initOptionContract()
//		ApiDemo.INSTANCE.controller().reqContractDetails(optionContract, this);
//		


	}
	public void initStockContract() {
		this.symbol = inputSymbol.getText();
		stockContract.symbol(this.symbol.toUpperCase() );
		stockContract.currency("USD".toUpperCase() );
		stockContract.secType(SecType.STK);
		stockContract.exchange("SMART".toUpperCase() );
		
		
		myOption.setOptionContract(stockContract);
//		stockContract = ContractATS.getContractFX("EUR/USD");
	}
	
	public void initOptionContract() {
		this.symbol = inputSymbol.getText();
		optionContract.symbol(this.symbol.toUpperCase() );
		optionContract.currency("USD".toUpperCase() );
		optionContract.secType(SecType.OPT);
		optionContract.exchange("SMART".toUpperCase() );
		optionContract.lastTradeDateOrContractMonth(getFridayOption());
		optionContract.lastTradeDateOrContractMonth("20240328"); // Thursday 
		
		myOption.setOptionContract(stockContract);

	}
	public void  goFunction(){
		System.out.println("Start trade...");
		
		initStockContract();
		processRealtime(stockContract);

	}

	public void stopFunction() {
		ApiDemo.INSTANCE.controller().cancelRealtimeBars(this);
		System.out.println("Stop API");
		
	}
	public  String getFridayOption() {
		LocalDate dt = LocalDate.now();   
		// next or previous
		String Friday = dt.with(TemporalAdjusters.next(DayOfWeek.FRIDAY)).toString();
		String daySplit[] = Friday.split("-");
		Friday = daySplit[0]+daySplit[1]+daySplit[2];
		return Friday;
	}
	
	public void processRealtime( Contract contract) {
		
		if (contract.secType().equals(SecType.STK)){
			/// stock
			ApiDemo.INSTANCE.controller().reqRealTimeBars(contract,WhatToShow.TRADES,false,this);
			
		}
		else if (contract.secType().equals(SecType.CASH)){
			/// CASH
			System.out.println("CASH");
			ApiDemo.INSTANCE.controller().reqRealTimeBars(contract,WhatToShow.MIDPOINT,false,this);
		}
		
	}
	public void processAccountSummary() {
		ApiDemo.INSTANCE.controller().reqAccountSummary("All", AccountSummaryTag.values(), this);
		
	}
	public void processLiveOrde(){
		ApiDemo.INSTANCE.controller().reqLiveOrders(this);
	}
	public void processHistorical(Contract contract) {
		// historical bar 
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
		String endDateTime = form.format(cal.getTime());
//		endDateTime = "20240314-13:45:00";
//		String endDateTime = form.format("20240314-13:45:00");
		int duration = 1;
		DurationUnit durationUnit = DurationUnit.DAY;
		BarSize barSize = BarSize._15_mins;
		WhatToShow whatToShow = WhatToShow.MIDPOINT;
		ApiDemo.INSTANCE.controller().reqHistoricalData(contract, endDateTime, duration, durationUnit, barSize,
				whatToShow, false, false, this);
	}
	
	public void  processOption() {

		if (stockContract.secType().equals(SecType.STK)){
			initOptionContract();
			
			ApiDemo.INSTANCE.controller().reqContractDetails(optionContract, this);
			
		}
		else { // CASH
			processPosition();
		}
			
	
	}
	public void processPosition(){
		System.out.println("Processing position..");
		
		ApiDemo.INSTANCE.controller().reqPositions(this);
	}
	
	public void checkOpenClose() 
	{
		double sig = Double.parseDouble(inputSignal.getText());
		System.out.println(" == = == = = \n");
		
		optContract = callData.get(callATM);
	
	
	
		OrderSend send = new OrderSend(optContract);
		String data = "Stock";
		
		double posN = Double.parseDouble(inputPos.getText());
		double liveposition =  Double.parseDouble(inputLivePos.getText());
		double qty = Double.parseDouble(inputQty.getText());
		System.out.println("Check data = " + data+" signal = "+sig+" posN = " + posN +	
				" liveposition = "+liveposition+ " qty"+qty+"\n");
		
		send.createOrderOption(data, sig, posN, liveposition, qty);

	}
	public void checkOpenCloseBack() 
	{
		double sig = Double.parseDouble(inputSignal.getText());
		System.out.println(" == = == = = \n");
		
		optContract = callData.get(callATM);
	
	
	
		OrderSend send = new OrderSend(optContract);
		String data = "Stock";
		
		double posN = Double.parseDouble(inputPos.getText());
		double liveposition =  Double.parseDouble(inputLivePos.getText());
		double qty = Double.parseDouble(inputQty.getText());
		System.out.println("Check data = " + data+" signal = "+sig+" posN = " + posN +	
				" liveposition = "+liveposition+ " qty"+qty+"\n");
		
		send.createOrderOption(data, sig, posN, liveposition, qty);
	}
	public double getATM(double stockPrice, ArrayList<Double> strikeList, Right right){

		for(int i=0;i< stockPrice;i++ ) {
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
	
	///////////////////////////// IContractDetailsHandler
	@Override
	public void contractDetails(List<ContractDetails> list) {
//		System.out.println(list);
//		HashMap<Double, Contract> callData = new HashMap();
//		HashMap<Double, Contract> putData = new HashMap();
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
		
		inputOptionCall.setText(""+callATM);
		inputOptionPut.setText(""+putATM);
		inputlastTradeDate.setText(lastTradeDate);

		
//		System.out.println("=========================");

		processPosition();
//
		// Call reqOptionMktData
//		Contract optContractCall = callData.get(callATM);
//		OptionMkt callMkt = new OptionMkt(optContractCall);
//		ApiDemo.INSTANCE.controller().reqOptionMktData(optContractCall, "", false, false, callMkt);
//	
//		Contract optContractPut = putData.get(putATM);
//		OptionMkt putMkt = new OptionMkt(optContractPut);
//		ApiDemo.INSTANCE.controller().reqOptionMktData(optContractPut, "", false, false, putMkt);
//		
	}

	
///////////////////////////// IOption Handler
	class OptionMkt implements IOptHandler{
		Right right;
		String localSymbol = "";
		OptionMkt(Contract contract){
			this.right = contract.right();
			this.localSymbol = contract.localSymbol();
			
		}
	@Override
	public void tickPrice(TickType tickType, double price, TickAttrib attribs) {
		// TODO Auto-generated method stub
		System.out.println(stockPrice);
		if( tickType.equals(TickType.BID) ||tickType.equals(TickType.ASK) )
		System.out.println("tickType "+tickType+" right "+right.toString() +" localSymbol "+ localSymbol.toString()+  ": "+price);
	}

	@Override
	public void tickSize(TickType tickType, Decimal size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickString(TickType tickType, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickSnapshotEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void marketDataType(int marketDataType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickReqParams(int tickerId, double minTick, String bboExchange, int snapshotPermissions) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void tickOptionComputation(TickType tickType, int tickAttrib, double impliedVol, double delta,
			double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice) {
		// TODO Auto-generated method stub
		
	}

	}
	// IRealTimeBar 
		@Override
		public void realtimeBar(Bar bar) {
			// first bar Bar20240312 20:30:00 Asia/Bangkok 173.15 173.46 171.01 171.4
			System.out.println("bar:" +bar);
			
			// GO to Historical
			currentDateTime = bar.formattedTime();
			
			inputPrice.setText(""+bar.close());
			
			processAccountSummary();
			
			
		}
		
		/// IAccountSummary
		@Override
		public void accountSummary(String account, AccountSummaryTag tag, String value, String currency) {
			// TODO Auto-generated method stub
			if (tag.equals(AccountSummaryTag.BuyingPower) ){
				
				buyingPower = Double.parseDouble(value);
				System.out.println("   tag "+ tag +" value "+ value);
				
				inputBuyingPower.setText(value);
			}
			if (tag.equals(AccountSummaryTag.NetLiquidation)){
				
				netLiquidation = Double.parseDouble(value);
				System.out.println("   tag "+ tag +" value "+ value);
				
				inputNetLiquidation.setText(value);
			}
			
			
			
		}
		@Override
		public void accountSummaryEnd() {
			// TODO Auto-generated method stub
			System.out.println("accountSummaryEnd\n");
			ApiDemo.INSTANCE.controller().cancelAccountSummary(this);
			processLiveOrde();
			
		}
		//// IHistorical
		@Override
		public void historicalData(Bar bar) {
			// TODO Auto-generated method stub
			
//			System.out.println("Hist Bar" +bar);
			lastBar = bar;
			
			stockPrice = bar.close();
		}
		@Override
		public void historicalDataEnd() {
			// TODO Auto-generated method stub
			System.out.println("historicalDataEnd");
			
//			ApiDemo.INSTANCE.controller().cancelHistoricalData(this);
	
			System.out.println("last bar = "+lastBar);
			inputPrice.setText(""+lastBar.close());
			//// process signal 
			/// if sig 1 -> call 
			/// if sig -1 -> put 
			
			processOption();
//			processPosition();
			
		}
	/////////////// IPosition Handler
	// auto when buy or sell 

	@Override
	public void position(String account, Contract contract, Decimal pos, double avgCost) {
		// TODO Auto-generated method stub
		System.out.println(contract.localSymbol() +" pos  "+ pos+ " avgCost "+avgCost);
		this.currentPosition = Double.parseDouble(""+pos);
		inputPos.setText(""+pos);
	}
	@Override
	public void positionEnd() {
		// TODO Auto-generated method stub
		System.out.println("positionEnd");
		ApiDemo.INSTANCE.controller().cancelPositions(this);
		
//		check();
//		if(this.init) {
//			this.init = false;
//		}else {
//			checkOpenClose();
//		}
		checkOpenClose();
		// live order 
//		ApiDemo.INSTANCE.controller().reqLiveOrders( this);
	}
	
	
	

	/// reqLiveorder 
	@Override
	public void openOrder(Contract contract, Order order, OrderState orderState) {
		// TODO Auto-generated method stub
		System.out.println(contract);

		System.out.println("contract "+contract.symbol()+ " "+ contract.getRight()+
				" order "+ order.auxPrice()+" orderState " +orderState.getStatus());
		
		System.out.println(order.transmit());
		
		inputLiveContract.setText(""+contract);
//		if(order.notHeld())
//		this.liveContract = contract;
	}
	@Override
	public void openOrderEnd() {
		// TODO Auto-generated method stub
		System.out.println("openOrderEnd");
		
		
		processHistorical(stockContract);
	}
	@Override
	public void orderStatus(int orderId, OrderStatus status, Decimal filled, Decimal remaining, double avgFillPrice,
			int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
		// TODO Auto-generated method stub
		System.out.println(status+" "+filled+" "+remaining+" "+avgFillPrice);
	}
	@Override
	public void handle(int orderId, int errorCode, String errorMsg) {
		// TODO Auto-generated method stub
		
	}
	
	
	//ITtickbyTick
	@Override
	public void tickByTickAllLast(int reqId, int tickType, long time, double price, Decimal size,
			TickAttribLast tickAttribLast, String exchange, String specialConditions) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void tickByTickBidAsk(int reqId, long time, double bidPrice, double askPrice, Decimal bidSize,
			Decimal askSize, TickAttribBidAsk tickAttribBidAsk) {
		// TODO Auto-generated method stub
		
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
	
	
	

}


