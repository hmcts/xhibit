package uk.gov.courtservice.xhibit.business.services.results;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.exception.CSException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.results.OARDAPrintInformation;
import uk.gov.courtservice.xhibit.business.database.results.OWRRPrintInformation;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSite;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_pub_running_list.XhbPubRunningListBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_pub_running_list.XhbPubRunningListBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_app_res_d20_map.XhbRefAppResD20Map;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_app_res_d20_map.XhbRefAppResD20MapBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_offence.XhbRefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerException;
import uk.gov.courtservice.xhibit.business.services.darts.DartsHelper;
import uk.gov.courtservice.xhibit.business.services.defendant.DisposalControllerException;
import uk.gov.courtservice.xhibit.business.services.results.authorise.AuthorisationWorkFlow;
import uk.gov.courtservice.xhibit.business.services.results.authorise.D20OffenceValidator;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.ADJSSReportList;
import uk.gov.courtservice.xhibit.common.results.vos.CFIXReport;
import uk.gov.courtservice.xhibit.common.results.vos.CTLRLReport;
import uk.gov.courtservice.xhibit.common.results.vos.CTLRPExReport;
import uk.gov.courtservice.xhibit.common.results.vos.CTLRPReport;
import uk.gov.courtservice.xhibit.common.results.vos.DARTSPrintValue;
import uk.gov.courtservice.xhibit.common.results.vos.DOCARPrintValue;
import uk.gov.courtservice.xhibit.common.results.vos.DRSRReport;
import uk.gov.courtservice.xhibit.common.results.vos.INFTRPCCaseNumReport;
import uk.gov.courtservice.xhibit.common.results.vos.INFTRPCReport;
import uk.gov.courtservice.xhibit.common.results.vos.ISingleRunLetterReport;
import uk.gov.courtservice.xhibit.common.results.vos.LFIXReport;
import uk.gov.courtservice.xhibit.common.results.vos.LFIXRunDate;
import uk.gov.courtservice.xhibit.common.results.vos.LODReport;
import uk.gov.courtservice.xhibit.common.results.vos.NFIXReport;
import uk.gov.courtservice.xhibit.common.results.vos.NHAReport;
import uk.gov.courtservice.xhibit.common.results.vos.NTRSFReport;
import uk.gov.courtservice.xhibit.common.results.vos.OBWPrintValue;
import uk.gov.courtservice.xhibit.common.results.vos.OGRDAOrder;
import uk.gov.courtservice.xhibit.common.results.vos.OGRROrder;
import uk.gov.courtservice.xhibit.common.results.vos.OUTCReport;
import uk.gov.courtservice.xhibit.common.results.vos.OWRDAPrintInformation;
import uk.gov.courtservice.xhibit.common.results.vos.PRLISReport;
import uk.gov.courtservice.xhibit.common.results.vos.PleaSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.RAGEReport;
import uk.gov.courtservice.xhibit.common.results.vos.RELCJReport;
import uk.gov.courtservice.xhibit.common.results.vos.RJSReport;
import uk.gov.courtservice.xhibit.common.results.vos.RRCAReport;
import uk.gov.courtservice.xhibit.common.results.vos.RRECReport;
import uk.gov.courtservice.xhibit.common.results.vos.RSITReport;
import uk.gov.courtservice.xhibit.common.results.vos.RUMOReport;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.UNLCReport;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationRequestValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthoriseWarning;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.D20OffenceLinkHelperValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.D20OffenceLinkReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.D20OffenceLinkValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.UnauthorisedCaseStatusValue;
import uk.gov.courtservice.xhibit.common.results.vos.common.OARRPrintInformation;

/**
 * <p>
 * Title: Results2ControllerBean
 * </p>
 * <p>
 * Description: Session Bean providing functionality for accessing and updating
 * case results
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @ejb.bean name="Results2Controller" description="Results Controller Bean"
 *           type="Stateless" view-type="both"
 *           jndi-name="Results2ControllerHome"
 *           local-jndi-name="Results2ControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 * @author Bal Bhamra, Will Fardell, Abdul Hussain
 * @version $Id: Results2ControllerBean.java,v 1.33 2005/04/15 15:36:07 szfnvt
 *          Exp $
 * @history James Powell 13/03/2009 - Added <code>getUanuthorisedCaseStatuses</code>
 * @history James Powerll 18/03/2009 - Added <code>getCharges</code>
 */
public class Results2ControllerBean extends CSSessionBean implements SessionBean {

    private static final long serialVersionUID = 1L;
    private DartsHelper dartsHelper;
    
    /**
     * Is this case varied?
     * 
     * @param 	caseNumber				The case number (AYYYYNNNN)
     * 
     * @return	true if varied
     * @ejb.interface-method view-type="both"
     */
    public boolean isVaried( Integer caseId ){
    	return Results2WorkFlow.isVaried(caseId);
    }
    /**
     * Get the results reference data for the specified court
     * 
     * @return the results reference data for the specified result type
     * @throws ResultsControllerException
     *             if an error occures
     * @ejb.interface-method view-type="both"
     */
    public ResultsReferenceValue getReference(Integer courtId) throws ResultsControllerException {
        return Results2WorkFlow.getReference(courtId);
    }

