package apitest;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ib.client.Contract;
import com.ib.client.Types.BarSize;
import com.ib.client.Types.DurationUnit;
import com.ib.client.Types.SecType;
import com.ib.client.Types.WhatToShow;

import apidemo.util.TCombo;
import apidemo.util.UpperField;
import apidemo.util.VerticalPanel;

public class BarDetailPanel extends JPanel {
//	String symbol = "EUR/USD";
//	JTextField txt_symbol = new JTextField(symbol);
//	TCombo <String> symbol_list = new  TCombo<String>();
	JTextField duration = new JTextField("2");
	TCombo<BarSize> barSize = new TCombo<>(BarSize.values());
	TCombo<DurationUnit> durationUnit = new TCombo<>(DurationUnit.values());
	TCombo<WhatToShow> whatToShow = new TCombo<>(WhatToShow.values());

	BarDetailPanel() {
//		setBarDetailPanel("20",DurationUnit.DAY,BarSize._1_day,WhatToShow.MIDPOINT);
//		this.duration.setText(duration);
		this.durationUnit.setSelectedItem(DurationUnit.DAY);
		this.barSize.setSelectedItem(BarSize._15_mins);
		this.whatToShow.setSelectedItem(WhatToShow.MIDPOINT);
		
		VerticalPanel p = new VerticalPanel();
    	p.add( "Duration", this.duration);
    	p.add( "DurationUnit", this.durationUnit);
    	p.add( "BarSize", this.barSize);    	
    	p.add( "WhatToShow", this.whatToShow);
    	
    	
    	setLayout( new BorderLayout() );
    	add( p);
	
	}

	BarDetailPanel(String duration, DurationUnit durationUnit, BarSize barSize, WhatToShow whatToShow) {
//		setBarDetailPanel(duration,durationUnit,barSize,whatToShow);
		this.duration.setText(duration.toString());
		this.durationUnit.setSelectedItem( durationUnit);
		this.barSize.setSelectedItem(barSize);
		this.whatToShow.setSelectedItem(whatToShow);
		
		VerticalPanel p = new VerticalPanel();
    	p.add( "Duration", this.duration);
    	p.add( "DurationUnit", this.durationUnit);
    	p.add( "BarSize", this.barSize);    	
    	p.add( "WhatToShow", this.whatToShow);
    	
    	
    	setLayout( new BorderLayout() );
    	add( p);
	}

	private void setBarDetailPanel(String duration, DurationUnit day, BarSize day2, WhatToShow midpoint) {
		this.duration.setText(duration);
		this.durationUnit.setSelectedItem(durationUnit);
		this.barSize.setSelectedItem(barSize);
		this.whatToShow.setSelectedItem(whatToShow);
		
		VerticalPanel p = new VerticalPanel();
    	p.add( "Duration", this.duration);
    	p.add( "DurationUnit", this.durationUnit);
    	p.add( "BarSize", this.barSize);    	
    	p.add( "WhatToShow", this.whatToShow);
    	
    	
    	setLayout( new BorderLayout() );
    	add( p);
		
	}



	

	public void setBarDetail(String duration, BarSize barSize) {
		this.duration.setText(duration);
		this.barSize.setSelectedItem(barSize);

	}

	@Override
	public Dimension getMaximumSize() {
		return super.getPreferredSize();
	}

}
