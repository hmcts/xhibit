package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.courtlog.CourtLogController;
import uk.gov.courtservice.xhibit.client.courtlog.FreeTextModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: Court Log Action
 * </p>
 * <p>
 * Description: extended by all court log actions. Automatically refreshes after
 * event added or editted and performs business logic
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Revision: 1.20 $
 */
public abstract class CourtLogAction extends XAction {
    /** <code>Logger</code> used for logging of debug/info statements */
    protected static final Logger log = CSServices.getLogger(CourtLogAction.class);

    private Collection listOfXac;

    private static HashSet startEvents = null;

    private static HashSet endHearingEvents = null;

    public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
        XhibitApplicationController xac = (XhibitApplicationController) getController();

        // Toggle the screen status label before and after displaying the dialog
        String oldStatusText = xac.getStatusLabel();
        String newStatusText = getLongDescription();
        xac.setStatusLabel(newStatusText);

        preCourtLogLogic((FreeTextModel) getModel(), xac);

        try {
            displayScreen(e);
        } finally {
            // status text may have change e.g. if the case has
            // synchronised.
            if (xac.getStatusLabel().equals(newStatusText)) {
                xac.setStatusLabel(oldStatusText);
            }
        }

        // get link group
        if (xac.getApplicationCaseModel().isLinked()) {
            listOfXac = XhibitSingleton.getInstance().getLinkGroup(xac, true);
        } else {
            listOfXac = new ArrayList();
            listOfXac.add(xac);
        }

        try {
            performCourtLogLogic();
        } catch (CSRecoverableException ex) {
            XHIBITConstant.handleError(ex);
        }

        refreshLog(((FreeTextModel) getModel()).isInEditMode());
    }

    protected abstract void displayScreen(ActionEvent e) throws CSRecoverableException;

    private void refreshLog(boolean isEdit) {
        Iterator iter = listOfXac.iterator();
        while (iter.hasNext()) {
            XhibitApplicationController xac = (XhibitApplicationController) iter.next();
            if (xac.getBodyPanel().getClass() == CourtLogController.class) {
                ((CourtLogController) xac.getBodyPanel()).refreshEvents(isEdit);
            }

            /**
             * If the court log event just processed was an end hearing event,
             * then indicate whether or not the hearing is ended for this XAC.
             * This is done irrespective of whether or not the user is currently
             * viewing the court log since they may have navigated to some other
             * related screen, e.g. charges, within the same case.
             */
            if (xac.getApplicationCaseModel() != null && xac.getApplicationCaseModel().getScheduledHearingId() != null) {
                if (getEndHearingEvents().contains(((FreeTextModel) getModel()).getEventType())) {
                    xac.setHearingEnded(XhibitDelegateHelper.getHearingDelegate().isHearingEnded(
                            xac.getApplicationCaseModel().getScheduledHearingId()).booleanValue());
                }
            }
        }
    }

    private void preCourtLogLogic(FreeTextModel event, XhibitApplicationController xac) throws CSRecoverableException {
        // Activate the public display if start event type.
        // Not do not perform on link group as only one
        // case can be active at any time.
        if (getStartEvents().contains(event.getEventType())) {
            // Don't activate displays if editting the event.
            if (!event.isInEditMode()) {
                XhibitHelper.activatePublicDisplay(xac);
            }
        }
    }

    private void performCourtLogLogic() throws CSRecoverableException {
        FreeTextModel event = (FreeTextModel) getModel();
        if (!event.isInEditMode()) {
            if (getStartEvents().contains(event.getEventType())) {
                Iterator iter = listOfXac.iterator();
                while (iter.hasNext()) {
                    XhibitApplicationController xac = (XhibitApplicationController) iter.next();
                    performCourtLogLogic2(event, xac);
                }
            }
        } else {
            // perform logic on current xac only - even if linked.
            performCourtLogLogic2(event, (XhibitApplicationController) getController());
        }
    }

    private void performCourtLogLogic2(FreeTextModel event, XhibitApplicationController xac)
            throws CSRecoverableException {
        if (getStartEvents().contains(event.getEventType())) {
            XhibitHelper.startShHearing(xac);
        }
    }

    private static HashSet getStartEvents() {
        if (startEvents == null) {
            startEvents = new HashSet();
            // @todo - this shouldn't be hardcoded!
            startEvents.add("10100");
            startEvents.add("10500");
        }
        return startEvents;
    }

    private static HashSet getEndHearingEvents() {
        if (endHearingEvents == null) {
            endHearingEvents = new HashSet();
            // @todo - this shouldn't be hardcoded!
            endHearingEvents.add("30500");
            endHearingEvents.add("30600");
        }

        return endHearingEvents;
    }

    /**
     * Helper method used to extract the common code of cloning this actions
     * model. If cloning fails then this actions model will be returned
     * directly. Also, the application controller will be set on the cloned
     * model.
     * 
     * @return The cloned <code>FreeTextModel</code>.
     * @see #getModel()
     * @see #getController()
     * @since version 1.14
     */
    protected FreeTextModel cloneModel() {
        FreeTextModel model = null;

        try {
            model = (FreeTextModel) ((FreeTextModel) this.getModel()).clone();
        } catch (Exception ex) {
            model = (FreeTextModel) this.getModel();
        }

        model.setXac((XhibitApplicationController) getController());
        return model;
    }

    /**
     * Helper method used to extract the common code of displaying the passed in
     * dialog, and determining if the user clicked the cancel button. If they
     * did, then a <code>UserCancelException</code> will be thrown.
     * 
     * @param myDialog
     *            The <code>XDialog</code> to display.
     * @throws UserCancelException
     *             If the user presses the cancel button.
     * @since version 1.14
     */
    protected void displayDialog(XDialog myDialog) throws UserCancelException {
         myDialog.setVisible(true);

        if (myDialog.isCancelClicked()) {
            throw new UserCancelException();
        }
    }

}