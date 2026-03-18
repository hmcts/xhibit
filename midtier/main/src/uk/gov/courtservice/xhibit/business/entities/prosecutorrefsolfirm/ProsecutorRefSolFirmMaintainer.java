package uk.gov.courtservice.xhibit.business.entities.prosecutorrefsolfirm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
import uk.gov.courtservice.xhibit.business.entities.caseprosecutoragency.CaseProsecutorAgency;
import uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm.RefSolicitorFirm;
import uk.gov.courtservice.xhibit.business.vos.entities.ProsecutorRefSolFirmBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.prosecutorrefsolfirm.ProsecutorRefSolFirmValue;

public class ProsecutorRefSolFirmMaintainer extends AbstractEntityMaintainer {

	private ProsecutorRefSolFirmHome home = null;

	private static Logger log = CSServices.getLogger(ProsecutorRefSolFirmHome.class);

	public ProsecutorRefSolFirmMaintainer() {
		if (home == null) {
			home = (ProsecutorRefSolFirmHome) CSServices.getServiceLocator()
					.getLocalHome(ProsecutorRefSolFirmHome.class);
		}
	}

	/**
	 * Find by Primary key
	 * 
	 * @param id
	 * @return
	 */
	public ProsecutorRefSolFirm findByPrimaryKey(Integer id) throws ObjectNotFoundException {
		String methodName = "findByPK() - ";

		log.debug(methodName + "called - Id: " + id);
		try {
			ProsecutorRefSolFirm prosRefSolFirm = home.findByPrimaryKey(id);
			log.debug(methodName + "exited - OK");
			return prosRefSolFirm;
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw new EJBException(e);
		}
	}
	public ProsecutorRefSolFirmBasicValue getProsecutorRefSolFirmBasicValue(ProsecutorRefSolFirm local) {
		ProsecutorRefSolFirmBasicValue value = new ProsecutorRefSolFirmBasicValue(local.getProsecutorRefSolFirmId(),
				local.getVersion());
		setProsecutorRefSolFirm(value, local);
		return value;
	}

	private void setProsecutorRefSolFirm(ProsecutorRefSolFirmBasicValue value, ProsecutorRefSolFirm local) {
		value.setProsecutorRefSolFirmId(local.getProsecutorRefSolFirmId());
		value.setId(local.getProsecutorRefSolFirmId());
		value.setRefSolicitorFirmId(local.getRefSolicitorFirmId());
		value.setCrestCpfId(local.getCrestCpfId());
		value.setRepStDate(local.getRepStDate());
		value.setRepEndDate(local.getRepEndDate());
		value.setCaseProsAgencyId(local.getCaseProsAgencyId());
		value.setLastUpdateDate(local.getLastUpdateDate());
		value.setCreationDate(local.getCreationDate());
		value.setCreatedBy(local.getCreatedBy());
		value.setLastUpdatedBy(local.getLastUpdatedBy());
		value.setVersion(local.getVersion());
		value.setSolicitorRef(local.getSolicitorRef());
		value.setRepType(local.getRepType());
		value.setObsInd(local.getObsInd());
		value.setLegalAidOrderId(local.getLegalAidOrderId());
	}
	
	public ProsecutorRefSolFirmValue getProsecutorRefSolFirm(ProsecutorRefSolFirm local) {
		ProsecutorRefSolFirmValue value = new ProsecutorRefSolFirmValue();
		value.setId(local.getProsecutorRefSolFirmId());
		value.setProsecutorRefSolFirmId(local.getProsecutorRefSolFirmId());
		value.setRefSolicitorFirmId(local.getRefSolicitorFirmId());
		value.setCrestCpfId(local.getCrestCpfId());
		value.setRepStDate(local.getRepStDate());
		value.setRepEndDate(local.getRepEndDate());
		value.setCaseProsAgencyId(local.getCaseProsAgencyId());
		value.setLastUpdateDate(local.getLastUpdateDate());
		value.setCreationDate(local.getCreationDate());
		value.setCreatedBy(local.getCreatedBy());
		value.setLastUpdatedBy(local.getLastUpdatedBy());
		value.setVersion(local.getVersion());
		value.setSolicitorRef(local.getSolicitorRef());
		value.setRepType(local.getRepType());
		value.setObsInd(local.getObsInd());
		value.setLegalAidOrderId(local.getLegalAidOrderId());
		return value;
	}
	