    /**
     * Creates a batch of results in XHIBIT and CREST
     * 
     * @param Integer
     *            caseId
     * @param Integer
     *            scheduledHearingId
     * @return ResultsCompositeValue an object containing ChargeCompositeValue
     *         and ArrayLists of pleas, verdicts, disposals.
     * @throws ResultsControllerException.
     *             When one or all Results cannot be created in XHIBIT or CREST.
     * @ejb.interface-method view-type="remote"
     */
    public ResultsCompositeValue getResults(Integer caseId, Integer scheduledHearingId)
            throws ResultsControllerException {
        return Results2WorkFlow.getResults(caseId, scheduledHearingId);
    }
    
    /**
     * Creates a batch of results in XHIBIT and CREST
     * 
     * @param Integer
     *            caseId
     * @return ResultsCompositeValue an object containing ChargeCompositeValue
     *         and ArrayLists of pleas, verdicts, disposals.
     * @throws ResultsControllerException.
     *             When one or all Results cannot be created in XHIBIT or CREST.
     * @ejb.interface-method view-type="remote"
     */    
    public ResultsCompositeValue getResults(Integer caseId) throws ResultsControllerException {
    	return Results2WorkFlow.getResults(caseId, true);
    }

    /**
     * Creates a batch of results in XHIBIT and CREST
     * 
     * @param ResultValue
     *            an object containing ArrayLists of pleas, verdicts, disposals
     * @param Username
     * @throws ResultsControllerException.
     *             When one or all Results cannot be created in XHIBIT or CREST.
     * @ejb.interface-method view-type="both"
     */
    public void setPleas(ResultsSaveValue results, String username) throws ResultsControllerException { 
    	try {
    		// Update Xhibit
    		saveResults(results);
    		String caseType = null;
    		Integer caseId = null;
    		if (results.getResultSaveValueCount() > 0) {
    			for (int i=0;i < results.getResultSaveValueCount(); i++) {
    				if (results.getResultSaveValue(i) instanceof PleaSaveValue) {
			        	PleaSaveValue pleaSaveValue = (PleaSaveValue) results.getResultSaveValue(i);
			        	caseId = pleaSaveValue.getCourtLogCaseId();
			        	caseType = pleaSaveValue.getCourtLogCaseType();
    				}
    			}
	        }
    		// Update xhibit
	    	if (caseId != null) {
	    		getDartsHelper().updatePlea(
							caseId,
							caseType,
							username);
	    	}
	    	// Update Courtlog / Darts Messages
	        postResults(results, true);
    	} catch (Exception e) {
            log.error("Error in setPleas: "+e.getMessage());
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new ResultsControllerException(null, "Error updating plea retention policy", e);
		}
    }

    /**
     * Creates a batch of results in XHIBIT and CREST
     * 
     * @param ResultValue
     *            an object containing ArrayLists of pleas, verdicts, disposals
     * @param Username
     * @throws ResultsControllerException.
     *             When one or all Results cannot be created in XHIBIT or CREST.
     * @ejb.interface-method view-type="both"
     */
    public void setVerdicts(ResultsSaveValue results, String username) throws ResultsControllerException {
        try {
        	// Update Xhibit
        	saveResults(results);
        	String caseType = null;
    		Integer caseId = null;
    		if (results.getResultSaveValueCount() > 0) {
    			for (int i=0;i < results.getResultSaveValueCount(); i++) {
    				if (results.getResultSaveValue(i) instanceof VerdictSaveValue) {
		        		VerdictSaveValue verdictSaveValue = (VerdictSaveValue) results.getResultSaveValue(i);
		        		caseId = verdictSaveValue.getCourtLogCaseId();
			        	caseType = verdictSaveValue.getCourtLogCaseType();
    				}
    			}
    		}
		    // Update xhibit
	    	if (caseId != null) {
	    		getDartsHelper().updateVerdict(
							caseId,
							caseType,
							username);
	    	}
			// Update Courtlog / Darts Messages
			postResults(results, true);
        } catch (Exception e) {
            log.error("Error in setVerdicts: "+e.getMessage());
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new ResultsControllerException(null, "Error updating verdict retention policy", e);
		}
    }

    /**
     * Creates a batch of results in XHIBIT and CREST
     * 
     * @param ResultValue
     *            an object containing ArrayLists of pleas, verdicts, disposals
     * @throws ResultsControllerException.
     *             When one or all Results cannot be created in XHIBIT or CREST.
     * @ejb.interface-method view-type="both"
     */
    public void setDisposals(ResultsSaveValue results) throws ResultsControllerException {        
        XhbCase caseDetails = XhbCaseBeanHelper2.findByPrimaryKey(results.getCaseIds()[0]);
    	String caseListed = caseDetails.getCaseListed(); 	// can return NULL  
    	
    	try {
	    	if(caseListed != null && !caseListed.isEmpty()) { 
	        	boolean isCourtLogRequired = caseListed.equals("Y"); 
                saveResults(results);
                postResults(results, isCourtLogRequired);
	    	}
    	} catch (Exception e) {
            log.error("Error in setDisposals: "+e.getMessage());
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new ResultsControllerException(null, "Error updating disposal retention policy", e);
    	}
    }

