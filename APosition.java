package apitest;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.Types.BarSize;
import com.ib.client.Types.DurationUnit;
import com.ib.client.Types.WhatToShow;
import com.ib.controller.ApiController.IHistoricalDataHandler;
import com.ib.controller.ApiController.ILiveOrderHandler;
import com.ib.controller.ApiController.IPositionHandler;

import apitest.PositionModel.PositionKey;
import apitest.PositionModel.PositionRow;

public class APosition extends PositionModel implements IPositionHandler {

	AutoRobotPanel autoTradePanel;
	boolean m_complete;
	double position = 0;
	double average = 0.0;
	
	String account;
	Contract contract;
	double pos;
	double avgCost;
	Object handler;
	APosition aPosition;
	public APosition(AutoRobotPanel autoTradePanel) {
		this.autoTradePanel = autoTradePanel;

	}

	public void start(APosition aPosition) {
		this.aPosition = aPosition;
		ApiDemo.INSTANCE.controller().reqPositions(aPosition);
	}

	public void setNextTask(Object handler) {
		this.handler = handler;
	}

	

	@Override
	public void positionEnd() {
		// TODO Auto-generated method stub
		System.out.println("Position end");
		
		
		fireTableDataChanged();
		m_complete = true;
		
//		autoTradePanel.liveOrder.reqLive();
//		ApiDemo.INSTANCE.controller().cancelPositions(this);
//		System.out.print("Corrected Positon >>> ");
		
//		ApiDemo.INSTANCE.controller().cancelPositions(aPosition);
		if (handler instanceof IHistoricalDataHandler) {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
			String endDateTime = form.format(cal.getTime());
			int duration = Integer.parseInt(autoTradePanel.m_barDetailPanel.duration.getText());
			DurationUnit durationUnit = autoTradePanel.m_barDetailPanel.durationUnit.getSelectedItem();
			BarSize barSize = autoTradePanel.m_barDetailPanel.barSize.getSelectedItem();
			WhatToShow whatToShow = autoTradePanel.m_barDetailPanel.whatToShow.getSelectedItem();
			ApiDemo.INSTANCE.controller().reqHistoricalData(autoTradePanel.m_contract, endDateTime, duration,
					durationUnit, barSize, whatToShow, false, false, (IHistoricalDataHandler) handler);
		} 
		else if (handler instanceof ILiveOrderHandler) {			
			ApiDemo.INSTANCE.controller().reqLiveOrders((ILiveOrderHandler) handler);
		}
		else if (handler instanceof IPositionHandler) {			
			ApiDemo.INSTANCE.controller().reqPositions( (IPositionHandler) handler);
		}
		else {
			System.out.println("No Next Task");
		}
	}

	@Override
	public void position(String account, Contract contract, Decimal pos, double avgCost) {
		// TODO Auto-generated method stub
		System.out.println(
				"account: " + account + " contract: " + contract.symbol() + " pos:" + pos + " avgCost: " + avgCost);
//		this.account = account;
//		this.contract = contract;
//		this.pos = pos;
//		this.avgCost = avgCost;
		
		PositionKey key = new PositionKey(account, contract.conid());
		PositionRow row = m_map.get(key);
		if (row == null) {
			row = new PositionRow();
			m_map.put(key, row);
			m_list.add(row);
		}
		row.update(account, contract, Double.parseDouble(""+pos), avgCost);

		if (m_complete) {
			fireTableDataChanged();
		}
		
		//////////// get position by symbol , check specific position
		if(autoTradePanel.m_contract.symbol().equals(contract.symbol()) && 
				autoTradePanel.m_contract.currency().equals(contract.currency()) &&
				autoTradePanel.m_contract.secType().equals(contract.secType())
				) {
			System.out.println(""+contract.symbol()+"/"+contract.currency()+" "+contract.secType());
			position = Double.parseDouble(""+pos);
			average = avgCost;

		}
	}

}
