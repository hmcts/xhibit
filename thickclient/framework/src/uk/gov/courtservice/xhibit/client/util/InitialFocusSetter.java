package uk.gov.courtservice.xhibit.client.util;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class InitialFocusSetter {
	public static void setInitialFocus(Window w, Component c) {
		w.addWindowListener(new FocusSetter(c));
	}
}