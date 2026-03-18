package uk.gov.courtservice.xhibit.business.services.results;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.exception.Message;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.results.LegalAidOrderDatabaseManager;
import uk.gov.courtservice.xhibit.business.database.results.OARDAPrintInformation;
import uk.gov.courtservice.xhibit.business.database.results.OWRRPrintInformation;
import uk.gov.courtservice.xhibit.business.database.results.ReportDatabaseManager;
import uk.gov.courtservice.xhibit.business.database.results.ResultsDatabase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_hearing.XhbHearingBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_app_res_d20_map.XhbRefAppResD20Map;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_app_res_d20_map.XhbRefAppResD20MapBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_app_result.XhbRefAppResult;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_app_result.XhbRefAppResultBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearingBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdict;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdictBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.caze.CaseAccessException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerLocal;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerLocalHome;
import uk.gov.courtservice.xhibit.business.services.caze.CaseRetrievalIntControllerException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerLocal;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerLocalHome;
import uk.gov.courtservice.xhibit.business.services.results.saver.IndictmentResultsGenerator;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DefendantOnOffenceMoveValue;
import uk.gov.courtservice.xhibit.common.progress.caseupdate.ProgressTriggerMaintainer;
import uk.gov.courtservice.xhibit.common.results.vos.ADJSSReportList;
import uk.gov.courtservice.xhibit.common.results.vos.CFIXReport;
import uk.gov.courtservice.xhibit.common.results.vos.CTLRLReport;
import uk.gov.courtservice.xhibit.common.results.vos.CTLRPExReport;
import uk.gov.courtservice.xhibit.common.results.vos.CTLRPReport;
import uk.gov.courtservice.xhibit.common.results.vos.DARTSPrintValue;
import uk.gov.courtservice.xhibit.common.results.vos.DOCARPrintValue;
import uk.gov.courtservice.xhibit.common.results.vos.DRSRReport;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;
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
import uk.gov.courtservice.xhibit.common.results.vos.RAGEReport;
import uk.gov.courtservice.xhibit.common.results.vos.RELCJReport;
import uk.gov.courtservice.xhibit.common.results.vos.RJSReport;
import uk.gov.courtservice.xhibit.common.results.vos.RRCAReport;
import uk.gov.courtservice.xhibit.common.results.vos.RRECReport;
import uk.gov.courtservice.xhibit.common.results.vos.RSITReport;
import uk.gov.courtservice.xhibit.common.results.vos.RUMOReport;
import uk.gov.courtservice.xhibit.common.results.vos.ResultSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.UNLCReport;
import uk.gov.courtservice.xhibit.common.results.vos.common.OARRPrintInformation;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.OutputTransformationException;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.TransformationException;
import uk.gov.courtservice.xhibit.integration.services.IntegrationFacadeFactory;
import uk.gov.courtservice.xhibit.integration.services.MercatorException;

/**
 * <p>
 * Title: Results2WorkFlow
 * </p>
 * <p>
 * Description: Static class providing implementation
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bal Bhamra, Will Fardell, Abdul Hussain
 * @version $Revisison$
 */
public class Results2WorkFlow {
    private static final Logger log = CSServices.getLogger(Results2WorkFlow.class);

    private static final ChargeControllerLocal chargeController = (ChargeControllerLocal) CSServices.getEJBServices()
            .createLocalSession(ChargeControllerLocalHome.class);

    private static final CaseControllerLocal caseController = (CaseControllerLocal) CSServices.getEJBServices()
            .createLocalSession(CaseControllerLocalHome.class);

    /**
     * Stop unnecisary construction of this class
     */
    private Results2WorkFlow() {
    }

