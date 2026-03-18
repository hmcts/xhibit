package uk.gov.courtservice.xhibit.business.services.legalaidorder;

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
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.caseprosecutoragency.CaseProsecutorAgency;
import uk.gov.courtservice.xhibit.business.entities.caseprosecutoragency.CaseProsecutorAgencyHome;
import uk.gov.courtservice.xhibit.business.entities.caseprosecutoragency.CaseProsecutorAgencyMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defoncaserefsolfirm.DefOnCaseRefSolFirmMaintainer;
import uk.gov.courtservice.xhibit.business.entities.legalaidamendment.LegalAidAmendment;
import uk.gov.courtservice.xhibit.business.entities.legalaidamendment.LegalAidAmendmentMaintainer;
import uk.gov.courtservice.xhibit.business.entities.legalaidorder.LegalAidOrder;
import uk.gov.courtservice.xhibit.business.entities.legalaidorder.LegalAidOrderMaintainer;
import uk.gov.courtservice.xhibit.business.entities.prosecutorrefsolfirm.ProsecutorRefSolFirmMaintainer;
import uk.gov.courtservice.xhibit.business.entities.reflistingdata.RefListingData;
import uk.gov.courtservice.xhibit.business.entities.reflistingdata.RefListingDataMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm.RefSolicitorFirm;
import uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm.RefSolicitorFirmHome;
import uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm.RefSolicitorFirmMaintainer;
import uk.gov.courtservice.xhibit.business.services.listing.DiaryNoteEntryHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.LegalAidAmendmentBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.LegalAidOrderBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefListingDataBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defoncasesolfirm.DefOnCaseRefSolFirmValue;
import uk.gov.courtservice.xhibit.business.vos.services.prosecutorrefsolfirm.ProsecutorRefSolFirmValue;

/**
 * <p>
 * Title: LegalAidOrderControllerBean
 * </p>
 * <p>
 * Description: Session bean for manipulating Legal Aid order details
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @ejb.bean name="LegalAidOrderController" description=
 *           "Legal aid order session Bean" type="Stateless"
 *           view-type="both" jndi-name="LegalAidOrderControllerHome"
 *           local-jndi-name="LegalAidOrderControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 * @author Nia Walters
 * 
 * @version 1.0
 */
public class LegalAidOrderControllerBean extends CSSessionBean implements SessionBean {

	private static final long serialVersionUID = 1L;

	private LegalAidOrderMaintainer maintainer;
	private DefOnCaseRefSolFirmMaintainer defMaintainer;
	private LegalAidAmendmentMaintainer amendmentMaintainer;
	private ProsecutorRefSolFirmMaintainer prosecutorMaintainer;
	private CaseProsecutorAgencyMaintainer caseProsMaintainer;
	private RefSolicitorFirmMaintainer refMaintainer;
	private DefendantOnCaseMaintainer defOnCaseMaintainer;
	
	private String deft ;
	private final String pros = "RESP ";
	private final String publicRepStarted="NEW L/A REPRESENTATION STARTED ";
	private final String repOrderRevoked = "REP ORDER REVOKED ";
	private final String repOrderEnded = "L/A REPRESENTATION ENDED ";
	private final String privateRepEnded = "PRIVATE REPRESENTATION ENDED ";
	private final String publicRepReInstated = "L/A REPRESENTATION REINSTATED";
			
	
	
	private enum ActionType {

		ADD_NEW_ORDER,
		REVOKE,
		REINSTATE,
		END_REP

	}
	public LegalAidOrderControllerBean() {
		maintainer = new LegalAidOrderMaintainer();
		defMaintainer = new DefOnCaseRefSolFirmMaintainer();
		amendmentMaintainer = new LegalAidAmendmentMaintainer();
		prosecutorMaintainer = new ProsecutorRefSolFirmMaintainer();
		caseProsMaintainer = new CaseProsecutorAgencyMaintainer();
		refMaintainer = new RefSolicitorFirmMaintainer();
		defOnCaseMaintainer = new DefendantOnCaseMaintainer();
		
	}
	
	/**
	 * Find by caseProsAgencyId.
	 * 
	 * 
	 * @param caseProsAgencyId
	 * @return LegalAidOrderBasicValue
 	 * @ejb.interface-method view-type="both"
	 */
	public Collection findByCaseProsAgencyId(Integer caseProsAgencyId) {
		log.debug("START: findByCaseProsAgencyId in legal aid order for : "+caseProsAgencyId);
		return  maintainer.findByCaseProsAgencyId(caseProsAgencyId);
	}
	
