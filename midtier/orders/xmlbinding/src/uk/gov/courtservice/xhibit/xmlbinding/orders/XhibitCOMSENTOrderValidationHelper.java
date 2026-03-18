package uk.gov.courtservice.xhibit.xmlbinding.orders;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.AdditionalNotes;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.CommunityOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.XHIBITCommunityOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.ConcurrentType;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper.ValidationHelper;

/**
 * <p>
 * Title: Helper class that populates the Castor bound java-XML object for the
 * XHIBITCommunityOrderStructure
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Validates the XHIBITCommunityOrderStructure for 'Community Punishment and
 * Rehabilitation', 'Community Rehabilitation' and 'Community Punishment'
 * orders.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */
//
// NOTE:
// when organising the imports in this class, please ensure the inner class
// OrderXMLHelper.ValidationHelper is referenced using '.' rather than '$'
// whilst legal, Jikes advises against it and fails to compile. JP.
//
public class XhibitCOMSENTOrderValidationHelper {

    // set up logger
    private static final Logger log = CSServices.getLogger(XhibitCOMSENTOrderValidationHelper.class);

    private static final String END_DATE_3_YEARS_MESSAGE = "ORDER_End_Date_greater_than_3_years";

    private static final String RESP_OFFICER_1_MESSAGE = "ORDER_Resp_Officer_1";

    private static final String RESP_OFFICER_2_MESSAGE = "ORDER_Resp_Officer_2";

    private static final String UNPAID_WORK_HOURS_MESSAGE = "ORDER_Unpaid_work_hours_range";

    private static final String UNPAID_WORK_DETAIL_MESSAGE = "ORDER_Unpaid_work_detail_empty";

    private static final String ACTIVITY_PERSON_MESSAGE = "ORDER_Activity_person_empty";

    private static final String ACTIVITY_PLACE_MESSAGE = "ORDER_Activity_place_empty";

    private static final String ACTIVITY_ACTIVITY_MESSAGE = "ORDER_Activity_activity_empty";

    private static final String PROGRAMME_PROGRAMME_MESSAGE = "ORDER_Programme_programme_empty";

    private static final String PROGRAMME_LOCATION_MESSAGE = "ORDER_Programme_location_empty";

    private static final String PROHIB_ACTIVITY_MESSAGE = "ORDER_Prohib_activity_empty";

    private static final String EXCLUSION_PLACE_MESSAGE = "ORDER_Exclusion_place_empty";

    private static final String EXCLUSION_PERIOD_MESSAGE = "ORDER_Exclusion_period_empty";

    private static final String RESIDENCE_HOSTEL_MESSAGE = "ORDER_Residence_hostel_empty";

    private static final String RESIDENCE_OTHERPLACE_MESSAGE = "ORDER_Residence_otherplace_empty";

    private static final String DRUG_REHAB_DIRECTOR_MESSAGE = "ORDER_Drug_rehab_director_empty";

    private static final String ALCOHOL_TREAT_DIRECTOR_MESSAGE = "ORDER_Alcohol_treat_director_empty";

    private static final String ALCOHOL_TREAT_LOCATION_MESSAGE = "ORDER_Alcohol_treat_location_empty";

    private static final String DRUG_REHAB_LOCATION_MESSAGE = "ORDER_Drug_rehab_location_empty";

    private static final String MENTAL_TREAT_LOCATION_MESSAGE = "ORDER_Mental_treat_location_empty";

    private static final String ACTIVITY_OPTIONS_MESSAGE = "ORDER_Activity_options_empty";
    
    private static final String FOREIGN_TRAVEL_PROHIBITION_MESSAGE = "ORDER_Foreign_travel_prohibition_empty";
    
    private static final String FOREIGN_TRAVEL_PROHIBITION_DAYS_MESSAGE = "ORDER_Foreign_travel_prohibition_date_empty";
    
