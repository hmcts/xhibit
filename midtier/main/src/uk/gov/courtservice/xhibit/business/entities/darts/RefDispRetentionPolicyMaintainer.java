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
import uk.gov.courtservice.xhibit.business.vos.entities.RefDispRetentionPolicyBasicValue;

public class RefDispRetentionPolicyMaintainer extends AbstractEntityMaintainer {

	private RefDispRetentionPolicyHome home = null;

	private static Logger log = CSServices.getLogger(RefDispRetentionPolicyHome.class);

	public RefDispRetentionPolicyMaintainer() {
		if (home == null) {
			home = (RefDispRetentionPolicyHome) CSServices.getServiceLocator().getLocalHome(RefDispRetentionPolicyHome.class);
		}
	}

	public RefDispRetentionPolicy findByPrimaryKey(Integer id) throws ObjectNotFoundException {
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

	public Collection<RefDispRetentionPolicy> findByDisposalCode(String disposalCode) throws ObjectNotFoundException {
		log.debug("*** entered into findByDisposalCode ***");
		try {
			return home.findByDisposalCode(disposalCode);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}
	
	public RefDispRetentionPolicyBasicValue getBasicValue(RefDispRetentionPolicy local) {
		RefDispRetentionPolicyBasicValue basicValue = new RefDispRetentionPolicyBasicValue(local.getRefDarRetentionPolicyId(), local.getVersion());
		loadValue(basicValue, local);
		return basicValue;
	}

	protected void loadValue(RefDispRetentionPolicyBasicValue basicValue, RefDispRetentionPolicy local) {
		basicValue.setRefDispRetentionPolicyId(local.getRefDispRetentionPolicyId());
		basicValue.setDisposalCode(local.getDisposalCode());
		basicValue.setRefDarRetentionPolicyId(local.getRefDarRetentionPolicyId());
		basicValue.setAllowsConsecutive(local.getAllowsConsecutive());
		basicValue.setHasLife(local.getHasLife());
		basicValue.setHasDuration(local.getHasDuration());
		basicValue.setObsInd(local.getObsInd());
	}
	
	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof RefDispRetentionPolicyBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			RefDispRetentionPolicyBasicValue basicValue = (RefDispRetentionPolicyBasicValue) value;
			RefDispRetentionPolicy local = home.create(basicValue.getRefDispRetentionPolicyId(),
					basicValue.getDisposalCode(), basicValue.getRefDarRetentionPolicyId(),
					basicValue.getAllowsConsecutive(), basicValue.getHasLife(), basicValue.getHasDuration(),
					userDisplayName);
			return local;
		} catch (CreateException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof RefDispRetentionPolicyBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current db values
			RefDispRetentionPolicy local = home.findByPrimaryKey(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			RefDispRetentionPolicyBasicValue basicValue = (RefDispRetentionPolicyBasicValue) value;

			// Update the record
			local.setDisposalCode(basicValue.getDisposalCode());
			local.setRefDarRetentionPolicyId(basicValue.getRefDarRetentionPolicyId());
			local.setAllowsConsecutive(basicValue.getAllowsConsecutive());
			local.setHasLife(basicValue.getHasLife());
			local.setHasDuration(basicValue.getHasDuration());
			local.setObsInd(basicValue.getObsInd());
			local.setUpdated(userDisplayName);

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
	
	public void delete(CSAbstractValue value, String userDisplayName) {	
		if (!(value instanceof RefDispRetentionPolicyBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current db values
			RefDispRetentionPolicy local = home.findByPrimaryKey(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			RefDispRetentionPolicyBasicValue basicValue = (RefDispRetentionPolicyBasicValue) value;
	 
	    	// Update the record
			local.setObsInd(basicValue.getObsInd());;
			local.setUpdated(userDisplayName);
			
	    } catch (ObjectNotFoundException ex) {
	        CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	        throw new EJBException(ex);
	    } catch (FinderException ex) {
	        CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	        throw new EJBException(ex);
	    }		
	}
}