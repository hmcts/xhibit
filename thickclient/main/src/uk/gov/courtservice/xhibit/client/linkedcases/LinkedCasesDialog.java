package uk.gov.courtservice.xhibit.client.linkedcases;

import java.util.ResourceBundle;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.CaseSchedHearingValue;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Dialog contains LinkedCasesPanel which displays Cases that are
 * available for linking and selected for linking
 * </p>
 * <p>
 * Description: The ok button on the dialog commits all changed by calling
 * stepDeInitialise on the LinkedCasesPanel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 * 
 */

public class LinkedCasesDialog extends XDialog {
    /**
     * ResourceBundle myResources
     */
    private ResourceBundle myResources = XHIBITConstant.getResourceBundle(XhibitBundles.TodaysSchedule);

    /**
     * CaseSchedHearingValue[] linkedCases
     */
    private CaseSchedHearingValue[] linkedCases = null;

    /**
     * The constructor passes the xac argument into the LinkedCasesPanel <init>
     * 
     * @param xac
     *            parameter for <init>
     * @throws CSRecoverableException -
     */
    public LinkedCasesDialog(XhibitApplicationController xac) throws CSRecoverableException {
        super(xac, "", true);
        super.setTitle(XHIBITConstant.getResource(myResources, "dialogTitleLinkedCases"));
        LinkedCasesPanel linkedCasesPanel = new LinkedCasesPanel(this, xac);
        addBodyPanel(linkedCasesPanel);
        pack();
    }

    /**
     * setLinkedCases
     * 
     * @param linkedCases
     *            parameter for setLinkedCases
     */
    public void setLinkedCases(CaseSchedHearingValue[] linkedCases) {
        this.linkedCases = linkedCases;
    }

    /**
     * getLinkedCases
     * 
     * @return the returned CaseSchedHearingValue[]
     */
    public CaseSchedHearingValue[] getLinkedCases() {
        return linkedCases;
    }
}