package uk.gov.courtservice.xhibit.business.services.results.authorise;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.results.authorise.AuthoriseCheckDatabaseManager;
import uk.gov.courtservice.xhibit.business.database.results.authorise.UnauthorisedCasesDatabaseManager;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddress;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_d20_offence_link.XhbD20OffenceLinkBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_d20_offence_link.XhbD20OffenceLinkBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendantBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_hearing.XhbHearing;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_offence.XhbRefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_offence.XhbRefOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_offence.XhbRefOffenceBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearingBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerLocal;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerLocalHome;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantOnCaseHelper;
import uk.gov.courtservice.xhibit.business.services.defendant.DisposalControllerException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerLocal;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerLocalHome;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.business.services.results.saver.DeportationCourtLogHelper;
import uk.gov.courtservice.xhibit.business.services.results.saver.HateCrimeCourtLogHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.DefendantChargesCompositeVO;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DisposalValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.common.results.vos.PleaValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationRequestValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthoriseWarning;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.D20OffenceLinkHelperValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.D20OffenceLinkReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.D20OffenceLinkValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.DefendantAuthorisationFailureValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.DefendantAuthorisationReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.DefendantOnCaseAuthorisationReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.FailureMessage;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.UnauthorisedCaseDeftValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.UnauthorisedCaseStatusValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.UnauthorisedCaseValue;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;

/**
 * <p>
 * Title: Results authorisation helper
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author Rakesh Lakhani
 * @version $Id: AuthorisationWorkFlow.java,v 1.12 2005/01/28 09:03:37 rzvddy
 *          Exp $
 * @history James Powell 13/03/2009 - Added a number of methods to provide functionality
 *          to get a list of unauthorised cases. Also modified <code>updateDefendantOnCaseFlags</code>
 *          to record the date of export.
 * @history Kelvin Davies 21/04/2009 - Altered processDefendantFlags to include new method of updating
 *          defendant on case records.
 */

public class AuthorisationWorkFlow {
	public static final Date DUMMY_DATE = new Date(0L);
	private static final String TT99 = "TT99"; // DVLC_CODE - Driving related
	private static final String DELIMITER = ",";
	private static final Logger LOG = CSServices.getLogger(AuthorisationWorkFlow.class);
    private static final String OBS_IND = "Y";
	private static final String TRIAL_CASE_TYPE = "T";
	private static final String SENTENCE_TYPE_CASE = "S";
	private static final String APPEAL_TYPE_CASE = "A";	
    private static final String INTERIM_DISQUALIFICATION = "DISINT"; // Driving Disposal
    private static final String YES = "Y";
    
    
    private AuthorisationWorkFlow() {
        // Empty
    }

    public static AuthorisationValue[] getAuthorisable(Integer caseId) throws ResultsControllerException {
        XhbCase caseValue = XhbCaseBeanHelper2.findByPrimaryKey(caseId);
        Collection defOnCase = caseValue.getXhbDefendantOnCases();
        
        Vector<AuthorisationValue> vector = new Vector<AuthorisationValue>();
        
        Iterator iter = defOnCase.iterator();
        while (iter.hasNext()) {
            XhbDefendantOnCase doc = (XhbDefendantOnCase) iter.next();
            if (doc.getObsInd() == null || !doc.getObsInd().equals("Y")) {
                vector.add(getAuthValue(doc));
            }
        }
        
        AuthorisationValue[] retVal = getAuthorisationValues(vector);
        return retVal;
    }

   
    public static AuthorisationValue[] getAuthorisableByDefendantOnCaseId(Integer defOnCaseID) throws ResultsControllerException {
    	XhbDefendantOnCase doc = XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(defOnCaseID);

        Vector<AuthorisationValue> vector = new Vector<AuthorisationValue>();
    	if (doc != null && !"Y".equals(doc.getObsInd())) {
    		vector.add(getAuthValue(doc));
        }

    	AuthorisationValue[] retVal = getAuthorisationValues(vector);
        return retVal;
    }
    

    public static AuthorisationValue[] getAuthorisationValues(Vector<AuthorisationValue> vector) {
    	AuthorisationValue[] retVal = new AuthorisationValue[vector.size()];
        int i = 0;
        for (AuthorisationValue authorisationValue : vector) {
            retVal[i++] = authorisationValue;
        }
        return retVal;
    }

    
    /**
     * Do the actual authorisation of results.
     * --- This is ONLY TO BE CALLED directly as part of the Authorise results process. ----
     * 
     * @param request
     * @param scheduledHearingId
     * @return
     * @throws ResultsControllerException
     */
    public static AuthorisationReturnValue authoriseResults(AuthorisationRequestValue request,
            Integer scheduledHearingId) throws ResultsControllerException{
        LOG.debug("authoriseResults - BEGIN");

        AuthorisationHelper authHelper = getAuthorisationHelper(request, scheduledHearingId);
        //logAuthValue(authHelper.getDefAuthReturnValues());
        
        processOffenceRules(request, authHelper);
        
        processD20OffenceLink(authHelper, request);

        updateDefendantOnCaseFlags(authHelper);

        createLog(request, scheduledHearingId, authHelper);

        LOG.debug("authoriseResults - ABOUT TO RETURN");

        return new AuthorisationReturnValue(authHelper.getCaseAuthorisationReturnValue(), authHelper
                .getDefAuthReturnValues(), authHelper.getDefOnCaseAuthReturnValues(),authHelper.getD20OffenceLinkReturnValue());
    }
    
    
    /**
     * 
     * @param request
     * @param scheduledHearingId
     * @return
     * @throws ResultsControllerException
     */
    public static AuthorisationReturnValue validateD20Order(AuthorisationRequestValue request,
            Integer scheduledHearingId) throws ResultsControllerException{
        
    	LOG.debug("validateD20Order - BEGIN");
    	AuthorisationHelper authHelper = getAuthorisationHelper(request, scheduledHearingId);
        
        processOffenceRules(request, authHelper);
        
        LOG.debug("validateD20Order - ABOUT TO RETURN");
        return new AuthorisationReturnValue(authHelper.getCaseAuthorisationReturnValue(), authHelper
                .getDefAuthReturnValues(), authHelper.getDefOnCaseAuthReturnValues(),authHelper.getD20OffenceLinkReturnValue());
    }

    
    /**
     * 
     * @param request
     * @param scheduledHearingId
     * @return
     * @throws ResultsControllerException
     */
	private static AuthorisationHelper getAuthorisationHelper(AuthorisationRequestValue request,
			Integer scheduledHearingId) throws ResultsControllerException {
		
		AuthorisationHelper authHelper = new AuthorisationHelper(request.getCaseId(), scheduledHearingId, request.getDefendantsToAuthorise());
		return authHelper;
	}


	/**
	 * 
	 * @param request
	 * @param authHelper
	 */
	private static void processOffenceRules(AuthorisationRequestValue request, AuthorisationHelper authHelper) {
		processCaseLevelRules(authHelper);

        processOffenceLevels(authHelper);

        processDefendants(authHelper);
        
        processDefOnCaseAndOffence(authHelper);
	}
    

	/**
	 * 
	 * @param request
	 * @param scheduledHearingId
	 * @param helper
	 * @return
	 * @throws ResultsControllerException
	 * @throws DisposalControllerException
	 * @throws ChargeControllerException
	 */
    public static D20OffenceLinkReturnValue generateD20OffenceLink(AuthorisationRequestValue request,
            Integer scheduledHearingId,D20OffenceLinkHelperValue helper ) throws ResultsControllerException, DisposalControllerException, ChargeControllerException{

    	LOG.debug("generateD20OffenceLink - BEGIN");
    	AuthorisationHelper authHelper = getAuthorisationHelper(request, scheduledHearingId);
    	
    	OffenceLinkValidationHelper validator = new OffenceLinkValidationHelper();
    	
    	// Re populate XHB_D20_OFFENCE_LINK if necessary, with the correct, and up to date, set of records  
	    validateAllOffenceLinks(validator, helper, authHelper);
	    
	    // Now find any existing data
	    List<XhbD20OffenceLinkBasicValue> d20OffenceLinkValue= validator.getD20OffenceLinks();
        
        D20OffenceLinkReturnValue returnVal = new D20OffenceLinkReturnValue();
        if (d20OffenceLinkValue != null) { // Pre-existing data in XHB_D20_OFFENCE_LINK exists
	        returnVal = generateD20OffencePopupData(d20OffenceLinkValue, helper.isD20Interim(), authHelper);
	        
	        returnVal.setFailures((ArrayList<String>) authHelper.getD20OffenceLinkReturnValue().getFailures());
        } else {
        	// TODO: Do we need to create data at this stage?
        	// Check where data is actually created.
        	LOG.debug("generateD20OffenceLink: No pre-existing data in XHB_D20_OFFENCE_LINK at this stage, is that right?");
        }
        LOG.debug("generateD20OffenceLink - ABOUT TO RETURN");
	    return returnVal;
	    
	}
    