    private static final String FOREIGN_TRAVEL_PROHIBITION_DATE_MESSAGE = "ORDER_Foreign_travel_prohibition_date";
    
    private static final String FOREIGN_TRAVEL_PROHIBITION_EXCEPTION_MESSAGE = "ORDER_Foreign_travel_prohibition_exception_empty";
    
    private static final String LOCAL_JUSTICE_AREA_EMPTY = "ORDER_Local_Justice_Area_Empty";
    
    private static final String REHABILITATION_ACTIVITY_DAYS_MESSAGE = "ORDER_Rehabilitation_activity_date_empty";

    private static final String ADDITIONAL_NOTES_MESSAGE = "ORDER_Additional_Notes_incorrect";
    
    private static final String TRIAL_MONITORING_DURATION_EXCEPTION_MESSAGE = "ORDER_Trial_Monitoring_duration_empty";
    
    private static final String ALCOHOL_ABSTINENCE_DAYS_EXCEPTION_MESSAGE = "ORDER_Alcohol_abstinence_date_empty";
    
    private static final String PROBLEM_ADDED = "*************** PROBLEM ADDED";

    // Statically initialise the required home interface.
    // Should be done for all home interfaces used.

    /**
     * Utility methodValidation
     * 
     * @param cos
     *            teh order structure
     * @param typeCode
     *            the code
     * @param helper
     *            the helper
     */
    public static void logicalValidateCommunityOrder(CommunityOrderStructure cos,
            OrderXMLHelper.ValidationHelper helper) {
        log.debug("$$$ XhibitCommunityOrderHelper.logicalValidateCommunityOrder");
        validateOrderEndDate(cos, helper);
        validateResponsibleOfficers(cos, helper);
        validateUnpaidWorkRequirement(cos, helper);
        validateActivityRequirement(cos, helper);
        validateProgrammeRequirement(cos, helper);
        validateProhibitedActivityRequirement(cos, helper);
        validateExclusionRequirement(cos, helper);
        validateResidenceRequirement(cos, helper);
        validateDrugRehabRequirement(cos, helper);
        validateAlcoholTreatmentRequirement(cos, helper);
        validateMentalHealthTreatmentRequirement(cos, helper);
        validateForeignTravelProhibitionRequirement(cos, helper);
        validateRehabilitationActivityRequirement(cos, helper);
        validateTrailMonitoringRequirement(cos, helper);
        validateAlcoholAbstinenceRequirement(cos,helper);
        validateLocalJusticeArea(cos, helper);
        validateAdditionalNotes(cos, helper);
    }

    /**
     * Utility method to check that any elements whose value depends on other
     * elements are set correctly. In the community orders the FailedToComply
     * element must be set to match the Breach option. Failed to comply cannot
     * be set via the gui as this option was removed, but it remains in the
     * schema.
     * 
     * @todo This function doesn't appear to be used and confusingly includes XHIBITCommunityOrderStructure
     *     consider removing or identifying what the difference is from CommunityOrderStructure.
     * 
     * @param cos
     *            the community order
     * @param typeCode
     *            the order type
     * @param helper
     *            the helper
     * @return the validated xml
     * @throws ValidationException
     * @throws MarshalException
     * @throws OrderXMLException
     */
    public static String structuralValidateCommunityOrder(XHIBITCommunityOrderStructure cos, String typeCode,
            OrderXMLHelper.ValidationHelper helper) throws ValidationException, MarshalException, OrderXMLException {
        log.debug("$$$ XhibitCommunityOrderHelper.structuralValidateCommunityOrder");
        return OrderXMLHelper.getXML(helper.getOrderToBeValidated());
    }

