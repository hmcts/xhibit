package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * <p>
 * Title: CaseMigratedPopup
 * </p>
 * <p>
 * Description: A popup to display on searches for cases that are migrated to prevent
 * further actions taking place on them.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2026
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Owain Greener
 * @version 1.0
 */
public class CaseMigratedPopup extends JDialog {

	private static final long serialVersionUID = 1L;

	public CaseMigratedPopup(Frame owner, String message) {
		super(owner, "Case Migrated", true);
		init(message);
	}
	
	private void init(String message) {
		JPanel content = new JPanel(new BorderLayout(10, 10));
		content.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		JLabel label = new JLabel(message);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		content.add(label, BorderLayout.CENTER);
		
		JButton okBtn = new JButton("OK");
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okBtn);
		content.add(buttonPanel, BorderLayout.SOUTH);
		
		setContentPane(content);
		pack();
		setLocationRelativeTo(getParent());
	}
}