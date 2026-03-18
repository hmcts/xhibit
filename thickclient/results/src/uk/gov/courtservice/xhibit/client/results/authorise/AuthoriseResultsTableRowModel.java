package uk.gov.courtservice.xhibit.client.results.authorise;

import java.awt.Component;

import javax.swing.JComboBox;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationValue;

/**
 * <p>
 * Title: AuthoriseResultsTableRowModel
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Stephen Tully
 * @version $Id: AuthoriseResultsTableRowModel.java,v 1.1 2004/06/01 10:25:39
 *          szn20z Exp $
 * @history v1.2 Kelvin Davies 03/04/2009 For CCN1263
 *          Updated to include new Date_Exported (dateRecordSheetSent) column.
 * 
 */

public class AuthoriseResultsTableRowModel {
    
    private String warningFlag;
    
    private String defendantName;

    private String authorisationStatus;
    
    private String dateRecordSheetSent;

    private Boolean selected;
    
    private Boolean reselected;
    
    private String amendedReason;
    
    private boolean previouslyAuthorised;
    
    private AuthorisationValue authorisationValue;

    // get methods
    public String getWarningFlag() {
        return warningFlag;
    }
    
    public String getDefendantName() {
        return defendantName;
    }

    public String getAuthorisationStatus() {
        return authorisationStatus;
    }
    
    public String getDateRecordSheetSent(){
        return dateRecordSheetSent;
    }

    public Boolean isSelected() {
        return selected;
    }
    
    public Boolean isReSelected() {
        return reselected;
    }
    
    public String getAmendedReason() {
        return amendedReason;
    }

    public AuthorisationValue getAuthorisationValue() {
        return authorisationValue;
    }
    
    public boolean getPreviouslyAuthorised(){
        return previouslyAuthorised;
    }

    // set methods
    public void setWarningFlag(String param) {
        warningFlag = param;
    }

    public void setDefendantName(String param) {
        defendantName = param;
    }

    public void setAuthorisationStatus(String param) {
        authorisationStatus = param;
    }
    
    public void setDateRecordSheetSent(String param){
        dateRecordSheetSent = param;
    }

    public void setSelected(Boolean param) {
        selected = param;
    }
    
    public void setReSelected(Boolean param) {
        reselected = param;
    }
    
    public void setAmendedReason(String param){
        amendedReason = param;
    }

    public void setAuthorisationValue(AuthorisationValue param) {
        authorisationValue = param;
    }

    public void setPreviouslyAuthorised(boolean param){
        previouslyAuthorised = param;
    }
    
    // utility
    public void printModel() {
        XHIBITConstant.info("AuthoriseResultsTableRowModel");
        XHIBITConstant.info("-----------------------------");
        XHIBITConstant.info("warningFlag         : " + getWarningFlag());
        XHIBITConstant.info("defendantName       : " + getDefendantName());
        XHIBITConstant.info("authorisationStatus : " + getAuthorisationStatus());
        XHIBITConstant.info("dateRecordSheetSent : " + getDateRecordSheetSent());  
        XHIBITConstant.info("selected            : " + (isSelected() == null ? false : isSelected().booleanValue()));
        XHIBITConstant.info("reselected          : " + (isReSelected() == null ? false : isReSelected().booleanValue()));
        XHIBITConstant.info("amendedReason       : " + getAmendedReason());
        XHIBITConstant.info("authorisationValue  : " + getAuthorisationValue().toString());
    }

}