    /**
     * Get the results reference data for the specified court and result type
     * 
     * @param courtID
     *            the id of the court.
     * @return the results reference data for the specified result type
     * @throws ResultsControllerException
     *             if an error occures
     */
    public static ResultsReferenceValue getReference(Integer courtId) throws ResultsControllerException {
        if (log.isDebugEnabled()) {
            log.debug("getReference(" + courtId + ")");
        }

        ResultsReferenceValue reference = new ResultsReferenceValue(courtId);
        ReferencePopulater[] referencePopulators = ResultsPopulaterFactory.getInstance().getReferencePopulaters();
        for (int i = 0; i < referencePopulators.length; i++) {
            referencePopulators[i].populate(reference);
        }

        if (log.isDebugEnabled()) {
            log.debug("getReference(" + courtId + ") = " + reference);
        }
        return reference;
    }

    /**
     * Gets the disposal reference details for the specified reference disposal
     * type id
     * 
     * @param refDisposalTypeId
     *            the id of the disposal reference details.
     * @return DisposalReferenceValue the results reference data for the
     *         specified result type id.
     */
    public static DisposalReferenceValue getReferenceDisposal(Integer refDisposalTypeId) {
        return ResultsDatabase.getReferenceDisposal(refDisposalTypeId);
    }

    /**
     * Creates a batch of results in XHIBIT and CREST
     * 
     * @param caseId
     *            the case id.
     * @return ResultsCompositeValue an object containing ChargeCompositeValue
     *         and ArrayLists of pleas, verdicts, disposals (depending on
     *         resultType parameter.
     * @throws ResultsControllerException
     *             When one or all Results cannot be created in XHIBIT or CREST.
     */
    public static ResultsCompositeValue getResults(Integer caseId) throws ResultsControllerException {
        return getResults(caseId, null);
    }

    /**
     * Gets a batch of results from XHIBIT
     * 
     * @param Integer
     *            caseId
     * @param String
     *            scheduledHearingId
     * @return ResultsCompositeValue an object containing ChargeCompositeValue
     *         and ArrayLists of pleas, verdicts, disposals (depending on
     *         resultType parameter.
     * @throws ResultsControllerException.
     *             When one or all Results cannot be created in XHIBIT or CREST.
     */
    public static ResultsCompositeValue getResults(Integer caseId, Integer scheduledHearingId)
            throws ResultsControllerException {
        if (log.isDebugEnabled()) {
            log.debug("getResults(" + caseId + ")");
        }

        ResultsCompositeValue results = new ResultsCompositeValue();

        // get and set chargeCompositeValue
        log.debug("getting charge composite for case id = " + caseId);
        results.setChargeCompositeValue(getCharges(caseId));
        results.setScheduledHearingId(scheduledHearingId);

        // Loop through and populate all results information
        log.debug("Looping through and populating all results information");
        ResultsPopulater[] resultsPopulators = ResultsPopulaterFactory.getInstance().getResultsPopulaters();
        for (int i = 0; i < resultsPopulators.length; i++) {
            resultsPopulators[i].populate(results);
        }

        if (log.isDebugEnabled()) {
            log.debug("getResults(" + caseId + ") = " + results);
        }
        return results;
    }
    
    /**
     * Gets a batch of results from XHIBIT but ignores hearings
     * 
     * @param Integer
     *            caseId
     * @param boolean
     *            overload
     * @return ResultsCompositeValue an object containing ChargeCompositeValue
     *         and ArrayLists of pleas, verdicts, disposals (depending on
     *         resultType parameter).
     * @throws ResultsControllerException.
     *             When one or all Results cannot be created in XHIBIT or CREST.
     */
    public static ResultsCompositeValue getResults(Integer caseId, boolean overload) 
    		throws ResultsControllerException {
    	ResultsCompositeValue results = new ResultsCompositeValue();
    	
    	results.setChargeCompositeValue(getCharges(caseId));
    	
    	ResultsPopulater[] resultsPopulators = ResultsPopulaterFactory.getInstance().getResultsPopulaters();
        for (int i = 0; i < resultsPopulators.length; i++) {
            resultsPopulators[i].populate(results);
        }

    	return results;
    }

