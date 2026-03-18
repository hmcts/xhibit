package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: AddBailActOffenceDialog
 * </p>
 * <p>
 * Description:
 * Wizard Dialog to add Failure to Appear charges AND associated Bail Act Offence.  
 * As the user is unaware of the separation, all dialogs will only refer to
 * Bail Act Offences.
 * </p>
 
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Luis Valenzuela
 * @version 1.0
 */

public class AddBailActOffenceDialog extends XWizardDialog implements BreachController {

    private static final long serialVersionUID = 1001L;
    
    private final static int DEFENDANT_PANEL = 0;
    private final static int OFFENCE_PANEL = 1;
    
    /* The Breach Type specified for Fail 2 Appear charges from CREST housekeeping spec */ 
    private final static String FAIL_2_APPEAR_BREACH_TYPE = "F";
    
    private XhibitApplicationController xac;
    private ChargesControllerModel ccm;
    /* Bail Act Offences held as a BREACH Charge associated with a single Offence */ 
    private BreachValue fail2AppearBreachValue;
    BreachWizardModel model = new BreachWizardModel();
    
    private SelectDefendantPanel selectDefendantPanel;
    private AddedOffenceToFail2AppearPanel addedOffencePanel;


    public AddBailActOffenceDialog(ChargesControllerModel ccm) throws CSRecoverableException {
        super(ccm.getACM().getXhibitApplicationController(), "", true);
        this.ccm = ccm;
        this.xac = ccm.getACM().getXhibitApplicationController();
        model.setOffenceRequired(true); //Required if forcing the addition on offences to breach

        fail2AppearBreachValue = new BreachValue();
        fail2AppearBreachValue.setCaseID(ccm.getACM().getCaseId());
        fail2AppearBreachValue.setBreachType(FAIL_2_APPEAR_BREACH_TYPE);
        
        // setDatePut will be set in the Pleas screen.
        // fail2AppearValue.setDatePut(Calendar.getInstance()); 
        
        // Check if user is in Court Room, rather than assuming that they are.
        // Charges can be added by Court Clerk, not in a court room
        fail2AppearBreachValue.setInCourt(XhibitSingleton.getInstance().isUserInCourtroom());
        fail2AppearBreachValue.setHoCode(HOProcCodeHelper.FAIL_2_APPEAR_HO_CODE);
        
        HOProcCodeHelper hoProcCodeHelper = new HOProcCodeHelper();
        RefSystemCodeBasicValue rSCBV = 
            hoProcCodeHelper.getHoProcCodeFromProcCodeAndProcType(
                    HOProcCodeHelper.FAIL_2_APPEAR_HO_CODE, HOProcCodeHelper.BREACH);
        
        if (rSCBV == null) {
            // This behaviour is consistent with the other uses of getHoProcCode
            // but we can probably do better.
            throw new UserCancelException();
        }
        fail2AppearBreachValue.setRefSystemCodeID(rSCBV.getId());
        
        model.setBreachValue(fail2AppearBreachValue);
        model.setAddedOffences(new ArrayList<OffenceValue>());
        model.setCaseID(ccm.getACM().getCaseId());
        model.setCaseType(ccm.getACM().getCaseType());
        model.setChargeType(ChargeTypes.FAIL2APPEAR);
        model.setCourtId(XhibitSingleton.getInstance().getCourtId());

        // create panels to add to wizard
        selectDefendantPanel = new SelectDefendantPanel(ccm, this, model);
        addedOffencePanel =  new AddedOffenceToFail2AppearPanel(this, model);

        // Panel Array
        ArrayList<XPanel> al = new ArrayList<XPanel>();
        al.add(selectDefendantPanel);
        al.add(addedOffencePanel);
        addBodyPanels(al);

        setWizardPanelImage("xwizardimage.jpg");
        pack();
    }

    /**
     * BreachController implementation called when the state of the screen data
     * changes to enable/disable the wizard button as appropriate.
     */
    public void stepUpdateViewState() {
        
        // Need to check what the current screen is
        switch (currentPanel) 
        {
       
        case DEFENDANT_PANEL:
            setTitle(getString("addFail2AppearWizardDialogTitle") + " - " + getString("selectDefendantTitle"));

            getButtonPanel().getBack().setEnabled(false);
            // If defendant selected Enable Next
            if (selectDefendantPanel != null) {
                if (selectDefendantPanel.getDefendantCb().getSelectedItem() instanceof DefendantValue) {
                    getButtonPanel().getNext().setEnabled(true);
                } else {
                    getButtonPanel().getNext().setEnabled(false);
                }
            } else {
                getButtonPanel().getNext().setEnabled(false);
            }
            getButtonPanel().getFinish().setEnabled(false);
            break;
        
        case OFFENCE_PANEL:
            setTitle(getString("addFail2AppearWizardDialogTitle") + " - " + getString("addBailActOffencesTitle"));
            
            getButtonPanel().getBack().setEnabled(true);
            getButtonPanel().getNext().setEnabled(false);
            if (addedOffencePanel != null) {
                if (model.isOffenceRequired()) {
                    // enable if row offence exists
                    getButtonPanel().getFinish().setEnabled(addedOffencePanel.isMandatoryFieldsCompleted());
                } else {
                    getButtonPanel().getFinish().setEnabled(true);
                }
            } else {
                getButtonPanel().getFinish().setEnabled(false);
            }
            
            break;
            
        }// end of Switch
    }// end of stepUpdateViewState

    
    public void stepDeinitialise() throws CSRecoverableException {
        ChargeValue chargeValue = new ChargeValue();
        chargeValue.setCaseID(ccm.getACM().getCaseId());
        chargeValue.setCourtID(new Integer(ccm.getCourtId()));
        chargeValue.setCourtLogDate(Calendar.getInstance());
        chargeValue.setChargeType(ChargeTypes.FAIL2APPEAR);

        // Set from BreachWizardModel
        chargeValue.setDefendantID(model.getDefendantID());
        BreachValue breachValue = model.getBreachValue();
        chargeValue.setBreachValue(breachValue);

        // offenceValue collection
        if (model.getAddedOffences() != null && model.getAddedOffences().size() > 0) {
            // Need to set offences as a vector not arraylist
            java.util.List offenceList = model.getAddedOffences();
            Integer seqNo = null;
            for (int i = 0; i < offenceList.size(); i++) {
                seqNo = new Integer(i + 1);
                ((OffenceValue) offenceList.get(i)).setCrestOffenceSeqNo(seqNo);
            }
            chargeValue.setOffenceValues(new Vector(model.getAddedOffences()));
        }

        // Call the integration facade
        ccm.getDelegate().addChargeToCase(chargeValue, xac.getApplicationCaseModel().getScheduledHearingId()==null?false:true,
        		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
     }

    
    public XhibitApplicationController getXac() {
        return xac;
    }

   
    /**
     * Get a resource string from the Breach resources
     * 
     * @param key
     *            the key to lookup
     * @return the resource fro the given key.
     */
    private String getString(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.Breaches, key);
    }
}