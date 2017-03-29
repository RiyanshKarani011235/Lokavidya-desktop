package CustomComponents;

import javax.swing.JButton;

public class LagJButton extends JButton{

	public LagJButton(String text) {
		super(text);
		setMultiClickThreshhold(1000);
	}
	
}
