package uk.gov.courtservice.xhibit.business.entities.cppformatting;

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
import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.vos.entities.CppFormattingBasicValue;

public class CppFormattingMaintainer extends AbstractEntityMaintainer {

	private CppFormattingHome home = null;

	private static Logger log = CSServices.getLogger(CppFormattingHome.class);

	public CppFormattingMaintainer() {
		if (home == null) {
			home = (CppFormattingHome) CSServices.getServiceLocator().getLocalHome(CppFormattingHome.class);
		}
	}
	
	public CppFormattingBasicValue getCppFormattingBasicValue(CppFormatting local) {
		CppFormattingBasicValue value = new CppFormattingBasicValue(local.getCppFormattingId(), local.getVersion());
		setCppFormattingBasicValue(value, local);
		return value;
	}

	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		throw new UnsupportedOperationException();
	}
	
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName, Court court) {
		log.debug(ENTER_METHOD + "create cppformattingvalue");
		if (!(value instanceof CppFormattingBasicValue)) {
	        throw new IllegalArgumentException("Unexpected type:" + value.getClass());
	    }
		try {
			CppFormattingBasicValue cppFormattingBV = (CppFormattingBasicValue) value;
			return home.create(cppFormattingBV.getStagingTableId(), cppFormattingBV.getDateIn(), cppFormattingBV.getFormatStatus(), cppFormattingBV.getDocumentType(), court, cppFormattingBV.getXmlDocumentClobId(), userDisplayName);
		} catch (CreateException e) {
			  CSServices.getDefaultErrorHandler().handleError(e, getClass());
	          throw new EJBException(e);
		}   
	}

	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
	    log.debug(ENTER_METHOD + "update cppformattingvalue");
		if (!(value instanceof CppFormattingBasicValue)) {
	        throw new IllegalArgumentException("Unexpected type:" + value.getClass());
	    }
		
		try {
		    // Get the current db values
		    CppFormatting cppFormatting = home.findByPrimaryKey(value.getId());
		       
		    // Check the version
		    if (cppFormatting.getVersion() == null || value.getVersion() == null || !cppFormatting.getVersion().equals(value.getVersion())) {
		        throw new OptimisticLockException("Optimistic Lock Error");
		    }
		    CppFormattingBasicValue cppFormattingBV = (CppFormattingBasicValue) value;
		    //cppFormatting.setCourtId(cppFormattingBV.getCourtId());
		    //cppFormatting.setCppFormattingId(cppFormattingBV.getCppFormattingId());
		    cppFormatting.setDateIn(cppFormattingBV.getDateIn());
		    cppFormatting.setDocumentType(cppFormattingBV.getDocumentType());
		    cppFormatting.setFormatStatus(cppFormattingBV.getFormatStatus());
		    cppFormatting.setXmlDocumentClobId(cppFormattingBV.getXmlDocumentClobId());
		    cppFormatting.setStagingTableId(cppFormattingBV.getStagingTableId());
		    cppFormatting.setUpdated(userDisplayName);
		    cppFormatting.setErrorMessage(cppFormattingBV.getErrorMessage());

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
			CppFormatting cppFormatting = home.findByPrimaryKey(id);
			if (!cppFormatting.getVersion().equals(version)) {
				throw new OptimisticLockException("Optimistic Lock Error");
			} else {
				cppFormatting.setObsInd("Y");
				cppFormatting.setUpdated(userDisplayName);
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

	public CppFormatting findByPrimaryKey(Integer id) throws ObjectNotFoundException {
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
	
	public Collection findByCourtAndDocType(Integer courtId, String documentType, Date creationDate)
			throws ObjectNotFoundException {
		log.debug(ENTER_METHOD +"findByCourtAndDocType for court "+courtId+" and documentType "+documentType+" and creationDate "+creationDate);
		try {
			return home.findByCourtAndDocType(courtId, documentType, creationDate);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}
	
	public CppFormattingBasicValue findLatestByCourtDateInDoc(Integer courtId, Date dateIn, String documentType) throws FinderException {
		log.debug(ENTER_METHOD +"findLatestByCourtDateInDoc for court "+courtId+" and date "+dateIn+" and document type "+documentType );
		CppFormattingBasicValue value = new CppFormattingBasicValue();
		setCppFormattingBasicValue(value,home.findLatestByCourtDateInDoc(courtId, dateIn, documentType));
		return value;
	}
	
	public Collection findAllNewByDocType(String documentType, Date creationDate)
			throws ObjectNotFoundException {
		log.debug(ENTER_METHOD +"findAllNewByDocType for documentType "+documentType+" and creationDate "+creationDate);
		try {
			return home.findAllNewByDocType(documentType, creationDate);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public static void setCppFormattingBasicValue(CppFormattingBasicValue value, CppFormatting local) {
		value.setCourtId(local.getCourtId());
		value.setCppFormattingId(local.getCppFormattingId());
		value.setCreationDate(local.getCreationDate());
		value.setCreatedBy(local.getCreatedBy());
		value.setDateIn(local.getDateIn());
		value.setDocumentType(local.getDocumentType());
		value.setFormatStatus(local.getFormatStatus());
		value.setId(local.getCppFormattingId());
		value.setVersion(local.getVersion());
		value.setLastUpdateDate(local.getLastUpdateDate());
		value.setLastUpdatedBy(local.getLastUpdatedBy());
		value.setXmlDocumentClobId(local.getXmlDocumentClobId());
		value.setObsInd(local.getObsInd());
		value.setStagingTableId(local.getStagingTableId());
		value.setErrorMessage(local.getErrorMessage());
	}
	
}