    /**
     * Get the list of offences for defendant on case.
     * 
     * @param defendantOnCaseId
     * 
     * @return	the list
     */
    public static List<D20OffenceLinkValue> getD20OffenceLinks(Integer defendantOnCaseId, boolean isD20Interim){
    	LOG.debug("getD20OffenceLinks - BEGIN");
    	List<D20OffenceLinkValue> d20OffenceLinks = null;
    	XhbD20OffenceLinkBasicValue[] d20OffenceLinkValue= XhbD20OffenceLinkBeanHelper2.findByDefendantOnCaseIdValue(defendantOnCaseId);
    	DefendantOnCaseHelper defendantOnCaseHelper =  new DefendantOnCaseHelper();
    	ConcurrentHashMap<String, D20OffenceLinkValue> d20OffenceMap = new ConcurrentHashMap<String, D20OffenceLinkValue>();
    	
    	for (XhbD20OffenceLinkBasicValue d20OffenceLink : d20OffenceLinkValue) {
    		String dvlaCode = d20OffenceLink.getDvlaOffenceCode();
    		
    		if ( d20OffenceLink.getConvictionDate()!=null &&
    			 !hasNoDrivingDisposals(defendantOnCaseHelper, d20OffenceLink.getDefendantOnCaseId(), null) &&
    			 !(isD20Interim && dvlaCode.equals(TT99))){
				
				D20OffenceLinkValue d20Offence = new D20OffenceLinkValue();
				d20Offence.setDvlaOffence(dvlaCode);
				d20Offence.setObsInd(d20OffenceLink.getObsInd());
				d20Offence.setRefOffenceID(d20OffenceLink.getRefOffenceId());
				d20Offence.setSequenceNo(d20OffenceLink.getSeqNo());
				d20Offence.setDateOfConviction(d20OffenceLink.getConvictionDate());
				d20Offence.setDateOffence(getOffenceDate(d20OffenceLink));
				d20Offence.setInterimD20(d20OffenceLink.getIntD20());
				d20Offence.setFinalD20(d20OffenceLink.getFinalD20());
				d20Offence.setVersion(d20OffenceLink.getVersion());
			   
				setRefOffenceDetails(d20Offence,d20OffenceLink.getRefOffenceId());
				
				boolean removed = false;
				
				// Get the map key
				String d20Key = d20Offence.getOffence()+DELIMITER+
						d20Offence.getRefOffenceID().toString()+DELIMITER+
						d20Offence.getSequenceNo().toString()+DELIMITER+
						dvlaCode;

				// Has this offence appeared on a D20 already
				if(d20OffenceMap.containsKey(d20Key)) {
					// Remove the previous versions of this entry
					for (Map.Entry<String, D20OffenceLinkValue> off : d20OffenceMap.entrySet()) {
						if (d20Key.equals(off.getKey())) {
							if (off.getValue().getVersion() < d20OffenceLink.getVersion()) {
								d20OffenceMap.remove(off.getKey());
								removed = true;
								break;
							}
						}
					}
					if (removed) { // Add current offence
						d20OffenceMap.put(d20Key, d20Offence);
					}
				} else {
					d20OffenceMap.put(d20Key, d20Offence);
				}

				// Transfer the map to an array
				d20OffenceLinks = new ArrayList<D20OffenceLinkValue>();
				
		        for(Map.Entry<String, D20OffenceLinkValue> entry : d20OffenceMap.entrySet()) {
		        	d20OffenceLinks.add(entry.getValue());
				}

    		}
    	}
    	
    	LOG.debug("getD20OffenceLinks - ABOUT TO RETURN");
    	return d20OffenceLinks;
    }

    /**
     * 
     * @param d20OffenceLinkValue
     * @return
     */
	private static D20OffenceLinkReturnValue generateD20OffencePopupData(List<XhbD20OffenceLinkBasicValue> d20OffenceLinkValue, boolean isD20Interim,
			AuthorisationHelper helper) {
		
		LOG.debug("generateD20OffencePopupData - BEGIN");
		
		D20OffenceLinkReturnValue d20OffenceLinkReturnValue = new D20OffenceLinkReturnValue(); // We want to return a non-null object
		if (d20OffenceLinkValue.size()>0) {
			DefendantOnCaseHelper defendantOnCaseHelper =  new DefendantOnCaseHelper();
			ConcurrentHashMap<String, D20OffenceLinkValue> d20OffenceMap = new ConcurrentHashMap<String, D20OffenceLinkValue>();
			
			for (XhbD20OffenceLinkBasicValue d20OffenceLink : d20OffenceLinkValue) {
				String dvlaCode = d20OffenceLink.getDvlaOffenceCode();
			   
				if(d20OffenceLink.getConvictionDate()==null) {
				   LOG.debug("Reject ID: "+d20OffenceLink.getD20OffenceLinkId()+" - No conviction date");
				   continue;
				}
				
				if(!isD20Interim && hasNoDrivingDisposals(defendantOnCaseHelper, d20OffenceLink.getDefendantOnCaseId(), null)) {
					LOG.debug("Reject ID: "+d20OffenceLink.getD20OffenceLinkId()+" - No driving disposals");
					continue;
				}
				
				if (isD20Interim && dvlaCode.equals(TT99)) {
					LOG.debug("Reject ID: "+d20OffenceLink.getD20OffenceLinkId()+" - No TT99s on Interim");
					continue;
				}
				
				D20OffenceLinkValue d20Offence = new D20OffenceLinkValue();
				d20Offence.setDvlaOffence(dvlaCode);
				d20Offence.setObsInd(d20OffenceLink.getObsInd());
				d20Offence.setRefOffenceID(d20OffenceLink.getRefOffenceId());
				d20Offence.setSequenceNo(d20OffenceLink.getSeqNo());
				if (!AuthorisationWorkFlow.DUMMY_DATE.equals(d20OffenceLink.getConvictionDate())) {
					d20Offence.setDateOfConviction(d20OffenceLink.getConvictionDate());
				}
				d20Offence.setDateOffence(getOffenceDate(d20OffenceLink));
				d20Offence.setInterimD20(d20OffenceLink.getIntD20());
				d20Offence.setFinalD20(d20OffenceLink.getFinalD20());
				d20Offence.setVersion(d20OffenceLink.getVersion());
			   
				
				setRefOffenceDetails(d20Offence,d20OffenceLink.getRefOffenceId());
				
				boolean removed = false;
				// Get the map key
				String d20Key = d20Offence.getOffence()+DELIMITER+
						d20Offence.getRefOffenceID().toString()+DELIMITER+
						d20Offence.getSequenceNo().toString()+DELIMITER+
						dvlaCode;

				// Has this offence appeared on a D20 already
				if(d20OffenceMap.containsKey(d20Key)) {
					// Remove the previous versions of this entry
					for (Map.Entry<String, D20OffenceLinkValue> off : d20OffenceMap.entrySet()) {
						if (d20Key.equals(off.getKey())) {
							if (off.getValue().getVersion() < d20OffenceLink.getVersion()) {
								d20OffenceMap.remove(off.getKey());
								removed = true;
								break;
							}
						}
					}
					if (removed) { // Add current offence
						d20OffenceMap.put(d20Key, d20Offence);
					}
				} else {
					d20OffenceMap.put(d20Key, d20Offence);
				}
			}

			// Transfer the map to an array
			List<D20OffenceLinkValue> d20Offences = new ArrayList<D20OffenceLinkValue>();
	        for(Map.Entry<String, D20OffenceLinkValue> entry : d20OffenceMap.entrySet()) {
	        	d20Offences.add(entry.getValue());
			}

			d20OffenceLinkReturnValue = new D20OffenceLinkReturnValue();
			d20OffenceLinkReturnValue.setD20Offences(d20Offences);
		}
		
		// If we don't already have any failures then do a final check
		// to ensure that we don't display an empty pop-up
		// so this is a catch-all when no D20 offences have been added
		if ( ( d20OffenceLinkReturnValue.getFailures() == null || d20OffenceLinkReturnValue.getFailures().isEmpty() ) &&
			 ( d20OffenceLinkReturnValue.getD20Offences() == null || d20OffenceLinkReturnValue.getD20Offences().size() == 0)
				) {
			LOG.debug("Doing a final check and there are no valid offences that have been found to display on pop-up");
			helper.getD20OffenceLinkReturnValue().addFailureKey("results.authorise.d20.noValidOffences");
		}
		
		LOG.debug("generateD20OffencePopupData - ABOUT TO RETURN");
		return d20OffenceLinkReturnValue;
	}

