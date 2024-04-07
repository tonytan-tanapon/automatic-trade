package apitest;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.ib.client.Contract;
import com.ib.client.Types.BarSize;
import com.ib.client.Types.DurationUnit;
import com.ib.client.Types.WhatToShow;
import com.ib.controller.ApiController.IHistoricalDataHandler;
import com.ib.controller.ApiController.IPositionHandler;
import com.ib.controller.ApiController.IRealTimeBarHandler;
import com.ib.controller.Bar;

/// AutoTradePanel ===> RealTimeModel ==> HistoricalModel ====> StrategyModel 
/// ===> PositonModel ===> AccountSummaryModel ===> SendOrder
public class RealTimeModel implements IRealTimeBarHandler {

	AutoTradePanel autotradePanel;
	Bar m_barRealtime;
	Contract contract;
	public RealTimeModel(AutoTradePanel autotradePanel) {
		// TODO Auto-generated constructor stub
		this.autotradePanel = autotradePanel;
	}
	
	public void reqRealTime() {
		contract = autotradePanel.m_contract;
		BarDetailPanel m_barDetailPanel = autotradePanel.m_barDetailPanel;

		WhatToShow whatToShow = autotradePanel.m_barDetailPanel.whatToShow.getSelectedItem();

		// temp when finish use below
//		autotradePanel.m_accountsummaryModel.reqAccount();
		
//		model.reqAccount();

		if(autotradePanel.c_bar.isSelected()) {
			// launch version
			ApiDemo.INSTANCE.controller().reqRealTimeBars(contract, whatToShow, false, this);
		}
		else {
			// simulate version
			autotradePanel.m_historicalData.reqHist();
		}
	}

	@Override
	public void realtimeBar(Bar bar) {
		// TODO Auto-generated method stub
		System.out.println(contract.description()+": " + bar);
		m_barRealtime = bar;
		autotradePanel.m_historicalData.reqHist();
	}

	public void cancelRealTime() {
		// TODO Auto-generated method stub
		ApiDemo.INSTANCE.controller().cancelRealtimeBars(this);
		System.out.println("Stop RealTime");
	}

}
