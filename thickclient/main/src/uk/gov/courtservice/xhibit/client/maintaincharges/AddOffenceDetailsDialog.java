package uk.gov.courtservice.xhibit.client.maintaincharges;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class AddOffenceDetailsDialog extends XDialog {
    
    private static final long serialVersionUID = 1L;

    public static enum TITLE{COUNT,OFFENCE}
    
    private OffenceDetailsPanel offenceDetailsPanel;
    
    
    public AddOffenceDetailsDialog(
            TITLE title, 
            OffenceValue offenceValue, 
            XhibitApplicationController xac, 
            String hoProcType, 
            ChargesControllerHelper.MODE mode) throws CSRecoverableException {
        super(xac, "", true, XDialog.OKCANCEL, XDialog.DEFAULTOK);


        if (title == null || mode == null ){
            throw new IllegalArgumentException 
            ("AddOffenceDetailsDialog - title and mode parameters must contain values");
        }
        
        switch (title){
        case COUNT:
            super.setTitle(getString("addCountInfo.Title"));
            break;
        case OFFENCE:
            super.setTitle(getString("addOffenceInfo.Title"));
            break;
        default:
            super.setTitle(getString("addOffenceInfo.Title"));
            break;
        }

        //Need a surounding panel which also includes the HOProcPanel
        offenceDetailsPanel = new OffenceDetailsPanel(
                xac, this.buttonPanel, offenceValue, hoProcType, mode);
        
        addBodyPanel(offenceDetailsPanel);
        
        pack();
    }
    
    /**
     * Get a resource string from the Additional resources
     * 
     * @param key
     *            the key to lookup
     * @return the resource from the given key.
     */
    private String getString(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.AddCountsDefendantsResources, key);
    }
}
