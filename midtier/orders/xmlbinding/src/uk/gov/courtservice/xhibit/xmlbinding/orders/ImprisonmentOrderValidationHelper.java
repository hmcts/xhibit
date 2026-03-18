package uk.gov.courtservice.xhibit.xmlbinding.orders;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.AdditionalNotes;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.ImprisonmentOrder5035CStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.LifeSentence;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.RiskVulnerabilityFactors;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.ImprisonmentTypeType;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.Section250TermTypeType;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.Section91TermTypeType;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper.ValidationHelper;

/**
 * <p>
 * Title: Helper class that validates the Castor bound java-XML object for the
 * ImprisonmentOrderStructure< /p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Validates the ImprisonmentOrderStructure for 'Commit to Young Offender
 * Institution' and 'Order for Imprisonment'.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class ImprisonmentOrderValidationHelper {
    private static final Logger log = CSServices.getLogger(ImprisonmentOrderValidationHelper.class);

    private static final String NA = "Not Applicable";
    
    private static final String DISCRETIONARY_LIFE_NON_ZERO_MESSAGE = "ORDER_Discretionary_Life_Sentence_must_be_non_zero";

    private static final String CUSTODIAL_SENTENCE_NON_ZERO_MESSAGE = "ORDER_Custodial_Sentence_must_be_non_zero";

    private static final String RETURN_PERIOD_NON_ZERO_MESSAGE = "ORDER_Return_Period_must_be_non_zero";

    private static final String CREDIT_FOR_REMAND_NON_ZERO_MESSAGE = "ORDER_Credit_For_Remand_must_be_non_zero";

    private static final String CREDIT_FOR_BAIL_NON_ZERO_MESSAGE = "ORDER_Credit_For_Bail_must_be_non_zero";

    private static final String TOTAL_RETURN_PERIOD_NON_ZERO_MESSAGE = "ORDER_Total_Return_Period_must_be_non_zero";

    private static final String TOTAL_RETURN_PERIOD_MAX_MESSAGE = "ORDER_Total_Return_Period_exceeded";

    private static final String ADDITIONAL_NOTES_MESSAGE = "ORDER_Additional_Notes_incorrect";
    
    private static final String EXTENDED_SENTENCE_CUSTODIAL_NON_ZERO_MESSAGE = "ORDER_Extended_Sentence_Custodial_must_be_non_zero";
    
    private static final String EXTENDED_SENTENCE_EXTENSION_NON_ZERO_MESSAGE = "ORDER_Extended_Sentence_Extension_must_be_non_zero";
    
    private static final String IO_NO_PERIOD_SELECTED = "ORDER_IO_No_Period_option_selected";
    
    private static final String YOI_NO_PERIOD_SELECTED = "ORDER_YOI_No_Period_option_selected";
    private static final String YO5044C_NO_PERIOD_SELECTED = "ORDER_YO5044C_No_Period_option_selected";
    
    private static final String YOI_SENTENCE_CUSTODIAL_NON_ZERO_MESSAGE = "ORDER_YOI_Sentence_Custodial_must_be_non_zero";
    private static final String YO5044C_SENTENCE_CUSTODIAL_NON_ZERO_MESSAGE = "ORDER_YO5044C_Sentence_Custodial_must_be_non_zero";
    private static final String YO5044D_SENTENCE_CUSTODIAL_NON_ZERO_MESSAGE = "ORDER_YO5044D_Sentence_Custodial_must_be_non_zero";
    
    private static final String YOI_SENTENCE_EXTENSION_NON_ZERO_MESSAGE = "ORDER_YOI_Sentence_Extension_must_be_non_zero";
    private static final String YO5044D_SENTENCE_EXTENSION_NON_ZERO_MESSAGE = "ORDER_YO5044D_Sentence_Extension_must_be_non_zero";
    private static final String YO5044C_SENTENCE_EXTENSION_NON_ZERO_MESSAGE = "ORDER_YO5044C_Sentence_Extension_must_be_non_zero";
    
    private static final String YOI_CONSECUTIVE_CONCURRENT_CONFLICT = "ORDER_YOI_consecutive_concurrent_cannot_be_set";
    private static final String YO5044D_CONSECUTIVE_CONCURRENT_CONFLICT = "ORDER_YO5044D_consecutive_concurrent_cannot_be_set";
    private static final String YO5044C_CONSECUTIVE_CONCURRENT_CONFLICT = "ORDER_YO5044C_consecutive_concurrent_cannot_be_set";
    
    private static final String YOI_CONSECUTIVE_CONCURRENT_NOT_SET = "ORDER_YOI_consecutive_concurrent_must_be_set";
    private static final String YO5044C_CONSECUTIVE_CONCURRENT_NOT_SET = "ORDER_YO5044C_consecutive_concurrent_must_be_set";
    private static final String YO5044D_CONSECUTIVE_CONCURRENT_NOT_SET = "ORDER_YO5044D_consecutive_concurrent_must_be_set";
    
    private static final String YOI_CONSECUTIVE_CONCURRENT_BOTH_SET = "ORDER_YOI_consecutive_concurrent_both_set";
    private static final String YO5044C_CONSECUTIVE_CONCURRENT_BOTH_SET = "ORDER_YO5044C_consecutive_concurrent_both_set";
    private static final String YO5044D_CONSECUTIVE_CONCURRENT_BOTH_SET = "ORDER_YO5044D_consecutive_concurrent_both_set";
    
    private static final String CUSTODIAL_CONSECUTIVE_CONCURRENT_CONFLICT = "ORDER_Custodial_consecutive_concurrent_cannot_be_set";
    private static final String CUSTODIAL_YO5044C_CONSECUTIVE_CONCURRENT_CONFLICT = "ORDER_Custodial_YO5044C_consecutive_concurrent_cannot_be_set";
    private static final String CUSTODIAL_YO5044D_CONSECUTIVE_CONCURRENT_CONFLICT = "ORDER_Custodial_YO5044D_consecutive_concurrent_cannot_be_set";
        
    private static final String LIFE_SENTENCE_MULTIPLE_SECTIONS_SELECTED = "ORDER_Life_Sentence_can_not_select_multiple_act_sections";
    
    private static final String LIFE_SENTENCE_MINIMUM_PERIOD_NON_ZERO_MESSAGE = "ORDER_Life_Sentence_Minimum_Term_must_be_non_zero";

    private static final String SECTION91_SENTENCE_NON_ZERO_MESSAGE = "ORDER_Section91_Sentence_must_be_non_zero";
    
    private static final String SECTION250_SENTENCE_NON_ZERO_MESSAGE = "ORDER_Section250_Sentence_must_be_non_zero";
    
    private static final String HMPLEASURE_SENTENCE_NON_ZERO_MESSAGE = "ORDER_HMPleasure_Sentence_must_be_non_zero";
    
    private static final String RISK_VULNERABILITY_MUST_EXIST = "ORDER_Custodial_risk_or_vulnerability_must_have_text";
    
    private static final int MAX_TOTAL_PERIOD_RETURN = 12;

    /**
     * Utility methodValidation
     * 
     * @param ios
     * @param typeCode
     * @param helper
     */
    public static void logicalValidateImpOrder(ImprisonmentOrderCommonStructure iocs, String typeCode,
            OrderXMLHelper.ValidationHelper helper) {
        log.debug("*****************logicalValidateImpOrder");
        ImprisonmentTypeType imprisonmentTypeType = iocs.getCustodialSentence().getImprisonmentType();
        //Section91TermType section91TermType = iocs.getCustodialSentence().getSection91TermType();

        if ( // Is a period of imprisonment.
        imprisonmentTypeType.equals(ImprisonmentTypeType.PERIOD)) // period
                // of
                // imprisonment
        // term selected
        {
            validatePeriodOfImprisonment(iocs, helper, typeCode);
        } else if ( // Is a life sentence OR a Section 93/94 case
        iocs.getCustodialSentence().getImprisonmentType().equals(ImprisonmentTypeType.LIFE)
                || iocs.getCustodialSentence().getImprisonmentType().equals(ImprisonmentTypeType.SECTION9394)) {
            // Check the period entered is not zero
            validateLifeSentence(iocs, helper, typeCode);
        } else if ( // Is an extended Sentence (Legal Aid Sentencing Bill change)
        iocs.getCustodialSentence().getImprisonmentType().equals(ImprisonmentTypeType.EXTENDED)) {
            //Check the periods entered aren't zero
            validateNewExtendedSentence(iocs, helper);
        }else if ( // Is Section 91 with term (Legal Aid Sentencing Bill Change)
        iocs.getCustodialSentence().getImprisonmentType().equals(ImprisonmentTypeType.SECTION91) && 
            iocs.getCustodialSentence().getSection91TermType().getContent().equals(Section91TermTypeType.SECTION91TERM)){
            //Check the periods entered aren't zero
            validateSection91Sentence(iocs, helper);
        }else if ( // Is Section 250 with term (Sentencing Act 2020 Change)
        iocs.getCustodialSentence().getImprisonmentType().equals(ImprisonmentTypeType.SECTION250) && 
            iocs.getCustodialSentence().getSection250TermType().getTermType().equals(Section250TermTypeType.SECTION250TERM)){
            //Check the periods entered aren't zero
            validateSection250Sentence(iocs, helper);
        }else if ( // Is HMPleasure with term (Legal Aid Sentencing Bill Change)
        imprisonmentTypeType.equals(ImprisonmentTypeType.HMPLEASURE) && 
            iocs.getCustodialSentence().getLifeSentence().getMinimumTerm().getSelected() &&
            iocs.getCustodialSentence().getLifeSentence().getMinimumTerm().hasSelected() &&
            !iocs.getCustodialSentence().getLifeSentence().getMinimumTerm().getNoMinimumTerm().getSelected()){
            //Check the periods entered aren't zero
            validateHMPleasureSentence(iocs, helper);
        }

        validateReturnOfDefendantsSection(iocs, helper);

        validateAdditionalNotes(iocs, helper);

        if (typeCode.equalsIgnoreCase("IO5035C") || typeCode.equalsIgnoreCase("YO5044C")
                || typeCode.equalsIgnoreCase("YO5044D")) {
        	validateRiskOrVulnerability((ImprisonmentOrder5035CStructure)iocs, helper);
            validateCreditPeriod((ImprisonmentOrder5035CStructure) iocs, helper);
        }
    }

    /**
     * If the imprisonment order is a IO5035C, validate the risk or vulnerability statement.
     * 
     * @param 	iocs					The imprisonment order
     * @param 	helper					The helper
     */
    private static void validateRiskOrVulnerability(ImprisonmentOrder5035CStructure iocs, ValidationHelper helper) {
		if ( iocs != null && helper != null){
			RiskVulnerabilityFactors riskVulnerabilityFactors = iocs.getRiskVulnerabilityFactors();
			
			if ( riskVulnerabilityFactors != null ){
				//	Only check for the text if the selected flag is false.
				if ( riskVulnerabilityFactors.getSelected()){
					if ( riskVulnerabilityFactors.getRiskText().trim().isEmpty()){
						log.debug("Empty risk string when selected.");
						
						helper.addLogicalProblem(RISK_VULNERABILITY_MUST_EXIST, null);
					}
				}
			}
		}
		
	}

	/**
     * Check that the Life Sentence Period is not zero
     * 
     * @param ios
     *            the imprisonment order
     * @param helper
     *            the helper
     */
    private static void validateLifeSentence(ImprisonmentOrderCommonStructure iocs, ValidationHelper helper, String typeCode) {
        log.debug("*****************validateLifeSentence");
        // If section 28 has been selected.
        if (iocs.getSection28().hasSelected() && iocs.getSection28().getSelected()) {
            // Validate discretionary relevant period.
            XhibitPeriodHelper.validatePeriodNonZero(iocs.getSection28().getDiscretionaryRelevantPart(), helper,
                    DISCRETIONARY_LIFE_NON_ZERO_MESSAGE);
        }
        
        //	Check that multiple sections 224a/225/283/285 have not been selected
        LifeSentence lifeSentence = iocs.getCustodialSentence().getLifeSentence();
        long sectionsSet = 0L;
        
        if ( typeCode.equals("IO5035C")){
        sectionsSet += lifeSentence.getSection224a() ? 1 : 0;
        sectionsSet += lifeSentence.getSection225() ? 1 :0;
        sectionsSet += lifeSentence.getSection283() ? 1 : 0;
        sectionsSet += lifeSentence.getSection285() ? 1 : 0;
        } 
        else if ( typeCode.equals("YO5044D")){
 	        sectionsSet += lifeSentence.getSection224a() ? 1 : 0;
	        sectionsSet += lifeSentence.getSection225() ? 1 :0;
	        sectionsSet += lifeSentence.getSection272() ? 1: 0;
	        sectionsSet += lifeSentence.getSection273() ? 1 : 0;
	        sectionsSet += lifeSentence.getSection274() ? 1 :0;
	        sectionsSet += lifeSentence.getSection275() ? 1 :0;
	        sectionsSet += lifeSentence.getSection93() ? 1 : 0;
	        sectionsSet += lifeSentence.getSection94() ? 1 : 0;
        }
        
        if ( sectionsSet > 1){
            helper.addLogicalProblem(LIFE_SENTENCE_MULTIPLE_SECTIONS_SELECTED, null);
        }
        
        if (iocs.getCustodialSentence().getLifeSentence().getMinimumTerm().getSelected() &&
                iocs.getCustodialSentence().getLifeSentence().getMinimumTerm().hasSelected() &&
                !iocs.getCustodialSentence().getLifeSentence().getMinimumTerm().getNoMinimumTerm().getSelected()){
            // Validate minimum term period.
            XhibitPeriodHelper.validatePeriodNonZero(iocs.getCustodialSentence().getLifeSentence().getMinimumTerm().getLifePeriod(), helper, 
                    LIFE_SENTENCE_MINIMUM_PERIOD_NON_ZERO_MESSAGE);
        }
    }
    
    /**
     * Check that the Section 91 Period of Imprisonment is not zero
     * 
     * @param ios
     *            the imprisonment order
     * @param helper
     *            the helper
     */
    private static void validateSection91Sentence(ImprisonmentOrderCommonStructure iocs, ValidationHelper helper) {
        log.debug("*****************validateSection91Sentence");
        XhibitPeriodHelper.validatePeriodNonZero(iocs.getCustodialSentence().getLifeSentence()
                .getMinimumTerm().getLifePeriod(), helper, SECTION91_SENTENCE_NON_ZERO_MESSAGE);

    }
    
    /**
     * Check that the Section 250 Period of Imprisonment is not zero
     * 
     * @param ios
     *            the imprisonment order
     * @param helper
     *            the helper
     */
    private static void validateSection250Sentence(ImprisonmentOrderCommonStructure iocs, ValidationHelper helper) {
        log.debug("*****************validateSection250Sentence");
        XhibitPeriodHelper.validatePeriodNonZero(iocs.getCustodialSentence()
        		.getSection250TermType().getTerm(), helper, SECTION250_SENTENCE_NON_ZERO_MESSAGE);

    }
    
    /**
     * Check that the HMPLeasure Minimum Period of Imprisonment is not zero
     * 
     * @param ios
     *            the imprisonment order
     * @param helper
     *            the helper
     */
    private static void validateHMPleasureSentence(ImprisonmentOrderCommonStructure iocs, ValidationHelper helper) {
        log.debug("*****************validateHMPleasureSentence");
        XhibitPeriodHelper.validatePeriodNonZero(iocs.getCustodialSentence().getLifeSentence()
                .getMinimumTerm().getLifePeriod(), helper, HMPLEASURE_SENTENCE_NON_ZERO_MESSAGE);

    }

    /**
     * Check that the Period of Imprisonment is not zero
     * 
     * @param ios
     *            the imprisonment order
     * @param helper
     *            the helper
     */
    private static void validatePeriodOfImprisonment(ImprisonmentOrderCommonStructure iocs, ValidationHelper helper, String typeCode) {
        log.debug("*****************validatePeriodOfImprisonment");
        
		//Check that either custodial or s235/236 option hase been selected
		if(!iocs.getCustodialSentence().getS235236().getSelected()
				&&!iocs.getCustodialSentence().getCustodialTerm().getSelected()){
				
			if(typeCode.equals("YO5044D")){
				helper.addLogicalProblem(YOI_NO_PERIOD_SELECTED, null);
			}
			if (typeCode.equals("YO5044C")){
				helper.addLogicalProblem(YO5044C_NO_PERIOD_SELECTED, null);
			}
			if(typeCode.equals("IO5035C")){
				helper.addLogicalProblem(IO_NO_PERIOD_SELECTED, null);
			}
			
		}

        if (iocs.getCustodialSentence().getCustodialTerm().hasSelected() 
            && iocs.getCustodialSentence().getCustodialTerm().getSelected()){
			//Validate custodial term
			XhibitPeriodHelper.validateExtendedPeriodNonZero(iocs.getCustodialSentence().getCustodialTerm(), helper, 
				CUSTODIAL_SENTENCE_NON_ZERO_MESSAGE);
			
			//Check that consecutive/concurrent flags not set if s235/236 not entered
			if(!iocs.getCustodialSentence().getCustodialTerm().getTermType().toString().equals(NA)
				&& !iocs.getCustodialSentence().getS235236().getSelected()){
				helper.addLogicalProblem(
						typeCode.equals("YO5044C") ? CUSTODIAL_YO5044C_CONSECUTIVE_CONCURRENT_CONFLICT :
						typeCode.equals("YO5044D") ? CUSTODIAL_YO5044D_CONSECUTIVE_CONCURRENT_CONFLICT :
						CUSTODIAL_CONSECUTIVE_CONCURRENT_CONFLICT, null);
			}
        }

        if (iocs.getCustodialSentence().getS235236().hasSelected()
                && iocs.getCustodialSentence().getS235236().getSelected()){
                // validate the two s236 sentence terms.
                XhibitPeriodHelper.validatePeriodNonZero(iocs.getCustodialSentence().getS235236().getExtensionPeriod(), helper,
                		typeCode.equals("IO5035C") ? YOI_SENTENCE_EXTENSION_NON_ZERO_MESSAGE : 
                			typeCode.equals("YO5044C") ? YO5044C_SENTENCE_EXTENSION_NON_ZERO_MESSAGE :
                			typeCode.equals("YO5044D") ? YO5044D_SENTENCE_EXTENSION_NON_ZERO_MESSAGE : "");
                XhibitPeriodHelper.validatePeriodNonZero(iocs.getCustodialSentence().getS235236().getCustodialPeriod(), helper, 
                    typeCode.equals("IO5035C") ? YOI_SENTENCE_CUSTODIAL_NON_ZERO_MESSAGE :
                    	typeCode.equals("YO5044C") ? YO5044C_SENTENCE_CUSTODIAL_NON_ZERO_MESSAGE :
                    	typeCode.equals("YO5044D") ? YO5044D_SENTENCE_CUSTODIAL_NON_ZERO_MESSAGE : "");
            
                //Check that consecutive/concurrent flags is not set if custodial option not selected.
                if(!iocs.getCustodialSentence().getS235236().getTermType().toString().equals(NA) 
                        && !iocs.getCustodialSentence().getCustodialTerm().getSelected()){
                    helper.addLogicalProblem(
                    		typeCode.equals("YO5044C") ? YO5044C_CONSECUTIVE_CONCURRENT_CONFLICT :
                    		typeCode.equals("YO5044D") ? YO5044D_CONSECUTIVE_CONCURRENT_CONFLICT :
                    		YOI_CONSECUTIVE_CONCURRENT_CONFLICT, null);
                }
        }
        
		if (iocs.getCustodialSentence().getS235236().hasSelected()
				&& iocs.getCustodialSentence().getS235236().getSelected()
				&&iocs.getCustodialSentence().getCustodialTerm().hasSelected() 
				&& iocs.getCustodialSentence().getCustodialTerm().getSelected()){
			
			// Check that consecutive/concurrent flags for both options are not set if custodial option selected.
			if(!iocs.getCustodialSentence().getS235236().getTermType().toString().equals(NA) 
					&& !iocs.getCustodialSentence().getCustodialTerm().getTermType().toString().equals(NA)){
				helper.addLogicalProblem(
						typeCode.equals("YO5044C") ? YO5044C_CONSECUTIVE_CONCURRENT_BOTH_SET :
						typeCode.equals("YO5044D") ? YO5044D_CONSECUTIVE_CONCURRENT_BOTH_SET :
						YOI_CONSECUTIVE_CONCURRENT_BOTH_SET, null);
			}
			
			// Check that consecutive/concurrent flags of at least one option is set if custodial option selected.
			if(iocs.getCustodialSentence().getS235236().getTermType().toString().equals(NA) 
					&& iocs.getCustodialSentence().getCustodialTerm().getTermType().toString().equals(NA)){
				helper.addLogicalProblem(
						typeCode.equals("YO5044C") ? YO5044C_CONSECUTIVE_CONCURRENT_NOT_SET :
						typeCode.equals("YO5044D") ? YO5044D_CONSECUTIVE_CONCURRENT_NOT_SET :
						YOI_CONSECUTIVE_CONCURRENT_NOT_SET, null);
			}
		}
        
        
        
    }
    
    
    /**
     * Check that the revised Extended Sentence period is not zero
     * Legal Aid Sentencing Bill change
     * 
     * @param ios
     *            the imprisonment order
     * @param helper
     *            the helper
     */
    private static void validateNewExtendedSentence(ImprisonmentOrderCommonStructure iocs, ValidationHelper helper) {
        log.debug("*****************validateNewExtendedSentence");
        //validate the two extended sentence terms.
        XhibitPeriodHelper.validatePeriodNonZero(iocs.getCustodialSentence().getExtendedSentence().getExtensionPeriod(), helper,
                EXTENDED_SENTENCE_EXTENSION_NON_ZERO_MESSAGE);
        XhibitPeriodHelper.validatePeriodNonZero(iocs.getCustodialSentence().getExtendedSentence().getCustodialPeriod(), helper, 
                EXTENDED_SENTENCE_CUSTODIAL_NON_ZERO_MESSAGE);
        
    }


    /**
     * Check that the Credit for Remand or Bail period is not zero
     * 
     * @param ios
     *            the imprisonment order
     * @param helper
     *            the helper
     */
    private static void validateCreditPeriod(ImprisonmentOrder5035CStructure ios, ValidationHelper helper) {
        log.debug("*****************validateCreditPeriod");
        // If section 28 has been selected.
        if (ios.getCreditForBail().hasSelected() && ios.getCreditForBail().getSelected()) {
            // Validate discretionary relevant period.
            XhibitPeriodHelper.validatePeriodNonZero(ios.getCreditForBail().getTerm(), helper,
                    CREDIT_FOR_BAIL_NON_ZERO_MESSAGE);
        }
        // If section 28 has been selected.
        if (ios.getCreditForRemand().hasSelected() && ios.getCreditForRemand().getSelected()) {
            // Validate discretionary relevant period.
            XhibitPeriodHelper.validatePeriodNonZero(ios.getCreditForRemand().getTerm(), helper,
                    CREDIT_FOR_REMAND_NON_ZERO_MESSAGE);
        }
    }


    /**
     * Check that the Return of Defendants Section is valid
     * 
     * @param ios
     *            the imprisonment order
     * @param helper
     *            the helper
     */
    private static void validateReturnOfDefendantsSection(ImprisonmentOrderCommonStructure iocs, ValidationHelper helper) {
        log.debug("*****************validateReturnOfDefendants");
        if (iocs.getReturnToImprisonment().hasSelected() && iocs.getReturnToImprisonment().getSelected()) {
            validateReturnOfDefendants(iocs, helper);

            validateTotalPeriodOfReturn(iocs, helper);
        }

    }

    /**
     * Check that the Total Period of Return is not zero
     * 
     * @param ios
     *            the imprisonment order
     * @param helper
     *            the helper
     */
    private static void validateTotalPeriodOfReturn(ImprisonmentOrderCommonStructure iocs, ValidationHelper helper) {
        log.debug("*****************validateTotalPeriodOfReturn");
        BigDecimal totalPeriodValue = iocs.getReturnToImprisonment().getTotalPeriodOfReturn().getContent();
        int totalPeriod = totalPeriodValue == null ? 0 : totalPeriodValue.intValue();

        if (iocs.getReturnToImprisonment().getTotalPeriodOfReturn().getSelected()) {
            if (iocs.getReturnToImprisonment().getTotalPeriodOfReturn().getSelected() && totalPeriod == 0) {
                log.debug("*************** PROBLEM ADDED");
                log.debug("Total Period of Return is ZERO");
                helper.addLogicalProblem(TOTAL_RETURN_PERIOD_NON_ZERO_MESSAGE, null);
            } else if (iocs.getReturnToImprisonment().getTotalPeriodOfReturn().getSelected()
                    && totalPeriod > MAX_TOTAL_PERIOD_RETURN) {
                log.debug("*************** PROBLEM ADDED");
                log.debug("Total Period of Return = " + totalPeriod);
                String[] messages = { Integer.toString(MAX_TOTAL_PERIOD_RETURN) };
                helper.addLogicalProblem(TOTAL_RETURN_PERIOD_MAX_MESSAGE, messages);
            }
        }
    }

    /**
     * Check that the Return of Defendants period is not zero
     * 
     * @param ios
     *            the imprisonment order
     * @param helper
     *            the helper
     */
    private static void validateReturnOfDefendants(ImprisonmentOrderCommonStructure iocs, ValidationHelper helper) {
        log.debug("*****************validateReturnOfDefendants");
        final String NO_STRING_VALUE = "no";
        if (iocs.getReturnToImprisonment().getReturnPeriod().getMax116().equals(NO_STRING_VALUE)
                && iocs.getReturnToImprisonment().getReturnPeriod().getPeriodInMonths() == 0) {
            log.debug("*************** PROBLEM ADDED");
            helper.addLogicalProblem(RETURN_PERIOD_NON_ZERO_MESSAGE, null);
        }
    }

    /**
     * Validate the Additional Notes Option
     * 
     * @param ios
     *            the imprisonment order
     * @param helper
     *            the helper
     */
    private static void validateAdditionalNotes(ImprisonmentOrderCommonStructure iocs, ValidationHelper helper) {
        AdditionalNotes notes = iocs.getAdditionalNotes();
        // check to ensure that the additional notes option has been selected
        if (notes.hasSelected() && notes.getSelected()) {
            log.debug("*****************AdditionalNotes Selected");
            // check to ensure that the Additional Notes have been set
            if (isEmptyString(notes.getContent())) {
                // No additional notes entered
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(ADDITIONAL_NOTES_MESSAGE, null);
            }
        }
    }

    // Utility Methods

    /**
     * Checks if a passed string is null, empty or a single space
     * 
     * @param value
     *            the string
     * @return true if string is null, empty or a single space
     */
    private static boolean isEmptyString(String value) {
        boolean result = false;
        if (value == null // if string is null
                || // OR
                value.equals("") // empty string
                || // OR
                value.equals(" ")) // single space
        {
            result = true;
        }
        return result;
    }
}