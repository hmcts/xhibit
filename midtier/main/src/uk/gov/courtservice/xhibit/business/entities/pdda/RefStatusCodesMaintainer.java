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
import uk.gov.courtservice.xhibit.business.vos.entities.RefStatusCodesBasicValue;

public class RefStatusCodesMaintainer extends AbstractEntityMaintainer {

	private final static String YES = "Y";
	
	private static Logger log = CSServices.getLogger(RefStatusCodesHome.class);
	
	private RefStatusCodesHome home = null;

	public RefStatusCodesMaintainer() {
		if (home == null) {
			home = (RefStatusCodesHome) CSServices.getServiceLocator().getLocalHome(RefStatusCodesHome.class);
		}
	}

	public RefStatusCodes findByPrimaryKey(Integer id) throws ObjectNotFoundException {
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
	
	public RefStatusCodes findByCode(String statusCode) throws ObjectNotFoundException {
		log.debug("*** entered into findByCode ***");
		try {
			return home.findByCode(statusCode);
			
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}
	public RefStatusCodesBasicValue getBasicValue(RefStatusCodes local) {
		RefStatusCodesBasicValue basicValue = new RefStatusCodesBasicValue(local.getRefStatusCodeId(), local.getVersion());
		loadValue(basicValue, local);
		return basicValue;
	}
	
	protected void loadValue(RefStatusCodesBasicValue basicValue, RefStatusCodes local) {
		basicValue.setRefStatusCodeId(local.getRefStatusCodeId());
		basicValue.setStatusCodeType(local.getStatusCodeType());
		basicValue.setStatusCode(local.getStatusCode());
		basicValue.setStatusCodeDescription(local.getStatusCodeDescription());
		basicValue.setObsInd(local.getObsInd());
	}
	
	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof RefStatusCodesBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			RefStatusCodesBasicValue basicValue = (RefStatusCodesBasicValue) value;
			RefStatusCodes local = home.create(basicValue.getRefStatusCodeId(), 
					basicValue.getStatusCodeType(), basicValue.getStatusCode(),
					basicValue.getStatusCodeDescription(), basicValue.getObsInd(), userDisplayName);
			return local;
		} catch (CreateException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof RefStatusCodesBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current db values
			RefStatusCodes local = home.findByPrimaryKey(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			RefStatusCodesBasicValue basicValue = (RefStatusCodesBasicValue) value;

			// Update the record
			local.setStatusCodeType(basicValue.getStatusCodeType());
			local.setStatusCode(basicValue.getStatusCode());
			local.setStatusCodeDescription(basicValue.getStatusCodeDescription());
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
			RefStatusCodes local = home.findByPrimaryKey(id);

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