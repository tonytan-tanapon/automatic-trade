package apitest;

import java.util.HashMap;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.ib.client.Contract;

public abstract class A_Position {
	protected A_Mediator mediator;
	protected String name;
	public A_Position(A_Mediator med, String name){
		this.mediator=med;
		this.name=name;
	
	}
	
	public abstract void send(String msg,Object data);
	
	public abstract void receive(String msg);
	
	public abstract void reqPostion();
	
	public abstract Object getData();
	public abstract DefaultTableModel getTabelData();

}
