package uk.gov.courtservice.xhibit.business.entities.pdda;

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
import uk.gov.courtservice.xhibit.business.vos.entities.RefPddaMessageTypeBasicValue;

public class RefPddaMessageTypeMaintainer extends AbstractEntityMaintainer {

	private final static String YES = "Y";
	
	private static Logger log = CSServices.getLogger(RefPddaMessageTypeHome.class);
	
	private RefPddaMessageTypeHome home = null;

	public RefPddaMessageTypeMaintainer() {
		if (home == null) {
			home = (RefPddaMessageTypeHome) CSServices.getServiceLocator().getLocalHome(RefPddaMessageTypeHome.class);
		}
	}

	public RefPddaMessageType findByPrimaryKey(Integer id) throws ObjectNotFoundException {
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

	public RefPddaMessageType findByMessageType(String messageType) throws ObjectNotFoundException {
		log.debug("*** entered into findByMessageType ***");
		try {
			return home.findByMessageType(messageType);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	
	public RefPddaMessageTypeBasicValue getBasicValue(RefPddaMessageType local) {
		RefPddaMessageTypeBasicValue basicValue = new RefPddaMessageTypeBasicValue(local.getRefPddaMessageTypeId(), local.getVersion());
		loadValue(basicValue, local);
		return basicValue;
	}
	
	protected void loadValue(RefPddaMessageTypeBasicValue basicValue, RefPddaMessageType local) {
		basicValue.setRefPddaMessageTypeId(local.getRefPddaMessageTypeId());
		basicValue.setPddaMessageType(local.getPddaMessageType());
		basicValue.setPddaMessageTypeDescription(local.getPddaMessageTypeDescription());
		basicValue.setObsInd(local.getObsInd());
	}
	
	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof RefPddaMessageTypeBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			RefPddaMessageTypeBasicValue basicValue = (RefPddaMessageTypeBasicValue) value;
			RefPddaMessageType local = home.create(basicValue.getRefPddaMessageTypeId(), 
					basicValue.getPddaMessageType(), basicValue.getPddaMessageTypeDescription(), 
					basicValue.getObsInd(), userDisplayName);
			return local;
		} catch (CreateException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof RefPddaMessageTypeBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current db values
			RefPddaMessageType local = home.findByPrimaryKey(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			RefPddaMessageTypeBasicValue basicValue = (RefPddaMessageTypeBasicValue) value;

			// Update the record
			local.setPddaMessageType(basicValue.getPddaMessageType());
			local.setPddaMessageTypeDescription(basicValue.getPddaMessageTypeDescription());
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
			RefPddaMessageType local = home.findByPrimaryKey(id);

			// Check the version
			if (local.getVersion() == null || version == null
					|| !local.getVersion().equals(version)) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			
	    	// Update the record
			local.setObsInd(YES);
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
}