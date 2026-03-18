package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.ArrayList;
import java.util.Calendar;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.services.charge.ResultsFoundException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: Change Bail Act Offence Dialog
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Logica
 */

public class ChangeBailActOffenceDialog extends XDialog implements BreachController {

    private static final long serialVersionUID = 1L;

    private AddedOffenceToFail2AppearPanel baoPanel;
    private XhibitApplicationController xac;
    private BreachWizardModel model;
    private ChargesControllerModel chargesControllerModel;
    private OffenceValue oldOffenceValue;
    private Calendar oldBreachOriginalSentenceDate; 
    private ChargesController chargesController;

    
    public ChangeBailActOffenceDialog(XhibitApplicationController xac) 
    throws CSRecoverableException {
        super(xac, "", true);
        this.xac = xac;
        chargesController = (ChargesController) xac.getBodyPanel();
        chargesControllerModel = chargesController.getModel();
        ChargeValue chargeValue = chargesControllerModel.getChargeValue();
        BreachValue breachValue = chargeValue.getBreachValue();
        oldBreachOriginalSentenceDate = breachValue.getOriginalSentenceDate();
        ArrayList<OffenceValue> offences = new ArrayList<OffenceValue>();
        oldOffenceValue = chargesControllerModel.getOffenceValue();
        offences.add(oldOffenceValue);
        
        model = new BreachWizardModel();
        model.setOffenceRequired(true);
        model.setBreachValue(breachValue);
        model.setAddedOffences(offences);
        model.setCaseID(chargesControllerModel.getACM().getCaseId());
        model.setCaseType(chargesControllerModel.getACM().getCaseType());
        model.setChargeType(ChargeTypes.FAIL2APPEAR);
        model.setCourtId(XhibitSingleton.getInstance().getCourtId());
        model.setDefendantID(chargesControllerModel.getDefendantValue().getId());
        model.setDefendantOnCaseID(chargesControllerModel.getDefendantValue().getDefOnCaseBasicValue().getId());
        
        super.setTitle(ResourceBundleHelper.getResource(XhibitBundles.Breaches, "changeBailActOffenceTitle"));

        baoPanel = new AddedOffenceToFail2AppearPanel(this, model);

        addBodyPanel(baoPanel);
        pack();
    }

    public XhibitApplicationController getXac() {
        return xac;
    }

    public void stepUpdateViewState() {
        if (baoPanel != null) {
            ((OkCancelPanel)this.getButtonPanel()).okButton.setEnabled(
                    baoPanel.isMandatoryFieldsCompleted());
        }
    }

    /*
     * To be used where chargesController.loadCharges(); would be excessive.
     */
    private void restoreModel() {
        BreachValue breachValue = model.getBreachValue();
        breachValue.setOriginalSentenceDate(oldBreachOriginalSentenceDate);
    }
    
    public void stepDeactivate() throws CSRecoverableException {
        if (this.isCancelClicked()) {
            restoreModel();
        }
    }
    
    public void stepDeinitialise() throws CSRecoverableException {
        try {
            stepDeinitialiseAux();
        } catch (CSRecoverableException e) {
            // If an exception has been thrown, for example, in the validation
            // done in Mercator at the CREST end, then we need to reload all
            // the values because we dont know what state things are in.
            chargesController.loadCharges();
            throw e;
        }
    }
    
    public void stepDeinitialiseAux() throws CSRecoverableException {
        
        boolean offenceOrBreachChanged = false;
        ChargeControllerBeanBusinessDelegate chargesBD = chargesControllerModel.getDelegate();
        
        // There will always be one, and only one offence, 
        // otherwise the OK button will be greyed out
        // and we will not get here.
        OffenceValue newOffenceValue = model.getAddedOffences().get(0);
        if (!equal(oldOffenceValue, newOffenceValue)) {
            oldOffenceValue.setRefOffenceID(newOffenceValue.getRefOffenceID());
            oldOffenceValue.setOffenceDescription(newOffenceValue.getOffenceDescription());
            oldOffenceValue.setCrestOffenceFreeText(null);
            oldOffenceValue.setCrestHOClass(null);
            oldOffenceValue.setCrestHOSubclass(null);
            oldOffenceValue.setCourtLogDate(Calendar.getInstance());
            
            try {
                oldOffenceValue.setDeleteResults(false);
                chargesBD.updateOffence(oldOffenceValue,
                		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
                offenceOrBreachChanged = true;
            } catch (ResultsFoundException rfe) {
                // There are already results recorded against the offence (pleas, disposals, etc).  
                // These must be deleted if the offence is changed.  Ask the user if they really do want
                // to change the offence, and hence delete the results.
                
                boolean resultsFoundMBReply = XMessageBox.alert(
                        xac, getResource("ChangeOffence.Results.Query.Title"), true,
                        XMessageBox.ICONQUESTION, getResource("ChangeOffence.Results.Query.Message"),
                        XMessageBox.YESNO, XMessageBox.DEFAULTNO);

                if (resultsFoundMBReply) {
                    try {
                        oldOffenceValue.setDeleteResults(true);
                        chargesBD.updateOffence(oldOffenceValue,
                        		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
                        offenceOrBreachChanged = true;
                    } catch (ResultsFoundException rfe2) {
                        // Should not get this exception when passing
                        // true to the method deleteOffence
                        String errMsg = getResource("ChangeOffence.ResultsFoundException2");
                        XHIBITErrorHandler.handleError(rfe2, null, errMsg);
                    }
                } else {
                    // If no is pressed we better not continue
                    // and change the breach.
                    restoreModel();
                    return;
                }
            }
        }
        
        BreachValue breachValue = model.getBreachValue();
        Calendar newBreachOriginalSentenceDate = breachValue.getOriginalSentenceDate();
        if (!equal(oldBreachOriginalSentenceDate, newBreachOriginalSentenceDate)) {
            breachValue.setCourtLogDate(Calendar.getInstance());
            breachValue.setDirty(true);
            chargesBD.updateBreach(breachValue, true);
            offenceOrBreachChanged = true;
        }
        
        if (offenceOrBreachChanged) {
            chargesController.loadCharges();
        }
    }
    
    private boolean equal(Calendar v1, Calendar v2) {
        return (v1 == null && v2 == null)
            || (v1 != null && v2 != null && v1.equals(v2));
    }
    
    private boolean equal(String v1, String v2) {
        return (v1 == null && v2 == null)
            || (v1 != null && v2 != null && v1.equals(v2));
    }
    
    private boolean equal(Integer v1, Integer v2) {
        return (v1 == null && v2 == null)
            || (v1 != null && v2 != null && v1.equals(v2));
    }
    
    private boolean equal(OffenceValue v1, OffenceValue v2) {
        return equal(v1.getRefOffenceID(), v2.getRefOffenceID()) 
            && equal(v1.getOffenceDescription(), v2.getOffenceDescription());
    }
    
    private String getResource(final String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.MaintainCharges, key);
    }
}