package apitest;

import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.ib.client.Types.SecType;
import com.ib.controller.ApiController.IOrderHandler;

import autotrade.ContractATS;

public class A_OrderSendImp extends A_OrderSend implements IOrderHandler{
	Contract contract = new Contract();
	public A_OrderSendImp(A_Mediator med, String name) {
		super(med, name);
		// TODO Auto-generated constructor stub
	}
	public Contract getOptionContract(String symbol, String date) {
		
		contract.symbol(symbol.toUpperCase() ); //
		contract.currency("USD".toUpperCase() );
		contract.secType(SecType.OPT);
		contract.exchange("SMART".toUpperCase() );
		contract.lastTradeDateOrContractMonth(date); // 
	
		
		return contract;
		
	}
	@Override
	public void send(String msg, Object data) {
		// TODO Auto-generated method stub
		mediator.sendMessage(msg, this);
	}

	@Override
	public void receive(String msg, A_Detail detail) {
		// TODO Auto-generated method stub
		System.out.println("Opend Order NOW");
		
		if(msg.equals("buycall")) {
			System.out.println("BUY CALL");
			
			Contract contract = detail.callData.get(detail.callATM);
			detail.ContractPosition = contract;
			ApiDemo.INSTANCE.controller().placeOrModifyOrder(contract, OrderATS.buyMarket(detail.qty), this);
	
		}
		else if(msg.equals("sellcall")){
			System.out.println("SELL");
			Contract contract = detail.ContractPosition;
			ApiDemo.INSTANCE.controller().placeOrModifyOrder(contract, OrderATS.sellMarket(detail.qty), this);
	
		}else if(msg.equals("buyput")) {
			System.out.println("BUY PUT");
			Contract contract = detail.putData.get(detail.putATM);
			detail.ContractPosition = contract;
			ApiDemo.INSTANCE.controller().placeOrModifyOrder(contract, OrderATS.buyMarket(detail.qty), this);
	
		}
		else if(msg.equals("sellput")){
			System.out.println("SELL put");
			Contract contract = detail.ContractPosition;
			ApiDemo.INSTANCE.controller().placeOrModifyOrder(contract, OrderATS.sellMarket(detail.qty), this);
	
		}
		
		reqOrderSend();
	}

	@Override
	public void reqOrderSend() {
		// TODO Auto-generated method stub;
		
		
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
//		System.out.println("\norderStatus: "+filled+" " +remaining);
//		System.out.println("MANNNNNNNNNNNNNNNNNNNN : "+remaining);
		 if(Double.parseDouble(""+remaining) == 0 ) {
//			 send message to mediator{}
			 
//			 send("complete",this);
		 }
		 
		 
		 
	}

	@Override
	public void handle(int errorCode, String errorMsg) {
		// TODO Auto-generated method stub
		
	}

}
