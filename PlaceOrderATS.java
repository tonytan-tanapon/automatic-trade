package apitest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.ib.client.Types.Action;
import com.ib.client.Types.TimeInForce;
import com.ib.controller.ApiController.ILiveOrderHandler;
import com.ib.controller.ApiController.IOrderHandler;

import autotrade.API;
import samples.testbed.orders.OrderSamples;

public class PlaceOrderATS implements IOrderHandler {

	public PlaceOrderATS() {
		// from Historical to Position
		System.out.println("Create placeOrder");

	}

	public void cancelOrder() {

		int orderId = 0;
//		API.INSTANCE.m_controller.cancelOrder(orderId);
//		API.INSTANCE.m_controller.cancelAllOrders();
	}

	

	public void placeOrder(Contract contract, Order order) {
		ApiDemo.INSTANCE.controller().placeOrModifyOrder(contract, order, this);

	}

	public void placeBracketOder(Contract contract, String direction, double pos, String type, double limitPrice,double takeProfitLimitPrice, double stopLossPrice) {
//		int id = ApiDemo.INSTANCE.controller().getOrderID();
//		System.out.println("=>>>>>>>>>>> ID <<<<<<<<<<<< " + id);
//		System.out.println("Direction:"+direction+" position "+pos+" type:"+type+" limitprice "+limitPrice+" takeProfitLimitPrice "+takeProfitLimitPrice+" stop "+stopLossPrice);

//		ArrayList<Order> bracket = BracketOrder(id, direction, pos, type,limitPrice, takeProfitLimitPrice, stopLossPrice);
//	        for(Order o : bracket) {
//	        	ApiDemo.INSTANCE.controller().placeOrModifyOrder( contract ,o, this);
//	        	ApiDemo.INSTANCE.controller().nextValidId(++id);
//	        }
	}
	public ArrayList<Order> BracketOrder(int parentOrderId, String action, double quantity,String type, double limitPrice, double takeProfitLimitPrice, double stopLossPrice) {

		//This will be our main or "parent" order
        Order parent = new Order();
        parent.orderId(parentOrderId);
        parent.account( action);
  
        if(type.equals("LMT")) {
        	 parent.orderType("LMT");
             parent.lmtPrice(limitPrice);
        }
        else {
        	 parent.orderType("MKT");
        }
       
        parent.totalQuantity(Decimal.parse(""+quantity));
        parent.transmit(false);
        
        //The parent and children orders will need this attribute set to false to prevent accidental executions.
        //The LAST CHILD will have it set to true, 
        
        
        Order takeProfit = new Order();
        takeProfit.orderId(parent.orderId() + 1);
        takeProfit.action(action.equals("BUY") ? "SELL" : "BUY");
        takeProfit.orderType("LMT");
        takeProfit.totalQuantity(Decimal.parse(""+quantity));
        takeProfit.lmtPrice(takeProfitLimitPrice);
        takeProfit.parentId(parentOrderId);
        takeProfit.goodTillDate();
        takeProfit.transmit(false);
        
        Order stopLoss = new Order();
        stopLoss.orderId(parent.orderId() + 2);
        stopLoss.action(action.equals("BUY") ? "SELL" : "BUY");
        stopLoss.orderType("STP");
        //Stop trigger price
        stopLoss.auxPrice(stopLossPrice);
        stopLoss.totalQuantity(Decimal.parse(""+quantity));
        stopLoss.parentId(parentOrderId);
        //In this case, the low side order will be the last child being sent. Therefore, it needs to set this attribute to true 
        //to activate all its predecessors
        stopLoss.transmit(true);
        
        ArrayList<Order> bracketOrder = new ArrayList<Order>();
        bracketOrder.add(parent);
        bracketOrder.add(takeProfit);
        bracketOrder.add(stopLoss);
        
        return bracketOrder;
    }
	
	@Override
	public void orderState(OrderState orderState) {
		// TODO Auto-generated method stub
//		System.out.println("orderState >>> " + orderState.toString());
	}


	

	@Override
	public void handle(int errorCode, String errorMsg) {
		// TODO Auto-generated method stub
		System.out.println("errorCode: " + errorCode);
		System.out.println("errorMsg: " + errorMsg);
		
	}

	@Override
	public void orderStatus(OrderStatus status, Decimal filled, Decimal remaining, double avgFillPrice, int permId,
			int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
		// TODO Auto-generated method stub
//		System.out.println("Get order");
//		System.out.println("status: " + status + "\n" + "filled: " + filled + "\n" + "remaining: " + remaining + "\n"
//				+ "avgFillPrice:" + avgFillPrice + "\n" + "permId: " + permId + "\n" + "parentId: " + parentId + "\n"
//				+ "lastFillPrice: " + lastFillPrice + "\n" + "clientId: " + clientId + "\n" + "whyHeld: " + whyHeld
//				+ "\n" + "mktCapPrice: " + mktCapPrice + "\n");
//		
//		System.out.println("End order");
	}
	
	
	
}