    /**
     * Check that the Order End Date is not greater than 3 years after the order
     * date
     * 
     * @param cos
     *            the community order sctructure
     * @param helper
     *            the helper
     */
    private static void validateOrderEndDate(CommunityOrderStructure cos, ValidationHelper helper) {
        log.debug("$$$ validateOrderEndDate() " + cos.getOrderHeader().getDefendant().getCharges().getChargeCount());
        Calendar today = Calendar.getInstance();
        Calendar compDate = cos.getCompletionDate().toCalendar();

        int age = compDate.get(Calendar.YEAR) - today.get(Calendar.YEAR);
        compDate.set(Calendar.YEAR, today.get(Calendar.YEAR));
        if (today.after(compDate)) {
            age--;
        }

        if (age >= 3) {
            log.debug(PROBLEM_ADDED);
            helper.addLogicalProblem(END_DATE_3_YEARS_MESSAGE, null);
        }
    }

    /**
     * Check that the Responsible Officers 1 & 2 have been populated
     * 
     * @param cos
     *            the community order sctructure
     * @param helper
     *            the helper
     */
    private static void validateResponsibleOfficers(CommunityOrderStructure cos, ValidationHelper helper) {
        log.debug("$$$ validateResponsibleOfficer1() " + cos.getResponsibleOfficer1());
        if (isEmptyString(cos.getResponsibleOfficer1())) {
            log.debug(PROBLEM_ADDED);
            helper.addLogicalProblem(RESP_OFFICER_1_MESSAGE, null);
        }

        log.debug("$$$ validateResponsibleOfficer2() " + cos.getResponsibleOfficer2());
        if (isEmptyString(cos.getResponsibleOfficer2())) {
            log.debug(PROBLEM_ADDED);
            helper.addLogicalProblem(RESP_OFFICER_2_MESSAGE, null);
        }
    }

    /**
     * Check that the Unpaid work details have been entered if option selected
     * 
     * @param cos
     *            the community order sctructure
     * @param helper
     *            the helper
     */
    private static void validateUnpaidWorkRequirement(CommunityOrderStructure cos, ValidationHelper helper) {
        log.debug("$$$ validateUnpaidWorkRequirement() ");
        int hours = cos.getOrderRequirements().getUnpaidWorkRequirement().getHours();
        if (cos.getOrderRequirements().getUnpaidWorkRequirement().getSelected() &&  hours > 300) {
            log.debug(PROBLEM_ADDED);
            helper.addLogicalProblem(UNPAID_WORK_HOURS_MESSAGE, null);
        }

        if (cos.getOrderRequirements().getUnpaidWorkRequirement().getSelected()
                && cos.getOrderRequirements().getUnpaidWorkRequirement().getConcurrent().getType() != ConcurrentType.NONE_TYPE
                && isEmptyString(cos.getOrderRequirements().getUnpaidWorkRequirement().getWorkDetails())) {
            log.debug(PROBLEM_ADDED);
            helper.addLogicalProblem(UNPAID_WORK_DETAIL_MESSAGE, null);
        }
    }

    /**
     * Check that the Activity Requirement details have been entered if option
     * selected
     * 
     * @param cos
     *            the community order sctructure
     * @param helper
     *            the helper
     */
    private static void validateActivityRequirement(CommunityOrderStructure cos, ValidationHelper helper) {
        log.debug("$$$ validateActivityRequirement() ");
        if (cos.getOrderRequirements().getActivityRequirement().getSelected()) {
            if (!cos.getOrderRequirements().getActivityRequirement().getPresentDetails().getSelected()
                    && !cos.getOrderRequirements().getActivityRequirement().getActivityDetails().getSelected()
                    && !cos.getOrderRequirements().getActivityRequirement().getAdditonalRequirements().getSelected()) {
                log.debug("*************** PROBLEM ADDED - person");
                helper.addLogicalProblem(ACTIVITY_OPTIONS_MESSAGE, null);
            }

            if (cos.getOrderRequirements().getActivityRequirement().getPresentDetails().getSelected()) {
                log.debug("*****************PresentDetails Selected");
                if (isEmptyString(cos.getOrderRequirements().getActivityRequirement().getPresentDetails().getPerson())) {
                    log.debug("*************** PROBLEM ADDED - person");
                    helper.addLogicalProblem(ACTIVITY_PERSON_MESSAGE, null);
                }
                if (isEmptyString(cos.getOrderRequirements().getActivityRequirement().getPresentDetails().getPlace()
                        .getSite())) {
                    log.debug("*************** PROBLEM ADDED - place");
                    helper.addLogicalProblem(ACTIVITY_PLACE_MESSAGE, null);
                }

            }
            if (cos.getOrderRequirements().getActivityRequirement().getActivityDetails().getSelected()) {
                log.debug("*****************ActivityDetails Selected");
                if (isEmptyString(cos.getOrderRequirements().getActivityRequirement().getActivityDetails()
                        .getActivity())) {
                    log.debug(PROBLEM_ADDED);
                    helper.addLogicalProblem(ACTIVITY_ACTIVITY_MESSAGE, null);
                }
            }
        }
    }

