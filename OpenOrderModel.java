package apitest;

import com.ib.client.Contract;
import com.ib.client.Types.Right;
import com.ib.client.Types.SecType;

public class OpenOrderModel {
	AutoTradePanel autoTradePanel;

	public OpenOrderModel(AutoTradePanel autoTradePanel) {
		// TODO Auto-generated constructor stub
		this.autoTradePanel = autoTradePanel;
	}
	public void reqOpenOrder() {
		System.out.println("open order");
		String symbol = getSymbol();
		double signal = getSignal();
		double position = getPosition();
		double liveposition = getLivePosition();
		double avg = getAverage();
		double price = getLastPrice();
		
		System.out.println("Symbol:" + symbol);
		System.out.println("Signal: " + signal);
		System.out.println("Position: " + position);
		System.out.println("Live Position: " + liveposition);
//		System.out.println("accountValue: " + accountValue);

		OrderSend orderSend = getOrderSend(signal,price);		
		
		String textWrite = getTextWrite();
		
		double qty = getQTY();
		double traget = getTraget();
		double stop = getStop();
		
		setInfoPanel(price, avg, position, signal, traget, stop);

		
//		int id = ApiDemo.INSTANCE.controller().getOrderID();
//		System.out.println("=>>>>>>>>>>> ID <<<<<<<<<<<< " + id);

		if (autoTradePanel.m_contract.getSecType().equals("CASH")) {
			double average = autoTradePanel.m_positionModel.average;

			if (autoTradePanel.buysellType.getSelectedIndex() == 0) { // Simple
				orderSend.createOrder(textWrite, signal, position, liveposition, qty);
			} else if (autoTradePanel.buysellType.getSelectedIndex() == 1) { // dynamic
				System.out.println("average: " + average + "traget: " + traget + " stop: " + stop);
				orderSend.createOrderDynamic(textWrite, price, signal, position, liveposition, average, qty, traget, stop);
			} else if (autoTradePanel.buysellType.getSelectedIndex() == 2) {
				orderSend.createOrderBacket(textWrite, price, signal, position, liveposition, qty, traget, stop);
			}
		} else {
			double average = autoTradePanel.m_positionModel.average;
			if (autoTradePanel.buysellType.getSelectedIndex() == 0) { // Simple
				orderSend.createOrder(textWrite, signal, position, liveposition, qty);
			} else if (autoTradePanel.buysellType.getSelectedIndex() == 1) { // dynamic
				orderSend.createOrderDynamic(textWrite, Indicator.floor(price), signal, position, liveposition, average, qty,
						traget, stop);
			} else if (autoTradePanel.buysellType.getSelectedIndex() == 2) {
				orderSend.createOrderBacket(textWrite, Indicator.floor(price), signal, position, liveposition, qty, traget,
						stop);
			}

		}
//		autoTradePanel.m_accountsummaryModel.reqAccount();
//		autoTradePanel.m_historicalData.reqHist();
	}
	
	public double getLastPrice() {
		double price = 0;
////////////Type of bar
		if (autoTradePanel.c_bar.isSelected()) {
			price = autoTradePanel.m_realtimeModel.m_barRealtime.close();
		} else {
			price = autoTradePanel.m_historicalData.m_bars
					.get(autoTradePanel.m_historicalData.m_bars.size() - 1).close();
		}
		return price;
	}

	public double getSignal() {
		/////// Type of Signal
		double signal = 0;
		if (autoTradePanel.c_signal.isSelected() == false) {
			try {
//				autoTradePanel.m_historicalData.str1.getSignal();
				signal = Integer.parseInt(autoTradePanel.m_signal.getText());
			} catch (Exception e) {
				signal = 0;
			}
		}
		return signal;
	}

	public double getAverage() {
		return autoTradePanel.m_positionModel.average;
	}

	public double getPosition() {
		return autoTradePanel.m_positionModel.position;
	}

	public String getSymbol() {
		String symbol = autoTradePanel.m_contract.symbol() + "/" + autoTradePanel.m_contract.currency() + " type:"
				+ autoTradePanel.m_contract.secType();
		return symbol;
	}

	public double getLivePosition() {
		return autoTradePanel.liveOrder.liveorder;
	}

	private OrderSend getOrderSend(double signal, double price) {
		OrderSend send = null;
		if (autoTradePanel.m_contract.equals("OPT")) {
			System.out.println("option contract created");
			Contract con = new Contract();

			con.symbol(autoTradePanel.m_contract.symbol());
			con.secType(SecType.OPT);
			con.lastTradeDateOrContractMonth(ContractPanel.getFridayOption()); // find

			double st = Math.round(price);
			System.out.println(" strike ===== > " + st);
			con.strike(st); // find
			if (signal == 1) {
				con.right(Right.Call); // find
			}
			if (signal == 0) {
				con.right(Right.Put); // find
			}

			con.multiplier("100");
			con.exchange("SMART");
			con.currency("USD");

			System.out.println();
			send = new OrderSend(con);
		} else {
			send = new OrderSend(autoTradePanel.m_contract);
		}

		return send;
	}

	private String getTextWrite() {
		return autoTradePanel.m_historicalData.getLastRowData();
	}

	public double getQTY() {
		return Double.parseDouble(autoTradePanel.m_qty.getText());
	}

	public double getTraget() {
		return Double.parseDouble(autoTradePanel.m_traget.getText());
	}

	public double getStop() {
		return Double.parseDouble(autoTradePanel.m_stop.getText());
	}

	public void setInfoPanel(double price, double avg, double position,double signal, double traget, double stop) {
		autoTradePanel.info.infoRow.price = "" + Indicator.floor(price);
		autoTradePanel.info.infoRow.avgerage = "" + Indicator.floor(avg);
		autoTradePanel.info.infoRow.PL = "" + Indicator.floor((price - avg) * position);
		autoTradePanel.info.infoRow.position = "" + position;
		autoTradePanel.info.infoRow.signal = "" + signal;
		autoTradePanel.info.infoRow.symbol = "" + autoTradePanel.m_contract.symbol();
		autoTradePanel.info.infoRow.account = "" + ApiDemo.INSTANCE.m_acctInfoPanel.accountValue;
		if (position > 0) {
			autoTradePanel.info.infoRow.traget = "" + Indicator.floor(avg + traget);
			autoTradePanel.info.infoRow.stop = "" + Indicator.floor(avg - stop);

		} else if (position < 0) {
			autoTradePanel.info.infoRow.traget = "" + Indicator.floor(avg - traget);
			autoTradePanel.info.infoRow.stop = "" + Indicator.floor(avg + stop);

		} else {
			autoTradePanel.info.infoRow.traget = "0";
			autoTradePanel.info.infoRow.stop = "0";
		}

		autoTradePanel.info.clear();
		autoTradePanel.info.insertRow(autoTradePanel.info.infoRow);
	}
}
