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
import uk.gov.courtservice.xhibit.business.vos.entities.RefDarRetentionPoliciesBasicValue;

public class RefDarRetentionPoliciesMaintainer extends AbstractEntityMaintainer {

	private RefDarRetentionPoliciesHome home = null;

	private static Logger log = CSServices.getLogger(RefDarRetentionPoliciesHome.class);

	public RefDarRetentionPoliciesMaintainer() {
		if (home == null) {
			home = (RefDarRetentionPoliciesHome) CSServices.getServiceLocator().getLocalHome(RefDarRetentionPoliciesHome.class);
		}
	}

	public RefDarRetentionPolicies findByPrimaryKey(Integer id) throws ObjectNotFoundException {
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

	@SuppressWarnings("unchecked")
	public Collection<RefDarRetentionPolicies> findAllPolicies() throws ObjectNotFoundException {
		log.debug("*** entered into findAll ***");
		try {
			return home.findAllPolicies();
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public RefDarRetentionPoliciesBasicValue getBasicValue(RefDarRetentionPolicies local) {
		RefDarRetentionPoliciesBasicValue basicValue = new RefDarRetentionPoliciesBasicValue(local.getRefDarRetentionPolicyId(), local.getVersion());
		loadValue(basicValue, local);
		return basicValue;
	}

	protected void loadValue(RefDarRetentionPoliciesBasicValue basicValue, RefDarRetentionPolicies local) {
		basicValue.setRefDarRetentionPolicyId(local.getRefDarRetentionPolicyId());
		basicValue.setPolicyNo(local.getPolicyNo());
		basicValue.setPolicyDescription(local.getPolicyDescription());
		basicValue.setObsInd(local.getObsInd());
	}
	
	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof RefDarRetentionPoliciesBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			RefDarRetentionPoliciesBasicValue basicValue = (RefDarRetentionPoliciesBasicValue) value;
			RefDarRetentionPolicies local = home.create(basicValue.getRefDarRetentionPolicyId(), 
					basicValue.getPolicyNo(), basicValue.getPolicyDescription(),
					userDisplayName);
			return local;
		} catch (CreateException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof RefDarRetentionPoliciesBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current db values
			RefDarRetentionPolicies local = home.findByPrimaryKey(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			RefDarRetentionPoliciesBasicValue basicValue = (RefDarRetentionPoliciesBasicValue) value;

			// Update the record
			local.setRefDarRetentionPolicyId(basicValue.getRefDarRetentionPolicyId());
			local.setPolicyNo(basicValue.getPolicyNo());
			local.setPolicyDescription(basicValue.getPolicyDescription());
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
		if (!(value instanceof RefDarRetentionPoliciesBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current db values
			RefDarRetentionPolicies local = home.findByPrimaryKey(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			RefDarRetentionPoliciesBasicValue basicValue = (RefDarRetentionPoliciesBasicValue) value;
	 
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