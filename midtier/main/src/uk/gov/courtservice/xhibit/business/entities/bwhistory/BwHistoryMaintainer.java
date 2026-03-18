package uk.gov.courtservice.xhibit.business.entities.bwhistory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.BwHistoryBasicValue;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseMaintainer;

public class BwHistoryMaintainer extends AbstractEntityMaintainer {

	private BwHistoryHome home = null;
	
	private static Logger log = CSServices.getLogger(BwHistoryHome.class);
	
	public BwHistoryMaintainer() {
		if(home == null) {
			home = (BwHistoryHome) CSServices.getServiceLocator().getLocalHome(BwHistoryHome.class);
		}
	}
	
	public Collection getBwHistoryBasicValues(Collection local) {
		ArrayList newValues = new ArrayList();
		
		Iterator localIterator = local.iterator();
		int num = 0;
        while (localIterator.hasNext()) {	
        	BwHistory localCopy = (BwHistory) localIterator.next();
        	BwHistoryBasicValue value = new BwHistoryBasicValue(localCopy.getBwHistoryId(), localCopy.getVersion());
        	setBwHistory(value, localCopy);
        	newValues.add(value);
        	log.debug("BwHistoryMaintainer, getBwHistoryBasicValues, Id: " + value.getId() 
        														+ ", BcStatusBwIssued: " + value.getBcStatusBwIssued()
        														+ ", BcStatusBwEnded: " + value.getBcStatusBwEnded() 
        														+ ", DefOnCaseId: " + value.getDefendantOnCaseId()
        														+ ", Coll. No: " + num);
        	num++;
        }
        
        return newValues;
	}
	
	public BwHistoryBasicValue getBwHistoryBasicValue(BwHistory local) {
		BwHistoryBasicValue value = new BwHistoryBasicValue(local.getBwHistoryId(), local.getVersion());
		setBwHistory(value, local);
		return value;
	}

	private void setBwHistory(BwHistoryBasicValue value, BwHistory local) {
		value.setAbsconding(local.getAbsconding());
		value.setBcStatusBwEnded(local.getBcStatusBwEnded());
		value.setBcStatusBwIssued(local.getBcStatusBwIssued());
		value.setBwEndDate(local.getBwEndDate());
		value.setBwIssueDate(local.getBwIssueDate());
		value.setDefendantOnCaseId(local.getDefendantOnCaseId());
		value.setObsInd(local.getObsInd());
		value.setVersion(local.getVersion());
		value.setWithdrawn(local.getWithdrawn());
	}

	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		throw new java.lang.UnsupportedOperationException();
	}

	public void update(CSAbstractValue value, String userDisplayName) {
		if(!(value instanceof BwHistoryBasicValue)) 
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());

		BwHistoryHome home = (BwHistoryHome) CSServices.getServiceLocator().getLocalHome(BwHistoryHome.class);
		BwHistoryBasicValue bwHistoryBasicValue = (BwHistoryBasicValue) value;
		BwHistory bwHistory;
		
		try {
			bwHistory = home.findByPrimaryKey(value.getId());
			
			if(bwHistory != null) {	// entry found
				// Check version
				if(bwHistory.getVersion() == null || value.getVersion() == null || !bwHistory.getVersion().equals(value.getVersion())) {
		            throw new OptimisticLockException("Optimistic Lock Error");
				} else {
					bwHistory.setAbsconding(bwHistoryBasicValue.getAbsconding());
					bwHistory.setBcStatusBwEnded(bwHistoryBasicValue.getBcStatusBwEnded());
					bwHistory.setBcStatusBwIssued(bwHistoryBasicValue.getBcStatusBwIssued());
					bwHistory.setBwEndDate(bwHistoryBasicValue.getBwEndDate());
					bwHistory.setBwIssueDate(bwHistoryBasicValue.getBwIssueDate());
					//bwHistory.setDefendantOnCaseId(bwHistoryBasicValue.getDefendantOnCaseId());
					
					if(bwHistoryBasicValue.getObsInd() != null) {
						bwHistory.setObsInd(bwHistoryBasicValue.getObsInd());
					}
		
					//bwHistory.setVersion(bwHistoryBasicValue.getVersion());
					bwHistory.setWithdrawn(bwHistoryBasicValue.getWithdrawn());	
					bwHistory.setUpdated(userDisplayName);
				}
			}
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	// DON'T USE DELETE, USE UPDATE WITH OBS_IND NOT NULL!
	public void delete(Integer id, Integer version) {
		throw new java.lang.UnsupportedOperationException();
	}
	
	public Collection findByDefendantOnCaseId(Integer defOnCaseId) {
		log.debug("*** entered into findByDefendantOnCaseId ***");
		try {
			return getBwHistoryBasicValues(home.findByDefendantOnCaseId(defOnCaseId));
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}	
	
	public Collection findOutstandingBenchWarrantsForDefOnCaseId(Integer defOnCaseId) {
		try {
			return getBwHistoryBasicValues(home.findOutstandingBenchWarrantsForDefOnCaseId(defOnCaseId));
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}
	
	 /**
     * Bw History creation process
     * @param value
     * return bw history id
     */
    public BwHistory createBwHistory(CSAbstractValue value, Integer defOnCaseId, String userDisplayName)  {
    	BwHistoryBasicValue val = (BwHistoryBasicValue) value;
    	DefendantOnCaseMaintainer defOnCaseMaintainer = new DefendantOnCaseMaintainer();
    	DefendantOnCase defOnCase;
    	
    	try {
    		defOnCase = defOnCaseMaintainer.findByPrimaryKey(defOnCaseId);
    		
    		log.debug("BwHistoryMaintainer: To be created value's BcStatusBwIssued: " + val.getBcStatusBwIssued());
    	
    		BwHistory bwHistoryCreated = home.create(defOnCase, val.getBwIssueDate(), val.getBwEndDate(), val.getBcStatusBwIssued(),
    				val.getBcStatusBwEnded(), val.getWithdrawn(), val.getAbsconding(), val.getObsInd(), userDisplayName);
    		
    		log.debug("BwHistoryMaintainer: Created value's BcStatusBwIssued: " + bwHistoryCreated.getBcStatusBwIssued());
    		return bwHistoryCreated;
    	} catch (Exception e) {
    		CSServices.getDefaultErrorHandler().handleError(e, getClass());
    		throw new EJBException(e);
    	}	
	}	
    
    public BwHistory findByPrimaryKey(Integer id) throws ObjectNotFoundException {
        try {
            return home.findByPrimaryKey(id);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new EJBException(e);
        }
    }
	
}