package uk.gov.courtservice.xhibit.client.casemanagement.util;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.regex.Pattern;

import javax.swing.JLabel;

import uk.gov.courtservice.xhibit.client.casemanagement.CaseXPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;

public class NoOfDefendantsListener implements FocusListener {

	private CaseXPanel parent;
	private JLabel errorLabel;
	
	public NoOfDefendantsListener(CaseXPanel parent, JLabel errorLabel) {
		this.parent = parent;
		this.errorLabel = errorLabel;
	}
	@Override
	public void focusGained(FocusEvent e) {
		
	}

	@Override
	public void focusLost(FocusEvent e)  {
		int count = 0;
		XTextField txtNoOfDefendants = (XTextField)e.getSource();
		if (!txtNoOfDefendants.isNullOrEmpty()) {
			if (Pattern.matches("[0-9]+", txtNoOfDefendants.getText())) {
				count = Integer.parseInt(txtNoOfDefendants.getText());

				// ctx-1861: handle changes to num defendants
				// - check if entry parses
				// - check if lower than that in defendant tab
				// if it passes these then set error label text to " "
				if (parent.getCurrentNoOfDefendants() > 0 && parent.getCurrentNoOfDefendants() > count) {
					errorLabel.setText("Must be at least equal to no. defendants on the case");
				} else {
					errorLabel.setText(" ");
				}
			} else {
				errorLabel.setText("Invalid Entry");
			}
		} else {
			errorLabel.setText("Mandatory Field");
		}
		parent.defendantCountResponder(count);
	}

}
