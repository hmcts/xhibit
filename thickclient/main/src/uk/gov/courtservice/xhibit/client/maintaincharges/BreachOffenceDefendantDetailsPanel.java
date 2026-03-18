package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DefendantOnOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.LinkCountDefValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class BreachOffenceDefendantDetailsPanel extends XPanel {

    private static final long serialVersionUID = 1L;

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private OffenceInfoPanel offenceInfoPanel;

    private DefendantOffenceDetails defendantOffenceDetails;

    private ChargesControllerHelper.MODE mode;

    private HOProcCodeHelper hoProcCodeHelper;

    private Collection hoProcList;

    private String hoProcType;

    private boolean showHOPanel = false;

    private AddBreachOffenceDefendantDetailsDialog parent;

    private OkCancelPanel okCancelPanel;

    private OffenceValue offenceValue;

    private DefendantOnOffenceComplexValue defOnOffComplexValue;
    
    private AddressValue addressValue;

    private String caseType;

    private Integer defendantID;

    private Integer defendantOnCaseID;

    private boolean booLinkCountDefValue;

    private ChargesControllerModel ccm;

    private List seqNosList;
    
    private XhibitApplicationController xac;
    
//  Fallback values
    private Calendar fallbackStartDateTime;
    private Calendar fallbackEndDateTime;
    private String fallbackForceLocationCode;
    private Integer fallbackRefSystemCodeID;
    private AddressValue fallbackAddressValue;
    

    /**
     * Added to AddBreachOffenceDefendantDetailsDialog when it is launched from 
     * Add Breach Wizard and Add Breach Offence functionality.
     * @param parent
     * @param okCancelPanel
     * @param offenceValue
     * @param cwm
     * @param booLinkCountDefValue
     * @param mode
     * @throws CSRecoverableException
     */
    public BreachOffenceDefendantDetailsPanel(
            XhibitApplicationController xac,
            AddBreachOffenceDefendantDetailsDialog parent,
            OkCancelPanel okCancelPanel, 
            OffenceValue offenceValue, 
            ChargeWizardModel cwm,
            boolean booLinkCountDefValue, 
            ChargesControllerHelper.MODE mode, 
            ChargesControllerModel ccm)
    throws CSRecoverableException {

        if (parent == null || okCancelPanel == null || offenceValue == null || cwm == null || mode == null
                || ccm == null) {
            throw new IllegalArgumentException(
                    "BreachOffenceDefendantDetailsPanel - Must have values for parent, model, okCancelPanel, "
                            + "offencValue, cwm, mode and ccm.");
        }

        //Need a reference to xac as defOnCaseSeqNo hashmap is now stored in app case model instead of charge model
        this.xac = xac;
        
        this.parent = parent;
        this.offenceValue = offenceValue;
        this.caseType = cwm.getCaseType();
        this.defendantID = cwm.getDefendantID();
        this.defendantOnCaseID = cwm.getDefendantOnCaseID();
        this.okCancelPanel = okCancelPanel;
        this.booLinkCountDefValue = booLinkCountDefValue;
        this.mode = mode;
        this.ccm = ccm;

        setFallbackValues();
        stepInitialise();
        init();
    }

    /**
     * Added to AddBreachOffenceDefendantDetailsDialog when it is launched from AddtionalBreachDefendantOffenceAction
     * @param parent
     * @param okCancelPanel
     * @param ccm
     * @param booLinkCountDefValue
     * @param mode
     * @throws CSRecoverableException
     */
    public BreachOffenceDefendantDetailsPanel(
            XhibitApplicationController xac,
            AddBreachOffenceDefendantDetailsDialog parent,
            OkCancelPanel okCancelPanel, 
            ChargesControllerModel ccm, 
            boolean booLinkCountDefValue,
            ChargesControllerHelper.MODE mode) 
    throws CSRecoverableException {

        if (parent == null || okCancelPanel == null || ccm == null || mode == null) {
            throw new IllegalArgumentException(
                    "BreachOffenceDefendantDetailsPanel - Must have values for parent, model, okCancelPanel, "
                            + "model and mode.");
        }

        //Need a reference to xac as defOnCaseSeqNo hashmap is now stored in app case model instead of charge model
        this.xac = xac;
        
        this.parent = parent;
        this.ccm = ccm;
        this.caseType = ccm.getACM().getCaseType();
        this.offenceValue = ccm.getOffenceValue();

        if (ccm.getDefendantValue() != null && ccm.getDefendantValue().getDefOnCaseBasicValue() != null) {
            this.defendantOnCaseID = ccm.getDefendantValue().getDefOnCaseBasicValue().getId();
        }

        if (ccm.getChargeValue() != null) {
            this.defendantID = ccm.getChargeValue().getDefendantID();
        }

        this.okCancelPanel = okCancelPanel;
        this.booLinkCountDefValue = booLinkCountDefValue;
        this.mode = mode;

        setFallbackValues();
        stepInitialise();
        init();
    }

    private void setFallbackValues() {
        if (offenceValue == null)
            return;
        
        if (offenceValue.getOffenceStartDateTime() != null)
            this.fallbackStartDateTime = (Calendar)offenceValue.getOffenceStartDateTime().clone();
        
        if (offenceValue.getOffenceEndDateTime() != null)
            this.fallbackEndDateTime = (Calendar)offenceValue.getOffenceEndDateTime().clone();
        
        this.fallbackForceLocationCode = offenceValue.getForceLocationCode();
        this.fallbackRefSystemCodeID = offenceValue.getRefSystemCodeID();
        
        if (offenceValue.getAddressValue() != null)
            this.fallbackAddressValue = new AddressValue(offenceValue.getAddressValue());
    }
    
    private void init() {
        this.setLayout(gridBagLayout1);
        this.add(getOffenceInfoPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        this.add(getDefendantOffenceDetails(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    }

    private OffenceInfoPanel getOffenceInfoPanel() {
        if (offenceInfoPanel == null) {
            offenceInfoPanel = new OffenceInfoPanel(xac, this, offenceValue.getCourtID(), mode);
        }
        return offenceInfoPanel;
    }

    private DefendantOffenceDetails getDefendantOffenceDetails() {
        if (defendantOffenceDetails == null) {
            defendantOffenceDetails = new DefendantOffenceDetails(this, true, mode, caseType, defOnOffComplexValue,
                    seqNosList);
        }
        return defendantOffenceDetails;
    }

    private void moveModelToScreen() {
        getOffenceInfoPanel().moveModelToScreen(offenceValue, addressValue);
        getDefendantOffenceDetails().moveModelToScreen();
    }

    private void updatedOffence() throws CSValidationException {
        // Below is require
        offenceValue = getOffenceInfoPanel().populateOffenceValue(offenceValue, addressValue);

        DefendantOnOffenceComplexValue docv = getDefendantOffenceDetails().updateDefendantOnOffenceComplexValue(
                defOnOffComplexValue);  
        
        HashMap<Integer, DefendantOnOffenceComplexValue> defendantsOnOffencesComplexValues = new HashMap<Integer, DefendantOnOffenceComplexValue>();

        defendantsOnOffencesComplexValues.put(defendantID, docv);
      
        // Set defendant on offence
        offenceValue.setDefOnOffenceBasicValues(defendantsOnOffencesComplexValues);
      
    }

    private void populateOffence() throws CSValidationException {
        // Below is require
        offenceValue = getOffenceInfoPanel().populateOffenceValue(offenceValue, addressValue);
        offenceValue.setUpdatingAdditionalInfo(mode == ChargesControllerHelper.MODE.EDIT);

        DefendantOnOffenceComplexValue docv = getDefendantOffenceDetails().createDefendantOnOffenceComplexValue();

        HashMap<Integer, DefendantOnOffenceComplexValue> defendantsOnOffencesComplexValues = new HashMap<Integer, DefendantOnOffenceComplexValue>();

        defendantsOnOffencesComplexValues.put(defendantID, docv);

        // Set defendant on offence
        offenceValue.setDefOnOffenceBasicValues(defendantsOnOffencesComplexValues);
    }

    public void stepActivate() throws CSRecoverableException {
        moveModelToScreen();

        stepUpdateViewState();
    }

    public void stepDeactivate() throws CSRecoverableException {
        // Empty
    }

    @Override
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (update) {
            if (booLinkCountDefValue) {
                if (mode == ChargesControllerHelper.MODE.ADD) {
                    // Usually if this Panel is part of a single process
                    LinkCountDefValue linkCountDefValue = new LinkCountDefValue();
                    linkCountDefValue.setCaseID(ccm.getACM().getCaseId());
                    linkCountDefValue.setCourtID(new Integer(ccm.getCourtId()));
                    linkCountDefValue.setIsInCourt(ccm.isUserInCourtRoom());
                    linkCountDefValue.setCourtLogDate(Calendar.getInstance());
                    linkCountDefValue.setAddDefendantToCount(true);
                    linkCountDefValue.setChargeType(ccm.getChargeValue().getChargeType());
                    Collection<DefendantOnOffenceValue> defendantsOnOffence = 
                        new ArrayList<DefendantOnOffenceValue>();

                    defendantsOnOffence.add(getDefendantOffenceDetails().createDefendantOnOffenceValue(
                            offenceValue.getOffenceID(), defendantID));
                    linkCountDefValue.setDefendantOnOffenceValues(defendantsOnOffence);
                    XhibitDelegateHelper.getChargeDelegate().linkCountsAndDefendants(linkCountDefValue,
                    		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
                } else { // EDIT MODE
                    updatedOffence();
                    
                    try {
                    	offenceValue.setUpdatingAdditionalInfo(true);
                        XhibitDelegateHelper.getChargeDelegate().updateOffenceAndDefOnOffence(offenceValue, defendantID,
                        		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
                      } catch (CSRecoverableException e) {
                        offenceValue.setOffenceStartDateTime(this.fallbackStartDateTime);
                        offenceValue.setOffenceEndDateTime(this.fallbackEndDateTime);
                        offenceValue.setForceLocationCode(this.fallbackForceLocationCode);
                        offenceValue.setRefSystemCodeID(this.fallbackRefSystemCodeID);
                        offenceValue.setAddressValue(this.fallbackAddressValue);
                        throw e;
                    }
                }
            } else { // No direct writing to DB.Part of AddBreachWizard process
                populateOffence();
            }
        }
    }

    public void stepInitialise() throws CSRecoverableException {
        // If no hoProcType OR if Edit mode then no ho code added.
        if (mode == ChargesControllerHelper.MODE.ADD || hoProcType != null) {
            // Determine if HO Panel needs to be added.
            hoProcCodeHelper = new HOProcCodeHelper();
            hoProcList = hoProcCodeHelper.getProcList(hoProcType);

            if (hoProcList != null)
                showHOPanel = hoProcList.size() > 1;

            if (!showHOPanel) {
                if (hoProcList.size() == 1) {
                    // Need to retrieve and set HO Rf System Id
                    offenceValue.setRefSystemCodeID(hoProcCodeHelper.getHoProcCode(hoProcType).getId());
                }
            }
        } else {
            // Initialise Address for editing.
            if (offenceValue.getAddressId() != null)
                // Retrieve exising address details
                this.addressValue = XhibitDelegateHelper.getChargeDelegate().getAddress(offenceValue.getAddressId());

            // Get exising defendant on offence details
            defOnOffComplexValue = offenceValue.getDefendantOnOffence(defendantID);
        }
        
        seqNosList = ccm.getAllDefendantsOnCaseSeqNosSortedMap().get(defendantOnCaseID);
    }

    public void stepUpdateViewState() throws CSRecoverableException {
        // Enable OK if mandatory dates are completed
        okCancelPanel.getOkAction().setEnabled(getOffenceInfoPanel().isMandatoryFieldsCompleted());
    }

    public void stepValidate() throws CSValidationException, CSRecoverableException {
        
        //Validate Post Code
        getOffenceInfoPanel().validatePostCode();
        
        // Validate dates
        getOffenceInfoPanel().validateDates();

        // Validate Force Location Code
        getOffenceInfoPanel().validateForceLocationCode();

        //Validate Defendant On Offence dates
        getDefendantOffenceDetails().validateDates(getOffenceInfoPanel().getEndDatePanel().getDate());
        getDefendantOffenceDetails().validateSeqNo();
    }
}
