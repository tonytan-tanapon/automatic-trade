package apitest;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.controller.ApiController.ICompletedOrdersHandler;

public class CompleteOrderModel implements ICompletedOrdersHandler{

	public void reqCompleteOrder() {
		ApiDemo.INSTANCE.controller().reqCompletedOrders(this);
	}
	@Override
	public void completedOrder(Contract contract, Order order, OrderState orderState) {
		// TODO Auto-generated method stub
		System.out.println(""+contract.toString()+"\n"+order.getAction()+"\n"+orderState.completedStatus());
		
		if(contract.symbol().contentEquals("EUR")) {
			System.out.println("Yes");
		}
	}

	@Override
	public void completedOrdersEnd() {
		// TODO Auto-generated method stub
		System.out.println("End");
	}

}
