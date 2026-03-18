package uk.gov.courtservice.xhibit.client.results.authorise;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBean;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * Description: This is a helperer class for the Authorise Results classes.
 * @author davieskl
 * @Version 1.0
 * @History Created by Kelvin Davies for CCN1263
 *
 */
public class AuthoriseResultsHelper {
    private static final Logger LOG = CSServices.getLogger(AuthoriseResultsHelper.class);

    /**
     * Description: Retrieves true if the associated case has the Vulnerable victim flag set to true.
     * @param caseValue
     * @return flag
     */
    public boolean isVulnerableVictim(Integer caseId){
        boolean flag = false;
        CaseControllerBeanBusinessDelegate caseDelegate = XhibitDelegateHelper.getCaseDelegate();
        CaseBasicValue caseBV = new CaseBasicValue();
        
        try {
            caseBV = caseDelegate.getCase(caseId);

            if(caseBV.getVulnerableVictimIndicator() != null){
                if(caseBV.getVulnerableVictimIndicator().equals("Y")){
                    flag = true;
                }
            }
        } catch (CaseControllerException e) {
            // this is an unexpected exception, the case id was 
            // retrieved as part of the defendant details so there should 
            // be an entity for this id
            CSServices.getDefaultErrorHandler().handleError(e, DefendantControllerBean.class);
            throw new CSUnrecoverableException("Could not find an Case entity "
                    + "from an id retrieved as part of " + "a case entity ", e);
        }
        
        return flag;
    }
    
    /**
     * Description: Retreieve the exported date from the defendant on case record
     * @param status
     * @param defendantOnCaseID
     * @param caseID
     * @return date: String
     * @throws DefendantControllerException 
     */
    public String getExportDate(String status, Integer defendantID, Integer caseID) throws DefendantControllerException{
        String date="";
        
        //If the record sheet was exported sucessfully, display date, otherwise do not display date.
        //For RFC2878 this changes to return the Amended Date Exported which stores the last date the record sheet
        //was exported.
        if(status != null){
            if(status.equalsIgnoreCase("E")){
                if(defendantID != null || caseID == null){
                    DefendantControllerBeanBusinessDelegate dcBD = XhibitDelegateHelper.getDefendantDelegate();
                    DefendantOnCaseValue docVO = dcBD.getDefendantOnCaseDetails(defendantID, caseID);
                    
                    if(docVO.getDefendantOnCaseBVO().getAmendedDateExported() != null){
                    date = docVO.getDefendantOnCaseBVO().getAmendedDateExported().getTime().toString();}
                }
            }
        }
        return date;
    }
    
    /**
     * Description: Check the exported date from the defendant on case record and set a Boolean flag
     * @param status
     * @param defendantOnCaseID
     * @param caseID
     * @return datePresent: Boolean
     * @throws DefendantControllerException 
     */
    public boolean getPreviouslyExported(String status, Integer defendantID, Integer caseID) throws DefendantControllerException{
        boolean datePresent = false;
        
        //New method for RFC2878 - Set flag to indicate if results have been previously authorised.
        //This will be used to determine if authorise or re-authorise check boxes will be enabled on screen
        if(status != null){
            if(status.equalsIgnoreCase("E")){
                if(defendantID != null || caseID == null){
                    DefendantControllerBeanBusinessDelegate dcBD = XhibitDelegateHelper.getDefendantDelegate();
                    DefendantOnCaseValue docVO = dcBD.getDefendantOnCaseDetails(defendantID, caseID);
                    
                    if(docVO.getDefendantOnCaseBVO().getAmendedDateExported() != null){
                    datePresent = true;}
                }
            }else if(status.equalsIgnoreCase("N")){
                if(defendantID != null || caseID == null){
                    DefendantControllerBeanBusinessDelegate dcBD = XhibitDelegateHelper.getDefendantDelegate();
                    DefendantOnCaseValue docVO = dcBD.getDefendantOnCaseDetails(defendantID, caseID);
                    
                    if(docVO.getDefendantOnCaseBVO().getAmendedDateExported() != null){
                    datePresent = true;}
                }
            }
        }
        return datePresent;
    }
        
    /**
     * Description: Update the vulnerable victim indicator for a specific XHB_Case record.
     * @param caseId
     * @param selected
     * @throws CaseControllerException 
     */
    public void updateCase(Integer caseId, boolean selected, String userDisplayName){
        CaseControllerBeanBusinessDelegate caseDelegate = XhibitDelegateHelper.getCaseDelegate();
        CaseBasicValue caseBV = new CaseBasicValue();
        try {
            caseBV = caseDelegate.getCase(caseId);

            //Update case values
            if (selected){
                caseBV.setVulnerableVictimIndicator("Y");
            }else{
                caseBV.setVulnerableVictimIndicator("N");
            }
            
            //Update case XHIBIT
            caseDelegate.updateCase(caseBV, userDisplayName);
            //Update case CREST
            //method call to remove,CTX-223
            //caseDelegate.updateCRESTCase(caseBV);
            
        } catch (CaseControllerException e) {
            // 
            //this is an unexpected exception, the case id was 
            // retrieved as part of the defendant details so there should 
            // be an entity for this id
            CSServices.getDefaultErrorHandler().handleError(e, DefendantControllerBean.class);
            throw new CSUnrecoverableException("Could not find an Case entity "
                    + "from an id retrieved as part of " + "a case entity ", e);
        }
    }
}