    /**
     * Gets the charges for the given case id, but does not get the charge
     * amendment log as this is not required for results screens.
     * 
     * @param caseId
     *            ID of the case.
     * @throws ResultsControllerException.
     *             When we cannot retrieve the charges.
     */
    private static ChargeCompositeValue getCharges(Integer caseId) throws ResultsControllerException {
        try {
            // Get the charges for the given case id. Don't get the charge
            // log.
            return chargeController.getCharges(caseId, false);
        } catch (ChargeControllerException e) {
            throw createResultsControllerException(e);
        }
    }

    /**
     * Creates a batch of results in XHIBIT and CREST
     * 
     * @param ResultValue
     *            an object containing ArrayLists of pleas, verdicts, disposals
     * @param  isCRESTExportRequired
     *            boolean as to whether an export to CREST is required 
     * @throws ResultsControllerException.
     *             When one or all Results cannot be created in XHIBIT or CREST.
     */
    public static void saveResults(ResultsSaveValue results) throws ResultsControllerException {
        try {
            if (log.isDebugEnabled()) {
                results.recordVoEditedDump();
                log.debug(results.getVoEditedDump());
            }

            preprocess(results);
            if (log.isDebugEnabled()) {
                results.recordVoPreprocessedDump();
                log.debug(results.getVoPreprocessedDump());
            }

            save(results);
            if (log.isDebugEnabled()) {
                results.recordVoSavedDump();
                log.debug(results.getVoSavedDump());
            }
        } catch (ResultsControllerException rce) {
            rce.setResultsDebugValue(results.getResultsDebugValue());
            throw rce;
        }
    }
    
    public static void postResults(ResultsSaveValue results, boolean isCourtlogRequired) throws ResultsControllerException {
    	try {
    		if (isCourtlogRequired) {
    			addCourtLog(results);
    		}
	        // Update the Progress Trigger table with the case ids
	        ProgressTriggerMaintainer.updateCaseTrigger(results.getCaseIds());
	    } catch (ResultsControllerException rce) {
	        rce.setResultsDebugValue(results.getResultsDebugValue());
	        throw rce;
	    }
    }

    public static void moveResults(DefendantOnOffenceMoveValue[] dofMoveValues) throws ResultsControllerException {
        log.debug("moveResults(DefendantOnOffenceMoveValue[] dofMoveValues) - START ");

        if (dofMoveValues != null && dofMoveValues.length > 0) {
            Integer courtId = getCourtId(dofMoveValues[0].getOldDefendantOnOffenceId());
            ResultsSaveValue results = new ResultsSaveValue(courtId);
            log.debug("Number of Defendant On Offences to process:" + dofMoveValues.length);
            for (int i = 0, len = dofMoveValues.length; i < len; i++) {
                IndictmentResultsGenerator.setDefendantOnOffenceResults(results, dofMoveValues[i]
                        .getOldDefendantOnOffenceId(), dofMoveValues[i].getNewDefendantOnOffenceId());
                log.debug("Iteration " + i + ", oldDefendantOnOffenceId="
                        + dofMoveValues[i].getOldDefendantOnOffenceId() + " No of results: "
                        + results.getResultSaveValueCount());
            }
            if (results.getResultSaveValueCount() > 0) {
                results.sortResultSaveValueForMove();
                saveResults(results);
                postResults(results, true);
            }
        }
        log.debug("moveResults(DefendantOnOffenceMoveValue[] dofMoveValues) - END ");

    }

    private static void preprocess(ResultsSaveValue results) throws XhibitFailureException {
        try {
            // Can NOT cache count as count can grow as preprocess may
            // (does) add new save values
            for (int i = 0; i < results.getResultSaveValueCount(); i++) {
                ResultSaveValue result = results.getResultSaveValue(i);
                ResultsSaverFactory.getInstance().getResultsSaver(result.getClass()).preprocess(results, i);
            }
        } catch (ResultsControllerException ex) {
            throw new XhibitFailureException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
        }

    }

