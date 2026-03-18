package uk.gov.courtservice.xhibit.business.services.defendant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.defendant.Defendant;
import uk.gov.courtservice.xhibit.business.entities.defendant.DefendantMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendanthistory.DefendantHistory;
import uk.gov.courtservice.xhibit.business.entities.defendanthistory.DefendantHistoryMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendantoncasehistory.DefendantOnCaseHistory;
import uk.gov.courtservice.xhibit.business.entities.defendantoncasehistory.DefendantOnCaseHistoryMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendantreference.DefendantReference;
import uk.gov.courtservice.xhibit.business.entities.defendantreference.DefendantReferenceMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPlea;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPleaBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_menu.XhbRefDisposalMenuBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_menu.XhbRefDisposalMenuBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_type.XhbRefDisposalTypeBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_type.XhbRefDisposalTypeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_offence.XhbRefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_offence.XhbRefOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdict;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdictBeanHelper2;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantHistoryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseHistoryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantReferenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.QACASValue;
import uk.gov.courtservice.xhibit.business.vos.services.caselinking.CaseLinkingValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.CaseStatusValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DisposalValue;

/**
 * <p>
 * Title: DefendantControllerBean
 * </p>
 * <p>
 * Description: Session bean for manipulating defendant details
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @ejb.bean name="DefendantController" description="Defendant Session Bean"
 *           type="Stateless" view-type="both"
 *           jndi-name="DefendantControllerHome"
 *           local-jndi-name="DefendantControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 * @author Kevin Buckthorpe
 * @author Sarah Tong
 * @history 20/02/2003 Ian Hannaford Added caseID paramter to
 *          <code>getDefendantDetails</code>
 * @version 2.0
 */
public class DefendantControllerBean extends CSSessionBean implements SessionBean {

	private static final long serialVersionUID = 1L;

	private DefendantHelper defendantHelper = new DefendantHelper();

	private final DefendantOnCaseHelper defendantOnCaseHelper = new DefendantOnCaseHelper();
	private final DefendantOnCaseMaintainer defendantOnCaseMaintainer = new DefendantOnCaseMaintainer();
	private final DefendantHistoryMaintainer defHistoryMaintainer = new DefendantHistoryMaintainer();
	private final DefendantOnCaseHistoryMaintainer defCaseHistoryMaintainer = new DefendantOnCaseHistoryMaintainer();

	/**
	 * Returns a defendantValue object for the given
	 * 
	 * @param defID
	 *            Integer
	 * @param caseID
	 *            ID Integer of the current case
	 * @return DefendantValue: details of defendant
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws DefendantControllerException
	 */
	public DefendantValue getDefendantDetails(Integer defId, Integer caseId) throws DefendantControllerException {
		log.debug("START: getDefendantDetails(" + defId + ", " + caseId + ")");
		return defendantHelper.getDefendantDetailsWithAddress(defId, caseId);
	}

	/**
	 * Update the defendant hide-in-public-display settings.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param defendant
	 *            DefendantValue
	 * @param caseStatusValue
	 *            caseStatusValue
	 * @throws DefendantControllerException
	 */
	public void updatePublicDisplayHideSettings(DefendantValue defendant, CaseStatusValue caseStatusValue,
			String userDisplayName)
			throws DefendantControllerException {
		log.debug("START: updatePublicDisplayHideSettings");

		try {
			log.debug("---------------------");
			defendantHelper.updatePublicDisplayHideSettings(defendant, caseStatusValue, userDisplayName);
			log.debug("---------------------");
		} catch (DefendantControllerException dex) {
			ctx.setRollbackOnly();
			log.debug("updatePublicDisplayHideSettings() Transaction ROLLBACK");
			CSServices.getDefaultErrorHandler().handleError(dex, DefendantControllerBean.class);
			throw dex;
		}
	}

	/**
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param defendantId
	 * @param caseId
	 * @return DefendantOnCaseValue
	 * @throws DefendantControllerException
	 */
	public DefendantOnCaseValue getDefendantOnCaseDetails(Integer defendantId, Integer caseId)
			throws DefendantControllerException {
		log.debug("*** getDefendantOnCaseDetails(" + defendantId + ", " + caseId + ") called ***");
		DefendantOnCaseValue d = defendantOnCaseHelper.getDefendantOnCaseDetails(defendantId, caseId);
		if (!(d == null)) {
			return d;
		} else {
			return null;
		}
	}

