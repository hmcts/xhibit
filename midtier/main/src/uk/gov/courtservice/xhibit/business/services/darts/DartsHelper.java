package uk.gov.courtservice.xhibit.business.services.darts;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.darts.DarRetentionPolicy;
import uk.gov.courtservice.xhibit.business.entities.darts.DarRetentionPolicyMaintainer;
import uk.gov.courtservice.xhibit.business.entities.darts.RefDarRetentionPolicies;
import uk.gov.courtservice.xhibit.business.entities.darts.RefDarRetentionPoliciesMaintainer;
import uk.gov.courtservice.xhibit.business.entities.darts.RefDispRetentionPolicy;
import uk.gov.courtservice.xhibit.business.entities.darts.RefDispRetentionPolicyMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendantonoffence.DefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.defendantonoffence.DefendantOnOffenceMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_config_prop.XhbConfigPropBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_config_prop.XhbConfigPropBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntry;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2BasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2BeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPleaBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPleaBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_app_result.XhbRefAppResultBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_app_result.XhbRefAppResultBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_line.XhbRefDisposalLineBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_line.XhbRefDisposalLineBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_system_code.XhbRefSystemCode;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_system_code.XhbRefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_system_code.XhbRefSystemCodeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdictBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdictBeanHelper2;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DarRetentionPolicyBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DarRetentionPolicyComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefDarRetentionPoliciesBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefDispRetentionPolicyBasicValue;

