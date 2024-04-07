package apitest;

public abstract class A_OptionChain {
	protected A_Mediator mediator;
	protected String name;
	public A_OptionChain(A_Mediator med, String name){
		this.mediator=med;
		this.name=name;
	
	}
	
	public abstract void send(String msg,Object data);
	
	public abstract void receive(String msg);
	
	public abstract void reqOptionChain();
	
	public abstract Object strikeList();
	public abstract Object getCallData();
	public abstract Object getPutData();
}
