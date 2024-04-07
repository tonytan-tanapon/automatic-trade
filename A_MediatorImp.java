package apitest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.ib.client.Bar;
import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.Types.Right;
import com.ib.controller.AccountSummaryTag;

public class A_MediatorImp implements A_Mediator {

	private A_Position position;
	private A_Tick tick;
	private A_Gui gui;
	private A_Account account;
	private A_Historical historical;
	private A_OrderSend orderSend;
	private A_OrderStatus OrderStatus;
	private A_Control control;
	private A_OptionChain optionChain;
	private A_Indicator indicator;
	private A_OpenOrder openOrder;
	private A_LiveOrder liveOrder;
	


//	HashMap<String , String > d_tick = new HashMap<String, String>();
//	HashMap<Contract, List<Object>> d_position = new HashMap<Contract, List<Object>>();

	A_Detail detail;

	public A_MediatorImp() {

	}
	@Override
	public void start(A_Detail detail) {
		this.detail = detail;
		HashMap<String, String> msg_account = new HashMap<String, String>();
		msg_account.put("cmd", A_MSG.Tick);
		msg_account.put("symbol", detail.symbol);

		System.out.println("Start");
		this.position.receive(A_MSG.Req);
		this.account.receive(A_MSG.Req);
		this.tick.receive(msg_account.toString());
		this.historical.receive(A_MSG.Req);
		this.liveOrder.receive("");

//		this.OrderStatus.receive("");
//		orderSend.receive("buycall", detail);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("symbol", detail.symbol);
//		map.put("date", detail.dateFirday);
		map.put("date", "20240405");
		this.optionChain.receive(map.toString());
//		processSignal();

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}
	
	public void processSignal() {

//		System.out.println("Bid:" + detail.bid + " " + "CALL:" + detail.callATM + " PUT: " + detail.putATM);
//		System.out.println("symbol: " + detail.symbol + " " +
//				"qty: " + detail.qty + " " + "pos: " + detail.pos + " " + "signal: " + detail.signal + " "
//				+ "accountCheck: " + detail.accountState + " " + "positionCheck: " + detail.positionState + " "
//				+ "optionState:" + detail.optionState + " ");

		if (detail.positionState && detail.accountState && detail.optionState) {
			if (detail.pos == 0.0) {
				// buy to open
				if (detail.signal == 1.0) {
					System.out.println(" buy call");
					// buy call
					detail.positionState = false;
					detail.callATM = A_MSG.getATM(detail.bidTick, detail.strikeList, Right.Call);
					orderSend.receive("buycall", detail);

				} else if (detail.signal == -1.0) {
					// buy put
					System.out.println(" buy put");
					detail.positionState = false;
					detail.putATM = A_MSG.getATM(detail.bidTick, detail.strikeList, Right.Put);
					orderSend.receive("buyput", detail);
				}
			} else if (detail.pos > 0.0) {
				if (detail.signal == 1.0 && detail.ContractPosition.right().equals(Right.Put)) {
					System.out.println(" sellput");
					detail.positionState = false;
					orderSend.receive("sellput", detail);

				} else if (detail.signal == -1.0 && detail.ContractPosition.right().equals(Right.Call)) {
					System.out.println(" sellcall");
					detail.positionState = false;
					orderSend.receive("sellcall", detail);

				}
			}

		}

	}
	
	@Override
	public void sendMessage(String msg, A_Tick tick) {
		// TODO Auto-generated method stub
		// === IN ===
		detail.d_tick = (HashMap<String, String>) tick.getData();
		
		detail.dateTick = detail.d_tick.get("time");
		detail.bidTick = Double.parseDouble(detail.d_tick.get("bid"));
		detail.askTick = Double.parseDouble(detail.d_tick.get("ask"));

		processSignal();
		
		
		/// === OUT ===
		gui.receive("Tick ", tick);				

	}

	@Override
	public void sendMessage(String msg, A_Control control) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMessage(String msg, A_Historical hist) {
		// TODO Auto-generated method stub
		// === IN ===
		ArrayList<Bar> d_hist = (ArrayList<Bar>) hist.getData();

		// === OUT ===
	}

	@Override
	public void sendMessage(String msg, A_OptionChain option) {
		// TODO Auto-generated method stub
		// === IN === 
		detail.optionState = true;
		detail.strikeList = (ArrayList<Double>) option.strikeList();
		detail.callData = (HashMap<Double, Contract>) option.getCallData();
		detail.putData = (HashMap<Double, Contract>) option.getPutData();
		
		/// === OUT ===
	}