    private void saveResults(ResultsSaveValue results) throws ResultsControllerException {
        try {
            Results2WorkFlow.saveResults(results);
        } catch (CrestSuccessRefreshResyncException ex) {
            ctx.setRollbackOnly(); // set roleback first in case
            // forceCaseResynch throws an exception
            debug("Success Resync and Rollback", ex);
            Results2WorkFlow.forceCaseResynch(results.getCaseIds());
            throw ex;
        } catch (CrestUnknownRefreshResyncException ex) {
            ctx.setRollbackOnly(); // set roleback first in case
            // forceCaseResynch throws an exception
            debug("Unknown Resync and Rollback", ex);
            Results2WorkFlow.forceCaseResynch(results.getCaseIds());
            throw ex;
        } catch (ResultsControllerException ex) {
            ctx.setRollbackOnly();
            debug("Unknown Rollback", ex);
            throw ex;
        }
    }
    
    private void postResults(ResultsSaveValue results, boolean isCourtLogRequired) throws ResultsControllerException {
        try {
            Results2WorkFlow.postResults(results, isCourtLogRequired);
        } catch (CrestSuccessRefreshResyncException ex) {
            ctx.setRollbackOnly(); // set roleback first in case
            // forceCaseResynch throws an exception
            debug("Success Resync and Rollback", ex);
            Results2WorkFlow.forceCaseResynch(results.getCaseIds());
            throw ex;
        } catch (CrestUnknownRefreshResyncException ex) {
            ctx.setRollbackOnly(); // set roleback first in case
            // forceCaseResynch throws an exception
            debug("Unknown Resync and Rollback", ex);
            Results2WorkFlow.forceCaseResynch(results.getCaseIds());
            throw ex;
        } catch (ResultsControllerException ex) {
            ctx.setRollbackOnly();
            debug("Unknown Rollback", ex);
            throw ex;
        }
    }

    /**
     * Returns the results authorisation state of the defendants on a case
     * 
     * @param Integer
     *            caseId case requiring authorisation
     * @return AuthorisationValue Defendants authorisation state
     * @throws ResultsControllerException.
     *             When one or all Results cannot be created in XHIBIT.
     * @ejb.interface-method view-type="remote"
     */
    public AuthorisationValue[] getAuthorisable(Integer caseId) throws ResultsControllerException {
        return AuthorisationWorkFlow.getAuthorisable(caseId);
    }
    
    /**
     * Returns the results authorisation state of the defendant on a case
     * 
     * @param Integer
     *            defOnCaseId defendant on case id requiring authorisation
     * @return AuthorisationValue Defendants authorisation state
     * @throws ResultsControllerException.
     *             When one or all Results cannot be created in XHIBIT.
     * @ejb.interface-method view-type="remote"
     */
    public AuthorisationValue[] getAuthorisableByDefendantOnCaseId(Integer defOnCaseId) throws ResultsControllerException {
    	return AuthorisationWorkFlow.getAuthorisableByDefendantOnCaseId(defOnCaseId);
    }
    