	/**
	 * Find by caseProsAgencyId including revoked records which have an OBS_IND = 'Y'
	 * 
	 * 
	 * @param caseProsAgencyId
	 * @return LegalAidOrderBasicValue
 	 * @ejb.interface-method view-type="both"
	 */
	public Collection findAllByCaseProsAgencyId(Integer caseProsAgencyId) {
		log.debug("START: findByCaseProsAgencyId in legal aid order for : "+caseProsAgencyId);
		return  maintainer.findAllByCaseProsAgencyId(caseProsAgencyId);
	}
	
	/**
	 * Find by defendantOnCaseId.
	 * 
	 * 
	 * @param defendantOnCaseId
	 * @return LegalAidOrderBasicValue
 	 * @ejb.interface-method view-type="both"
	 */
	public Collection findByDefendantOnCaseId(Integer defendantOnCaseId) {
		log.debug("START: findByDefendantOnCaseId in legal aid order for : "+defendantOnCaseId);
		return  maintainer.findByDefendantOnCaseId(defendantOnCaseId);
	}
	
	/**
	 * Find by defendantOnCaseId including revoked records which have an OBS_IND = 'Y'.
	 * 
	 * 
	 * @param defendantOnCaseId
	 * @return LegalAidOrderBasicValue
 	 * @ejb.interface-method view-type="both"
	 */
	public Collection findAllByDefendantOnCaseId(Integer defendantOnCaseId) {
		log.debug("START: findAllByDefendantOnCaseId in legal aid order for : "+defendantOnCaseId);
		return  maintainer.findAllByDefendantOnCaseId(defendantOnCaseId);
	}
	
	/**
	 * Updates a legal aid order and creates an entry in amend 
	 * 
	 * @param LegalAidOrderBasicValue
	 *            legalAidBasicValue
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 */
	public void updateLegalAidOrder(LegalAidOrderBasicValue legalAidBasicValue, LegalAidAmendmentBasicValue amendVal, String userDisplayName) {
		log.debug("START: updateLegalAidOrder");
		try {
			maintainer.update(legalAidBasicValue, userDisplayName);
			LegalAidOrder order = maintainer.findByPrimaryKey(legalAidBasicValue.getId());
			amendmentMaintainer.create(amendVal, order, userDisplayName);
		} catch (ObjectNotFoundException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		} catch(EJBException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		}
	}
	