	public Collection getProsecutorRefSolFirm(Collection locals) {
		if (locals == null)
			return null;
		List<ProsecutorRefSolFirmValue> prosValues = new ArrayList<ProsecutorRefSolFirmValue>();
		Iterator it = locals.iterator();
		while (it.hasNext()) {
			prosValues.add(getProsecutorRefSolFirm((ProsecutorRefSolFirm) it.next()));
		}
		return prosValues;
	}

	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		throw new java.lang.UnsupportedOperationException();
	}

	public ProsecutorRefSolFirmValue create(ProsecutorRefSolFirmValue val, String userDisplayName,
			CaseProsecutorAgency caseProsecutorAgency, RefSolicitorFirm refSolicitorFirm ) {
		log.debug("Entering : create ProsRefSolFirm");
		try {
			return getProsecutorRefSolFirm(home.create(val.getCrestCpfId(),val.getRepType(),val.getRepStDate(), 
					val.getRepEndDate(), refSolicitorFirm, caseProsecutorAgency, userDisplayName, userDisplayName, 
					val.getSolicitorRef(), null, val.getLegalAidOrderId()));
		} catch (CreateException e) {
			  CSServices.getDefaultErrorHandler().handleError(e, getClass());
	          throw new EJBException(e);
		}    
	}
	public void update(CSAbstractValue value, String userDisplayName) {
		throw new java.lang.UnsupportedOperationException();
	}
	public void update(CSAbstractValue value, String userDisplayName, CaseProsecutorAgency casePros, RefSolicitorFirm refSolicitorFirm) {
		if (!(value instanceof ProsecutorRefSolFirmValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			 // Get the current db values
			 ProsecutorRefSolFirm prosResp = home.findByPrimaryKey(((ProsecutorRefSolFirmValue)value).getProsecutorRefSolFirmId());
		       
		     // Check the version
		     if (prosResp.getVersion() == null || value.getVersion() == null || !prosResp.getVersion().equals(value.getVersion())) {
		         throw new OptimisticLockException("Optimistic Lock Error");
		     }
		     
		     ProsecutorRefSolFirmValue val = (ProsecutorRefSolFirmValue) value;
		     prosResp.setUpdated(userDisplayName);
		     prosResp.setCrestCpfId(val.getCrestCpfId());
			 prosResp.setRepType(val.getRepType());
	   	     prosResp.setRepStDate(val.getRepStDate());
		     prosResp.setRepEndDate(val.getRepEndDate());
		     prosResp.setRefSolicitorFirm(refSolicitorFirm);
		     prosResp.setCaseProsecutorAgency(casePros);
		     prosResp.setSolicitorRef(val.getSolicitorRef());
		     prosResp.setObsInd(val.getObsInd());
		     prosResp.setLegalAidOrderId(val.getLegalAidOrderId());

		 } catch (FinderException ex) {
	    	 CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	         throw new EJBException(ex);
		}
	}

	public void delete(Integer id, Integer version) {
		throw new java.lang.UnsupportedOperationException();
	}
	/**
	 * Set obs ind to Y for the amendment
	 * @param id
	 * @param version
	 * @param userDisplayName
	 * @throws ObjectNotFoundException
	 */
	public void delete(Integer id, Integer version, String userDisplayName) {
		ProsecutorRefSolFirm prosRef;
		try {
			prosRef = home.findByPrimaryKey(id);
			if (!prosRef.getVersion().equals(version)) {
				throw new OptimisticLockException("Optimistic Lock Error");
			} else {
				prosRef.setObsInd("Y");
				prosRef.setUpdated(userDisplayName);
			}
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
		
	}
	
	public Collection findPrivateRepByCaseProsAgency(Integer caseProsAgencyId){
		log.debug("*** entered into findPrivateRepByCaseProsAgency ***");
		try { 
			return getProsecutorRefSolFirm(home.findPrivateRepByCaseProsAgency(caseProsAgencyId));
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		} 
	}
	
	public Collection findPublicRepByLegalAidOrderId(Integer legalAidOrderId) {
		log.debug("*** entered into findPublicRepByLegalAidOrderId ***");
		try {
			return getProsecutorRefSolFirm(home.findPublicRepByLegalAidOrderId(legalAidOrderId));
		}  catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

}