package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;


public class PrintNotification extends JDialog{
	
	private static final long serialVersionUID = 1L;
	
	// Buttons
	private JButton btnPrintNotifications;
	private JButton btnClose;
	
	// Labels
	private JLabel lblSuccessMessage1;
	private JLabel lblSuccessMessage2;
	private JLabel lblSuccessMessage3;

	public PrintNotification() {
		setSize(new Dimension(300, 200));
		setPreferredSize(new Dimension(300, 200));
		setResizable(false);
		setModal(true);
		setTitle("Case Transferred");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		// Labels
		lblSuccessMessage1 = new JLabel("Case transferred successfully.");
		lblSuccessMessage2 = new JLabel("Please click 'Print Notifcations' to preview");
		lblSuccessMessage3 = new JLabel("and print the Transfer Notifications");
		
		lblSuccessMessage1.setBounds(60, 20, 200, 23);
		lblSuccessMessage2.setBounds(30, 50, 250, 23);
		lblSuccessMessage3.setBounds(53, 80, 200, 23);
		
		lblSuccessMessage1.setVisible(true);
		lblSuccessMessage2.setVisible(true);
		lblSuccessMessage3.setVisible(true);
		
		getContentPane().add(lblSuccessMessage1);
		getContentPane().add(lblSuccessMessage2);
		getContentPane().add(lblSuccessMessage3);
		
		// Buttons
		btnClose = new JButton("Close");
		btnClose.setBounds(190, 140, 89, 23);
		btnClose.addActionListener(new CloseButtonActionListener());
		btnClose.setEnabled(true);
		getContentPane().add(btnClose);	
		
		btnPrintNotifications = new JButton("Print Notifications");
		btnPrintNotifications.setBounds(10, 140, 140, 23);
		btnPrintNotifications.addActionListener(new PrintNotificationsButtonActionListener());
		btnPrintNotifications.setEnabled(true);
		getContentPane().add(btnPrintNotifications);
	}
	
	// Custom action listeners for close and print notification buttons
	private class CloseButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();	// close down window
		} 
	}	
	
	private class PrintNotificationsButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Empty method, print notifications not spec'ed at time of writing
		}
	}
}