    private static void save(ResultsSaveValue results) throws XhibitFailureException {
        try {
            for (int i = 0, c = results.getResultSaveValueCount(); i < c; i++) {
                ResultSaveValue result = results.getResultSaveValue(i);
                ResultsSaverFactory.getInstance().getResultsSaver(result.getClass()).save(results, i);
            }
        } catch (ResultsControllerException ex) {
            throw new XhibitFailureException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
        }
    }
    

    private static void addCourtLog(ResultsSaveValue results) throws CrestSuccessRefreshResyncException {
        try {
            for (int i = 0, c = results.getResultSaveValueCount(); i < c; i++) {
                ResultSaveValue result = results.getResultSaveValue(i);

                ResultsSaver saver = ResultsSaverFactory.getInstance().getResultsSaver(result.getClass());
                saver.saveCrestKeys(results, i);
                resetResultsVerified(results);
                if (result.isCourtLogged()) {
                    saver.log(results, i);
                }
            }
        } catch (ResultsControllerException ex) {
            throw new CrestSuccessRefreshResyncException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
        }
    }

    /**
     * @description exports results to CREST
     * @param results
     *            as sent by mid tier
     * @return ResultsSaveValue as modified by Mercator (crest keys,error codes)
     * @throws CrestSuccessRefreshResyncException
     *             If error occurred after mercator returned successfully
     * @throws CrestUnknownRefreshResyncException
     *             If mercator/crest failed
     * @throws XhibitFailureException
     *             If xhibit failed before calling mercator
     */
    private static ResultsSaveValue export(ResultsSaveValue results) throws CrestSuccessRefreshResyncException,
            CrestUnknownRefreshResyncException, XhibitFailureException

    {
        try {
            // set results in CREST using integration tier and Mercator
            return IntegrationFacadeFactory.getInstance().getIntegrationFacade().setResults(results);
        } catch (OutputTransformationException ex) {
            throw new CrestSuccessRefreshResyncException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
        } catch (TransformationException ex) {
            throw new XhibitFailureException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
        } catch (MercatorException ex) {
            throw new CrestUnknownRefreshResyncException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
        } catch (CSUnrecoverableException ex) {
            throw new CrestUnknownRefreshResyncException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
        }
    }

    /**
     * @description checks return code for each plea, verdict, disposal etc that
     *              was sent to mercator if any is not zero, generate a user
     *              message based on the return code and mvo data type
     * @param results
     * @throws XhibitFailureException
     *             if crest/mercator rejected any record.
     */
    private static void examineReturnCodes(ResultsSaveValue results) throws XhibitFailureException {

        XhibitFailureException xhibitFailureException = null;

        for (int i = 0, c = results.getResultSaveValueCount(); i < c; i++) {
            ResultSaveValue result = results.getResultSaveValue(i);
            Integer rc = result.getReturnCode();
            if (rc == null || rc.intValue() != 0) {
                if (xhibitFailureException == null) {
                    xhibitFailureException = new XhibitFailureException();
                }
                xhibitFailureException.addMessage(new Message(result.getMessageKey(), result.getMessageParameters()));
            }
        }

        if (xhibitFailureException != null) {
            throw xhibitFailureException;
        }
    }

    /**
     * Utility method to deal with handling Exceptions from other controllers,
     * and wrapping and rethrowing them as ResultsControllerException
     * 
     * @param ex
     *            The Exception to handle and rethrow, must be a
     *            <code>CSRecoverableException</code> or child of
     * @return ResultsControllerException the new Exception to throw
     */
    private static ResultsControllerException createResultsControllerException(CSRecoverableException ex) {
        CSServices.getDefaultErrorHandler().handleError(ex, Results2WorkFlow.class);
        if (ex.getUserMessageAsMessage().getParameters().length > 0) {
            return new ResultsControllerException(ex.getUserMessageAsMessage().getKey(), ex.getUserMessageAsMessage()
                    .getParameters(), ex.getMessage(), ex);
        } else {
            return new ResultsControllerException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
        }
    }

