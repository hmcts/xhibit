package uk.gov.courtservice.xhibit.client.casemanagement;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class OkCancelPopup extends JFrame{
	public OkCancelPopup() {
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 301, 95);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.setBounds(160, 37, 100, 23);
		panel.add(btnNewButton_1);
		
		JButton btnNewButton = new JButton("OK");
		btnNewButton.setBounds(20, 37, 107, 23);
		panel.add(btnNewButton);
	}
}
