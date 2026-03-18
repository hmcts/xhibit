package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.Collection;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Vector;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

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
 * Company: EDS
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 06-08-2003 AW Daley Apply button added.
 * 
 * 27-08-2003 AW Daley Hashmap of DefendantOnOffenceBasicValue added
 */
public class AddDefendantsToOffenceDialog extends XDialog {
    
    private static final long serialVersionUID = 1L;

    public static enum TITLE{DEFENDANT_TO_COUNT,DEFENDANT_TO_OFFENCE,COUNT_TO_DEFENDANT}
    
    private ResourceBundle myResources = XHIBITConstant.getResourceBundle(XhibitBundles.AddCountsDefendantsResources);
    
    private Vector defendantIDs = null;

    private HashMap defendantOnOffenceComplexValues;

    private AddDefendantsToOffencePanel addDefendantsCountsPanel;
    
    public XhibitApplicationController xac;

    public AddDefendantsToOffenceDialog(XhibitApplicationController xac, boolean booLinkCountDefValue, 
            TITLE title, Collection notOnOffence, ChargesControllerHelper.MODE mode )
            throws CSRecoverableException {
        super(xac, "", true, XDialog.OKCANCEL, XDialog.DEFAULTOK);

        if (xac == null || title == null || notOnOffence == null || mode == null){
            throw new IllegalArgumentException 
            ("AddDefendantsToOffenceDialog - xac, title, notOnOffence and mode parameters must contain values.");
        }
        
        this.xac = xac;
        ChargesController chargesController = (ChargesController) xac.getBodyPanel();
        ChargesControllerModel model = chargesController.getModel();
   
        switch (title){
        case DEFENDANT_TO_COUNT:
            super.setTitle(XHIBITConstant.getResource(myResources, "addDefendantToCount.title"));
            break;
        case COUNT_TO_DEFENDANT:
            super.setTitle(XHIBITConstant.getResource(myResources, "addCountToDefendant.title"));
            break;
        case DEFENDANT_TO_OFFENCE:
            if (mode == ChargesControllerHelper.MODE.EDIT) {
                super.setTitle(XHIBITConstant.getResource(myResources, "editDefendantOnOffence.title"));
            } else {
                super.setTitle(XHIBITConstant.getResource(myResources, "addDefendantToOffence.title"));
            }
            break;
        default:
            if (mode == ChargesControllerHelper.MODE.EDIT) {
                super.setTitle(XHIBITConstant.getResource(myResources, "editDefendantOnOffence.title"));
            } else {
                super.setTitle(XHIBITConstant.getResource(myResources, "addDefendantToOffence.title"));
            }
            break;
        }
                
        addDefendantsCountsPanel = new AddDefendantsToOffencePanel(this, model, buttonPanel, 
                booLinkCountDefValue, mode, notOnOffence);
        addBodyPanel(addDefendantsCountsPanel);
        pack();
    }

    public void setDefendantID(Vector defendantIDs) {
        this.defendantIDs = defendantIDs;
    }

    public Vector getDefendantIDs() {
        return defendantIDs;
    }

    public void setDefendantsOnOffenceComplexValues(HashMap defendantOnOffenceComplexValues) {
        this.defendantOnOffenceComplexValues = defendantOnOffenceComplexValues;
    }

    public HashMap getDefendantOnOffenceComplexValues() {
        return this.defendantOnOffenceComplexValues;
    }
}
