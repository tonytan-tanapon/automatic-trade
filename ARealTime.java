package apitest;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.ib.client.Contract;
import com.ib.client.Types.BarSize;
import com.ib.client.Types.DurationUnit;
import com.ib.client.Types.WhatToShow;
import com.ib.controller.Bar;
import com.ib.controller.ApiController.IHistoricalDataHandler;
import com.ib.controller.ApiController.IRealTimeBarHandler;

public class ARealTime implements IRealTimeBarHandler {
	AutoRobotPanel autotradePanel;
	Bar m_barRealtime;
	Contract contract;
	Object handler;
	public ARealTime(AutoRobotPanel autoTradePanel) {
		this.autotradePanel = autoTradePanel;
	}
	ARealTime aRealTime;
	public void start(ARealTime aRealTime, Object handler) {
		
		this.aRealTime = aRealTime;
		setNextTask(handler);
		
		contract = autotradePanel.m_contract;
		BarDetailPanel m_barDetailPanel = autotradePanel.m_barDetailPanel;

		WhatToShow whatToShow = autotradePanel.m_barDetailPanel.whatToShow.getSelectedItem();

		// temp when finish use below
//		autotradePanel.m_accountsummaryModel.reqAccount();
		
//		model.reqAccount();

		if(autotradePanel.c_bar.isSelected()) {
			// launch version
			System.out.println("Real time trade");
			ApiDemo.INSTANCE.controller().reqRealTimeBars(contract, whatToShow, false, aRealTime);
			

		}
		else {
			// simulate version
			System.out.println("Simulate time trade");
			if (handler instanceof IHistoricalDataHandler) {
				realTiemToHistorical();
			} 
			else {
				System.out.println("no handler");
			}
		}
	}
	
	void realTiemToHistorical(){
		
		if (handler instanceof IHistoricalDataHandler) {
			((AHistorical) handler).start((AHistorical)  handler);
		}
//		Calendar cal = Calendar.getInstance();
//		SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
//		String endDateTime = form.format(cal.getTime());
//		int duration = Integer.parseInt(autotradePanel.m_barDetailPanel.duration.getText());
//		DurationUnit durationUnit = autotradePanel.m_barDetailPanel.durationUnit.getSelectedItem();
//		BarSize barSize = autotradePanel.m_barDetailPanel.barSize.getSelectedItem();
//		WhatToShow whatToShow1 = autotradePanel.m_barDetailPanel.whatToShow.getSelectedItem();
//		ApiDemo.INSTANCE.controller().reqHistoricalData(autotradePanel.m_contract, endDateTime, duration,
//				durationUnit, barSize, whatToShow1, false, false, (IHistoricalDataHandler) handler);
	}

	public void setNextTask(Object handler) {
		this.handler = handler;
	}
	@Override
	public void realtimeBar(Bar bar) {
		// TODO Auto-generated method stub
		System.out.println(contract.description()+": " + bar);
		
		m_barRealtime = bar;
		
		
		if (handler instanceof IHistoricalDataHandler) {
			realTiemToHistorical();
		} 
		else {
			//System.out.println("No next task");
		}
		
	}

	public void cancelRealTime() {
		// TODO Auto-generated method stub
		ApiDemo.INSTANCE.controller().cancelRealtimeBars(aRealTime);
		System.out.println("Stop RealTime");
	}

}
