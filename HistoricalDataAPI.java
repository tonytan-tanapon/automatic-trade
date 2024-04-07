package apitest;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;

import com.ib.client.Contract;
import com.ib.client.Types.BarSize;
import com.ib.client.Types.DurationUnit;
import com.ib.client.Types.WhatToShow;
import com.ib.controller.ApiController.IHistoricalDataHandler;
import com.ib.controller.Bar;

public class HistoricalDataAPI {
	// request
	IHistoricalDataHandler handler = null;
	Contract contract = new Contract();

	// defualt
	String endDateTime = "";
	int duration = 5;
	DurationUnit durationUnit = DurationUnit.DAY;
	BarSize barSize = BarSize._1_day;
	WhatToShow whatToShow = WhatToShow.MIDPOINT;
	boolean rthOnly = false;
	boolean keepUpToDate = false;

	public HistoricalDataAPI(Contract contract, IHistoricalDataHandler handler) {
		this.contract = contract;
		this.handler = handler;
		setLastDay();
	}

	public void setLastDay() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
		this.endDateTime = form.format(cal.getTime());
	}

	public void reqHistoricalData() {
		ApiDemo.INSTANCE.controller().reqHistoricalData(contract, endDateTime, duration, durationUnit, barSize,
				whatToShow, rthOnly, keepUpToDate, handler);
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public void setEndTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public void setBarSize(BarSize barSize) {
		this.barSize = barSize;
	}
	public void setWhatToShow(WhatToShow whatToShow) {
		this.whatToShow = whatToShow;
	}
	public void setrthOnly(boolean rthOnly) {
		this.rthOnly = rthOnly;
	}
	public void setkeepUpToDate(boolean keepUpToDate) {
		this.keepUpToDate = keepUpToDate;
	}
	

}
