package uk.gov.courtservice.xhibit.business.services.listing;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.directionsforcase.DirectionsForCase;
import uk.gov.courtservice.xhibit.business.entities.directionsforcase.DirectionsForCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.listing.CaseListingEntry;
import uk.gov.courtservice.xhibit.business.entities.listing.CaseListingEntryMaintainer;
import uk.gov.courtservice.xhibit.business.entities.listing.CaseOnList;
import uk.gov.courtservice.xhibit.business.entities.listing.CaseOnListMaintainer;
import uk.gov.courtservice.xhibit.business.entities.listing.DefOnCaseOnList;
import uk.gov.courtservice.xhibit.business.entities.listing.DefOnCaseOnListMaintainer;
import uk.gov.courtservice.xhibit.business.entities.listing.List;
import uk.gov.courtservice.xhibit.business.entities.listing.ListMaintainer;
import uk.gov.courtservice.xhibit.business.entities.listing.SittingOnList;
import uk.gov.courtservice.xhibit.business.entities.listing.SittingOnListMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refhearingtype.RefHearingType;
import uk.gov.courtservice.xhibit.business.entities.refhearingtype.RefHearingTypeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refjudge.RefJudge;
import uk.gov.courtservice.xhibit.business.entities.refjudge.RefJudgeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refjudgeticket.RefJudgeTicket;
import uk.gov.courtservice.xhibit.business.entities.refjudgeticket.RefJudgeTicketMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refsystemcode.RefSystemCode;
import uk.gov.courtservice.xhibit.business.entities.refsystemcode.RefSystemCodeMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.helper.AbstractHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ListSaveResult;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.listing.ListValue;

