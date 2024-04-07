package apitest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.controller.Formats;
import com.ib.controller.ApiController.IPositionHandler;

public class PositionData extends PositionModel implements IPositionHandler {

	
	boolean m_complete;
	AutoTradePanel autoTradePanel;
	double position = 0;
	double average = 0.0;
	
	PositionData(AutoTradePanel autoTradePanel) {
		this.autoTradePanel = autoTradePanel;
	}

	public void reqPosition() {
	
		ApiDemo.INSTANCE.controller().reqPositions(this);
	}
	


	@Override
	public void positionEnd() {
		// TODO Auto-generated method stub		
//		PlaceOrderATS place = new  PlaceOrderATS();
//		place.placeOrder(contract, order);
//		m_model.fireTableDataChanged();
		fireTableDataChanged();
		m_complete = true;
	
		
//		autoTradePanel.m_openOrder.reqOpenOrder();
		
//		autoTradePanel.m_accountsummaryModel.reqAccount();
//		autoTradePanel.m_live.reqLiveOrder();
		
		autoTradePanel.liveOrder.reqLive();
		ApiDemo.INSTANCE.controller().cancelPositions(this);
		System.out.print("Corrected Positon >>> ");
		/// go to reqAccount
	}

	@Override
	public void position(String account, Contract contract, Decimal pos, double avgCost) {
		// TODO Auto-generated method stub
		PositionKey key = new PositionKey(account, contract.conid());
		PositionRow row = m_map.get(key);
		if (row == null) {
			row = new PositionRow();
			m_map.put(key, row);
			m_list.add(row);
		}
		row.update(account, contract, Double.parseDouble(""+pos), avgCost);

		if (m_complete) {
			fireTableDataChanged();
		}
		
		//////////// get position by symbol , check specific position
		if(autoTradePanel.m_contract.symbol().equals(contract.symbol()) && 
				autoTradePanel.m_contract.currency().equals(contract.currency()) &&
				autoTradePanel.m_contract.secType().equals(contract.secType())
				) {
			System.out.println(""+contract.symbol()+"/"+contract.currency()+" "+contract.secType());
			position = Double.parseDouble(""+pos) ;
			average = avgCost;

		}
	}

	

}
