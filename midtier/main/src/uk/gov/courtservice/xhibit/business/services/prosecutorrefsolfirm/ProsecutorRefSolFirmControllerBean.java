package uk.gov.courtservice.xhibit.business.services.prosecutorrefsolfirm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.caseprosecutoragency.CaseProsecutorAgency;
import uk.gov.courtservice.xhibit.business.entities.caseprosecutoragency.CaseProsecutorAgencyHome;
import uk.gov.courtservice.xhibit.business.entities.caseprosecutoragency.CaseProsecutorAgencyMaintainer;
import uk.gov.courtservice.xhibit.business.entities.legalaidamendment.LegalAidAmendment;
import uk.gov.courtservice.xhibit.business.entities.legalaidamendment.LegalAidAmendmentMaintainer;
import uk.gov.courtservice.xhibit.business.entities.legalaidorder.LegalAidOrder;
import uk.gov.courtservice.xhibit.business.entities.legalaidorder.LegalAidOrderMaintainer;
import uk.gov.courtservice.xhibit.business.entities.prosecutorrefsolfirm.ProsecutorRefSolFirm;
import uk.gov.courtservice.xhibit.business.entities.prosecutorrefsolfirm.ProsecutorRefSolFirmHome;
import uk.gov.courtservice.xhibit.business.entities.prosecutorrefsolfirm.ProsecutorRefSolFirmMaintainer;
import uk.gov.courtservice.xhibit.business.entities.reflistingdata.RefListingData;
import uk.gov.courtservice.xhibit.business.entities.reflistingdata.RefListingDataMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm.RefSolicitorFirm;
import uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm.RefSolicitorFirmHome;
import uk.gov.courtservice.xhibit.business.services.listing.DiaryNoteEntryHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.LegalAidAmendmentBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefListingDataBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.prosecutorrefsolfirm.ProsecutorRefSolFirmValue;

/**
 * <p>
 * Title: ProsecutorRefSolFirmControllerBean
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
 * @ejb.bean name="ProsecutorRefSolFirmController" description=
 *           "Defendant on case ref sol firm Session Bean" type="Stateless"
 *           view-type="both" jndi-name="ProsecutorRefSolFirmControllerHome"
 *           local-jndi-name="ProsecutorRefSolFirmControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 * @author Chris Kudzin
 * 
 * @version 2.0
 */
public class ProsecutorRefSolFirmControllerBean extends CSSessionBean implements SessionBean {

	private static final long serialVersionUID = 1L;
	private String prosString = "PROS ";
	private final String privateRepStarted="PRIVATE REPRESENTATION STARTED ";
	private final String repOrderEnded = "L/A REPRESENTATION ENDED ";
	private final String privateRepEnded = "PRIVATE REPRESENTATION ENDED ";
	private final String privateRepReInstated = "PRIVATE REPRESENTATION REINSTATED";
	
	private enum ActionType {
		ADD,
		END_REP,
		REINSTATE,

	}
	private LegalAidAmendmentMaintainer amendmentMaintainer = new LegalAidAmendmentMaintainer();
	private ProsecutorRefSolFirmMaintainer prosMaintainer = new ProsecutorRefSolFirmMaintainer();
	private LegalAidOrderMaintainer orderMaintainer = new LegalAidOrderMaintainer();


	/**
	 * Returns a ProsecutorRefSolFirmValue
	 * 
	 * @param caseProsAgencyId
	 *            Integer
	 * @param currentDate
	 *            Date
	 * @return collection: details of ProsecutorRefSolFirm
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws FinderException
	 */
	public Collection findPrivateRepByCaseProsAgency(Integer caseProsAgencyId) {
		log.debug("START: findPrivateRepByCaseProsAgency(" + caseProsAgencyId +")");
		return prosMaintainer.findPrivateRepByCaseProsAgency(caseProsAgencyId);
	}
	
	/**
	 * Returns a ProsecutorRefSolFirmValue
	 * 
	 * @param legalAidOrderId
	 *            Integer
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 */
	public Collection findPublicRepByLegalAidOrderId(Integer legalAidOrderId) {
		log.debug("START: findPublicRepByLegalAidOrderId(" + legalAidOrderId + ")");
		
			return prosMaintainer.findPublicRepByLegalAidOrderId(legalAidOrderId);
		
	}
	
