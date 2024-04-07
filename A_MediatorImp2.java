package apitest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ib.client.Contract;



public class A_MediatorImp2 implements A_Mediator{
	private A_Position position;
	private A_Tick tick;
	private A_Gui gui;
	
	String stockSymbol= "AAPL";
	String dateFriday = "20240405";
	
	double sig =0.0;
	double bid  = 0.0;
	
	public A_MediatorImp2() {
//		this.positions=new ArrayList<>();
//		this.ticks=new ArrayList<>();
	}
	@Override
	public void add(A_Control control) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessage(String msg, A_Control control) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessage(String msg, A_Position position) {
		// TODO Auto-generated method stub
		gui.receive("position 1234 ", position);
//		tick.receive("tick");
	}


	@Override
	public void sendMessage(String msg, A_Tick tick) {
		// TODO Auto-generated method stub
		gui.receive("Tick ", tick);
		
		//recive tick 
		
		
		
	}

	@Override
	public void sendMessage(String msg, A_Gui gui) {
		// TODO Auto-generated method stub
		
		tick.receive(msg);
//		gui.receive("Hello", position);
		
	}

	@Override
	public void add(A_Position position) {
		// TODO Auto-generated method stub
		this.position = position;
	}

	@Override
	public void add(A_Tick tick) {
		// TODO Auto-generated method stub
		this.tick = tick;
	}

	@Override
	public void add(A_Gui gui) {
		// TODO Auto-generated method stub
		this.gui = gui;
	}

	@Override
	public void add(A_Historical hist) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessage(String msg, A_Historical hist) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(A_OrderStatus orderStatus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessage(String msg, A_OrderStatus orderStatus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(A_Account acc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessage(String msg, A_Account acc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(A_Indicator indicator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessage(String msg, A_Indicator indicator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(A_OpenOrder openOrder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessage(String msg, A_OpenOrder openOrder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(A_OrderSend openSend) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessage(String msg, A_OrderSend openSend) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(A_OptionChain openSend) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessage(String msg, A_OptionChain openSend) {
		// TODO Auto-generated method stub
		HashMap <String, String> data = new HashMap<String, String>();
		data.put("symbol", stockSymbol);
		data.put("date", dateFriday);
		openSend.receive(data.toString());
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void start(A_Detail detail) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void sendMessage(String msg, A_LiveOrder liveOrder) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void add(A_LiveOrder liveOrder) {
		// TODO Auto-generated method stub
		
	}

	
	
}
