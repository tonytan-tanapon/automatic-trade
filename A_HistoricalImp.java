package apitest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.table.DefaultTableModel;

import com.ib.client.Contract;
import com.ib.client.Types.BarSize;
import com.ib.client.Types.DurationUnit;
import com.ib.client.Types.WhatToShow;
import com.ib.controller.ApiController.IHistoricalDataHandler;
import com.ib.controller.Bar;

import autotrade.ContractATS;

public class A_HistoricalImp extends A_Historical implements IHistoricalDataHandler{
	ArrayList<Bar> data = new ArrayList<Bar>();
	String[] columnNames = {"datetime","open","high","low","close","volume"};
	DefaultTableModel tableModel = new DefaultTableModel();
	public A_HistoricalImp(A_Mediator med, String name) {
		super(med, name);
		// TODO Auto-generated constructor stub
		
		for(int i=0;i<columnNames.length ;i++) {
			tableModel.addColumn(columnNames[i]);
		}
	}
	public void resetTable() {
		if (tableModel.getRowCount() > 0) {
		    for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
		    	tableModel.removeRow(i);
		    }
		}
	}
	@Override
	public void send(String msg, Object data) {
		// TODO Auto-generated method stub
		mediator.sendMessage(msg, this);
		
	}

	@Override
	public void receive(String msg) {
		// TODO Auto-generated method stub
		resetTable();
		data.clear();
		reqHistorical();
	}
	@Override
	public void reqHistorical() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");

				///time_zone US/Eastern
				cal.add(Calendar.HOUR, 4);
				String endDateTime = form.format(cal.getTime());
				

//				Date currentTimePlusOneMinute = cal.getTime();
//				String endDateTime = form.format("20240314-13:45:00");
				int duration = 1;
				DurationUnit durationUnit = DurationUnit.DAY;
//				BarSize barSize = BarSize._1_min;
				BarSize barSize = BarSize._30_mins;
				WhatToShow whatToShow = WhatToShow.TRADES;
				
			
				Contract contract = ContractATS.getContractStock("AAPL");
				System.out.println(" >>20240314-13:45:00>>>>>>>>>>>>> "+endDateTime);
				ApiDemo.INSTANCE.controller().reqHistoricalData(contract, endDateTime, duration, durationUnit, barSize, whatToShow, false, false, this);
//				ApiDemo.INSTANCE.controller().reqHistoricalData(contract, endDateTime, duration, durationUnit, barSize,
//						whatToShow, false, false, this);
	}
	

	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return data;
	}

	@Override
	public void historicalData(Bar bar) {
		// TODO Auto-generated method stub
		tableModel.insertRow(tableModel.getRowCount(), new Object[] { bar.timeStr(), bar.open(), bar.high(), bar.low(), bar.close(), bar.volume() });
		
		data.add(bar);
//		System.out.println(bar);
		
	}

	@Override
	public void historicalDataEnd() {
		// TODO Auto-generated method stub
//		ApiDemo.INSTANCE.controller().cancelHistoricalData(this);
		send("hist", this);
	}

	@Override
	public Object getTableData() {
		// TODO Auto-generated method stub
		return tableModel;
	}

	

}