	/**
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param defendantId
	 * @param caseId
	 * @return
	 * @throws DefendantControllerException
	 */
	public DefendantOnCaseBasicValue getDefendantOnCaseBasicValue(Integer defendantId, Integer caseId)
			throws DefendantControllerException {
		log.debug("*** getDefendantOnCaseDetails(" + defendantId + ", " + caseId + ") called ***");
		DefendantOnCaseValue d = defendantOnCaseHelper.getDefendantOnCaseDetails(defendantId, caseId);
		DefendantOnCaseBasicValue bv = null;
		if (d != null) {
			bv = getDefendantOnCaseDetails(d.getDefendantOnCaseBVO().getDefendantOnCaseId());
		}
		if (!(bv == null)) {
			return bv;
		} else {
			return null;
		}
	}
	
	/**
	 * @ejb.interface-method view-type="both"
	 * Gets DefendantOnOffenceId for defendant on case and offence
	 * @param defendantOnCaseId
	 * @param offenceID
	 * @return
	 * @throws DefendantControllerException
	 */
	public Integer getDefendantOnOffenceID(java.lang.Integer defendantOnCaseId, Integer offenceID )
			throws DefendantControllerException 
	{
		return defendantOnCaseHelper.getDefendantOnOffenceID(defendantOnCaseId, offenceID);
	}
	
	


	/**
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param defendantId
	 * @param caseId
	 * @return
	 * @throws DefendantControllerException
	 */
	public DefendantOnCaseBasicValue getDefendantOnCaseDetails(Integer defendantonCaseId)
			throws DefendantControllerException {
		log.debug("*** getDefendantOnCaseDetails(" + defendantonCaseId + ") called ***");
		DefendantOnCaseBasicValue d = defendantOnCaseHelper.findByDefendantOnCaseId(defendantonCaseId);
		if (!(d == null)) {
			return d;
		} else {
			return null;
		}
	}
	
	

	/**
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param defendantId
	 * @param firstName
	 * @param surname
	 * @param gender
	 * @param prisonerNo
	 * @return collection of defendant value objects
	 * @throws ObjectNotFoundException
	 * @throws DefendantControllerException
	 */
	public Collection findDefendantByCourtIdSurnameGenderFirstNamePrisonerNumber(Integer courtId, String firstName,
			String surname, Integer gender, String prisonerNumber) throws ObjectNotFoundException {
		log.debug("*** findDefendantByCourtIdSurnameGenderFirstName(" + courtId + ", " + firstName + ", " + surname
				+ ", " + gender + ", " + prisonerNumber + " ) called ***");
		return defendantHelper.findDefendantByCourtIdSurnameGenderFirstNamePrisonerNumber(courtId, firstName, surname,
				gender, prisonerNumber);
	}

	/**
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param defendantId
	 * @param firstName
	 * @param surname
	 * @param prisonerNumber
	 * @return collection of defendant value objects
	 * @throws ObjectNotFoundException
	 * @throws DefendantControllerException
	 */
	public Collection findDefendantByCourtIdSurnameFirstNamePrisonerNumber(Integer courtId, String firstName,
			String surname, String prisonerNumber) throws ObjectNotFoundException {
		log.debug("*** findDefendantByCourtIdSurnameFirstName(" + courtId + ", " + firstName + ", " + surname + ", "
				+ prisonerNumber + ") called ***");
		return defendantHelper.findDefendantByCourtIdSurnameFirstNamePrisonerNumber(courtId, firstName, surname,
				prisonerNumber);
	}

	/**
	 * Returns a Integer
	 * 
	 * @param AddressValue
	 *            address
	 * @param String 
	 *            userDisplayName
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws CreateException
	 */
	public Integer createDefendantAddress(AddressValue address, String userDisplayName) throws DefendantControllerException, CreateException {
		log.debug("START: getDefendant(_" + address);
		return defendantHelper.createDefendantAddress(address, userDisplayName);
	}

