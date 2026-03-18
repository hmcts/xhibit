package uk.gov.courtservice.xhibit.xmlbinding.orders;



import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.AdditionalNotes;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.DetentionAndTrainingOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.RiskVulnerabilityFactors;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper.ValidationHelper;

/**
 * <p>
 * Title: Helper class that validates the Castor bound java-XML object for the
 * Detention&TrainingOrderStructure< /p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Validates the DetentionAndTrainingStructure for 'Detention & Training Orders'.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Brian Hingston
 * @version 1.0
 */

public class DTOrderValidationHelper {
    private static final Logger log = CSServices.getLogger(DTOrderValidationHelper.class);

    private static final String DETENTION_PERIOD_NON_ZERO_MESSAGE = "ORDER_Detention_Period_must_be_non_zero";

    private static final String SECTION_105_NON_ZERO_MESSAGE = "ORDER_Section105_Period_must_be_non_zero";

    private static final String ADDITIONAL_NOTES_MESSAGE = "ORDER_Additional_Notes_incorrect";
    
    private static final String TRIAL_MONITORING_DURATION_EXCEPTION_MESSAGE = "ORDER_Trial_Monitoring_duration_empty";
    
    private static final String RISK_OR_VULNERABILITY_EMPTY_MESSAGE = "ORDER_Risk_Vulnerability_emptyt";

    /**
     * Utility methodValidation
     * 
     * @param dtos
     * @param typeCode
     * @param helper
     */
    public static void logicalValidateDTOrder(DetentionAndTrainingOrderStructure dtos,
            OrderXMLHelper.ValidationHelper helper) {
        log.debug("*****************logicalValidateDTOrder");
        
        validateRiskOrVulneability(dtos, helper );
        
        validatePeriodOfDetention(dtos, helper);
        
        //check section 105 period if present
        if (dtos.getOrderPeriod().getSection105().getSelected() &&
                dtos.getOrderPeriod().getSection105().hasSelected()){
            validateSection105Period(dtos, helper);
        }
        
        validateTrailMonitoringRequirement(dtos, helper);
        validateAdditionalNotes(dtos, helper);


    }

    /**
     * Validate the Risk or Vulnerability factors. If selected, text must be entered
     * @param dtos		The Order structure
     * @param helper	The message helper
     */
    private static void validateRiskOrVulneability(DetentionAndTrainingOrderStructure dtos, ValidationHelper helper) {
		//	Check ro see if Risk or vulnearbility ticked.
    	RiskVulnerabilityFactors riskOrVulnerabilityFactors = dtos.getRiskVulnerabilityFactors();
    	
    	if ( riskOrVulnerabilityFactors.getSelected())
    	{
    		if ( riskOrVulnerabilityFactors.getRiskText().trim().isEmpty() ){
    			helper.addLogicalProblem(RISK_OR_VULNERABILITY_EMPTY_MESSAGE, null);
    		}
    	}
	}

	private static void validateTrailMonitoringRequirement(DetentionAndTrainingOrderStructure dtos, ValidationHelper helper) {
        log.debug("$$$ validateTrailMonitoringRequirement() ");

        if (dtos.getDTOrderRequirements().getTrailMonitoringRequirement().getSelected()
                && isEmptyString(dtos.getDTOrderRequirements().getTrailMonitoringRequirement().getDuration())) {
            log.debug("*************** PROBLEM ADDED");
            helper.addLogicalProblem(TRIAL_MONITORING_DURATION_EXCEPTION_MESSAGE, null);
        }           
    }
    
    /**
     * Check that the Section 105 Period is not zero
     * 
     * @param dtos
     *            the detention and training order
     * @param helper
     *            the helper
     */
    private static void validateSection105Period(DetentionAndTrainingOrderStructure dtos, ValidationHelper helper) {
        log.debug("*****************validateSection105Period");
                
        XhibitPeriodHelper.validatePeriodNonZero(dtos.getOrderPeriod().getSection105().getPeriod(), helper, 
                    SECTION_105_NON_ZERO_MESSAGE);
        
    }
    
    

    /**
     * Check that the Period of Detention is not zero
     * 
     * @param dtos
     *            the detention & training order
     * @param helper
     *            the helper
     */
    private static void validatePeriodOfDetention(DetentionAndTrainingOrderStructure dtos, ValidationHelper helper) {
        log.debug("*****************validatePeriodOfDetention");
        validatePeriod(dtos, helper);

        
    }

    

    /**
     * Check that the period is not zero
     * 
     * @param dtos
     *            the detention & training order
     * @param helper
     *            the helper
     */
    private static void validatePeriod(DetentionAndTrainingOrderStructure dtos, ValidationHelper helper) {
        log.debug("*****************validatePeriod");
        // Validate the term.
        XhibitPeriodHelper.validatePeriodNonZero(dtos.getOrderPeriod().getPeriod(), helper,
                DETENTION_PERIOD_NON_ZERO_MESSAGE);
    }

    

    /**
     * Validate the Additional Notes Option
     * 
     * @param dtos
     *            the detention and training order
     * @param helper
     *            the helper
     */
    private static void validateAdditionalNotes(DetentionAndTrainingOrderStructure dtos, ValidationHelper helper) {
        AdditionalNotes notes = dtos.getAdditionalNotes();
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