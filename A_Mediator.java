package apitest;

import java.util.HashMap;
import java.util.List;

import com.ib.client.Contract;

public interface  A_Mediator {
	


	A_MediatorControl conttol = new A_MediatorControl(); 
	
	public void start(A_Detail detail);
	public void stop();
	
	public void sendMessage(String msg,A_Control control);
	public void sendMessage(String msg, A_Tick tick);
	public void sendMessage(String msg,A_Historical hist);
	public void sendMessage(String msg,A_OptionChain openSend);
	public void sendMessage(String msg, A_Position position);
	public void sendMessage(String msg,A_Account acc);
	public void sendMessage(String msg,A_OrderStatus orderStatus);
	public void sendMessage(String msg,A_Indicator indicator);
	public void sendMessage(String msg,A_OpenOrder openOrder);
	public void sendMessage(String msg,A_OrderSend openSend);
	public void sendMessage(String msg,A_Gui gui);
	public void sendMessage(String msg,A_LiveOrder liveOrder);
	
	public void add(A_Control control);
	public void add(A_Tick tick);
	public void add(A_Historical hist);
	public void add(A_OptionChain optionChain);
	public void add(A_Position position);
	public void add(A_Account acc);	
	public void add(A_OrderStatus orderStatus);	
	public void add(A_Indicator indicator);	
	public void add(A_OpenOrder openOrder);
	public void add(A_OrderSend openSend);	
	public void add(A_Gui gui);
	public void add(A_LiveOrder liveOrder);

	
	

}
