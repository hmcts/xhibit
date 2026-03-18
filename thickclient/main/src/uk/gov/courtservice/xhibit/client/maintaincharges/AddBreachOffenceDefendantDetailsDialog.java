package uk.gov.courtservice.xhibit.client.maintaincharges;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class AddBreachOffenceDefendantDetailsDialog extends XDialog {

        private BreachOffenceDefendantDetailsPanel breachOffenceDefendantDetailsPanel;
        
        private OffenceValue offenceValue;
       
        /**
         * Launched from Add Breach Wizard and Add Breach Offence functionality.
         * @param offenceValue
         * @param defendantOnOffenceValue
         * @param xac
         * @throws CSRecoverableException
         */
        public AddBreachOffenceDefendantDetailsDialog(XhibitApplicationController xac, OffenceValue offenceValue, 
                ChargeWizardModel cwm, boolean booLinkCountDefValue,
                ChargesControllerHelper.MODE mode, ChargesControllerModel ccm) throws CSRecoverableException {
            super(xac, "", true, XDialog.OKCANCEL, XDialog.DEFAULTOK);
            super.setTitle(getString("addBreachOffenceToDefendantTitle"));
            
            if (xac == null || offenceValue == null || cwm == null || mode == null || ccm == null){
                throw new IllegalArgumentException 
                ("AddBreachOffenceDefendantDetailsDialog - xac, offenceValue, cwm, mode and ccm parameters must contain values.");
            }
            
            breachOffenceDefendantDetailsPanel = new BreachOffenceDefendantDetailsPanel(xac,this, buttonPanel, offenceValue, cwm, booLinkCountDefValue, mode, ccm);
            addBodyPanel(breachOffenceDefendantDetailsPanel);
            
            pack();
        }
        
        /**
         * Launched from AddtionalBreachDefendantOffenceAction
         * @param xac
         * @param model
         * @param booLinkCountDefValue
         * @param mode
         * @throws CSRecoverableException
         */
        public AddBreachOffenceDefendantDetailsDialog(XhibitApplicationController xac, ChargesControllerModel ccm, boolean booLinkCountDefValue,
                ChargesControllerHelper.MODE mode) throws CSRecoverableException {
            super(xac, "", true, XDialog.OKCANCEL, XDialog.DEFAULTOK);

            super.setTitle(getString("addBreachOffenceToDefendantTitle"));
            
            if (xac == null || ccm == null || mode == null){
                throw new IllegalArgumentException 
                ("AddBreachOffenceDefendantDetailsDialog - xac, offenceValue, cwm and mode parameters must contain values.");
            }
            
            breachOffenceDefendantDetailsPanel = new BreachOffenceDefendantDetailsPanel(xac,this, buttonPanel, ccm, booLinkCountDefValue, mode);
            
            addBodyPanel(breachOffenceDefendantDetailsPanel);
            
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
