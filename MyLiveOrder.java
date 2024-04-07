package apitest;

import java.util.HashMap;

import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.ib.controller.AccountSummaryTag;

public class MyLiveOrder {
	Contract contract;
	Order order;
	OrderState orderState;
	static int  count = 0;
	public void setLiveOrder(Contract contract, Order order, OrderState orderState) {
		
		this.contract = contract;
		this.order = order;
		this.orderState = orderState;
		
		System.out.println("111111>> "+contract.localSymbol());

	}
	
	public void setLiveOrder(int orderId, OrderStatus status, Decimal filled, Decimal remaining, double avgFillPrice,
			int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
		
		System.out.println("22222>> "+orderId+" "+permId+" "+parentId +" "+remaining);
		if(Double.parseDouble(""+remaining)>0.0) {
			count++;
		}
	}
	
	
	
	public int getNumberLiveOrder() {
		return count;
	}
}
