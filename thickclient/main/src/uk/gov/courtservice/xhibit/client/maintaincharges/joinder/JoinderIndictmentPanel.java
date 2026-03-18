package uk.gov.courtservice.xhibit.client.maintaincharges.joinder;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.WizardButtonPanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: XHIBIT 2 - Joinder Panel
 * </p>
 * <p>
 * Description: This joinder panel extends XPanel to inherit its functionality
 * as well as provide default functionality for the joinder panel subclasses.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Antoniou
 * @version 1.0
 */

abstract public class JoinderIndictmentPanel extends XPanel {
    /** The wizard dialog associated to this wizard. */
    protected XWizardDialog wizardDialog = null;

    /**
     * The model associated to the wizard dialog. Static instance is used by all
     * wizard panels.
     */
    protected static JoinderIndictmentModel model = null;

    /**
     * A clone of the model, used for wizard 'BACK' events when the back button
     * is pressed.
     */
    protected JoinderIndictmentModel clonedModel = null;

    /** The general layout used throughout the lifescope of the wizard. */
    protected GridBagLayout gbLayout = null;

    /** The general constraints. */
    protected GridBagConstraints gbConstraints = null;

    /** The resources containing the text of this wizard. */
    protected ResourceBundle resources = null;

    /**
     * Prevents activate reentrancy issues due to setVisible changes in the
     * underlying JDK 1.5
     */
    private boolean activateReentrancy = false;

    /**
     * Prevents deactivate reentrancy issues due to setVisible changes in the
     * underlying JDK 1.5
     */
    private boolean deactivateReentrancy = false;

    /** The logger. */
    protected static Logger log;

    /**
     * Default Constructor.
     */
    public JoinderIndictmentPanel(XWizardDialog dialog) {
        this.wizardDialog = dialog;
        this.log = CSServices.getLogger(JoinderIndictmentPanel.class);
        this.resources = ResourceBundleHelper.getResourceBundle(XhibitBundles.JoinderResources);
        this.gbLayout = new GridBagLayout();
        this.gbConstraints = new GridBagConstraints();
    }

    /**
     * Get the resource string for the given resource key in Joinder resources.
     * 
     * @param resourceKey
     *            the resource to lookup
     * @return the resource string.
     */
    protected String getResource(String resourceKey) {
        return ResourceBundleHelper.getResource(resources, resourceKey);
    }

    /**
     * Overrides the abstract method in XPanel. Will check to see if the last
     * event generated from the main button panel of the wizard dialog was a
     * 'Prev' action. If so then it sets the active model to be that of the
     * clone.
     */
    public void stepActivate() throws CSRecoverableException {
        // Refer to stepDeactivate to see why the reentrancy checks are required
        // in JDK1.5
        if (activateReentrancy) {
            log.debug("stepActivate - reentrancy has occurred");
            return;
        } else {
            activateReentrancy = true;
            deactivateReentrancy = false;
        }

        log.debug("stepActivate - Begin - LATEST ACTION: " + wizardDialog.getLatestEvent());

        if (wizardDialog.getLatestEvent() == XWizardDialog.PREV_EVENT) {
            // Call the doBack method, will make the cloned model active by
            // default.
            this.stepActivateOnBack();
        }
    }

    /**
     * Overrides the abstract method in XPanel.
     */
    public void stepDeactivate() throws CSRecoverableException {
        // The reason why this reentrancy code is required is setVisible is
        // called
        // frequently from the SUN infrastructure in JDK1.5 - this method is
        // overridden in XPanel to call stepActivate / stepDeactivate
        // This will call stepDeactivateOnNext in stepDeactivate twice
        // leading to the openCase thread executing again and causing the
        // panels to get displayed incorrectly and the models won't be set
        // correctly either
        // Hence this localized reentrancy fix
        if (deactivateReentrancy) {
            log.debug("stepDeactivate - reentrancy has occurred");
            return;
        } else {
            deactivateReentrancy = true;
            activateReentrancy = false;
        }

        log.debug("stepDeactivate - Begin - latest event = " + wizardDialog.getLatestEvent());

        if (wizardDialog.getLatestEvent() == XWizardDialog.NEXT_EVENT) {
            try {
                // Clone the current model, in case user changes mind and calls
                // back.
                this.clonedModel = (JoinderIndictmentModel) this.model.clone();
            } catch (CloneNotSupportedException e) {
                log.warn(e);
            }

            // Invoke the next method, implemented by subclass.
            this.stepDeactivateOnNext();
        }

        stepDeactivateOnAll();
    }

    /**
     * Overrides method in XPanel to make call to stepActivate(). Implementation
     * specific to this panel being contained in XWizardDialog. On a prev action
     * of the dialog - it does not call stepActivate() on the panel that is
     * being set but stepUpdateViewState(), so this calls stepActivate()
     * instead.
     * 
     * @throws CSRecoverableException
     */
    // public void stepUpdateViewState () throws CSRecoverableException
    // {
    // if (this.wizardDialog.getLatestEvent()==XWizardDialog.PREV_EVENT)
    // {
    // this.stepActivate();
    // }
    // }
    /**
     * This method is called by stepActivate () when the last generated event
     * from the main button panel of the wizard dialog, was as a result of a
     * PREV action.
     */
    private void stepActivateOnBack() {
        // A back/prev event was made, so now work from the cloneable model.
        try {
            // Changed from old model.clone(model) implemtation.
            this.model = (JoinderIndictmentModel) this.clonedModel.clone();
        } catch (CloneNotSupportedException e) {
            XHIBITErrorHandler.handleError(e);
        }
    }

    /**
     * Subclasses should override and implement this method for defining
     * deactivate functionality on the selected panel in the wizard, when the
     * last event generated was a 'Next' action.
     */
    abstract public void stepDeactivateOnNext() throws CSRecoverableException;

    /**
     * Sets reentrancy variable at end of deactivate
     */
    abstract public void stepDeactivateOnAll();

    /**
     * Has no implementation by default.
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
    }

    protected void setDefaultButton() {
        final WizardButtonPanel buttonPanel = wizardDialog.getButtonPanel();
        if (buttonPanel.getNext().isEnabled()) {
            getRootPane().setDefaultButton(buttonPanel.getNext());
        } else if (buttonPanel.getFinish().isEnabled()) {
            getRootPane().setDefaultButton(buttonPanel.getFinish());
        } else {
            getRootPane().setDefaultButton(buttonPanel.getCancel());
        }
    }

    /**
     * Subclasses should override this to reset the status and values they hold.
     */
    public void reset() {
        this.clonedModel = null;
    }
}