	/**
	 * 
	 * @param d20OffenceLink
	 * @return
	 */
	private static Date getOffenceDate(XhbD20OffenceLinkBasicValue d20OffenceLink) {
		LOG.debug("getOffenceDate - BEGIN");
		if (d20OffenceLink.getFinalD20() !=null && d20OffenceLink.getFinalD20().equals("Y")) {
		    return d20OffenceLink.getFinalD20Date();
		} else {
			if ( d20OffenceLink.getIntD20() !=null && d20OffenceLink.getIntD20().equals("Y")) {
				return d20OffenceLink.getIntD20Date();
			}
		}
		
		LOG.debug("getOffenceDate - about to return null!!");
		return null;
	}

	
	
	/** 
	 * Method conducts the validation check of offences links to ensure they 
	 * Comply with the rules for allowed D20 offences
	 * Method primarily used for D20 offences
	 * 
	 * @param request properties used to query the offence link records
	 * @return the results of the checks made during validation
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	private static void validateAllOffenceLinks(OffenceLinkValidationHelper validator, D20OffenceLinkHelperValue helper, AuthorisationHelper authHelper) {
		
		LOG.debug("validateAllOffenceLinks - BEGIN");
		try {
			validator.validateResults(helper, authHelper);
		} catch(Exception e) {
			LOG.error("Error in validateAllOffenceLinks: ",e);
			CSServices.getDefaultErrorHandler().handleError(e, AuthorisationWorkFlow.class);
			throw new EJBException(e);
		}
		
		LOG.debug("validateAllOffenceLinks - END");
		
		// Default return value meaning the checks have all failed
	}
    
    
    /**
     * 
     * @param d20Offence
     * @param refOffenceId
     */
    private static void setRefOffenceDetails(D20OffenceLinkValue d20Offence, Integer refOffenceId) {
    	
    	if(refOffenceId!=null){
    		try{
    		    XhbRefOffenceBasicValue refOffence = XhbRefOffenceBeanHelper2.findByPrimaryKeyValue(refOffenceId);
    		    d20Offence.setOffence(refOffence.getOffenceCode());
    		    d20Offence.setOffenceDescription(refOffence.getOffenceDesc());
    		}
    		catch(XhbRefOffenceBeanNotFoundException ex){
    			LOG.debug("Ref offence not found");
    		}
    	}
	}

    /**
     * 
     * @param offence
     * @param caseId
     * @param defendantId
     * @param defendantOnCaseId
     * @return
     */
    private static DisposalValue[] getDisposal(OffenceValue offence, Integer caseId, Integer defendantId, Integer defendantOnCaseId) {
    	
    	LOG.debug("getDisposal - BEGIN");
    	
    	DefendantOnOffenceComplexValue defendantOnOffence = offence.getDefendantOnOffence(defendantId);
	    try {
	    	List<DisposalValue> value = Arrays.asList(getDefendantDelegate().getDisposalsForDefendantOnOffence(defendantOnOffence.getDefendantOnOffenceId(), defendantOnCaseId, caseId));
	    	List<DisposalValue> matchingDisposals = new ArrayList<DisposalValue>();
	    	for (int i=0; i < value.size(); i++) {
	    		if (value.get(i).getDefendantOnOffenceId().equals(defendantOnOffence.getDefendantOnOffenceId())) {
	    			matchingDisposals.add(value.get(i));
	    		}
	    	}

	    	DisposalValue[] disposals =  new DisposalValue[matchingDisposals.size()];
	    	matchingDisposals.toArray(disposals);
	    	
	    	LOG.debug("getDisposal - ABOUT TO RETURN");
	        return disposals;
	    } catch (Exception e) {
	    	LOG.debug("Error in retrieving the disposals");
	    }
	    
	    LOG.debug("getDisposal - About to return null!!");
		return null;
    }
   
    /**
     * 
     * @param caseId
     * @param defendantId
     * @return
     * @throws ChargeControllerException
     */
	private static Collection getOffences(Integer caseId, Integer defendantId) throws ChargeControllerException {
		Collection offences = null;
		ChargeCompositeValue ccv = null;
		List<OffenceValue> offencesForDefendant = new ArrayList<OffenceValue>();
		ccv = getChargeDelegate().getCharges(caseId, true);
		Collection charges = ccv.getCharges();
		Iterator chargeIterator = charges.iterator();
		
		while (chargeIterator.hasNext()) {	
            ChargeValue charge = (ChargeValue) chargeIterator.next();
            if(!charge.getChargeType().equals(ChargeTypes.BREACH.getChargeType())){
            	 offences = charge.getOffenceValues();
            	 if(offences!=null && offences.size() > 0){
            		 OffenceValue thisOffence = null;
            		 Iterator<OffenceValue> ov = offences.iterator();
            		 while(ov.hasNext()){
            			thisOffence = ov.next();
            			 Collection<Integer> defendantIds = thisOffence.getDefendantIDs();
            			 
            			 if ( defendantIds.contains( defendantId )){
 							offencesForDefendant.add( thisOffence );
 						}
            		 }
            	 }
            }
        }
        return offencesForDefendant;
	}
	
    /**
     *
     * 
     * @param 	request				The authorisation request
     * @param	scheduledHearingId	The scheduled hearing ID
     * 
     * @return	A fixed integer value
     * @throws ResultsControllerException 
     */
    public static AuthorisationReturnValue authoriseSentenceOutcome( AuthorisationRequestValue request, Integer scheduledHearingId ) 
    		throws ResultsControllerException
    {
    	Boolean outcome = new Boolean( false );				//	Assume failure
        AuthorisationHelper authHelper = getAuthorisationHelper(request, scheduledHearingId);

    	String caseType = authHelper.getCaseType();
    	
    	//	Only interested in Trial type cases at this point
    	if (caseType.equals( TRIAL_CASE_TYPE ) || caseType.equals( SENTENCE_TYPE_CASE )) {            
    		processOffenceRules(request, authHelper);
    		outcome = true;
    	}
    	
    	return new AuthorisationReturnValue(authHelper.getCaseAuthorisationReturnValue(), authHelper
                .getDefAuthReturnValues(), authHelper.getDefOnCaseAuthReturnValues(),authHelper.getD20OffenceLinkReturnValue());
    }
  
 
    /**
     * 
     * @param helper
     * @param request
     */
    private static void processD20OffenceLink(final AuthorisationHelper helper,AuthorisationRequestValue request){ 
    	
    	LOG.debug("processD20OffenceLink - BEGIN");
    	
    	/** set defendantAuthoriseCount **/
    	helper.getD20OffenceLinkReturnValue().setDefendantAuthoriseCount(request.getDefendantsToAuthorise().length);
	 
    	for(AuthorisationValue value : request.getDefendantsToAuthorise()){
    		Integer defendantOnCaseId = value.getDefendantOnCaseId();
    		Integer defendantId = value.getDefendant().getDefendantId();
    		XhbD20OffenceLinkBasicValue[] d20OffenceLink= XhbD20OffenceLinkBeanHelper2.findByDefendantOnCaseIdValue(defendantOnCaseId);
    		
    		//Rows in the XHB_D20_OFFENCE_LINK Table, check driving offences/disposals
    		if(d20OffenceLink.length!=0){
	       		for(XhbD20OffenceLinkBasicValue offenceLink : d20OffenceLink){
	       			processOffenceLink(helper, offenceLink, defendantOnCaseId, request.getCaseId());
	       		}
	       	}    

    		// Check for missing disposals
    		processDrivingOffencesWithoutDisposal(helper, defendantOnCaseId, request.getCaseType(), request.getCaseId(), defendantId, d20OffenceLink);
    	}
	    
    	LOG.debug("processD20OffenceLink - END");
    }

    
    /**
     * 
     * 
     * @param helper
     * @param offenceLink
     * @param defendantOnCaseId
     * @param caseId
     */
    private static void processOffenceLink(AuthorisationHelper helper, XhbD20OffenceLinkBasicValue offenceLink,Integer defendantOnCaseId, Integer caseId) {
   		if(offenceLink!=null){
			if(offenceLink.getFinalD20Date()!=null){
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
				addD20Failure(helper, "results.authorise.d20.offenceLinkFinalD20DateError");
	    		helper.getD20OffenceLinkReturnValue().setFinalD20Date(sdf.format(offenceLink.getFinalD20Date()));
			} else {
				processDrivingDisposal(helper, defendantOnCaseId, caseId);
				processD20SignedAndPrinted(helper, offenceLink);
			}
        }
    }