    /**
     * Check that the Programme Requirement details have been entered if option
     * selected
     * 
     * @param cos
     *            the community order sctructure
     * @param helper
     *            the helper
     */
    private static void validateProgrammeRequirement(CommunityOrderStructure cos, ValidationHelper helper) {
        log.debug("$$$ validateProgrammeRequirement() ");
        if (cos.getOrderRequirements().getProgrammeRequirement().getSelected()) {
            log.debug("*****************Programme Selected");
            if (isEmptyString(cos.getOrderRequirements().getProgrammeRequirement().getProgramme())) {
                log.debug(PROBLEM_ADDED);
                helper.addLogicalProblem(PROGRAMME_PROGRAMME_MESSAGE, null);
            }
            log.debug("*****************Place Selected");
            if (isEmptyString(cos.getOrderRequirements().getProgrammeRequirement().getPlace().getSite())) {
                log.debug(PROBLEM_ADDED);
                helper.addLogicalProblem(PROGRAMME_LOCATION_MESSAGE, null);
            }
        }
    }

    /**
     * Check that the Prohibited ACtivity Requirement details have been entered
     * if option selected
     * 
     * @param cos
     *            the community order sctructure
     * @param helper
     *            the helper
     */
    private static void validateProhibitedActivityRequirement(CommunityOrderStructure cos, ValidationHelper helper) {
        log.debug("$$$ validateProhibitedActivityRequirement() ");
        if (cos.getOrderRequirements().getProhibitedActivityRequirement().getSelected() && 
        isEmptyString(cos.getOrderRequirements().getProhibitedActivityRequirement().getActivity())) {
                log.debug(PROBLEM_ADDED);
                helper.addLogicalProblem(PROHIB_ACTIVITY_MESSAGE, null);
            
        }
    }

    /**
     * Check that the Exclusion Requirement details have been entered if option
     * selected
     * 
     * @param cos
     *            the community order sctructure
     * @param helper
     *            the helper
     */
    private static void validateExclusionRequirement(CommunityOrderStructure cos, ValidationHelper helper) {
        log.debug("$$$ validateExclusionRequirement() ");
        if (cos.getOrderRequirements().getExclusionRequirement().getSelected()) {
            log.debug("*****************Place Selected");
            if (isEmptyString(cos.getOrderRequirements().getExclusionRequirement().getPlace().getSite())) {
                log.debug(PROBLEM_ADDED);
                helper.addLogicalProblem(EXCLUSION_PLACE_MESSAGE, null);
            }
            if (cos.getOrderRequirements().getExclusionRequirement().getBetweenPeriod().getSelected()) {
                log.debug("*****************BetweenPeriod Selected");
                if (isEmptyString(cos.getOrderRequirements().getExclusionRequirement().getBetweenPeriod()
                        .getApplicablePeriod())) {
                    log.debug(PROBLEM_ADDED);
                    helper.addLogicalProblem(EXCLUSION_PERIOD_MESSAGE, null);
                }

            }
        }
    }

