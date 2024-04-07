package apitest;

import java.util.HashMap;
import java.util.List;

import com.ib.client.Contract;

public abstract class A_Tick {
	protected A_Mediator mediator;
	protected String name;
	
	
	public A_Tick(A_Mediator med, String name){
		this.mediator=med;
		this.name=name;
	
	}
	

	public abstract void send( String msg, A_Tick s);
	
	public abstract void receive(String msg);
	public abstract void reqTick(Contract contract);
	public abstract Object getData();

}
