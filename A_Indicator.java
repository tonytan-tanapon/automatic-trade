package apitest;

public abstract class A_Indicator {
	protected A_Mediator mediator;
	protected String name;
	public A_Indicator(A_Mediator med, String name){
		this.mediator=med;
		this.name=name;
	
	}
	
	public abstract void send(String msg,Object data);
	
	public abstract void receive(String msg);
	
	public abstract void reqIndicator();
	
	public abstract Object getData();
}
