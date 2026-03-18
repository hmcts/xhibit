package uk.gov.courtservice.xhibit.business.services.createcase;

import java.sql.SQLException;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;

/**
 * This class provides the method to create
 * the case number based on court id and 
 * 
 * @author waltersn
 *
 * @ejb.bean name="GenerateCaseNumberController" description="Generate Case Number Session Bean"
 *           type="Stateless" view-type="both" jndi-name="GenerateCaseNumberControllerHome"
 *           local-jndi-name="GenerateCaseNumberControllerLocalHome"
 * @ejb.transaction type="Required"
 */
public class GenerateCaseNumberControllerBean extends CSSessionBean implements SessionBean {
    private static final long serialVersionUID = 1L;
    private static final String MISC = "MISC";
    private static final String APPEAL = "APPEAL";

    /**
     * Instantiates all relevant home interfaces for use during active life of
     * Stateless Session Bean.
     * 
     * @throws CreateException
     * @ejb.create-method
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
        
    }
    
    /**
     * Generate the case number
     * 
     * @ejb.interface-method view-type="both"
     * @param courtId from which the case is being created
     * @param caseType : Trial/Appeal/Sentence/Trial Indictment
     * @return String for the new case number
     */
    public String generateCaseNumber(int courtId, String caseType) throws SQLException{
    	 if(caseType.equals(MISC)) {
        	caseType = APPEAL;
	     }
    	return CreateCaseNumber.getCaseNumber(courtId, caseType);
    }
}
