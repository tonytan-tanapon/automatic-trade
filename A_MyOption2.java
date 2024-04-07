package apitest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import com.ib.client.Contract;
import com.ib.client.Types.Right;
import com.ib.client.Types.SecType;
import com.ib.controller.AccountSummaryTag;

public class A_MyOption2 {
	Contract callContract = new Contract();
	Contract putContract = new Contract(); 
	Contract stockContract = new Contract();
	Contract optionContract = new Contract();
	MyStatus status =MyStatus.None;
	MyAccount account = new MyAccount();
	MyLiveOrder liveorder = new MyLiveOrder();
	MyHistorical historical = new MyHistorical();
	MyOptionChain optionchain = new MyOptionChain();
	MyPosition position = new MyPosition();
//	MySendOrder sendOrder = new MySendOrder();
	String symbol = "AAPL";
	String lastTradeDate = "20240328";
	
	double sig=0.0;
	A_MyOption2(String symbol){
		this.symbol = symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public void setOptionContract(Contract contract) {
		if(contract.right() == Right.Call) {
			this.callContract = contract; 
		}
		if(contract.right() == Right.Put) {
			this.putContract = contract; 
		}
		this.stockContract= contract;
		
	}
	public Contract getOptionContract() {
		if(this.status == MyStatus.BuyCall) {
			return callContract;
		}
		else if(this.status == MyStatus.BuyPut) {
			return putContract;
		}
		return null;
	}
	
	public void setStatus(MyStatus status) {
		this.status = status;
	}
	
	public void setAccount(String account, AccountSummaryTag tag, String value, String currency) {
		this.account.account = account;
		this.account.tag = tag;
		this.account.value = value;
		this.account.currency = currency;
		this.account.data.put(tag, value);
	}
	
	public String getAccountValue(AccountSummaryTag tag) {
		return this.account.data.get(tag);
	}
	public void initStockContract() {
		
		stockContract.symbol(this.symbol.toUpperCase() );
		stockContract.currency("USD".toUpperCase() );
		stockContract.secType(SecType.STK);
		stockContract.exchange("SMART".toUpperCase() );
		
		
	}
	public void initOptionContract() {
		
		optionContract.symbol(symbol.toUpperCase() );
		optionContract.currency("USD".toUpperCase() );
		optionContract.secType(SecType.OPT);
		optionContract.exchange("SMART".toUpperCase() );
		optionContract.lastTradeDateOrContractMonth(getFridayOption());
		optionContract.lastTradeDateOrContractMonth("20240328"); // Thursday 
		
	}
	public  String getFridayOption() {
		LocalDate dt = LocalDate.now();   
		// next or previous
		String Friday = dt.with(TemporalAdjusters.next(DayOfWeek.FRIDAY)).toString();
		String daySplit[] = Friday.split("-");
		Friday = daySplit[0]+daySplit[1]+daySplit[2];
		return Friday;
	}
	
	public void sendOrder(double sig,Contract contract,double posN,double  liveposition,double qty) {
	
	//		double sig = Double.parseDouble(inputSignal.getText());
		System.out.println(" == = == = = \n");
		
	
		OrderSend send = new OrderSend(contract);
		String data = "Stock";
		
//		double posN = Double.parseDouble(inputPos.getText());
//		double liveposition =  Double.parseDouble(inputLivePos.getText());
//		double qty = Double.parseDouble(inputQty.getText());
		System.out.println("Check data = " + data+" signal = "+sig+" posN = " + posN +	
				" liveposition = "+liveposition+ " qty"+qty+"\n");
		
		send.createOrderOption(data, sig, posN, liveposition, qty);
	}
}





