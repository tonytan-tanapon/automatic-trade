/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package apitest;

import static com.ib.controller.Formats.fmtNz;
import static com.ib.controller.Formats.fmtPct;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
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

class OptionChainsPanelTrade4 extends JPanel implements IContractDetailsHandler, IPositionHandler, IRealTimeBarHandler, 
IHistoricalDataHandler, IAccountSummaryHandler, ILiveOrderHandler, ITickByTickDataHandler{
	JButton btnBuy = new JButton("Buy");
	JButton btnSell = new JButton("Sell");
	JButton btnWait = new JButton("Wait");
	InputText inputSignal = new InputText("Signal","0.0");
	InputText inputPos = new InputText("PosN","0.0");
	InputText inputLivePos = new InputText("Live pos","0.0");
//	InputText inputQty = new InputText("Qty","20000.0");
	InputText inputQty = new InputText("Qty","10.0");
	
	InputText inputBuyingPower = new InputText("Buy Power","0.0");
	InputText inputNetLiquidation = new InputText("NetLiquidation","0.0");
	
	String currentDateTime = "";
	
	DayOfWeek dayOfWeek = DayOfWeek.THURSDAY;

	
	double buyingPower = 0.0; 
	double netLiquidation = 0.0;
	String symbol = "AAPL";
	MyOption myOption = new MyOption(symbol,dayOfWeek);
	
	InputText inputSymbol = new InputText("Symbol", symbol); 
	InputText inputLivePrice = new InputText("Price", "0"); 
	InputText inputHistPrice = new InputText("Price", "0"); 
	InputText inputOptionCall = new InputText("Call ATM","0");
	InputText inputOptionPut = new InputText("Put ATM","0");
	InputText inputContract = new InputText("Current contract", "Contract");
	InputText inputlastTradeDate = new InputText("Last Trade Date", "DDMMYYY", 10);
	
	InputText inputLiveContract = new InputText("Live Contract", "Live contract",20);
	InputText inputLiveOrder = new InputText("Live Order", "Live order");
	
	JTable tableHistorical ;
	JTable tablePosition ;
	
	JScrollPane scrolHistorical;
	JComboBox listDay;
	OptionChainsPanelTrade4(){
		
		tablePosition = new  JTable(myOption.position.tableModel);
		tableHistorical = new JTable(myOption.historical.tableModel);
		
		
		tableHistorical.setPreferredScrollableViewportSize(new Dimension(450,200));
		tableHistorical.setFillsViewportHeight(true);

		
		this.setLayout(new GridLayout(2, 3));
		JPanel column1 = new JPanel(new GridLayout(7, 1));
		column1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JButton btnInit = new JButton("Init");
		JButton btnGo = new JButton("Go");
		JButton btnStop = new JButton("Stop");
		
		JPanel panelBTN = new JPanel();
		panelBTN.add(btnInit);
		panelBTN.add(btnGo);
		panelBTN.add(btnStop);
		column1.add(panelBTN);
		
		JPanel panelStockDetail = new JPanel();
		panelStockDetail.add(inputSymbol);
		panelStockDetail.add(inputLivePrice);
		panelStockDetail.add(inputOptionCall);
		panelStockDetail.add(inputOptionPut);
		column1.add(panelStockDetail);

		
		JPanel panelDate = new JPanel(); 
		
		String s1[] = { "Thu", "Fri" };
		 
        // create checkbox
		listDay = new JComboBox(s1);
		listDay.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub

				if(e.getStateChange() == 1) {
					if(e.getItem().equals("Thu")) {
						myOption.dayOfWeek = DayOfWeek.THURSDAY;
					}
					if(e.getItem().equals("Fri")) {
						myOption.dayOfWeek = DayOfWeek.FRIDAY;
					}
				}

			}
		});
	
        // add ItemListener
		
		panelDate.add(inputlastTradeDate);
		panelDate.add(listDay);
		column1.add(panelDate);
		

		JPanel panelLiquiataion = new JPanel();
		panelLiquiataion.add(inputBuyingPower);
		panelLiquiataion.add(inputNetLiquidation);
		column1.add(panelLiquiataion);

		
		
		JPanel panelSig = new JPanel();
		panelSig.add(btnBuy);
		panelSig.add(btnSell);
		panelSig.add(btnWait);
		panelSig.add(inputSignal);
		
		column1.add(panelSig);
		this.add(column1);
		
		JPanel column2 = new JPanel();
		scrolHistorical = new JScrollPane(tableHistorical);
		JScrollBar bar = scrolHistorical.getVerticalScrollBar();
		bar.setPreferredSize(new Dimension(40, 0));
		column2.add(scrolHistorical);
		this.add(column2);
		
		JPanel column3 = new JPanel(new GridLayout(10, 1));
		column3.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		column3.add(inputContract);
		
		column3.add(inputPos);
		column3.add(inputLivePos);
		column3.add(inputQty);
		
		 
		column3.add(inputLiveContract);
		column3.add(inputLiveOrder);
		
		this.add(column3);
		
		JPanel column4 = new JPanel();
		JScrollPane scrollPosition = new JScrollPane(tablePosition);
		column4.add(scrollPosition);
		this.add(column4);
		
		
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
		
		btnBuy.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				inputSignal.setText("1.0");
			}
		});
		btnSell.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				inputSignal.setText("-1.0");
			}
		});
		btnWait.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				inputSignal.setText("0.0");
			}
		});
	}
	
	public void initFunction() {

		this.myOption.symbol = inputSymbol.getText();
		
		this.myOption.initStockContract();
	
		processAccountSummary();

//		processPosition();
//		 request Live order 

//		
		
		// request option chains
//		initOptionContract()
//		ApiDemo.INSTANCE.controller().reqContractDetails(optionContract, this);
//		


	}
	
	

	public void  goFunction(){
		System.out.println("Start trade...");
		this.myOption.symbol = inputSymbol.getText();
		this.myOption.initStockContract();
		
//		processAutoTrade();
		processRealtime(this.myOption.stockContract);

	}

	public void stopFunction() {
		ApiDemo.INSTANCE.controller().cancelRealtimeBars(this);
		System.out.println("Stop API");
		
	}
	public void processAutoTrade() {
		processPosition();
		
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
	public void processLiveOrder(){
		ApiDemo.INSTANCE.controller().reqLiveOrders(this);
	}
	public void processHistorical(Contract contract) {
		// historical bar 

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");

		///time_zone US/Eastern
		cal.add(Calendar.HOUR, 4);
		String endDateTime = form.format(cal.getTime());
		

		Date currentTimePlusOneMinute = cal.getTime();
//		String endDateTime = form.format("20240314-13:45:00");
		int duration = 1;
		DurationUnit durationUnit = DurationUnit.DAY;
		BarSize barSize = BarSize._1_min;
//		BarSize barSize = BarSize._15_mins;
		WhatToShow whatToShow = WhatToShow.TRADES;
		
		this.myOption.historical.resetTable();
		
		System.out.println(" >>20240314-13:45:00>>>>>>>>>>>>> "+endDateTime);
		ApiDemo.INSTANCE.controller().reqHistoricalData(contract, endDateTime, duration, durationUnit, barSize,
				whatToShow, false, false, this);
	}
	
	public void  processOption() {

		if (this.myOption.stockContract.secType().equals(SecType.STK)){
			this.myOption.initOptionContract();
			
			ApiDemo.INSTANCE.controller().reqContractDetails(this.myOption.optionContract, this);
			
		}
		else { // CASH
			processPosition();
		}
			
	
	}
	public void processPosition(){
		System.out.println("Processing position..");
		myOption.position.resetTable();
		ApiDemo.INSTANCE.controller().reqPositions(this);
	}
	
	public void processOrder() 
	{
	
		this.myOption.sendOrder(Double.parseDouble(inputSignal.getText()));
		
	}
	
	///////////////////////////// IContractDetailsHandler
	@Override
	public void contractDetails(List<ContractDetails> list) {
		
		double stockPrice = this.myOption.historical.getClose();
		this.myOption.optionchain.setOptionChain(list, stockPrice);

		inputLivePrice.setText(""+stockPrice);
		inputOptionCall.setText(""+this.myOption.optionchain.callATM);
		inputOptionPut.setText(""+this.myOption.optionchain.putATM);
		inputlastTradeDate.setText(this.myOption.dayOfWeek.toString());

	
		processPosition();
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
			
			inputLivePrice.setText(""+bar.close());
			
			processAccountSummary();
			
			
		}
		
		/// IAccountSummary
		@Override
		public void accountSummary(String account, AccountSummaryTag tag, String value, String currency) {
			// TODO Auto-generated method stub
			myOption.setAccount(account, tag, value, currency);

		}
		@Override
		public void accountSummaryEnd() {
			// TODO Auto-generated method stub
			System.out.println("accountSummaryEnd\n");
			
			inputBuyingPower.setText(myOption.getAccountValue(AccountSummaryTag.BuyingPower));
			inputNetLiquidation.setText(myOption.getAccountValue(AccountSummaryTag.NetLiquidation));
			
			ApiDemo.INSTANCE.controller().cancelAccountSummary(this);
			processLiveOrder();
			
		}
		//// IHistorical
		@Override
		public void historicalData(Bar bar) {
			// TODO Auto-generated method stub
			this.myOption.historical.setBar(bar);
			
		
		}
		@Override
		public void historicalDataEnd() {
			// TODO Auto-generated method stub
			System.out.println("historicalDataEnd");
//			this.myOption.historical.getLastRow();
			
			inputHistPrice.setText(""+this.myOption.historical.getClose());
			
			
			//// process signal 
			/// if sig 1 -> call 
			/// if sig -1 -> put 
			scrolHistorical.getVerticalScrollBar().setValue(scrolHistorical.getVerticalScrollBar().getMaximum());
			processOption();
//			processPosition();
			
		}
	/////////////// IPosition Handler
	// auto when buy or sell 

	@Override
	public void position(String account, Contract contract, Decimal pos, double avgCost) {
		// TODO Auto-generated method stub
		this.myOption.position.setPosition(account, contract, pos, avgCost);
		
		System.out.println(contract.localSymbol() +" pos  "+ pos+ " avgCost "+avgCost);
	
		inputPos.setText(""+this.myOption.position.getPosition());
	}
	@Override
	public void positionEnd() {
		// TODO Auto-generated method stub
		System.out.println("positionEnd :"+ this.myOption.position.size());
		ApiDemo.INSTANCE.controller().cancelPositions(this);
		

		processOrder();
		// live order 
//		ApiDemo.INSTANCE.controller().reqLiveOrders( this);
	}
	
	
	

	/// reqLiveorder 
	////openOrder  ---> orderStatus ----> openOrderEnd
	@Override
	public void openOrder(Contract contract, Order order, OrderState orderState) {
		// TODO Auto-generated method stub
		this.myOption.liveorder.setLiveOrder(contract, order, orderState);
		
	}
	
	@Override
	public void orderStatus(int orderId, OrderStatus status, Decimal filled, Decimal remaining, double avgFillPrice,
			int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
		// TODO Auto-generated method stub
		this.myOption.liveorder.setLiveOrder(orderId, status, filled, remaining, avgFillPrice, permId, parentId, lastFillPrice, clientId, whyHeld, mktCapPrice);
		inputLiveContract.setText("# live order: "+this.myOption.liveorder.getNumberLiveOrder());
	}
	@Override
	public void openOrderEnd() {
		// TODO Auto-generated method stub
		System.out.println("openOrderEnd");
		
		inputLiveContract.setText("# live order: "+this.myOption.liveorder.getNumberLiveOrder());
		processHistorical(this.myOption.stockContract);
	}
	@Override
	public void handle(int orderId, int errorCode, String errorMsg) {
		// TODO Auto-generated method stub
		System.out.println("44444444444444444444444444");
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


