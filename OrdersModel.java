package apitest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.ib.controller.ApiController.ILiveOrderHandler;

class OrdersModel extends AbstractTableModel implements ILiveOrderHandler {
	private Map<Integer, OrderRow> m_map = new HashMap<>();
	private List<OrderRow> m_orders = new ArrayList<>();

	@Override
	public int getRowCount() {
		return m_orders.size();
	}
	public void clear() {
		m_orders.clear();
		m_map.clear();
	}

	public OrderRow get(int i) {
		return m_orders.get(i);
	}

	@Override
	public void openOrder(Contract contract, Order order, OrderState orderState) {
		OrderRow full = m_map.get(order.permId());

		if (full != null) {
			full.m_order = order;
			full.m_state = orderState;
			fireTableDataChanged();
		} else if (shouldAdd(contract, order, orderState)) {
			full = new OrderRow(contract, order, orderState);
			add(full);
			m_map.put(order.permId(), full);
			fireTableDataChanged();
		}
	}

	protected boolean shouldAdd(Contract contract, Order order, OrderState orderState) {
		return true;
	}

	protected void add(OrderRow full) {
		m_orders.add(full);
	}

	@Override
	public void openOrderEnd() {
	}


	@Override
	public int getColumnCount() {
		return 10;
	}

	@Override
	public String getColumnName(int col) {
		switch (col) {
		case 0:
			return "Perm ID";
		case 1:
			return "Client ID";
		case 2:
			return "Order ID";
		case 3:
			return "Account";
		case 4:
			return "ModelCode";
		case 5:
			return "Action";
		case 6:
			return "Quantity";
		case 7:
			return "Cash Qty";
		case 8:
			return "Contract";
		case 9:
			return "Status";
		default:
			return null;
		}
	}

	@Override
	public Object getValueAt(int row, int col) {
		OrderRow fullOrder = m_orders.get(row);
		Order order = fullOrder.m_order;
		switch (col) {
		case 0:
			return order.permId();
		case 1:
			return order.clientId();
		case 2:
			return order.orderId();
		case 3:
			return order.account();
		case 4:
			return order.modelCode();
		case 5:
			return order.action();
		case 6:
			return order.totalQuantity();
		case 7:
			return (order.cashQty() == Double.MAX_VALUE) ? "" : String.valueOf(order.cashQty());
		case 8:
			return fullOrder.m_contract.description();
		case 9:
			return fullOrder.m_state.status();
		default:
			return null;
		}
	}

	@Override
	public void handle(int orderId, int errorCode, String errorMsg) {
	}

	static class OrderRow {
		Contract m_contract;
		Order m_order;
		OrderState m_state;

		OrderRow(Contract contract, Order order, OrderState state) {
			m_contract = contract;
			m_order = order;
			m_state = state;
		}
	}

	static class Key {
		int m_clientId;
		int m_orderId;

		Key(int clientId, int orderId) {
			m_clientId = clientId;
			m_orderId = orderId;
		}
	}

	@Override
	public void orderStatus(int orderId, OrderStatus status, Decimal filled, Decimal remaining, double avgFillPrice,
			int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
		// TODO Auto-generated method stub
//		OrderRow full = m_map.get(permId);
//		if (full != null) {
//			full.m_state.status(status);
//		}
//		fireTableDataChanged();
	}
}
