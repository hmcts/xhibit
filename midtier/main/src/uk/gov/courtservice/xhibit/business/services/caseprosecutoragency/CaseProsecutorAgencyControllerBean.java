package uk.gov.courtservice.xhibit.business.services.caseprosecutoragency;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.xhibit.business.vos.services.caseprosecutoragency.CaseProsecutorAgencyValue;
import uk.gov.courtservice.xhibit.business.entities.caseprosecutoragency.CaseProsecutorAgencyMaintainer;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;

/**
 * <p>
 * Title: CaseProsecutorAgencyControllerBean
 * </p>
 * <p>
 * Description: Session bean for manipulating defendant details
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @ejb.bean name="CaseProsecutorAgencyController" description=
 *           "CaseProsecutorAgency Session Bean" type="Stateless"
 *           view-type="both" jndi-name="CaseProsecutorAgencyControllerHome"
 *           local-jndi-name="CaseProsecutorAgencyControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 * @author Chris Kudzin
 * 
 * @version 1.0
 */
public class CaseProsecutorAgencyControllerBean extends CSSessionBean implements SessionBean {

	private static final long serialVersionUID = 1L;
	private static final String PACKAGE_NAME = "uk.gov.courtservice.xhibit.business.services.caseprosecutoragency";
	private static final String CLASS_NAME = ".CaseProsecutorAgencyControllerBean";
	private static final String START= "START: ";
	private CaseProsecutorAgencyMaintainer caseProsMaintainer;

	public CaseProsecutorAgencyControllerBean() {
		if(caseProsMaintainer==null){
			caseProsMaintainer = new CaseProsecutorAgencyMaintainer();
		}
	}

	/**
	 * Returns a CaseProsecutorAgencyValue
	 * 
	 * @param caseId
	 *            Integer
	 * @return ArrayList: List of CaseProsecutorAgencies
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws FinderException
	 */
	public List<CaseProsecutorAgencyValue> findByCaseId(Integer caseId)  {
		log.debug(START + PACKAGE_NAME + CLASS_NAME +"findByCaseId(" + caseId + ")");
		return caseProsMaintainer.getCaseProsValues(caseProsMaintainer.findByCaseId(caseId));	
	}
	
	/**
	 * Updates the CaseProsecutorAgencyValue attached to the given caseId
	 * 
	 * @return void
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public void amendProsecutorAgency(Integer caseId, List<CaseProsecutorAgencyValue> prosRespList, 
									  String userDisplayName) {
		log.debug(START + PACKAGE_NAME + CLASS_NAME +"amendProsecutorAgency for "+caseId+" for a list of size "+prosRespList.size());
		try {
			if (caseId != null) {
				caseProsMaintainer.amendProsecutor(prosRespList, userDisplayName, caseId);
			}
			log.debug("amendProsecutorAgency exited");
		} catch(EJBException e) {
			ctx.setRollbackOnly();
			throw e;
		} catch(OptimisticLockException e) {
			ctx.setRollbackOnly();
			throw e;
		}	
	}
	

	/**
	 * Gets case prosecutors using the given RefProsecutorAgencyID.
	 * 
	 * @param refProsecutorAgencyId
	 *            RefProsecutorAgencyID
	 * @throws CaseControllerException
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public Collection getCaseProsecutorAgenciesByRefProsecutorId(Integer refProsecutorAgencyId) {
		String methodName = ".getCaseProsecutorAgency - ";
		log.debug(START + PACKAGE_NAME + CLASS_NAME + methodName + refProsecutorAgencyId);
		
		Collection caseProsecutorAgencies = caseProsMaintainer
				.findByRefProsecutorAgencyId(refProsecutorAgencyId);
		if (caseProsecutorAgencies != null) {
			log.debug(methodName + " exited");
			return caseProsMaintainer.getCaseProsValues(caseProsecutorAgencies);
		} else {	
			log.debug(methodName + " exited");
			return null;
		}
	}
}