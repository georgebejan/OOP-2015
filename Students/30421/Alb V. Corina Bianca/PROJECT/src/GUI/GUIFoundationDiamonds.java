package GUI;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLayeredPane;

public class GUIFoundationDiamonds extends JLayeredPane{
	
	public GUIFoundationDiamonds() {
		
		JButton card = new JButton();
		card.setBackground(Color.GRAY);
    	card.setBounds(10, 0, 90, 120);
    	add(card);
		
	}

}