    /**
     * Validate the D20 is signed and printed (FinalD20 or IntD20 is 'Y')
     */
    private static void processD20SignedAndPrinted(AuthorisationHelper helper, final XhbD20OffenceLinkBasicValue offenceLink) {
    	if (offenceLink != null && !YES.equals(offenceLink.getFinalD20()) && !YES.equals(offenceLink.getIntD20())) {
    		addD20Failure(helper, "results.authorise.d20.offenceLinkError");
		}
    }

    private static void addD20Failure(AuthorisationHelper helper, final String messageKey) {
    	if (!helper.getD20OffenceLinkReturnValue().getFailures().contains(messageKey)) {
			helper.getD20OffenceLinkReturnValue().addFailureKey(messageKey);
		}	
    }

    /**
     * 
     * @param helper
     * @param defendantOnCaseId
     * @param caseId
     */
	private static void processDrivingDisposal(AuthorisationHelper helper, Integer defendantOnCaseId, Integer caseId) {
	    DefendantOnCaseHelper defendantOnCaseHelper =  new DefendantOnCaseHelper();
		if(hasNoDrivingDisposals(defendantOnCaseHelper,defendantOnCaseId,caseId)) {
			addD20Failure(helper, "results.authorise.d20.offenceLinkError");
		}
	}
	
	private static boolean hasNoDrivingDisposals(DefendantOnCaseHelper defendantOnCaseHelper, Integer defendantOnCaseId, Integer caseId) {
		try {
			DisposalValue[] disposals = defendantOnCaseHelper.getDisposalsForDefendantOnCase(defendantOnCaseId,caseId);
			if(!isDrivingDisposal(disposals)){
				return true;
			}
		} catch (DisposalControllerException ex) {
			LOG.debug(ex.getMessage());
			CSServices.getDefaultErrorHandler().handleError(ex, AuthorisationWorkFlow.class);
		}
		return false;
	}
	
