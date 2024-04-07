package apitest;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.border.TitledBorder;

import com.ib.client.Contract;
import com.ib.client.Types.BarSize;
import com.ib.client.Types.SecType;

import apidemo.util.VerticalPanel;
import apidemo.util.NewTabbedPanel.INewTab;
import apidemo.util.VerticalPanel.HorzPanel;
import apidemo.util.VerticalPanel.StackPanel;

public class BackTestPanel extends StackPanel implements INewTab {
	
	public  Contract m_contract = new Contract();
	public  ContractPanel m_contractPanel = new ContractPanel(m_contract);
	public BarDetailPanel m_barDetailPanel = new BarDetailPanel();
//	BackTestData backData = new BackTestData(this);
//	PanelData backPanel = new PanelData(backData.data);
	
	BackTestDataAPI backData = new BackTestDataAPI(this);
	PanelData backPanel = new PanelData(backData.data);
	
	BackTestResult backTestResult = new BackTestResult();
	PanelData resultPanel = new PanelData(backTestResult);
//	final private List<Bar> m_rows = new ArrayList<>();
//	final public Chart m_chart = new Chart(m_rows);

	JButton btn_start = new JButton("Start");
	JButton btn_stop = new JButton("Stop");


	
	InputText m_pl = new InputText("PL", "0");
	InputText m_qty = new InputText("QTY","0");
	InputText m_traget = new InputText("Traget","");
	InputText m_stop = new InputText("Stop", "");
//	String tradeType= "";
	String buytype[]={"Simple", "Dynamic", "Bracket"};        
    JComboBox buysellType=new JComboBox(buytype);
	
	

	private static Component sp(int n) {
		return Box.createHorizontalStrut(n);
	}
	
	
	BackTestPanel( String symbol, SecType sectype, double qty, double traget, double stop, String day, BarSize barsize) {

		this.m_qty.setText(""+qty);
		this.m_traget.setText(""+traget);
		this.m_stop.setText(""+stop);
		m_contractPanel.setContract(symbol, sectype);
		m_barDetailPanel.setBarDetail(day, barsize);
		
		btn_start.setEnabled(true);
		btn_stop.setEnabled(false);
		
	
		HorzPanel buttonPanel = new HorzPanel();
		buttonPanel.add(btn_start);		
		buttonPanel.add(btn_stop);
		buttonPanel.add(m_pl);
		buttonPanel.add(m_qty);
		buttonPanel.add(m_traget);
		buttonPanel.add(m_stop);
		
		
		VerticalPanel verti_barsig = new VerticalPanel();
		verti_barsig.add(buttonPanel);

		StackPanel leftTop = new StackPanel();
		leftTop.setBorder(new TitledBorder("Buy sell type"));
		leftTop.add(buysellType);
		
		StackPanel leftPanel = new StackPanel();
		leftPanel.setBorder(new TitledBorder("Define Contract"));
		
		leftPanel.add(m_contractPanel);
		leftPanel.add(m_barDetailPanel);
		
		StackPanel rightPanel = new StackPanel();
		rightPanel.setBorder(new TitledBorder("Information"));
		
		
		rightPanel.add(verti_barsig);
		rightPanel.add(resultPanel.getScrollPane());
		rightPanel.add(backPanel.getScrollPane());
		
		
		HorzPanel horzPanel = new HorzPanel();
		VerticalPanel ver_main = new VerticalPanel();
		ver_main.add(leftTop);
		ver_main.add(leftPanel);
		horzPanel.add(ver_main);
		horzPanel.add(rightPanel);
		
		add(horzPanel);

		btn_start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
//				btn_start.setEnabled(false);
				btn_stop.setEnabled(true);
				onStart();

			}
		});
		btn_stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btn_start.setEnabled(true);
				btn_stop.setEnabled(false);
				onStop();
			}
		});
	}

	protected void onStart() {
		m_contractPanel.onOK();
		System.out.println("Back test");	
		backData.reqHist();
	
	}

	protected void onStop() {
		
	}
	
	@Override
	public void activated() {
	// when tab is activated
		m_contractPanel.onOK();
		
		
//		accountPanel = new PanelData(apitest.ApiDemo.INSTANCE.accountModel);
//		Contract m = m_contractPanel.getContract();
	}

	@Override
	public void closed() {
		// TODO Auto-generated method stub

	}

}