    /**
     * Check that the Residence Requirement details have been entered if option
     * selected
     * 
     * @param cos
     *            the community order sctructure
     * @param helper
     *            the helper
     */
    private static void validateResidenceRequirement(CommunityOrderStructure cos, ValidationHelper helper) {
        log.debug("$$$ validateResidenceRequirement() ");
        if (cos.getOrderRequirements().getResidenceRequirement().getSelected()) {
            log.debug("*****************Hostel Selected");
            if (isEmptyString(cos.getOrderRequirements().getResidenceRequirement().getHostel().getSite())) {
                log.debug(PROBLEM_ADDED);
                helper.addLogicalProblem(RESIDENCE_HOSTEL_MESSAGE, null);
            }
            if (cos.getOrderRequirements().getResidenceRequirement().getPlaceOption().getSelected()) {
                log.debug("*****************PlaceOption Selected");
                if (isEmptyString(cos.getOrderRequirements().getResidenceRequirement().getPlaceOption().getPlace()
                        .getSite())) {
                    log.debug(PROBLEM_ADDED);
                    helper.addLogicalProblem(RESIDENCE_OTHERPLACE_MESSAGE, null);
                }
            }
        }
    }

    /**
     * Check that the Drug Rehab Requirement details have been entered if option
     * selected
     * 
     * @param cos
     *            the community order sctructure
     * @param helper
     *            the helper
     */
    private static void validateDrugRehabRequirement(CommunityOrderStructure cos, ValidationHelper helper) {
        log.debug("$$$ validateDrugRehabRequirement() "
                + cos.getOrderRequirements().getDrugRehabilitationRequirement().getSelected());
        if (cos.getOrderRequirements().getDrugRehabilitationRequirement().getSelected()) {
            log.debug("$$$ Director "
                    + cos.getOrderRequirements().getDrugRehabilitationRequirement().getTreatmentDirector());
            if (isEmptyString(cos.getOrderRequirements().getDrugRehabilitationRequirement().getTreatmentDirector())) {
                log.debug(PROBLEM_ADDED);
                helper.addLogicalProblem(DRUG_REHAB_DIRECTOR_MESSAGE, null);
            }

            log.debug("$$$ validateDrugRehabRequirement() "
                    + cos.getOrderRequirements().getDrugRehabilitationRequirement().getTreatmentOption().getSelected());
            if (cos.getOrderRequirements().getDrugRehabilitationRequirement().getTreatmentOption().getSelected()) {
                log.debug("$$$ Clinic "
                        + cos.getOrderRequirements().getDrugRehabilitationRequirement().getTreatmentOption()
                                .getTreatmentLocation().getSite());
                if (isEmptyString(cos.getOrderRequirements().getDrugRehabilitationRequirement().getTreatmentOption()
                        .getTreatmentLocation().getSite())) {
                    log.debug(PROBLEM_ADDED);
                    helper.addLogicalProblem(DRUG_REHAB_LOCATION_MESSAGE, null);
                }

            }
        }
    }

    /**
     * Check that the Alcohol Treatment Requirement details have been entered if
     * option selected
     * 
     * @param cos
     *            the community order sctructure
     * @param helper
     *            the helper
     */
    private static void validateAlcoholTreatmentRequirement(CommunityOrderStructure cos, ValidationHelper helper) {
        log.debug("$$$ validateAlcoholTreatmentRequirement() "
                + cos.getOrderRequirements().getAlcoholTreatmentRequirement().getSelected());
        if (cos.getOrderRequirements().getAlcoholTreatmentRequirement().getSelected()) {
            log.debug("$$$ Director "
                    + cos.getOrderRequirements().getAlcoholTreatmentRequirement().getTreatmentDirector());
            if (isEmptyString(cos.getOrderRequirements().getAlcoholTreatmentRequirement().getTreatmentDirector())) {
                log.debug(PROBLEM_ADDED);
                helper.addLogicalProblem(ALCOHOL_TREAT_DIRECTOR_MESSAGE, null);
            }

            log.debug("$$$ Treatment option "
                    + cos.getOrderRequirements().getAlcoholTreatmentRequirement().getTreatmentOption().getSelected());
            if (cos.getOrderRequirements().getAlcoholTreatmentRequirement().getTreatmentOption().getSelected()) {
                log.debug("$$$ Location "
                        + cos.getOrderRequirements().getAlcoholTreatmentRequirement().getTreatmentOption()
                                .getTreatmentLocation().getSite());
                if (isEmptyString(cos.getOrderRequirements().getAlcoholTreatmentRequirement().getTreatmentOption()
                        .getTreatmentLocation().getSite())) {
                    log.debug(PROBLEM_ADDED);
                    helper.addLogicalProblem(ALCOHOL_TREAT_LOCATION_MESSAGE, null);
                }

            }
        }
    }

