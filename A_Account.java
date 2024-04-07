package apitest;

import com.ib.controller.ApiController.IAccountSummaryHandler;

public abstract class A_Account implements IAccountSummaryHandler {
	protected A_Mediator mediator;
	protected String name;
	public A_Account(A_Mediator med, String name){
		this.mediator=med;
		this.name=name;
	
	}
	
	public abstract void send(String msg,Object data);
	
	public abstract void receive(String msg);
	
	public abstract void reqAccount();
	
	public abstract Object getData();
}
