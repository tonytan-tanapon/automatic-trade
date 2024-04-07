/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package apitest;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.ib.client.Contract;
import com.ib.client.Types.BarSize;
import com.ib.client.Types.DurationUnit;
import com.ib.client.Types.SecType;
import com.ib.client.Types.WhatToShow;
import com.ib.controller.ApiController.IHistoricalDataHandler;
import com.ib.controller.ApiController.IPositionHandler;
import com.ib.controller.ApiController.IRealTimeBarHandler;
import com.ib.controller.Bar;

import apidemo.ApiDemo;
import apidemo.util.HtmlButton;
import apidemo.util.NewTabbedPanel;
import apidemo.util.TCombo;
import apidemo.util.UpperField;
import apidemo.util.VerticalPanel;
import apidemo.util.NewTabbedPanel.INewTab;
import apidemo.util.VerticalPanel.BorderPanel;
import apidemo.util.VerticalPanel.HorzPanel;
import apidemo.util.VerticalPanel.StackPanel;
import apitest.PositionsPanel.PositionKey;
import apitest.PositionsPanel.PositionRow;
import autotrade.API;

import autotrade.Position;
import autotrade.PositionATS;
import autotrade.Series;
/// AutoTradePanel ===> RealTimeModel ==> HistoricalModel ====> StrategyModel 
/// ===> PositonModel ===> AccountSummaryModel ===> SendOrder
public class AutoTradePanel extends StackPanel implements INewTab {
	
	public  Contract m_contract = new Contract();
	public  ContractPanel m_contractPanel = new ContractPanel(m_contract);
	public BarDetailPanel m_barDetailPanel = new BarDetailPanel();

//	final private List<Bar> m_rows = new ArrayList<>();
//	final public Chart m_chart = new Chart(m_rows);

	JButton btn_start = new JButton("Start");
	JButton btn_stop = new JButton("Stop");

////important variable
	RealTimeModel m_realtimeModel = new RealTimeModel(this);
	HistoricalData m_historicalData = new HistoricalData(this);
	PositionData m_positionModel = new PositionData(this);
	AccountSummaryModel m_accountsummaryModel = new AccountSummaryModel(this);
	InformationModel info = new InformationModel();
//	LiveOrder m_live = new LiveOrder(this);
	OpenOrderModel m_openOrder = new OpenOrderModel(this);
	LiveOrderModel liveOrder =new LiveOrderModel(this);
	
	
	PanelData histPanel = new PanelData(m_historicalData);
	PanelData positionPanel = new PanelData(m_positionModel);
//	PanelData accountPanel = new PanelData(m_accountsummaryModel);

	PanelData infoPanel = new PanelData(info);
	PanelData livePanel = new PanelData(liveOrder);

	InputText m_signal = new InputText("signal", "0");
	InputText m_qty = new InputText("QTY","0");
	InputText m_traget = new InputText("Traget","");
	InputText m_stop = new InputText("Stop", "");
		
	JCheckBox c_signal = new JCheckBox("Real sig",true);
	JCheckBox c_bar = new JCheckBox("Real bar",true);
//	String tradeType= "";
	String buytype[]={"Simple", "Dynamic", "Bracket"};        
    JComboBox buysellType=new JComboBox(buytype);
	
	private static Component sp(int n) {
		return Box.createHorizontalStrut(n);
	}
	AutoTradePanel(AutoTradeBuilder autoTradeBuilder){
		createPanel( autoTradeBuilder.symbol, 
				autoTradeBuilder.sectype, 
				autoTradeBuilder.qty, 
				autoTradeBuilder.traget, 
				autoTradeBuilder.stop, 
				autoTradeBuilder.day, 
				autoTradeBuilder.barsize);
	}
	AutoTradePanel( String symbol, SecType sectype, double qty, double traget, double stop, String day, BarSize barsize) {
		createPanel(symbol,  sectype,  qty,  traget,  stop,  day,  barsize);
	}
	
	private void createPanel( String symbol, SecType sectype, double qty, double traget, double stop, String day, BarSize barsize) {

		this.m_qty.setText(""+qty);
		this.m_traget.setText(""+traget);
		this.m_stop.setText(""+stop);
		m_contractPanel.setContract(symbol, sectype);
		m_barDetailPanel.setBarDetail(day, barsize);
		
		btn_start.setEnabled(true);
		btn_stop.setEnabled(false);
		
		
		
		HorzPanel buttonPanel = new HorzPanel();
		buttonPanel.add(btn_start);		
		buttonPanel.add(btn_stop);
		buttonPanel.add(m_signal);
		buttonPanel.add(m_qty);
		buttonPanel.add(m_traget);
		buttonPanel.add(m_stop);
		
		
		VerticalPanel verti_barsig = new VerticalPanel();
		VerticalPanel verti_buysell = new VerticalPanel();
		verti_barsig.add(buttonPanel);
		
		verti_barsig.add(c_bar);
		verti_barsig.add(c_signal);
		
		
//		verti_main.add(verti1);
		StackPanel leftTop = new StackPanel();
		leftTop.setBorder(new TitledBorder("Buy sell type"));
		leftTop.add(buysellType);
		
		StackPanel leftPanel = new StackPanel();
		leftPanel.setBorder(new TitledBorder("Define Contract"));
		
		leftPanel.add(m_contractPanel);
		leftPanel.add(m_barDetailPanel);
		
		StackPanel rightPanel = new StackPanel();
		rightPanel.setBorder(new TitledBorder("Information"));
		rightPanel.add(verti_barsig);
		rightPanel.add(infoPanel.getScrollPane());
		rightPanel.add(histPanel.getScrollPane());
		rightPanel.add(positionPanel.getScrollPane());
		rightPanel.add(livePanel.getScrollPane());
		

//		JScrollPane chartScroll = new JScrollPane(m_chart);
//		m_chart.setBorder(new TitledBorder("chart"));
//		chartScroll.setBorder(new TitledBorder("chart scroll"));
		
		
		HorzPanel horzPanel = new HorzPanel();
		VerticalPanel ver_main = new VerticalPanel();
		ver_main.add(leftTop);
		ver_main.add(leftPanel);
		horzPanel.add(ver_main);
//		horzPanel.add(m_chart);
		horzPanel.add(rightPanel);
		
		add(horzPanel);


		btn_start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
//				btn_start.setEnabled(false);
				btn_stop.setEnabled(true);
				onStart();

			}
		});
		btn_stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btn_start.setEnabled(true);
				btn_stop.setEnabled(false);
				onStop();
			}
		});
	}

	protected void onStart() {
		m_contractPanel.onOK(); // set Contract
		
		
		System.out.println("get Realtime");		
		m_realtimeModel.reqRealTime();
//		m_live.reqLiveOrder();

	}

	protected void onStop() {
		m_realtimeModel.cancelRealTime();
	}
	
	@Override
	public void activated() {
	// when tab is activated
//		m_contractPanel.onOK();
		
		
//		accountPanel = new PanelData(apitest.ApiDemo.INSTANCE.accountModel);
//		Contract m = m_contractPanel.getContract();
	}

	@Override
	public void closed() {
		// TODO Auto-generated method stub

	}

}
