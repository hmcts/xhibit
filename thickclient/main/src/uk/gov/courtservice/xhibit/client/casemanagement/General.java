package uk.gov.courtservice.xhibit.client.casemanagement;

import javax.swing.*;

public class General extends JPanel {
	private static final long serialVersionUID = 1L;

	public General() {
		JTextPane txtpnGeneralPanel = new JTextPane();
		txtpnGeneralPanel.setText("General Panel");
		add(txtpnGeneralPanel);
	}
}
