package apitest;

import com.ib.client.Decimal;
import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.ib.controller.ApiController.IOrderHandler;

public class A_OrderStatusImp extends A_OrderStatus implements IOrderHandler{

	public A_OrderStatusImp(A_Mediator med, String name) {
		super(med, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void send(String msg, Object data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receive(String msg) {
		// TODO Auto-generated method stub
//		ApiDemo.INSTANCE.controller()
		
		
	}

	@Override
	public void reqOrderStatus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void orderState(OrderState orderState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void orderStatus(OrderStatus status, Decimal filled, Decimal remaining, double avgFillPrice, int permId,
			int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
		// TODO Auto-generated method stub
	System.out.println("orderStatus");	
	}

	@Override
	public void handle(int errorCode, String errorMsg) {
		// TODO Auto-generated method stub
		
	}

}