	/**
	 * 
	 * @param helper
	 * @param defendantOnCaseId
	 * @param caseType
	 * @param caseId
	 * @param defendantId
	 */
	private static void processDrivingOffencesWithoutDisposal(AuthorisationHelper helper, Integer defendantOnCaseId, String caseType, Integer caseId, Integer defendantId, 
			XhbD20OffenceLinkBasicValue[] d20OffenceLink) {
		try {
			boolean hasDrivingOffences = false;
			Collection offences = getOffences(caseId, defendantId);		
			if (offences != null) {
				ResultsCompositeValue rcv = helper.getResultsCompositeValue();
				OffenceValue offence;
				XhbRefOffenceBasicValue refOffence;
				PleaValue plea;
		        VerdictValue verdict;
		        Date convictionDate;
				Iterator offenceIter = offences.iterator();
			    while (offenceIter.hasNext()) {
			        offence = (OffenceValue) offenceIter.next();
			        refOffence = getDefendantDelegate().getOffenceRefFromOffenceCode(offence.getRefOffenceID());
			        // Is a driving offence
					if ( refOffence.getDvlcCode() != null ) {  
						hasDrivingOffences = true;
				        DefendantOnOffenceComplexValue defendantOnOffence = offence.getDefendantOnOffence(defendantId);
				        //Is convicted 
				        plea = rcv.getPlea(defendantOnOffence.getDefendantOnOffenceId());
				        verdict = rcv.getVerdict(defendantOnOffence.getDefendantOnOffenceId());
				        convictionDate = getConvictionDate(plea, verdict, caseType, caseId);
				        if (convictionDate != null) {
					        DisposalValue[] disposals = getDisposal(offence,caseId, defendantId, defendantOnOffence.getDefendantOnCaseId());
					        // With no Driving Disposals
					        if (!isDrivingDisposal(disposals)) {
					        	helper.getD20OffenceLinkReturnValue().addFailureKey("results.authorise.d20.defendantWarningMessage");
					        	break;
					        }
				        }
				    }
			    }
			}
			
			// Has driving offences but no D20
			if (hasDrivingOffences && d20OffenceLink.length==0) {
				addD20Failure(helper, "results.authorise.d20.offenceLinkError");
			}
		} catch (Exception ex) {
			LOG.debug(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	private static Date getConvictionDate(PleaValue plea, VerdictValue verdict,String caseType, Integer caseId) {
		Date convictionDate = null;
    	if(caseType.startsWith(TRIAL_CASE_TYPE)){
			if(plea!=null && plea.isGuilty()){
				convictionDate = plea.getArraignmentDate();
			}
			else{
				if(verdict!=null && verdict.isGuiltyVerdict()){
				   convictionDate = verdict.getVerdictDate();
				}
			}
    	}
        if(caseType.startsWith(SENTENCE_TYPE_CASE) || caseType.startsWith(APPEAL_TYPE_CASE)){
        	try {
                XhbCaseBasicValue caseBasicValue=  XhbCaseBeanHelper2.findByPrimaryKeyValue(caseId);
            	if(caseBasicValue !=null){
            	    convictionDate = caseBasicValue.getMagConvictionDate();
            	}
             } catch (XhbCaseBeanNotFoundException ex) {
            	 LOG.debug("Could not get case information for case id : " + caseId);
             }

    	}
		return convictionDate;
		
	}

	private static boolean isDrivingDisposal(DisposalValue[] disposals) {
		 boolean drivingDisposal=false;
		 for(DisposalValue dispose : disposals){
			 if(!dispose.getRefDisposalType().getDisposalCode().equals(INTERIM_DISQUALIFICATION)){
				 try{
					 int number = getDefendantDelegate().getParentRefDisposalId(dispose.getRefDisposalTypeId(), dispose.getRefDisposalType().getCourtId());
					 drivingDisposal = isDrivingDisposal(number);
				 }
				 catch (Exception ex){
					 LOG.debug(ex.getMessage());
					 ex.printStackTrace();
				 }
			 }
			 if (drivingDisposal) {
				 break;
			 }
		 }
		 return drivingDisposal;
	} 
	
	public static boolean isDrivingDisposal(int parentRefDisposalId) {
		switch(parentRefDisposalId) {
		 case 500:
		 case 501: 
		 case 505:
				return true;
		}
		return false;
	}

    private static DefendantControllerBeanBusinessDelegate getDefendantDelegate() {
	    return DefendantControllerBeanBusinessDelegate.DelegateFactory.getInstance();
    }

	public static ChargeControllerBeanBusinessDelegate getChargeDelegate() {
		return ChargeControllerBeanBusinessDelegate.DelegateFactory.getInstance();
	}
	private static void logAuthValue(DefendantAuthorisationReturnValue[] defs) {
        DefendantAuthorisationReturnValue def = null;
        for (int i =0; i<defs.length; i++) {
            LOG.debug(defs[i].toString());
        }
    }
    
    
    /**
     * This method makes a call to a stored procedure to return a list of cases which are not authorised and have at 
     * least one deft with a disposal against every offence. It then iterates through the result set and creates an array of
     * UnauthorisedCaseStatusValue objects providing that the most recent hearing has ended and the case still meets the criteria.
     * <p/>
     * The criteria to return a case to the midtier are as follows:
     * <p/>
     * - The case must have its most recent hearing ended and
     * <br/>
     * - Have at least one defendant which
     * <br/>
     * <t/>1. Has at least one disposal against every offence and
     * <br/>
     * <t/>2. Has not been authorised
     * @param courtId
     * @return
     * @throws ResultsControllerException
     */
    public static UnauthorisedCaseStatusValue[] getUnauthorisedCaseStatusValues(Integer courtId)
            throws ResultsControllerException{
        LOG.debug("getUnauthorisedCaseStatusValues - BEGIN");

        //Call Stored Procedure to return a list of caseIds which are unauthorised
        UnauthorisedCasesDatabaseManager dbMan = new UnauthorisedCasesDatabaseManager();
        UnauthorisedCaseValue[] caseArray = dbMan.getUnauthorisedCases(courtId);
                              
        if(caseArray == null){
            LOG.debug("Null value retuned from call to database("+courtId+")");
            return null;
        }
        
        Vector <UnauthorisedCaseStatusValue> returnValues = new Vector<UnauthorisedCaseStatusValue>();
        for(int i = 0;i<caseArray.length;i++){
            //Process Each case           
            UnauthorisedCaseValue ucValue = caseArray[i];
            LOG.debug("Processing "+i+" Case:"+ucValue.getCase_id()+"at Court "+courtId);
            
            XhbCase caseValue = null;
            try{
                caseValue = XhbCaseBeanHelper2.findByPrimaryKey(ucValue.getCase_id());
            }catch(XhbCaseBeanNotFoundException e){
                //case not found so ignore
                LOG.debug("Case not found: "+ucValue.getCase_id()+" For Court: "+courtId.toString());
                continue;
            }
            if(caseValue == null){
                LOG.debug("caseValue returned for "+ucValue.getCase_id()+" is null");
                continue;
            }            

            UnauthorisedCaseStatusValue ucsv = null;
            try{
                 ucsv = constructReturnValue(caseValue);
            }catch(HearingNotEndedException ex){
                //Hearing has not ended so ignore this case
                LOG.debug("Most recent hearing not ended: "+caseArray[i].getCase_id());
                continue;
            }catch(NoDeftsNeedAuthorisingException e){
                //No Defendants need authorising so ignore this case
                LOG.debug("No Defendant needs authorising: "+caseArray[i].getCase_id());
                continue;
            }catch(NoHearingFoundException nhfe){
                //No hearing found so log an error but continue processing
                LOG.error("No hearings found for case: "+caseArray[i].getCase_id());
                continue;
            }
            
            returnValues.add(ucsv);        
        }   
        
        //Now construct the array to return
        UnauthorisedCaseStatusValue[] retVal = new UnauthorisedCaseStatusValue[returnValues.size()];
        int x = 0;
        for (UnauthorisedCaseStatusValue usv: returnValues) {
            retVal[x++] = usv;
        }
        
        LOG.debug("getUnauthorisedCaseStatusValues - about to return");
        return retVal;       
    }    
    
    /**
     * This method takes in many of the required objects to construct an UnauthorisedCaseStatusValue
     * which will be returned to the thick client.
     * @param defOnCaseCol
     * @param caseValue
     * @param caseBasic
     * @param courtId
     * @param dateClosed
     * @return UnauthorisedCaseStatusValue
     */
    private static UnauthorisedCaseStatusValue constructReturnValue(XhbCase caseValue)throws ResultsControllerException{
        XhbCaseBasicValue caseBasic = caseValue.getData();                            
        Collection defOnCaseCol = caseValue.getXhbDefendantOnCases();
        
        //Get an array of defendants to return
        UnauthorisedCaseDeftValue[] defs = getDefendantsArray(defOnCaseCol,caseValue.getCaseId());
        
        //Get the most recent scheduled hearing value for this case
        ScheduledHearingValue ts_Shv = getScheduledHearingValue(caseValue);                               
        
        //Get the end date for the hearing
        java.util.Date endDate = getHearingEndDate(ts_Shv);                
        
        //Get the court room for the hearing
        CourtRoomBasicValue courtRoom = getCourtRoom(ts_Shv);               
        
        //Construct the return value for the objects created above
        UnauthorisedCaseStatusValue usv = new UnauthorisedCaseStatusValue(defs,courtRoom,
                caseBasic,ts_Shv.getScheduledHearingId(),ts_Shv.getScheduledHearingBasicValue().getOriginalTime(),endDate);
        
        return usv;
    }
    
    /**
     * Run a pre-authorisation check.
     * @param caseId
     * @return AuthoriseCheckValue[]
     * @throws ResultsControllerException
     */
    public static AuthoriseWarning[] getAuthoriseWarnings(Integer caseId) 
    throws ResultsControllerException {
        LOG.debug("getAuthoriseCheckResults caseId=" + caseId + " - BEGIN");

        // AuthoriseCheckDatabaseManager calls a PL/SQL package to do the query
        AuthoriseCheckDatabaseManager dbMan = new AuthoriseCheckDatabaseManager();
        AuthoriseWarning[] results = dbMan.getAuthoriseWarnings(caseId);

        LOG.debug("getAuthoriseCheckResults found " + results.length + " results - END");
        return results;
    }
    
    /**
     * Check whether the Record Sheet Preview XML has been generated
     * @param defendantOnCaseId
     * @return preview generation status
     * @throws ResultsControllerException
     */
    public static String getRecordSheetPreviewGenerationStatus(Integer defendantOnCaseId) 
    throws ResultsControllerException {
        LOG.debug("getRecordSheetPreviewGenerationStatus defendantOnCaseId=" + defendantOnCaseId + " - BEGIN");

        RecordSheetPreviewDatabaseManager dbMan = new RecordSheetPreviewDatabaseManager();
        String status = dbMan.getRecordSheetPreviewGenerationStatus(defendantOnCaseId);

        LOG.debug("getRecordSheetPreviewGenerationStatus found " + status + " results - END");
        return status;
    }

    /**
     * Retrieve the generated Record Sheet Preview XML
     * @param defendantOnCaseId
     * @return Generated Record Sheet preview XML
     * @throws ResultsControllerException
     */
    public static String getGeneratedPreviewXML(Integer defendantOnCaseId) 
    throws ResultsControllerException {
        LOG.debug("getGeneratedPreviewXML defendantOnCaseId=" + defendantOnCaseId + " - BEGIN");

        RecordSheetPreviewDatabaseManager dbMan = new RecordSheetPreviewDatabaseManager();
        String previewXML = dbMan.getGeneratedPreviewXML(defendantOnCaseId);

        return previewXML;
    }
    
    
    /**
     * Record Sheet Preview XML generation requested
     * @param defendantOnCaseId
     * @throws ResultsControllerException
     */
    public static void requestRecordSheetPreviewGeneration(Integer defendantOnCaseId) 
    throws ResultsControllerException {
        LOG.debug("requestRecordSheetPreviewGeneration defendantOnCaseId=" + defendantOnCaseId + " - BEGIN");

        RecordSheetPreviewDatabaseManager dbMan = new RecordSheetPreviewDatabaseManager();
        dbMan.requestRecordSheetPreviewGeneration(defendantOnCaseId);

        LOG.debug("Record Sheet Preview Generation requested");
    }
    
    /**
     * Cancel Record Sheet Preview XML generation
     * @param defendantOnCaseId
     * @throws ResultsControllerException
     */
    public static void cancelRecordSheetPreviewGeneration(Integer defendantOnCaseId) 
    throws ResultsControllerException {
        LOG.debug("cancelRecordSheetPreviewGeneration defendantOnCaseId=" + defendantOnCaseId + " - BEGIN");

        RecordSheetPreviewDatabaseManager dbMan = new RecordSheetPreviewDatabaseManager();
        dbMan.cancelRecordSheetPreviewGeneration(defendantOnCaseId);

        LOG.debug("Record Sheet Preview Generation requested");
    }
    
    /**
     * Given a ScheduledHearingId, return the endDate for the hearing. If the start date
     * or end date are null then throw HearingNotEndedException
     * @param shv
     * @return
     * @throws HearingNotEndedException
     */
    private static java.util.Date getHearingEndDate(ScheduledHearingValue shv) 
        throws HearingNotEndedException{
        XhbScheduledHearing shValue = XhbScheduledHearingBeanHelper2.findByPrimaryKey(shv.getScheduledHearingId());
        XhbHearing hearing = shValue.getXhbHearing();
        if(hearing.getHearingStartDate()==null || hearing.getHearingEndDate()==null){
            //Hearing has not ended so need to ignore this case
            throw new HearingNotEndedException();
        }
        return hearing.getHearingEndDate();
    }
    
    /**
     * given a ScdheduledHearingValue, return the corresponding courtRoomBasicValue
     * @param shv
     * @return CourtRoomBasicValue
     */
    private static CourtRoomBasicValue getCourtRoom(ScheduledHearingValue shv){                
        if (shv == null){
            throw new CSUnrecoverableException("No hearings found for scheduled hearing");        
        }
        
        CourtRoomBasicValue courtRoom = shv.getCourtRoomBasicValue();
        
        return courtRoom;

    }
    
    /**
     * This method is responsible for finding and returning the most recent ScheduledHearingValue 
     * for a given case and courtId.
     * 
     * @param caseValue
     * @param courtId
     * @return
     */        
    private static ScheduledHearingValue getScheduledHearingValue(XhbCase caseValue) throws ResultsControllerException{
        CaseControllerLocal ccl = (CaseControllerLocal) CSServices.getEJBServices().createLocalSession(
                CaseControllerLocalHome.class);
        Collection schedHearings = null;
        try{
            //Get ScheduledHearing collection for this case
            schedHearings = ccl.getScheduledHearings(caseValue.getCaseId());
        }catch(CaseControllerException ex){
            throw new CSUnrecoverableException("Problem occurred when getting scheduled hearing for case:"+caseValue.getCaseId().toString());
        }
        
        if(schedHearings == null || schedHearings.isEmpty()){
            //There should always be at least one ScheduledHearing for a case
            throw new NoHearingFoundException();
        }
        
        LOG.debug("schedHearings found = "+schedHearings.size());
        
        //Get the scheduled hearing value with the most recent date
        uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue largestShv = getLargestSHV(schedHearings);              
                
        if(largestShv == null){
            throw new CSUnrecoverableException("Largest Hearing Value is null");
        }
        
        return getScheduledHearingValue(largestShv);
        
        
    }
       
    /**
     * This method takes a collection of ScheduledHearingValues and returns the object with the most recent HearingDate
     * @param Collection - Collection of ScheduledHearingValue objects
     * @return ScheduledHearingValue 
     */
    private static uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue getLargestSHV(
            Collection schedHearings) {
        
        Iterator myIter = schedHearings.iterator();
        uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue largestShv = null;
        while (myIter.hasNext()) {
            uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue item = (uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue) myIter
                    .next();
            if (largestShv == null) {
                largestShv = item;
            } else {
                largestShv = largestShv.getScheduledHearingDate().getTime().compareTo(item.getScheduledHearingDate().getTime()) > 0 ? largestShv
                        : item;
            }
        } 
        return largestShv;
    }
       
    /**
     * This method takes a ScheduledHearingValue and uses the HearingScheduleController EJB to return the full
     * ScheduledHearingValue
     * 
     * @param shv
     * @return
     */
    private static ScheduledHearingValue getScheduledHearingValue(
            uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue shv){
        Integer shvId = shv.getScheduledHearingID();
        LOG.debug("Largest shvid = "+shvId.toString());
        
        HearingScheduleControllerLocal hsl = (HearingScheduleControllerLocal) CSServices.getEJBServices().createLocalSession(
                HearingScheduleControllerLocalHome.class);        
        ScheduledHearingValue[] ts_Shv = null;
        try{
             //Call method on EJB
             ts_Shv = hsl.getScheduledHearings(new Integer[] { shvId });
        }catch(HearingScheduleException ex){
            throw new CSUnrecoverableException("Problem occurred when getting scheduled hearing value for id:"+shvId.toString());
        }
        if(ts_Shv==null || ts_Shv.length == 0){
            LOG.debug("ts_Shv = null");
            return null;
        }else{
            //Only interested in the first ScheduledHearingValue returned
            return ts_Shv[0];
        }
    }
    
    /**
     * This method takes a collection of DefendantOnCase values and constructs and returns an
     * array of UnauthorisedCaseDeftValue objects. An UnauthorisedCaseDeftValue object is an 
     * XhbDefendantBasicValue/XhbDefandantOnCaseBasicValue pair, which is required by the thick client 
     * 
     * @param defOnCaseCol
     * @return
     */
    private static UnauthorisedCaseDeftValue[] getDefendantsArray(Collection defOnCaseCol,Integer caseId) throws ResultsControllerException{
        int validDefCount = getValidDeftCount(defOnCaseCol);                 
        
        //Now create an array of that size
        UnauthorisedCaseDeftValue[] defArray = new UnauthorisedCaseDeftValue[validDefCount];
        boolean allDeftsAuthorised = true;
        boolean aDeftBeenAuthorised = false;
        
        Iterator iter = defOnCaseCol.iterator();
        int intDefs = 0;
        
        while (iter.hasNext()) {
            XhbDefendantOnCase doc = (XhbDefendantOnCase)iter.next();
            if(doc.getObsInd() != null && doc.getObsInd().equals(OBS_IND)){
                //Ignore obsolete defendants on case
                continue;
            }
            
            if(doc.getResultsVerified() == null || !doc.getResultsVerified().equals(AuthorisationValue.AUTHORISED_EXPORT_SUCCESS)){
                allDeftsAuthorised = false;
            }else{
                aDeftBeenAuthorised=true;
            }
            
            XhbDefendantBasicValue defBasic = doc.getXhbDefendant().getData();
            XhbDefendantOnCaseBasicValue defOnCaseBasic = doc.getData();
            UnauthorisedCaseDeftValue ucDef = new UnauthorisedCaseDeftValue();
            ucDef.setDefBasic(defBasic);
            ucDef.setExportDate(defOnCaseBasic.getDateExported());
            ucDef.setAuthoriseStatus(defOnCaseBasic.getResultsVerified());
            defArray[intDefs] = ucDef;
            intDefs++;
        }
        /*
         * We need to check that the case still contains a defendant which meets the criteria. This is because the
         * situation could occur where a defendant could be picked up by the initial query as 'Unauthorised' but could have
         * been changed to 'Authorised Successful' in the interim period. This would result in the
         * case being shown unnecessarily in the grid.
         */
        if(allDeftsAuthorised){
            //All defendants have been authorised on this case so it should be ignored
            throw new NoDeftsNeedAuthorisingException();
        }
        
        if(aDeftBeenAuthorised && !deftNeedsAuthorising(defOnCaseCol, caseId)){
            /*at least One defendant has been authorised and no other defts need authorising
            This will be because of the situation described above. So, ignore this case*/
            throw new NoDeftsNeedAuthorisingException();
        }
        
        return defArray;
    }    
    
    private static int getValidDeftCount(Collection defOnCaseCol){       
        /*We first need to establish how many defendants are not obsolete before creating 
        an array of that size*/
        Iterator iter = defOnCaseCol.iterator();
        int validDefCount = 0;
        while (iter.hasNext()) {
            XhbDefendantOnCase doc = (XhbDefendantOnCase)iter.next();
            if(doc.getObsInd() == null ||! doc.getObsInd().equals(OBS_IND)){
                //defendant is not obsolete so increment validDefCount
                validDefCount++;
            }
        }
        return validDefCount;
    }
        
    /**
     * This class takes a collection of defendants and a caseId. It tries to determine whether at least one
     * defendant meets the criteria to be returned to the thick client as an 'Unauthorised Case'. Currently,
     * these criteria are that the case has at least one defendant which has a disposal against all offences 
     * and has not yet been authorised.
     * <p/>
     * The code iterates through the defendant on case collection and exits the method if it finds one defendant
     * which meets the criteria. This is to avoid unnecassarily processing the entire collection of
     * defendants.
     * @param defOnCaseCol
     * @param caseId
     * @return
     * @throws ResultsControllerException
     */
    private static boolean deftNeedsAuthorising(Collection defOnCaseCol,Integer caseId)throws ResultsControllerException {
        UnauthorisedCasesHelper helper = new UnauthorisedCasesHelper(caseId);
        
        Iterator iter = defOnCaseCol.iterator();                        
        while (iter.hasNext()) {
            XhbDefendantOnCase doc = (XhbDefendantOnCase)iter.next();
            if(doc.getObsInd() == null || !doc.getObsInd().equals(OBS_IND)){
                if(doc.getResultsVerified() == null || !doc.getResultsVerified().equals(AuthorisationValue.AUTHORISED_EXPORT_SUCCESS)){
                    LOG.debug("caseId: "+doc.getCaseId()+". defNeedsVerifyin. checking disposals");
                    if( helper.deftHasDisposalAgainstAllCounts(doc.getDefendantId())){
                        //Defendant has not been authorised and has a disposal against every count
                        return true;                
                    }
                }                   
            }
        }
        
        return false;
    }
    
    
    
    
    
    /**
     * CR46, Create a court log after acknowledging that the authorisation has
     * been successful.
     *
     * @param request
     *            User Interface Call
     * @param scheduledHearingId
     *            Needed to create court log
     * @param authHelper
     *            Holds the return values and error reports
     */
    private static void createLog(AuthorisationRequestValue request, Integer scheduledHearingId,
            AuthorisationHelper authHelper) {
        // Check if case has been verified
        if (!authHelper.getCaseAuthorisationReturnValue().hasCaseLevelFailed()) {
            DefendantAuthorisationReturnValue[] av = authHelper.getDefAuthReturnValues();
            DefendantOnCaseAuthorisationReturnValue[] docav = authHelper.getDefOnCaseAuthReturnValues();
            for (int i = 0; i < av.length; i++) {
                // Check if defendant has been verified
                if (!av[i].hasDefendantFailed() && !docav[i].hasDefendantFailed()) {
                    try {
                        AuthoriseResultsCourtLogHelper.getInstance().log(request.getCaseId(),
                                request.getCourtLogDate(), scheduledHearingId, av[i].getDefendantOnCaseId(),
                                av[i].getDefendant().getSurname() + ", " + av[i].getDefendant().getFirstName());
                    } catch (CourtLogBusinessException ex) {
                        LOG.error(ex.getMessage());
                    }
                    
                    try {
                        //CCN400 - Updated to include call to create deportation event
                        DeportationCourtLogHelper.getInstance().log(request.getCaseId(),
                                request.getCourtLogDate(), scheduledHearingId, av[i].getDefendantOnCaseId(),
                                av[i].getDefendant().getSurname() + ", " + av[i].getDefendant().getFirstName());
                        
                        // 2014 Legislative changes
                        HateCrimeCourtLogHelper.getInstance().log(request.getCaseId(),
                                request.getCourtLogDate(), scheduledHearingId, av[i].getDefendantOnCaseId(),
                                av[i].getDefendant().getSurname() + ", " + av[i].getDefendant().getFirstName());
                    } catch (CourtLogBusinessException ex) {
                        LOG.error(ex.getMessage());
                    }
                    
                    
                }
            }
        }
    }

    private static AuthorisationValue getAuthValue(XhbDefendantOnCase defOnCase) {
        return new AuthorisationValue(defOnCase.getXhbDefendantData(), defOnCase.getDefendantOnCaseId(), defOnCase
                .getResultsVerified(), defOnCase.getAmendedReason());
    }

    private static void processCaseLevelRules(AuthorisationHelper helper) {
        CaseRule[] rules = AuthorisationRuleFactory.getCaseLevelRules(helper.getCaseType(), helper.getCaseSubType());
        for (int i = 0; i < rules.length; i++) {
        	Map <Integer,DefendantChargesCompositeVO> selectedDefendantChargesCompositeMap = helper.getDefendantChargesOnCase();
            String[] failures = rules[i].process(helper.getResultsCompositeValue(), selectedDefendantChargesCompositeMap);
            for (int f = 0; f < failures.length; f++) {
                helper.getCaseAuthorisationReturnValue().addFailureKey(failures[f]);
            }
        }
    }

    /*
     * If a case is pre-bichard and missing bichard data that is ok, provided all
     * offences on the case are missing it.  If any bichard data is provided then
     * it must be provided for ALL offences.
     */
    private static boolean isOkToCheckBichardData(AuthorisationHelper helper) {
        boolean bichardOffenceFound = false;
        boolean preBichardOffenceFound = false;
        
        ChargeCompositeValue chargeValue = helper.getResultsCompositeValue().getChargeCompositeValue();
        Collection charges = chargeValue.getCharges();
        Iterator chargeIter = charges.iterator();
        while (chargeIter.hasNext()) {
            ChargeValue charge = (ChargeValue) chargeIter.next();
            
            if (!bichardChargeType(charge.getChargeType())) {
                continue;
            }
            
            Collection offences = charge.getOffenceValues();
            Iterator offenceIter = offences.iterator();
            while (offenceIter.hasNext()) {
                OffenceValue offence = (OffenceValue) offenceIter.next();
                
                if (isValidBichardCharge(offence)){
                    bichardOffenceFound = true;
                }else{
                    preBichardOffenceFound = true;
                }

                if (bichardOffenceFound && preBichardOffenceFound){
                    return true;
                }
            }
        }
        return false;
    }
    
    /*
     * Return true if the charge type is one that bichard data checks
     * should be carried out on.
     */
    private static boolean bichardChargeType(String chargeType) {
        return (chargeType.equals(ChargeTypes.INDICTMENT.getChargeType())
                || chargeType.equals(ChargeTypes.BREACH.getChargeType())
                || chargeType.equals(ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType())
                || chargeType.equals(ChargeTypes.SECTION_41.getChargeType()));
    }
    
    /*
     * Return true if the bichard offence has all the mandatory bichard data.
     */
    private static boolean isValidBichardCharge(OffenceValue offence) {
        if (offence.getOffenceStartDateTime() == null) {
            return false;
        }
        if (offence.getAddressId() == null) {
            return false;
        }
        /*
         * The AddressValue is left empty by
         * GetChargesHelper.populateOffenceValue
         * 
        AddressValue addressValue = offence.getAddressValue();
        if (addressValue == null) {
            return false;
        }
        if (addressValue.getAddress1() == null 
                || addressValue.getAddress1().equals("")) {
            return false;
        }
        if (addressValue.getAddress2() == null 
                || addressValue.getAddress2().equals("")) {
            return false;
        }
        */
        try {
            XhbAddress address =
                XhbAddressBeanHelper2.findByPrimaryKey(offence.getAddressId());
            
            if (address == null) {
                return false;
            }
            if (address.getAddress1() == null 
                    || address.getAddress1().equals("")) {
                return false;
            }
            if (address.getAddress2() == null 
                    || address.getAddress2().equals("")) {
                return false;
            }
            
        } catch (XhbAddressBeanNotFoundException e) {
            return false;
        }
        
        return true;
    }
    
    
    private static void processOffenceLevels(AuthorisationHelper helper) {
        final boolean checkBichardData = isOkToCheckBichardData(helper);
        ChargeCompositeValue chargeValue = helper.getResultsCompositeValue().getChargeCompositeValue();
        Collection charges = chargeValue.getCharges();
        Iterator chargeIter = charges.iterator();
        while (chargeIter.hasNext()) {
            ChargeValue charge = (ChargeValue) chargeIter.next();
            Collection offences = charge.getOffenceValues();
            Iterator offenceIter = offences.iterator();
            while (offenceIter.hasNext()) {
                OffenceValue offence = (OffenceValue) offenceIter.next();
                Collection defendantIds = offence.getDefendantIDs();
                Iterator defendantIter = defendantIds.iterator();
                while (defendantIter.hasNext()) {
                    Integer defendantId = (Integer) defendantIter.next();
                    DefendantOnOffenceComplexValue defendantOnOffence = offence.getDefendantOnOffence(defendantId);
                    if (defendantOnOffence != null) {
	                    Integer defendantOnCaseId = defendantOnOffence.getDefendantOnCaseId();
	                    if (LOG.isDebugEnabled()) {
	                        LOG.debug("defendant on case id being processed = " + defendantOnCaseId);
	                        LOG.debug("for Charge Type: " + charge.getChargeType() + " no. " + charge.getCrestChargeSeqNo()
	                                + ", Offence no. " + offence.getCrestOffenceSeqNo());
	                    }
	                    if (helper.processDefendant(defendantOnCaseId)) {
	                        processOffenceLevelRules(checkBichardData, helper, charge, offence, offence.getDefendantOnOffence(defendantId));
	                    }
                    }
                }
            }
        }
    }

    private static void processOffenceLevelRules(
            boolean checkBichardData,
            AuthorisationHelper helper, 
            ChargeValue charge, 
            OffenceValue offence,
            DefendantOnOffenceComplexValue defendant) {
        
        OffenceRule[] rules = AuthorisationRuleFactory.getOffenceLevelRules(
                checkBichardData, charge.getChargeType());

        for (int i = 0; i < rules.length; i++) {
            LOG.debug("About to process rule: " + rules[i].getClass().getName());
            String[] failures = rules[i].process(helper.getResultsCompositeValue(), offence, defendant);
            for (int f = 0; f < failures.length; f++) {
                DefendantAuthorisationFailureValue fail = new DefendantAuthorisationFailureValue(charge, offence,
                        failures[f]);
                helper.getDefAuthReturnValue(defendant.getDefendantOnCaseId()).addFailure(fail);
            }
        }
    }

    private static void processDefendants(final AuthorisationHelper helper) {
        LOG.debug("processDefendants - BEGIN");

        DefendantRule[] rules = AuthorisationRuleFactory.getDefendantLevelRules(helper.getCaseType(), helper
                .getCaseSubType());

        if (rules.length > 0) {
            ChargeCompositeValue chargeCompositeValue = helper.getResultsCompositeValue().getChargeCompositeValue();
            Collection defendants = chargeCompositeValue.getAllDefendants();
            if (defendants != null && defendants.size() > 0) {
                for (Iterator i = defendants.iterator(); i.hasNext();) {
                    DefendantValue defendant = (DefendantValue) i.next();
                    processDefendantLevelRules(helper, rules, defendant);
                }
            }
        }
        LOG.debug("processDefendants - END");
    }

    private static void processDefendantLevelRules(final AuthorisationHelper helper, final DefendantRule[] rules,
            final DefendantValue defendant) {
        for (int i = 0; i < rules.length; i++) {
            Integer defendantOnCaseId = defendant.getDefOnCaseBasicValue().getId();

            FailureMessage[] failures = rules[i].process(helper.getResultsCompositeValue(), defendantOnCaseId);

            for (int f = 0; f < failures.length; f++) {
                helper.getDefOnCaseAuthReturnValue(defendantOnCaseId).addFailure(failures[f]);
            }
        }
    }

    private static void processDefOnCaseAndOffence(final AuthorisationHelper helper) {
        LOG.debug("processDefOnCaseAndOffence - BEGIN");

        LOG.debug("processDefOnCaseAndOffence - Case Type:" + helper.getCaseType());
        
        DefOnCaseAndOffenceRule[] rules = AuthorisationRuleFactory.getDefOnCaseAndOffenceLevelRules(helper.getCaseType());

        if (rules.length > 0) {
            processDefOnCaseAndOffenceLevelRules(helper, rules);
        }
        LOG.debug("processDefOnCaseAndOffence - END");
    }

    private static void processDefOnCaseAndOffenceLevelRules(final AuthorisationHelper helper, 
                                                             final DefOnCaseAndOffenceRule[] rules) {
        
        Map <Integer,DefendantChargesCompositeVO> defendantChargesCompositeMap = helper.getDefendantChargesOnCase();
        Integer defendantId = null;
        Integer defendantOnCaseId = null;
        boolean isUnrelatedDisposal = false;
        
        for (int i = 0; i < rules.length; i++) 
        {
            LOG.debug("processDefOnCaseAndOffenceLevelRules - processing rule");
            
            for(DefendantChargesCompositeVO defendantChargesCompositeVO:defendantChargesCompositeMap.values())
            {
                LOG.debug("processDefOnCaseAndOffenceLevelRules - processing defendantChargesCompositeVO");
                
                if(defendantChargesCompositeVO.getDefendantOnCase()!=null &&
                   defendantChargesCompositeVO.getDefendantOnCase().getDefendantId()!=null &&
                   defendantChargesCompositeVO.getDefendantOnCase().getDefendantOnCaseId()!=null)
                {
                    defendantId = defendantChargesCompositeVO.getDefendantOnCase().getDefendantId();
                    defendantOnCaseId = defendantChargesCompositeVO.getDefendantOnCase().getDefendantOnCaseId();
                    
                    LOG.debug("processDefOnCaseAndOffenceLevelRules - defendantId:" + defendantId);
                    LOG.debug("processDefOnCaseAndOffenceLevelRules - defendantOnCaseId:" + defendantOnCaseId);
                    
                    isUnrelatedDisposal = helper.isUnrelatedDisposalsOnCase(defendantId);
                    
                    FailureMessage[] failures = rules[i].process(defendantChargesCompositeVO,isUnrelatedDisposal);

                    for (int f = 0; f < failures.length; f++) {
                        helper.getDefOnCaseAuthReturnValue(defendantOnCaseId).addFailure(failures[f]);
                    }
                    
                    defendantId = null;
                    defendantOnCaseId = null;                
                }
                else
                {
                    LOG.info("processDefOnCaseAndOffenceLevelRules - Def On Case is Null");
                }
            }
        }
    }
    
    
    /**
     * Goes through each defendant to be processed and if no failures are
     * recorded sets the results verified to
     * AuthorisationValue.AUTHORISED_READY. Note: if there is a case level
     * failure, no defendants will be authorised.
     *
     * If all defendants on the case are authorised
     *
     * @see isAuthorised then the case level ResultsVerified is also set to
     *      AuthorisationValue.AUTHORISED_READY.
     * @param helper
     *            AuthorisationHelper
     */
    private static void updateDefendantOnCaseFlags(AuthorisationHelper helper) {
        if (helper.getCaseAuthorisationReturnValue().hasCaseLevelFailed()) {
            // if case level fails no defendants can be authorised
            return;
        }

        boolean allAuthorised = true;

        XhbCase caze = helper.getXhbCase();
        Collection defs = caze.getXhbDefendantOnCases();

        Iterator iter = defs.iterator();
        while (iter.hasNext()) {
            XhbDefendantOnCase item = (XhbDefendantOnCase) iter.next();

            if (isDefendantReadyForAuthorisation(helper, item.getDefendantOnCaseId())
                    && isDefOnCaseReadyForAuthorisation(helper, item.getDefendantOnCaseId())) {
                
                /*item.setResultsVerified(AuthorisationValue.AUTHORISED_READY);
                Calendar cal = Calendar.getInstance();
                item.setDateExported(new java.sql.Date(cal.getTime().getTime()));
                */
                String amendedReason = "";
                // Ensure defendant on case id matches up
                DefendantAuthorisationReturnValue[] defsReturn = helper.getDefAuthReturnValues();
                for (int i =0; i<defsReturn.length; i++) {
                    if ((defsReturn[i] != null) && (defsReturn[i].getDefendantOnCaseId() != null) && (item.getDefendantOnCaseId() != null)) { 
                        if (defsReturn[i].getDefendantOnCaseId().intValue() == item.getDefendantOnCaseId().intValue()) {
                            amendedReason = defsReturn[i].getAmendedReason();
                            LOG.debug("Setting amendedReason to: "+amendedReason);
                        }
                    }
                }
                    
                helper.saveDefendant(AuthorisationValue.AUTHORISED_READY, item.getCaseId(), item.getDefendantId(), amendedReason);
                
            }

            allAuthorised = allAuthorised && isAuthorised(item);
        }

        if (allAuthorised) {
            caze.setResultsVerified(AuthorisationValue.AUTHORISED_READY);
        }
    }

    private static boolean isDefendantReadyForAuthorisation(final AuthorisationHelper helper,
            final Integer defendantOnCaseId) {
        boolean defendantReadyForAuthorisation = false;

        if (helper.processDefendant(defendantOnCaseId)) {
            if (!helper.getDefAuthReturnValue(defendantOnCaseId).hasDefendantFailed()) {
                defendantReadyForAuthorisation = true;
            }
        }
        return defendantReadyForAuthorisation;
    }

    private static boolean isDefOnCaseReadyForAuthorisation(final AuthorisationHelper helper,
            final Integer defendantOnCaseId) {
        boolean defendantReadyForAuthorisation = false;

        if (helper.processDefOnCase(defendantOnCaseId)) {
            if (!helper.getDefOnCaseAuthReturnValue(defendantOnCaseId).hasDefendantFailed()) {
                defendantReadyForAuthorisation = true;
            }
        }
        return defendantReadyForAuthorisation;
    }

    /**
     * Returns if the defendant on case is authorised, which includes ready for
     * export, exporting and export success states.
     *
     * @param def
     * @return
     */
    private static boolean isAuthorised(XhbDefendantOnCase def) {
        String authString = def.getResultsVerified();
        return authString != null
                && (authString.equals(AuthorisationValue.AUTHORISED_READY)
                        || authString.equals(AuthorisationValue.AUTHORISED_EXPORTING) || authString
                        .equals(AuthorisationValue.AUTHORISED_EXPORT_SUCCESS));
    }

    /**
     * Return the XhbRefOffenceBasicValue by it's primary key
     * 
     * @param xhbRefOffenceId			The primary key
     * 
     * @return							The instance
     */
	public static XhbRefOffenceBasicValue getRefOffence(Integer xhbRefOffenceId) {
		return XhbRefOffenceBeanHelper2.findByPrimaryKeyValue(xhbRefOffenceId);
	}    
	
}