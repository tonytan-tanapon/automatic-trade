package apitest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.ib.client.Types.Right;
import com.ib.client.Types.SecType;
import com.ib.controller.AccountSummaryTag;
import com.ib.controller.ApiController.IOrderHandler;

public class A_MyOption implements IOrderHandler {
	A_Mediator mediator = new A_MediatorImp();
	A_Position m_position = new A_PositionImp(mediator, "Position");
	A_Tick m_tick = new A_TickImp(mediator, "Tick");
	A_Account m_account = new A_AccountImp(mediator, "Account");
	A_Historical m_hist = new A_HistoricalImp(mediator, "Hist");
	A_OpenOrder m_openOrder = new A_OpenOrderImp(mediator, "OpendOrder");
	A_OrderSend m_orderSend = new A_OrderSendImp(mediator, "OrderSend");
	A_OrderStatus m_OrdeStatus = new A_OrderStatusImp(mediator, "OrderStatus");
	A_OptionChain m_optionChain = new A_OptionChainImp(mediator, "Option Chain");
	
	Contract callContract = new Contract();
	Contract putContract = new Contract(); 
	Contract stockContract = new Contract();
	Contract optionContract = new Contract();
	MyStatus status =MyStatus.None;
	MyAccount account = new MyAccount();
	MyLiveOrder liveorder = new MyLiveOrder();
	MyHistorical historical = new MyHistorical();
	MyOptionChain optionchain = new MyOptionChain();
	MyPosition position = new MyPosition();
	MyIndicator indicator = new MyIndicator();
//	MyPlaceOrder placeOrder = new MyPlaceOrder();
	
//	MySendOrder sendOrder = new MySendOrder();
	String symbol = "AAPL";
	DayOfWeek dayOfWeek = DayOfWeek.SATURDAY;
	boolean step = false;
	boolean canOpenOrder = true;
	double pos = 0.0;
	
	double sig=0.0;
	public A_MyOption(String symbol){
		mediator.add(m_position);
		mediator.add(m_tick);
		mediator.add(m_account);
		mediator.add(m_hist);
		mediator.add(m_openOrder);
		mediator.add(m_openOrder);
		mediator.add(m_orderSend);
		mediator.add(m_OrdeStatus);
		mediator.add(m_optionChain);
		
		this.symbol = symbol;
		
	}
	
