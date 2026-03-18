package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JOptionPane;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
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
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: AddCountToJoinderAction
 * </p>
 * <p>
 * Description: Add Count To Joinder Action
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Krishna Pokala
 * @version 1.0
 * 
 */

public class AddCountToJoinderAction extends XAction implements SearchProcessHandler {

    private static final long serialVersionUID = 1L;

    public AddCountToJoinderAction() {
        populateFromBundle("AddCountToJoinder");
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
            Integer chargeID = null;
            Integer caseID = null;
            Integer courtID = null;

            // get existing case details
            xac = (XhibitApplicationController) getController();
            chargesController = (ChargesController) xac.getBodyPanel();
            model = chargesController.getModel();
            chargesBD = model.getDelegate();
            chargeID = model.getChargeValue().getChargeID();
  
            caseID = model.getACM().getCaseId();
            courtID = new Integer(model.getCourtId());
            
            // validation that the new Offence code is not equal to a "stayed" offence 
            // that is already on the joinder
            
            String refOffenceCode = refOffence.getOffenceCode();
            
            ChargeValue chargesOnIndictment = model.getChargeValue();
            
            Iterator joinderOffenceInterator = chargesOnIndictment.getOffenceValues().iterator();
            while (joinderOffenceInterator.hasNext())
            {
                OffenceValue joinderOffence = (OffenceValue) joinderOffenceInterator.next();
                String JoinderOffenceCode = joinderOffence.getOffenceCode();
                if (refOffenceCode.equals(JoinderOffenceCode))
                {
                    Iterator DefOnOffComplexValuesIterator = joinderOffence.getDefOnOffenceBasicValues().values().iterator();
                    
                    while (DefOnOffComplexValuesIterator.hasNext())
                    {
                        DefendantOnOffenceComplexValue DefOnOffence = (DefendantOnOffenceComplexValue) DefOnOffComplexValuesIterator.next();
                        
                        if (DefOnOffence.getIsStayed().equals("Y"))
                        {
                            String errorMessage = ResourceBundleHelper.getResource(XhibitBundles.JoinderResources,
                                                        "addCountToJoinder.offenceStayed");
                            JOptionPane.showMessageDialog(null, errorMessage);
                            throw new UserCancelException();
                        }
                    }
                }
            }
            
            
            // set up offence to add to the joinder
            OffenceValue offenceValue = new OffenceValue();
            offenceValue.setCaseID(caseID);
            offenceValue.setChargeID(chargeID);
            offenceValue.setCourtID(courtID);
            offenceValue.setRefOffenceID(refOffence.getId());
            offenceValue.setOffenceDescription(refOffence.getOffenceDesc());
            offenceValue.setOffenceCode(refOffence.getOffenceCode());
            offenceValue.setCourtLogDate(Calendar.getInstance());

            AddOffenceDetailsDialog addOffenceDetailsDialog = new AddOffenceDetailsDialog(
                    AddOffenceDetailsDialog.TITLE.COUNT, offenceValue, xac, HOProcCodeHelper.TRIAL,
                    ChargesControllerHelper.MODE.ADD);
            addOffenceDetailsDialog.setVisible(true);
            if (addOffenceDetailsDialog.isCancelClicked())
                throw new UserCancelException();

            chargesBD.addJoinderOffence(offenceValue,
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