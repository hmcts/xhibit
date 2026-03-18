package uk.gov.courtservice.xhibit.business.services.bwhistory;

import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.bwhistory.BwHistory;
import uk.gov.courtservice.xhibit.business.entities.bwhistory.BwHistoryMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.BwHistoryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.bwhistory.BwHistoryValue;



/**
 * <p>
 * Title: BwHistoryControllerBean
 * </p>
 * <p>
 * Description: Session bean for manipulating defendant details
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @ejb.bean name="BwHistoryController" description=
 *           "Bw History Session Bean" type="Stateless"
 *           view-type="both" jndi-name="BwHistoryControllerHome"
 *           local-jndi-name="BwHistoryControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 * @author Matt Newman
 * 
 * @version 2.0
 */
public class BwHistoryControllerBean extends CSSessionBean implements SessionBean {

	private static final long serialVersionUID = 1L;

	private BwHistoryHelper bwHistoryHelper = new BwHistoryHelper();
	
    /**
     * Get the bw history entry for the given id.
     * 
     * @param bwHistoryId
     *            the id of the bw history entry to retrieve.
     * @return BwHistoryBasicValue
     * @throws BwHistoryControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public BwHistoryBasicValue getBwHistory(Integer bwHistoryId) throws BwHistoryControllerException {
        try {
            BwHistoryMaintainer bwHistoryMaintainer = new BwHistoryMaintainer();
            BwHistory bwHistory = bwHistoryMaintainer.findByPrimaryKey(bwHistoryId);
            BwHistoryBasicValue bwHistoryBasic = bwHistoryMaintainer.getBwHistoryBasicValue(bwHistory);
            return bwHistoryBasic; 
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
            throw new EJBException(ex);
        }
    }	
	
	/**
	 * Returns a Collection of BwHistoryValue(s)
	 * 
	 * @param defOnCaseId
	 *            Integer
	 * @return Collection: collection of BwHistoryValue
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws FinderException, BwHistoryControllerException
	 */
	public Collection findByDefendantOnCaseId(Integer defOnCaseId) throws BwHistoryControllerException {
		
        BwHistoryMaintainer bwHistoryMaintainer = new BwHistoryMaintainer();
    	try {
    		Collection bwHistoryColl = bwHistoryMaintainer.findByDefendantOnCaseId(defOnCaseId);
    		if(bwHistoryColl != null && !bwHistoryColl.isEmpty()) {
    			return bwHistoryColl;
    		} else {
    			return null;
    		}
			
		} catch (Exception ex) {
			CSServices.getDefaultErrorHandler().handleError(ex,  this.getClass());
			throw new BwHistoryControllerException("BW_HISTORY_NOT_FOUND", "Exception", ex);
		}
	}
	
	/**
	 * Returns a Collection of BwHistoryValue(s) with outstanding bench warrants for given defendant on case id
	 * 
	 * @param defOnCaseId
	 *            Integer
	 * @return Collection: collection of BwHistoryValue
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws FinderException, BwHistoryControllerException
	 */
	public Collection findOutstandingBenchWarrantsForDefOnCaseId(Integer defOnCaseId) throws BwHistoryControllerException {
		BwHistoryMaintainer bwHistoryMaintainer = new BwHistoryMaintainer();
		
		try {
 			Collection bwHistoryColl = bwHistoryMaintainer.findOutstandingBenchWarrantsForDefOnCaseId(defOnCaseId);
 			if(bwHistoryColl != null && !bwHistoryColl.isEmpty()) {
				return bwHistoryColl;
			} else {
				return null;
			}
		} catch (Exception ex) {
			CSServices.getDefaultErrorHandler().handleError(ex,  this.getClass());
			throw new BwHistoryControllerException("BW_HISTORY_NOT_FOUND", "Exception", ex);
		}
	}
	
	
    /**
     * Creates the bw history entry using the given BwHistoryBasicValue.
     * 
     * @param bwBV, defOnCase, userDisplayName
     *            BwHistoryBasicValue, DefendantOnCase, userDisplayName
     * @throws BwHistoryControllerException
     * 
     * @return integer 
     * 		   BwHistoryId
     * 
     * @ejb.interface-method view-type="both"
     */
    public Integer createBwHistory(BwHistoryBasicValue bwBV, Integer defOnCaseId, String userDisplayName) throws BwHistoryControllerException {
    	BwHistoryMaintainer bwHistoryMaintainer = new BwHistoryMaintainer();
    	
    	try {
    		BwHistory bwCreated = bwHistoryMaintainer.createBwHistory(bwBV, defOnCaseId, userDisplayName);
    		return bwCreated.getBwHistoryId();
    	} catch(Exception e ) {
	        CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
	        throw new EJBException(e);    	
		}
    }	
	
	
    /**
     * Updates the bw_history entry using the given BwHistoryValue.
     * 
     * @param bwValue
     *            BwHistoryValue
     * @param userDisplayName
     *            String
     * @throws BwHistoryControllerException
     * 
     * @ejb.interface-method view-type="both"
     */	
	public boolean updateBwHistory(BwHistoryValue bwValue, String userDisplayName) throws BwHistoryControllerException {
		log.debug("START: updateBwHistory");
		return bwHistoryHelper.updateBwHistory(bwValue, userDisplayName);
	}
	
	
    /**
     * Updates the bw_history entry using the given BwHistoryValue.
     * 
     * @param bwValue
     *            BwHistoryValue
     * @throws BwHistoryControllerException
     * 
     * @ejb.interface-method view-type="both"
     */	
    public void updateBwHistory(BwHistoryBasicValue bwBV, String userDisplayName) throws BwHistoryControllerException {
        BwHistoryMaintainer bwHistoryMaintainer = new BwHistoryMaintainer();
        
        bwHistoryMaintainer.update(bwBV, userDisplayName);
    }
    
    /**
     * Delete the court log and make the relevant changes to the bw_history entry.
     * 
     * @param Long 
     *            logEntryId
     * @param Integer
     *            defendantOnCaseId
     * @param Boolean
     *            isEndWarrantEvent
     * @param String
     *            userDisplayName
     * @throws BwHistoryControllerException
     * 
     * @ejb.interface-method view-type="both"
     */
    public void deleteCourtLogEntry(final Long logEntryId, final Integer defendantOnCaseId, final Boolean isEndWarrantEvent,
    		final String userDisplayName) throws BwHistoryControllerException {
    	bwHistoryHelper.deleteCourtLogEntry(logEntryId, defendantOnCaseId, isEndWarrantEvent, userDisplayName);
    }
}