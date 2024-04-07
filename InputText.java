package apitest;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputText extends JPanel{
	String title ="Input";
	String value = "";
	
	JLabel label ;
	JTextField text; 
	InputText(String title, String value){
		this.title = title;
		this.value = value; 
		
		label = new JLabel(this.title);
		text = new JTextField(value, 5);
		
		add(label);
		add(text);
	}
	
	InputText(String title, String value, int lenght){
		this.title = title;
		this.value = value; 
		
		label = new JLabel(this.title);
		text = new JTextField(value, lenght);
		
		add(label);
		add(text);
	}
	
	
	public String getText() {
	
		return text.getText();
	}
	public void setText(String text) {
		value = text;
		this.text.setText(text);
	}
}
