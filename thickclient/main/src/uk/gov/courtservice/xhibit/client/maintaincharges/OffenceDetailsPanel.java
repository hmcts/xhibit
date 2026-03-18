package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Calendar;
import java.util.Collection;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.SingleItemSelectionPanel;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class OffenceDetailsPanel extends XPanel {

    private static final long serialVersionUID = 1L;
    private OffenceInfoPanel offenceInfoPanel;
    private SingleItemSelectionPanel hoProcCodePanel;
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private HOProcCodeHelper hoProcCodeHelper;
    private String hoProcType;
    private ChargesControllerHelper.MODE mode;
    private boolean showHOPanel = false;
    private Collection hoProcList;
    private OffenceValue offenceValue;
    private OkCancelPanel okCancelPanel;
    private AddressValue addressValue;
    private XhibitApplicationController xac;
    
    // Fallback values
    private Calendar fallbackStartDateTime;
    private Calendar fallbackEndDateTime;
    private String fallbackForceLocationCode;
    private Integer fallbackRefSystemCodeID;
    private AddressValue fallbackAddressValue;
    private String fallbackAppealType;
    
    
    public OffenceDetailsPanel(
            XhibitApplicationController xac,
            OkCancelPanel okCancelPanel, 
            OffenceValue offenceValue, 
            String hoProcType, 
            ChargesControllerHelper.MODE mode) 
    throws CSRecoverableException {
        if (okCancelPanel == null || mode == null ){
            throw new IllegalArgumentException 
            ("OffenceDetailsPanel - okCancelPanel and mode parameters must contain values");
        }
        this.xac = xac;
        this.okCancelPanel = okCancelPanel;
        this.offenceValue = offenceValue;
        this.mode = mode;
        this.hoProcType = hoProcType;
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
        
        if (offenceValue.getAppealType() != null) 
        	this.fallbackAppealType = offenceValue.getAppealType();
    }

    private void init(){
        this.setLayout(gridBagLayout1);
        this.add(getOffenceInfoPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        if (showHOPanel){
            this.add(getHoProcCodePanel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        }
    }
    
    private OffenceInfoPanel getOffenceInfoPanel(){
        if (offenceInfoPanel == null){
            offenceInfoPanel = new OffenceInfoPanel(xac, this, offenceValue.getCourtID(), mode);
        }
        return offenceInfoPanel;
    }

    private SingleItemSelectionPanel getHoProcCodePanel(){
        if (hoProcCodePanel == null){
        	if(offenceValue.getRefSystemCodeID()!=null) {
        		hoProcCodePanel = hoProcCodeHelper.getUserSelectionPanel(this, hoProcList, offenceValue.getRefSystemCodeID());
        	} else {
        		hoProcCodePanel = hoProcCodeHelper.getUserSelectionPanel(this, hoProcList, null);
        	}
        }
        return hoProcCodePanel;
    }
    
    @Override
    public void stepActivate() throws CSRecoverableException {
        moveModelToScreen();
        
        stepUpdateViewState();
    }

    @Override
    public void stepDeactivate() throws CSRecoverableException {
        moveScreenToModel();
    }

    @Override
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if(update){
            if (mode == ChargesControllerHelper.MODE.EDIT){
                try {
                    XhibitDelegateHelper.getChargeDelegate().updateOffence(offenceValue,
                    		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
                } catch (CSRecoverableException e) {
                    offenceValue.setOffenceStartDateTime(this.fallbackStartDateTime);
                    offenceValue.setOffenceEndDateTime(this.fallbackEndDateTime);
                    offenceValue.setForceLocationCode(this.fallbackForceLocationCode);
                    offenceValue.setRefSystemCodeID(this.fallbackRefSystemCodeID);
                    offenceValue.setAddressValue(this.fallbackAddressValue);
                    offenceValue.setAppealType(this.fallbackAppealType);
                    throw e;
                }
            }
        }
    }

    @Override
    public void stepInitialise() throws CSRecoverableException {
        
        //If no hoProcType OR if Edit mode then no ho code added.
        if (mode == ChargesControllerHelper.MODE.ADD && hoProcType != null){
            //Determine if HO Panel needs to be added.
            hoProcCodeHelper = new HOProcCodeHelper();
            hoProcList = hoProcCodeHelper.getProcList(hoProcType);
            
            if (hoProcList != null)
                showHOPanel =  hoProcList.size()> 1;
                
            
            if (!showHOPanel){
                if (hoProcList.size() == 1){
                //Need to retrieve and set HO Rf System Id
                offenceValue.setRefSystemCodeID(
                        hoProcCodeHelper.getHoProcCode(hoProcType).getId());
                }
            }
        }else if(mode == ChargesControllerHelper.MODE.EDIT && hoProcType != null){
        	 //Determine if HO Panel needs to be added.
            hoProcCodeHelper = new HOProcCodeHelper();
            hoProcList = hoProcCodeHelper.getProcList(hoProcType);
            
            if (hoProcList != null) {
                showHOPanel =  hoProcList.size()> 1;
            }               
            
          //Initialise Address for editing. 
            if (offenceValue.getAddressId() != null) {
                this.addressValue = XhibitDelegateHelper.getChargeDelegate().getAddress(offenceValue.getAddressId());
            }
            
        } else {
            //Initialise Address for editing. 
            if (offenceValue.getAddressId() != null)
                this.addressValue = XhibitDelegateHelper.getChargeDelegate().getAddress(offenceValue.getAddressId());
            }
            //TODO Need to determine if DefendantOnOffenceAdditionalInfo exists so can warn user.
        }

    @Override
    public void stepUpdateViewState() throws CSRecoverableException {
        // Enable OK if mandatory dates are completed
        okCancelPanel.getOkAction().setEnabled(getOffenceInfoPanel().isMandatoryFieldsCompleted() );
    }

    @Override
    public void stepValidate() throws CSValidationException, CSRecoverableException {

        //Validate Post code
        getOffenceInfoPanel().validatePostCode();
        
        //Validate dates
        getOffenceInfoPanel().validateDates(offenceValue);
        
        //Validate Force Location Code
        getOffenceInfoPanel().validateForceLocationCode();
    }
    
    private void moveModelToScreen(){
        getOffenceInfoPanel().moveModelToScreen(offenceValue,addressValue);
    }
    
    private void moveScreenToModel() throws CSValidationException{
        offenceValue = getOffenceInfoPanel().populateOffenceValue(offenceValue, addressValue);
        offenceValue.setUpdatingAdditionalInfo(mode == ChargesControllerHelper.MODE.EDIT);
        if (showHOPanel) {
            Object o = getHoProcCodePanel().getSelectedItem();
            if (o instanceof RefSystemCodeBasicValue) {
                RefSystemCodeBasicValue rscbv = (RefSystemCodeBasicValue) o;
                offenceValue.setRefSystemCodeID(rscbv.getId());
            }
        }
    }
}