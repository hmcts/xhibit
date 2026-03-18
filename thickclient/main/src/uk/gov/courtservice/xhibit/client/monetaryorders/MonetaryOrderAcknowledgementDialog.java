package uk.gov.courtservice.xhibit.client.monetaryorders;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JDialog;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.monetaryorders.MonetaryOrderAcknowledgementModel;
import uk.gov.courtservice.xhibit.client.monetaryorders.MonetaryOrderAcknowledgementPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;

public class MonetaryOrderAcknowledgementDialog extends XDialog {
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	private MonetaryOrderAcknowledgementPanel monetaryOrderAcknowledgementPanel;
	private MonetaryOrderAcknowledgementModel monetaryOrderAcknowledgementModel;
	
	public MonetaryOrderAcknowledgementDialog(Frame frame, MonetaryOrderAcknowledgementModel monetaryOrderAcknowledgementModel) throws CSRecoverableException { 
		super(frame, "Monetary Order Acknowledgement", true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);
		setPreferredSize(new Dimension(900,400));
		setMinimumSize(getPreferredSize());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.monetaryOrderAcknowledgementModel = monetaryOrderAcknowledgementModel;
		monetaryOrderAcknowledgementPanel = new MonetaryOrderAcknowledgementPanel(this, monetaryOrderAcknowledgementModel);
		addBodyPanel(monetaryOrderAcknowledgementPanel);
		pack();
	}

	@Override
	public void dispose() {
        clearStatusBarScreenCode();
		super.dispose();
	}
}