/**
 * <p>
 * Title: ListHelper
 * </p>
 * <p>
 * Description: 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class ListHelper extends AbstractHelper {
	private ListMaintainer listMaintainer;
	private CaseMaintainer caseMaintainer;
	private SittingOnListMaintainer sittingOnListMaintainer;
	private CaseOnListMaintainer caseOnListMaintainer;
	private RefJudgeMaintainer refJudgeMaintainer;
	private RefJudgeTicketMaintainer refJudgeTicketMaintainer;
	private RefHearingTypeMaintainer refHearingTypeMaintainer;
	private DefOnCaseOnListMaintainer defOnCaseOnListMaintainer;
	private CaseListingEntryMaintainer caseListingEntryMaintainer;
	private DirectionsForCaseMaintainer directionsForCaseMaintainer; 
	private RefSystemCodeMaintainer refSystemCodeMaintainer;
	
	/**
	 * Default constructor that instantiate the necessary maintainers.
	 */
	public ListHelper() {
		listMaintainer = new ListMaintainer();
		caseMaintainer = new CaseMaintainer();
		sittingOnListMaintainer = new SittingOnListMaintainer();
		caseOnListMaintainer = new CaseOnListMaintainer();
		refJudgeMaintainer = new RefJudgeMaintainer();
		refJudgeTicketMaintainer = new RefJudgeTicketMaintainer();
		refHearingTypeMaintainer = new RefHearingTypeMaintainer();
		defOnCaseOnListMaintainer = new DefOnCaseOnListMaintainer();
		caseListingEntryMaintainer = new CaseListingEntryMaintainer();
		directionsForCaseMaintainer = new DirectionsForCaseMaintainer();
		refSystemCodeMaintainer = new RefSystemCodeMaintainer();
	}

	/**
	 * Description: Find List
	 * 
	 * @param listId
	 * @return ListBasicValue
	 * @throws FinderException 
	 */
	public ListBasicValue findByPrimaryKey(Integer listId) throws FinderException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findByPrimaryKey(listId="+listId+")");
       	}
		try {
			List local = listMaintainer.findByPrimaryKey(listId);
			ListBasicValue result = listMaintainer.getBasicValue(local);
			return result;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
    	}
	}

	/**
	 * Description: Find List detail
	 * 
	 * @param listId
	 * @return ListComplexValue
	 * @throws FinderException 
	 */
	public ListComplexValue findDetailByPrimaryKey(Integer listId) throws FinderException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findDetailByPrimaryKey(listId="+listId+")");
       	}
		try {
			List local = listMaintainer.findByPrimaryKey(listId);
			ListComplexValue result = listMaintainer.getComplexValue(local);
			populateComplexValue(result);
			return result;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
    	}
	}

	/**
	 * Description: Find List detail
	 * 
	 * @param ListValue
	 * @return ListComplexValue
	 * @throws FinderException 
	 */
	public ListComplexValue findDetailByListValue(ListValue listValue) throws FinderException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findDetailByListValue(listValue="+listValue+")");
       	}
		try {
			List local = listMaintainer.findByPrimaryKey(listValue.getList().getListId());
			ListComplexValue result = listMaintainer.getComplexValue(local);
			populateComplexValue(result, listValue);
			return result;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
    	}
	}
	
	/**
	 * Description: Find Collection of Lists by courtId
	 * 
	 * @param courtId
	 * @return Collection
	 * @throws FinderException 
	 */
	@SuppressWarnings("unchecked")
	public Collection findByCourtId(Integer courtId) throws FinderException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findByCourtId(courtId="+courtId+")");
       	}
		try {
			Collection result = newCollection();
			Collection locals = listMaintainer.findByCourtId(courtId);
			for (List local : (Collection<List>) locals) {
				ListBasicValue value = listMaintainer.getBasicValue(local);
				result.add(value);
			}
			return result;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
    	}
	}
	

	/**
	 * Description: Save List
	 * 
	 * @param ListValue
	 * @throws OptimisticLockException,
	 *             CreateException, FinderException
	 */
	public ListSaveResult saveList(ListValue listValue, String userDisplayName)
			throws OptimisticLockException, CreateException, FinderException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: saveList(listValue="+listValue+", userDisplayName="+userDisplayName+")");
       	}
		ListSaveResult result = new ListSaveResult();
		
		try {
			// Save list
			if (listValue.getList().getListId() == null) {
				List local = (List)listMaintainer.create(listValue.getList(), userDisplayName);
				listValue.getList().setListId(local.getListId());
				result.setListId(local.getListId());
        	} else {
        		if (listValue.getList().isDirty()) {
        			result= listMaintainer.update(listValue.getList(), userDisplayName);
        		} else {
        			result.setListId(listValue.getList().getListId());
        		}
        	}
			
			// Save sittings on list
			for (SittingOnListBasicValue value : listValue.getSittingsOnList()) {
				// If new sitting on list row, must set list id here as for
				// new lists the list id will be unknown until save above
				if (value.getVersion().equals(Integer.valueOf(-1))) {
					value.setListId(listValue.getList().getListId());
					sittingOnListMaintainer.create(value, userDisplayName);
				} else {
					sittingOnListMaintainer.update(value, userDisplayName);
				}
			}
			
			// Save cases on list
			for (CaseOnListBasicValue value : listValue.getCasesOnList()) {
				// If new case on list row, must set list id here as for
				// new lists the list id will be unknown until save above
				if (value.getVersion().equals(Integer.valueOf(-1))) {
					value.setListId(listValue.getList().getListId());
					caseOnListMaintainer.create(value, userDisplayName);
				} else {
					caseOnListMaintainer.update(value, userDisplayName);
				}
			}
			
			// Save def on cases on list
			for (DefOnCaseOnListBasicValue value : listValue.getDefOnCasesOnList()) {
				if (value.getVersion().equals(Integer.valueOf(-1))) {
					defOnCaseOnListMaintainer.create(value, userDisplayName);
				} else {
					defOnCaseOnListMaintainer.update(value, userDisplayName);
				}
			}
			
			// Return new or existing list id
			return result;
			
		} catch (OptimisticLockException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		} catch (EJBException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			if (ex.getCause() instanceof SQLException) {
				SQLException sqlException = (SQLException) ex.getCause(); 
				Integer concurrentUserOptimisticLockValue = Integer.valueOf(20101);
				if (concurrentUserOptimisticLockValue.equals(sqlException.getErrorCode())) {
					throw new OptimisticLockException("SQLError:"+ sqlException.getErrorCode());
				}
			}
			throw ex;
		}
	}
	
	/**
	 * Description: Populate list complex value with all sittings and cases on list
	 * 
	 * @param list
	 * @throws ObjectNotFoundException
	 */
	@SuppressWarnings("unchecked")
	protected void populateComplexValue(ListComplexValue list) throws ObjectNotFoundException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: populateComplexValue(list="+list+")");
       	}
		// Populate sittings on list
		Collection<SittingOnList> sittingOnListLocals = sittingOnListMaintainer.findByListId(list.getListId());
		Collection<SittingOnListComplexValue> sittingOnListValues = sittingOnListMaintainer.getComplexValues(sittingOnListLocals);
		for (SittingOnListComplexValue value : sittingOnListValues) {
			populateComplexValue(value);
		}
		list.setSittingsOnList(sittingOnListValues);
		
		// Populate cases on list
		Collection<CaseOnList> caseOnListLocals = caseOnListMaintainer.findByListId(list.getListId());
		Collection<CaseOnListComplexValue> caseOnListValues = caseOnListMaintainer.getComplexValues(caseOnListLocals);
		for (CaseOnListComplexValue value : caseOnListValues) {
			populateComplexValue(value);
		}
		list.setCasesOnList(caseOnListValues);
	}
	
	/**
	 * Description: Populate list complex value with the latest
	 * versions of the supplied sittings and cases on list
	 * 
	 * @param list
	 * @throws ObjectNotFoundException
	 */
	protected void populateComplexValue(ListComplexValue list, ListValue listValue) throws ObjectNotFoundException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: populateComplexValue(list="+list+", listValue="+listValue+")");
       	}
		// Get sittings on list specified in list value
		ArrayList<SittingOnListComplexValue> sittingOnListValues = new ArrayList<SittingOnListComplexValue>();
		for (SittingOnListBasicValue value : listValue.getSittingsOnList()) {
			SittingOnList local = sittingOnListMaintainer.findByPrimaryKey(value.getSittingOnListId());
			SittingOnListComplexValue result = sittingOnListMaintainer.getComplexValue(local);
			populateComplexValue(result);
			sittingOnListValues.add(result);
		}
		list.setSittingsOnList(sittingOnListValues);
		
		// Get cases on list specified in list value
		ArrayList<CaseOnListComplexValue> caseOnListValues = new ArrayList<CaseOnListComplexValue>();
		for (CaseOnListBasicValue value : listValue.getCasesOnList()) {
			CaseOnList local = caseOnListMaintainer.findByPrimaryKey(value.getCaseOnListId());
			CaseOnListComplexValue result = caseOnListMaintainer.getComplexValue(local);
			populateComplexValue(result);
			caseOnListValues.add(result);
		}
		list.setCasesOnList(caseOnListValues);
	}

	/**
	 * Populate sitting on list complex value
	 * 
	 * @param value
	 * @throws ObjectNotFoundException
	 */
	protected void populateComplexValue(SittingOnListComplexValue value) throws ObjectNotFoundException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: populateComplexValue(value="+value+")");
       	}
		value.setRefJudge(getRefJudge(value.getJudgeRefId()));
		value.setTimeMarking(getRefSystemCode(value.getTimeMarkingId()));
	}

	/**
	 * Populate case on list complex value
	 * 
	 * @param value
	 * @throws ObjectNotFoundException
	 */
	protected void populateComplexValue(CaseOnListComplexValue value) throws ObjectNotFoundException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: populateComplexValue(value="+value+")");
       	}
		value.setHearingType(getRefHearingType(value.getHearingTypeId()));
		value.setCase(getCase(value.getCaseId()));
		value.setDefOnCaseOnLists(getDefOnCaseOnLists(value.getCaseOnListId()));
		value.setCaseListingEntry(getCaseListingEntry(value.getCaseId()));
		value.setDirectionsForCase(getDirectionsForCase(value.getCaseId()));
		value.setTimeMarking(getRefSystemCode(value.getTimeMarkingId()));
	}
	
	/**
	 * Description: Find RefJudgeBasicValue by refJudgeId
	 * 
	 * @param refJudgeId
	 * @throws ObjectNotFoundException 
	 */
	private RefJudgeComplexValue getRefJudge(Integer refJudgeId) throws ObjectNotFoundException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: getRefJudge(refJudgeId="+refJudgeId+")");
       	}
		RefJudgeComplexValue result = null;
        if (refJudgeId != null) {
        	// Get judge complex value
        	RefJudge refJudge = refJudgeMaintainer.findByPrimaryKey(refJudgeId);
			result = getRefJudgeComplexValue(refJudge);
		}
		return result;
	}

	/**
	 * Description: Find RefJudgeComplexValue by crestJudgeId
	 * 
	 * @param courtId
	 * @param crestJudgeId
	 */
	public RefJudgeComplexValue findJudgeByCourtIdAndCrestJudgeId(Integer courtId, Integer crestJudgeId) {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: findJudgeByCourtIdAndCrestJudgeId(courtId="+courtId+", crestJudgeId="+crestJudgeId+")");
       	}
		RefJudgeComplexValue result = null;
		if (crestJudgeId != null) {
			try {
				@SuppressWarnings("unchecked")
				java.util.List<RefJudge> refJudges = (java.util.List<RefJudge>) refJudgeMaintainer.findByCourtIdAndCrestJudgeId(courtId, crestJudgeId);
				if (refJudges != null && !refJudges.isEmpty()) {
					result = getRefJudgeComplexValue(refJudges.get(0));
				}
			} catch (ObjectNotFoundException e) {
				result = null;
			}
		}
		return result;
	}

	private RefJudgeComplexValue getRefJudgeComplexValue(RefJudge refJudge) throws ObjectNotFoundException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: getRefJudgeComplexValue(refJudge="+refJudge+")");
       	}
		RefJudgeComplexValue result = null;
		if (refJudge != null) {
        	result = refJudgeMaintainer.getComplexValue(refJudge);

        	// Get judge tickets
    		@SuppressWarnings("unchecked")
			Collection<RefJudgeTicket> refJudgeTickets = refJudgeTicketMaintainer.findJudgeTicketsByJudgeId(refJudge.getRefJudgeId());
    		result.setRefJudgeTickets(refJudgeTicketMaintainer.getBasicValues(refJudgeTickets));
        }
    	return result;
	}

	/**
	 * Description: Find RefHearingTypeBasicValue by refHearingTypeId
	 * 
	 * @param refHearingTypeId
	 * @throws ObjectNotFoundException 
	 */
	private RefHearingTypeBasicValue getRefHearingType(Integer refHearingTypeId) throws ObjectNotFoundException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: getRefHearingType(refHearingTypeId="+refHearingTypeId+")");
       	}
        RefHearingTypeBasicValue result = null;
        if (refHearingTypeId != null) {
        	RefHearingType rht = refHearingTypeMaintainer.findByPrimaryKey(refHearingTypeId);
            result = refHearingTypeMaintainer.getBasicValue(rht);
        }   
    	return result;
	}

	/**
	 * Description: Find CaseBasicValue by caseId
	 * 
	 * @param caseId
	 * @throws ObjectNotFoundException 
	 */
	private CaseBasicValue getCase(Integer caseId) throws ObjectNotFoundException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: getCase(caseId="+caseId+")");
       	}
        CaseBasicValue result = null;
        if (caseId != null) {
            Case cs = caseMaintainer.findByPrimaryKey(caseId);
            result = caseMaintainer.getCaseBasicValue(cs);
        }   
    	return result;
	}

	/**
	 * Description: Find DefOnCaseOnListBasicValue(s) by caseOnListId
	 * 
	 * @param caseOnListId
	 * @throws ObjectNotFoundException 
	 */
	private Collection<DefOnCaseOnListBasicValue> getDefOnCaseOnLists(Integer caseOnListId) throws ObjectNotFoundException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: getDefOnCaseOnLists(caseOnListId="+caseOnListId+")");
       	}
		Collection<DefOnCaseOnListBasicValue> results = null;
        if (caseOnListId != null) {
        	Collection<DefOnCaseOnList> locals = defOnCaseOnListMaintainer.findByCaseOnListId(caseOnListId);
            results = defOnCaseOnListMaintainer.getBasicValues(locals);
        }   
    	return results;
	}

	/**
	 * Description: Find CaseListingEntryBasicValue by caseId
	 * 
	 * @param caseId
	 * @throws ObjectNotFoundException 
	 */
	private CaseListingEntryBasicValue getCaseListingEntry(Integer caseId) throws ObjectNotFoundException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: getCaseListingEntry(caseId="+caseId+")");
       	}
		CaseListingEntryBasicValue result = null;
        if (caseId != null) {
        	try {
	        	CaseListingEntry cle = caseListingEntryMaintainer.findByCaseId(caseId);
	            result = caseListingEntryMaintainer.getBasicValue(cle);
			} catch (ObjectNotFoundException ex) {
				// Case listing entry may not exist, e.g. for U and B cases
			}
        }   
    	return result;
	}

	/**
	 * Description: Find DirectionsForCaseBasicValue by caseId
	 * 
	 * @param caseId
	 * @throws ObjectNotFoundException 
	 */
	private DirectionsForCaseBasicValue getDirectionsForCase(Integer caseId) throws ObjectNotFoundException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: getDirectionsForCase(caseId="+caseId+")");
       	}
        DirectionsForCaseBasicValue result = null;
        if (caseId != null) {
	       	try {
	       		DirectionsForCase local = directionsForCaseMaintainer.findByCaseId(caseId);
	       		result = directionsForCaseMaintainer.getBasicValue(local);
	   	    } catch (ObjectNotFoundException ex) {
				// Directions for case may not exist, e.g. for U and B cases
	        }
        }
       	return result;
	}

	/**
	 * Description: Find RefSystemCodeBasicValue by id
	 * 
	 * @param caseId
	 * @throws ObjectNotFoundException 
	 */
	private RefSystemCodeBasicValue getRefSystemCode(Integer id) throws ObjectNotFoundException {
		if ( log.isDebugEnabled() ) {
       		log.debug("START: getRefSystemCode(id="+id+")");
       	}
		RefSystemCodeBasicValue result = null;
        if (id != null) {
       		RefSystemCode local = refSystemCodeMaintainer.findByPrimaryKey(id);
       		result = refSystemCodeMaintainer.getBasicValue(local);
        }
       	return result;
	}
}