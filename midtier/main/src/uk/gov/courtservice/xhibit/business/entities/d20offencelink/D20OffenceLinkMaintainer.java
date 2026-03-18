package uk.gov.courtservice.xhibit.business.entities.d20offencelink;


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
import uk.gov.courtservice.xhibit.business.vos.entities.D20OffenceLinkBasicValue;

public class D20OffenceLinkMaintainer extends AbstractEntityMaintainer {

	private D20OffenceLinkHome home = null;

	private static Logger log = CSServices.getLogger(D20OffenceLinkHome.class);

	public D20OffenceLinkMaintainer() {
		if (home == null) {
			home = (D20OffenceLinkHome) CSServices.getServiceLocator().getLocalHome(D20OffenceLinkHome.class);
		}
	}
	
	public D20OffenceLinkBasicValue getD20OffenceLinkBasicValue(D20OffenceLink local) {
		D20OffenceLinkBasicValue value = new D20OffenceLinkBasicValue(local.getD20OffenceLinkId(), local.getVersion());
		setD20OffenceLinkBasicValue(value, local);
		return value;
	}

	
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		log.debug(ENTER_METHOD + "create cppStagingInboundvalue");
		if (!(value instanceof D20OffenceLinkBasicValue)) {
	        throw new IllegalArgumentException("Unexpected type:" + value.getClass());
	    }
		try {
			D20OffenceLinkBasicValue d20OffenceLinkBV = (D20OffenceLinkBasicValue) value;
			return home.create(d20OffenceLinkBV.getDefendantOnCaseId(), d20OffenceLinkBV.getSeqNo(), d20OffenceLinkBV.getDvlaOffenceCode(), d20OffenceLinkBV.getRefOffenceId(), d20OffenceLinkBV.getConvictionDate(),
					d20OffenceLinkBV.getIntD20(), d20OffenceLinkBV.getIntD20Date(), d20OffenceLinkBV.getFinalD20(), d20OffenceLinkBV.getFinalD20Date(), userDisplayName);
		} catch (CreateException e) {
			  CSServices.getDefaultErrorHandler().handleError(e, getClass());
	          throw new EJBException(e);
		}   
	}

	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
	    log.debug(ENTER_METHOD + "update cppStagingInboundvalue");
		if (!(value instanceof D20OffenceLinkBasicValue)) {
	        throw new IllegalArgumentException("Unexpected type:" + value.getClass());
	    }
		
		try {
		    // Get the current db values
		    D20OffenceLink d20OffenceLink = home.findByPrimaryKey(value.getId());
		       
		    // Check the version
		    if (d20OffenceLink.getVersion() == null || value.getVersion() == null || !d20OffenceLink.getVersion().equals(value.getVersion())) {
		        throw new OptimisticLockException("Optimistic Lock Error");
		    }
		    D20OffenceLinkBasicValue d20OffenceLinkdBV = (D20OffenceLinkBasicValue) value;
		    d20OffenceLink.setDefendantOnCaseId(d20OffenceLinkdBV.getDefendantOnCaseId());
		    d20OffenceLink.setSeqNo(d20OffenceLinkdBV.getSeqNo());
		    d20OffenceLink.setDvlaOffenceCode(d20OffenceLinkdBV.getDvlaOffenceCode());
		    d20OffenceLink.setRefOffenceId(d20OffenceLinkdBV.getRefOffenceId());
		    d20OffenceLink.setConvictionDate(d20OffenceLinkdBV.getConvictionDate());
		    d20OffenceLink.setIntD20(d20OffenceLinkdBV.getIntD20());
		    d20OffenceLink.setIntD20Date(d20OffenceLinkdBV.getIntD20Date());
		    d20OffenceLink.setFinalD20(d20OffenceLinkdBV.getFinalD20());
		    d20OffenceLink.setFinalD20Date(d20OffenceLinkdBV.getFinalD20Date());
		    d20OffenceLink.setUpdated(userDisplayName);
		    d20OffenceLink.setObsInd(d20OffenceLinkdBV.getObsInd());
		    
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
			D20OffenceLink d20OffenceLink = home.findByPrimaryKey(id);
			if (!d20OffenceLink.getVersion().equals(version)) {
				throw new OptimisticLockException("Optimistic Lock Error");
			} else {
				d20OffenceLink.setObsInd("Y");
				d20OffenceLink.setUpdated(userDisplayName);
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

	public D20OffenceLink findByPrimaryKey(Integer id) throws ObjectNotFoundException {
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
	public D20OffenceLinkHome getHome() {
		if (this.home == null) {
			this.home = (D20OffenceLinkHome) CSServices.getServiceLocator().getLocalHome(D20OffenceLinkHome.class);
		}
		return this.home;
	}
	
	
	/**
	 * Find the entities using the supplied defendantOnCaseId
	 * 
	 * @param defendantOnCaseId
	 *
	 * @return The local interface of the returned entities
	 * @throws ObjectNotFoundException
	 */
	public Collection findByDefendantOnCaseId(Integer defendantOnCaseId) throws ObjectNotFoundException {
		try {
			log.debug("Entered: findByDefendantOnCaseId");
			
			return this.getHome().findByDefendantOnCaseId(defendantOnCaseId);
		} catch (ObjectNotFoundException anException) {
			throw anException;
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}
	
	
	/**
	 * Find the entities using the supplied defendantOnCaseId
	 * 
	 * @param defendantOnCaseId
	 * @param dvlaOffenceCode
	 * 
	 * @return The local interface of the returned entities
	 * @throws ObjectNotFoundException
	 */
	public Collection findByDefOnCaseIdAndOffenceCode(Integer defendantOnCaseId, String dvlaOffenceCode) throws ObjectNotFoundException {
		try {
			log.debug("Entered: findByDefOnCaseIdAndOffenceCode");
			
			return this.getHome().findByDefOnCaseIdAndOffenceCode(defendantOnCaseId, dvlaOffenceCode);
		} catch (ObjectNotFoundException anException) {
			throw anException;
		} catch (FinderException anException) {
			throw new EJBException(anException);
		}
	}
	
	/**
	 * Find the entities using the supplied defendantOnCaseId
	 * 
	 * @param defendantOnCaseId
	 * @param refOffenceId
	 * 
	 * @return The local interface of the returned entities
	 * @throws ObjectNotFoundException
	 */
	public Collection findByDefOnCaseIdAndRefOffenceId(Integer defendantOnCaseId, Integer refOffenceId) throws ObjectNotFoundException {
		try {
			log.debug("Entered: findByDefOnCaseIdAndRefOffenceId");
			
			return this.getHome().findByDefOnCaseIdAndRefOffenceId(defendantOnCaseId, refOffenceId);
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
	public static void setD20OffenceLinkBasicValue(D20OffenceLinkBasicValue value, D20OffenceLink local) {
		value.setId(local.getD20OffenceLinkId());
		value.setDefendantOnCaseId(local.getDefendantOnCaseId());
		value.setSeqNo(local.getSeqNo());
		value.setDvlaOffenceCode(local.getDvlaOffenceCode());
		value.setRefOffenceId(local.getRefOffenceId());
		value.setConvictionDate(local.getConvictionDate());
		value.setIntD20(local.getIntD20());
		value.setIntD20Date(local.getIntD20Date());
		value.setFinalD20(local.getFinalD20());
		value.setFinalD20Date(local.getFinalD20Date());
		value.setCreationDate(local.getCreationDate());
		value.setCreatedBy(local.getCreatedBy());
		value.setVersion(local.getVersion());
		value.setLastUpdateDate(local.getLastUpdateDate());
		value.setLastUpdatedBy(local.getLastUpdatedBy());
		value.setObsInd(local.getObsInd());
	}
	
}