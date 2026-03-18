package uk.gov.courtservice.xhibit.client.order.screens.dialog;

import java.awt.Dimension;
import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderOffenceModel;
import uk.gov.courtservice.xhibit.client.order.screens.panel.OrderOffencePanel;
import uk.gov.courtservice.xhibit.client.util.XDialog; 

/**
 * Dialog for adding offences to inlclude in the D20 order
 * 
 * @author guthriec
 *
 */
public class OrderOffenceDialog extends XDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OrderOffenceModel model;
	/**
	 * Constructer for the OrderOffenceDialog
	 * 
	 * @param frame XhibitApplicationController
	 * @param model {@link OrderOffenceModel}
	 * @throws CSRecoverableException
	 */
	public OrderOffenceDialog(Frame frame, OrderOffenceModel model) throws CSRecoverableException{
		super(frame, "Select Offences to include on D20", true, XDialog.OKCANCEL, XDialog.DEFAULTOK);
		this.setPreferredSize(new Dimension(840, 400));
		this.setModel(model);
		buttonPanel.okButton.setEnabled(false);
		this.bodyPanel = new OrderOffencePanel(this, model);
		addBodyPanel(bodyPanel);
		pack();				
	}

	/**
	 * @return model for offences to add to the D20 order
	 */
	public OrderOffenceModel getModel() {
		return model;
	}

	/**
	 * @return set model for offences for the D20 order
	 */
	public void setModel(OrderOffenceModel model) {
		this.model = model;
	}
	
	/**
	 * @param enable whether to enable the ok button or not
	 */
	public void okButtonSetEnabled(boolean enabled){
		getButtonPanel().okButton.setEnabled(enabled);
	}
	
}
