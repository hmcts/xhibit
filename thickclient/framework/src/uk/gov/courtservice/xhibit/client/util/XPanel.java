package uk.gov.courtservice.xhibit.client.util;

import java.awt.LayoutManager;

import javax.swing.JPanel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;

/**
 * <p>
 * Title: XHIBIT 2 -
 * </p>
 * <p>
 * Description: This Panel is used instead of JPanel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.1
 */

public abstract class XPanel extends JPanel {
    public static final String property_modified = "modified";

    public static final String property_selectAll = "selectAll";

    private boolean modified;

    private boolean selectAllEnabled;
    
    private Object deinitialiseSource;

    // private PropertyChangeSupport changes = new
    // PropertyChangeSupport(this);
    public static boolean internalDebug = false;

    static {
        try {
            XHIBITConstant.debug("Started static initialisation of XPanel.");
            internalDebug = XHIBITConstant.isInternalDebug("XPanel");

        } catch (Exception e) {
            XHIBITConstant.error("Exception during Static initialisation of XPanel");
            XHIBITConstant.error(e);
        } finally {
            XHIBITConstant.debug("Static initialisation XPanel finished.");
        }
    }

    /**
     * Default Constructor
     */
    public XPanel() {
        super();
        // stepInitialise();
    }

    /**
     * Constructor that accepts a layout manager
     * 
     * @param l
     *            The layout the panel should use
     */
    public XPanel(LayoutManager l) {
        super(l);
        // stepInitialise();
    }

    /**
     * Overrides the JPanel set visible to call stepActivate or stepDeactivate
     * by default. Requires the user to explicitly call setVisible.
     * 
     * @param isVisible
     */
    public void setVisible(boolean isVisible) {
        if (internalDebug)
            XHIBITConstant.debug("Set visible called on XPanel isVisible: " + isVisible + " where XPanel class is: "
                    + this.getClass());

        // JDK1.5 This needs to be done first otherwise any changes to
        // components that spawn events
        // during screen initialisation will assume that no panel is visible
        // when CardLayout methods
        // execute and erroneously activate the first panel in the sequence
        super.setVisible(isVisible);

        try {
            if (isVisible) {
                if (internalDebug)
                    XHIBITConstant.debug("setVisible triggers XPanel's stepActivate()");
                stepActivate();
            } else {
                if (internalDebug)
                    XHIBITConstant.debug("setVisible triggers XPanel's stepDeactivate()");
                stepDeactivate();
            }
        } catch (CSRecoverableException re) {
            /** @todo: Implement this catch block */
            XHIBITConstant
                    .debug("Exception thrown in XPanel's  public void setVisible(boolean isVisible) /** @todo: Implement this catch block */");
            XHIBITConstant.error(re);
        }
    }

    /**
     * Call this method to flag the panel as modified.
     */
    public void modified() {
        setModified(true);
    }

    /**
     * Set the modified flag<br>
     * This is used by the save button if your class implements CommonFunctions
     * 
     * @param isModified
     */
    public void setModified(boolean isModified) {
        boolean oldModified = modified;
        modified = isModified;
        firePropertyChange(property_modified, oldModified, isModified);
    }

    /**
     * @return The current state of the modified flag
     */
    public boolean getModified() {
        return modified;
    }

    public void enableSelectAll(boolean isEnabled) {
        if (internalDebug)
            XHIBITConstant.debug("[XPanel] enableSelectAll: selectAllEnabled = " + selectAllEnabled + ";  isEnabled = "
                    + isEnabled);
        firePropertyChange(property_selectAll, selectAllEnabled, isEnabled);
        selectAllEnabled = isEnabled;
    }

    /**
     * @return The current state of the modified flag
     */
    public boolean isSelectAllEnabled() {
        return selectAllEnabled;
    }

    /**
     * 
     * @return the object causing the deinitialise
     */
    public Object getDeinitialiseSource() {
		return deinitialiseSource;
	}

	/**
     * Life-cycle method that is executed when the screen is destroyed. This
     * allows implementations of the stepDeinitialise method to identify
     * which custom button on the button panel triggered the action.
     * 
     * @param source the object causing the deinitialise
     */
    public void stepPreDeinitialise(Object source) {
    	this.deinitialiseSource = source;
    }

    /**
     * Initialises variables and models required to load screen information.
     * A call to this method is typically added to the constructor of the
     * subclass or a parent panel.
     * 
     * @throws CSRecoverableException
     */
	abstract public void stepInitialise() throws CSRecoverableException;

	/**
     * Life-cycle method executed whenever the panel is made visible.
     * The implementation of this method typically moves data from
     * the model to the screen and then updates the view.
	 * 
	 * @throws CSRecoverableException
	 */
	abstract public void stepActivate() throws CSRecoverableException;

	/**
     * Life-cycle method to enable/disable screen components depending on
     * user input. A call to this method is typically added to the panel
     * activation and to actions fired from user interaction with the panel.
	 * 
	 * @throws CSRecoverableException
	 */
    abstract public void stepUpdateViewState() throws CSRecoverableException;

    /**
     * Life-cycle method that is executed when the screen is destroyed.
     * The implementation of this method should be used to ensure user
     * input is valid.
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    abstract public void stepValidate() throws CSValidationException, CSRecoverableException;

    /**
     * Life-cycle method that is executed whenever the screen is made invisible or
     * is closed. The implementation of this method typically moves data from the
     * screen to the model.
     * 
     * @throws CSRecoverableException
     */
    abstract public void stepDeactivate() throws CSRecoverableException;

    /**
     * Life-cycle method that is executed when the screen is destroyed. This is
     * typically as a result of the user clicking the OK/Apply/Cancel or one of
     * the custom buttons on the XDialog's button panel. The implementation of
     * this method typically persists updates in the model to the database.
     * 
     * @param update boolean parameter used to indicate if the processing triggered
     * 				 by the one of the dialog buttons is being activated (true) or
     * 				 if the dialog is being closed without changes (false)
     * @throws CSRecoverableException
     */
    abstract public void stepDeinitialise(boolean update) throws CSRecoverableException;
}