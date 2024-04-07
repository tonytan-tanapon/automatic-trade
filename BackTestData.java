package apitest;

import com.ib.controller.ApiController.IHistoricalDataHandler;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import com.ib.client.Types.BarSize;
import com.ib.client.Types.DurationUnit;
import com.ib.client.Types.SecType;
import com.ib.client.Types.WhatToShow;
import com.ib.controller.Bar;



public class BackTestData implements IHistoricalDataHandler {
	//request variable
	public HistoricalModel data = new HistoricalModel();
	BackTestPanel backTestData;
	
	//additional variable
	double sumPL = 0;
	
	BackTestData(BackTestPanel backTestPanel) {
		this.backTestData = backTestPanel;
		data.setDefualColume();

	}

	public void reqHist() {
		data.clear();
		

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
		String endDateTime = form.format(cal.getTime());
		
		int duration = Integer.parseInt(backTestData.m_barDetailPanel.duration.getText());
		DurationUnit durationUnit = backTestData.m_barDetailPanel.durationUnit.getSelectedItem();
		BarSize barSize = backTestData.m_barDetailPanel.barSize.getSelectedItem();
		WhatToShow whatToShow = backTestData.m_barDetailPanel.whatToShow.getSelectedItem();
		ApiDemo.INSTANCE.controller().reqHistoricalData(backTestData.m_contract, endDateTime, duration, durationUnit,
				barSize, whatToShow, false, false, this);
	}

	@Override
	public void historicalData(Bar bar) {
		boolean preMaket = false;

		if (backTestData.m_contract.secType().equals(SecType.STK) && preMaket == false) {
//			System.out.println(bar);
			data.setBarStock(bar);
		} else {
//			if (autoTradePanel.m_contract.secType().equals(SecType.CASH)) {
			data.setBarFX(bar);
//			}
		}
	}

	Strategy strategy;

	@Override
	public void historicalDataEnd() {
		// TODO Auto-generated method stub

		strategy = new StrategyMA(data);
		strategy.setIndicator();

		data.fireTableStructureChanged();
		data.fireTableDataChanged();
		System.out.println("Backtest end: " + backTestData.m_contract.symbol() + " : " + data.getRowCount());

		System.out.println(data.m_bars + " " + data.barAdj);
		System.out.println(data.col_name.toString());
		System.out.println(data.getColumIndex("Signal"));

		for(int i = backTestData.backTestResult.getRowCount()-1 ; i>=0;i--) {
		backTestData.backTestResult.removeRow(i);
		}
		sumPL = 0;
		/// get signal buy
		for (int i = 0; i < data.m_bars.size(); i++) {
			String time = data.getValueAt(i, data.getColumIndex("Date/Time")).toString();
			double signal = Double.parseDouble(data.getValueAt(i, data.getColumIndex("Signal")).toString());
			double price = Double.parseDouble(data.getValueAt(i, data.getColumIndex("Close")).toString()); // close

			if (signal != 0) {
				double buy = price * 1;
//				table.addRow(new String[] { time, "" + signal, "" + buy, "" + "", "1" });
				Vector<Object> rowData = new Vector<Object>();
				rowData.add(time);
				rowData.add(signal);
				rowData.add(buy);
				rowData.add(0);
				rowData.add(0);
				backTestData.backTestResult.addRow(rowData);
			}

		}
		/// get signal sell
		for (int i = 0; i < backTestData.backTestResult.getRowCount() - 1; i++) {
			Double signal = Double.parseDouble(backTestData.backTestResult.getValueAt(i, 1).toString()); // signal
			Double buy =  Double.parseDouble(backTestData.backTestResult.getValueAt(i, 2).toString()); // get buy col
			Double sell = Double.parseDouble(backTestData.backTestResult.getValueAt(i + 1, 2).toString()); // get sell col from buy
			backTestData.backTestResult.setValueAt(sell, i, 3); // set sell col
			Double qty = Double.parseDouble(backTestData.m_qty.getText());
			Double pl = sell - buy;
			pl = pl * signal * qty;
			
			Double percent = (sell - buy)/sell*100 * signal;
			backTestData.backTestResult.setValueAt(Indicator.floor(pl), i, 4); // set sell col
			backTestData.backTestResult.setValueAt(Indicator.floor(percent), i, 5); // set sell col
			
			sumPL+= pl;
		}
		backTestData.m_pl.setText(""+sumPL);
		System.out.println("PL: "+sumPL);
//		backTestData.backPanel.setBackground(Color.blue);
	}

	
}
