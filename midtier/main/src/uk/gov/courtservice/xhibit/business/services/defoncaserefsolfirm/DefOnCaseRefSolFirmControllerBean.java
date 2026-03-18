package uk.gov.courtservice.xhibit.business.services.defoncaserefsolfirm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defoncaserefsolfirm.DefOnCaseRefSolFirm;
import uk.gov.courtservice.xhibit.business.entities.defoncaserefsolfirm.DefOnCaseRefSolFirmHome;
import uk.gov.courtservice.xhibit.business.entities.defoncaserefsolfirm.DefOnCaseRefSolFirmMaintainer;
import uk.gov.courtservice.xhibit.business.entities.legalaidamendment.LegalAidAmendment;
import uk.gov.courtservice.xhibit.business.entities.legalaidamendment.LegalAidAmendmentMaintainer;
import uk.gov.courtservice.xhibit.business.entities.legalaidorder.LegalAidOrder;
import uk.gov.courtservice.xhibit.business.entities.legalaidorder.LegalAidOrderMaintainer;
import uk.gov.courtservice.xhibit.business.entities.reflistingdata.RefListingData;
import uk.gov.courtservice.xhibit.business.entities.reflistingdata.RefListingDataMaintainer;
import uk.gov.courtservice.xhibit.business.services.listing.DiaryNoteEntryHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.LegalAidAmendmentBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefListingDataBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defoncasesolfirm.DefOnCaseRefSolFirmValue;
import javax.ejb.ObjectNotFoundException;

/**
 * <p>
 * Title: DefOnCaseRefSolFirmControllerBean
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
 * @ejb.bean name="DefOnCaseRefSolFirmController" description=
 *           "Defendant on case ref sol firm Session Bean" type="Stateless"
 *           view-type="both" jndi-name="DefOnCaseRefSolFirmControllerHome"
 *           local-jndi-name="DefOnCaseRefSolFirmControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 * @author Chris Kudzin
 * 
 * @version 2.0
 */
public class DefOnCaseRefSolFirmControllerBean extends CSSessionBean implements SessionBean {

	private static final long serialVersionUID = 1L;

	private DefOnCaseRefSolFirmMaintainer defMaintainer = new DefOnCaseRefSolFirmMaintainer();
	private LegalAidAmendmentMaintainer amendmentMaintainer = new LegalAidAmendmentMaintainer();
	private String deft;
	private final String privateRepStarted="PRIVATE REPRESENTATION STARTED ";
	private final String repOrderEnded = "L/A REPRESENTATION ENDED ";
	private final String privateRepEnded = "PRIVATE REPRESENTATION ENDED ";
	private final String privateRepReInstated = "PRIVATE REPRESENTATION REINSTATED";
	
	private enum ActionType {
		ADD,
		END_REP,
		REINSTATE,

	}

	/**
	 * Returns a DefOnCaseRefSolFirm
	 * 
	 * @param defOnCaseId
	 *            Integer
	 * @param currentDate
	 *            Date
	 * @return DefOnCaseRefSolFirmValue: details of defendantOnCaseSolFirm
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 */
	public Collection findPrivateRepByDefendantOnCaseId(Integer defOnCaseId) {
		log.debug("START: findPrivateRepByDefendantOnCaseId(" + defOnCaseId + ")");
		
			return defMaintainer.findPrivateRepByDefendantOnCaseId(defOnCaseId);
		
	}
	
	/**
	 * Returns a DefOnCaseRefSolFirm
	 * 
	 * @param legalAidOrderId
	 *            Integer
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 */
	public Collection findPublicRepByLegalAidOrderId(Integer legalAidOrderId) {
		log.debug("START: findPublicRepByLegalAidOrderId(" + legalAidOrderId + ")");
		
			return defMaintainer.findPublicRepByLegalAidOrderId(legalAidOrderId);
		
	}

	/**
	 * Returns a boolean
	 * 
	 * @param DefOnCaseRefSolFirm
	 *            DefOnCaseRefSolFirmValue
	 * @return Boolean: success/failure of update
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 */
	public void updateDefOnCaseRefSolFirm(DefOnCaseRefSolFirmValue defOnCaseRefSolFirm, Integer courtid, Date originalDate, String userDisplayName, String caseType){
		log.debug("START: updateDefOnCaseRefSolFirm ");
		try {
			defMaintainer.update(defOnCaseRefSolFirm, userDisplayName);
			//don't want to write to log if we are deleting the rep
			if(defOnCaseRefSolFirm.getObsInd()==null ||(defOnCaseRefSolFirm.getObsInd()!=null && !defOnCaseRefSolFirm.getObsInd().equals("Y"))) {
				if((originalDate !=null && defOnCaseRefSolFirm.getRepEndDate()==null)){
					saveToCaseDiary(defOnCaseRefSolFirm,userDisplayName, courtid, ActionType.REINSTATE, caseType);
				} else if(defOnCaseRefSolFirm.getRepEndDate()!=null && (originalDate==null || defOnCaseRefSolFirm.getRepEndDate().compareTo(originalDate)!=0)) {
					saveToCaseDiary(defOnCaseRefSolFirm,userDisplayName, courtid, ActionType.END_REP, caseType);
				}
			}
		} catch (FinderException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		} catch (CreateException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		} catch (OptimisticLockException e) {
			ctx.setRollbackOnly();
			throw e;
		} catch (IllegalArgumentException e) {
			ctx.setRollbackOnly();
			throw e;
		}
	}

