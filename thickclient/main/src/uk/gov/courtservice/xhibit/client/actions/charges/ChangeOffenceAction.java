package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.charge.ResultsFoundException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.search.AbstractSearchAction;
import uk.gov.courtservice.xhibit.client.actions.search.SearchProcessHandler;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.HOProcCodeHelper;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */

public class ChangeOffenceAction extends XAction implements SearchProcessHandler {
    public ChangeOffenceAction() {
        populateFromBundle("ChangeOffence");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException, Exception {
        AbstractSearchAction sa = (AbstractSearchAction) XhibitActions.getAction(
                (XhibitApplicationController) getController(), XhibitActions.OpenSearchOffence);
        sa.setCaller(this);
        sa.xActionPerformed(e);
    }

    public void processResults(AbstractSearchAction searchAction) throws CSRecoverableException {
        // Get search results:
        Collection col = searchAction.getResults();
        Iterator it = col.iterator();
        if (it.hasNext()) {
            RefOffenceBasicValue refOffence = (RefOffenceBasicValue) it.next();

            XhibitApplicationController xac = (XhibitApplicationController) getController();
            ChargesController chargesController = (ChargesController) xac.getBodyPanel();
            ChargesControllerModel ccm = chargesController.getModel();
            ChargeControllerBeanBusinessDelegate chargesBD = ccm.getDelegate();
            OffenceValue offenceValue = ccm.getOffenceValue();

            boolean offenceChanged = false;
            boolean resultsFoundMBReply = false;

            if (refOffence == null)
                throw new UserCancelException();

            HOProcCodeHelper hop = new HOProcCodeHelper((Frame) getController());
            String hopType = "";

            if (ccm.getChargeValue().getChargeType().equals("S")) {
                hopType = HOProcCodeHelper.SENT;
            } else {
                hopType = HOProcCodeHelper.S41;
            }
            RefSystemCodeBasicValue hoproc = hop.getHoProcCode(hopType, offenceValue.getRefSystemCodeID());

            if (hoproc == null) {
                throw new UserCancelException();
            } else {
                offenceValue.setRefSystemCodeID(hoproc.getId());
            }

            offenceValue.setRefOffenceID(refOffence.getId());
            offenceValue.setOffenceDescription(refOffence.getOffenceDesc());
            offenceValue.setCrestOffenceFreeText(null);
            offenceValue.setCrestHOClass(null);
            offenceValue.setCrestHOSubclass(null);
            offenceValue.setCourtLogDate(Calendar.getInstance());

            try {
                offenceValue.setDeleteResults(false);
                chargesBD.updateOffence(offenceValue,
                		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
                offenceChanged = true;
            } catch (ResultsFoundException rfe) {
                resultsFoundMBReply = XMessageBox.alert(xac, getResource("ChangeOffence.Results.Query.Title"), true,
                        XMessageBox.ICONQUESTION, getResource("ChangeOffence.Results.Query.Message"),
                        XMessageBox.YESNO, XMessageBox.DEFAULTNO);

                if (resultsFoundMBReply) {
                    try {
                        offenceValue.setDeleteResults(true);
                        chargesBD.updateOffence(offenceValue,
                        		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
                        offenceChanged = true;
                    } catch (ResultsFoundException rfe2) {
                        // Should not get this exception when passing
                        // true to the method deleteOffence
                        String errMsg = getResource("ChangeOffence.ResultsFoundException2");
                        XHIBITErrorHandler.handleError(rfe2, null, errMsg);
                    }
                }
            }

            // Refresh Charges tab
            if (offenceChanged) {
                chargesController.loadCharges();
            }
        } else {
            throw new UserCancelException();
        }
    }

    private String getResource(final String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.MaintainCharges, key);
    }
}