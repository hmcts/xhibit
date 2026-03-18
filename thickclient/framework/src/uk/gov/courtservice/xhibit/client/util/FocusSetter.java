package uk.gov.courtservice.xhibit.client.util;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FocusSetter extends WindowAdapter {
	Component initComp;
	
	FocusSetter(Component c) {
		initComp = c;
	}
	
	public void windowOpened(WindowEvent e) {
		initComp.requestFocus();
		e.getWindow().removeWindowListener(this);
	}
}