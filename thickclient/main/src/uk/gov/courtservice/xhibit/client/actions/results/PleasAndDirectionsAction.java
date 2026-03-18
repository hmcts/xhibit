package uk.gov.courtservice.xhibit.client.actions.results;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.results.PleasAndDirectionsController;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: PleasAndDisposalsAction
 * </p>
 * <p>
 * Description: action for pleas and directions
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Paul Morris
 * @version $Id: PleasAndDirectionsAction.java,v 1.7 2005/05/31 11:12:02 szfnvt
 *          Exp $
 */

public class PleasAndDirectionsAction extends SynchXAction {
    private XhibitApplicationController xac;

    private PleasAndDirectionsController padc;

    public PleasAndDirectionsAction() {
        populateFromBundle("PleasAndDirections");
    }

    public void preSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        xac = (XhibitApplicationController) getController();
        xac.callBodyPanelCloseLifeCycleMethods();
    }

    public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        padc = new PleasAndDirectionsController(xac);
    }

    public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        if (padc != null && xac != null) {
            xac.open(padc);
        }
    }

    /**
     * Override the default read access check to check for two security roles
     * 
     * @return
     */
    public boolean hasReadAccess() {
        return FunctionList.hasAccess(FunctionList.ECourtLog) || FunctionList.hasAccess(FunctionList.EPlea);
    }

    /**
     * Override the default edit access check to check for two security roles
     * 
     * @return
     */
    public boolean hasEditAccess() {
        return FunctionList.hasAccess(FunctionList.ECourtLog) || FunctionList.hasAccess(FunctionList.EPlea);
    }
}