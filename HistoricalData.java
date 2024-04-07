package apitest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.table.DefaultTableModel;

import com.ib.client.Contract;
import com.ib.client.Types.BarSize;
import com.ib.client.Types.DurationUnit;
import com.ib.client.Types.SecType;
import com.ib.client.Types.WhatToShow;
import com.ib.controller.ApiController.IHistoricalDataHandler;
import com.ib.controller.Bar;

public class HistoricalData extends HistoricalModel implements IHistoricalDataHandler {
	
	AutoTradePanel autoTradePanel;
//	public HistoricalModel data = new HistoricalModel();
	HistoricalData(AutoTradePanel autoTradePanel) {
		this.autoTradePanel = autoTradePanel;
		
		setDefualColume();
//		data.setDefualColume();

	}
//	public HistoricalModel getModel() {
//		return data;
//	}
	@Override
	public void historicalData(Bar bar) {
		// TODO Auto-generated method stub
		
//		data.addBar(bar);
		boolean preMaket = false;
		
		if (autoTradePanel.m_contract.secType().equals(SecType.STK) && preMaket == false) {
//			System.out.println(bar);
			setBarStock(bar);
		}
		else {
//			if (autoTradePanel.m_contract.secType().equals(SecType.CASH)) {
				setBarFX(bar);		
//			}
		} 
		
	}
	StrategySMA str;
	@Override
	public void historicalDataEnd() {
		

		/// Calling strategy
//		Strategy str1 = new Strategy(this);
		str = new StrategySMA(this);
	
		fireTableStructureChanged();
		fireTableDataChanged();
		autoTradePanel.histPanel.setScrollToButtom();
//		autoTradePanel.m_contractPanel.setOptionStrike(model.m_bars.get(model.m_bars.size()-1).close());  // close price

		autoTradePanel.m_positionModel.reqPosition();
		System.out.println("Corrected historicalData >>> ");
		/// go to reqPosition
		 
	}
	
	public void reqHist() {

		clear();

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
		String endDateTime = form.format(cal.getTime());
		int duration = Integer.parseInt(autoTradePanel.m_barDetailPanel.duration.getText());
		DurationUnit durationUnit = autoTradePanel.m_barDetailPanel.durationUnit.getSelectedItem();
		BarSize barSize = autoTradePanel.m_barDetailPanel.barSize.getSelectedItem();
		WhatToShow whatToShow = autoTradePanel.m_barDetailPanel.whatToShow.getSelectedItem();
		ApiDemo.INSTANCE.controller().reqHistoricalData(autoTradePanel.m_contract, endDateTime, duration, durationUnit, barSize,
				whatToShow, false, false, this);

	}
}
	
	