	/**
	 * Returns a boolean
	 * 
	 * @param pros
	 *            ProsecutorRefSolFirmValue
	 * @return Boolean: success/failure of update
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ProsecutorRefSolFirmControllerException 
	 */
	public void updateProsecutorRefSolFirm(ProsecutorRefSolFirmValue pros, Integer courtid, Date originalDate, String type, String userDisplayName) {
		if(type.equals("R")) {
			prosString = "RESP ";
		}
		log.debug("START: updateDefOnCaseRefSolFirm");
		try {
			CaseProsecutorAgency casePros = null;
			RefSolicitorFirm refSolicitor = null;
			
			if (pros.getCaseProsAgencyId() != null) {
				CaseProsecutorAgencyHome cPHome = (CaseProsecutorAgencyHome) CSServices.getServiceLocator()
						.getLocalHome(CaseProsecutorAgencyHome.class);
				casePros = cPHome.findByPrimaryKey(pros.getCaseProsAgencyId());
			}
			if (pros.getRefSolicitorFirmId() != null) {
				Integer refSolicitorFirmId = pros.getRefSolicitorFirmId();
				RefSolicitorFirmHome rSHome = (RefSolicitorFirmHome) CSServices.getServiceLocator()
					.getLocalHome(RefSolicitorFirmHome.class);
				refSolicitor = rSHome.findByPrimaryKey(refSolicitorFirmId);
			}
			prosMaintainer.update(pros, userDisplayName, casePros, refSolicitor);
			if(pros.getObsInd()==null ||(pros.getObsInd()!=null && !pros.getObsInd().equals("Y"))) {
				if((originalDate !=null && pros.getRepEndDate()==null)){
					saveToCaseDiary(pros, userDisplayName, courtid, ActionType.REINSTATE);
				} else if(pros.getRepEndDate()!=null && (originalDate==null || pros.getRepEndDate().compareTo(originalDate)!=0)) {
					saveToCaseDiary(pros, userDisplayName, courtid, ActionType.END_REP);
				}
			}
		} catch(IllegalArgumentException e){
			ctx.setRollbackOnly();
			throw e;
		} catch(OptimisticLockException e){ 
			ctx.setRollbackOnly();
			throw e;
		} catch(EJBException e) {
			ctx.setRollbackOnly();
			throw e;
		}	catch (CreateException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		} catch (FinderException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}			
	}

