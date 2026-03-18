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
import uk.gov.courtservice.xhibit.business.vos.entities.PddaMessageBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.PddaMessageComplexValue;

public class PddaMessageMaintainer extends AbstractEntityMaintainer {
	
	private static Logger log = CSServices.getLogger(RefPddaMessageTypeHome.class);

	private final static String YES = "Y";
	
	private PddaMessageHome home = null;

	public PddaMessageMaintainer() {
		if (home == null) {
			home = (PddaMessageHome) CSServices.getServiceLocator().getLocalHome(PddaMessageHome.class);
		}
	}

	public PddaMessage findByPrimaryKey(Integer id) throws ObjectNotFoundException {
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

	public Collection<PddaMessage> findByBatchId(Integer pddaBatchId) throws ObjectNotFoundException {
		log.debug("*** entered into findByBatchId ***");
		try {
			return home.findByBatchId(pddaBatchId);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}
	
	public PddaMessageBasicValue getBasicValue(PddaMessage local) {
		PddaMessageBasicValue basicValue = new PddaMessageBasicValue(local.getPddaMessageId(), local.getVersion());
		loadValue(basicValue, local);
		return basicValue;
	}

	public PddaMessageComplexValue getComplexValue(PddaMessage local) {
		PddaMessageComplexValue complexValue = new PddaMessageComplexValue(local.getPddaMessageId(), local.getVersion());
		loadValue(complexValue, local);
		return complexValue;
	}
	
	protected void loadValue(PddaMessageBasicValue basicValue, PddaMessage local) {
		basicValue.setPddaMessageId(local.getPddaMessageId());
		basicValue.setCourtId(local.getCourtId());
		basicValue.setCourtRoomId(local.getCourtRoomId());
		basicValue.setPddaMessageGuid(local.getPddaMessageGuid());
		basicValue.setPddaMessageTypeId(local.getPddaMessageTypeId());
		basicValue.setPddaMessageDataId(local.getPddaMessageDataId());
		basicValue.setPddaBatchId(local.getPddaBatchId());
		basicValue.setTimeSent(local.getTimeSent());
		basicValue.setCpDocumentName(local.getCpDocumentName());
		basicValue.setCpDocumentStatus(local.getCpDocumentStatus());
		basicValue.setCpResponseGenerated(local.getCpResponseGenerated());
		basicValue.setCpStagingInboundId(local.getCpStagingInboundId());
		basicValue.setObsInd(local.getObsInd());
	}
	
	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof PddaMessageBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			PddaMessageBasicValue basicValue = (PddaMessageBasicValue) value;
			PddaMessage local = home.create(
					basicValue.getPddaMessageId(), basicValue.getCourtId(),
					basicValue.getCourtRoomId(), basicValue.getPddaMessageGuid(),
					basicValue.getPddaMessageTypeId(), basicValue.getPddaMessageDataId(),
					basicValue.getPddaBatchId(), basicValue.getTimeSent(),
					basicValue.getCpDocumentName(), basicValue.getCpDocumentStatus(),
					basicValue.getCpResponseGenerated(), basicValue.getCpStagingInboundId(),
					basicValue.getObsInd(), userDisplayName);
			return local;
		} catch (CreateException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof PddaMessageBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current db values
			PddaMessage local = home.findByPrimaryKey(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			PddaMessageBasicValue basicValue = (PddaMessageBasicValue) value;

			// Update the record
			local.setCourtId(basicValue.getCourtId());
			local.setCourtRoomId(basicValue.getCourtRoomId());
			local.setPddaMessageGuid(basicValue.getPddaMessageGuid());
			local.setPddaMessageTypeId(basicValue.getPddaMessageTypeId());
			local.setPddaMessageDataId(basicValue.getPddaMessageDataId());
			local.setPddaBatchId(basicValue.getPddaBatchId());
			local.setTimeSent(basicValue.getTimeSent());
			local.setCpDocumentName(basicValue.getCpDocumentName());
			local.setCpDocumentStatus(basicValue.getCpDocumentStatus());
			local.setCpResponseGenerated(basicValue.getCpResponseGenerated());
			local.setCpStagingInboundId(basicValue.getCpStagingInboundId());
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
			PddaMessage local = home.findByPrimaryKey(id);

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