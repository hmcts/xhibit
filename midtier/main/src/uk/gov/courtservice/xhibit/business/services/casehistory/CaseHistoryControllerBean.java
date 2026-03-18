package uk.gov.courtservice.xhibit.business.services.casehistory;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.casehistory.CaseHistoryValue;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerBean;

/**
 * <p>
 * Title: CaseHistoryControllerBean
 * </p>
 * <p>
 * Description: Session bean for manipulating Delete Case
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @ejb.bean name="CaseHistoryController" description=
 *           "Delete Case Information from Session Bean" type="Stateless"
 *           view-type="both" jndi-name="CaseHistoryControllerHome"
 *           local-jndi-name="CaseHistoryControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 * @author Chris Kudzin
 * 
 * @version 2.0
 */

public class CaseHistoryControllerBean extends CSSessionBean implements SessionBean {

	private static final long serialVersionUID = 1L;

	private CaseHistoryHelper caseHistoryHelper = new CaseHistoryHelper();
	private CaseHistoryDatabaseManager caseHistoryDatabaseManager = new CaseHistoryDatabaseManager();

	/**
	 * Returns a CaseHistoryValue
	 * 
	 * @param Integer
	 *            caseHistoryId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws FinderException
	 */
	public CaseHistoryValue findByCaseNumberCaseTypeAndCourtId(String caseType, String caseNumber, Integer courtId) throws FinderException {
		return caseHistoryHelper.findByCaseNumberCaseTypeAndCourtId(caseType, caseNumber, courtId);
	}
	
	/**
	 * Returns a CaseHistoryValue
	 * 
	 * @param Integer
	 *            caseHistoryId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws FinderException
	 */
	public CaseHistoryValue findByCaseHistoryId(Integer caseHistoryId) throws FinderException {
		return caseHistoryHelper.findByCaseHistoryId(caseHistoryId);
	}

	/**
	 * Logically deletes a case from the database (marks as obsolete)
	 * 
	 * @param caseId
	 * @param reasonForDeletion
	 * @ejb.interface-method view-type="both"
	 */
	public void deleteCase(Integer caseId, String reasonForDeletion)  {
		try {
			//write to the case history table
			caseHistoryDatabaseManager.writeToCaseHistory(caseId, reasonForDeletion);
			CaseMaintainer caseMaintainer = new CaseMaintainer();
			Case cs = caseMaintainer.findByPrimaryKey(caseId);
			//set the case status to D
			cs.setCaseStatus("D");
		} catch(FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			ctx.setRollbackOnly();
			throw new EJBException(e);
		}
	}

}