	/**
	 * Returns a defendantbasicvalue object
	 * 
	 * @param DefendantValue
	 *            dv
	 * @param Integer
	 *            addressId
	 * @param String
	 *            userDisplayName
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws DefendantControllerException
	 */
	public DefendantBasicValue createDefendant(DefendantValue dv, String userDisplayName, DefendantReferenceBasicValue dr1,
			DefendantReferenceBasicValue dr2, DefendantReferenceBasicValue dr3, DefendantReferenceBasicValue dr4)
			throws DefendantControllerException {
		log.debug("START: getDefendant(" + dv + ", " + userDisplayName + ")");
		try {
			//first create the address
			Integer addressId = createDefendantAddress(dv.getAddressValue(), userDisplayName);
			//then create defendant
			DefendantBasicValue defendant = defendantHelper.createDefendant(dv, addressId, userDisplayName);
			DefendantValue defValue = defendantHelper.findByDefId(defendant.getId());

			createUpdateDefendantReference(defValue, userDisplayName, defendant.getId(), dr1, dr2, dr3, dr4);
			
			//create defendant reference
			/*if (!(dr1.getReferenceValue() == null || dr1.getReferenceValue().equals(""))) {
				defendantHelper.createDefendantReference(dr1.getReferenceValue(), dr1.getReferenceName(), defendant.getId(), userDisplayName);
			}
			if (!(dr2.getReferenceValue() == null || dr2.getReferenceValue().equals(""))) {
				defendantHelper.createDefendantReference(dr2.getReferenceValue(), dr2.getReferenceName(), defendant.getId(), userDisplayName);			
			}
			if (!(dr3.getReferenceValue() == null || dr3.getReferenceValue().equals(""))) {
				defendantHelper.createDefendantReference(dr3.getReferenceValue(), dr3.getReferenceName(), defendant.getId(), userDisplayName);			
			}
			if (!(dr4.getReferenceValue() == null || dr4.getReferenceValue().equals(""))) {
				defendantHelper.createDefendantReference(dr4.getReferenceValue(), dr4.getReferenceName(), defendant.getId(), userDisplayName);			
			}*/
			return defendant;
		} catch(CreateException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, DefendantControllerBean.class);
			throw new EJBException(e);
		} catch (FinderException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, DefendantControllerBean.class);
			throw new EJBException(e);
		}
	}

	/**
	 * Returns a DefendantValue
	 * 
	 * @param Integer
	 *            DefendantId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws FinderException
	 */
	public DefendantValue findByDefId(Integer DefendantId) throws FinderException {
		return defendantHelper.findByDefId(DefendantId);
	}

	/**
	 * @return DefendantValue
	 * 
	 * @param Integer
	 *            defendantId
	 * @param Integer
	 *            caseId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws DefendantControllerException
	 */
	public DefendantValue getFullDefendantDetails(Integer defendantId, Integer caseId)
			throws DefendantControllerException {
		return defendantHelper.getFullDefendantDetails(defendantId, caseId);
	}


	/**
	 * Update the defendant details. including address and reference.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param defValue
	 *            DefendantValue
	 * @param addValue
	 *            AddressValue
	 * @return boolean
	 * @throws DefendantControllerException
	 */
	public void updateDefendant(DefendantValue defValue, AddressBasicValue addValue, String userDisplayName, Integer defendantId, DefendantReferenceBasicValue dr1, DefendantReferenceBasicValue dr2,
			DefendantReferenceBasicValue dr3, DefendantReferenceBasicValue dr4) {
		log.debug("*** updateDefendant(" + defValue + ") called ***");
		try {
			defendantHelper.updateDefendant(defValue, addValue, userDisplayName);
			defendantHelper.updateAddress(addValue, userDisplayName);
			
			createUpdateDefendantReference(defValue, userDisplayName, defendantId, dr1, dr2, dr3, dr4);

		} catch(CreateException e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		} catch(FinderException e) {
			ctx.setRollbackOnly();
			throw new EJBException(e);
		} catch(EJBException e) {	
			ctx.setRollbackOnly();
			throw e;
		} catch (OptimisticLockException e) {
			ctx.setRollbackOnly();
			throw e;
		} catch(IllegalArgumentException e) {
			ctx.setRollbackOnly();
			throw e;
		}
	}
	
	
	public void createUpdateDefendantReference(DefendantValue defValue, String userDisplayName, Integer defendantId, DefendantReferenceBasicValue dr1, DefendantReferenceBasicValue dr2,
			DefendantReferenceBasicValue dr3, DefendantReferenceBasicValue dr4) throws ObjectNotFoundException, FinderException, CreateException {
		
		DefendantReferenceMaintainer defRefMaintainer = new DefendantReferenceMaintainer();
		
		try {
			DefendantReference defRefDriverNumber = defRefMaintainer.findByDefendantIdAndReferenceName(defendantId, DefendantReferenceProperties.DRIVER_NUMBER);

			// there is a reference so get the basic value
			DefendantReferenceBasicValue defRefBasicValueDriverForUpdate = defRefMaintainer.getDefendantReferenceBasicValue(defRefDriverNumber);
			defRefBasicValueDriverForUpdate.setReferenceValue(dr1.getReferenceValue());
			defRefMaintainer.update(defRefBasicValueDriverForUpdate, userDisplayName);
		} catch (ObjectNotFoundException onfe) {
			defendantHelper.createDefendantReferenceWithDefendant(dr1.getReferenceValue(), dr1.getReferenceName(), defendantId, defValue, userDisplayName);
		}
		
		try {
			DefendantReference defRefPrisonerNumber = defRefMaintainer.findByDefendantIdAndReferenceName(defendantId, DefendantReferenceProperties.PRISONER_NUMBER);

			// there is a reference so get the basic value
			DefendantReferenceBasicValue defRefBasicValuePrisonerNumberForUpdate = defRefMaintainer.getDefendantReferenceBasicValue(defRefPrisonerNumber);
			defRefBasicValuePrisonerNumberForUpdate.setReferenceValue(dr2.getReferenceValue());
			defRefMaintainer.update(defRefBasicValuePrisonerNumberForUpdate, userDisplayName);
		} catch (ObjectNotFoundException onfe) {
			defendantHelper.createDefendantReferenceWithDefendant(dr2.getReferenceValue(), dr2.getReferenceName(), defendantId, defValue, userDisplayName);
		}
		
		try {
			DefendantReference defRefLicenceType = defRefMaintainer.findByDefendantIdAndReferenceName(defendantId, DefendantReferenceProperties.LICENCE_TYPE);

			// there is a reference so get the basic value
			DefendantReferenceBasicValue defRefBasicValueLicenceTypeForUpdate = defRefMaintainer.getDefendantReferenceBasicValue(defRefLicenceType);
			defRefBasicValueLicenceTypeForUpdate.setReferenceValue(dr3.getReferenceValue());
			defRefMaintainer.update(defRefBasicValueLicenceTypeForUpdate, userDisplayName);
		} catch (ObjectNotFoundException onfe) {
			defendantHelper.createDefendantReferenceWithDefendant(dr3.getReferenceValue(), dr3.getReferenceName(), defendantId, defValue, userDisplayName);
		}
		
		try {
			DefendantReference defRefLicenceIssueNumber = defRefMaintainer.findByDefendantIdAndReferenceName(defendantId, DefendantReferenceProperties.LICENCE_ISSUE_NUMBER);
			
			// There is a reference, so get the basic value:
			DefendantReferenceBasicValue defRefBasicValueLicenceIssueNumberForUpdate = defRefMaintainer.getDefendantReferenceBasicValue(defRefLicenceIssueNumber);
			
			defRefBasicValueLicenceIssueNumberForUpdate.setReferenceValue(dr4.getReferenceValue());
			defRefMaintainer.update(defRefBasicValueLicenceIssueNumberForUpdate, userDisplayName);
		} catch (ObjectNotFoundException onfe) {
			defendantHelper.createDefendantReferenceWithDefendant(dr4.getReferenceValue(), dr4.getReferenceName(), defendantId, defValue, userDisplayName);
		}
	}

	/**
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param caseId
	 * @return collection of DefendantOnCase objects
	 * @throws DefendantControllerException
	 */
	public Collection findByCaseId(Integer caseId) {
		log.debug("*** findByCaseId(" + caseId + ") called ***");
		Collection defendantCollection = defendantOnCaseHelper.findByCaseId(caseId);
		return defendantCollection; 
	}

	/**
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param defendantId
	 * @return collection of Integer objects
	 * @throws DefendantControllerException
	 */
	public Collection findByDefendantId(Integer defendantId) throws DefendantControllerException {
		log.debug("*** findByDefendantId(" + defendantId + ") called ***");
		return defendantOnCaseHelper.findByDefendantId(defendantId);
	}

	/**
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param defendantId
	 * @return collection of Integer objects
	 * @throws DefendantControllerException
	 */
	public Collection findDefendantByCourtIdSurname(Integer courtId, String defendantSurname)
			throws DefendantControllerException {
		log.debug("*** findByDefendantSurnameId(" + defendantSurname + ") called ***");
		try {
			Collection<DefendantValue> defendants = defendantHelper.findDefendantByCourtIdSurname(courtId,
					defendantSurname);
			return defendants;
		} catch (Exception ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, DefendantControllerBean.class);
			throw new DefendantControllerException("Defendant Not Found", "Exception", ex);
		}
	}
	
	/**
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param defendantId
	 * @return collection of Integer objects
	 * @throws DefendantControllerException
	 */
	public Collection findDefendantByCourtIdFirstNameSurname(Integer courtId, String defendantFirstName, String defendantSurname)
			throws DefendantControllerException {
		log.debug("*** findDefendantByCourtIdSurnameFirstName(" + defendantFirstName + " "+defendantSurname +") called ***");
		try {
			Collection<DefendantValue> defendants = defendantHelper.findDefendantByCourtIdFirstNameSurname(courtId, defendantFirstName,
					defendantSurname);
			return defendants;
		} catch (Exception ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, DefendantControllerBean.class);
			throw new DefendantControllerException("Defendant Not Found", "Exception", ex);
		}
	}

	/**
	 * Update the defendant on case details only. This will only be udpated in
	 * the xhibit database.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param defOnCaseValue
	 *            DefendantOnCaseValue
	 * @throws DefendantControllerException
	 */
	public void updateDefendantOnCaseDetails(DefendantOnCaseValue defOnCaseValue, String userDisplayName)
			throws DefendantControllerException {
		log.debug("*** updateDefendantOnCaseDetails(" + defOnCaseValue + ") called ***");
		try {
			defendantOnCaseHelper.updateDefendantOnCaseDetails(defOnCaseValue, userDisplayName);
		} catch (DefendantControllerException dex) {
			ctx.setRollbackOnly();
			log.debug("updateDefendantOnCaseDetails() Transaction ROLLBACK");
			CSServices.getDefaultErrorHandler().handleError(dex, DefendantControllerBean.class);
			throw dex;
		}
	}

	/**
	 * Update the defendant on case Basic value only. This will only be updated
	 * in the xhibit database.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param defOnCaseBasicValue
	 *            DefendantOnCaseBasicValue
	 * @throws DefendantControllerException
	 */
	public void updateDefendantOnCaseBasicValue(DefendantOnCaseBasicValue defOnCaseBasicValue, String userDisplayName)
			throws DefendantControllerException {
		log.debug("*** updateDefendantOnCaseBasicValue(" + defOnCaseBasicValue + ") called ***");
		defendantOnCaseMaintainer.update(defOnCaseBasicValue, userDisplayName);
	}
	

	/**
	 * Update the defendant on case court of appeal values. This will only be updated
	 * in the xhibit database.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param defOnCaseBasicValue
	 *            DefendantOnCaseBasicValue
	 * @throws DefendantControllerException
	 */
	public void updateDefendantCourtOfAppealValues(DefendantOnCaseBasicValue defOnCaseBasicValue, String userDisplayName)
			throws DefendantControllerException {
		log.debug("*** updateDefendantOnCaseBasicValue(" + defOnCaseBasicValue + ") called ***");
		defendantOnCaseMaintainer.updateDefCourtOfAppealValues(defOnCaseBasicValue, userDisplayName);
	}
	
	/**
	 * Update the defendant on case coa status. This will only be updated
	 * in the xhibit database.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param defOnCaseBasicValue
	 *            DefendantOnCaseBasicValue
	 * @throws DefendantControllerException
	 */
	public void updateDefOnCaseCOAStatus(DefendantOnCaseBasicValue defOnCaseBasicValue, String userDisplayName)
			throws DefendantControllerException {
		log.debug("*** updateDefendantOnCaseBasicValue(" + defOnCaseBasicValue + ") called ***");
		defendantOnCaseMaintainer.updateDefOnCaseCOAStatus(defOnCaseBasicValue, userDisplayName);
	}
	
	

	/**
	 * Retrieve all disposals for the defendant on case
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param defendantOnCaseId
	 *            Integer
	 * @param caseId
	 *            Integer
	 * @throws DefendantControllerException
	 */
	public DisposalValue[] getDisposalsForDefendantOnCase(Integer defendantOnCaseId, Integer caseId)
			throws DisposalControllerException {
		log.debug("*** getDisposalsForDefendantOnCase(" + defendantOnCaseId + ", " + caseId + ") called ***");
		DisposalValue[] disposalValues = null;
		try {
			disposalValues = defendantOnCaseHelper.getDisposalsForDefendantOnCase(defendantOnCaseId, caseId);
		} catch (DisposalControllerException dex) {
			ctx.setRollbackOnly();
			log.debug("getDisposalsForDefendantOnCase() Transaction ROLLBACK");
			CSServices.getDefaultErrorHandler().handleError(dex, DefendantControllerBean.class);
			// throw dex;
		}

		return disposalValues;
	}
	
	/**
	 * Retrieve all disposals for the defendant on offence
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param defendantOnCaseId
	 *            Integer
	 * @param caseId
	 *            Integer
	 *            
	 * @param defendantOnOffId 
	 * 			Integer
	 *            
	 * @throws DefendantControllerException
	 */
	public DisposalValue[] getDisposalsForDefendantOnOffence(Integer defendantOnOffId, Integer defendantOnCaseId, Integer caseId)
			throws DisposalControllerException {
		log.debug("*** getDisposalsForDefendantOnOffence(" + defendantOnCaseId + ", " + caseId + ") called ***");
		DisposalValue[] disposalValues = null;
		try {
			disposalValues = defendantOnCaseHelper.getDisposalsForDefendantOnOffence(defendantOnOffId,defendantOnCaseId, caseId);
			
		} catch (DisposalControllerException dex) {
			ctx.setRollbackOnly();
			log.debug("getDisposalsForDefendantOnOffence() Transaction ROLLBACK");
			CSServices.getDefaultErrorHandler().handleError(dex, DefendantControllerBean.class);
			// throw dex;
		}

		return disposalValues;
	}

	/**
	 * Given a ref disposal type id, get the "parent" which means we need to -
	 * get the disposal code from xhb_ref_disposal_type - given that, obtain the
	 * parent from xhb_ref_disposal_menu
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param refDisposalId
	 * @return
	 */
	public Integer getParentRefDisposalId(Integer refDisposalId, Integer courtId) {
		Integer parentId = null;

		XhbRefDisposalTypeBasicValue xrdtbv = XhbRefDisposalTypeBeanHelper2.findByPrimaryKeyValue(refDisposalId);
		if ((xrdtbv != null) && (xrdtbv.getDisposalCode() != null)) {
			String disposalCode = xrdtbv.getDisposalCode();
			XhbRefDisposalMenuBasicValue[] parentRefDisposalMenuColl = XhbRefDisposalMenuBeanHelper2
					.findRSByDisposalCodeAndCourtIdValue(disposalCode, courtId);
			// Some disposal codes may have more than one parent but that's ok
			// as any of them is fine - in that case just return the first one
			if (parentRefDisposalMenuColl.length >= 1) {
				XhbRefDisposalMenuBasicValue xrdmbv = parentRefDisposalMenuColl[0];
				parentId = xrdmbv.getParent();
			}
		}

		return parentId;
	}
	
	
	
	
	
	/**
	 * Checks refernce disposal to see if it is any of the following parent types
	 *
     * @ejb.interface-method view-type="both"
	 * @return
	 */
	public boolean isDODisposal(Integer refDisposalTypeId, Integer courtId)
	{
		
		int parent =  this.getParentRefDisposalId(refDisposalTypeId, courtId); 
		
		switch(parent)
		{
			case 500: 
			case 501: 
			case 505:
				return true; 
			default: 
				return false;
		}
		
		
	}

	/**
	 * Given a defendant on offence id get the offence code
	 * 
	 * Change 18/10/2016: For Monetary Orders we need to determine if the
	 * defendant has pled guilty or been convicted of a lesser offence and if so
	 * use that instead of the original charge. To do this, before we check the
	 * offence table, have a look at the plea and verdict table, if an
	 * alt_ref_offence_id is present then that should be used instead.
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param dataType
	 *            - will be either "code" or "desc" to decide whether to return
	 *            the offence code or offence desc
	 * @param defOnOffId
	 * @return
	 */
	public String getOffenceForDefendantOnOffence(String dataType, Integer defOnOffId) {
		String offenceData = "";
		Integer altRefOffenceId = new Integer(0);

		// These finders already strip out obsolete results
		Collection pleas = XhbPleaBeanHelper2.findByDefendantOnOffenceId(defOnOffId);
		Collection verdicts = XhbVerdictBeanHelper2.findByDefendantOnOffenceId(defOnOffId);

		boolean foundOffenceData = false;

		// Check whether there is a plea and if so if there's an alt ref offence
		// id, if so we use that to get the offence code
		Iterator iter = pleas.iterator();
		while (iter.hasNext()) {
			XhbPlea thePlea = (XhbPlea) iter.next();
			if (thePlea.getAltRefOffenceId() != null) {
				altRefOffenceId = thePlea.getAltRefOffenceId();
				foundOffenceData = true;
			}
		}

		if (!foundOffenceData) {
			// Check verdict and if there's an alt ref offence id, if so we use
			// that to get the offence code
			iter = verdicts.iterator();
			while (iter.hasNext()) {
				XhbVerdict theVerdict = (XhbVerdict) iter.next();
				if (theVerdict.getAltRefOffenceId() != null) {
					altRefOffenceId = theVerdict.getAltRefOffenceId();
					foundOffenceData = true;
				}
			}
		}

		// Finally, the most common method which will return a result is just to
		// get the offence code from the original charge
		if (!foundOffenceData) {
			XhbDefendantOnOffenceBasicValue xdoobv = XhbDefendantOnOffenceBeanHelper2.findByPrimaryKeyValue(defOnOffId);
			if ((xdoobv != null) && (xdoobv.getOffenceId() != null)) {
				Integer offenceId = xdoobv.getOffenceId();
				XhbOffenceBasicValue xobv = XhbOffenceBeanHelper2.findByPrimaryKeyValue(offenceId);
				if ((xobv != null) && (xobv.getRefOffenceId() != null)) {
					altRefOffenceId = xobv.getRefOffenceId();
				} else {
					log.error(
							"getOffenceCodeForDefendantOnOffence:: Failed to find an offenceid; found the defendant on offence but not the offence.");
					log.error("getOffenceCodeForDefendantOnOffence:: defOnOffId = " + defOnOffId.intValue());
				}
			} else {
				if (defOnOffId != null) {
					log.error(
							"getOffenceCodeForDefendantOnOffence:: Failed to find an offenceid for defendant_on_offence_id = "
									+ defOnOffId.intValue());
				} else {
					log.error(
							"getOffenceCodeForDefendantOnOffence:: Failed to find an offenceid for defendant_on_offence_id = null");
				}
			}
		}

		if (dataType.equals("code")) {
			offenceData = getOffenceCodeFromRefOffenceId(altRefOffenceId, defOnOffId);
		} else {
			offenceData = getOffenceDescFromRefOffenceId(altRefOffenceId, defOnOffId);
		}

		return offenceData;
	}

	/**
	 * Given a ref offence id get the offence code
	 * 
	 * @param refOffenceId
	 * @param defOnOffId
	 *            - only used in error handling
	 */
	private String getOffenceCodeFromRefOffenceId(Integer refOffenceId, Integer defOnOffId) {
		String offenceCode = "";
		XhbRefOffenceBasicValue xrobv = XhbRefOffenceBeanHelper2.findByPrimaryKeyValue(refOffenceId);
		if ((xrobv != null) && (xrobv.getOffenceCode() != null)) {
			offenceCode = xrobv.getOffenceCode();
		} else {
			log.error(
					"getOffenceCodeFromRefOffenceId:: Failed to find a refOffenceid; found the offence but not the ref_offence.");
			log.error("getOffenceCodeFromRefOffenceId:: defOnOffId = " + defOnOffId.intValue());
		}
		return offenceCode;
	}
	
	/**
	 * Given a ref offence id get the offence  ref
 	 * @ejb.interface-method view-type="both"
	 * @param refOffenceId
	 * @param defOnOffId
	 *            - only used in error handling
	 */
	public XhbRefOffenceBasicValue getOffenceRefFromOffenceCode(Integer offenceCode) {
		
		return XhbRefOffenceBeanHelper2.findByPrimaryKeyValue(offenceCode);
		
		
	}

	/**
	 * Given a ref offence id get the offence desc
	 * 
	 * @param refOffenceId
	 * @param defOnOffId
	 *            - only used in error handling
	 */
	private String getOffenceDescFromRefOffenceId(Integer refOffenceId, Integer defOnOffId) {
		String offenceDesc = "";
		XhbRefOffenceBasicValue xrobv = XhbRefOffenceBeanHelper2.findByPrimaryKeyValue(refOffenceId);
		if ((xrobv != null) && (xrobv.getOffenceDesc() != null)) {
			offenceDesc = xrobv.getOffenceDesc();
		} else {
			log.error(
					"getOffenceDescFromRefOffenceId:: Failed to find a refOffenceid; found the offence but not the ref_offence.");
			log.error("getOffenceDescFromRefOffenceId:: defOnOffId = " + defOnOffId.intValue());
		}
		return offenceDesc;
	}

	/**
	 * Find def on case value given a defendant on case id
	 * 
	 * @param diffRep
	 *            String
	 * @ejb.interface-method view-type="both"
	 */
	public void updateDifferenceReport(String diffRep, int id, String userName) {
		DefendantOnCaseMaintainer main = new DefendantOnCaseMaintainer();
		try {
			DefendantOnCaseBasicValue val = main.getDefendantOnCaseBasicValue(main.findByPrimaryKey(id));
			val.setDifferenceReport(diffRep);
			main.update(val, userName);
		} catch (ObjectNotFoundException e) {
			log.error("Unable to find by primary key " + id);
		}

	}

	/** 
	 *  Find def on case entries where defOnCase matches supplied defendant id and doesn't have results_verified='E'
	 *  @param defendantId Integer
	 *  @ejb.interface-method view-type="both"
	 */
	public Collection findDefendantOnActiveCases(Integer defendantId) {
		DefendantOnCaseMaintainer main = new DefendantOnCaseMaintainer();
		Collection returnColl = null;
		
		try {
			returnColl = main.findDefendantOnActiveCases(defendantId);
		} catch (ObjectNotFoundException e) {
			log.error("Unable to find by defendant id "  + defendantId);
		}
		
		return returnColl;
	}
	
	/** 
	 *  Find def on case entries where defOnCase matches supplied defendant id and doesn't have results_verified='E'
	 *  @param defendantId Integer
	 *  @param courtId     Integer
	 *  @ejb.interface-method view-type="both"
	 */
	public Collection<DefendantOnCaseBasicValue> findDefendantOnActiveCasesAndCourt(Integer defendantId, Integer courtId) {
		DefendantOnCaseDatabaseManager docDBManager = new DefendantOnCaseDatabaseManager();
		return docDBManager.findDefendantsOnActiveCases(defendantId, courtId);
	}

	/**
	 * Finds CaseLinkingValue by courtId, defendantId and caseGroupNumber
	 * returns collection case linking Value
	 * 
	 * @param courtId
	 *            Integer
	 * @param defendantId
	 *            Integer
	 * @param caseGroupNumber
	 *            Integer
	 * @ejb.interface-method view-type="both"
	 */
	public Collection<CaseLinkingValue> findLinkedCases(Integer courtId, Integer defendantId, Integer caseGroupNumber) {
		DefendantOnCaseMaintainer main = new DefendantOnCaseMaintainer();
		return main.findLinkedCases(courtId, defendantId, caseGroupNumber);
	}
	
	/**
	 * Finds the defendant records for this deleted Case (given the caseHistoryId).
	 * 
	 * @param caseHistoryId
	 *            Integer
	 * @ejb.interface-method view-type="both"
	 */
	public Collection<QACASValue> findDefendantHistoryRecords(Integer caseHistoryId) throws ObjectNotFoundException {
		//This will hold the values that we return to the QACASPanel
		Collection<QACASValue> qacasValues = new ArrayList<QACASValue>();
				
		Collection<DefendantOnCaseHistory> defHistory = defCaseHistoryMaintainer.findByCaseHistoryId(caseHistoryId);
		Iterator<DefendantOnCaseHistory> iter = defHistory.iterator();
		while(iter.hasNext()) {
			DefendantOnCaseHistoryBasicValue defOnCaseBv = defCaseHistoryMaintainer.returnBasicValue(iter.next());
			DefendantHistoryBasicValue defBv = defHistoryMaintainer.returnBasicValue(defHistoryMaintainer.findByPrimaryKey(defOnCaseBv.getDefendantHistoryId()));
			
			QACASValue val = new QACASValue(defBv, defOnCaseBv);
			qacasValues.add(val);
		}
		
		return qacasValues;
	}
	
	/**
	 * Returns the DefendantHistory object by the surname passed in
	 * @param surname
	 * @ejb.interface-method view-type="both"
	 */
	public Collection<DefendantHistoryBasicValue> findDefendantHistoryBySurname(String surname, Integer courtId) {
		Collection<DefendantHistoryBasicValue> defValues = new ArrayList<DefendantHistoryBasicValue>();
		try {
			Collection<DefendantHistory> defHistory= defHistoryMaintainer.findBySurname(surname, courtId);
			Iterator<DefendantHistory> iter = defHistory.iterator();
			while(iter.hasNext()) {
				defValues.add(defHistoryMaintainer.returnBasicValue(iter.next()));
			}
			return defValues;
		} catch(ObjectNotFoundException ex) {
			log.error("Unable to find history for the inserted surname");
			return null;
		}
		

	}
	

	
	/**
	 * Finds the defendant case history for this defendant (given the defendantHistoryId).
	 * 
	 * @param defendantHistoryId
	 *            Integer
	 * @ejb.interface-method view-type="both"
	 */
	public Collection<DefendantOnCaseHistoryBasicValue> findDefendantOnCaseHistoryRecords(Integer defHistoryId){
		
		Collection<DefendantOnCaseHistoryBasicValue> defs = new ArrayList<DefendantOnCaseHistoryBasicValue>();
		
		Collection<DefendantOnCaseHistory> defHistory;
		try {
			defHistory = defCaseHistoryMaintainer.findByDefendantHistoryId(defHistoryId);
			Iterator<DefendantOnCaseHistory> iter = defHistory.iterator();
			while(iter.hasNext()) {
				DefendantOnCaseHistoryBasicValue defOnCaseBv = defCaseHistoryMaintainer.returnBasicValue(iter.next());	
				defs.add(defOnCaseBv);
			}
			
			return defs;
		} catch (ObjectNotFoundException e) {
			log.error("Unable to find case history for defendant");
			return null;
		}
		
	}
}