/**
 * <p>
 * Title: DartsHelper
 * </p>
 * <p>
 * Description: 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2021
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class DartsHelper {
	private static final Logger LOG = CSServices.getLogger(DartsHelper.class);
	
	private static final String DELETED = "DELETED";
	private static final String NO = "N";
	private static final String YES = "Y";
	private static final String DVR_RELEASE_DATE = "DARTS_DVR_RELEASE_DATE";
	private static final DateFormat DATEFORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private static final String APPEAL_CASE_TYPE = "A";
	
	private static interface POLICY {
		static final Integer SEVEN_YEAR_NONCUSTODIAL = 2;
	}
	
	private static interface COURT_LOG_EVENT {
		static final Integer SEX_OFFENDERS_REGISTER = Integer.valueOf(21600);
		static final Integer CASE_CLOSED = Integer.valueOf(30300);
		static final Integer CREATE_RELATED_DISPOSAL = Integer.valueOf(40750);
	    static final Integer CREATE_UNRELATED_DISPOSAL = Integer.valueOf(40751);
	    static final Integer EDIT_RELATED_DISPOSAL = Integer.valueOf(40752);
	    static final Integer EDIT_UNRELATED_DISPOSAL = Integer.valueOf(40753);
	    static final Integer DELETE_RELATED_DISPOSAL = Integer.valueOf(40754);
	    static final Integer DELETE_UNRELATED_DISPOSAL = Integer.valueOf(40755);
	    static final Integer ADD_EDIT_PLEA_60101 = Integer.valueOf(60101);
	    static final Integer ADD_EDIT_PLEA_60102 = Integer.valueOf(60102);
	    static final Integer ADD_EDIT_PLEA_60103 = Integer.valueOf(60103);
	    static final Integer ADD_EDIT_VERDICT_40720 = Integer.valueOf(40720);
	    static final Integer ADD_EDIT_VERDICT_40721 = Integer.valueOf(40721);
	    static final Integer ADD_EDIT_VERDICT_40722 = Integer.valueOf(40722);
	    static final Integer ADD_EDIT_VERDICT_40725 = Integer.valueOf(40725);
	    static final Integer ADD_EDIT_VERDICT_40726 = Integer.valueOf(40726);
	    static final Integer ADD_EDIT_VERDICT_40727 = Integer.valueOf(40727);
	    static final Integer ADD_EDIT_VERDICT_40730 = Integer.valueOf(40730);
	    static final Integer ADD_EDIT_VERDICT_40731 = Integer.valueOf(40731);
	    static final Integer ADD_EDIT_VERDICT_40732 = Integer.valueOf(40732);
	    static final Integer ADD_EDIT_VERDICT_40733 = Integer.valueOf(40733);
	    static final Integer ADD_EDIT_VERDICT_40736 = Integer.valueOf(40736);
	    static final Integer DELETE_APPEAL_RESULT_40735 = Integer.valueOf(40735);
	    static final Integer ADD_EDIT_VERDICT_40737 = Integer.valueOf(40737);	    
	    static final Integer ADD_EDIT_VERDICT_40738 = Integer.valueOf(40738);
	    static final Integer ADD_EDIT_VERDICT_40756 = Integer.valueOf(40756);
	}

	private CaseMaintainer caseMaintainer;
	private DarRetentionPolicyMaintainer darMaintainer;
	private RefDarRetentionPoliciesMaintainer refDarMaintainer;
	private RefDispRetentionPolicyMaintainer refDispMaintainer;
	private DefendantOnCaseMaintainer defendantOnCaseMaintainer;
	private DefendantOnOffenceMaintainer defendantOnOffenceMaintainer;
	private ConfigPropMaintainer configPropMaintainer;
	

	private String methodName;

	public DartsHelper() {
	}
		
	/**
	 * Description: Find the Ref Darts Retention Policy 
	 * 
	 * @param refDarRetentionPolicyId
	 * @throws FinderException 
	 */
	public RefDarRetentionPoliciesBasicValue findRefDarRetentionPolicy(final Integer refDarRetentionPolicyId) throws FinderException {
		methodName = "findRefDarRetentionPolicy()";
		LOG.debug(methodName + " called");
		RefDarRetentionPoliciesBasicValue result = null;
		try {
			RefDarRetentionPolicies refDarLocal = getRefDarMaintainer().findByPrimaryKey(refDarRetentionPolicyId);
			result = getRefDarMaintainer().getBasicValue(refDarLocal);
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
		return result;
	}
	
	/**
	 * Description: Find the Dar Retention Policy 
	 * 
	 * @param darRetentionPolicyId
	 * @throws FinderException 
	 */
	public DarRetentionPolicyBasicValue findDarRetentionPolicy(final Integer darRetentionPolicyId) throws FinderException {
		methodName = "findDarRetentionPolicy()";
		LOG.debug(methodName + " called");
		DarRetentionPolicyBasicValue result = null;
		try {
			DarRetentionPolicy darLocal = getDarMaintainer().findByPrimaryKey(darRetentionPolicyId);
			result = getDarMaintainer().getBasicValue(darLocal);
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
		return result;
	}

	/**
	 * Description: Find the Ref Disposal Retention Policy 
	 * 
	 * @param darRetentionPolicyId
	 * @throws FinderException 
	 */
	public RefDispRetentionPolicyBasicValue findRefDispRetentionPolicy(final Integer refDispRetentionPolicyId) throws FinderException {
		methodName = "findRefDispRetentionPolicy()";
		LOG.debug(methodName + " called");
		RefDispRetentionPolicyBasicValue result = null;
		try {
			RefDispRetentionPolicy refDispLocal = getRefDispMaintainer().findByPrimaryKey(refDispRetentionPolicyId);
			result = getRefDispMaintainer().getBasicValue(refDispLocal);
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
		return result;
	}
	
	/**
	 * Description: Find the Darts Retention Policy 
	 * 
	 * @param caseId
	 * @param darRetentionPolicyId
	 * @throws FinderException 
	 */
	public DarRetentionPolicyComplexValue findDartsRetentionPolicy(final Integer caseId, final Integer darRetentionPolicyId) throws FinderException {
		methodName = "findDartsRetentionPolicy()";
		LOG.debug(methodName + " called");
		DarRetentionPolicyComplexValue result = null;
		try {
		    result = getCaseRetentionPolicy(caseId, darRetentionPolicyId);
		    if (result != null) {
			    Integer refDarRetentionPolicyId = result.getRefDarRetentionPolicyId();
			    if (refDarRetentionPolicyId == null && result.getRefDispRetentionPolicyId() != null) {
			    	RefDispRetentionPolicy dispLocal = getRefDispMaintainer().findByPrimaryKey(result.getRefDispRetentionPolicyId());
			    	refDarRetentionPolicyId = dispLocal.getRefDarRetentionPolicyId();
			    }
			    RefDarRetentionPoliciesBasicValue refDarBasicValue = null;
			    if (refDarRetentionPolicyId != null) {
					RefDarRetentionPolicies refDarLocal = getRefDarMaintainer().findByPrimaryKey(refDarRetentionPolicyId);
					refDarBasicValue = getRefDarMaintainer().getBasicValue(refDarLocal);
			    }
			    result.setRefDarRetentionPoliciesBasicValue(refDarBasicValue);
		    } else {
		    	result = new DarRetentionPolicyComplexValue(null, 1); 
		    }
		    
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		}
		return result;
	}

	private DarRetentionPolicyComplexValue getCaseRetentionPolicy(final Integer caseId, final Integer darRetentionPolicyId) throws FinderException {
		DarRetentionPolicyComplexValue result = null;
		// Get the summary from the caseId
		if (darRetentionPolicyId != null) {
			result = getDarMaintainer().findComplexValueById(darRetentionPolicyId);
		}
		// If the summary has not been populated or doesn't have a policy on it then check by caseId
		if (result == null || result.getRefDarRetentionPolicyId() == null) {
			result = getDarMaintainer().getCaseDarRetentionPolicy(caseId);
		}
		return result;
	}
	
	/**
	 * Description: Find the Darts Variable Retention Release Date 
	 */
	public Date getDvrReleaseDate() {
		Date dvrReleaseDate = null;
		String dvrReleaseDateStr = getConfigPropMaintainer().getPropertyValue(DVR_RELEASE_DATE);
		if (dvrReleaseDateStr != null && !"".equals(dvrReleaseDateStr)) {
			try {
				dvrReleaseDate = DATEFORMAT.parse(dvrReleaseDateStr);
			} catch (ParseException e) {
				dvrReleaseDate = null;
			}
		}
		return dvrReleaseDate;
	}
	
	/**
	 * Description: Recalculate Darts retention policy for Case 
	 * 
	 * @param caseId
	 * @param userDisplayName
	 * @throws FinderException 
	 */
	public Integer recalculateCaseRetentionPolicy(final Integer caseId, final String userDisplayName) throws FinderException {
		return updateCaseRetentionPolicy(caseId, null, null, null, null, null, null, userDisplayName);
	}
	
	/**
	 * Description: Check whether to send an outgoing retention policy
	 * 
	 * @param xhibitEventType
	 * @return boolean 
	 */
	public boolean isSendOutGoingRetentionPolicyRequired(final Integer xhibitEventType) {
		methodName = "isSendOutGoingRetentionPolicyRequired("+xhibitEventType+")";
		LOG.debug(methodName + " called");
		return xhibitEventType != null &&
				(xhibitEventType.equals(COURT_LOG_EVENT.SEX_OFFENDERS_REGISTER) ||
						xhibitEventType.equals(COURT_LOG_EVENT.CASE_CLOSED) ||
						xhibitEventType.equals(COURT_LOG_EVENT.CREATE_RELATED_DISPOSAL) ||
						xhibitEventType.equals(COURT_LOG_EVENT.EDIT_RELATED_DISPOSAL) ||
						xhibitEventType.equals(COURT_LOG_EVENT.DELETE_RELATED_DISPOSAL) ||
						xhibitEventType.equals(COURT_LOG_EVENT.CREATE_UNRELATED_DISPOSAL) ||
						xhibitEventType.equals(COURT_LOG_EVENT.EDIT_UNRELATED_DISPOSAL) ||
						xhibitEventType.equals(COURT_LOG_EVENT.DELETE_UNRELATED_DISPOSAL) ||
						xhibitEventType.equals(COURT_LOG_EVENT.ADD_EDIT_PLEA_60101) ||
						xhibitEventType.equals(COURT_LOG_EVENT.ADD_EDIT_PLEA_60102) ||
						xhibitEventType.equals(COURT_LOG_EVENT.ADD_EDIT_PLEA_60103) ||
						xhibitEventType.equals(COURT_LOG_EVENT.ADD_EDIT_VERDICT_40720) ||
						xhibitEventType.equals(COURT_LOG_EVENT.ADD_EDIT_VERDICT_40721) ||
						xhibitEventType.equals(COURT_LOG_EVENT.ADD_EDIT_VERDICT_40722) ||
						xhibitEventType.equals(COURT_LOG_EVENT.ADD_EDIT_VERDICT_40725) ||
						xhibitEventType.equals(COURT_LOG_EVENT.ADD_EDIT_VERDICT_40726) ||
						xhibitEventType.equals(COURT_LOG_EVENT.ADD_EDIT_VERDICT_40727) ||
						xhibitEventType.equals(COURT_LOG_EVENT.ADD_EDIT_VERDICT_40730) ||
						xhibitEventType.equals(COURT_LOG_EVENT.ADD_EDIT_VERDICT_40731) ||
						xhibitEventType.equals(COURT_LOG_EVENT.ADD_EDIT_VERDICT_40732) ||
						xhibitEventType.equals(COURT_LOG_EVENT.ADD_EDIT_VERDICT_40733) ||
						xhibitEventType.equals(COURT_LOG_EVENT.DELETE_APPEAL_RESULT_40735) ||
						xhibitEventType.equals(COURT_LOG_EVENT.ADD_EDIT_VERDICT_40736) ||
						xhibitEventType.equals(COURT_LOG_EVENT.ADD_EDIT_VERDICT_40737) ||
						xhibitEventType.equals(COURT_LOG_EVENT.ADD_EDIT_VERDICT_40738) ||
						xhibitEventType.equals(COURT_LOG_EVENT.ADD_EDIT_VERDICT_40756)
						);
	}
	
	/**
	 * Description: Get the reference darts retention policies by policyNo
	 * 
	 * @param policyNo
	 */
	public RefDarRetentionPoliciesBasicValue getDefaultRefDarPolicy() {
		methodName = "getDefaultRefDarPolicy()";
		LOG.debug(methodName + " called");
		//return getRefDarRetentionPolicyByPolicyNo(POLICY.SEVEN_YEAR_NONCUSTODIAL);
		return null;
	}
	
	/**
	 * Description: Update retention policy on send of an outgoing msg
	 * 
	 * @param xhibitEventType
	 * @param darRetentionPolicyId
	 * @param caseId
	 * @param userDisplayName
	 */
	public Integer updateRetentionPolicyOnMsgSend(final Integer xhibitEventType,
			final Integer darRetentionPolicyId,
			final Integer caseId, final String caseType, final String userDisplayName) throws FinderException {
		LOG.debug("updateRetentionPolicyOnMsgSend()");
		//If no case retention policy has been set, then...
		if (darRetentionPolicyId == null) {
			// Recalculate the case retention totals
			return updateCaseRetentionPolicy(caseId, null, null, null, null, null, xhibitEventType, userDisplayName);
		} else {
			// Get the case (this already has the correct darRetentionPolicyId)
			CaseBasicValue caseBasicValue = getCaseBasicValue(caseId);
			Integer retentionPolicyNo = null;
			
			// Get the disposal lines
			List<XhbDisposalLineBasicValue> disposalLines = getDisposalLines(caseId);
			
			// Get the Retention Policies
			List<RefDarRetentionPolicies> refPolicies = getLocalRefPolicies();
			
			// Get the plea retention policyNo
			retentionPolicyNo = getPleaPolicyNo(caseId, caseType, null, retentionPolicyNo);
			
			// Get verdicts
			XhbVerdictBasicValue[] array = XhbVerdictBeanHelper2.findByCaseIdValue(caseId);
			List<XhbVerdictBasicValue> verdicts = Arrays.asList(array);
			retentionPolicyNo = getVerdictPolicyNo(caseId, caseType, null, retentionPolicyNo, verdicts);
			
			// Get the appeal results
			Map<Integer,List<DartsAppealResultEnum>> appealResultEnums = getAppealResultsOnCase(caseType, verdicts);
			
			// Get map of counts with the appeal rules from the appeal results on case
			Map<Integer,List<DartsAppealRetentionRulesEnum>> appealRuleEnums = getAppealRulesOnCase(appealResultEnums);
			
			// Recalculate the retention policy 
			CaseRetentionData caseRetentionData = new CaseRetentionData(caseId, caseType, retentionPolicyNo, disposalLines, refPolicies, appealRuleEnums, xhibitEventType);
			Integer refDarRetentionPolicyId = caseRetentionData.caseData.refDarRetentionPolicyId;
			
			// Get the case retention policy record
			DarRetentionPolicyBasicValue darBasicValue = findDarRetentionPolicy(darRetentionPolicyId);
			
			// If there is no retention policy (ie no pleas) then set the default one
			if (refDarRetentionPolicyId == null && darBasicValue.getRefDarRetentionPolicyId() == null) {
				RefDarRetentionPoliciesBasicValue refDarPolicy = getDefaultRefDarPolicy();
				refDarRetentionPolicyId = refDarPolicy != null ? refDarPolicy.getRefDarRetentionPolicyId() : null;
			}
			
			// Update the retention policy
			if (refDarRetentionPolicyId != null) {
				// Update the case retention policy no
				darBasicValue.setRefDarRetentionPolicyId(refDarRetentionPolicyId);
				getDarMaintainer().update(darBasicValue, userDisplayName);
			}
			
			// Update the CRP date
			updateCRP(caseBasicValue, userDisplayName);
			return darRetentionPolicyId;
		}	
	}
	
	/**
	 * Description: Update DARTS tables upon Create of a Disposal 
	 * 
	 * @param caseId
	 * @param disposalCode
	 * @param userDisplayName
	 * @throws FinderException 
	 */
	public void createDisposal(final Integer caseId, final Integer disposal2Id, final String disposalCode, 
			final Integer defendantOnCaseId, final Integer defendantOnOffenceId,
			final XhbDisposalLineBasicValue[] disposalLines,
			final String userDisplayName) throws FinderException {
		methodName = "createDisposal()";
		LOG.debug(methodName + " called");
		updateDisposalRetentionPolicy(caseId, disposal2Id, disposalCode, 
				defendantOnCaseId, defendantOnOffenceId, disposalLines,
				userDisplayName);
	}
		
	private void updateDisposalRetentionPolicy(final Integer caseId, final Integer disposal2Id, final String disposalCode, 
			final Integer defendantOnCaseId, final Integer defendantOnOffenceId,
			final XhbDisposalLineBasicValue[] disposalLines,
			final String userDisplayName) throws FinderException {
		// Get the Retention Policies
		List<RefDarRetentionPolicies> refDarPolicies = getLocalRefPolicies();
		
		// Get the data for population
		DisposalRetentionData darRetentionData = new DisposalRetentionData(defendantOnCaseId, defendantOnOffenceId,
				Arrays.asList(disposalLines), refDarPolicies);
		
		// Create the Disposal Policy
		setDisposalBasicValue(userDisplayName, darRetentionData.disposalMap, false);
		
		updateCaseRetentionPolicy(caseId, null, null, null, null, null, null, userDisplayName);
	}
	
	private void setDisposalBasicValue( 
			final String userDisplayName,
			final Map<Integer, DartsDisposalLineData> disposalMap,
			final boolean createOnly) throws FinderException {
		if (disposalMap.size() > 0) {
			DarRetentionPolicy darLocal = null;
			for (DartsDisposalLineData disposal2Data : disposalMap.values()) {
				darLocal = getLocalDartsRetentionPolicyByDisposal2Id(disposal2Data.disposal2Id);
				
				DarRetentionPolicyBasicValue basicValue;
				if (darLocal == null) {
					basicValue = new DarRetentionPolicyBasicValue(null, 1);
					basicValue.setDisposal2Id(disposal2Data.disposal2Id);
					basicValue.setDefendantOnCaseId(disposal2Data.defendantOnCaseId);
					basicValue.setDefendantOnOffenceId(disposal2Data.defendantOnOffenceId);
					basicValue.setIsUpdated(NO);
				} else {
					basicValue = getDarMaintainer().getBasicValue(darLocal);
					basicValue.setIsUpdated(YES);
				}
				basicValue.setRefDispRetentionPolicyId(disposal2Data.dispRetentionPolicy.getRefDispRetentionPolicyId());
				Integer[] durations = DartsRetentionCalcHelper.calculateDurations(disposal2Data.durationMap);
				basicValue.setDurationDays(durations[0]); 
				basicValue.setDurationMonths(durations[1]); 
				basicValue.setDurationYears(durations[2]);
				basicValue.setHasLife(disposal2Data.hasLife);
				basicValue.setIsConsecutive(disposal2Data.isConsecutive); 
				basicValue.setObsInd(NO);
				
				LOG.debug("DisposalId: " + basicValue.getDisposal2Id()
						+ ", DefendantOnCaseId: " + basicValue.getDefendantOnCaseId() 
						+ ", DefendantOnOffenceId: " + basicValue.getDefendantOnOffenceId() 
						+ " - Days= " + basicValue.getDurationDays() 
						+ ", Months= " + basicValue.getDurationMonths() 
						+ ", Years= " + basicValue.getDurationYears());
				
				if (darLocal == null) {
					darLocal = (DarRetentionPolicy) getDarMaintainer().create(basicValue, userDisplayName);
				} else if (!createOnly) {
					getDarMaintainer().update(basicValue, userDisplayName);
				}
				 
				Integer darRetentionPolicyId = darLocal.getDarRetentionPolicyId();
				
				// Update defendantOnCase (if applicable)
				if (disposal2Data.defendantOnCaseId != null) {
					getDefendantOnCaseMaintainer().updateDarRetentionPolicy(disposal2Data.defendantOnCaseId, darRetentionPolicyId, userDisplayName);
				}
				
				// Update defendantOnOffence (if applicable)
				if (disposal2Data.defendantOnOffenceId != null) {
					getDefendantOnOffenceMaintainer().updateDarRetentionPolicy(disposal2Data.defendantOnOffenceId, darRetentionPolicyId, userDisplayName);
				}
			}
		}
	}

	/**
	 * Description: Update DARTS tables upon Update of a Disposal 
	 * 
	 * @param caseId
	 * @param disposal2Id
	 * @param disposalCode
	 * @param defendantOnCaseId
	 * @param defendantOnOffenceId
	 * @param userDisplayName
	 * @throws FinderException 
	 */
	public void updateDisposal(final Integer caseId, final Integer disposal2Id, final String disposalCode, 
			final Integer defendantOnCaseId, final Integer defendantOnOffenceId,
			final XhbDisposalLineBasicValue[] disposalLines,
			final String userDisplayName) throws FinderException {
		methodName = "updateDisposal()";
		LOG.debug(methodName + " called");
		updateDisposalRetentionPolicy(caseId, disposal2Id, disposalCode, 
				defendantOnCaseId, defendantOnOffenceId, disposalLines,
				userDisplayName);
	}

	/**
	 * Description: Update DARTS tables upon Delete of a Disposal 
	 * 
	 * @param caseId
	 * @param disposal2Id
	 * @param userDisplayName
	 * @throws FinderException 
	 */
	public void deleteDisposal(final Integer caseId, final Integer disposal2Id, final String userDisplayName) {
		methodName = "deleteDisposal()";
		LOG.debug(methodName + " called");
		
		try {
			// Delete the disposal
			Collection<DarRetentionPolicy> darLocals = getDarMaintainer().findByDisposal2Id(disposal2Id);
			for (DarRetentionPolicy darLocal : darLocals) {
				getDarMaintainer().delete(darLocal.getDarRetentionPolicyId(), darLocal.getVersion(), userDisplayName);
			}
			
			// Recalculate the case totals
			updateCaseRetentionPolicy(caseId, null, null, null, null, null, null, userDisplayName);
		} catch (FinderException ex) {
			LOG.debug(methodName + "- no data found");
		}
	}
	
	private Map<DartsRetentionPolicyEnum, Integer> getPleasOnCase(Integer caseId, String caseType) {
		Map<DartsRetentionPolicyEnum, Integer> results = new HashMap<DartsRetentionPolicyEnum, Integer>();
		Collection<XhbPleaBasicValue> pleas = getPleasByCaseId(caseId);
		if (pleas != null) {
			for (XhbPleaBasicValue plea : pleas) {
				if (YES.equals(plea.getObsInd())) {
					continue;
				}
				String refPleaCode = null;
				String refPleaDeCode = null;
				boolean breachAdmitted = YES.equals(plea.getBreachAdmitted());
				if (plea.getRefPleaId() != null) {
					XhbRefSystemCodeBasicValue refPlea = getRefVerdict(plea.getRefPleaId());
					if (refPlea!= null && !YES.equals(refPlea.getObsInd())) {
						refPleaCode = refPlea.getCode();
						refPleaDeCode = refPlea.getDeCode();
					}
				}
				DartsRetentionPolicyEnum pleaEnum = getDartsPleaEnum(caseType, refPleaCode, refPleaDeCode, breachAdmitted);
				if (pleaEnum != null) {
					Integer count = 0;
					if (results.containsKey(pleaEnum)) {
						count = results.get(pleaEnum);
					}
					count = count+1;
					results.put(pleaEnum,count);
				}
			}
		}
		return results;
	}

	private Collection<XhbPleaBasicValue> getPleasByCaseId(Integer caseId) {
		LOG.debug("getPleasByCaseId("+caseId+")");
		Collection<XhbPleaBasicValue> offencePleas = Arrays.asList(XhbPleaBeanHelper2.findByOffenceCaseIdValue(caseId));
		Collection<XhbPleaBasicValue> chargePleas = Arrays.asList(XhbPleaBeanHelper2.findByChargeCaseIdValue(caseId));
		Collection<XhbPleaBasicValue> results = new ArrayList<XhbPleaBasicValue>();
		results.addAll(offencePleas);
		results.addAll(chargePleas);
		return results;
	}
	
	private Map<DartsRetentionPolicyEnum, Integer> getVerdictsOnCase(Integer caseId, List<XhbVerdictBasicValue> verdicts) {
		Map<DartsRetentionPolicyEnum, Integer> results = new HashMap<DartsRetentionPolicyEnum, Integer>();
		if (verdicts != null) {
			for (XhbVerdictBasicValue verdict : verdicts) {
				if (YES.equals(verdict.getObsInd())) {
					continue;
				}
				XhbRefSystemCodeBasicValue refVerdict = getRefVerdict(verdict.getRefVerdictId());
				if (refVerdict != null && !YES.equals(refVerdict.getObsInd())) {
					DartsRetentionPolicyEnum verdictEnum = getDartsVerdictEnum(refVerdict.getCode(), refVerdict.getDeCode());
					if (verdictEnum != null) {
						Integer count = 0;
						if (results.containsKey(verdictEnum)) {
							count = results.get(verdictEnum);
						}
						count = count+1;
						results.put(verdictEnum,count);
					}
				}
			}
		}
		return results;
	}
	
	@SuppressWarnings("unchecked")
	private Map<Integer,List<DartsAppealResultEnum>> getAppealResultsOnCase(String caseType, List<XhbVerdictBasicValue> verdicts) {
		Map<Integer,List<DartsAppealResultEnum>> results = new HashMap<Integer,List<DartsAppealResultEnum>>();
		
		// Get the refAppResults for the appeal case
		if (APPEAL_CASE_TYPE.equals(caseType)) {
			Map<Integer, List<XhbRefAppResultBasicValue>> refAppResultsMap = getAppealResultsFromVerdicts(verdicts);
			if (refAppResultsMap != null && refAppResultsMap.size() > 0) {
				// Loop the countNo's
				for (Map.Entry entry : refAppResultsMap.entrySet()) {
					Integer countNo = (Integer) entry.getKey();
					List<XhbRefAppResultBasicValue> refAppResults = (List<XhbRefAppResultBasicValue>) entry.getValue();
					// Loop the refAppResults within the countNo
					if (refAppResults != null && refAppResults.size() > 0) {
						for (XhbRefAppResultBasicValue refAppResult : refAppResults) {
							if (refAppResult.getAppResultCode() != null) {
						
								// Get the list of enums for this countNo
								List<DartsAppealResultEnum> enumList;
								if (results.containsKey(countNo)) {
									enumList = results.get(countNo);
								} else {
									enumList = new ArrayList<DartsAppealResultEnum>();
								}
							
								// Get the enum and add to the results
								DartsAppealResultEnum enumValue = getDartsAppealResultEnum(
										refAppResult.getAppResultCode(),refAppResult.getAppResultDescr1(),refAppResult.getAppResultDescr2());
								if (enumValue != null && !enumList.contains(enumValue)) {
									enumList.add(enumValue);
								}
								results.put(countNo,  enumList);
							}
						}
					}
				}
			}
		}
		return results;
	}

	private Map<Integer,List<DartsAppealRetentionRulesEnum>> getAppealRulesOnCase(Map<Integer,List<DartsAppealResultEnum>> appResultEnumsMap) {
		Map<Integer,List<DartsAppealRetentionRulesEnum>> results = new HashMap<Integer,List<DartsAppealRetentionRulesEnum>>();
		for (Map.Entry entry : appResultEnumsMap.entrySet()) {
			Integer countNo = (Integer) entry.getKey();
			List<DartsAppealResultEnum> appResultEnums = appResultEnumsMap.get(countNo);
			List<DartsAppealRetentionRulesEnum> enumList = new ArrayList<DartsAppealRetentionRulesEnum>();
			if (appResultEnums != null && appResultEnums.size() > 0) {
				for (DartsAppealResultEnum appResultEnum : appResultEnums) {
					DartsAppealRetentionRulesEnum rulesEnum = appResultEnum.getRulesEnum();
					if (rulesEnum != null && !enumList.contains(rulesEnum)) {
						enumList.add(rulesEnum);
					}
				}
			}
			results.put(countNo,  enumList);
		}
		return results;
	}
	
	private List<XhbDisposalLineBasicValue> getDisposalLines(Integer caseId) {
		List<XhbDisposalLineBasicValue> disposalLines = new ArrayList<XhbDisposalLineBasicValue>();
		XhbDisposalLineBasicValue[] defOnOffdisposalLines = XhbDisposalLineBeanHelper2.findRSOffencesByCaseIdValue(caseId);
		if (defOnOffdisposalLines != null && defOnOffdisposalLines.length > 0) {
			disposalLines.addAll(Arrays.asList(defOnOffdisposalLines));
		}
		XhbDisposalLineBasicValue[] defOnCasedisposalLines = XhbDisposalLineBeanHelper2.findRSByCaseIdValue(caseId);
		if (defOnCasedisposalLines != null && defOnCasedisposalLines.length > 0) {
			disposalLines.addAll(Arrays.asList(defOnCasedisposalLines));
		}
		return disposalLines;
	}
	
	private Map<Integer, List<XhbRefAppResultBasicValue>> getAppealResultsFromVerdicts(List<XhbVerdictBasicValue> verdicts) {
		Map<Integer, List<XhbRefAppResultBasicValue>> results = new HashMap<Integer, List<XhbRefAppResultBasicValue>>();
		if (verdicts != null) {
			Map<Integer, XhbRefAppResultBasicValue> refAppResultMap = new HashMap<Integer, XhbRefAppResultBasicValue>();
			for (XhbVerdictBasicValue verdict : verdicts) {
				if (!YES.equals(verdict.getObsInd()) && verdict.getRefAppResultId() != null) {
					
					// Get the refAppResult for the verdict
					XhbRefAppResultBasicValue refAppResult;
					if (!refAppResultMap.containsKey(verdict.getRefAppResultId())) {
						refAppResult = getAppealResultsFromVerdict(verdict.getRefAppResultId());
						refAppResultMap.put(verdict.getRefAppResultId(), refAppResult);
					} else {
						refAppResult = refAppResultMap.get(verdict.getRefAppResultId());
					}
					
					// Get the countNo for the verdict (countNo=0 for unrelated cases)
					Integer countNo = getDefendantOffenceCountNo(verdict.getDefendantOnOffenceId());
					
					// Add the refAppResult to the list
					List<XhbRefAppResultBasicValue> list;
					if (results.containsKey(countNo)) {
						list = results.get(countNo);
					} else {
						list = new ArrayList<XhbRefAppResultBasicValue>();
					}
					list.add(refAppResult);
					
					// Add to the results
					results.put(countNo, list);
				}
			}
		}
		return results;
	}
	
	public XhbRefAppResultBasicValue getAppealResultsFromVerdict(Integer refAppResultId) {
		if (refAppResultId != null) {
			XhbRefAppResultBasicValue refAppResult = XhbRefAppResultBeanHelper2.findByPrimaryKeyValue(refAppResultId);
			return refAppResult;
		}
		return null;
	}
	
	/**
	 * Description: Update DARTS tables upon update of plea  
	 * 
	 * @param caseId
	 * @param pleaCode
	 * @param pleaDesc
	 * @param userDisplayName
	 * @throws ObjectNotFoundException 
	 * @throws FinderException 
	 */
	public void updatePlea(final Integer caseId, String caseType, 
			final String userDisplayName) throws FinderException {
		methodName = "updatePlea()";
		LOG.debug(methodName + " called");
		
		// Get the pleas on the case
		Map<DartsRetentionPolicyEnum, Integer> pleasOnCase = getPleasOnCase(caseId, caseType);
		
		// Get the list of pleas
		List<DartsRetentionPolicyEnum> pleaEnums = convertEnumMapToList(pleasOnCase);
		
		updateCaseRetentionPolicy(caseId, null, pleaEnums, null, null, null, null, userDisplayName);
	}
	
	private List<DartsRetentionPolicyEnum> convertEnumMapToList(Map<DartsRetentionPolicyEnum, Integer> map) {
		List<DartsRetentionPolicyEnum> enumValues = new ArrayList<DartsRetentionPolicyEnum>();
		for (Map.Entry entry : map.entrySet()) {
			DartsRetentionPolicyEnum enumValue = (DartsRetentionPolicyEnum) entry.getKey();
			Integer count = (Integer) entry.getValue();
			if (count > 0 && enumValue != null) {
				enumValues.add(enumValue);
			}
		}
		return enumValues;
	}
	
	public DartsPleaEnum getDartsPleaEnum(String caseType, String pleaCode, String pleaDesc, Boolean isAdmitted) {
		if (pleaCode != null) {
			return DartsPleaEnum.fromCodeAndDesc(pleaCode, pleaDesc);
		} else if ("S".equals(caseType) && isAdmitted != null) {
			// If admitted then use the Guilty values otherwise Not Guilty
			return isAdmitted ? DartsPleaEnum.G : DartsPleaEnum.NG;
		}
		return null;
	}
	
	public DartsVerdictEnum getDartsVerdictEnum(String verdictCode, String verdictDesc) {
		if (verdictCode != null) {
			return DartsVerdictEnum.fromCodeAndDesc(verdictCode, verdictDesc);
		}
		return null;
	}
	
	public DartsAppealResultEnum getDartsAppealResultEnum(String code, String desc1, String desc2) {
		if (code != null) {
			return DartsAppealResultEnum.fromCodeAndDesc(code, desc1, desc2);
		}
		return null;
	}
	
	/**
	 * Description: Update DARTS tables upon update of verdict  
	 * 
	 * @param caseId
	 * @param verdictCode
	 * @param @verdictDesc
	 * @param userDisplayName
	 * @throws FinderException 
	 */
	public void updateVerdict(final Integer caseId, final String caseType,
			final String userDisplayName) throws FinderException {
		methodName = "updateVerdict()";
		LOG.debug(methodName + " called");
		
		// Get the verdicts on the case
		XhbVerdictBasicValue[] array = XhbVerdictBeanHelper2.findByCaseIdValue(caseId);
		List<XhbVerdictBasicValue> verdicts = Arrays.asList(array);		
		
		// Get the verdict enums on the case
		Map<DartsRetentionPolicyEnum, Integer> verdictsOnCase = getVerdictsOnCase(caseId, verdicts);
		
		// Get the list of verdicts
		List<DartsRetentionPolicyEnum> verdictEnums = convertEnumMapToList(verdictsOnCase);
		
		// Get the list of appeal results
		Map<Integer,List<DartsAppealResultEnum>> refAppResultEnumsMap = getAppealResultsOnCase(caseType, verdicts);
		
		updateCaseRetentionPolicy(caseId, null, null, verdictEnums, refAppResultEnumsMap, verdicts, null, userDisplayName);
	}

	public DartsVerdictEnum getOriginalVerdictEnum(final String caseType, final Integer originalRefVerdictId,
			final Integer refAppResultId) {
		if (refAppResultId != null) {
			// Appeal Result
			return null;
	    } else if (originalRefVerdictId != null && !APPEAL_CASE_TYPE.equals(caseType)) {
			final XhbRefSystemCodeBasicValue refVerdict = getRefVerdict(originalRefVerdictId);
			if (refVerdict != null  && !YES.equals(refVerdict.getObsInd()) && refVerdict.getCode() != null && refVerdict.getDeCode() != null) {
				return getDartsVerdictEnum(refVerdict.getCode(), refVerdict.getDeCode());
			}
		}
		return null;
	}

	public DartsPleaEnum getOriginalPleaEnum(final String caseType, final Integer originalRefPleaId, 
			final Boolean originalBreachAdmitted) {
		String refPleaCode = null;
		String refPleaDeCode = null;
		if (originalRefPleaId != null) {
			final XhbRefSystemCodeBasicValue refPlea = getRefVerdict(originalRefPleaId);
			if (refPlea != null && !YES.equals(refPlea.getObsInd()) && refPlea.getCode() != null && refPlea.getDeCode() != null) {
				refPleaCode = refPlea.getCode();
				refPleaDeCode = refPlea.getDeCode();
			}
		}
		return getDartsPleaEnum(caseType, refPleaCode, refPleaDeCode, originalBreachAdmitted);
	}
	
	private XhbRefSystemCodeBasicValue getRefVerdict(final Integer refVerdictId) {
		if (refVerdictId != null) {
			XhbRefSystemCode refVerdict = XhbRefSystemCodeBeanHelper2.findByPrimaryKey(refVerdictId);
			XhbRefSystemCodeBasicValue basicValue = refVerdict.getData();
			return basicValue;
		}
		return null;
	}

	private CaseBasicValue getCaseBasicValue(final Integer caseId) throws FinderException {
		Case caseLocal = getCaseMaintainer().findByPrimaryKey(caseId);
		CaseBasicValue caseBasicValue = getCaseMaintainer().getCaseBasicValue(caseLocal);
		// If the retention policy isn't set then check if there is a case record 
		if (caseBasicValue.getDarRetentionPolicyId() == null) {
			DarRetentionPolicyComplexValue darValue = getDarMaintainer().getCaseDarRetentionPolicy(caseId);
			if (darValue != null) {
				caseBasicValue.setDarRetentionPolicyId(darValue.getDarRetentionPolicyId());
			}
		}
		return caseBasicValue;
	}
	
	private Integer updateCaseRetentionPolicy(final Integer caseId, 
			Integer retentionPolicyNo, 
			List<DartsRetentionPolicyEnum> pleaEnums,
			List<DartsRetentionPolicyEnum> verdictEnums,
			Map<Integer,List<DartsAppealResultEnum>> appealResultEnums,
			List<XhbVerdictBasicValue> verdicts,
			final Integer xhibitEventType,
			final String userDisplayName) throws FinderException {
		methodName = "updateCaseRetentionPolicy()";
		LOG.debug(methodName + " called");
		
		// Get the case retention policy
		CaseBasicValue caseBasicValue = getCaseBasicValue(caseId);
		String caseType = caseBasicValue.getCaseType();
		Integer darRetentionPolicyId = caseBasicValue.getDarRetentionPolicyId();
		
		// Get the verdicts if we dont already have them
		if (verdicts == null) {
			XhbVerdictBasicValue[] array = XhbVerdictBeanHelper2.findByCaseIdValue(caseId);
			verdicts = Arrays.asList(array);
		}
		
		// Get the plea dominant policyNo
		retentionPolicyNo = getPleaPolicyNo(caseId, caseType, pleaEnums, retentionPolicyNo);
		
		// Get the verdict dominant policyNo
		retentionPolicyNo = getVerdictPolicyNo(caseId, caseType, verdictEnums, retentionPolicyNo, verdicts);
		
		// Get map of counts with the appeal results from the case
		if (appealResultEnums == null) {
			appealResultEnums = getAppealResultsOnCase(caseType, verdicts);
		}
		
		// Get map of counts with the appeal rules from the appeal results on case
		Map<Integer,List<DartsAppealRetentionRulesEnum>> appealRuleEnums = getAppealRulesOnCase(appealResultEnums);
		
		// Get the disposal lines
		List<XhbDisposalLineBasicValue> disposalLines = getDisposalLines(caseId);
				
		// Get the existing darts retention policy from darRetentionPolicyId
		DarRetentionPolicyBasicValue darBasicValue = (DarRetentionPolicyBasicValue) getCaseRetentionPolicy(caseId, darRetentionPolicyId); 
		if (darBasicValue == null) {
			darBasicValue = new DarRetentionPolicyBasicValue(null, 1);
			darBasicValue.setCaseId(caseId);
			darBasicValue.setRefDispRetentionPolicyId(null);
		}
		
		// Get the policies
		List<RefDarRetentionPolicies> refDarPolicies = getLocalRefPolicies();
		
		// Recalculate the retention policy 
		CaseRetentionData caseRetentionData = new CaseRetentionData(caseId, caseType, retentionPolicyNo, disposalLines, refDarPolicies, appealRuleEnums, xhibitEventType);
		
		// Add legacy missing disposals
		addMissingDisposalPolicies(caseRetentionData, userDisplayName);
		
		// Update Case Darts Retention Policy
		darBasicValue.setRefDarRetentionPolicyId(caseRetentionData.caseData.refDarRetentionPolicyId);
		
		Integer[] durations = DartsRetentionCalcHelper.calculateDurations(caseRetentionData.caseData.durationMap);
		darBasicValue.setDurationDays(durations[0]); 
		darBasicValue.setDurationMonths(durations[1]); 
		darBasicValue.setDurationYears(durations[2]);
		darBasicValue.setHasLife(caseRetentionData.caseData.hasLife);
		darBasicValue.setIsConsecutive(NO);
		darBasicValue.setIsUpdated(NO);
		darBasicValue.setObsInd(NO);
		
		LOG.debug("Case: " + darBasicValue.getCaseId()
				+ " - Days= " + darBasicValue.getDurationDays() 
				+ ", Months= " + darBasicValue.getDurationMonths() 
				+ ", Years= " + darBasicValue.getDurationYears());
		
					
		try {
			if (darBasicValue.getDarRetentionPolicyId() == null) {
				DarRetentionPolicy darLocal = (DarRetentionPolicy) getDarMaintainer().create(darBasicValue, userDisplayName);
				darBasicValue = getDarMaintainer().getBasicValue(darLocal);
			} else {
				getDarMaintainer().update(darBasicValue, userDisplayName);
			}
		} catch (EJBException ex) {
			LOG.debug("Case total has been written by another thread");
			darBasicValue = getCaseRetentionPolicy(caseId, null);
			LOG.debug("Fetch updated darRetentionPolicyId "+darBasicValue.getDarRetentionPolicyId());
		}
		
		// Update the case
		caseBasicValue.setDarRetentionPolicyId(darBasicValue.getDarRetentionPolicyId());
		updateCRP(caseBasicValue, userDisplayName);
		return darRetentionPolicyId;
	}
	
	private Integer getDefendantOffenceCountNo(Integer defendantOnOffenceId) {
		Integer countNo = 0;
		// Get the defendant on offence
		if (defendantOnOffenceId != null) {
			XhbDefendantOnOffenceBasicValue defendantOnOffenceBasicValue = XhbDefendantOnOffenceBeanHelper2.findByPrimaryKeyValue(defendantOnOffenceId);
		
			// Get the offence
			if (defendantOnOffenceBasicValue != null) {
				XhbOffenceBasicValue offenceBasicValue = XhbOffenceBeanHelper2.findByPrimaryKeyValue(defendantOnOffenceBasicValue.getOffenceId());
				
				// Set the count number
				if (offenceBasicValue != null) {
					countNo = offenceBasicValue.getCrestOffenceSeqNo();
				}
			}
		}
		return countNo;
	}
	
	private Integer getPleaPolicyNo(final Integer caseId, final String caseType, 
			List<DartsRetentionPolicyEnum> enumValues, Integer retentionPolicyNo) {
		LOG.debug("getPleaPolicyNo("+caseId+")");
		// Get the pleas (if we haven't already)
		if (enumValues ==  null) {
			// Get the pleas on the case
			Map<DartsRetentionPolicyEnum, Integer> pleasOnCase = getPleasOnCase(caseId, caseType);
			// Get the list of pleas
			enumValues = convertEnumMapToList(pleasOnCase);
		}
		return getDominantPolicyNo(caseId, caseType, enumValues, retentionPolicyNo);
	}

	private Integer getVerdictPolicyNo(final Integer caseId, final String caseType, 
			List<DartsRetentionPolicyEnum> enumValues, Integer retentionPolicyNo, List<XhbVerdictBasicValue> verdicts) {
		LOG.debug("getVerdictPolicyNo("+caseId+")");
		// Get the verdicts (if we haven't already)
		if (enumValues ==  null) {
			// Get the pleas on the case
			Map<DartsRetentionPolicyEnum, Integer> verdictsOnCase = getVerdictsOnCase(caseId, verdicts);
			// Get the list of pleas
			enumValues = convertEnumMapToList(verdictsOnCase);
		}
		return getDominantPolicyNo(caseId, caseType, enumValues, retentionPolicyNo);
	}
	
	private Integer getDominantPolicyNo(final Integer caseId, final String caseType, 
			List<DartsRetentionPolicyEnum> enumValues, Integer retentionPolicyNo) {
		if (enumValues != null && enumValues.size() > 0) {
			for (DartsRetentionPolicyEnum enumValue : enumValues) {
				if (retentionPolicyNo == null || retentionPolicyNo < enumValue.getRetentionPolicyNo()) {
					retentionPolicyNo = enumValue.getRetentionPolicyNo();
				}
			}
		}
		LOG.debug("getDominantPolicyNo("+caseId+") = "+retentionPolicyNo);
		return retentionPolicyNo;
	}
	
	private void updateCRP(final CaseBasicValue caseBasicValue, final String userDisplayName) throws FinderException {
		getCaseMaintainer().updateCRP(caseBasicValue, userDisplayName);
	}
	
	private void addMissingDisposalPolicies(CaseRetentionData caseRetentionData, String userDisplayName) throws FinderException {
		if (caseRetentionData.disposalMap.size() > 0) {
			// If no record exists then create one
			setDisposalBasicValue(userDisplayName, caseRetentionData.disposalMap, true);
		}
	}
	
	private RefDispRetentionPolicy getLocalDisposalRetentionPolicyByDisposalCode(final String disposalCode) {
		methodName = "getLocalDisposalRetentionPolicyByDisposalCode()";
		LOG.debug(methodName + " called");
		RefDispRetentionPolicy result = null;
		try {
			Collection<RefDispRetentionPolicy> dispLocals = getRefDispMaintainer().findByDisposalCode(disposalCode);
			RefDispRetentionPolicy dispLocal = null;
			
			Iterator iterator = dispLocals.iterator();
			while (iterator.hasNext()) {
				dispLocal = (RefDispRetentionPolicy) iterator.next();
				result = dispLocal;
				break;
			}
			return result;
		} catch (FinderException ex) {
			return null;
		}
	}

	private DarRetentionPolicy getLocalDartsRetentionPolicyByDisposal2Id(final Integer disposal2Id) {
		methodName = "getLocalDartsRetentionPolicyByDisposal2Id()";
		LOG.debug(methodName + " called");
		DarRetentionPolicy result = null;
		try {
			Collection<DarRetentionPolicy> darLocals = getDarMaintainer().findByDisposal2Id(disposal2Id);
			DarRetentionPolicy darLocal = null;
			
			Iterator iterator = darLocals.iterator();
			while (iterator.hasNext()) {
				darLocal = (DarRetentionPolicy) iterator.next();
				result = darLocal;
				break;
			}
			return result;
		} catch (FinderException ex) {
			return null;
		}
	}
	
	private List<RefDarRetentionPolicies> getLocalRefPolicies() {
		methodName = "getLocalRefPolicies()";
		LOG.debug(methodName + " called");
		try{
			List<RefDarRetentionPolicies> refDarLocals = new ArrayList<RefDarRetentionPolicies>(getRefDarMaintainer().findAllPolicies());
			return refDarLocals;
		} catch (FinderException ex) {
			return null;
		}
	}
	
	/*
	 * Maintainers
	 */
	private CaseMaintainer getCaseMaintainer() {
		if (caseMaintainer == null) {
			caseMaintainer = new CaseMaintainer();
		}
		return caseMaintainer;
	}
	
	private DefendantOnCaseMaintainer getDefendantOnCaseMaintainer() {
		if (defendantOnCaseMaintainer == null) {
			defendantOnCaseMaintainer = new DefendantOnCaseMaintainer();
		}
		return defendantOnCaseMaintainer;
	}
	
	private DefendantOnOffenceMaintainer getDefendantOnOffenceMaintainer() {
		if (defendantOnOffenceMaintainer == null) {
			defendantOnOffenceMaintainer = new DefendantOnOffenceMaintainer();
		}
		return defendantOnOffenceMaintainer;
	}
	
	private DarRetentionPolicyMaintainer getDarMaintainer() {
		if (darMaintainer == null) {
			darMaintainer = new DarRetentionPolicyMaintainer();
		}
		return darMaintainer;
	}
	
	private RefDarRetentionPoliciesMaintainer getRefDarMaintainer() {
		if (refDarMaintainer == null) {
			refDarMaintainer = new RefDarRetentionPoliciesMaintainer();
		}
		return refDarMaintainer;
	}
	
	private RefDispRetentionPolicyMaintainer getRefDispMaintainer() {
		if (refDispMaintainer == null) {
			refDispMaintainer = new RefDispRetentionPolicyMaintainer();
		}
		return refDispMaintainer;
	}
	
	private ConfigPropMaintainer getConfigPropMaintainer() {
		if (configPropMaintainer == null) {
			configPropMaintainer = new ConfigPropMaintainer();
		}
		return configPropMaintainer;
	}
	
	/*
	 * Darts Retention Data Population logic
	 */
	private Integer getDefendantId(final Integer defendantOnCaseId, final Integer defendantOnOffenceId) {
		Integer defendantId = getDefendantIdFromDefOnCase(defendantOnCaseId);
		if (defendantOnOffenceId != null) {
			try {
				DefendantOnOffence defOnOffence = getDefendantOnOffenceMaintainer().findByPrimaryKey(defendantOnOffenceId);
				defendantId = getDefendantIdFromDefOnCase(defOnOffence.getDefendantOnCaseId());
			} catch (ObjectNotFoundException e) {
				defendantId = null;
			}
		}
		return defendantId;
	}
	
	private Integer getDefendantIdFromDefOnCase(final Integer defendantOnCaseId) {
		Integer defendantId = null;
		if (defendantOnCaseId != null) {
			try {
				DefendantOnCase defOnCase = getDefendantOnCaseMaintainer().findByPrimaryKey(defendantOnCaseId);
				defendantId = defOnCase.getDefendantId();
			} catch (ObjectNotFoundException e) {
				defendantId = null;
			}
		}
		return defendantId;
	}
	
	private class CaseRetentionData extends AbstractRetentionData {		

		public DartsCaseData caseData;
		private Integer defaultRefDarRetentionPolicyId;
		
		public CaseRetentionData(
				Integer caseId,
				String caseType, 
				Integer defaultRetentionPolicyNo,
				List<XhbDisposalLineBasicValue> disposalLines,
				List<RefDarRetentionPolicies> refDarPolicies,
				Map<Integer,List<DartsAppealRetentionRulesEnum>> appealRuleEnums,
				Integer xhibitEventType) {
			super(null, null, refDarPolicies);
			setDefaultRefDarRetentionPolicyId(defaultRetentionPolicyNo);
			processDisposalLines(disposalLines);
			recalculateCasePolicyData(appealRuleEnums, xhibitEventType, caseId, caseType);
		}
		
		private void setDefaultRefDarRetentionPolicyId(Integer defaultRetentionPolicyNo) {
			for (RefDarRetentionPolicies refDarPolicy : refDarPolicies) {
				if (refDarPolicy.getPolicyNo().equals(defaultRetentionPolicyNo)) {
					this.defaultRefDarRetentionPolicyId = refDarPolicy.getRefDarRetentionPolicyId();
				}
			}
		}
		
		@Override
		protected void setDisposal2Id(Integer disposal2Id) {
            // The disposal2Id has changed
			if (!disposal2Id.equals(getDisposal2Id())) {
				XhbDisposal2BasicValue disposal2BasicValue = XhbDisposal2BeanHelper2.findByPrimaryKeyValue(disposal2Id);
				// Set the defendantOnCaseId for the disposal2Id being processed
				setDefendantOnCaseId(disposal2BasicValue.getDefendantOnCaseId());
				// Set the defendantOnOffenceId for the disposal2Id being processed
				setDefendantOnOffenceId(disposal2BasicValue.getDefendantOnOffenceId());
				// Get the court Type (Crown or Magistrate)
				setCourtType(disposal2BasicValue.getCourtType());
				
				// Get the count number for this defendant / offence
				Integer countNo = getDefendantOffenceCountNo(getDefendantOnOffenceId());
				
				// Set the offence count number (0=unrelated offences)
				setCountNo(countNo);
            }
			super.setDisposal2Id(disposal2Id);
		}
		
		private void recalculateCasePolicyData(Map<Integer,List<DartsAppealRetentionRulesEnum>> appealRuleEnums,
				Integer xhibitEventType, Integer caseId, String caseType) {
			DartsCourtTypeData dartsCourtTypeData = new DartsCourtTypeData(caseId, caseType, appealRuleEnums);
			caseData = new DartsCaseData();
			caseData.refDarRetentionPolicyId = defaultRefDarRetentionPolicyId;
			if (disposalMap.size() > 0) {
				for (DartsDisposalLineData disposal2Data : disposalMap.values()) {
					
					dartsCourtTypeData.addCountNo(disposal2Data.countNo);
					
					// Calculate the overall totals
					caseData = calculateCaseDataDuration(caseData, disposal2Data);
					caseData = calculateCaseDataPolicy(caseData, disposal2Data);
					
					// Calculate the courtType totals
					if (dartsCourtTypeData.useAppealRules()) {
						Integer countNo = disposal2Data.countNo;
						String courtType = disposal2Data.courtType;
						Integer refDarRetentionPolicyId = disposal2Data.dispRetentionPolicy != null ? disposal2Data.dispRetentionPolicy.getRefDarRetentionPolicyId() : null;
						
						// Set the highest Policy for this court type / count
						if (courtType != null) {
							Integer courtTypeRefDarRetentionPolicyId = dartsCourtTypeData.getCourtTypePolicyId(countNo, courtType);
							courtTypeRefDarRetentionPolicyId = getHighestPolicyId(courtTypeRefDarRetentionPolicyId, refDarRetentionPolicyId);
							if (courtTypeRefDarRetentionPolicyId != null) {
								dartsCourtTypeData.setCourtTypePolicyId(countNo, courtType, courtTypeRefDarRetentionPolicyId);
							}
						}
						
						// Calculate the court type duration
						DartsCaseData courtTypeCaseData = dartsCourtTypeData.getCourtTypeCaseData(countNo, courtType);
						if (courtTypeCaseData == null) {
							courtTypeCaseData = new DartsCaseData();
							courtTypeCaseData.refDarRetentionPolicyId = defaultRefDarRetentionPolicyId;
						}
						courtTypeCaseData = calculateCaseDataDuration(courtTypeCaseData, disposal2Data);
						courtTypeCaseData = calculateCaseDataPolicy(courtTypeCaseData, disposal2Data);
						dartsCourtTypeData.setCourtTypeCaseData(countNo, courtType,courtTypeCaseData);
					}
				}
			}
			
			// Apply the rules to the court type data
			dartsCourtTypeData = applyAppealRules(dartsCourtTypeData);
			
			// Determine which court type caseData to use (or use the overall values)
			caseData = getApplicableCaseData(dartsCourtTypeData);
			
			// Is this a sex offender register event or has there been one in the past
			boolean isSexOffender = COURT_LOG_EVENT.SEX_OFFENDERS_REGISTER.equals(xhibitEventType) ||
					getCourtLogEvent(caseId, COURT_LOG_EVENT.SEX_OFFENDERS_REGISTER) != null;
			
			// If this is an event then apply the event logic to the Retention Policy No
			if (isSexOffender) {
				Integer policyId = getPolicyId(POLICY.SEVEN_YEAR_NONCUSTODIAL);
				caseData.refDarRetentionPolicyId = getHighestPolicyId(caseData.refDarRetentionPolicyId, policyId);
			}
			
		}
		
		private DartsCaseData getApplicableCaseData(DartsCourtTypeData dartsCourtTypeData) {
			LOG.debug("getApplicableCaseData()");
			DartsCaseData result = null;
			if (APPEAL_CASE_TYPE.equals(dartsCourtTypeData.getCaseType())) {
				if (dartsCourtTypeData.useAppealRules()) {
					List<DartsCaseData> applicableCaseDataList = getApplicabaleCaseDataList(dartsCourtTypeData);
					for (DartsCaseData caseDataToUse : applicableCaseDataList) {
						if (result == null) {
							result = caseDataToUse;
							result.refDarRetentionPolicyId = dartsCourtTypeData.highestPolicyId;
						} else {
							for (Map.Entry entry : caseDataToUse.defendantTotalsMap.entrySet()) {
								Integer defendantId = (Integer) entry.getKey();
								DartsDefendantTotals defendantTotalsMap = (DartsDefendantTotals) entry.getValue();
								if (result.defendantTotalsMap.containsKey(defendantId)) {
									DartsDefendantTotals defendantTotalsMapToAdd = result.defendantTotalsMap.get(defendantId);
									defendantTotalsMap.addConsecutiveDuration(defendantTotalsMapToAdd.getTotalConsecutiveDurationMap());
									defendantTotalsMap.addGreatestDuration(defendantTotalsMapToAdd.getGreatestDurationMap());
								}
								result.defendantTotalsMap.put(defendantId, defendantTotalsMap);
							}
							setCasePolicyDuration(result);
						}
					}
				}
				if (result == null) {
					LOG.debug("No applicable appeal rules - Updating to 'Not Set'");
					result = caseData;
					result.resetPolicyId(); 
					result.resetDefendantTotals();
					setCasePolicyDuration(result);
				}
			} else {
				LOG.debug("Do not use appeal rules");
				result = caseData;
				setCasePolicyDuration(result);
			}
			
			return result;
		}
		
		@SuppressWarnings("unchecked")
		private List<DartsCaseData> getApplicabaleCaseDataList(DartsCourtTypeData dartsCourtTypeData) {
			LOG.debug("getApplicabaleCaseDataList()");
			List<DartsCaseData> results = new ArrayList<DartsCaseData>();
			List<Integer> unprocessedCountNoList = new ArrayList<Integer>(dartsCourtTypeData.getCountNoList());
			// Loop the counts with appeals set against them
			for (Map.Entry entry : dartsCourtTypeData.appealRuleEnums.entrySet()) {
				Integer countNo = (Integer) entry.getKey();
				List<DartsAppealRetentionRulesEnum> ruleEnums = (List<DartsAppealRetentionRulesEnum>) entry.getValue();
				// Remove the countNo from the unprocessed countNo list
				if (unprocessedCountNoList.contains(countNo)) {
					unprocessedCountNoList.remove(countNo);
				}
				// Loop through the rules for this appeal count
				for (DartsAppealRetentionRulesEnum ruleEnum : ruleEnums) {
					String courtType = ruleEnum.getCourtType();
					if (courtType != null) {
						DartsCaseData caseDataToUse = dartsCourtTypeData.getCourtTypeCaseData(countNo, courtType);
						if (caseDataToUse != null) {
							// Add this counts caseData to the applicable list
							setCasePolicyDuration(caseDataToUse);
							results.add(caseDataToUse);
						}
					} else if (DartsAppealRetentionRulesEnum.USE_NG_ACQUITTAL.equals(ruleEnum)) {
						for (Map.Entry caseDataEntry : dartsCourtTypeData.getCourtTypeCaseDataMap(countNo).entrySet()) {
							courtType = (String) caseDataEntry.getKey();
							DartsCaseData caseDataToUse = (DartsCaseData) caseDataEntry.getValue();
							if (caseDataToUse != null) {
								// Clear the durations as this is NG now
								caseDataToUse.resetDefendantTotals();
								results.add(caseDataToUse);
							}
						}
					}
				}
			}
			// Loop any counts without any appeals set against them
			for (Integer countNo : unprocessedCountNoList) {
				for (Map.Entry entry : dartsCourtTypeData.getCourtTypeCaseDataMap(countNo).entrySet()) {
					DartsCaseData caseDataToUse = (DartsCaseData) entry.getValue();
					if (caseDataToUse != null) {
						dartsCourtTypeData.highestPolicyId = getHighestPolicyId(dartsCourtTypeData.highestPolicyId, caseDataToUse.refDarRetentionPolicyId);
						setCasePolicyDuration(caseDataToUse);
						results.add(caseDataToUse);
					}
				}
			}
			return results;
		}
		
		private DartsCaseData calculateCaseDataDuration(DartsCaseData caseData, DartsDisposalLineData disposal2Data) {
			DartsDefendantTotals defendantTotals = caseData.defendantTotalsMap.get(disposal2Data.defendantId);
			if (defendantTotals == null) {
				defendantTotals = new DartsDefendantTotals();	
			}
			
			// Get the duration map
			Map<String, Integer> durationMap = disposal2Data.durationMap;
			
			// Life - Maximum duration
			if (YES.equals(disposal2Data.hasLife)) {
				caseData.hasLife = YES;
			}
			
			// Total the consecutive durations
			if (YES.equals(disposal2Data.isConsecutive)) {
				caseData.isConsecutive = YES;
				
				// Add the durations into the total consecutive duration
				defendantTotals.addConsecutiveDuration(durationMap);
			} else {
				// Greatest duration
				defendantTotals.addGreatestDuration(durationMap);	
			}
			caseData.defendantTotalsMap.put(disposal2Data.defendantId, defendantTotals);
			return caseData;
		}
		
		private DartsCaseData calculateCaseDataPolicy(DartsCaseData caseData, DartsDisposalLineData disposal2Data) {
			// Set the Case Ref Dar Retention Policy Id
			caseData.refDarRetentionPolicyId = getHighestPolicyId(
						caseData.refDarRetentionPolicyId, disposal2Data.dispRetentionPolicy.getRefDarRetentionPolicyId());
			return caseData;
		}
		
		@SuppressWarnings("unchecked")
		private DartsCourtTypeData applyAppealRules(DartsCourtTypeData dartsCourtTypeData) {
			// Loop through the Counts
			if (dartsCourtTypeData.useAppealRules()) {
				dartsCourtTypeData.highestPolicyId = null;
				for (Map.Entry entry : dartsCourtTypeData.appealRuleEnums.entrySet()) {
					Integer countNo = (Integer) entry.getKey();
					List<DartsAppealRetentionRulesEnum> ruleEnums = (List<DartsAppealRetentionRulesEnum>) entry.getValue();
					// Loop through the rules
					for (DartsAppealRetentionRulesEnum ruleEnum : ruleEnums) {
						Integer policyId = null;
						if (DartsAppealRetentionRulesEnum.USE_NG_ACQUITTAL.equals(ruleEnum)) {
							Integer policyNo = DartsVerdictEnum.NG.getRetentionPolicyNo();
							policyId = getPolicyId(policyNo);
						} else if (ruleEnum.getCourtType() != null) {
							policyId = dartsCourtTypeData.getCourtTypePolicyId(countNo, ruleEnum.getCourtType());
						}
						Integer newPolicyId = getHighestPolicyId(dartsCourtTypeData.highestPolicyId, policyId);
						if (dartsCourtTypeData.highestPolicyId != newPolicyId) {
							dartsCourtTypeData.applicableRuleEnum = ruleEnum;
						}
						dartsCourtTypeData.highestPolicyId = newPolicyId;
						
					}
				}
			}
			return dartsCourtTypeData;
		}
		
		private Integer getHighestPolicyId(Integer oldRefDarRetentionPolicyId, Integer newRefDarRetentionPolicyId) {
			if (oldRefDarRetentionPolicyId == null) {
				LOG.debug("getHighestPolicyId() = "+newRefDarRetentionPolicyId);
				return newRefDarRetentionPolicyId;
			} else if (newRefDarRetentionPolicyId != null && oldRefDarRetentionPolicyId != newRefDarRetentionPolicyId) {
				RefDarRetentionPolicies refDarPolicyOld = getPolicy(oldRefDarRetentionPolicyId);
				RefDarRetentionPolicies refDarPolicyNew = getPolicy(newRefDarRetentionPolicyId);
				if (refDarPolicyNew.getPolicyNo() != null && refDarPolicyOld.getPolicyNo() < refDarPolicyNew.getPolicyNo()) {
					LOG.debug("getHighestPolicyId() = "+newRefDarRetentionPolicyId);
					return newRefDarRetentionPolicyId;
				}
			}
			return oldRefDarRetentionPolicyId;
		}
		
		private void setCasePolicyDuration(DartsCaseData caseData) {
			// Loop round the defendant totals and determine which to use
			caseData.durationMap = null;
			for (DartsDefendantTotals defendantTotals : caseData.defendantTotalsMap.values()) {
				if (defendantTotals.getTotalConsecutiveDurationMap() != null && 
					defendantTotals.getTotalConsecutiveDurationMap().size() > 0){
					// Add the greatest durations into the total consecutive duration
					if (defendantTotals.getGreatestDurationMap() != null){
						for (String durationUnit : defendantTotals.getGreatestDurationMap().keySet()) {
							Integer durationValue = defendantTotals.getGreatestDurationMap().get(durationUnit);
							
							Integer durationValueTotal = defendantTotals.getTotalConsecutiveDurationMap().get(durationUnit);
							durationValueTotal = (durationValueTotal == null ? 0 : durationValueTotal) + durationValue;
							defendantTotals.getTotalConsecutiveDurationMap().put(durationUnit, durationValueTotal);	
						}
					}
					
					if (caseData.durationMap == null || 
							DartsRetentionCalcHelper.getDurationInDays(defendantTotals.getTotalConsecutiveDurationMap()) > 
							DartsRetentionCalcHelper.getDurationInDays(caseData.durationMap)) {
						caseData.durationMap = defendantTotals.getTotalConsecutiveDurationMap();
					}
				} else {
					if (caseData.durationMap == null ||
							DartsRetentionCalcHelper.getDurationInDays(defendantTotals.getGreatestDurationMap()) >
					DartsRetentionCalcHelper.getDurationInDays(caseData.durationMap)) {
						caseData.durationMap = defendantTotals.getGreatestDurationMap();
					}
				}
			}
			
			
		}
		
		@SuppressWarnings("unchecked")
		private XhbCourtLogEntry getCourtLogEvent(Integer caseId, Integer eventType) {
			LOG.debug("getCourtLogEvent("+caseId+","+eventType+")");
			XhbCourtLogEntry result = null;
			Collection<XhbCourtLogEntry> courtLogEntries = XhbCourtLogEntryBeanHelper2.findByCaseIdEventTypeToDateDescending(caseId, eventType, new Date());
			if (courtLogEntries != null && courtLogEntries.size() > 0) {
				for (XhbCourtLogEntry courtLogEntry : courtLogEntries) {
					LOG.debug("Found - entryId"+courtLogEntry.getEntryId());
					result = courtLogEntry;
					break;
				}
			}
			return result;
		}
	}
	
	private class DisposalRetentionData extends AbstractRetentionData {
		
		public DisposalRetentionData(
				Integer defendantOnCaseId,
				Integer defendantOnOffenceId,
				List<XhbDisposalLineBasicValue> disposalLines,
				List<RefDarRetentionPolicies> refDarPolicies) {
			super(defendantOnCaseId, defendantOnOffenceId, refDarPolicies);
			processDisposalLines(disposalLines);
		}
	}
	
	private abstract class AbstractRetentionData {
		private static final String MONTH = "month";
		private static final String YEAR = "year";

		public Map<Integer, DartsDisposalLineData> disposalMap = new HashMap<Integer, DartsDisposalLineData>();
		protected List<RefDarRetentionPolicies> refDarPolicies;
		private Integer defendantOnCaseId;
		private Integer defendantOnOffenceId;
		private Integer disposal2Id;
		private String courtType;
		private Integer countNo;
		
		public AbstractRetentionData(Integer defendantOnCaseId, Integer defendantOnOffenceId, 
				List<RefDarRetentionPolicies> refDarPolicies) {
			this.setDefendantOnCaseId(defendantOnCaseId);
			this.setDefendantOnOffenceId(defendantOnOffenceId);
			this.refDarPolicies = refDarPolicies;
		}
		
		protected RefDarRetentionPolicies getPolicy(Integer refDarRetentionPolicyId) {
			for (RefDarRetentionPolicies refDarPolicy : refDarPolicies) {
				if (refDarPolicy.getRefDarRetentionPolicyId().equals(refDarRetentionPolicyId)) {
					return refDarPolicy;
				}
			}
			return null;
		}

		protected Integer getPolicyId(Integer policyNo) {
			for (RefDarRetentionPolicies refDarPolicy : refDarPolicies) {
				if (refDarPolicy.getPolicyNo().equals(policyNo)) {
					return refDarPolicy.getRefDarRetentionPolicyId();
				}
			}
			return null;
		}
		
		protected void processDisposalLines(List<XhbDisposalLineBasicValue> disposalLines) {				
			// Temporary variables
			boolean isComprisingOf = false;
			boolean isExtending = false;
			boolean isConsecutiveLabel = false;
			String durationUnit = null;
			Integer durationValue = null;
			String disposalCode = "";
			
			// Loop through the disposal lines
			for(XhbDisposalLineBasicValue item : disposalLines) {
				if(item.getLineData() != null && !DELETED.equals(item.getLineData()) &&
						!YES.equals(item.getObsInd())) {
					boolean isConsecutive = false;
				
					// Set which disposal2Id we are processing
					setDisposal2Id(item.getDisposal2Id());
					
					// Get the disposal data
					DartsDisposalLineData disposal2Data;
					XhbRefDisposalLineBasicValue refDisposalLine;
					if (disposalMap.containsKey(item.getDisposal2Id())) {
		                disposal2Data = disposalMap.get(item.getDisposal2Id());
		                refDisposalLine = XhbRefDisposalLineBeanHelper2.findByPrimaryKeyValue(item.getRefDisposalLineId());
		            } else {
		            	disposal2Data = new DartsDisposalLineData();
		            	refDisposalLine = XhbRefDisposalLineBeanHelper2.findByPrimaryKeyValue(item.getRefDisposalLineId());
		            	disposal2Data.dispRetentionPolicy = getLocalDisposalRetentionPolicyByDisposalCode(refDisposalLine.getDisposalCode());
						if (disposal2Data.dispRetentionPolicy == null) {
							LOG.error(methodName + " No XHB_REF_DISP_RETENTION_POLICY record found for disposalCode " + refDisposalLine.getDisposalCode());
						    continue;
						}
						disposal2Data.disposal2Id = getDisposal2Id();
						disposal2Data.courtType = getCourtType();
						disposal2Data.countNo = getCountNo();
						disposal2Data.disposalCode = refDisposalLine.getDisposalCode();
						disposal2Data.defendantOnCaseId = getDefendantOnCaseId();
						disposal2Data.defendantOnOffenceId = getDefendantOnOffenceId();
						disposal2Data.defendantId = getDefendantId(disposal2Data.defendantOnCaseId, disposal2Data.defendantOnOffenceId);
						if (YES.equals(disposal2Data.dispRetentionPolicy.getHasLife())) {
							disposal2Data.hasLife = YES;
						}
						if (YES.equals(disposal2Data.dispRetentionPolicy.getHasDuration())) {
							disposal2Data.hasDuration = YES;
						}
		            }
					
					// Reset flags
					if (!disposal2Data.disposalCode.equals(disposalCode) || 
							NO.equals(refDisposalLine.getInputFlag())) {
						isComprisingOf = false;
						isExtending = false;
						// Store the disposal currently being processed
						disposalCode = disposal2Data.disposalCode;
					}
					
					// Flag duration conditions
					if (NO.equals(refDisposalLine.getInputFlag())) {
						isConsecutiveLabel = false;
						if (item.getLineData().toLowerCase().contains("comprising")) {
							isComprisingOf = true;	
						}
						if (item.getLineData().toLowerCase().contains("extension")) {
							isExtending = true;	
						}
						if (refDisposalLine.getLineData().toLowerCase().contains("consecutive")) {
							isConsecutiveLabel = true;	
						}
					}
					
					// Process duration
					if (YES.equals(disposal2Data.hasDuration) && 
							!YES.equals(disposal2Data.hasLife) &&
							!isComprisingOf && !isExtending) {
						if ("TSUSP".equals(disposal2Data.disposalCode)) { 
							 if (Integer.valueOf(20).equals(refDisposalLine.getDilSeqNo()) ||
									 Integer.valueOf(40).equals(refDisposalLine.getDilSeqNo())) {
								durationValue = Integer.parseInt(item.getLineData());
							 } else if (Integer.valueOf(30).equals(refDisposalLine.getDilSeqNo()) ||
									 Integer.valueOf(50).equals(refDisposalLine.getDilSeqNo())) {
								 durationUnit = item.getLineData();
							 }
						}
						else if ("ODIMP".equals(disposal2Data.disposalCode)) { 
							if (Integer.valueOf(80).equals(refDisposalLine.getDilSeqNo()) ||
									 Integer.valueOf(120).equals(refDisposalLine.getDilSeqNo())) {
								durationValue = Integer.valueOf(1);
								durationUnit = DateTimeUtilities.DAY;
							 }
						}
						else if ("SS".equals(disposal2Data.disposalCode)) { 
							 if (Integer.valueOf(40).equals(refDisposalLine.getDilSeqNo())) {
								durationValue = Integer.parseInt(item.getLineData());
							 } else if (Integer.valueOf(50).equals(refDisposalLine.getDilSeqNo())) {
								 durationUnit = item.getLineData();
							 }
						}
						else if ("D5".equals(refDisposalLine.getDbdestin())) {
							durationValue = Integer.parseInt(item.getLineData());
						} 
						else if ("D6".equals(refDisposalLine.getDbdestin())) {
							durationUnit = item.getLineData();
						} 
						else if ("D7".equals(refDisposalLine.getDbdestin())) {
							durationValue = Integer.parseInt(item.getLineData());
						} 
						else if ("D8".equals(refDisposalLine.getDbdestin())) {
							durationUnit = item.getLineData();	
						} 
						else if (refDisposalLine.getDbdestin() == null) {
							// Special Cases
							if ("POPSA".equals(disposal2Data.disposalCode) && 
									Integer.valueOf(100).equals(refDisposalLine.getDilSeqNo())) {
								durationValue = Integer.parseInt(item.getLineData());
								durationUnit = YEAR; 
							} else if ("POPSA".equals(disposal2Data.disposalCode) && 
									Integer.valueOf(140).equals(refDisposalLine.getDilSeqNo())) {
								durationValue = Integer.parseInt(item.getLineData());
								durationUnit = MONTH;
							}
							else if (refDisposalLine.getPrompt() != null && 
									refDisposalLine.getPrompt().toLowerCase().contains("duration")) {
								durationValue = Integer.parseInt(item.getLineData());
							}
							else if (refDisposalLine.getPrompt() != null && 
									refDisposalLine.getPrompt().toLowerCase().contains("unit")) {
								durationUnit = item.getLineData();
							}
						}
					}
					
					// Process Consecutive
					if (YES.equals(disposal2Data.dispRetentionPolicy.getAllowsConsecutive())) {
						if (isConsecutive) {
							disposal2Data.isConsecutive = YES;	
						} else if (item.getLineData() != null && isConsecutiveLabel &&
								item.getLineData().toLowerCase().contains("consecutive")) {
							disposal2Data.isConsecutive = YES;
						} else if ("CUSMSEC".equals(disposal2Data.disposalCode) && 
								(Integer.valueOf(30).equals(refDisposalLine.getDilSeqNo()) ||
								 Integer.valueOf(60).equals(refDisposalLine.getDilSeqNo()))) {
							// CUSMSEC is always consecutive
							disposal2Data.isConsecutive = YES;	
						} else if ("CJCO".equals(disposal2Data.disposalCode) && 
								(Integer.valueOf(260).equals(refDisposalLine.getDilSeqNo()) ||
								 Integer.valueOf(280).equals(refDisposalLine.getDilSeqNo()))) {
							// If defendant / appellant is still unticked then its consecutive
							disposal2Data.isConsecutive = YES;
						} else if ("POCCON".equals(disposal2Data.disposalCode) && 
								(Integer.valueOf(260).equals(refDisposalLine.getDilSeqNo()) ||
								 Integer.valueOf(280).equals(refDisposalLine.getDilSeqNo()))) {
							// If defendant / appellant is still unticked then its consecutive
							disposal2Data.isConsecutive = YES;
						}
					}
					
					// We have a duration pair, so store it
					if (durationUnit != null && durationValue != null) {
						// Add the actual duration unit/value to the map
						Integer durationValueTotal = disposal2Data.durationMap.get(durationUnit);
						durationValueTotal = (durationValueTotal == null ? 0 : durationValueTotal) + durationValue;
						disposal2Data.durationMap.put(durationUnit, durationValueTotal);
						
						// Clear it ready for the next pair
						durationUnit = null;
						durationValue = null;
					}
			
					// Add the data to the map
					disposalMap.put(item.getDisposal2Id(), disposal2Data);
				}
			}
		}
		
		protected Integer getDisposal2Id() {
			return disposal2Id;
		}
		
		protected void setDisposal2Id(Integer disposal2Id) {
			this.disposal2Id = disposal2Id;
		}
		
		protected Integer getDefendantOnCaseId() {
			return defendantOnCaseId;
		}

		protected void setDefendantOnCaseId(Integer defendantOnCaseId) {
			this.defendantOnCaseId = defendantOnCaseId;
		}
		
		protected Integer getDefendantOnOffenceId() {
			return defendantOnOffenceId;
		}

		protected void setDefendantOnOffenceId(Integer defendantOnOffenceId) {
			this.defendantOnOffenceId = defendantOnOffenceId;
		}
		
		protected String getCourtType() {
			return courtType;
		}
		
		protected void setCourtType(String courtType) {
			this.courtType = courtType;
		}
		
		protected Integer getCountNo() {
			return countNo;
		}
		
		protected void setCountNo(Integer countNo) {
			this.countNo = countNo;
		}
	}
	
	private class ConfigPropMaintainer {
		
		public XhbConfigPropBasicValue getConfigPropBasicValue(String propertyName) {
			XhbConfigPropBasicValue[] properties = XhbConfigPropBeanHelper2.findByPropertyNameValue(propertyName);
			if (properties != null) {
				for( XhbConfigPropBasicValue basicValue : properties){
					return basicValue;
				}
			}
			return null;
		}
		
		public String getPropertyValue(String propertyName) {
			XhbConfigPropBasicValue basicValue = getConfigPropBasicValue(propertyName);
			if (basicValue != null) {
				return basicValue.getPropertyValue();
			}
			return null;
		}
	}
}