	/**
	 * Returns a DefOnCaseRefSolFirmValue
	 * 
	 * @param value
	 *            DefOnCaseRefSolFirmValue
	 * 
	 * @return DefOnCaseRefSolFirmValue: newly created DefOnCaseRefSolFirmValue
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws DefOnCaseRefSolFirmControllerException
	 */
	public DefOnCaseRefSolFirmValue createDefOnCaseRefSolFirm(DefOnCaseRefSolFirmValue value, Integer courtid, String userDisplayName, String caseType) {
		log.debug("START: createDefOnCaseRefSolFirm(" + value + ")");
		try {
			DefOnCaseRefSolFirmValue def = defMaintainer.returnDefOnCaseRefSolFirmValue(defMaintainer.create(value, userDisplayName));
			saveToCaseDiary(def, userDisplayName, courtid, ActionType.ADD, caseType);
			return def;
		} catch (FinderException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		} catch (CreateException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	/**
	 * Returns a DefOnCaseRefSolFirmValue
	 * 
	 * @param defOnCaseRefSolFirmId
	 *            Integer
	 * 
	 * @return DefOnCaseRefSolFirmValue
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 */
	public DefOnCaseRefSolFirmValue findByPrimaryKey(Integer defOnCaseRefSolFirmId) {
		log.debug("START: findByPrimaryKey(" + defOnCaseRefSolFirmId + ")");
		try {
			return defMaintainer.returnDefOnCaseRefSolFirmValue(defMaintainer.findByPrimaryKey(defOnCaseRefSolFirmId));
			//if we have passed in a dodgy id then something has gone wrong here.
		} catch (ObjectNotFoundException e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Delete private rep
	 * @param defValue
	 * @param userDisplayName
 	 * @ejb.interface-method view-type="both"
	 */
	public void deletePrivateRep(DefOnCaseRefSolFirmValue defValue, String userDisplayName) {
		log.debug("Deleting rep for "+defValue.getDefOnCaseRefSolFirmId());
		defMaintainer.delete(defValue.getDefOnCaseRefSolFirmId(), defValue.getVersion(), userDisplayName);
	}
	
	/**
	 * Delete public rep and the latest 'Solicitor' legal aid amendment
	 * @param defValue
	 * @param userDisplayName
 	 * @ejb.interface-method view-type="both"
	 */
	public void deletePublicRepAndLatestSolAmendment(DefOnCaseRefSolFirmValue defValue, String userDisplayName) {
		log.debug("Deleting public rep for "+defValue.getDefOnCaseRefSolFirmId());
		try {
			// Set current XHB_DEF_ON_CASE_REF_SOL_FIRM to obsolete
			defMaintainer.delete(defValue.getDefOnCaseRefSolFirmId(), defValue.getVersion(), userDisplayName);
			
			// Set most recent 'Solicitor' legal_aid_amendment with matching LEGAL_AID_ORDER_ID to obsolete. Leave any other legal_aid_amendment records. 
			// There is no (easy!) way of knowing what has been changed, so leave any Counsel ones and the Solicitor ones that go with the previous solicitor changes.
			if ( defValue.getLegalAidOrderId() != null ) {
				Collection legalAidAmendments = amendmentMaintainer.findByLegalAidOrderId(defValue.getLegalAidOrderId());
				Iterator it = legalAidAmendments.iterator();
				while(it.hasNext()) {
					// Search to find the most recent 'Solicitor' amendment on the legal aid order and make it obsolete
					LegalAidAmendment amend = (LegalAidAmendment)it.next();
					if ( LegalAidAmendment.AMENDMENT_TYPE_SOLICITOR.equals(amend.getAmendmentType()) ) {
						amendmentMaintainer.delete(amend.getLegalAidAmendmentId(), amend.getVersion(), userDisplayName);
						break;
					}
				}
			}
		} catch (FinderException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}
    
	/**
	 * Saves the def on case end date and writes to legal aid amend table 
	 * part of public rep
	 * @param defValue
	 *            DefOnCaseRefSolFirmValue
	 * @param legalAidVal
	 *            LegalAidAmendmentBasicValue
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 */
	public void updateDefOnCaseAndLegalAmend(DefOnCaseRefSolFirmValue defValue, LegalAidAmendmentBasicValue legalAidVal, String userDisplayName, Integer courtid, String caseType) {
		DefOnCaseRefSolFirmHome xdHome = (DefOnCaseRefSolFirmHome) CSServices.getServiceLocator()
				.getLocalHome(DefOnCaseRefSolFirmHome.class);
		try {
			DefOnCaseRefSolFirm dv = xdHome.findByPrimaryKey(defValue.getDefOnCaseRefSolFirmId());
            if (dv.getVersion() == null || defValue.getVersion() == null || !dv.getVersion().equals(defValue.getVersion())) {
				log.debug("OptimisticLock exception - Entity: " + dv.getVersion() + "VO: " + defValue.getVersion());
				throw new OptimisticLockException("Optimistic Lock Error");
			} else {
				dv.setRepEndDate(defValue.getRepEndDate());
				dv.setLastUpdatedBy(userDisplayName);
				LegalAidOrderMaintainer orderMaintainer = new LegalAidOrderMaintainer();
				LegalAidOrder order = orderMaintainer.findByPrimaryKey(legalAidVal.getLegalAidOrderId());
				amendmentMaintainer.create(legalAidVal, order, userDisplayName);
				saveToCaseDiary(defValue, userDisplayName, courtid, ActionType.END_REP, caseType);

			}
		} catch (FinderException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		} catch (CreateException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
		
	}
	
	private void saveToCaseDiary(DefOnCaseRefSolFirmValue value, String userDisplayName, Integer courtid, ActionType actionType, String caseType) throws ObjectNotFoundException, FinderException, CreateException {
		//save to case diary note CTX-934
		DiaryNoteEntryComplexValue diaryNote = new DiaryNoteEntryComplexValue();
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		final String repStartDate = dateFormat.format(((DefOnCaseRefSolFirmValue)value).getRepStDate()).toUpperCase();
		String repEndDate = null;
		if((((DefOnCaseRefSolFirmValue)value).getRepEndDate())!=null) {
			repEndDate = dateFormat.format(((DefOnCaseRefSolFirmValue)value).getRepEndDate()).toUpperCase();
		}
		
		DefendantOnCaseMaintainer defOnCaseMaintainer = new DefendantOnCaseMaintainer();
		diaryNote.setCourtId(courtid);

		DefendantOnCase defOnCase = defOnCaseMaintainer.findByPrimaryKey(((DefOnCaseRefSolFirmValue)value).getDefendantOnCaseId());
		if(caseType.equals("A")) {
			deft = "APPLT ";
		} else {
			deft = "DEFT "+defOnCase.getDefendantNumber()+" ";
		}
		diaryNote.setCaseId(defOnCase.getCaseId());		
		switch(actionType) {
			case ADD :
				diaryNote.setDiaryNoteText(deft+privateRepStarted+ repStartDate);
				break;
			case END_REP:
				if(((DefOnCaseRefSolFirmValue)value).getRepType().equalsIgnoreCase("P")){
					diaryNote.setDiaryNoteText(deft+privateRepEnded+repEndDate);
				} else {
					diaryNote.setDiaryNoteText(deft+repOrderEnded+repEndDate);
				}
				break;
			case REINSTATE:
				diaryNote.setDiaryNoteText(deft+privateRepReInstated);
				break;
		}
		
		DiaryNoteEntryHelper helper = new DiaryNoteEntryHelper();
		
		RefListingDataMaintainer refDataMain = new RefListingDataMaintainer();
		Collection<RefListingData> locals = refDataMain.findByRefDataTypeAndDataValue(RefListingDataBasicValue.DataType.NOTE_TYPE,RefListingDataBasicValue.DataValue.CASE_NOTE); 
		if (!locals.isEmpty()) {
			diaryNote.setNoteType(refDataMain.getBasicValue(((ArrayList<RefListingData>) locals).get(0)));
			diaryNote.setNoteTypeId(diaryNote.getNoteType().getRefListingDataId());
		}
		
		//There are multiple entries for note classification so have to get standard one 
		locals = refDataMain.findByRefDataTypeAndDataValue(RefListingDataBasicValue.DataType.NOTE_CLASSIFICATION,"Priority"); 
		if (!locals.isEmpty()) {
			RefListingData data = (((ArrayList<RefListingData>) locals).get(0));
			diaryNote.setNoteClassification(refDataMain.getBasicValue(data));
			diaryNote.setNoteClassificationId(data.getRefListingDataId());
		}
		
		helper.saveDiaryNoteEntry(diaryNote, userDisplayName);
	}
	
}