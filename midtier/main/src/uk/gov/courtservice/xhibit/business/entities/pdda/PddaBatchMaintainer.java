package uk.gov.courtservice.xhibit.business.entities.pdda;

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
import uk.gov.courtservice.xhibit.business.vos.entities.PddaBatchBasicValue;

public class PddaBatchMaintainer extends AbstractEntityMaintainer {
	
	private static Logger log = CSServices.getLogger(PddaBatchHome.class);

	private final static String YES = "Y";
	
	private PddaBatchHome home = null;

	public PddaBatchMaintainer() {
		if (home == null) {
			home = (PddaBatchHome) CSServices.getServiceLocator().getLocalHome(PddaBatchHome.class);
		}
	}

	public PddaBatch findByPrimaryKey(Integer id) throws ObjectNotFoundException {
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

	public Collection<PddaBatch> findOpenBatch(Integer refStatusCodesId) throws ObjectNotFoundException {
		log.debug("*** entered into findOpenBatch ***");
		try {
			return home.findOpenBatch(refStatusCodesId);
			
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public Collection<PddaBatch> findReadyToSendBatches(Integer refStatusCodesId) throws ObjectNotFoundException {
		log.debug("*** entered into findReadyToSendBatches ***");
		try {
			return home.findReadyToSendBatches(refStatusCodesId);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public Collection<PddaBatch> findBatchesToResend(Integer maxResends, Integer refStatusCodesId) throws ObjectNotFoundException {
		log.debug("*** entered into findBatchesToResend ***");
		try {
			return home.findBatchesToResend(maxResends, refStatusCodesId);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}
	
	public PddaBatchBasicValue getBasicValue(PddaBatch local) {
		PddaBatchBasicValue basicValue = new PddaBatchBasicValue(local.getPddaBatchId(), local.getVersion());
		loadValue(basicValue, local);
		return basicValue;
	}
	
	protected void loadValue(PddaBatchBasicValue basicValue, PddaBatch local) {
		basicValue.setPddaBatchId(local.getPddaBatchId());
		basicValue.setNoOfRecordsInBatch(local.getNoOfRecordsInBatch());
		basicValue.setBatchOpenedDatetime(local.getBatchOpenedDatetime());
		basicValue.setBatchClosedDatetime(local.getBatchClosedDatetime());
		basicValue.setBatchStatusId(local.getBatchStatusId());
		basicValue.setBatchMessage(local.getBatchMessage());
		basicValue.setBatchNoResends(local.getBatchNoResends());
		basicValue.setBatchSentTime(local.getBatchSentTime());
		basicValue.setObsInd(local.getObsInd());
	}
	
	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof PddaBatchBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			PddaBatchBasicValue basicValue = (PddaBatchBasicValue) value;
			PddaBatch local = home.create(
					basicValue.getPddaBatchId(), basicValue.getNoOfRecordsInBatch(),
					basicValue.getBatchOpenedDatetime(), basicValue.getBatchClosedDatetime(),
					basicValue.getBatchStatusId(), basicValue.getBatchMessage(),
					basicValue.getBatchNoResends(), basicValue.getBatchSentTime(),
					basicValue.getObsInd(), userDisplayName);
			return local;
		} catch (CreateException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof PddaBatchBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current db values
			if (value != null) {
				log.debug("About to try and update a PDDA batch ; value.getId() == " + value.getId());
			} else {
				log.debug("About to try and update a PDDA batch; value is null ");
			}
			PddaBatch local = home.findByPrimaryKey(value.getId());

			// Check the version but DO NOT error at this stage!
			
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				log.warn("Version conflict: db="+local.getVersion()+" , client="+value.getVersion()+" , id="+value.getId());
				log.warn("Used to ..... throw new OptimisticLockException here");
			}
			PddaBatchBasicValue basicValue = (PddaBatchBasicValue) value;

			// Update the record
			local.setNoOfRecordsInBatch(basicValue.getNoOfRecordsInBatch());
			local.setBatchOpenedDatetime(basicValue.getBatchOpenedDatetime());
			local.setBatchClosedDatetime(basicValue.getBatchClosedDatetime());
			local.setBatchStatusId(basicValue.getBatchStatusId());
			local.setBatchMessage(basicValue.getBatchMessage());
			local.setBatchNoResends(basicValue.getBatchNoResends());
			local.setBatchSentTime(basicValue.getBatchSentTime());
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
			PddaBatch local = home.findByPrimaryKey(id);

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