	@Override
	public void sendMessage(String msg, A_Position position) {
		
		// TODO Auto-generated method stub
		// === IN ===  set position
		System.out.println(msg);
		detail.positionState = true;

		detail.d_position = (HashMap<Contract, List<Object>>) position.getData();
		detail.d_position.forEach((key, value) -> processPosition(key, value));

		// === OUT ===
		gui.receive("position", position);

	}

	public void processPosition(Contract key, List<Object> value) {
		if (Double.parseDouble(value.get(0).toString()) != 0) {
			if (key.symbol().equals(detail.symbol)) {
				detail.pos = Double.parseDouble(value.get(0).toString());
				detail.ContractPosition = key;
				detail.ContractPosition.exchange("SMART");
			}
		} 
	}

	@Override
	public void sendMessage(String msg, A_Account acc) {
		// TODO Auto-generated method stub
		// === IN === 
		HashMap<AccountSummaryTag, String> data = (HashMap<AccountSummaryTag, String>) acc.getData();
		detail.d_buyingPower = Double.parseDouble(data.get(AccountSummaryTag.BuyingPower));
		detail.d_netLiquidation = Double.parseDouble(data.get(AccountSummaryTag.NetLiquidation));
		detail.accountState = true;
		System.out.println(detail.d_buyingPower + " " + detail.d_netLiquidation);

		// === OUT ===
		gui.receive(msg, acc);
	
	}
	@Override
	public void sendMessage(String msg, A_LiveOrder liveOrder) {
		// TODO Auto-generated method stub
		// === IN === 
		if(position.getTabelData().getRowCount() != 0) {
			detail.pos = Double.parseDouble(""+position.getTabelData().getValueAt(0, 1));	
		}else {
			detail.pos = 0;
		}
		
		// === OUT ===
		this.position.receive(A_MSG.Req);
		this.account.receive(A_MSG.Req);
	}
	@Override
	public void sendMessage(String msg, A_OrderStatus orderStatus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMessage(String msg, A_Indicator indicator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMessage(String msg, A_OpenOrder openOrder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMessage(String msg, A_OrderSend openSend) {
		// TODO Auto-generated method stub
//		System.out.println("HELLOOOOOOOOOOOOOOOOOOOO");

		detail.positionState = true;

		this.position.receive(A_MSG.Req);
	}

	@Override
	public void sendMessage(String msg, A_Gui gui) {
		// TODO Auto-generated method stub
//		System.out.println("============= "+msg);
		if (msg.equals("stop")) {
			tick.receive(A_MSG.Stop);
		} else {
//			System.out.println("msg GUI" + msg);
			detail.signal = Double.parseDouble(msg);
		}

	}

	@Override
	public void add(A_Control control) {
		// TODO Auto-generated method stub
		this.control = control;
	}

	@Override
	public void add(A_Tick tick) {
		// TODO Auto-generated method stub
		this.tick = tick;
	}

	@Override
	public void add(A_Historical hist) {
		// TODO Auto-generated method stub
		this.historical = hist;
	}

	@Override
	public void add(A_OptionChain optionChain) {
		// TODO Auto-generated method stub
		this.optionChain = optionChain;
	}

	@Override
	public void add(A_Position position) {
		// TODO Auto-generated method stub
		this.position = position;
	}

	@Override
	public void add(A_Account acc) {
		// TODO Auto-generated method stub
		this.account = acc;
	}

	@Override
	public void add(A_OrderStatus orderStatus) {
		// TODO Auto-generated method stub
		this.OrderStatus = orderStatus;
	}

	@Override
	public void add(A_Indicator indicator) {
		// TODO Auto-generated method stub
		this.indicator = indicator;
	}

	@Override
	public void add(A_OpenOrder openOrder) {
		// TODO Auto-generated method stub
		this.openOrder = openOrder;
	}

	@Override
	public void add(A_OrderSend openSend) {
		// TODO Auto-generated method stub
		this.orderSend = openSend;

//		HashMap <String, String> data = new HashMap<String, String>();
//		data.put("symbol", stockSymbol);
//		data.put("date", dateFriday);
//		openSend.receive(data.toString());
	}

	@Override
	public void add(A_Gui gui) {
		// TODO Auto-generated method stub
		this.gui = gui;
	}



	@Override
	public void add(A_LiveOrder liveOrder) {
		// TODO Auto-generated method stub
		this.liveOrder = liveOrder;
	}

}
