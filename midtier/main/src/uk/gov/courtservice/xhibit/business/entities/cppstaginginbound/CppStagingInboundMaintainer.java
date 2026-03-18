package uk.gov.courtservice.xhibit.business.entities.cppstaginginbound;


import java.util.Collection;
import java.util.Date;

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
import uk.gov.courtservice.xhibit.business.entities.cpplist.CppListHome;
import uk.gov.courtservice.xhibit.business.services.cppstaginginbound.CppStagingInboundHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.CppStagingInboundBasicValue;

public class CppStagingInboundMaintainer extends AbstractEntityMaintainer {

	private CppStagingInboundHome home = null;

	private static Logger log = CSServices.getLogger(CppStagingInboundHome.class);

	public CppStagingInboundMaintainer() {
		if (home == null) {
			home = (CppStagingInboundHome) CSServices.getServiceLocator().getLocalHome(CppStagingInboundHome.class);
		}
	}
	
	public CppStagingInboundBasicValue getCppStagingInboundBasicValue(CppStagingInbound local) {
		CppStagingInboundBasicValue value = new CppStagingInboundBasicValue(local.getCppStagingInboundId(), local.getVersion());
		setCppStagingInboundBasicValue(value, local);
		return value;
	}

	
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		log.debug(ENTER_METHOD + "create cppStagingInboundvalue");
		if (!(value instanceof CppStagingInboundBasicValue)) {
	        throw new IllegalArgumentException("Unexpected type:" + value.getClass());
	    }
		try {
			CppStagingInboundBasicValue cppStagingInboundBV = (CppStagingInboundBasicValue) value;
			return home.create(cppStagingInboundBV.getDocumentName(), cppStagingInboundBV.getCourtCode(), cppStagingInboundBV.getDocumentType(), cppStagingInboundBV.getTimeLoaded(), cppStagingInboundBV.getClobId(),
					cppStagingInboundBV.getValidationStatus(), cppStagingInboundBV.getAcknowledgmentStatus(), cppStagingInboundBV.getProcessingStatus(), cppStagingInboundBV.getValidationErrorMessage(), userDisplayName);
		} catch (CreateException e) {
			  CSServices.getDefaultErrorHandler().handleError(e, getClass());
	          throw new EJBException(e);
		}   
	}

	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
	    log.debug(ENTER_METHOD + "update cppStagingInboundvalue");
		if (!(value instanceof CppStagingInboundBasicValue)) {
	        throw new IllegalArgumentException("Unexpected type:" + value.getClass());
	    }
		
		try {
		    // Get the current db values
		    CppStagingInbound cppStagingInbound = home.findByPrimaryKey(value.getId());
		       
		    // Check the version
		    if (cppStagingInbound.getVersion() == null || value.getVersion() == null || !cppStagingInbound.getVersion().equals(value.getVersion())) {
		        throw new OptimisticLockException("Optimistic Lock Error");
		    }
		    CppStagingInboundBasicValue cppStagingInboundBV = (CppStagingInboundBasicValue) value;
		    cppStagingInbound.setDocumentName(cppStagingInboundBV.getDocumentName());
		    cppStagingInbound.setCourtCode(cppStagingInboundBV.getCourtCode());
		    cppStagingInbound.setDocumentType(cppStagingInboundBV.getDocumentType());
		    cppStagingInbound.setTimeLoaded(cppStagingInboundBV.getTimeLoaded());
		    cppStagingInbound.setClobId(cppStagingInboundBV.getClobId());
		    cppStagingInbound.setValidationStatus(cppStagingInboundBV.getValidationStatus());
		    cppStagingInbound.setAcknowledgmentStatus(cppStagingInboundBV.getAcknowledgmentStatus());
		    cppStagingInbound.setProcessingStatus(cppStagingInboundBV.getProcessingStatus());
		    cppStagingInbound.setValidationErrorMessage(cppStagingInboundBV.getValidationErrorMessage());
		    cppStagingInbound.setUpdated(userDisplayName);
		    
		 } catch (ObjectNotFoundException ex) {
	            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	            throw ex;
	     } catch (FinderException ex) {
	         CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	         throw new EJBException(ex);
	     }		
	}
	

	public void delete(Integer id, Integer version) {
		throw new java.lang.UnsupportedOperationException();
	}
	
	public void delete(Integer id, Integer version, String userDisplayName) throws ObjectNotFoundException {
		log.debug(ENTER_METHOD +"delete for "+id+" and version "+version+" for user "+userDisplayName);
		try {
			CppStagingInbound cppStagingInbound = home.findByPrimaryKey(id);
			if (!cppStagingInbound.getVersion().equals(version)) {
				throw new OptimisticLockException("Optimistic Lock Error");
			} else {
				cppStagingInbound.setObsInd("Y");
				cppStagingInbound.setUpdated(userDisplayName);
			}
		} catch (ObjectNotFoundException ex) {
			//shouldn't be trying to delete if it doesn't exist so right to throw an exception
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw ex;
        }
		catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public CppStagingInbound findByPrimaryKey(Integer id) throws ObjectNotFoundException {
		log.debug(ENTER_METHOD +"findByPrimaryKey *** for "+id);
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
	
	/**
	 * The CppStagingInbound Home.
	 * 
	 * @return CppStagingInboundHome
	 */
	public CppStagingInboundHome getHome() {
		if (this.home == null) {
			this.home = (CppStagingInboundHome) CSServices.getServiceLocator().getLocalHome(CppStagingInboundHome.class);
		}
		return this.home;
	}
	
	
	/**
	 * Find the entities using the supplied date and status'
	 * 
	 * @param timeLoaded
	 *            Date to search for records that are equal to or newer than
	 * @return The local interface of the returned entities
	 * @throws ObjectNotFoundException
	 */
	public Collection findNextDocumentByStatus(final Date timeLoaded, String validationStatus, String processingStatus) throws ObjectNotFoundException {
		try {
			// Either validationStatus or processingStatus should be null/empty string
			log.debug("Entered: findNextDocumentByStatus");
			
			if ((validationStatus != null) && (validationStatus.length() > 0) &&
					((processingStatus != null) && processingStatus.length() > 0)) {
				return this.getHome().findNextDocumentByValidationAndProcessingStatus(timeLoaded, validationStatus, processingStatus);
			} else if ((validationStatus != null) && validationStatus.length() > 0)
				return this.getHome().findNextDocumentByValidationStatus(timeLoaded, validationStatus);
			else
				return this.getHome().findNextDocumentByProcessingStatus(timeLoaded, processingStatus);
		} catch (ObjectNotFoundException anException) {
			throw anException;
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}
	
	
	/**
	 * 
	 * @param value
	 * @param local
	 */
	public static void setCppStagingInboundBasicValue(CppStagingInboundBasicValue value, CppStagingInbound local) {
		value.setId(local.getCppStagingInboundId());
		value.setCppStagingInboundId(local.getCppStagingInboundId());
		value.setDocumentName(local.getDocumentName());
		value.setCourtCode(local.getCourtCode());
		value.setDocumentType(local.getDocumentType());
		value.setTimeLoaded(local.getTimeLoaded());
		value.setClobId(local.getClobId());
		value.setValidationStatus(local.getValidationStatus());
		value.setAcknowledgmentStatus(local.getAcknowledgmentStatus());
		value.setProcessingStatus(local.getProcessingStatus());
		value.setValidationErrorMessage(local.getValidationErrorMessage());
		value.setCreationDate(local.getCreationDate());
		value.setCreatedBy(local.getCreatedBy());
		value.setVersion(local.getVersion());
		value.setLastUpdateDate(local.getLastUpdateDate());
		value.setLastUpdatedBy(local.getLastUpdatedBy());
		value.setObsInd(local.getObsInd());
	}
	
}