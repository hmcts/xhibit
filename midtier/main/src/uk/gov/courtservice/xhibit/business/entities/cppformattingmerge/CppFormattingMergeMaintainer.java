package uk.gov.courtservice.xhibit.business.entities.cppformattingmerge;

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
import uk.gov.courtservice.xhibit.business.entities.cppformatting.CppFormatting;
import uk.gov.courtservice.xhibit.business.vos.entities.CppFormattingMergeBasicValue;

public class CppFormattingMergeMaintainer extends AbstractEntityMaintainer {

	private CppFormattingMergeHome home = null;

	private static Logger log = CSServices.getLogger(CppFormattingMergeHome.class);

	public CppFormattingMergeMaintainer() {
		if (home == null) {
			home = (CppFormattingMergeHome) CSServices.getServiceLocator().getLocalHome(CppFormattingMergeHome.class);
		}
	}
	
	public CppFormattingMergeBasicValue getCppFormattingBasicValue(CppFormattingMerge local) {
		CppFormattingMergeBasicValue value = new CppFormattingMergeBasicValue(local.getCppFormattingMergeId(), local.getVersion());
		setCppFormattingMergeBasicValue(value, local);
		return value;
	}

	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		throw new UnsupportedOperationException();
	}
	
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName, Court court, CppFormatting cppFormatting) {
		log.debug(ENTER_METHOD + "create cppformattingmergevalue");
		if (!(value instanceof CppFormattingMergeBasicValue)) {
	        throw new IllegalArgumentException("Unexpected type:" + value.getClass());
	    }
		try {
			CppFormattingMergeBasicValue cppFormattingBV = (CppFormattingMergeBasicValue) value;
			return home.create(cppFormattingBV.getFormattingId(), cppFormattingBV.getXhibitClobId(), cppFormattingBV.getLanguage(), court, cppFormatting, userDisplayName);
		} catch (CreateException e) {
			  CSServices.getDefaultErrorHandler().handleError(e, getClass());
	          throw new EJBException(e);
		}   
	}

	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
	    log.debug(ENTER_METHOD + "update cppformattingmergevalue");
		if (!(value instanceof CppFormattingMergeBasicValue)) {
	        throw new IllegalArgumentException("Unexpected type:" + value.getClass());
	    }
		
		try {
		    // Get the current db values
		    CppFormattingMerge cppFormattingMerge = home.findByPrimaryKey(value.getId());
		       
		    // Check the version
		    if (cppFormattingMerge.getVersion() == null || value.getVersion() == null || !cppFormattingMerge.getVersion().equals(value.getVersion())) {
		        throw new OptimisticLockException("Optimistic Lock Error");
		    }
		    CppFormattingMergeBasicValue cppFormattingBV = (CppFormattingMergeBasicValue) value;
		    cppFormattingMerge.setCourtId(cppFormattingBV.getCourtId());
		    cppFormattingMerge.setCppFormattingId(cppFormattingBV.getCppFormattingId());
		    cppFormattingMerge.setCppFormattingMergeId(cppFormattingBV.getCppFormattingMergeId());
		    cppFormattingMerge.setFormattingId(cppFormattingBV.getFormattingId());
		    cppFormattingMerge.setLanguage(cppFormattingBV.getLanguage());
		    cppFormattingMerge.setXhibitClobId(cppFormattingBV.getXhibitClobId());
		    cppFormattingMerge.setUpdated(userDisplayName);

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
		try {
			CppFormattingMerge cppFormattingMerge = home.findByPrimaryKey(id);
			if (!cppFormattingMerge.getVersion().equals(version)) {
				throw new OptimisticLockException("Optimistic Lock Error");
			} else {
				cppFormattingMerge.setObsInd("Y");
				cppFormattingMerge.setUpdated(userDisplayName);
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

	public CppFormattingMerge findByPrimaryKey(Integer id) throws ObjectNotFoundException {
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

	public static void setCppFormattingMergeBasicValue(CppFormattingMergeBasicValue value, CppFormattingMerge local) {
		value.setCourtId(local.getCourtId());
		value.setCppFormattingMergeId(local.getCppFormattingMergeId());
		value.setCppFormattingId(local.getCppFormattingId());
		value.setFormattingId(local.getFormattingId());
		value.setCreationDate(local.getCreationDate());
		value.setCreatedBy(local.getCreatedBy());
		value.setLanguage(local.getLanguage());
		value.setXhibitClobId(local.getXhibitClobId());
		value.setId(local.getCppFormattingMergeId());
		value.setVersion(local.getVersion());
		value.setLastUpdateDate(local.getLastUpdateDate());
		value.setLastUpdatedBy(local.getLastUpdatedBy());
		value.setObsInd(local.getObsInd());		
	}
	
}