	/**
	 * Returns a ProsecutorRefSolFirmValue
	 * 
	 * @param pros
	 *            ProsecutorRefSolFirmValue
	 *            
	 * @return ProsecutorRefSolFirmValue: newly created record
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ProsecutorRefSolFirmControllerException
	 */
	public ProsecutorRefSolFirmValue createProsecutorRefSolFirm(ProsecutorRefSolFirmValue pros, Integer courtid, String type, String userDisplayName) {
		log.debug("START: createProsecutorRefSolFirm(" + pros + ")");
		if(type.equals("R")) {
			prosString = "RESP ";
		}
		
		CaseProsecutorAgency casePros = null;
		RefSolicitorFirm refSolicitor = null;
		
		try {	
			if (pros.getCaseProsAgencyId() != null) {
				CaseProsecutorAgencyHome cPHome = (CaseProsecutorAgencyHome) CSServices.getServiceLocator()
						.getLocalHome(CaseProsecutorAgencyHome.class);
				casePros = cPHome.findByPrimaryKey(pros.getCaseProsAgencyId());
			}
			if (pros.getRefSolicitorFirmId() != null) {
				Integer refSolicitorFirmId = pros.getRefSolicitorFirmId();
				RefSolicitorFirmHome rSHome = (RefSolicitorFirmHome) CSServices.getServiceLocator()
					.getLocalHome(RefSolicitorFirmHome.class);
				refSolicitor = rSHome.findByPrimaryKey(refSolicitorFirmId);
			}
			ProsecutorRefSolFirmValue prosValue = prosMaintainer.create(pros, userDisplayName, casePros, refSolicitor);
			saveToCaseDiary(pros,userDisplayName, courtid, ActionType.ADD);
			return prosValue;

		} catch (CreateException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		} catch (FinderException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
		
	}
	
	/**
	 * Returns a ProsecutorRefSolFirmValue
	 * 
	 * @param prosecutorRefSolFirmId
	 *            Integer
	 *            
	 * @return prosecutorRefSolFirmValue
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws ProsecutorRefSolFirmControllerException
	 * @throws FinderException 
	 */
	public ProsecutorRefSolFirmValue findByPrimaryKey(Integer prosecutorRefSolFirmId) {
		log.debug("START: findByPrimaryKey(" + prosecutorRefSolFirmId + ")");
		try {
			return prosMaintainer.getProsecutorRefSolFirm(prosMaintainer.findByPrimaryKey(prosecutorRefSolFirmId));
			//this shouldn't happen as we shouldn't be passing in duff id's
		} catch(ObjectNotFoundException e) {
			throw new EJBException(e);
		}
	}
	
	/**
	 * Delete private rep
	 * @param prosValue
	 * @param userDisplayName
 	 * @ejb.interface-method view-type="both"
	 */
	public void deletePrivateRep(ProsecutorRefSolFirmValue pros, String userDisplayName) {
		log.debug("Deleting rep for "+pros.getProsecutorRefSolFirmId());
		prosMaintainer.delete(pros.getProsecutorRefSolFirmId(), pros.getVersion(), userDisplayName);	
	}
	
	/**
	 * Delete public rep and the latest 'Solicitor' legal aid amendment
	 * @param pros
	 * @param userDisplayName
 	 * @ejb.interface-method view-type="both"
	 */
	public void deletePublicRepAndLatestSolAmendment(ProsecutorRefSolFirmValue pros, String userDisplayName) {
		log.debug("Deleting public rep for "+pros.getProsecutorRefSolFirmId());
		try {
			// Set current ProsecutorRefSolFirm to obsolete
			prosMaintainer.delete(pros.getProsecutorRefSolFirmId(), pros.getVersion(), userDisplayName);
			
			// Set most recent 'Solicitor' legal_aid_amendment with matching LEGAL_AID_ORDER_ID to obsolete. Leave any other legal_aid_amendment records. 
			// There is no (easy!) way of knowing what has been changed, so leave any Counsel ones and the Solicitor ones that go with the previous solicitor changes.
			if ( pros.getLegalAidOrderId() != null ) {
				Collection legalAidAmendments = amendmentMaintainer.findByLegalAidOrderId(pros.getLegalAidOrderId());
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
	 * Saves the end date and writes to legal aid amend table 
	 * part of public rep (Step 1 of Solicitor Amend).
	 * @param prosValue
	 *            ProsecutorRefSolFirmValueFirmValue
	 * @param legalAidVal
	 *            LegalAidAmendmentBasicValue
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 */
	public void updateProsRefSolFirmAndLegalAmend(ProsecutorRefSolFirmValue prosValue, LegalAidAmendmentBasicValue legalAidVal, String updatedBy, Integer courtid, String type) {
		ProsecutorRefSolFirmHome xdHome = (ProsecutorRefSolFirmHome) CSServices.getServiceLocator()
			.getLocalHome(ProsecutorRefSolFirmHome.class);
		if(type.equals("R")) {
			prosString = "RESP ";
		}
		try {
			ProsecutorRefSolFirm pros = xdHome.findByPrimaryKey(prosValue.getProsecutorRefSolFirmId());
            if (pros.getVersion() == null || prosValue.getVersion() == null || !pros.getVersion().equals(prosValue.getVersion())) {
				log.debug("OptimisticLock exception - Entity: " + pros.getVersion() + "VO: " + prosValue.getVersion());
				throw new OptimisticLockException("Optimistic Lock Error");
			} else {
				pros.setRepEndDate(prosValue.getRepEndDate());
				pros.setLastUpdatedBy(updatedBy);
				LegalAidOrder order = orderMaintainer.findByPrimaryKey(legalAidVal.getLegalAidOrderId());
				amendmentMaintainer.create(legalAidVal, order, updatedBy);
				saveToCaseDiary(prosValue,updatedBy, courtid, ActionType.END_REP);

			}
		} catch(OptimisticLockException e){
			ctx.setRollbackOnly();
			throw e;
		} catch(EJBException e){
			ctx.setRollbackOnly();
			throw e;
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
	
	private void saveToCaseDiary(ProsecutorRefSolFirmValue value, String userDisplayName, Integer courtid, ActionType actionType) throws ObjectNotFoundException, FinderException, CreateException {
		log.debug("About to save to case diary");
		//save to case diary note CTX-934
		DiaryNoteEntryComplexValue diaryNote = new DiaryNoteEntryComplexValue();
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		final String repStartDate = dateFormat.format(((ProsecutorRefSolFirmValue)value).getRepStDate()).toUpperCase();
		String repEndDate = null;
		if(((ProsecutorRefSolFirmValue)value).getRepEndDate()!=null) {
      repEndDate = dateFormat.format(((ProsecutorRefSolFirmValue)value).getRepEndDate()).toUpperCase();
    }
		
		CaseProsecutorAgencyMaintainer caseProsMaintainer = new CaseProsecutorAgencyMaintainer();
		diaryNote.setCourtId(courtid);

		CaseProsecutorAgency casePros = caseProsMaintainer.findByPrimaryKey(((ProsecutorRefSolFirmValue)value).getCaseProsAgencyId());
		diaryNote.setCaseId(casePros.getCaseId());		
		switch(actionType) {
		case ADD :
			diaryNote.setDiaryNoteText(prosString+privateRepStarted+repStartDate);
			break;
		case END_REP:
			if(((ProsecutorRefSolFirmValue)value).getRepType().equalsIgnoreCase("P")){
				diaryNote.setDiaryNoteText(prosString+privateRepEnded+repEndDate);
			} else {
				diaryNote.setDiaryNoteText(prosString+repOrderEnded+repEndDate);
			}
			break;
		case REINSTATE:
			diaryNote.setDiaryNoteText(prosString+privateRepReInstated);
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