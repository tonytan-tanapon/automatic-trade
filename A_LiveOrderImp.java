package apitest;

import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.ib.controller.AccountSummaryTag;
import com.ib.controller.ApiController.ILiveOrderHandler;

public class A_LiveOrderImp extends A_LiveOrder implements ILiveOrderHandler {

	public A_LiveOrderImp(A_Mediator med, String name) {
		super(med, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void openOrder(Contract contract, Order order, OrderState orderState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openOrderEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void orderStatus(int orderId, OrderStatus status, Decimal filled, Decimal remaining, double avgFillPrice,
			int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
		// TODO Auto-generated method stub
//		System.out.println("Liveorder" +remaining );
		if(Double.parseDouble(""+remaining) == 0 ) {
//			 send message to mediator{}
			 
			 send("complete",this);
		 }
	
	}

	@Override
	public void handle(int orderId, int errorCode, String errorMsg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(String msg, Object data) {
		// TODO Auto-generated method stub
		mediator.sendMessage(msg, this);
	}

	@Override
	public void receive(String msg) {
		// TODO Auto-generated method stub
		ApiDemo.INSTANCE.controller().reqLiveOrders(this);
	}

	@Override
	public void reqLiveOrder() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return null;
	}


}
