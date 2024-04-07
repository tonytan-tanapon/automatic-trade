package apitest;

import com.ib.client.Contract;

public abstract class A_OrderSend {
	protected A_Mediator mediator;
	protected String name;
	public A_OrderSend(A_Mediator med, String name){
		this.mediator=med;
		this.name=name;
	
	}
	
	public abstract void send(String msg,Object data);
	
	public abstract void receive(String msg, A_Detail detail);
	
	public abstract void reqOrderSend();
	
	public abstract Object getData();
}
