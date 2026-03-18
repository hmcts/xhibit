package uk.gov.courtservice.xhibit.business.services.createcase;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;

/**
 * This class provides the method to create
 * the case group number based on court id
 * 
 * @author m.newman
 *
 * @ejb.bean name="GenerateCaseGroupController" description="Generate Case Group Session Bean"
 *           type="Stateless" view-type="both" jndi-name="GenerateCaseGroupControllerHome"
 *           local-jndi-name="GenerateCaseGroupControllerLocalHome"
 * @ejb.transaction type="Required"
 */
public class GenerateCaseGroupControllerBean extends CSSessionBean implements SessionBean {
    private static final long serialVersionUID = 1L;

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
     * @return Integer the new case group number
     */
    public int generateCaseGroupNumber(int courtId){
    	return CreateCaseGroupNumber.getCaseGroupNumber(courtId);
    }
}
