package apitest;

import static com.ib.controller.Formats.fmt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.EWrapper;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.ib.client.Types.Right;
import com.ib.client.Types.SecType;
import com.ib.controller.ApiController.ILiveOrderHandler;

import autotrade.AutotradePanel;
import autotrade.ContractATS;

public class LiveOrder extends DefaultTableModel implements ILiveOrderHandler {
//	Map<String, LiveOrderRow> m_map = new HashMap<>();
//	List<LiveOrderRow> m_list = new ArrayList<>();

	int permId = 0;
	AutoTradePanel autoTradePanel;
	double liveposition = 0;
	Contract livecontract;

	public LiveOrder(AutoTradePanel autoTradePanel) {
		livecontract = autoTradePanel.m_contract;
		this.autoTradePanel = autoTradePanel;
		addColumn("permid");
		addColumn("symbol");
		addColumn("Filled");
		addColumn("remaining");
		addColumn("localSymbol");
		addColumn("Type");
		addColumn("status");

	}

	public void setLivePostion(Contract m_contract) {
		livecontract = m_contract;
	}

	public double getLivePosition() {
		// TODO Auto-generated method stub
		return liveposition;
	}

	public void reqLiveOrder() {
		clear();
		ApiDemo.INSTANCE.controller().reqLiveOrders(this);
	}

	public void clear() {
		
		for (int i = getRowCount() - 1; i >= 0; i--) {
			removeRow(i);
		}
		
	}

	Contract contract;
	Order order;
	OrderState orderState;

	@Override
	public void openOrder(Contract contract, Order order, OrderState orderState) {
		// TODO Auto-generated method stub
//		System.out.println("contract " + contract.symbol()+ " type "+ contract.getSecType()  );
//		System.out.println("order id "+ order.orderId()+ " permId "+order.permId());
		this.contract = contract;
		this.order = order;
		this.orderState = orderState;

	}

	@Override
	public void openOrderEnd() {
		// TODO Auto-generated method stub
		liveposition = 0;
		
		//check live order
//		for(int i=0;i<this.getRowCount();i++) {
			// check specific live order
//			if(getDataVector().get(i).get(1).equals(obj))
//			System.out.println("data "+this.getDataVector().get(i));
//			if (livecontract.symbol().equals(contract.symbol()) && livecontract.getSecType().equals(contract.getSecType())
//					&& livecontract.currency().equals(contract.currency()) && status.toString().equals("Cancelled")) {
//				liveposition = 0;
//			} else if (livecontract.symbol().equals(contract.symbol())
//					&& livecontract.getSecType().equals(contract.getSecType())
//					&& livecontract.currency().equals(contract.currency())) {
//				liveposition = remaining;
//
//			}
//		}
		
		
	}

	@Override
	public void handle(int orderId, int errorCode, String errorMsg) {
		// TODO Auto-generated method stub
		System.out.println(">>> " + orderId);

	}

	@Override
	public void orderStatus(int orderId, OrderStatus status, Decimal filled, Decimal remaining, double avgFillPrice,
			int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
		// TODO Auto-generated method stub
//		addRow(new Object[] { permId, contract.symbol(), 
//			Double.parseDouble(""+filled)	,
//			Double.parseDouble(""+remaining)	, contract.localSymbol(),
//				contract.getSecType(), status });
	}

}