    /**
     * @description Calls forceCaseResynch on case controller bean in. This
     *              method starts a new transaction.
     * @param caseIds
     * @throws RefreshResyncFailureException
     */
    public static void forceCaseResynch(Integer[] caseIds) throws RefreshResyncFailureException {
        try {
            if (log.isDebugEnabled()) {
                log.debug("forceCaseResynch(" + valueOf(caseIds) + ")");
            }
            caseController.forceCaseResynch(caseIds);
        } catch (CaseRetrievalIntControllerException ex) {
            throw new RefreshResyncFailureException("Result2Workflow.ResynchFailure", "Cases with caseIDs "
                    + valueOf(caseIds) + " could not be synchronized.  Please try again later.", ex);
        } catch (CaseAccessException ex) {
            throw new RefreshResyncFailureException("Result2Workflow.ResynchFailure", "Cases with caseIDs "
                    + valueOf(caseIds) + " could not be synchronized.  Please try again later.", ex);
        }

    }

    private static void resetResultsVerified(ResultsSaveValue results) {
        resetResultsVerifiedDefendant(results.getDefendantOnCaseIds());
        resetResultsVerifiedCase(results.getCaseIds());
    }

    private static void resetResultsVerifiedDefendant(Integer[] defendantOnCaseIds) {
        log.debug("resetResultsVerifiedDefendant - defendantOnCase.length: " + defendantOnCaseIds.length);

        for (int i = 0, len = defendantOnCaseIds.length; i < len; i++) {
            /**
             * @todo Below cjeck is required beacuse currently
             *       defendantOnCaseIds are null. Will these need to be
             *       populated??
             */
            log.debug("resetResultsVerifiedDefendant - defendantOnCaseIds[" + i + "]: " + defendantOnCaseIds[i]);
            if (defendantOnCaseIds[i] != null) {
                XhbDefendantOnCaseBasicValue docValue = XhbDefendantOnCaseBeanHelper2
                        .findByPrimaryKeyValue(defendantOnCaseIds[i]);
                docValue.setResultsVerified("N");
                XhbDefendantOnCaseBeanHelper2.update(docValue);
            }
        }
    }

    private static void resetResultsVerifiedCase(Integer[] caseIds) {
        log.debug("resetResultsVerifiedCase - caseIds.length: " + caseIds.length);

        for (int i = 0, len = caseIds.length; i < len; i++) {
            /**
             * @todo Below check is required because currently caseIds are null.
             *       Will these need to be populated??
             */
            log.debug("resetResultsVerifiedCase - caseIds[" + i + "]: " + caseIds[i]);
            if (caseIds[i] != null) {
                XhbCaseBasicValue caseValue = XhbCaseBeanHelper2.findByPrimaryKeyValue(caseIds[i]);
                caseValue.setResultsVerified("N");
                XhbCaseBeanHelper2.update(caseValue);
            }
        }
    }

