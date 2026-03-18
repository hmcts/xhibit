package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.search.AbstractSearchAction;
import uk.gov.courtservice.xhibit.client.actions.search.SearchProcessHandler;
import uk.gov.courtservice.xhibit.client.maintaincharges.AddOffenceDetailsDialog;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerHelper;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.HOProcCodeHelper;
import uk.gov.courtservice.xhibit.client.maintaincharges.log.CrestIndictmentLog;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
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
 * 
 * Change Log
 * @version 1.1 - Frederik Vandendrie ssche - Rework to work with Iteration 2
 *          Search functionality.
 */

public class AddCountAction extends XAction implements SearchProcessHandler {
    public AddCountAction() {
        populateFromBundle("AddCount");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException, Exception {
        AbstractSearchAction sa = (AbstractSearchAction) XhibitActions.getAction(
                (XhibitApplicationController) getController(), XhibitActions.OpenSearchOffence);
        sa.setCaller(this);
        sa.xActionPerformed(e);
    }

    public void processResults(AbstractSearchAction searchAction) throws CSRecoverableException {
        Collection col = searchAction.getResults();
        Iterator it = col.iterator();
        if (it.hasNext()) {
            RefOffenceBasicValue refOffence = (RefOffenceBasicValue) it.next();

            ChargesController chargesController;
            XhibitApplicationController xac;
            ChargeControllerBeanBusinessDelegate chargesBD;
            ChargesControllerModel model = null;
            Integer chargeID;
            Integer caseID = null;
            Integer courtID = null;

            xac = (XhibitApplicationController) getController();
            chargesController = (ChargesController) xac.getBodyPanel();
            model = chargesController.getModel();
            chargesBD = model.getDelegate();
            chargeID = model.getChargeValue().getChargeID();
            caseID = model.getACM().getCaseId();
            courtID = new Integer(model.getCourtId());

            // offenceValue = new OffenceValue(new Integer(1), new
            // Integer(1), vector);
            OffenceValue offenceValue = new OffenceValue();
            offenceValue.setCaseID(caseID);
            offenceValue.setChargeID(chargeID);
            offenceValue.setCourtID(courtID);
            offenceValue.setRefOffenceID(refOffence.getId());
            offenceValue.setOffenceDescription(refOffence.getOffenceDesc());
            offenceValue.setOffenceCode(refOffence.getOffenceCode());
            offenceValue.setCourtLogDate(java.util.Calendar.getInstance());
            // offenceValue.setPlea("Y");

 
            AddOffenceDetailsDialog addOffenceDetailsDialog = 
                new AddOffenceDetailsDialog(AddOffenceDetailsDialog.TITLE.COUNT,offenceValue, xac, HOProcCodeHelper.TRIAL, ChargesControllerHelper.MODE.ADD);
            addOffenceDetailsDialog.setVisible(true);
            if( addOffenceDetailsDialog.isCancelClicked())
                    throw new UserCancelException();
 
            chargesBD.addOffence(offenceValue,
            		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));

            // Log that a count has been added.
            CrestIndictmentLog.getInstance().addCountLog(
                    xac.getApplicationCaseModel().getScheduledHearingValue().getCaseBasicValue(), offenceValue);

            // Refresh Charges tab
            chargesController.loadCharges();
        } else {
            throw new UserCancelException();
        }

    }

}