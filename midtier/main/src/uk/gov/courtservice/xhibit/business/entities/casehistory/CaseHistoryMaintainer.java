package uk.gov.courtservice.xhibit.business.entities.casehistory;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseHistoryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.casehistory.CaseHistoryValue;

/**
 * Maintainer for CaseHistory
 *
 */
public class CaseHistoryMaintainer extends AbstractEntityMaintainer {
	private CaseHistoryHome home = null;

	/**
	 * Default constructor.
	 */
	public CaseHistoryMaintainer() {
	}

	/**
	 * The CaseHistory Home.
	 * 
	 * @return CaseHistoryHome
	 */
	public CaseHistoryHome getHome() {
		if (this.home == null) {
			log.debug("::getHome: lazy initialise CaseHistory");
			this.home = (CaseHistoryHome) CSServices.getServiceLocator().getLocalHome(CaseHistoryHome.class);
		}
		return this.home;
	}
	public CaseHistoryBasicValue getCaseHistoryBasicValue(CaseHistory local) {
		CaseHistoryBasicValue value = new CaseHistoryBasicValue(local.getCaseHistoryId(), local.getVersion());
		setCaseHistoryBasicValue(value, local);
		return value;
	}
	
	/**
	 * Returns the caseHistoryBasicValue object
	 * @param caseType String
	 * @param caseNumber String
	 * @param courtId Integer
	 * @return CaseHistoryValue
	 * @throws ObjectNotFoundException
	 */
	public CaseHistoryValue findByCaseNumberCaseTypeAndCourtId(String caseType, String caseNumber, Integer courtId) throws ObjectNotFoundException {
		try {
			log.debug("Entered findByCaseNumberCaseTypeAndCourtId");
				return returnCaseHistoryValue(this.getHome().findByCaseNumberCaseTypeAndCourtId(caseType, caseNumber, courtId));
		} catch (ObjectNotFoundException anException) {
			throw anException;
		} catch (FinderException e) {
			throw new EJBException(e);
		}
		
	}


	private void setCaseHistoryBasicValue(CaseHistoryBasicValue value, CaseHistory local) {
		value.setCaseHistoryId(local.getCaseHistoryId());
		value.setCaseNumber(local.getCaseNumber());
		value.setCaseTitle(local.getCaseTitle());
		value.setCaseType(local.getCaseType());
		value.setCommittalDate(local.getCommittalDate());
		value.setCourtId(local.getCourtId());
		value.setDateArchived(local.getDateArchived());
		value.setPsdCTCode(local.getPsdCTCode());
		value.setId(local.getCaseHistoryId());
		value.setReasonDeleted(local.getReasonDeleted());
		value.setSentForTrial(local.getSentForTrialDate());
		value.setVersion(local.getVersion());
	}

	public CaseHistoryValue returnCaseHistoryValue(CaseHistory local) {
		CaseHistoryValue value = new CaseHistoryValue();
		value.setCaseHistoryId(local.getCaseHistoryId());
		value.setCaseNumber(local.getCaseNumber());
		value.setCaseTitle(local.getCaseTitle());
		value.setCaseType(local.getCaseType());
		value.setCommittalDate(local.getCommittalDate());
		value.setCourtId(local.getCourtId());
		value.setDateArchived(local.getDateArchived());
		value.setPsdCTCode(local.getPsdCTCode());
		value.setId(local.getCaseHistoryId());
		value.setReasonDeleted(local.getReasonDeleted());
		value.setSentForTrial(local.getSentForTrialDate());
		value.setVersion(local.getVersion());
		return value;
	}

	private void setCaseHistoryValue(CaseHistoryBasicValue value, CaseHistory local) {
		value.setCaseHistoryId(local.getCaseHistoryId());
		value.setCaseNumber(local.getCaseNumber());
		value.setCaseTitle(local.getCaseTitle());
		value.setCaseType(local.getCaseType());
		value.setCommittalDate(local.getCommittalDate());
		value.setCourtId(local.getCourtId());
		value.setDateArchived(local.getDateArchived());
		value.setPsdCTCode(local.getPsdCTCode());
		value.setId(local.getCaseHistoryId());
		value.setReasonDeleted(local.getReasonDeleted());
		value.setSentForTrial(local.getSentForTrialDate());
		value.setVersion(local.getVersion());
	}

	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		throw new java.lang.UnsupportedOperationException();
		
	}

	@Override
	public void delete(Integer id, Integer version) throws ObjectNotFoundException {
		throw new java.lang.UnsupportedOperationException();
		
	}

}
