package uk.gov.courtservice.xhibit.business.services.datareset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.resetdata.ResetDataDatabaseManager;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerLocal;
import uk.gov.courtservice.xhibit.business.services.datareset.vo.ManagedCase;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtBasicValue;

/**
 * <p>
 * Title: The DataReset Stateless Session EJB.
 * </p>
 * <p>
 * Description: <p/>
 * 
 * <br/> <p/> This is the Stateless Session Bean that provides core business
 * services required for the Witness Facilities part of XHIBIT .
 * </p>
 * 
 * @author Scott Atwell
 * @ejb.bean name="DataResetController" description="Data Reset Controller Bean"
 *           type="Stateless" view-type="remote"
 *           jndi-name="DataResetControllerHome"
 * @ejb.transaction type="Required"
 *                        <p>
 *                        Copyright: Copyright (c) 2015
 *                        </p>
 *                        <p>
 *                        Company: CGI
 *                        </p>
 */
public class DataResetControllerBean extends CSSessionBean implements SessionBean {
    
    /**
     * Instantiates all relevant home interfaces for use during active life of
     * Stateless Session Bean.
     * 
     * @throws CreateException
     * @ejb.create-method
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();

        log.debug("Exiting ejbCreate");
    }
    
    
    /**
     * Given the court name, find the court id
     * @param courtName
     * @return
     * 
     * @ejb.interface-method view-type="both"
     */
    public Integer getCourtId(String courtName) throws Exception {
    	Integer courtId = null;
    	ResetDataDatabaseManager rddb = new ResetDataDatabaseManager();
    	CourtBasicValue[] theCourts = rddb.getCourt(courtName.toUpperCase());
    	if (theCourts.length > 1) {
    		throw new Exception("Too many courts returned for " + courtName);
    	}
    	if (theCourts.length == 0) {
    		courtId = 0;
    	} else {
    		//	Assume 1 court
    		courtId = theCourts[0].getId();
    	}
    	
    	return courtId;
    }
    
    /**
     * Given the court id, get the managed cases
     * @param courtId
     * @return
     * 
     * @ejb.interface-method view-type="both" 
     */
    public Collection getManagedCasesByCourt(Integer courtId) {
		ArrayList<ManagedCase> managedCases = new ArrayList();
    	ResetDataDatabaseManager rddb = new ResetDataDatabaseManager();
    	ManagedCase[] theCases = rddb.getManagedCasesByCourt(courtId);
    	
    	// Convert into ManagedCase[]
    	// Get the defendants on case for each case id
    	// Get the defendants for each defendant on case id
    	for (int i=0; i<theCases.length; i++) {
    		ManagedCase mc = new ManagedCase();
    		mc.setCaseId(theCases[i].getCaseId());
    		mc.setCaseNumber(theCases[i].getCaseNumber());
    		mc.setCaseType(theCases[i].getCaseType());
    		mc.setDefendantId(theCases[i].getDefendantId());
    		
    		StringBuffer name = new StringBuffer();
    		if (theCases[i].getDefendantFirstName() != null) {
    			name.append(theCases[i].getDefendantFirstName()+" ");
    		}
    		if (theCases[i].getDefendantMiddleName() != null) {
    			name.append(theCases[i].getDefendantMiddleName()+" ");
    		}
    		if (theCases[i].getDefendantSurname() != null) {
    			name.append(theCases[i].getDefendantSurname());
    		}
    		mc.setDefendantName(name.toString());
    		mc.setDefendantDOB(theCases[i].getDefendantDOB());
    		mc.setCourtName(theCases[i].getCourtName());
    		
    		// Get the defendantoncase's for the caseid
    		//caseController.getDefendants(theCases[i].getId());
    		managedCases.add(mc);
    	}
    	
    	// Now get the defendants on case for each case id
    	
    	return managedCases;
    }
    
    /**
     * Update the date of birth of a defendant
     * 
     * @param newDefendantDOB
     * @param defendantId
     * @return
     * 
     * @ejb.interface-method view-type="both" 
     */
    public String updateDefendantDOB(Date newDefendantDOB, Integer defendantId) {
    	String returnValue = "";
    	
    	ResetDataDatabaseManager rddb = new ResetDataDatabaseManager();
    	rddb.updateDefendantDOB(new java.sql.Date(newDefendantDOB.getTime()), defendantId);
    	
    	return returnValue;
    }
    
    /**
     * Update the config table to say that CREST can now be updated
     * 
     * @return
     * 
     * @ejb.interface-method view-type="both" 
     */
    public String updateCREST() {
    	String returnValue = "";
    	
    	ResetDataDatabaseManager rddb = new ResetDataDatabaseManager();
    	rddb.updateCREST();
    	
    	return returnValue;
    }
    
    /**
     * Remove a managed case
     * 
     * @param caseId
     * @return
     * 
     * @ejb.interface-method view-type="both" 
     */
    public void removeManagedCase(Integer caseId) {
    	ResetDataDatabaseManager rddb = new ResetDataDatabaseManager();
    	rddb.removeManagedCase(caseId);
    }
    
    /**
     * Add a managed case
     * 
     * @param caseId
     * 
     * @ejb.interface-method view-type="both" 
     */
    public void addManagedCase(Integer caseId) {
    	ResetDataDatabaseManager rddb = new ResetDataDatabaseManager();
    	rddb.addManagedCase(caseId);
    }
    
    /**
     * Check if a case exists in the database
     * 
     * @param courtId
     * @param caseType
     * @param caseNumber
     * @return
     * 
     * @ejb.interface-method view-type="both" 
     */
    public String checkCaseExists(Integer courtId, String caseType, Integer caseNumber) {
    	String returnValue = "";
    	
    	ResetDataDatabaseManager rddb = new ResetDataDatabaseManager();
    	returnValue = rddb.checkCaseExists(courtId, caseType, caseNumber);
    	
    	return returnValue;
    }
    
    /**
     * Check if a case has already been flagged as 'managed'
     * @param caseId
     * @return
     * 
     * @ejb.interface-method view-type="both" 
     */
    public String checkCaseAlreadyAdded(Integer caseId) {
    	String returnValue = "";
    	
    	ResetDataDatabaseManager rddb = new ResetDataDatabaseManager();
    	returnValue = rddb.checkCaseAlreadyAdded(caseId);
    	
    	return returnValue;
    }

}
