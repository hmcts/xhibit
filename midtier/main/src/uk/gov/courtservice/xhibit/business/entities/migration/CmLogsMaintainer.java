package uk.gov.courtservice.xhibit.business.entities.migration;

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
import uk.gov.courtservice.xhibit.business.vos.entities.CmLogsBasicValue;

public class CmLogsMaintainer extends AbstractEntityMaintainer {
	
	private static Logger log = CSServices.getLogger(CmLogsMaintainer.class);

	private final static String YES = "Y";
	
	private CmLogsHome home = null;

	public CmLogsMaintainer() {
		if (home == null) {
			home = (CmLogsHome) CSServices.getServiceLocator().getLocalHome(CmLogsHome.class);
		}
	}

	public CmLogs findByPrimaryKey(Integer id) throws ObjectNotFoundException {
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
		
	public CmLogsBasicValue getBasicValue(CmLogs local) {
		CmLogsBasicValue basicValue = new CmLogsBasicValue(local.getCmLogsId(), local.getVersion());
		loadValue(basicValue, local);
		return basicValue;
	}

	protected void loadValue(CmLogsBasicValue basicValue, CmLogs local) {
		basicValue.setCmLogsId(local.getCmLogsId());
		basicValue.setOriginalUploadedFilename(local.getOriginalUploadedFilename());
		basicValue.setOriginalUploadedFileClobId(local.getOriginalUploadedFileClobId());
		basicValue.setProcessingStatus(local.getProcessingStatus());
		basicValue.setProcessingDatetime(local.getProcessingDatetime());
		basicValue.setLogsClobId(local.getLogsClobId());
		basicValue.setFileChecksum(local.getFileChecksum());
		basicValue.setUploadSize(local.getUploadSize());
		basicValue.setNoOfCasesInFile(local.getNoOfCasesInFile());
	}
	
	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof CmLogsBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			CmLogsBasicValue basicValue = (CmLogsBasicValue) value;
			CmLogs local = home.create(
					basicValue.getCmLogsId(), basicValue.getOriginalUploadedFilename(), basicValue.getOriginalUploadedFileClobId(),
					basicValue.getProcessingStatus(), basicValue.getProcessingDatetime(), basicValue.getLogsClobId(),
					basicValue.getFileChecksum(), basicValue.getUploadSize(),
					basicValue.getNoOfCasesInFile(), userDisplayName);
			return local;
		} catch (CreateException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof CmLogsBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current db values
			CmLogs local = home.findByPrimaryKey(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			CmLogsBasicValue basicValue = (CmLogsBasicValue) value;

			// Update the record
			local.setOriginalUploadedFilename(basicValue.getOriginalUploadedFilename());
			local.setOriginalUploadedFileClobId(basicValue.getOriginalUploadedFileClobId());
			local.setProcessingStatus(basicValue.getProcessingStatus());
			local.setProcessingDatetime(basicValue.getProcessingDatetime());
			local.setLogsClobId(basicValue.getLogsClobId());
			local.setFileChecksum(basicValue.getFileChecksum());
			local.setUploadSize(basicValue.getUploadSize());
			local.setNoOfCasesInFile(basicValue.getNoOfCasesInFile());
			
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
			CmLogs local = home.findByPrimaryKey(id);

			// Check the version
			if (local.getVersion() == null || version == null
					|| !local.getVersion().equals(version)) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			
	    	// Update the record
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