    /**
     * Returns cases to be authorised
     * 
     * @param Integer courtId
     * 
     * @return UnauthorisedCaseStatusValue[]
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public UnauthorisedCaseStatusValue[] getUnauthorisedCaseStatuses(Integer CourtId) throws ResultsControllerException {
        return AuthorisationWorkFlow.getUnauthorisedCaseStatusValues(CourtId);
    }  

    /**
     * Returns defendants with outstanding court of appeals
     * 
	 * @param Date formsSentDate
     * @param Integer courtId
     * 
     * @return DOCARValue[]
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public DOCARPrintValue getDOCARValues(Date formsSentDate, Integer CourtId) throws ResultsControllerException {
        return Results2WorkFlow.getDOCARValues(formsSentDate, CourtId);
    }  	
    
    /**
     * Returns List of Defendants Put Back
     * @param reportName 
	 *
     * @return ADJSSReportList
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public ADJSSReportList getListOfDefendantsPutBackReport(Integer courtId, String putBackType, String reportName) throws ResultsControllerException {
        return Results2WorkFlow.getListOfDefendantsPutBackReport(courtId, putBackType, reportName);
    } 
    

    /**
     * Returns NFIX - Notification of Fixtures
	 *
     * @return NFIXReport
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public NFIXReport getNFIXReport(Integer CourtId) throws ResultsControllerException {
        return Results2WorkFlow.getNFIXReport(CourtId);
    }
    
    /**
     * Returns CFIX - Cumulative Fixed Hearing List
	 *
     * @return CFIXReport
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    
    
    public CFIXReport getCFIXReport(Integer courtId,Date hearingFromDate,Date hearingEndDate)throws ResultsControllerException {
    	return Results2WorkFlow.getCFIXReport(courtId, hearingFromDate, hearingEndDate);
    }
    

    /**
     * Returns OUTC - Outstanding cases by various criteria
	 *
     * @return OUTCReport
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
     
    public OUTCReport getOUTCReport(Integer courtId,String caseType,String caseClass,String bcStatus,
    		String hearingTypeCode,Integer	timeEstFrom,Integer timeEstTo,Integer units,Integer refJudgeType,String judgeDescription,Integer unitsWeeks,
    		String SecureCourtRoom,	String juvenileOnly,String priorityNotes,String RestrictedNotes,String standardNotes,String sortBy)throws ResultsControllerException {
    	return Results2WorkFlow.getOUTCReport(courtId, caseType, caseClass, bcStatus, 
				hearingTypeCode,timeEstFrom,timeEstTo,units,refJudgeType,judgeDescription,unitsWeeks,
				SecureCourtRoom,juvenileOnly,priorityNotes,RestrictedNotes,standardNotes,
				sortBy);
    }
    
    /**
     * Returns UNLC - Unlisted cases by various criteria
	 *
     * @return UNLCReport
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
     
    public UNLCReport getUNLCReport(Integer courtId,String caseType,String caseClass,String bcStatus,
    		String hearingTypeCode,Integer	timeEstFrom,Integer timeEstTo,Integer units,Integer refJudgeType,String judgeDescription,Integer unitsWeeks,
    		String SecureCourtRoom,	String juvenileOnly,String priorityNotes,String RestrictedNotes,String standardNotes,String sortBy)throws ResultsControllerException {
    	return Results2WorkFlow.getUNLCReport(courtId, caseType, caseClass, bcStatus, 
				hearingTypeCode,timeEstFrom,timeEstTo,units,refJudgeType,judgeDescription,unitsWeeks,
				SecureCourtRoom,juvenileOnly,priorityNotes,RestrictedNotes,standardNotes,
				sortBy);
    }
    
    
    /**
     * Returns LFIX - List of Fixtures
	 *
     * @return LFIXReport
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public LFIXReport getLFIXReport(Integer CourtId, Date runDate) throws ResultsControllerException {
        return Results2WorkFlow.getLFIXReport(CourtId, runDate);
    }
    
    /**
     * Returns DARTS - Reconciliation Report
	 *
     * @return DARTSPrintValue
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public DARTSPrintValue getDARTSReport(Integer courtId, Date startDate, Date endDate) throws ResultsControllerException {
        return Results2WorkFlow.getDARTSReport(courtId, startDate, endDate);
    }
    
    /**
     * Returns DRSR - Missing Cracked/Effective Codes
	 *
     * @return DRSRReport
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public DRSRReport getDRSRReport(Integer CourtId, String monthPeriod,String yearPeriod) throws ResultsControllerException {
        return Results2WorkFlow.getDRSRReport(CourtId, monthPeriod,yearPeriod);
    }
    
    /**
     * Returns LOD - Listing Officers Diary
	 *
     * @return LODReport
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public LODReport getLODReport(Integer CourtId, Date diaryDate) throws ResultsControllerException {
        return Results2WorkFlow.getLODReport(CourtId, diaryDate);
    }
    
    /**
     * Returns LOD - Listing Officers Diary
	 *
     * @return LODBetweenDatesReport
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public LODReport getLODBetweenDatesReport(Integer CourtId, Date fromDate, Date toDate) throws ResultsControllerException {
    	return Results2WorkFlow.getLODBetweenDatesReport(CourtId, fromDate, toDate);
    }
    
    /**
     * Returns CTLRP - Trials Approaching Custody Time Limit
	 *
     * @return CTLRPReport
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public CTLRPReport getCTLRPReport(Integer CourtId, Date limitDate) throws ResultsControllerException {
    	return Results2WorkFlow.getCTLRPReport(CourtId, limitDate);
    }
    
    /**
     * Returns CTLRL - Custody Time Limit Reminder Letters
	 *
     * @return CTLRPReport
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public CTLRLReport getCTLRLReport(Integer CourtId, Date limitDate) throws ResultsControllerException {
    	return Results2WorkFlow.getCTLRLReport(CourtId, limitDate);
    }
    
    /**
     * Returns CTLRPEx - Trials Approaching Custody Time Limit Exceptions Report
	 *
     * @return CTLRPExReport
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public CTLRPExReport getCTLRPExReport(Integer CourtId) throws ResultsControllerException {
    	return Results2WorkFlow.getCTLRPExReport(CourtId);
    }
    
    /**
     * Returns RAGE - Cases Over 'N' Weeks Old
     * 
     * @return RAGEReport
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public RAGEReport getRAGEReport(Integer CourtId,String bcStatus,String classCode,Integer fromBetween,Integer toBetween) throws ResultsControllerException {
    	return Results2WorkFlow.getRAGEReport(CourtId, bcStatus, classCode, fromBetween, toBetween);
    }
    
    /**
     * Returns RREC - Cases Received and Disposed
     * 
     * @return RRECReport
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public RRECReport getRRECReportDetail(RRECReport rrecReport, Integer CourtId, Date endDate) throws ResultsControllerException {
    	return Results2WorkFlow.getRRECReportDetail(rrecReport, CourtId, endDate);
    }
    
    /**
     * Returns RREC - Cases Received and Disposed
     * 
     * @return RRECReport
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public RRECReport getRRECReportSummary(Integer CourtId, Date endDate) throws ResultsControllerException {
    	return Results2WorkFlow.getRRECReportSummary(CourtId, endDate);
    }
    
    /**
     * Returns LFIX - List of Run Date
	 *
     * @return LFIXRunDate
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public LFIXRunDate getReportRunDate(Integer courtId, String reportType) throws ResultsControllerException {
        return Results2WorkFlow.getReportRunDate(courtId, reportType);
    }
    
    /**
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public void updateLFIXCaseDiaryFixture(String list) throws ResultsControllerException {
        Results2WorkFlow.updateLFIXCaseDiaryFixture(list);
    }
    
    /**
     * @param String list
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public void updateCTLRLCaseReminderPrinted(String list) throws ResultsControllerException {
        Results2WorkFlow.updateCTLRLCaseReminderPrinted(list);
    }
    
    /**
     * Returns Running List
     * @param previousReportId 
	 * @param Integer courtId
	 *
     * @return PRLISReport
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public PRLISReport getPRLISReport(Integer courtId, Integer previousReportId) throws ResultsControllerException {
        return Results2WorkFlow.getPRLISReport(courtId, previousReportId);
    } 
    
    /**
     * Publishes the Running List attached
     * @param publishedCases 
	 * @param Integer courtId
	 * 
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public void publishRunningList(Integer courtId, Integer[] publishedCases) throws ResultsControllerException {
        Results2WorkFlow.publishRunningList(publishedCases, courtId);
    } 
    
    /**
     * Returns Previously Published Running Lists
	 *
	 * @param Integer courtId
	 *
     * @return XhbPubRunningList
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public XhbPubRunningListBasicValue[] getPreviouslyPublishedRunningLists(Integer courtId) throws ResultsControllerException {
        return XhbPubRunningListBeanHelper2.findPreviouslyPublishedRunningListsValue(courtId);
    } 
    

    /**
     * Returns RELCJ - Cases with required judge
	 *
     * @return RELCJReport
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public RELCJReport getRELCJReport(Integer CourtId) throws ResultsControllerException {
        return Results2WorkFlow.getRELCJReport(CourtId);
    }
    

    /**
     * Returns NHA - Notice of Hearing of Appeal
	 *
     * @return NHAReport
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public NHAReport getNHAReport(Integer CourtId) throws ResultsControllerException {
        return Results2WorkFlow.getNHAReport(CourtId);
    }
    
    /**
     * @param setNHALettersSentFlag 
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public void setLettersSentFlagOnCases(ISingleRunLetterReport report) throws ResultsControllerException {
        Results2WorkFlow.setLettersSentFlagOnCases(report);
    }

    /**
     * Returns pre-authorise check data
     * 
     * @param Integer caseId
     * 
     * @return AuthoriseCheckValue[]
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public AuthoriseWarning[] getAuthoriseWarnings(Integer caseId) 
    throws ResultsControllerException {
        return AuthorisationWorkFlow.getAuthoriseWarnings(caseId);
    }
    
    
    /**
     * Check whether the Record Sheet Preview XML has been generated
     * @param defendantOnCaseId
     * @return preview generation status
     * @throws ResultsControllerException
     *
     * @ejb.interface-method view-type="remote"
     */
    public String getRecordSheetPreviewGenerationStatus(Integer defendantOnCaseId) 
    	    throws ResultsControllerException {
        return AuthorisationWorkFlow.getRecordSheetPreviewGenerationStatus(defendantOnCaseId);
    }
    
