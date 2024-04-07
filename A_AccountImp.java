package apitest;

import java.util.HashMap;

import com.ib.controller.AccountSummaryTag;
import com.ib.controller.ApiController.IAccountSummaryHandler;

public class A_AccountImp extends A_Account {
	HashMap<AccountSummaryTag ,String> data = new HashMap<AccountSummaryTag , String>();
	public A_AccountImp(A_Mediator med, String name) {
		super(med, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void send(String msg, Object data) {
		// TODO Auto-generated method stub
		mediator.sendMessage(msg, this);
	}

	@Override
	public void receive(String msg) {
		// TODO Auto-generated method stub
//		System.out.println(this.name+": Received Message:"+msg);
		if(msg.equals(A_MSG.Req)) {
			reqAccount();

		}
	}

	@Override
	public void reqAccount() {
		// TODO Auto-generated method stub
		data.clear();
		ApiDemo.INSTANCE.controller().reqAccountSummary("All", AccountSummaryTag.values(), this);

	}

	@Override
	public HashMap<AccountSummaryTag ,String> getData() {
		// TODO Auto-generated method stub
		
		return data;
	}

	@Override
	public void accountSummary(String account, AccountSummaryTag tag, String value, String currency) {
		// TODO Auto-generated method stub
//		System.out.println(tag+" "+value);
		data.put(tag, value);
	}

	@Override
	public void accountSummaryEnd() {
		// TODO Auto-generated method stub
		ApiDemo.INSTANCE.controller().cancelAccountSummary(this);
		System.out.println("Buying Power = " + data.get(AccountSummaryTag.BuyingPower));
		System.out.println("NetLiquidation = " + data.get(AccountSummaryTag.NetLiquidation));
//		System.out.println("aaa Cannecl");
		
		HashMap<String, String> map = new HashMap<String, String>(); 
		map.put(AccountSummaryTag.BuyingPower.toString(), data.get(AccountSummaryTag.BuyingPower));
		map.put(AccountSummaryTag.NetLiquidation.toString(), data.get(AccountSummaryTag.NetLiquidation));
		send(map.toString(), data);
		
		
	}

	
}