	public A_MyOption(String symbol, DayOfWeek dayOfWeek){
//		mediator.add(m_position, m_tick);
//		m_position.send("test");
		
		
		this.dayOfWeek = dayOfWeek;
		this.symbol = symbol;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public void setOptionContract(Contract contract) {
		if(contract.right() == Right.Call) {
			this.callContract = contract; 
		}
		if(contract.right() == Right.Put) {
			this.putContract = contract; 
		}
		this.stockContract= contract;
		
	}
	
	public Contract getOptionContract() {
		if(this.status == MyStatus.BuyCall) {
			return callContract;
		}
		else if(this.status == MyStatus.BuyPut) {
			return putContract;
		}
		return null;
	}

	public void setStatus(MyStatus status) {
		this.status = status;
	}
	
	public void setAccount(String account, AccountSummaryTag tag, String value, String currency) {
		this.account.account = account;
		this.account.tag = tag;
		this.account.value = value;
		this.account.currency = currency;
		this.account.data.put(tag, value);
	}
	
	public String getAccountValue(AccountSummaryTag tag) {
		
		return this.account.data.get(tag);
	}
	public String getBuyingPower() {
		return getAccountValue(AccountSummaryTag.BuyingPower);
	}
	public String getNetLiquidation() {
		return getAccountValue(AccountSummaryTag.NetLiquidation);
	}
	public void initStockContract() {
		
		stockContract.symbol(this.symbol.toUpperCase() );
		stockContract.currency("USD".toUpperCase() );
		stockContract.secType(SecType.STK);
		stockContract.exchange("SMART".toUpperCase() );
		
		
	}
	public void initOptionContract() {
		
		optionContract.symbol(symbol.toUpperCase() );
		optionContract.currency("USD".toUpperCase() );
		optionContract.secType(SecType.OPT);
		optionContract.exchange("SMART".toUpperCase() );
		System.out.println("getLastDateOption() >>> "+getLastDateOption());
		optionContract.lastTradeDateOrContractMonth(getLastDateOption());
	
		
	}
	
	
	public void setDayOfWeek(DayOfWeek dayOfWeek, boolean step) {
		this.dayOfWeek = dayOfWeek;
		this.step =step;
	}
	public  String getLastDateOption() {
		LocalDate dt = LocalDate.now();   
		// next or previous
		String Friday;
		if(!step) {
			Friday= dt.with(TemporalAdjusters.nextOrSame(dayOfWeek)).toString();
		}
		else {
			Friday = dt.with(TemporalAdjusters.next(dayOfWeek)).toString();
		}
		
		String daySplit[] = Friday.split("-");
		Friday = daySplit[0]+daySplit[1]+daySplit[2];
		System.out.println(">>>>>>>>>>>> " +Friday);
		return Friday;
//		return "20240328";
	}
	
	public void sendOrder(double sig,Contract contract,double posN,double  liveposition,double qty) {
	
	//		double sig = Double.parseDouble(inputSignal.getText());
		System.out.println(" == = == = = \n");
		
	
		OrderSend send = new OrderSend(contract);
		String data = "Stock";
		
//		double posN = Double.parseDouble(inputPos.getText());
//		double liveposition =  Double.parseDouble(inputLivePos.getText());
//		double qty = Double.parseDouble(inputQty.getText());
		System.out.println("Check data = " + data+" signal = "+sig+" posN = " + posN +	
				" liveposition = "+liveposition+ " qty"+qty+"\n");
		
		send.createOrderOption(data, sig, posN, liveposition, qty);
	}
	
	public void sendOrder(double signal) {
		this.sig = signal;
	
		System.out.println(" == SEND ORDER = \n");
		
		if(getPos() == 0.0) {
			//buy call
			if(sig == 1.0) {
				OrderSend send = new OrderSend(optionchain.getCallATMContract());
				String data = "Stock";
			
				double liveposition = 0;
				send.createOrderOption(data, signal, getPos(), liveposition, getQty());
			}
			
		}
		if(getPos() > 0) {
			if(sig == -1.0){
				System.out.println("NEDD TO SELLLL");

				System.out.println(this.position.getPositionContract()); 
				OrderSend send = new OrderSend(this.position.getPositionContract());
				String data = "Stock";
////			
				double liveposition = 0;
				send.createOrderOption(data, signal, getPos(), liveposition, getPos());
			}
		}
		
	}
	public double getQty() {
		
		
		double buyingPower = Double.parseDouble(getBuyingPower());
		double percent = 0.01;
		double optionPrice = 200;
//		
		double risk = buyingPower*percent;
//		
		double qty = (int) (risk/optionPrice);

		return qty;
	}
	
	public double getPos() {
			return position.getPosition();
		}
	
	
	
		////
	
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
	
	
	public void processSignal(double sig) {
		
		System.out.println("Process Order ");
//		boolean canOpenOrder =this.myOption.canOpenOrder;
//		double pos = this.myOption.pos;
		
//		double qty = this.myOption.getQty();
		double qty = 2.0;
		System.out.println("CanOpenOrder " +canOpenOrder+" pos"+pos+" sig" + sig);
		if(sig == 0.0 ) {
			System.out.println("     Wait...");
		}
		else if(canOpenOrder ) {
			System.out.println("   Can order process signal...");
			
			
			if( sig == 1.0  ) {
				System.out.println("   Sig 1 ");
				if(pos ==0 ) {
					System.out.println("   pos 0 ");
					canOpenOrder = false;
					Contract contract  = optionchain.getCallATMContract();
					System.out.println("       Buy call...");
					
					OrderSend send = new OrderSend(contract);		
					placeOrder(contract, OrderATS.buyMarket(qty));
//					MyPlaceOrder p = new MyPlaceOrder();
//					p.placeOrder(contract, OrderATS.buyMarket(qty));
				}
				else if(pos != 0  ) {
					System.out.println("     pos != 0 ");
					Contract contract  = position.getPositionContract();
					if(contract.right().equals(Right.Put)) {
						canOpenOrder = false;
						
						System.out.println("     Sell put...");
						
						OrderSend send = new OrderSend(contract);	
						placeOrder(contract, OrderATS.sellMarket(qty));
//						MyPlaceOrder p = new MyPlaceOrder();
//						p.placeOrder(contract, OrderATS.sellMarket(qty));
					}	
				}
			}
			if(sig == -1.0 ) {
				System.out.println("   Sig 1 ");
				if(pos != 0  ) {
					System.out.println("   pos != 0 ");
					System.out.println("SELL");
					
					Contract contract  = position.getPositionContract();
					System.out.println(contract);
					if(contract.right().equals(Right.Call)) {
						
						
						canOpenOrder = false;
						System.out.println("     Sell Call...");
						
						OrderSend send = new OrderSend(contract);	
						placeOrder(contract, OrderATS.sellMarket(qty));
//						MyPlaceOrder p = new MyPlaceOrder();
						
//						p.placeOrder(contract, OrderATS.sellMarket(qty));
					}	
				}
				else if(pos ==0 ) {
					System.out.println("   pos 0 ");
					canOpenOrder = false;
					Contract contract  = optionchain.getPutATMContract();
					System.out.println("     Buy put...");
					
					OrderSend send = new OrderSend(contract);
					placeOrder(contract, OrderATS.buyMarket(qty));
//					MyPlaceOrder p = new MyPlaceOrder();
//					p.placeOrder(contract, OrderATS.buyMarket(qty));
					
					
				}
			}
		}
	}
	@Override
	public void orderState(OrderState orderState) {
		// TODO Auto-generated method stub
	//	System.out.println("orderState >>> " + orderState.toString());
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
		System.out.println("Get order  MyOption" + " filled: " + filled +" " + "remaining: " + remaining );
//		System.out.println("status: " + status + "\n" + "filled: " + filled + "\n" + "remaining: " + remaining + "\n"
//				+ "avgFillPrice:" + avgFillPrice + "\n" + "permId: " + permId + "\n" + "parentId: " + parentId + "\n"
//				+ "lastFillPrice: " + lastFillPrice + "\n" + "clientId: " + clientId + "\n" + "whyHeld: " + whyHeld
//				+ "\n" + "mktCapPrice: " + mktCapPrice + "\n");
//		
		if(Double.parseDouble(remaining.toString()) == 0) {
			this.pos = Double.parseDouble(filled.toString());
			this.canOpenOrder = true;
//			System.out.println("End order");
			
			
			
		}
		
		
	}

}





