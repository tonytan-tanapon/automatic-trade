package apitest;

import com.ib.controller.ApiController.IHistoricalDataHandler;
import com.ib.controller.ApiController.IPositionHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.ib.client.Types.BarSize;
import com.ib.client.Types.DurationUnit;
import com.ib.client.Types.SecType;
import com.ib.client.Types.WhatToShow;
import com.ib.controller.Bar;

public class AHistorical extends HistoricalModel implements IHistoricalDataHandler {

	AutoRobotPanel autoTradePanel;
	Bar bar;
	Object handle;
	//***********************
//	StrategySMA str;
	AStrategy str;
	AHistorical(AutoRobotPanel autoTradePanel) {
		this.autoTradePanel = autoTradePanel;

		setDefualColume();
//		data.setDefualColume();

	}

	public void start(IHistoricalDataHandler aHistorical) {
		clear();
		System.out.println("HI");
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
		String endDateTime = form.format(cal.getTime());
		int duration = Integer.parseInt(autoTradePanel.m_barDetailPanel.duration.getText());
		DurationUnit durationUnit = autoTradePanel.m_barDetailPanel.durationUnit.getSelectedItem();
		BarSize barSize = autoTradePanel.m_barDetailPanel.barSize.getSelectedItem();
		WhatToShow whatToShow = autoTradePanel.m_barDetailPanel.whatToShow.getSelectedItem();

		
		ApiDemo.INSTANCE.controller().reqHistoricalData(autoTradePanel.m_contract, endDateTime, duration, durationUnit,
				barSize, whatToShow, false, false, aHistorical);
	}

	public void setNextTask(Object next) {
		this.handle = next;
	}
	
	@Override
	public void historicalData(Bar bar) {
		// TODO Auto-generated method stub
		System.out.println(bar);
//		this.bar = bar;

		boolean preMaket = false;

		if (autoTradePanel.m_contract.secType().equals(SecType.STK) && preMaket == false) {
//			System.out.println(bar);
			setBarStock(bar);
		} else {
//			if (autoTradePanel.m_contract.secType().equals(SecType.CASH)) {
			setBarFX(bar);
//			}
		}
	}

	@Override
	public void historicalDataEnd() {
		// TODO Auto-generated method stub
		System.out.println("historical END");
		
		//***********************
		str = new AStrategy(this);
		
		fireTableStructureChanged();
		fireTableDataChanged();
		autoTradePanel.histPanel.setScrollToButtom();
//		autoTradePanel.m_contractPanel.setOptionStrike(model.m_bars.get(model.m_bars.size()-1).close());  // close price

//		autoTradePanel.m_positionModel.reqPosition();
//		System.out.println("Corrected historicalData >>> ");
		

		if (handle instanceof IPositionHandler) {
			ApiDemo.INSTANCE.controller().reqPositions((IPositionHandler) handle);
		} else {
			System.out.println("No Next Task");
		}
	}
}
