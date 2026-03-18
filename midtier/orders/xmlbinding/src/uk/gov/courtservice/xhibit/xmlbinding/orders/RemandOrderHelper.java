package uk.gov.courtservice.xhibit.xmlbinding.orders;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.RemandOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper.ValidationHelper;

/**
 * 
 * <p>
 * Title: RemandOrderHelper
 * </p>
 * <p>
 * Description: Helper class to validate a Remand Order
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */
public class RemandOrderHelper {
    private static final Logger log = CSServices.getLogger(RemandOrderHelper.class);

    private static final String REPORT_DETAILS_MESSAGE = "ORDER_RO_Report_Details_incorrect";

    private static final String ADDITIONAL_NOTES_DETAILS_MESSAGE = "ORDER_RO_Additional_Notes_incorrect";

    private static final String REMAND_ESTABLISHMENT_MESSAGE = "ORDER_RO_Remand_Establishment_incorrect";
    
    private static final String DESIGNATED_LOCAL_AUTHORUTY_MESSAGE = "ORDER_RO_Designated_Local_Authority_Missing";
    
    private static final String REQUIREMENTS_ON_LOCAL_AUTHORUTY_MESSAGE = "ORDER_RO_Requirements_on_Local_Authority_Missing";
    
    private static final String CONDITIONS_ON_DEFENDANT_MESSAGE = "ORDER_RO_Conditions_on_Defendant_Missing";
    
    private static final String CHARGED_WITH_DETAILS_MESSAGE = "ORDER_RO_Charged_With_Empty";
    
    private static final String LOCAL_AUTHORITY = "localauthority";
    
    private static final String YOUTH_DETENTION = "youthdetention";
    
    private static final String REASONS_GIVEN_MISSING = "ORDER_RO_Reasons_Given_In_Court_Missing";
    private static final String NECESSITY_CONDITION_MISSING = "ORDER_RO_Reason_Necessity_Condition_Missing";
    

    /**
     * Utility methodValidation
     * 
     * @param ros
     * @param typeCode
     * @param helper
     */
    public static void logicalValidateRemandOrder(RemandOrderStructure ros, String typeCode,
            OrderXMLHelper.ValidationHelper helper) {
        log.debug("********************Remand Order: Logical Validation");
        validateItWasOrdered(ros, helper);
        validateAdditionalNotes(ros, helper);
        validateChargedWith(ros, helper);
        validateReasons(ros, helper);
    }

    /**
     * Validate the It Was Ordered Section
     * 
     * @param ros
     *            the Remand Order
     * @param helper
     *            the helper
     */
    private static void validateItWasOrdered(RemandOrderStructure ros, ValidationHelper helper) {
        log.debug("*******************validateItWasOrdered");
        validateRemandEstablishment(ros, helper);
        validateReportSection(ros, helper);
        validateRemanOrderType(ros, helper);

    }

    /**
     * Validate the Report Section
     * 
     * @param ros
     *            the Remand Order
     * @param helper
     *            the helper
     */
    private static void validateRemandEstablishment(RemandOrderStructure ros, ValidationHelper helper) {
        // check to ensure that the Remand Establishment has been entered
        log.debug("*****************Remand Establishment");
        if (isEmptyString(ros.getCustodyLocation())) {
            // No remand establishment entered
            log.debug("*************** PROBLEM ADDED");
            helper.addLogicalProblem(REMAND_ESTABLISHMENT_MESSAGE, null);
        }
    }

    /**
     * Validate the Report Section
     * 
     * @param ros
     *            the Remand Order
     * @param helper
     *            the helper
     */
    private static void validateReportSection(RemandOrderStructure ros, ValidationHelper helper) {
        if (ros.getReportDetails().hasSelected() && ros.getReportDetails().getSelected()) {
            // check to ensure that the Report Details has been selected
            log.debug("*****************Report Option Selected");
            // check to ensure that the Report details have been set
            if (isEmptyString(ros.getReportDetails().getContent())) {
                // No report details entered
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(REPORT_DETAILS_MESSAGE, null);
            }
        }
    }

    /**
     * Validate the Additional Notes
     * 
     * @param ros
     *            the Remand Order
     * @param helper
     *            the helper
     */
    private static void validateAdditionalNotes(RemandOrderStructure ros, ValidationHelper helper) {
        if (ros.getAdditionalInfo().hasSelected() && ros.getAdditionalInfo().getSelected()) {
            // check to ensure that the Additional Notes condition has been
            // selected
            log.debug("*****************Additional Notes Selected");
            // check to ensure that the notes details have been set
            if (isEmptyString(ros.getAdditionalInfo().getContent())) {
                // No notes details entered
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(ADDITIONAL_NOTES_DETAILS_MESSAGE, null);
            }
        }
    }
    