	/**
	 * Updates a legal aid order , def on case ref sol firm and creates an entry in amend 
	 * 
	 * Used during amending for correction and counsel( if sol ref is enabled )
	 * 
	 * @param LegalAidOrderBasicValue
	 *            legalAidBasicValue
	 * @param LegalAidAmendmentBasicValue
	 * 			  amendVal
     * @param DefOnCaseRefSolFirmValue
	 * 			  defValue
	 * @param String
	 * 			  userDisplayName
	 * @ejb.interface-method view-type="both"
	 * 
	 */
	public void updateLegalAidOrderAndDefOnCase(LegalAidOrderBasicValue legalAidBasicValue, LegalAidAmendmentBasicValue amendVal, DefOnCaseRefSolFirmValue defValue, String userDisplayName, 
			String amendType, Date originalDate, int courtid, String caseType) {
		log.debug("START: updateLegalAidOrderAndDefOnCase");
		try {
			maintainer.update(legalAidBasicValue, userDisplayName);
			LegalAidOrder order = maintainer.findByPrimaryKey(legalAidBasicValue.getId());
			defValue.setLastUpdatedBy(userDisplayName);
			defMaintainer.update(defValue, userDisplayName);
			if(!(amendType.equals("CORRECTION"))){
				amendmentMaintainer.create(amendVal, order, userDisplayName);
			}	
			if((originalDate !=null && defValue.getRepEndDate()==null)){
				saveToCaseDiary(defValue, userDisplayName, courtid, ActionType.REINSTATE, amendType, caseType);
			} else if(defValue.getRepEndDate()!=null && (originalDate==null || defValue.getRepEndDate().compareTo(originalDate)!=0)) {
				saveToCaseDiary(defValue, userDisplayName, courtid, ActionType.END_REP, amendType, caseType);
			}
		}
		catch(IllegalArgumentException e) {
			ctx.setRollbackOnly();
			throw e;
		} catch(OptimisticLockException e) {
			ctx.setRollbackOnly();
			throw e;
		} catch(EJBException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
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
	 * Updates a legal aid order , pros ref sol firm and creates an entry in amend 
	 * 
	 * @param LegalAidOrderBasicValue
	 *            legalAidBasicValue
	 * @param LegalAidAmendmentBasicValue
	 * 			  amendVal
     * @param ProsecutorRefSolFirmValue
	 * 			  prosValue
	 * @param String
	 * 			  userDisplayName
	 * @ejb.interface-method view-type="both"
	 * 
	 */
	public void updateLegalAidOrderAndProsRefSolFirm(LegalAidOrderBasicValue legalAidBasicValue, LegalAidAmendmentBasicValue amendVal, ProsecutorRefSolFirmValue prosValue, String userDisplayName, String amendType, Date originalDate, int courtid) {
		log.debug("START: updateLegalAidOrderAndProsRefSolFirm");
		try {
			maintainer.update(legalAidBasicValue, userDisplayName);
			LegalAidOrder order = maintainer.findByPrimaryKey(legalAidBasicValue.getId());
			prosValue.setLastUpdatedBy(userDisplayName);
			
			CaseProsecutorAgency casePros = null;
			RefSolicitorFirm refSolFirm = null;
			if(prosValue.getRefSolicitorFirmId()!=null) {
				refSolFirm = refMaintainer.findByPrimaryKey(prosValue.getRefSolicitorFirmId());
			}
			if(prosValue.getCaseProsAgencyId()!=null) {
				casePros = caseProsMaintainer.findByPrimaryKey(prosValue.getCaseProsAgencyId());
			}
			prosecutorMaintainer.update(prosValue, userDisplayName, casePros, refSolFirm) ;
			if(!(amendType.equals("CORRECTION"))){
				amendmentMaintainer.create(amendVal, order, userDisplayName);
			}
			if((originalDate !=null && prosValue.getRepEndDate()==null)){
				saveToCaseDiary(prosValue, userDisplayName, courtid, ActionType.REINSTATE, amendType, null);
			} else if(prosValue.getRepEndDate()!=null && (originalDate==null ||prosValue.getRepEndDate().compareTo(originalDate)!=0)) {
				saveToCaseDiary(prosValue, userDisplayName, courtid, ActionType.END_REP, amendType, null);
			}
			//need to catch as the prosecutorMaintainer may throw one of these
			//and we need to rollback any previous changes 
		} catch(IllegalArgumentException e) {
			ctx.setRollbackOnly();
			throw e;
		} catch(OptimisticLockException e) {
			ctx.setRollbackOnly();
			throw e;
		} catch(EJBException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
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

	
	
	/**
	 * Create a legal aid order
	 * Used for Creation of a new public rep or convert private to public.
	 * 
	 * @param LegalAidOrderBasicValue
	 *            legalAidBasicValue
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 */
	public void createLegalAidOrderAndDefOnCaseRefSolFirm(LegalAidOrderBasicValue legalAidBasicValue, DefOnCaseRefSolFirmValue defValue, String userDisplayName, DefOnCaseRefSolFirmValue privateToPublic, Integer courtid, String caseType) throws EJBException{
		log.debug("START: createLegalAidOrderAndDefOnCaseRefSolFirm");
		try {
			if(privateToPublic!=null && privateToPublic.getDefendantOnCaseId()!=null) {			
					defMaintainer.delete(privateToPublic.getDefOnCaseRefSolFirmId(), privateToPublic.getVersion(), userDisplayName);
			}
			// Create the Legal Aid Order first
			maintainer.create(legalAidBasicValue, userDisplayName);
			if ( "L".equals(defValue.getRepType()) ) {
				// Retrieve the newly created Legal Aid Order Id and update the DefOnCaseRefSolFirmValue value with it
				
				ArrayList<LegalAidOrderBasicValue> legalAidList = (ArrayList<LegalAidOrderBasicValue>)findByDefendantOnCaseId(defValue.getDefendantOnCaseId());
				if (legalAidList.size()>0 && legalAidList.get(0).getDateOfRevocation()==null) {
					defValue.setLegalAidOrderId(legalAidList.get(0).getId());
				}
			}
			
			// Create the DefOnCaseRefSolFirm Object
			 defMaintainer.create(defValue, userDisplayName);
			
			saveToCaseDiary(defValue, userDisplayName, courtid, ActionType.ADD_NEW_ORDER, null, caseType);
		}
		//if any fail then set rollback and log issue
		catch(EJBException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
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
	 * Create a legal aid order.
	 * Used when creating a new public rep or convert private to public.
	 * 
	 * @param LegalAidOrderBasicValue
	 *            legalAidBasicValue
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 */
	public void createLegalAidOrderAndProsRefSolFirm(LegalAidOrderBasicValue legalAidBasicValue, ProsecutorRefSolFirmValue prosValue, String userDisplayName, ProsecutorRefSolFirmValue privateToPublic, Integer courtid) throws EJBException{
		log.debug("START: createLegalAidOrderAndProsRefSolFirm");
		try {
			if(privateToPublic!=null && privateToPublic.getCaseProsAgencyId()!=null) {
				prosecutorMaintainer.delete(privateToPublic.getProsecutorRefSolFirmId(), privateToPublic.getVersion(), userDisplayName);
			}
			
			CaseProsecutorAgency caseProsecutorAgency = null;
			RefSolicitorFirm refSolicitorFirm = null;
			if (prosValue.getCaseProsAgencyId() != null) {
				CaseProsecutorAgencyHome cPHome = (CaseProsecutorAgencyHome) CSServices.getServiceLocator()
						.getLocalHome(CaseProsecutorAgencyHome.class);
				 caseProsecutorAgency = cPHome.findByPrimaryKey( prosValue.getCaseProsAgencyId());
			}
			
			if (prosValue.getRefSolicitorFirmId() != null) {
				Integer refSolicitorFirmId = prosValue.getRefSolicitorFirmId();
				RefSolicitorFirmHome rSHome = (RefSolicitorFirmHome) CSServices.getServiceLocator()
						.getLocalHome(RefSolicitorFirmHome.class);
				refSolicitorFirm = rSHome.findByPrimaryKey(refSolicitorFirmId);
			}
			
			// Create the Legal Aid Order first
			maintainer.create(legalAidBasicValue, userDisplayName);
			if ( "L".equals(prosValue.getRepType()) ) {
				// Retrieve the newly created Legal Aid Order Id and update the ProsecutorRefSolFirmValue value with it
				
				ArrayList<LegalAidOrderBasicValue> legalAidList2 = (ArrayList<LegalAidOrderBasicValue>)findByCaseProsAgencyId(prosValue.getCaseProsAgencyId());
				if (legalAidList2.size()>0 && legalAidList2.get(0).getDateOfRevocation()==null) {
					prosValue.setLegalAidOrderId(legalAidList2.get(0).getId());
				}
			}
			
			// Create the ProsecutorRefSolFirm Object
			prosecutorMaintainer.create(prosValue, userDisplayName, caseProsecutorAgency, refSolicitorFirm);
			
			saveToCaseDiary(prosValue, userDisplayName, courtid, ActionType.ADD_NEW_ORDER, null, null);
		}
		//if any fail then set rollback and log issue
		catch(EJBException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
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

	/**
	 * returns true if order has amendment.
	 * 
	 * @param orderId - the id of order to search on
	 * @ejb.interface-method view-type="both"
	 */
	public Boolean hasAmendmentByOrderId(Integer orderId) {
		log.debug("START : find by legal amendment by order id");	
		try {
			Collection coll = amendmentMaintainer.findByLegalAidOrderId(orderId);
			if(coll.size()>0) {
				return true;
			}
			else {
				return false;
			}
		}
		//If it throws finder exception then something's gone wrong as if it finds 0 results
		//then it'll just return an empty collection so won't get here
		catch(FinderException e ){
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}
	
	/**
	 * returns basic value if order has amendment.
	 * 
	 * @param orderId - the id of order to search on
	 * @ejb.interface-method view-type="both"
	 */
	public LegalAidAmendmentBasicValue getLatestLegalAidAmendmentByLegalAidOrderId(Integer orderId) {
		log.debug("START : find by legal amendment by order id");	
		try {
			return amendmentMaintainer.latestLegalAidAmendmentByLegalAidOrderId(orderId);
		} catch(ObjectNotFoundException e ){
			log.debug("No amendment for "+orderId);
			return null;
		} catch(FinderException e ){
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	/**
	 * Create a defoncaserefsolfirm and update order
	 * 
	 * @param LegalAidOrderBasicValue
	 *            legalAidBasicValue
	 *            
	 * @oaram defValue
	 * 				DefOnCaseRefSolFirmValue
	 * 
	 * @param userDisplayName
	 * 				String
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 */
	public void createDefOnCaseAndUpdateLegalAidOrder(LegalAidOrderBasicValue legalAidBasicValue, DefOnCaseRefSolFirmValue defValue, String userDisplayName, Integer courtid, String caseType) throws EJBException{ 
		log.debug("START: createDefOnCaseAndUpdateLegalAidOrder");
		try {
			defMaintainer.create(defValue, userDisplayName);
			maintainer.update(legalAidBasicValue, userDisplayName);
			saveToCaseDiary(defValue, userDisplayName, courtid, ActionType.ADD_NEW_ORDER, null, caseType);
		}
		//if any fail then set rollback
		catch(EJBException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch(IllegalArgumentException e) {
			ctx.setRollbackOnly();
			throw e;
		} catch(OptimisticLockException e) {
			ctx.setRollbackOnly();
			throw e;
		}catch (FinderException e) {
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
	 * Create a prosrefsolfirm and update order.
	 * Second step of amending solicitor
	 * 
	 * @param LegalAidOrderBasicValue
	 *            legalAidBasicValue
	 *            
	 * @oaram prosValue
	 * 				ProsecutorRefSolFirmValue
	 * 
	 * @param userDisplayName
	 * 				String
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 */
	public void createProsRefSolFirmAndUpdateLegalAidOrder(LegalAidOrderBasicValue legalAidBasicValue, ProsecutorRefSolFirmValue prosValue, String userDisplayName, Integer courtid) throws EJBException{ 
		log.debug("START: createProsRefSolFirmAndUpdateLegalAidOrder");
		try {
			 CaseProsecutorAgency caseProsecutorAgency = null;
			 RefSolicitorFirm refSolicitorFirm = null;
			if (prosValue.getCaseProsAgencyId() != null) {
				CaseProsecutorAgencyHome cPHome = (CaseProsecutorAgencyHome) CSServices.getServiceLocator()
						.getLocalHome(CaseProsecutorAgencyHome.class);
				 caseProsecutorAgency = cPHome.findByPrimaryKey( prosValue.getCaseProsAgencyId());
			}
			
			if (prosValue.getRefSolicitorFirmId() != null) {
				Integer refSolicitorFirmId = prosValue.getRefSolicitorFirmId();
				RefSolicitorFirmHome rSHome = (RefSolicitorFirmHome) CSServices.getServiceLocator()
						.getLocalHome(RefSolicitorFirmHome.class);
				refSolicitorFirm = rSHome.findByPrimaryKey(refSolicitorFirmId);
			}
			prosecutorMaintainer.create(prosValue, userDisplayName, caseProsecutorAgency, refSolicitorFirm);
			maintainer.update(legalAidBasicValue, userDisplayName);
			saveToCaseDiary(prosValue, userDisplayName, courtid, ActionType.ADD_NEW_ORDER, null, null);
		}
		//if any fail then set rollback
		catch(EJBException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch(IllegalArgumentException e) {
			ctx.setRollbackOnly();
			throw e;
		} catch(OptimisticLockException e) {
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
	
	/**
	 * Sets the order to be revoked and updates the def on case info
	 * 
	 * @param order
	 * @param def on case ref sol firm
	 * @ejb.interface-method view-type="both"
	 */
	public void revokeOrder(LegalAidOrderBasicValue order, DefOnCaseRefSolFirmValue def, String userDisplayName, Integer courtid, String caseType) {
		log.debug("Revoke Order "+order.getId());
		try {
			maintainer.update(order, userDisplayName);
			defMaintainer.update(def, userDisplayName);
			saveToCaseDiary(def, userDisplayName, courtid, ActionType.REVOKE, null, caseType);


		} catch(EJBException e) {
			ctx.setRollbackOnly();
			throw e;
		} catch(IllegalArgumentException e) {
			ctx.setRollbackOnly();
			throw e;
		} catch(OptimisticLockException e) {
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
	
	/**
	 * Sets the order to be revoked and updates the prosresp on case info
	 * 
	 * @param order
	 * @param the prosecutor/respondent ref sol firm value
	 * @ejb.interface-method view-type="both"
	 */
	public void revokeOrder(LegalAidOrderBasicValue order, ProsecutorRefSolFirmValue prosResp, String userDisplayName, Integer courtid) {
		log.debug("Revoke Order "+order.getId());
		try {
			maintainer.update(order, userDisplayName);
			
			CaseProsecutorAgency caseProsecutorAgency = null;
			RefSolicitorFirm refSolicitorFirm = null;
			if (prosResp.getCaseProsAgencyId() != null) {
				CaseProsecutorAgencyHome cPHome = (CaseProsecutorAgencyHome) CSServices.getServiceLocator()
						.getLocalHome(CaseProsecutorAgencyHome.class);
				 caseProsecutorAgency = cPHome.findByPrimaryKey( prosResp.getCaseProsAgencyId());
			}
			
			if (prosResp.getRefSolicitorFirmId() != null) {
				Integer refSolicitorFirmId = prosResp.getRefSolicitorFirmId();
				RefSolicitorFirmHome rSHome = (RefSolicitorFirmHome) CSServices.getServiceLocator()
						.getLocalHome(RefSolicitorFirmHome.class);
				refSolicitorFirm = rSHome.findByPrimaryKey(refSolicitorFirmId);
			}
			
			prosecutorMaintainer.update(prosResp, userDisplayName, caseProsecutorAgency, refSolicitorFirm);
			saveToCaseDiary(prosResp, userDisplayName, courtid, ActionType.REVOKE, null, null);

			

		} catch(EJBException e) {
			ctx.setRollbackOnly();
			throw e;
		} catch(IllegalArgumentException e) {
			ctx.setRollbackOnly();
			throw e;
		} catch(OptimisticLockException e) {
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

	/**
	 * Delete order and related amendments.
	 * @param legalAidBasicValue
	 * @param defValue
	 * @param userDisplayName
 	 * @ejb.interface-method view-type="both"
	 */
	public void deleteOrder(LegalAidOrderBasicValue legalAidBasicValue, DefOnCaseRefSolFirmValue defValue, String userDisplayName) {
		log.debug("Deleting Order "+legalAidBasicValue.getId()+" and all linked info");
		try {
			maintainer.delete(legalAidBasicValue.getId(), legalAidBasicValue.getVersion(), userDisplayName);
			
			Collection legalAidAmendments = amendmentMaintainer.findByLegalAidOrderId(legalAidBasicValue.getId());
			Iterator it = legalAidAmendments.iterator();
			while(it.hasNext()) {
				LegalAidAmendment amend = (LegalAidAmendment)it.next();
				amendmentMaintainer.delete(amend.getLegalAidAmendmentId(), amend.getVersion(), userDisplayName);
			}
			defMaintainer.delete(defValue.getDefOnCaseRefSolFirmId(), defValue.getVersion(), userDisplayName);
		} catch(EJBException e) {
			ctx.setRollbackOnly();
			throw e;
		} catch (FinderException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		} catch(OptimisticLockException e) {
			ctx.setRollbackOnly();
			throw e;
		}
	}
	
	/**
	 * Delete order and related amendments.
	 * @param legalAidBasicValue
	 * @param defValue
	 * @param userDisplayName
 	 * @ejb.interface-method view-type="both"
	 */
	public void deleteOrder(LegalAidOrderBasicValue legalAidBasicValue, ProsecutorRefSolFirmValue prosValue, String userDisplayName) {
		log.debug("Deleting Order "+legalAidBasicValue.getId()+" and all linked info");
		try {
			maintainer.delete(legalAidBasicValue.getId(), legalAidBasicValue.getVersion(), userDisplayName);
			
			Collection legalAidAmendments = amendmentMaintainer.findByLegalAidOrderId(legalAidBasicValue.getId());
			Iterator it = legalAidAmendments.iterator();
			while(it.hasNext()) {
				LegalAidAmendment amend = (LegalAidAmendment)it.next();
				amendmentMaintainer.delete(amend.getLegalAidAmendmentId(), amend.getVersion(), userDisplayName);
			}
			prosecutorMaintainer.delete(prosValue.getProsecutorRefSolFirmId(), prosValue.getVersion(), userDisplayName);
		} catch(EJBException e) {
			ctx.setRollbackOnly();
			throw e;
		} catch (FinderException e) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		} catch(OptimisticLockException e) {
			ctx.setRollbackOnly();
			throw e;
		}
	}
	
	private void saveToCaseDiary(CSAbstractValue value, String userDisplayName, Integer courtid, ActionType actionType, String amendType, String caseType) throws ObjectNotFoundException, FinderException, CreateException {
		log.debug("About to save to case diary during public rep ");
		//save to case diary note CTX-934
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		final String currentDate = dateFormat.format(Calendar.getInstance().getTime()).toUpperCase();
		String repEndDate  = null;
		
		DiaryNoteEntryComplexValue diaryNote = new DiaryNoteEntryComplexValue();
		diaryNote.setCourtId(courtid);
		if(value instanceof ProsecutorRefSolFirmValue) {
			final String repStartDate = dateFormat.format(((ProsecutorRefSolFirmValue)value).getRepStDate()).toUpperCase();
			if((((ProsecutorRefSolFirmValue)value).getRepEndDate())!=null){
        repEndDate = dateFormat.format(((ProsecutorRefSolFirmValue)value).getRepEndDate()).toUpperCase();
			}
			
			CaseProsecutorAgency casePros = caseProsMaintainer.findByPrimaryKey(((ProsecutorRefSolFirmValue)value).getCaseProsAgencyId());
			diaryNote.setCaseId(casePros.getCaseId());
			switch(actionType) {
				case ADD_NEW_ORDER :
					diaryNote.setDiaryNoteText(pros+publicRepStarted+repStartDate);
					break;
				case END_REP:
					if(((ProsecutorRefSolFirmValue)value).getRepType().equalsIgnoreCase("P")){
						diaryNote.setDiaryNoteText(pros+privateRepEnded+currentDate);
					} else {
						if(amendType!=null && amendType.equals("CORRECTION")){
							diaryNote.setDiaryNoteText(pros+" (Correction) "+repOrderEnded+repEndDate);
						} else {
							diaryNote.setDiaryNoteText(pros+repOrderEnded+repEndDate);

						}
					}
					break;
				case REVOKE:
					diaryNote.setDiaryNoteText(pros+repOrderRevoked+repEndDate);
					break;
				case REINSTATE:
					if(amendType!=null && amendType.equals("CORRECTION")){
						diaryNote.setDiaryNoteText(pros+" (Correction) "+publicRepReInstated);
					} else {
						diaryNote.setDiaryNoteText(pros+publicRepReInstated);
					}
					break;
			}
		}
		else {
			DefendantOnCase defOnCase = defOnCaseMaintainer.findByPrimaryKey(((DefOnCaseRefSolFirmValue)value).getDefendantOnCaseId());
			if(caseType.equals("A")) {
				deft = "APPLT ";
			} else {
				deft = "DEFT "+defOnCase.getDefendantNumber()+" ";
			}
			diaryNote.setCaseId(defOnCase.getCaseId());			
			final String repStartDate = dateFormat.format(((DefOnCaseRefSolFirmValue)value).getRepStDate()).toUpperCase();
			if((((DefOnCaseRefSolFirmValue)value).getRepEndDate())!=null) {
				repEndDate = dateFormat.format(((DefOnCaseRefSolFirmValue)value).getRepEndDate()).toUpperCase();
			}
			switch(actionType) {
			case ADD_NEW_ORDER :
				diaryNote.setDiaryNoteText(deft+publicRepStarted+repStartDate);
				break;
			case END_REP:
				if(((DefOnCaseRefSolFirmValue)value).getRepType().equalsIgnoreCase("P")){
					diaryNote.setDiaryNoteText(deft+privateRepEnded+ currentDate);
				} else {
					if(amendType!=null && amendType.equals("CORRECTION")){
						diaryNote.setDiaryNoteText(deft+"(Correction) "+repOrderEnded+repEndDate);
					} else {
						diaryNote.setDiaryNoteText(deft+repOrderEnded+repEndDate);
					}
				}
				break;
			case REINSTATE:
				if(amendType!=null && amendType.equals("CORRECTION")){
					diaryNote.setDiaryNoteText(deft+"(Correction) "+publicRepReInstated);
				} else {
					diaryNote.setDiaryNoteText(deft+publicRepReInstated);
				}
				break;
			case REVOKE:
				diaryNote.setDiaryNoteText(deft+repOrderRevoked+ repEndDate);
				break;
			}
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