    /**
     * Retrieve the Record Sheet Preview XML
     * @param defendantOnCaseId
     * @return Generated preview XML 
     * @throws ResultsControllerException
     *
     * @ejb.interface-method view-type="remote"
     */
    public String getGeneratedPreviewXML(Integer defendantOnCaseId) 
    	    throws ResultsControllerException {
        return AuthorisationWorkFlow.getGeneratedPreviewXML(defendantOnCaseId);
    }
    
    /**
     * Record Sheet Preview XML generation requested
     * @param defendantOnCaseId
     * @throws ResultsControllerException
     *
     * @ejb.interface-method view-type="remote"
     */
    public void requestRecordSheetPreviewGeneration(Integer defendantOnCaseId) 
    	    throws ResultsControllerException {
        AuthorisationWorkFlow.requestRecordSheetPreviewGeneration(defendantOnCaseId);
    }
    
    /**
     * Cancel Record Sheet Preview XML generation
     * @param defendantOnCaseId
     * @throws ResultsControllerException
     *
     * @ejb.interface-method view-type="remote"
     */
    public void cancelRecordSheetPreviewGeneration(Integer defendantOnCaseId) 
    	    throws ResultsControllerException {
        AuthorisationWorkFlow.cancelRecordSheetPreviewGeneration(defendantOnCaseId);
    }
    
    
    /**
     * Requests the authorisation of one or more defendants on a case
     * 
     * @param AuthorisationRequestValue
     *            request contains the list of defendants
     * @return AuthorisationReturnValue Containing the authorisation state of
     *         the case and the authorisation state of each defendant after the
     *         request has been processed. Failed authorisation will contain
     *         reasons
     * @throws ResultsControllerException.
     *             When one or all Results cannot be created in XHIBIT or CREST.
     * @ejb.interface-method view-type="remote"
     */
    public AuthorisationReturnValue authoriseResults(AuthorisationRequestValue request, Integer scheduledHearingId)
            throws ResultsControllerException {
        return AuthorisationWorkFlow.authoriseResults(request, scheduledHearingId);
    }

