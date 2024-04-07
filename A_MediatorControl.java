package apitest;

public class A_MediatorControl {
	A_Mediator mediator = new A_MediatorImp();
	A_Position m_position = new A_PositionImp(mediator, "Position");
	A_Tick m_tick = new A_TickImp(mediator, "Tick");
	A_Account m_account = new A_AccountImp(mediator, "Account");
	A_Historical m_hist = new A_HistoricalImp(mediator, "Hist");
	A_OpenOrder m_openOrder = new A_OpenOrderImp(mediator, "OpendOrder");
	A_OrderSend m_orderSend = new A_OrderSendImp(mediator, "OrderSend");
	A_OrderStatus m_OrdeStatus = new A_OrderStatusImp(mediator, "OrderStatus");
	A_OptionChain m_optionChain = new A_OptionChainImp(mediator, "Option Chain");
	A_Control m_control = new A_ControlImp(mediator, "Control");
	A_LiveOrder m_liveOrder = new A_LiveOrderImp(mediator, "Control");
	
	A_MediatorControl(){
		mediator.add(m_position);
		mediator.add(m_tick);
		mediator.add(m_account);
		mediator.add(m_hist);
		mediator.add(m_openOrder);
		mediator.add(m_openOrder);
		mediator.add(m_orderSend);
		mediator.add(m_OrdeStatus);
		mediator.add(m_optionChain);
		mediator.add(m_control);
		mediator.add(m_liveOrder);

	}
	private A_Detail detail = new A_Detail();
	public void start(String symbol, String fridayOpion ) {
		// TODO Auto-generated method stub
		this.detail.symbol = symbol;
		this.detail.fridayOpion = fridayOpion;
		mediator.start(detail);

		
	}
	public void start( ) {
		// TODO Auto-generated method stub
		
		mediator.start(detail);

		
	}
	public A_Mediator getMediator() {
		return mediator;
	}
	
	public void reqPosition() {
		m_position.reqPostion();
	}

	public void reqTick() {
		// TODO Auto-generated method stub
//		m_tick.reqTick();
	}

	public void reqAccount() {
		// TODO Auto-generated method stub
		m_account.reqAccount();
	}

	public void reqHistorical() {
		// TODO Auto-generated method stub
		m_hist.reqHistorical();
	}

	public void reqOrderSend() {
		// TODO Auto-generated method stub
		m_orderSend.reqOrderSend();
	}

	public void reqOptionChain() {
		// TODO Auto-generated method stub
		m_optionChain.reqOptionChain();
	}

	
}
