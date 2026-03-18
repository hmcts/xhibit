package uk.gov.courtservice.xhibit.client.util;

import java.awt.event.FocusEvent;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;

import mseries.Calendar.MFieldListener;
import mseries.ui.MChangeEvent;
import mseries.ui.MChangeListener;

/**
 * <p>
 * Title: Xhibit
 * </p>
 * <p>
 * Description: XDate Panel with an event upon date selection or change
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class XDatePanelWithEvent extends XDatePanel {

	private static final long serialVersionUID = 1L;
	
	public XDatePanelWithEvent(JPanel containingPanel, Calendar defaultDate, boolean required) {
		super(containingPanel, defaultDate, required);			
		addListener();
	}

	public XDatePanelWithEvent(JPanel containingPanel, Calendar defaultDate, boolean required, JLabel WarningLabel, String beforeOrAfter) {
		super(containingPanel, defaultDate, required, WarningLabel,	beforeOrAfter);
		addListener();
	}
	
	private void addListener() {
		// Date selector listener (for immediate event)
		this.getDateComponent().addMChangeListener(new MChangeListener() {
			@Override
			public void valueChanged(MChangeEvent e) {
				if (e.getType() == MChangeEvent.PULLDOWN_CLOSED) {
					fireEvent();
				}
			}
		});
		// Tab from field listener (for event upon completion of typing date)
		this.getDateComponent().addMFieldListener(new MFieldListener() {
			@Override
			public void fieldEntered(FocusEvent event) {
				// Do nothing
			}

			@Override
            public void fieldExited(FocusEvent event) {
                if (!event.isTemporary()) {
                	fireEvent();
                }
            }
        });
	}
	
	protected void fireEvent() {
		// Override and add custom code here
	}
}