    /**
     * Requests the D20OffenceLink 
     * 
     * @param AuthorisationRequestValue, D20OffenceLinkHelperValue
     *            helper values related d20Offence 
     * @return D20OffenceLinkReturnValue Containing the D20OffenceLink 
     * @throws ResultsControllerException,DisposalControllerException, ChargeControllerException.
     *             
     * @ejb.interface-method view-type="remote"
     */
    public D20OffenceLinkReturnValue generateD20OffenceLink(AuthorisationRequestValue request,
            Integer scheduledHearingId, D20OffenceLinkHelperValue d20Helper)
            throws  ResultsControllerException, DisposalControllerException, ChargeControllerException {
         return AuthorisationWorkFlow.generateD20OffenceLink(request,scheduledHearingId,d20Helper);
    }

    /**
     * Get the list of offences for the defendant
     * 
     * @param 	defendantOnCaseId					The defendant on the case
     * 
     * @return	A List
     *             
     * @ejb.interface-method view-type="remote"
    */
    public List<D20OffenceLinkValue> getD20OffenceLinks(Integer defendantOnCaseId, boolean isD20Interim){
    	return AuthorisationWorkFlow.getD20OffenceLinks(defendantOnCaseId, isD20Interim);
    }
    
    /**
     * Return the XhbRefOffenceBasicValue by it's primary key
     * 
     * @param xhbRefOffenceId				The key
     * 
     * @return	The instance
     * 
     * @ejb.interface-method view-type="remote"
     */
    public XhbRefOffenceBasicValue getRefOffence( Integer xhbRefOffenceId ){
    	return AuthorisationWorkFlow.getRefOffence(xhbRefOffenceId);
    }
    
    /**
     * Validate the D20 Order 
     * 
     * @param  AuthorisationRequestValue
     *         request contains the list of defendants
     * @return AuthorisationReturnValue. Failed authorisation will contain reasons
     * @throws ResultsControllerException.
     *         When one or all Results cannot be created in XHIBIT or CREST.
     * @ejb.interface-method view-type="remote"
     */
    public AuthorisationReturnValue validateD20Order(AuthorisationRequestValue request, Integer scheduledHearingId)
            throws ResultsControllerException {
        return AuthorisationWorkFlow.validateD20Order(request, scheduledHearingId);
    }


    
    /**
     * Authorise the sentence outcome
     * 
     * @param 	request
     * @param	scheduleHearingId
     * 
     * @return	Boolean (another thing)
     * 
     * @throws ResultsControllerException Maybe.
     * @ejb.interface-method view-type="remote"
     */
    public AuthorisationReturnValue authoriseSentenceOutcome( AuthorisationRequestValue request, Integer scheduleHearingId ) throws ResultsControllerException {
    	return AuthorisationWorkFlow.authoriseSentenceOutcome( request, scheduleHearingId );
    }
    
    /**
     * Gets the ChargeCompositeValue for a case
     * 
     * @param Integer caseId
     * @return ChargeCompositeValue for the supplied case
     * @throws ResultsControllerException.
     * 
     * @ejb.interface-method view-type="remote"
     */
    public ChargeCompositeValue getCharges(Integer caseId) throws ResultsControllerException{
        return Results2WorkFlow.getResults(caseId).getChargeCompositeValue();
    }
    
