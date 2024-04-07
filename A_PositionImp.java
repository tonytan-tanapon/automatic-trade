package apitest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.controller.ApiController.IPositionHandler;

public class A_PositionImp extends A_Position implements IPositionHandler {
    HashMap<Contract,List<Object> > data = new HashMap<Contract, List<Object>>();
	
    
    String[] columnNames = {"Contract","pos","avgCost"};
	DefaultTableModel tableModel= new DefaultTableModel(); 

	public A_PositionImp(A_Mediator med, String name) {
		super(med, name);
		
		for(int i=0;i<columnNames.length ;i++) {
			tableModel.addColumn(columnNames[i]);
		}
	}
	
	
	public void setPosition(String account, Contract contract, Decimal pos, double avgCost) {
		
	
		
	}
	public void resetTable() {
		if (tableModel.getRowCount() > 0) {
		    for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
		    	tableModel.removeRow(i);
		    }
		}
	}
	@Override
	public void reqPostion() 
	{	
		
		resetTable();
		data.clear();
		ApiDemo.INSTANCE.controller().reqPositions(this);
	}

	@Override
	public void send(String msg,Object data) {
		// TODO Auto-generated method stub
//		System.out.println(this.name+": Sending Message="+msg);
		mediator.sendMessage(msg, this);
	}
	
	@Override
	public void receive(String msg) {
//		System.out.println(this.name+": Received Message:"+msg);
		if(msg.equals(A_MSG.Req)) {
			reqPostion();
		}
	}
	
	@Override
	public void position(String account, Contract contract, Decimal pos, double avgCost) {
		// TODO Auto-generated method stub
	
		if(Double.parseDouble(""+pos) > 0.0 ) {
			tableModel.insertRow(tableModel.getRowCount(), new Object[] { contract,pos,avgCost });
		}
		
		List<Object> list =  new ArrayList<>();
		list.add(pos);
		list.add(avgCost);
		
		data.put(contract, list);
		
		
		
	}

	@Override
	public void positionEnd() {
		ApiDemo.INSTANCE.controller().cancelPositions(this);
	
		// TODO Auto-generated method stub
	
//		Contract a = (Contract) tableModel.getValueAt(0, 0);
//		System.out.println(a);
//		data.forEach(
//	            (key, value)-> System.out.println("Contract:" +key.localSymbol()+
//	            		" [Pos, AvgCost]:" + value) );
//		
	
		send("Postion",this);
	}

	@Override
	public Object  getData() {
		// TODO Auto-generated method stub
		
		return data;
	}
	@Override
	public DefaultTableModel getTabelData() {
		// TODO Auto-generated method stub
		
		return tableModel;
	}

	



	

}
