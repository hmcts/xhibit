package uk.gov.courtservice.xhibit.client.util;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;

public class CustomButtonPanel extends OkCancelPanel {

    private static final long serialVersionUID = 1L;

    protected GridBagConstraints constraints;
    
    protected boolean showOkButton;
    
    protected boolean showCancelButton;
    
    protected List<JButton> buttons;
	
	public CustomButtonPanel(JDialog containingPanel, int defaultButton) {
		super(containingPanel, defaultButton);
	}

	public CustomButtonPanel(JDialog containingPanel) {
		super(containingPanel);
	}

	public boolean isShowOkButton() {
		return showOkButton;
	}

	public void setShowOkButton(boolean showOkButton) {
		this.showOkButton = showOkButton;
	}

	public boolean isShowCancelButton() {
		return showCancelButton;
	}

	public void setShowCancelButton(boolean showCancelButton) {
		this.showCancelButton = showCancelButton;
	}
	
	public List<JButton> getButtons() {
		return Collections.unmodifiableList(buttons);
	}

	@Override
	protected void init() {
		super.init();
		
		// Create button constraints and list for all buttons
		this.constraints = new GridBagConstraints(GridBagConstraints.RELATIVE, 0, 1, 1, 0.0, 0.0,
				 			GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				 			XHIBITConstant.nonContainerInsets, 0, 0);
		this.buttons = new ArrayList<JButton>();
		
		// Default is to remove the ok and cancel buttons because if
		// they are both required the OkCancelPanel should be used
		removeOkCancelButtons();
	}
	
	@Override
    protected void setDefaultButton() {
		// Clear default to prevent ok/cancel firing
        this.myContainer.getRootPane().setDefaultButton(null);
    }

	/**
	 * Add button for supplied action.
	 * 
	 * @param action XAction implementation
	 * @return button
	 */
	public JButton addButton(XAction action) {
		// Create button and use action to set its properties
        JButton button = new JButton();
        button.setAction(action);

        // Add button to panel
        addButton(button);
        
        // Return button to caller
        return button;
	}
	
	/**
	 * Add button for supplied action name and whether window closes.
	 * Buttons created by this method must be used to save data.
	 * 
	 * @param actionName action resource
	 * @param closeWindow should the window close when button clicked
	 * @return
	 */
	public JButton addButton(String actionName, boolean closeWindow) {
		return addButton(actionName, closeWindow, true);
	}
	
	/**
	 * Add button for supplied action name, whether window closes and
	 * if this button is used to save data.
	 * 
	 * @param actionName action resource
	 * @param closeWindow should the window close when button clicked
	 * @param save is data being saved when button clicked
	 * @return
	 */
	public JButton addButton(String actionName, boolean closeWindow, boolean save) {
		// Create button and use action to set its properties
        JButton button = new JButton();
        button.setAction(new CustomAction(actionName, closeWindow, save));

        // Add button to panel
        addButton(button);
        
        // Return button to caller
        return button;
	}
	
	/**
	 * Add the supplied button after the previously added buttons and
	 * before the ok and cancel buttons if they are set to be shown.
	 * 
	 * @param button
	 */
	protected void addButton(JButton button) {
		// First remove the ok and cancel buttons as re-adding
		// is the easiest way to ensure they are on the right
		removeOkCancelButtons();
		
		// Add new button to panel and to list
		this.add(button, constraints);
		this.buttons.add(button);
		
		// Re-add the ok and cancel to the right of new button
		addOkCancelButtons();
	}
	
	/**
	 * Remove the ok and cancel buttons from the panel.
	 */
	protected void removeOkCancelButtons() {
		this.remove(this.okButton);
		this.remove(this.cancelButton);
		this.buttons.remove(this.okButton);
		this.buttons.remove(this.cancelButton);
	}

	/**
	 * Add the ok and cancel buttons to the panel.
	 */
	protected void addOkCancelButtons() {
		if (this.showOkButton) {
			this.add(this.okButton, this.constraints);
			this.buttons.add(this.okButton);
		}
		if (showCancelButton) {
			this.add(this.cancelButton, this.constraints);
			this.buttons.add(this.cancelButton);
		}
	}

	/**
	 * Get the gridbag constraints for the button panel 
	 */
	public GridBagConstraints getGridBagConstraints() {
		return this.constraints;
	}
	
	/**
	 * Custom action which uses the custom lifecycle in XDialog
	 * passing in whether the window is to close and/or save
	 */
    class CustomAction extends XAction {

        private static final long serialVersionUID = 1L;
        
        boolean closeWindow;
        
        boolean save;

        public CustomAction(String actionName, boolean closeWindow, boolean save) {
            this.closeWindow = closeWindow;
            this.save = save;
            populateFromBundle(actionName);
        }

        public void xActionPerformed(ActionEvent ae) throws Exception {
        	XDialog dialog = (XDialog) CustomButtonPanel.this.myContainer;
        	dialog.customClicked(save, closeWindow, ae);
        }
    }
}