    /**
     * Returns defendants with outstanding court of appeals
     * 
	 * @param Date formsSentDate
     * @param Integer courtId
     * 
     * @return DOCARValue[]
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public OBWPrintValue getOBWValues(Date bwIssueDate, Integer courtId) throws ResultsControllerException {
        return Results2WorkFlow.getOBWValues(bwIssueDate, courtId);
    }
    
    
    /**
     * Returns INFTRPC - Cracked/Effective Trial Cases
	 *
     * @return INFTRPCReport
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public INFTRPCReport getINFTRPCReport(Integer CourtId, String monthPeriod,String yearPeriod) throws ResultsControllerException {
        return Results2WorkFlow.getINFTRPCReport(CourtId, monthPeriod,yearPeriod);
    }
    
    
    
    
    /**
     * Returns INFTRPC - Cracked/Effective Trial Cases
	 *
     * @return INFTRPCCaseNumReport
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public INFTRPCCaseNumReport getINFTRPCCaseNumReport(Integer CourtId, String monthPeriod,String yearPeriod) throws ResultsControllerException {
        return Results2WorkFlow.getINFTRPCCaseNumReport(CourtId, monthPeriod,yearPeriod);
    }
    
    
    
    /**
     * Returns NTRSF - Notification of Case Transfer
	 *
     * @return NTRSFReport
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public NTRSFReport getNTRSFReport(Integer CourtId, Integer caseId) throws ResultsControllerException {
    	return Results2WorkFlow.getNTRSFReport(CourtId, caseId);
    }
    
    /**
     * Returns RUMO - Unacknowledged Monetray Orders
	 *
     * @return RUMOReport
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public RUMOReport getRUMOReport(Integer courtId) throws ResultsControllerException {
        return Results2WorkFlow.getRUMOReport(courtId);
    }
    
    /**
     * Returns RRCA - Outstanding Trial Cases By Age
	 *
     * @return RRCAReport
     * @throws ResultsControllerExceptionS
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public RRCAReport getRRCASummaryReport(Integer CourtId, Date endDate) throws ResultsControllerException {
    	return Results2WorkFlow.getRRCASummaryReport(CourtId, endDate);
    }
    
    /**
     * Returns RRCA - Outstanding Trial Cases By Age
	 *
     * @return RRCAReport
     * @throws ResultsControllerExceptionS
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public RRCAReport getRRCADetailReport(RRCAReport report, Integer CourtId, Date endDate) throws ResultsControllerException {
    	return Results2WorkFlow.getRRCADetailReport(report,CourtId, endDate);
    }
    
    
    /**
     * Returns RSIT - Courtroom Sitting Times
	 *
     * @return RSITReport
     * @throws ResultsControllerExceptionS
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public RSITReport getRSITReport(Integer CourtId, Integer CourtSiteId, Date endDate) throws ResultsControllerException {
    	return Results2WorkFlow.getRSITReport(CourtId, CourtSiteId, endDate);
    }
    
    
    /**
     * Returns Collection - Court Sites
	 *
     * @return Collection
     * @throws ResultsControllerExceptionS
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    @SuppressWarnings("unchecked")
	public Collection getCourtSites(Integer courtId) throws ResultsControllerException {
    	ArrayList<XhbCourtSite> siteInfo = (ArrayList<XhbCourtSite>)XhbCourtSiteBeanHelper2.findByCourtId(courtId);
        Collection<StringBuffer> rtnSiteList = new ArrayList<StringBuffer>();
        
        Collections.sort(siteInfo, new Comparator<XhbCourtSite>() {
            @Override
            public int compare(XhbCourtSite left, XhbCourtSite right) {
                return left.getCourtSiteCode().compareTo(right.getCourtSiteCode()); 
            }
        });
       
        for (Iterator it = siteInfo.iterator(); it.hasNext();) {
            XhbCourtSite site = (XhbCourtSite) it.next();
            if (!"Y".equals(site.getObsInd())) {
            	StringBuffer detail = new StringBuffer();
            	detail.append(site.getCourtSiteId());    
            	detail.append("**");
            	detail.append(site.getCourtSiteName());
            	log.debug("getSiteInfo :: " + detail.toString());
            	rtnSiteList.add(detail);
            }
        }
        return rtnSiteList;
    }
    
    private void debug(String message, ResultsControllerException root) {
        if (log.isDebugEnabled()) {
            log.debug(message + " ...");

            log.debug("... root exception.", root);
            int number = 0;
            for (Throwable cause = root.getCause(); cause != null; cause = cause instanceof CSException ? ((CSException) cause)
                    .getCause()
                    : null) {
                log.debug("... causal exception " + (number++) + ".", cause);
            }
        }
    }
    
     /**
     * Returns RJS - Report of Judge Sittings
	 *
     * @return RJSReport
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    
    public RJSReport getRJSReport(final Integer courtId, final Date sittingDate) throws ResultsControllerException {
        return Results2WorkFlow.getRJSReport(courtId, sittingDate);
    }
    
    /**
     * Returns OGRDA - Order Granting Representation for Defendants and Appellants
	 *
     * @return OGRDAOrder
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public OGRDAOrder getOGRDAOrder(final Integer courtId, final Integer legalAidOrderId) throws ResultsControllerException {
        return Results2WorkFlow.getOGRDAOrder(courtId, legalAidOrderId);
    }
    
    /**
     * Returns OGRR - Order Granting Representation for Respondents
	 *
     * @return OGRROrder
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public OGRROrder getOGRROrder(final Integer legalAidOrderId) throws ResultsControllerException {
        return Results2WorkFlow.getOGRROrder(legalAidOrderId);
    }
    
    
    /**
     * Returns OARDA - Order Amending Representation for Defendants/Appellants
	 *
     * @return OARDAPrintInformation
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public OARDAPrintInformation getOARDAPrintInformation(final Integer courtId, final Integer legalAidAmendmentId) throws ResultsControllerException {
        return Results2WorkFlow.getOARDAPrintInformation(courtId, legalAidAmendmentId);
    }
    
    /**
     * Returns OARR - Order Amending Representation for Respondents
	 *
     * @return OARRPrintInformation
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public OARRPrintInformation getOARRPrintInformation(final Integer courtId, final Integer legalAidAmendmentId) throws ResultsControllerException {
        return Results2WorkFlow.getOARRPrintInformation(courtId, legalAidAmendmentId);
    }
    
    /**
     * Returns OWRDA - Order Revoking Representation for Defendants/Appellants
	 *
     * @return OWRDAPrintInformation
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public OWRDAPrintInformation getOWRDAPrintInformation(final Integer courtId, final Integer legalAidId) throws ResultsControllerException {
        return Results2WorkFlow.getOWRDAPrintInformation(courtId, legalAidId);
    }
    
    
    /**
     * Returns OWRR - Order Revoking Representation for Respondents
	 *
     * @return OWRRPrintInformation
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public OWRRPrintInformation getOWRRPrintInformation(final Integer courtId, final Integer legalAidId) throws ResultsControllerException {
        return Results2WorkFlow.getOWRRPrintInformation(courtId, legalAidId);
    }
    
	 /**
     * Returns Appeal Result Order information
	 *
     * @return String
     * @throws ResultsControllerException
     * 
     * @ejb.interface-method view-type="remote"
     */
    public String getAROInformation(final Integer caseId) throws ResultsControllerException {
    	return Results2WorkFlow.getAROInformation(caseId);
    }
    
    
    /**
     * Using an APP_RESULT_ID (generally from XHB_VERDICT)
     * Based on this get the XHB_REF_APP_RESULT.APP_RESULT_CODE value and use that to determine if the XHB_REF_APP_RES_D20_MAP.D20_RESULT='Varied'
     * 
     * @param String
     *            a String object with a RefAppResultCode
     * @throws ResultsControllerException.
     *             When one or all Results cannot be created in XHIBIT.
     * @ejb.interface-method view-type="both"
     */
    public Boolean isThisAppResultVariedForD20(final String refAppResultCode) {
    	
    	Boolean retValue = false;
    	
    	if (refAppResultCode != null) {
    		Collection refAppResD20MapResults = XhbRefAppResD20MapBeanHelper2.findByAppResultCode(refAppResultCode);
    		Iterator it = refAppResD20MapResults.iterator();
    		// Expect only one entry to be returned
    		if (it.hasNext()) {
    			XhbRefAppResD20Map refAppResD20Map = (XhbRefAppResD20Map) it.next();
    			
	    		if ((refAppResD20Map.getD20Result() != null) && (refAppResD20Map.getD20Result().toLowerCase().equals("varied"))) {
	    			retValue = true;
	    		}
	    	}
    	}
    	
    	return retValue; 
    }
    
