package uk.gov.courtservice.xhibit.business.entities.defoncaserefsolfirm;

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
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseRefSolFirmBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defoncasesolfirm.DefOnCaseRefSolFirmValue;

public class DefOnCaseRefSolFirmMaintainer extends AbstractEntityMaintainer {

	private DefOnCaseRefSolFirmHome home = null;

	private static Logger log = CSServices.getLogger(DefOnCaseRefSolFirmHome.class);

	public DefOnCaseRefSolFirmMaintainer() {
		if (home == null) {
			home = (DefOnCaseRefSolFirmHome) CSServices.getServiceLocator().getLocalHome(DefOnCaseRefSolFirmHome.class);
		}
	}

	public DefOnCaseRefSolFirmBasicValue getDefOnCaseRefSolFirmBasicValue(DefOnCaseRefSolFirm local) {
		DefOnCaseRefSolFirmBasicValue value = new DefOnCaseRefSolFirmBasicValue(local.getDefOnCaseRefSolFirmId(),
				local.getVersion());
		setDefOnCaseRefSolFirm(value, local);
		return value;
	}
	
	public DefOnCaseRefSolFirmValue returnDefOnCaseRefSolFirmValue(DefOnCaseRefSolFirm local) {
		DefOnCaseRefSolFirmValue val = new DefOnCaseRefSolFirmValue();
		val.setId(local.getDefOnCaseRefSolFirmId());
		val.setCreatedBy(local.getCreatedBy());
		val.setCreationDate(local.getCreationDate());
		val.setVersion(local.getVersion());
		val.setCrestCpfId(local.getCrestCpfId());
		val.setDefendantOnCaseId(local.getDefendantOnCaseId());
		val.setDefOnCaseRefSolFirmId(local.getDefOnCaseRefSolFirmId());
		val.setObsInd(local.getObsInd());
		val.setRefSolicitorFirmId(local.getRefSolicitorFirmId());
		val.setRepEndDate(local.getRepEndDate());
		val.setRepStDate(local.getRepStDate());
		val.setRepType(local.getRepType());
		val.setSolicitorRef(local.getSolicitorRef());
		val.setLegalAidOrderId(local.getLegalAidOrderId());
		return val;
	}
	
	public Collection returnDefOnCaseRefSolFirmValue(Collection locals) {
		if (locals == null)
			return null;
		List<DefOnCaseRefSolFirmValue> defValues = new ArrayList<DefOnCaseRefSolFirmValue>();
		Iterator it = locals.iterator();
		while (it.hasNext()) {
			defValues.add(returnDefOnCaseRefSolFirmValue((DefOnCaseRefSolFirm) it.next()));
		}
		return defValues;
	}


	private void setDefOnCaseRefSolFirm(DefOnCaseRefSolFirmBasicValue value, DefOnCaseRefSolFirm local) {
		value.setDefOnCaseRefSolFirmId(local.getDefOnCaseRefSolFirmId());
		value.setDefendantOnCaseId(local.getDefendantOnCaseId());
		value.setRefSolicitorFirmId(local.getRefSolicitorFirmId());
		value.setCrestCpfId(local.getCrestCpfId());
		value.setRepStDate(local.getRepStDate());
		value.setRepEndDate(local.getRepEndDate());
		value.setLastUpdateDate(local.getLastUpdateDate());
		value.setCreationDate(local.getCreationDate());
		value.setCreatedBy(local.getCreatedBy());
		value.setLastUpdatedBy(local.getLastUpdatedBy());
		value.setVersion(local.getVersion());
		value.setSolicitorRef(local.getSolicitorRef());
		value.setLegalAidOrderId(local.getLegalAidOrderId());
	}

	public DefOnCaseRefSolFirm create(CSAbstractValue value, String userDisplayName) {
	
			DefOnCaseRefSolFirmValue val = (DefOnCaseRefSolFirmValue)value;
			try {
				return home.create(val.getDefendantOnCaseId(), val.getRefSolicitorFirmId(), val.getCrestCpfId(), val.getRepType(), val.getRepStDate(), val.getRepEndDate(), 
						null, userDisplayName, userDisplayName, val.getSolicitorRef(), null, val.getLegalAidOrderId());
			} catch (CreateException e) {
				CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
				throw new EJBException(e);
			}
		
		
	}

	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException{
		if (!(value instanceof DefOnCaseRefSolFirmValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
     }
	 try {
		 // Get the current db values
		 DefOnCaseRefSolFirm defRef = home.findByPrimaryKey(((DefOnCaseRefSolFirmValue)value).getDefOnCaseRefSolFirmId());
	       
	     // Check the version
	     if (defRef.getVersion() == null || value.getVersion() == null || !defRef.getVersion().equals(value.getVersion())) {
	         throw new OptimisticLockException("Optimistic Lock Error");
	     }
	     
	     DefOnCaseRefSolFirmValue val = (DefOnCaseRefSolFirmValue) value;
	     defRef.setUpdated(userDisplayName);
	     
	     defRef.setDefendantOnCaseId(val.getDefendantOnCaseId());
	     defRef.setRefSolicitorFirmId(val.getRefSolicitorFirmId());
	     defRef.setCrestCpfId(val.getCrestCpfId());
	     defRef.setRepStDate(val.getRepStDate());
	     defRef.setRepEndDate(val.getRepEndDate());
	     defRef.setSolicitorRef(val.getSolicitorRef());
	     defRef.setObsInd(val.getObsInd());
	     defRef.setRepType(val.getRepType());
	     defRef.setLegalAidOrderId(val.getLegalAidOrderId());


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
	/**
	 * Set obs ind to Y for the amendment
	 * @param id
	 * @param version
	 * @param userDisplayName
	 * @throws ObjectNotFoundException
	 */
	public void delete(Integer id, Integer version, String userDisplayName){
		DefOnCaseRefSolFirm defOnCaseRef;
		try {
			defOnCaseRef = home.findByPrimaryKey(id);
			if (!defOnCaseRef.getVersion().equals(version)) {
				throw new OptimisticLockException("Optimistic Lock Error");
			} else {
				defOnCaseRef.setObsInd("Y");
				defOnCaseRef.setUpdated(userDisplayName);
			}
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
		
	}
	
	public Collection findPrivateRepByDefendantOnCaseId(Integer defOnCaseId) {
		log.debug("*** entered into findPrivateRepByDefendantOnCaseId ***");
		try {
			return returnDefOnCaseRefSolFirmValue(home.findPrivateRepByDefendantOnCaseId(defOnCaseId));
		}  catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}
	
	public Collection findPublicRepByLegalAidOrderId(Integer legalAidOrderId) {
		log.debug("*** entered into findPublicRepByLegalAidOrderId ***");
		try {
			return returnDefOnCaseRefSolFirmValue(home.findPublicRepByLegalAidOrderId(legalAidOrderId));
		}  catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	/**
	 * Find by Primary key
	 * 
	 * @param id
	 * @return
	 */
	public DefOnCaseRefSolFirm findByPrimaryKey(Integer id) throws ObjectNotFoundException {
		String methodName = "findByPK() - ";

		log.debug(methodName + "called - Id: " + id);
		try {
			DefOnCaseRefSolFirm defOnCaseRefSolFirm = home.findByPrimaryKey(id);
			log.debug(methodName + "exited - OK");
			return defOnCaseRefSolFirm;
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
			throw new EJBException(e);
		}
	}

	
	
}