    private static String valueOf(Integer[] array) {
        if (array != null) {
            if (0 < array.length) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(array[0]);
                for (int i = 1; i < array.length; i++) {
                    buffer.append(", ");
                    buffer.append(array[i]);
                }
                return buffer.toString();
            } else {
                return "{}";
            }
        } else {
            return "null";
        }
    }

    private static Integer getCourtId(Integer defendantOnOffenceId) {
        XhbCaseBasicValue[] caseBVs = XhbCaseBeanHelper2.findByDefendantOnOffenceIdValue(defendantOnOffenceId);
        if (0 < caseBVs.length) {
            return caseBVs[0].getCourtId();
        } else {
            throw new IllegalArgumentException("defendantOnOffence has no corresponding case");
        }
    }

    public static XhbHearingBasicValue getHearing(Integer scheduledHearingId) {
        // HearingEndWorkflow hearingEndWorkflow = new HearingEndWorkflow();
        // hearingEndWorkflow.reCalculateHearingDuration(scheduledHearingId,
        // defendantId);
        return XhbScheduledHearingBeanHelper2.findByPrimaryKey(scheduledHearingId).getXhbHearingData();
    }
	
	public static DOCARPrintValue getDOCARValues(Date formsSentDate, Integer courtId) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getDOCARValues(formsSentDate, courtId);
	}


	public static ADJSSReportList  getListOfDefendantsPutBackReport(Integer courtId, String putBackType, String reportName) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getListOfDefendantsPutBackReport(courtId, putBackType, reportName);
	}

	public static NFIXReport getNFIXReport(Integer courtId) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getNFIXReport(courtId);
	}
	
	public static CFIXReport getCFIXReport( Integer courtId,Date hearingFromDate,Date hearingEndDate ) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getCFIXReport(courtId,hearingFromDate,hearingEndDate );
	}
	
	
	public static PRLISReport getPRLISReport(Integer courtId, Integer previousReportId) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getPRLISReport(courtId, previousReportId);
	}
	
	public static void publishRunningList(Integer[] publishedCases, Integer courtId) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		dbMan.publishRunningList(publishedCases, courtId);
	}
	
	public static LODReport getLODReport(Integer courtId, Date diaryDate) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getLODReport(courtId, diaryDate);
	}
	
	public static LODReport getLODBetweenDatesReport(Integer courtId, Date fromDate, Date toDate) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getLODBetweenDatesReport(courtId, fromDate, toDate);
	}
	
	public static NHAReport getNHAReport(Integer courtId) {		
		ReportDatabaseManager dbMan = new ReportDatabaseManager();		
		return dbMan.getNHAReport(courtId);	
	}
	
	public static void setLettersSentFlagOnCases(ISingleRunLetterReport report) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();		
		dbMan.setLettersSentFlagOnCases(report);	
		
	}

	public static LFIXReport getLFIXReport(Integer courtId, Date runDate) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getLFIXReport(courtId, runDate);
	}
	
	public static LFIXRunDate getReportRunDate(Integer courtId, String reportType) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getReportRunDate(courtId, reportType);
	}
	
	public static void updateLFIXCaseDiaryFixture(String list) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		dbMan.updateLFIXCaseDiaryFixture(list);
	}
	
	public static DARTSPrintValue getDARTSReport(Integer courtId, Date startDate, Date endDate) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getDARTSReport(courtId, startDate, endDate);
	}
	
	public static void updateCTLRLCaseReminderPrinted(String list) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		dbMan.updateCTLRLCaseReminderPrinted(list);
	}
	
	public static CTLRPReport getCTLRPReport(Integer courtId, Date limitDate) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getCTLRPReport(courtId, limitDate);
	}
	
	public static CTLRPExReport getCTLRPExReport(Integer courtId) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getCTLRPExReport(courtId);
	}

	public static RAGEReport getRAGEReport(Integer CourtId, String bcStatus, String classCode, Integer fromBetween, Integer toBetween) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getRAGEPReport(CourtId, bcStatus, classCode, fromBetween, toBetween);
	}
	
	public static RRECReport getRRECReportDetail(RRECReport rrecReport, Integer CourtId, Date endDate) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getRRECDetailReport(rrecReport, CourtId, endDate);
	}
	
	public static RRECReport getRRECReportSummary(Integer CourtId, Date endDate) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getRRECSummaryReport(CourtId, endDate);
	}

	public static OUTCReport getOUTCReport(Integer courtId,String caseType,String caseClass,String bcStatus,		
			String hearingTypeCode,Integer	timeEstFrom,Integer timeEstTo,Integer units,Integer refJudgeType,String judgeDescription,Integer unitsWeeks,		
			String SecureCourtRoom,String juvenileOnly,String priorityNotes,String RestrictedNotes,String standardNotes,String sortBy) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getOUTCReport(courtId, caseType, caseClass, bcStatus, 
				hearingTypeCode,timeEstFrom,timeEstTo,units,refJudgeType,judgeDescription,unitsWeeks,
				SecureCourtRoom,juvenileOnly,priorityNotes,RestrictedNotes,standardNotes,
				sortBy);
	}
	
	public static UNLCReport getUNLCReport(Integer courtId,String caseType,String caseClass,String bcStatus,		
			String hearingTypeCode,Integer	timeEstFrom,Integer timeEstTo,Integer units,Integer refJudgeType,String judgeDescription,Integer unitsWeeks,		
			String SecureCourtRoom,String juvenileOnly,String priorityNotes,String RestrictedNotes,String standardNotes,String sortBy) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getUNLCReport(courtId, caseType, caseClass, bcStatus, 
				hearingTypeCode,timeEstFrom,timeEstTo,units,refJudgeType,judgeDescription,unitsWeeks,
				SecureCourtRoom,juvenileOnly,priorityNotes,RestrictedNotes,standardNotes,
				sortBy);
	}

	
	public static CTLRLReport getCTLRLReport(Integer courtId, Date limitDate) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getCTLRLReport(courtId, limitDate);
	}

	public static OBWPrintValue getOBWValues(Date bwIssueDate, Integer courtId) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getOBWValues(bwIssueDate, courtId);
	}
	
	public static DRSRReport getDRSRReport(Integer courtId, String monthPeriod,String yearPeriod ) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getDRSRReport(courtId, monthPeriod,yearPeriod);
	}
	
	public static INFTRPCReport getINFTRPCReport(Integer courtId, String monthPeriod,String yearPeriod ) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getINFTRPCReport(courtId, monthPeriod,yearPeriod);
	}
	
	
	public static INFTRPCCaseNumReport getINFTRPCCaseNumReport(Integer courtId, String monthPeriod,String yearPeriod ) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getINFTRPCCaseNumReport(courtId, monthPeriod,yearPeriod);
	}
	
	
	public static RELCJReport getRELCJReport(Integer courtId) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getRELCJReport(courtId);
	}

	public static NTRSFReport getNTRSFReport(Integer courtId, Integer caseId) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getNTRSFReport(courtId, caseId);
	}

	public static RJSReport getRJSReport(Integer courtId, Date sittingDate) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getRJSReport(courtId, sittingDate);
	}

	public static RUMOReport getRUMOReport(Integer courtId) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getRUMOReport(courtId);
	}

	public static RRCAReport getRRCASummaryReport(Integer courtId, Date endDate) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getRRCASummaryReport(courtId, endDate);
	}
	
	public static RRCAReport getRRCADetailReport(RRCAReport report, Integer courtId, Date endDate) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getRRCADetailReport(report, courtId, endDate);
	}
	
	public static RSITReport getRSITReport(Integer courtId, Integer courtSiteId, Date endDate) {
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getRSITReport(courtId, courtSiteId, endDate);
	}
	
	public static OGRDAOrder getOGRDAOrder(Integer courtId, Integer legalAidOrderId) {
		LegalAidOrderDatabaseManager dbMan = new LegalAidOrderDatabaseManager();
		return dbMan.getOGRDAOrder(courtId, legalAidOrderId);
	}
	
	public static OGRROrder getOGRROrder(Integer legalAidOrderId) {
		LegalAidOrderDatabaseManager dbMan = new LegalAidOrderDatabaseManager();
		return dbMan.getOGRROrder(legalAidOrderId);
	}
	
	public static OARDAPrintInformation getOARDAPrintInformation(Integer courtId, Integer legalAidAmendmentId){
		LegalAidOrderDatabaseManager dbMan = new LegalAidOrderDatabaseManager();
		return dbMan.getOARDAPrintInformation(courtId, legalAidAmendmentId);
	}
	
	public static OARRPrintInformation getOARRPrintInformation(Integer courtId, Integer legalAidAmendmentId){
		LegalAidOrderDatabaseManager dbMan = new LegalAidOrderDatabaseManager();
		return dbMan.getOARRPrintInformation(courtId, legalAidAmendmentId);
	}
	
	public static OWRDAPrintInformation getOWRDAPrintInformation(Integer courtId, Integer legalAidAmendmentId){
		LegalAidOrderDatabaseManager dbMan = new LegalAidOrderDatabaseManager();
		return dbMan.getOWRDAPrintInformation(courtId, legalAidAmendmentId);
	}
	
	public static OWRRPrintInformation getOWRRPrintInformation(Integer courtId, Integer legalAidAmendmentId){
		LegalAidOrderDatabaseManager dbMan = new LegalAidOrderDatabaseManager();
		return dbMan.getOWRRPrintInformation(courtId, legalAidAmendmentId);
	}
	
	public static String getAROInformation(Integer caseId){
		ReportDatabaseManager dbMan = new ReportDatabaseManager();
		return dbMan.getAROInformation(caseId);
	}

	/**
	 * Do all the work to determine that this case is varied.
	 * @return
	 */
	public static boolean isVaried( Integer defOnCaseId ) {
		Collection defendantsOnOffence = XhbDefendantOnOffenceBeanHelper2.findByDefOnCaseId(defOnCaseId);
		
		boolean outcome = defendantsOnOffence != null && defendantsOnOffence.size() > 0;
		boolean varied = false;
		
		if ( outcome ){
			Iterator iter = defendantsOnOffence.iterator();
			
			List<Integer> listOfRefAppresultId = new ArrayList<Integer>();
			
			while ( iter.hasNext()){
				XhbDefendantOnOffence defendantOnOffence =(XhbDefendantOnOffence) iter.next();
				
				if ( defendantOnOffence != null){
					log.debug(String.format("Def on off ID: %d",  defendantOnOffence.getDefendantOnOffenceId()));
					
					Collection verdicts = XhbVerdictBeanHelper2.findByDefendantOnOffenceId(defendantOnOffence.getDefendantOnOffenceId());
					
					outcome = ( verdicts != null && verdicts.size() > 0 );
					
					if ( outcome ){
						Iterator verdictIter = verdicts.iterator();
						
						while( verdictIter.hasNext()){
							XhbVerdict verdict = (XhbVerdict)verdictIter.next();
							
							Integer refAppResultId = verdict.getRefAppResultId();
							
							if ( !listOfRefAppresultId.contains(refAppResultId)){
								listOfRefAppresultId.add(refAppResultId);
							}
						}
						
					}
				}
			}
			
			//	Check for all the RefAppResult stuff.
			for ( int index = 0; index < listOfRefAppresultId.size() && outcome && !varied; index++){
				Integer id = listOfRefAppresultId.get(index);
				
				XhbRefAppResult refAppResult = XhbRefAppResultBeanHelper2.findByPrimaryKey(id);
				
				outcome = refAppResult != null;
				
				if ( outcome ){
					String appResultCode = refAppResult.getAppResultCode();
					
					Collection refAppResults = XhbRefAppResD20MapBeanHelper2.findByAppResultCode(appResultCode);
					
					log.debug(String.format("Ref app resuots: %d", refAppResults.size()));
					
					Iterator refAppIter = refAppResults.iterator();
					
					while( refAppIter.hasNext() && !varied){
						XhbRefAppResD20Map object = (XhbRefAppResD20Map) refAppIter.next();
						
						if ( object != null ){
							log.debug("Non null object");
							String d20Result = object.getD20Result();
							
							varied = d20Result.equals("Varied");
						}
					}
				}
			}
		}
		
		return varied;
	}
}