    /**
     * Check that the disposal is a driving type disposal
     * 
     * @param 	disposalTypeId				The disposal type id
     * @param 	courtId						The court id
     * 
     * @return	true if the disposal is a driving type
     * 
     * @ejb.interface-method view-type="both"
     */
    public Boolean isDrivingDisqualDisposal( Integer disposalTypeId, Integer courtId)
    {
    	Boolean outcome = ( disposalTypeId != null && courtId != null );
    	
    	if ( outcome ){
    		//	We can get away with no "D20OffenceLinkHelperValue" and no "AuthorisationHelper" as the calls we use don't use them. (I know, I checked)
    		//	In fact, you could just lift the few lines of code and call that direct from here. That would introduce code duplication and all 
    		//	the ills that that throws up!
    		D20OffenceValidator validator = new D20OffenceValidator(null, null);
    		Integer number = validator.getParentRefDisposalId(disposalTypeId, courtId);
    		
    		//	The function can return a null Integer. We can also assume that a null return value is a failure.
    		outcome = number != null;
    		
    		if ( outcome ){
    			outcome = AuthorisationWorkFlow.isDrivingDisposal( number );
    		}
    	}
    	
    	return outcome;
    }
    
    
    /**
     * Check that the offence has a DISTOT
     * 
     * @param 	defOnOffenceId
     * @param 	defendantId
     * @param	caseId
     * 
     * @return	true if the offence has a DISTOT
     * 
     * @ejb.interface-method view-type="both"
     */
    public Boolean checkForDISTOT(Integer defOnOffenceId, Integer defendantId, Integer caseId) {
    	return new D20OffenceValidator().checkForDISTOT(defOnOffenceId, defendantId, caseId);
    }
 
    private DartsHelper getDartsHelper() {
    	if (dartsHelper == null) {
    		dartsHelper = new DartsHelper();
    	}
    	return dartsHelper;
    }
}