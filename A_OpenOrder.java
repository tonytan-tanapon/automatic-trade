package apitest;

public abstract class A_OpenOrder {
	protected A_Mediator mediator;
	protected String name;
	public A_OpenOrder(A_Mediator med, String name){
		this.mediator=med;
		this.name=name;
	
	}
	
	public abstract void send(String msg,Object data);
	
	public abstract void receive(String msg);
	
	public abstract void reqOpendOrder();
	
	public abstract Object getData();
}
