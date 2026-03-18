package uk.gov.courtservice.xhibit.client.util;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * XPanel version of CustomButtonPanel (XDialog)
 * 
 * @author Mark Harris
 *
 */
public class CustomButtonPanelForXPanel extends CustomButtonPanel {

	private static final long serialVersionUID = 1L;
	private XhibitApplicationController xac;
	private XPanel parent;

	public CustomButtonPanelForXPanel(XhibitApplicationController xac, XPanel parent) {
		super(null);
		initPanel(xac, parent, null);
	}

	public CustomButtonPanelForXPanel(XhibitApplicationController xac, XPanel parent, Integer defaultButton) {
		super(null, defaultButton);
		initPanel(xac, parent, defaultButton);
	}

	private void initPanel(XhibitApplicationController xac, XPanel parent, Integer defaultButton) {
		this.xac = xac;
		this.parent = parent;
		if (defaultButton != null) {
			setDefaultButton();
		}
	}

	@Override
	public JButton addButton(String actionName, boolean closeWindow, boolean save) {
		// Create button and use action to set its properties
		JButton button = new JButton();
		button.setAction(new CustomActionForXPanel(actionName, closeWindow, save));

		// Add button to panel
		addButton(button);

		// Return button to caller
		return button;
	}

	@Override
	protected void setDefaultButton() {	
		if (xac != null) { 
			xac.getRootPane().setDefaultButton(getDefaultButton());
		}
	}

	@Override
	public XAction getCancelAction() {
		if (cancelAction == null) {
			cancelAction = new CancelActionForXPanel();
		}
		return cancelAction;
	}

	private void performCloseLifeCycle(boolean save, boolean closeWindow, ActionEvent ae) throws Exception {
		Object source = (ae != null) ? ae.getSource() : null;
		parent.stepPreDeinitialise(source);
		if (!closeWindow) {
			if (save) {
				parent.stepValidate();
				parent.stepDeactivate();
			}
			parent.stepDeinitialise(save);
		} else {
			// Close action will fire the parents:
			//    stepValidate, stepDeactivate and stepDeinitialise
			XAction action = XhibitActions.getAction(xac, XhibitActions.Close);
			action.actionPerformed(new ActionEvent(xac, 0, action.getName()));
		}
	}

	private class CancelActionForXPanel extends CustomActionForXPanel {

		private static final long serialVersionUID = 1L;

		public CancelActionForXPanel() {
			super("btnCancel", true, false);
		}
	}

	private class CustomActionForXPanel extends XAction {

		private static final long serialVersionUID = 1L;
		boolean closeWindow;
		boolean save;

		public CustomActionForXPanel(String actionName, boolean closeWindow, boolean save) {
			this.closeWindow = closeWindow;
			this.save = save;
			populateFromBundle(actionName);
		}

		@Override
		public void xActionPerformed(ActionEvent ae) throws Exception {	        	
			performCloseLifeCycle(save, closeWindow, ae);
		}
	}
}
