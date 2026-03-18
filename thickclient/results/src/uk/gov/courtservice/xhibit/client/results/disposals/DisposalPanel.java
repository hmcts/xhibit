package uk.gov.courtservice.xhibit.client.results.disposals;

import java.awt.GridBagLayout;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * <p>
 * Title: DisposalPanel
 * </p>
 * <p>
 * Description: Provide common disposal panel functionality.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.6 $
 */
public abstract class DisposalPanel extends XPanel {

    /**
     * Construct a new Disposal Panel to select and initialise a new disposal
     * for specified defendantOnOffence or defendantOnCase
     */
    public DisposalPanel() throws CSRecoverableException {
        super(new GridBagLayout());
        stepInitialise();
    }

    /**
     * XPanel Lifecycle Method: called from the constructor
     */
    public void stepInitialise() throws CSRecoverableException {
        // Implementation of life cycle method
    }

    /**
     * XPanel Lifecycle Method: called when the panel is made visible
     */
    public void stepActivate() throws CSRecoverableException {
        stepUpdateViewState();
    }

    /**
     * XPanel Lifecycle Method: called when a component state is changed
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        // Implementation of life cycle method
    }

    /**
     * XPanel Lifecycle Method: called when the ok button (on containing dialog)
     * is pressed
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        // Implementation of life cycle method
    }

    /**
     * XPanel Lifecycle Method: called when panel is made invisible
     */
    public void stepDeactivate() throws CSRecoverableException {
        // Implementation of life cycle method
    }

    /**
     * XPanel Lifecycle Method: called when the panel is disposed (screen
     * closed)
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        // Implementation of life cycle method
    }
}