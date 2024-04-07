package apitest;

public abstract class A_Gui {
	protected A_Mediator mediator;
	protected String name;
	public A_Gui(A_Mediator med, String name){
		this.mediator=med;
		this.name=name;
	
	}

	public abstract void send(String msg);
	public abstract void receive(String msg, A_Position postion);
	public abstract void receive(String msg, A_Tick tick);
	public abstract void receive(String msg, A_Historical hist);
	public abstract void receive(String msg, A_OpenOrder openOrder);
	public abstract void receive(String msg, A_OrderStatus orderStatus);
	public abstract void receive(String msg, A_Account account);
	public abstract void receive(String msg, A_Indicator indicator);
}