    /**
     * Check that the Mental Health Treatment Requirement details have been
     * entered if option selected
     * 
     * @param cos
     *            the community order sctructure
     * @param helper
     *            the helper
     */
    private static void validateMentalHealthTreatmentRequirement(CommunityOrderStructure cos, ValidationHelper helper) {
        log.debug("$$$ validateMentalHealthTreatmentRequirement() "
                + cos.getOrderRequirements().getMentalHealthTreatmentRequirement().getSelected());
        if (cos.getOrderRequirements().getMentalHealthTreatmentRequirement().getSelected()) {
            log.debug("$$$ Treatment option "
                    + cos.getOrderRequirements().getMentalHealthTreatmentRequirement().getTreatmentOption()
                            .getSelected());
            if (cos.getOrderRequirements().getMentalHealthTreatmentRequirement().getTreatmentOption().getSelected()) {
                log.debug("$$$ Location "
                        + cos.getOrderRequirements().getMentalHealthTreatmentRequirement().getTreatmentOption()
                                .getTreatmentLocation().getSite());
                if (isEmptyString(cos.getOrderRequirements().getMentalHealthTreatmentRequirement().getTreatmentOption()
                        .getTreatmentLocation().getSite())) {
                    log.debug(PROBLEM_ADDED);
                    helper.addLogicalProblem(MENTAL_TREAT_LOCATION_MESSAGE, null);
                }
            }
        }
    }
    
    
    /**
     * Check that the Foreign Travel Prohibition details have been entered if option selected
     * 
     * @param cos
     *            the community order sctructure
     * @param helper
     *            the helper
     */
    private static void validateForeignTravelProhibitionRequirement(CommunityOrderStructure cos, ValidationHelper helper) {
        log.debug("$$$ validateForeignTravelProhibitionRequirement() ");

        if (cos.getOrderRequirements().getForeignTravelProhibitionRequirement().getSelected()
                && isEmptyString(cos.getOrderRequirements().getForeignTravelProhibitionRequirement().getProhibitedFrom())) {
            log.debug(PROBLEM_ADDED);
            helper.addLogicalProblem(FOREIGN_TRAVEL_PROHIBITION_MESSAGE, null);
        }
        
        if (cos.getOrderRequirements().getForeignTravelProhibitionRequirement().getSelected()
                && !cos.getOrderRequirements().getForeignTravelProhibitionRequirement().getFromToOption().getSelected() 
                && isEmptyString(cos.getOrderRequirements().getForeignTravelProhibitionRequirement().getDays())) {
        
            log.debug(PROBLEM_ADDED);
            helper.addLogicalProblem(FOREIGN_TRAVEL_PROHIBITION_DAYS_MESSAGE, null);
        }
        
        if (cos.getOrderRequirements().getForeignTravelProhibitionRequirement().getSelected()
                && cos.getOrderRequirements().getForeignTravelProhibitionRequirement().getFromToOption().getSelected()) {
            
            Calendar fromDate = cos.getOrderRequirements().getForeignTravelProhibitionRequirement().getFromToOption().getFromDate().toCalendar();
            Calendar toDate = cos.getOrderRequirements().getForeignTravelProhibitionRequirement().getFromToOption().getToDate().toCalendar();

            if (fromDate.after(toDate)) {
                log.debug(PROBLEM_ADDED);
                helper.addLogicalProblem(FOREIGN_TRAVEL_PROHIBITION_DATE_MESSAGE, null);
            }
        }
        
        if (cos.getOrderRequirements().getForeignTravelProhibitionRequirement().getSelected()
                && cos.getOrderRequirements().getForeignTravelProhibitionRequirement().getExceptionOption().getSelected()
                && isEmptyString(cos.getOrderRequirements().getForeignTravelProhibitionRequirement().getExceptionOption().getException())){
            
                log.debug(PROBLEM_ADDED);
                helper.addLogicalProblem(FOREIGN_TRAVEL_PROHIBITION_EXCEPTION_MESSAGE, null);
        
        }
    }
    
    
    private static void validateLocalJusticeArea(CommunityOrderStructure cos, ValidationHelper helper) {
        log.debug("$$$ validateLocalJusticeArea() ");
        if (cos.getPettySessionalArea().getCourtHouseName() == null
                || cos.getPettySessionalArea().getCourtHouseName().trim().equals("")) {
            log.debug(PROBLEM_ADDED);
            helper.addLogicalProblem(LOCAL_JUSTICE_AREA_EMPTY, null);
        }
    }
    
