package uk.gov.courtservice.xhibit.business.entities.darts;

import java.util.Collection;

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
import uk.gov.courtservice.xhibit.business.vos.entities.DarRetentionPolicyBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DarRetentionPolicyComplexValue;

public class DarRetentionPolicyMaintainer extends AbstractEntityMaintainer {

	private final static String YES = "Y";
	
	private static Logger log = CSServices.getLogger(DarRetentionPolicyHome.class);
	
	private DarRetentionPolicyHome home = null;

	public DarRetentionPolicyMaintainer() {
		if (home == null) {
			home = (DarRetentionPolicyHome) CSServices.getServiceLocator().getLocalHome(DarRetentionPolicyHome.class);
		}
	}

	public DarRetentionPolicy findByPrimaryKey(Integer id) throws ObjectNotFoundException {
		log.debug("*** entered into findByPrimaryKey ***");
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
	
	public DarRetentionPolicyComplexValue findComplexValueById(Integer id) throws ObjectNotFoundException {
		log.debug("*** entered into findComplexValueById ***");
		DarRetentionPolicy local = findByPrimaryKey(id);
		if (local != null) {
			return getComplexValue(local);
		}
		return null;
	}

	public Collection<DarRetentionPolicy> findByDisposal2Id(Integer disposal2Id) throws ObjectNotFoundException {
		log.debug("*** entered into findByDisposal2Id ***");
		try {
			return home.findByDisposal2Id(disposal2Id);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}
	
	private Collection<DarRetentionPolicy> findByCaseId(Integer caseId) throws ObjectNotFoundException {
		log.debug("*** entered into findByCaseId ***");
		try {
			return home.findByCaseId(caseId);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}
	
	public DarRetentionPolicyComplexValue getCaseDarRetentionPolicy(final Integer caseId) throws ObjectNotFoundException {
		DarRetentionPolicyComplexValue result = null;
		Collection<DarRetentionPolicy> darLocals = findByCaseId(caseId);
		for (DarRetentionPolicy darLocal : darLocals) {
			if (darLocal.getDisposal2Id() == null) {
				result = getComplexValue(darLocal);
				break;
			}
		}
		return result;
	}
	
	public DarRetentionPolicyBasicValue getBasicValue(DarRetentionPolicy local) {
		DarRetentionPolicyBasicValue basicValue = new DarRetentionPolicyBasicValue(local.getDarRetentionPolicyId(), local.getVersion());
		loadValue(basicValue, local);
		return basicValue;
	}

	public DarRetentionPolicyComplexValue getComplexValue(DarRetentionPolicy local) {
		DarRetentionPolicyComplexValue complexValue = new DarRetentionPolicyComplexValue(local.getDarRetentionPolicyId(), local.getVersion());
		loadValue(complexValue, local);
		return complexValue;
	}
	
	protected void loadValue(DarRetentionPolicyBasicValue basicValue, DarRetentionPolicy local) {
		basicValue.setDarRetentionPolicyId(local.getDarRetentionPolicyId());
		basicValue.setDisposal2Id(local.getDisposal2Id());
		basicValue.setCaseId(local.getCaseId());
		basicValue.setDefendantOnCaseId(local.getDefendantOnCaseId());
		basicValue.setDefendantOnOffenceId(local.getDefendantOnOffenceId());	
		basicValue.setRefDispRetentionPolicyId(local.getRefDispRetentionPolicyId());
		basicValue.setRefDarRetentionPolicyId(local.getRefDarRetentionPolicyId());
		basicValue.setDurationDays(local.getDurationDays());
		basicValue.setDurationMonths(local.getDurationMonths());
		basicValue.setDurationYears(local.getDurationYears());
		basicValue.setHasLife(local.getHasLife());
		basicValue.setIsConsecutive(local.getIsConsecutive());
		basicValue.setIsUpdated(local.getIsUpdated());
		basicValue.setObsInd(local.getObsInd());
	}
	
	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof DarRetentionPolicyBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			DarRetentionPolicyBasicValue basicValue = (DarRetentionPolicyBasicValue) value;
			log.debug("create(caseId="+basicValue.getCaseId()
					+",disposal2Id="+basicValue.getDisposal2Id()+") - "
					+basicValue.getDurationDays()+"D"
					+basicValue.getDurationMonths()+"M"
					+basicValue.getDurationYears()+"Y");
			DarRetentionPolicy local = home.create(basicValue.getDarRetentionPolicyId(), 
					basicValue.getDisposal2Id(), basicValue.getCaseId(), basicValue.getDefendantOnCaseId(), 
					basicValue.getDefendantOnOffenceId(), basicValue.getRefDispRetentionPolicyId(),
					basicValue.getRefDarRetentionPolicyId(),
					basicValue.getDurationDays(), basicValue.getDurationMonths(), basicValue.getDurationYears(),
					basicValue.getHasLife(), basicValue.getIsConsecutive(), basicValue.getIsUpdated(),
					basicValue.getObsInd(), userDisplayName);
			return local;
		} catch (CreateException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof DarRetentionPolicyBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current db values
			DarRetentionPolicy local = home.findByPrimaryKey(value.getId());

			// Check the version
			checkVersions(local.getVersion(), value.getVersion());
			
			DarRetentionPolicyBasicValue basicValue = (DarRetentionPolicyBasicValue) value;
			log.debug("update("+value.getId()+") - "
					+basicValue.getDurationDays()+"D"
					+basicValue.getDurationMonths()+"M"
					+basicValue.getDurationYears()+"Y"
					+" v"+local.getVersion());
			// Update the record
			local.setDisposal2Id(basicValue.getDisposal2Id());
			local.setCaseId(basicValue.getCaseId());
			local.setDefendantOnCaseId(basicValue.getDefendantOnCaseId());
			local.setDefendantOnOffenceId(basicValue.getDefendantOnOffenceId());
			local.setRefDispRetentionPolicyId(basicValue.getRefDispRetentionPolicyId());
			local.setRefDarRetentionPolicyId(basicValue.getRefDarRetentionPolicyId());
			local.setDurationDays(basicValue.getDurationDays());
			local.setDurationMonths(basicValue.getDurationMonths());
			local.setDurationYears(basicValue.getDurationYears());
			local.setHasLife(basicValue.getHasLife());
			local.setIsConsecutive(basicValue.getIsConsecutive());
			local.setIsUpdated(basicValue.getIsUpdated());
			local.setObsInd(basicValue.getObsInd());
			if (userDisplayName != null) {
				local.setUpdated(userDisplayName);
			}
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new EJBException(ex);
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new EJBException(ex);
		}
	}

	@Override
	public void delete(Integer id, Integer version) {
		throw new java.lang.UnsupportedOperationException();
	}
	
    public void delete(Integer id, Integer version, String userDisplayName) {
    	try {
			// Get the current db values
			DarRetentionPolicy local = home.findByPrimaryKey(id);

			// Check the version
			checkVersions(local.getVersion(),version);
			
	    	// Update the record
			local.setObsInd(YES);
			local.setUpdated(userDisplayName);
			
	    } catch (ObjectNotFoundException ex) {
	        CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	        throw new EJBException(ex);
	    } catch (FinderException ex) {
	        CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	        throw new EJBException(ex);
	    }		

    }
    
    private void checkVersions(Integer localVersion, Integer bvVersion) throws OptimisticLockException {
    	Integer dbVersion = localVersion != null ? localVersion : 1;
    	Integer screenVersion = bvVersion != null ? bvVersion : 1;
    	if (!dbVersion.equals(screenVersion)) {
			throw new OptimisticLockException("Optimistic Lock Error");
		}
    }
}