    /**
     * Validate the ChargedWith
     * Added 17/07/2014 for L-R-4224-01 changes associated with LASBO
     * 
     * @param ros
     *            the Remand Order
     * @param helper
     *            the helper
     */
    private static void validateChargedWith(RemandOrderStructure ros, ValidationHelper helper) {
        if (ros.getChargedWith().hasSelected() && ros.getChargedWith().getSelected()) {
            // check to ensure that the Charged WIth condition has been
            // selected
            log.debug("*****************ACharged WIth Selected");
            // check to ensure that the charged with details have been set
            if (isEmptyString(ros.getChargedWith().getContent())) {
                // No charged with details entered
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(CHARGED_WITH_DETAILS_MESSAGE, null);
            }
        }
    }
    
    /**
     * Validate the additonal fields associated with remand order type
     * Added 04/07/2014 for L-R-4224-01 changes associated with LASBO
     * 
     * @param ros
     *            the Remand Order
     * @param helper
     *            the helper
     */
    private static void validateRemanOrderType(RemandOrderStructure ros, ValidationHelper helper) {
        
          
        if(ros.getRemandOrderType().getRemandOrdType().toString().equals(LOCAL_AUTHORITY)){
            if(isEmptyString(ros.getRemandOrderType().getLADesignatedLocalAuthority())){
                //no designated local authority entered
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(DESIGNATED_LOCAL_AUTHORUTY_MESSAGE, null);
            }
            if(ros.getRemandOrderType().getRequirementsOnLocalAuthority().hasSelected() &&
                    ros.getRemandOrderType().getRequirementsOnLocalAuthority().getSelected()){
                if(isEmptyString(ros.getRemandOrderType().getRequirementsOnLocalAuthority().getContent())){
                    //no local authority requirements entered
                    log.debug("*************** PROBLEM ADDED");
                    helper.addLogicalProblem(REQUIREMENTS_ON_LOCAL_AUTHORUTY_MESSAGE, null);
                }
            }
            if(ros.getRemandOrderType().getConditionsOnDefendant().hasSelected() &&
                    ros.getRemandOrderType().getConditionsOnDefendant().getSelected()){
                if(isEmptyString(ros.getRemandOrderType().getConditionsOnDefendant().getContent())){
                    //no conditions on defendant entered
                    log.debug("*************** PROBLEM ADDED");
                    helper.addLogicalProblem(CONDITIONS_ON_DEFENDANT_MESSAGE, null);
                }
            }
        }
        else if(ros.getRemandOrderType().getRemandOrdType().toString().equals(YOUTH_DETENTION)){
            if(isEmptyString(ros.getRemandOrderType().getYDDesignatedLocalAuthority())){
                //no designated local authority entered
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(DESIGNATED_LOCAL_AUTHORUTY_MESSAGE, null);
            }
        }
    }

    private static void validateReasons(RemandOrderStructure ros, ValidationHelper helper) {
    	if (ros.getReasonsForRemand().getSelected()) {
    		log.debug("*****************Reasons Given Selected");
    		boolean reasonsMissing = true;
    		
    		if (ros.getReasonsForRemand().getSeriousnessForRemand().getSelected()) {
    			reasonsMissing = false;
    		}
    		if (ros.getReasonsForRemand().getRemandHistory().getSelected()) {
    			reasonsMissing = false;
    		}
    		if (ros.getReasonsForRemand().getNecessityCondition().getSelected()) {
    			reasonsMissing = false;
    			validateNecessityCondition(ros,helper);
    		}
    		if (ros.getReasonsForRemand().getOtherReasonsForRemand().getSelected()) {
    			if (validateOtherReasons(ros,helper)) {
    				reasonsMissing = false;
    			}
    		}
    		
    		// No reasons have been selected
    		if (reasonsMissing) {
    			log.debug("*****************REASONS_GIVEN_MISSING");
                helper.addLogicalProblem(REASONS_GIVEN_MISSING, null);
            }
        }
    }
        
    private static void validateNecessityCondition(RemandOrderStructure ros, ValidationHelper helper) {
    	if (!ros.getReasonsForRemand().getNecessityCondition().getRiskOfHarm()) {
    		log.debug("*****************NECESSITY_CONDITION_MISSING");
    		helper.addLogicalProblem(NECESSITY_CONDITION_MISSING, null);
    	}
    }
    
    private static boolean validateOtherReasons(RemandOrderStructure ros, ValidationHelper helper) {
    	if (!ros.getReasonsForRemand().getOtherReasonsForRemand().getWelfareReason().getSelected() &&
    			!ros.getReasonsForRemand().getOtherReasonsForRemand().getOwnProtectionReason().getSelected() &&
    			!ros.getReasonsForRemand().getOtherReasonsForRemand().getLackOfPlacementReason().getSelected() &&
    			!ros.getReasonsForRemand().getOtherReasonsForRemand().getBailISSNotAvailableReason().getSelected() &&
    			!ros.getReasonsForRemand().getOtherReasonsForRemand().getBailInadequateReason().getSelected()) {
    		log.debug("*****************OTHER_REASONS_MISSING");
    		return false;
    	}
    	return true;
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
                value.trim().equals("")) // empty string
        {
            result = true;
        }
        return result;
    }
}