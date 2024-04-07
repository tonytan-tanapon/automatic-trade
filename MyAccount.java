package apitest;

import java.util.HashMap;

import com.ib.client.Contract;
import com.ib.controller.AccountSummaryTag;

public class MyAccount {
	HashMap<AccountSummaryTag, String> data = new HashMap<AccountSummaryTag, String>();
	String account;
	AccountSummaryTag tag; 
	String value;
	String currency;
}
