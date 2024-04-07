package apitest;

import com.ib.controller.ApiController.IHistoricalDataHandler;
import com.ib.controller.ApiController.IPositionHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.ib.client.Contract;
import com.ib.client.Types.BarSize;
import com.ib.client.Types.DurationUnit;
import com.ib.client.Types.WhatToShow;
import com.ib.controller.Bar;

public class ATrade {
	AutoRobotPanel autoTradePanel;
	
	AInfo aInfo;
	APosition aPosition;
	AHistorical aHistorical ;
	ALiveOrder aLiveOrder;
	AOpenOrder aOpenOrder;
	
	ARealTime aRealTime;
	
	Contract contractOption = new Contract();
	
//	AProcess aProcess ;
//	AHistorical aHistorical = new AHistorical();
	public ATrade(AutoRobotPanel autoTradePanel){
		this.autoTradePanel = autoTradePanel;
		
		this.aInfo = new AInfo(autoTradePanel);
		this.aPosition = new APosition(autoTradePanel);
		this.aHistorical = new AHistorical(autoTradePanel);
		this.aLiveOrder = new ALiveOrder(autoTradePanel);
		this.aOpenOrder = new AOpenOrder(autoTradePanel);
		this.aRealTime = new ARealTime(autoTradePanel);
	}
	
	public void run() {

		
		System.out.println("ATrade");
		
//		aRealTime -> aHistorical (indicator) -> aPosition -> aLiveOrder -> aOpenOrder
		aRealTime.start(aRealTime,aHistorical);
			//		aRealTime.setNextTask(aHistorical);
		aHistorical.setNextTask(aPosition);
		aPosition.setNextTask(aLiveOrder);
		aLiveOrder.setNextTask(aOpenOrder);
		
		
//		aHistorical -> aPosition
//        aHistorical.start(aHistorical);
//        aHistorical.setNextTask(aPosition);
//        aPosition.setNextTask(aLiveOrder);
		
//		aPosition -> aHistorical
//        aPosition.start( aPosition);
//        aPosition.setNextTask(aHistorical);
		

//		showHistorical();
//		showPosition();
	}
	public void stop() {
		// TODO Auto-generated method stub
		aRealTime.cancelRealTime();
	}
}