    private static void validateRehabilitationActivityRequirement(CommunityOrderStructure cos, ValidationHelper helper) {
        log.debug("$$$ validateForeignTravelProhibitionRequirement() ");

        if (cos.getOrderRequirements().getRehabilitationActivityRequirement().getSelected()
                && isEmptyString(cos.getOrderRequirements().getRehabilitationActivityRequirement().getDays())) {
        
            log.debug(PROBLEM_ADDED);
            helper.addLogicalProblem(REHABILITATION_ACTIVITY_DAYS_MESSAGE, null);
        }
    }
    
    private static void validateTrailMonitoringRequirement(CommunityOrderStructure cos, ValidationHelper helper) {
        log.debug("$$$ validateTrailMonitoringRequirement() ");

        if (cos.getOrderRequirements().getTrailMonitoringRequirement().getSelected()
                && isEmptyString(cos.getOrderRequirements().getTrailMonitoringRequirement().getDuration())) {
            log.debug(PROBLEM_ADDED);
            helper.addLogicalProblem(TRIAL_MONITORING_DURATION_EXCEPTION_MESSAGE, null);
        }           
    }
    
        private static void validateAlcoholAbstinenceRequirement(CommunityOrderStructure ssos, ValidationHelper helper) {
        log.debug("$$$ validateAlcoholAbstinenceRequirement() ");

        if (ssos.getOrderRequirements().getAlcoholAbstinenceAndMonitoringRequirement().getSelected()
                && isEmptyString(ssos.getOrderRequirements().getAlcoholAbstinenceAndMonitoringRequirement().getDays())) {
            log.debug(PROBLEM_ADDED);
            helper.addLogicalProblem(ALCOHOL_ABSTINENCE_DAYS_EXCEPTION_MESSAGE, null);
        }           
    }
    
    /**
     * Validate the Additional Notes Option
     * 
     * @param cos
     *            the community order
     * @param helper
     *            the helper
     */
    private static void validateAdditionalNotes(CommunityOrderStructure cos, ValidationHelper helper) {
        AdditionalNotes notes = cos.getAdditionalNotes();
        // check to ensure that the additional notes option has been selected
        if (notes.hasSelected() && notes.getSelected()) {
            log.debug("*****************AdditionalNotes Selected");
            // check to ensure that the Additional Notes have been set
            if (isEmptyString(notes.getContent())) {
                // No additional notes entered
                log.debug(PROBLEM_ADDED);
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