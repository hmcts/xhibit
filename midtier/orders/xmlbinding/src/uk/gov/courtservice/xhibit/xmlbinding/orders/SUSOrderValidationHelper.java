package uk.gov.courtservice.xhibit.xmlbinding.orders;

import java.util.Calendar;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.ConcurrentType;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper.ValidationHelper;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.AdditionalNotes;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.SuspendedSentenceOrderStructure;

/**
 * <p>
 * Title: Helper class that populates the Castor bound java-XML object for the
 * XHIBITSuspendedSentenceOrderStructure
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Validates the XHIBITSuspendedSentenceOrderStructure for Suspended Sentence Order.
 * As this order is based on the Community Order, its helpers are based on the COMSENT order's.
 * </p>
 * * <p>
 * Company: Logica
 * </p>
 * 
 * @author Luis Valenzuela
 * @version 1.0
 */
//
// NOTE:
// when organising the imports in this class, please ensure the inner class
// OrderXMLHelper.ValidationHelper is referenced using '.' rather than '$'
// whilst legal, Jikes advises against it and fails to compile. JP.
//
public class SUSOrderValidationHelper {

    // set up logger
    private static final Logger log = CSServices.getLogger(SUSOrderValidationHelper.class);

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
    private static final String REHABILITATION_ACTIVITY_DAYS_EXCEPTION_MESSAGE = "ORDER_Rehabilitation_activity_date_empty";
    private static final String ALCOHOL_ABSTINENCE_DAYS_EXCEPTION_MESSAGE = "ORDER_Alcohol_abstinence_date_empty";
    private static final String LOCAL_JUSTICE_AREA_EMPTY = "ORDER_Local_Justice_Area_Empty";
    private static final String ADDITIONAL_NOTES_MESSAGE = "ORDER_Additional_Notes_incorrect";
    private static final String TRIAL_MONITORING_DURATION_EXCEPTION_MESSAGE = "ORDER_Trial_Monitoring_duration_empty";
    private static final String PROBLEM_ADDED = "*************** PROBLEM ADDED";

    
    // Statically initialise the required home interface.
   // Should be done for all home interfaces used.

    /**
     * Utility methodValidation
     * 
     * @param ssos
     *            The order structure
     * @param typeCode
     *            the code
     * @param helper
     *            the helper
     */
    public static void logicalValidateSuspendedSentenceOrder(SuspendedSentenceOrderStructure ssos,
            OrderXMLHelper.ValidationHelper helper) {
        log.debug("$$$ XhibitSuspendedSentenceOrderHelper.logicalValidateSuspendedSentenceOrder");
        validateOrderEndDate(ssos, helper);
        validateResponsibleOfficers(ssos, helper);
        validateUnpaidWorkRequirement(ssos, helper);
        validateActivityRequirement(ssos, helper);
        validateProgrammeRequirement(ssos, helper);
        validateProhibitedActivityRequirement(ssos, helper);
        validateExclusionRequirement(ssos, helper);
        validateResidenceRequirement(ssos, helper);
        validateDrugRehabRequirement(ssos, helper);
        validateAlcoholTreatmentRequirement(ssos, helper);
        validateMentalHealthTreatmentRequirement(ssos, helper);
        validateForeignTravelProhibitionRequirement(ssos, helper);
        validateRehabilitationActivityRequirement(ssos, helper);
        validateTrailMonitoringRequirement(ssos, helper);
        validateAlcoholAbstinenceRequirement(ssos,helper);
        validateLocalJusticeArea(ssos, helper);
        validateAdditionalNotes(ssos, helper);
    }

   
    /**
     * Check that the Order End Date is not greater than 3 years after the order
     * date
     * 
     * @param ssos
     *            the order sctructure
     * @param helper
     *            the helper
     */
    private static void validateOrderEndDate(SuspendedSentenceOrderStructure ssos, ValidationHelper helper) {
        log.debug("$$$ validateOrderEndDate() " + ssos.getOrderHeader().getDefendant().getCharges().getChargeCount());
        Calendar today = Calendar.getInstance();
        Calendar compDate = ssos.getCompletionDate().toCalendar();

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
     * @param ssos
     *            the order sctructure
     * @param helper
     *            the helper
     */
    private static void validateResponsibleOfficers(SuspendedSentenceOrderStructure ssos, ValidationHelper helper) {
        log.debug("$$$ validateResponsibleOfficer1() " + ssos.getResponsibleOfficer1());
        if (isEmptyString(ssos.getResponsibleOfficer1())) {
            log.debug(PROBLEM_ADDED);
            helper.addLogicalProblem(RESP_OFFICER_1_MESSAGE, null);
        }

        log.debug("$$$ validateResponsibleOfficer2() " + ssos.getResponsibleOfficer2());
        if (isEmptyString(ssos.getResponsibleOfficer2())) {
            log.debug(PROBLEM_ADDED);
            helper.addLogicalProblem(RESP_OFFICER_2_MESSAGE, null);
        }
    }

    /**
     * Check that the Unpaid work details have been entered if option selected
     * 
     * @param ssos
     *            the order sctructure
     * @param helper
     *            the helper
     */
    private static void validateUnpaidWorkRequirement(SuspendedSentenceOrderStructure ssos, ValidationHelper helper) {
        log.debug("$$$ validateUnpaidWorkRequirement() ");
        int hours = ssos.getOrderRequirements().getUnpaidWorkRequirement().getHours();
        if (ssos.getOrderRequirements().getUnpaidWorkRequirement().getSelected() &&  hours > 300) {
            log.debug(PROBLEM_ADDED);
            helper.addLogicalProblem(UNPAID_WORK_HOURS_MESSAGE, null);
        }

        if (ssos.getOrderRequirements().getUnpaidWorkRequirement().getSelected()
                && ssos.getOrderRequirements().getUnpaidWorkRequirement().getConcurrent().getType() != ConcurrentType.NONE_TYPE
                && isEmptyString(ssos.getOrderRequirements().getUnpaidWorkRequirement().getWorkDetails())) {
            log.debug(PROBLEM_ADDED);
            helper.addLogicalProblem(UNPAID_WORK_DETAIL_MESSAGE, null);
        }
    }

    /**
     * Check that the Activity Requirement details have been entered if option
     * selected
     * 
     * @param ssos
     *            the order sctructure
     * @param helper
     *            the helper
     */
    private static void validateActivityRequirement(SuspendedSentenceOrderStructure ssos, ValidationHelper helper) {
        log.debug("$$$ validateActivityRequirement() ");
        if (ssos.getOrderRequirements().getActivityRequirement().getSelected()) {
            if (!ssos.getOrderRequirements().getActivityRequirement().getPresentDetails().getSelected()
                    && !ssos.getOrderRequirements().getActivityRequirement().getActivityDetails().getSelected()
                    && !ssos.getOrderRequirements().getActivityRequirement().getAdditonalRequirements().getSelected()) {
                log.debug("*************** PROBLEM ADDED - person");
                helper.addLogicalProblem(ACTIVITY_OPTIONS_MESSAGE, null);
            }

            if (ssos.getOrderRequirements().getActivityRequirement().getPresentDetails().getSelected()) {
                log.debug("*****************PresentDetails Selected");
                if (isEmptyString(ssos.getOrderRequirements().getActivityRequirement().getPresentDetails().getPerson())) {
                    log.debug("*************** PROBLEM ADDED - person");
                    helper.addLogicalProblem(ACTIVITY_PERSON_MESSAGE, null);
                }
                if (isEmptyString(ssos.getOrderRequirements().getActivityRequirement().getPresentDetails().getPlace()
                        .getSite())) {
                    log.debug("*************** PROBLEM ADDED - place");
                    helper.addLogicalProblem(ACTIVITY_PLACE_MESSAGE, null);
                }

            }
            if (ssos.getOrderRequirements().getActivityRequirement().getActivityDetails().getSelected()) {
                log.debug("*****************ActivityDetails Selected");
                if (isEmptyString(ssos.getOrderRequirements().getActivityRequirement().getActivityDetails()
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
     * @param ssos
     *            the order sctructure
     * @param helper
     *            the helper
     */
    private static void validateProgrammeRequirement(SuspendedSentenceOrderStructure ssos, ValidationHelper helper) {
        log.debug("$$$ validateProgrammeRequirement() ");
        if (ssos.getOrderRequirements().getProgrammeRequirement().getSelected()) {
            log.debug("*****************Programme Selected");
            if (isEmptyString(ssos.getOrderRequirements().getProgrammeRequirement().getProgramme())) {
                log.debug(PROBLEM_ADDED);
                helper.addLogicalProblem(PROGRAMME_PROGRAMME_MESSAGE, null);
            }
            log.debug("*****************Place Selected");
            if (isEmptyString(ssos.getOrderRequirements().getProgrammeRequirement().getPlace().getSite())) {
                log.debug(PROBLEM_ADDED);
                helper.addLogicalProblem(PROGRAMME_LOCATION_MESSAGE, null);
            }
        }
    }

    /**
     * Check that the Prohibited ACtivity Requirement details have been entered
     * if option selected
     * 
     * @param ssos
     *            the  order sctructure
     * @param helper
     *            the helper
     */
    private static void validateProhibitedActivityRequirement(SuspendedSentenceOrderStructure ssos, ValidationHelper helper) {
        log.debug("$$$ validateProhibitedActivityRequirement() ");
        if (ssos.getOrderRequirements().getProhibitedActivityRequirement().getSelected() &&
        		isEmptyString(ssos.getOrderRequirements().getProhibitedActivityRequirement().getActivity())) {
            log.debug(PROBLEM_ADDED);
            helper.addLogicalProblem(PROHIB_ACTIVITY_MESSAGE, null);
            
        }
    }

    /**
     * Check that the Exclusion Requirement details have been entered if option
     * selected
     * 
     * @param ssos
     *            the order sctructure
     * @param helper
     *            the helper
     */
    private static void validateExclusionRequirement(SuspendedSentenceOrderStructure ssos, ValidationHelper helper) {
        log.debug("$$$ validateExclusionRequirement() ");
        if (ssos.getOrderRequirements().getExclusionRequirement().getSelected()) {
            log.debug("*****************Place Selected");
            if (isEmptyString(ssos.getOrderRequirements().getExclusionRequirement().getPlace().getSite())) {
                log.debug(PROBLEM_ADDED);
                helper.addLogicalProblem(EXCLUSION_PLACE_MESSAGE, null);
            }
            if (ssos.getOrderRequirements().getExclusionRequirement().getBetweenPeriod().getSelected()) {
                log.debug("*****************BetweenPeriod Selected");
                if (isEmptyString(ssos.getOrderRequirements().getExclusionRequirement().getBetweenPeriod()
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
     * @param ssos
     *            the  order sctructure
     * @param helper
     *            the helper
     */
    private static void validateResidenceRequirement(SuspendedSentenceOrderStructure ssos, ValidationHelper helper) {
        log.debug("$$$ validateResidenceRequirement() ");
        if (ssos.getOrderRequirements().getResidenceRequirement().getSelected()) {
            log.debug("*****************Hostel Selected");
            if (isEmptyString(ssos.getOrderRequirements().getResidenceRequirement().getHostel().getSite())) {
                log.debug(PROBLEM_ADDED);
                helper.addLogicalProblem(RESIDENCE_HOSTEL_MESSAGE, null);
            }
            if (ssos.getOrderRequirements().getResidenceRequirement().getPlaceOption().getSelected()) {
                log.debug("*****************PlaceOption Selected");
                if (isEmptyString(ssos.getOrderRequirements().getResidenceRequirement().getPlaceOption().getPlace()
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
     * @param ssos
     *            the  order sctructure
     * @param helper
     *            the helper
     */
    private static void validateDrugRehabRequirement(SuspendedSentenceOrderStructure ssos, ValidationHelper helper) {
        log.debug("$$$ validateDrugRehabRequirement() "
                + ssos.getOrderRequirements().getDrugRehabilitationRequirement().getSelected());
        if (ssos.getOrderRequirements().getDrugRehabilitationRequirement().getSelected()) {
            log.debug("$$$ Director "
                    + ssos.getOrderRequirements().getDrugRehabilitationRequirement().getTreatmentDirector());
            if (isEmptyString(ssos.getOrderRequirements().getDrugRehabilitationRequirement().getTreatmentDirector())) {
                log.debug(PROBLEM_ADDED);
                helper.addLogicalProblem(DRUG_REHAB_DIRECTOR_MESSAGE, null);
            }

            log.debug("$$$ validateDrugRehabRequirement() "
                    + ssos.getOrderRequirements().getDrugRehabilitationRequirement().getTreatmentOption().getSelected());
            if (ssos.getOrderRequirements().getDrugRehabilitationRequirement().getTreatmentOption().getSelected()) {
                log.debug("$$$ Clinic "
                        + ssos.getOrderRequirements().getDrugRehabilitationRequirement().getTreatmentOption()
                                .getTreatmentLocation().getSite());
                if (isEmptyString(ssos.getOrderRequirements().getDrugRehabilitationRequirement().getTreatmentOption()
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
     * @param ssos
     *            the  order sctructure
     * @param helper
     *            the helper
     */
    private static void validateAlcoholTreatmentRequirement(SuspendedSentenceOrderStructure ssos, ValidationHelper helper) {
        log.debug("$$$ validateAlcoholTreatmentRequirement() "
                + ssos.getOrderRequirements().getAlcoholTreatmentRequirement().getSelected());
        if (ssos.getOrderRequirements().getAlcoholTreatmentRequirement().getSelected()) {
            log.debug("$$$ Director "
                    + ssos.getOrderRequirements().getAlcoholTreatmentRequirement().getTreatmentDirector());
            if (isEmptyString(ssos.getOrderRequirements().getAlcoholTreatmentRequirement().getTreatmentDirector())) {
                log.debug(PROBLEM_ADDED);
                helper.addLogicalProblem(ALCOHOL_TREAT_DIRECTOR_MESSAGE, null);
            }

            log.debug("$$$ Treatment option "
                    + ssos.getOrderRequirements().getAlcoholTreatmentRequirement().getTreatmentOption().getSelected());
            if (ssos.getOrderRequirements().getAlcoholTreatmentRequirement().getTreatmentOption().getSelected()) {
                log.debug("$$$ Location "
                        + ssos.getOrderRequirements().getAlcoholTreatmentRequirement().getTreatmentOption()
                                .getTreatmentLocation().getSite());
                if (isEmptyString(ssos.getOrderRequirements().getAlcoholTreatmentRequirement().getTreatmentOption()
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
     * @param ssos
     *            the  order sctructure
     * @param helper
     *            the helper
     */
    private static void validateMentalHealthTreatmentRequirement(SuspendedSentenceOrderStructure ssos, ValidationHelper helper) {
        log.debug("$$$ validateMentalHealthTreatmentRequirement() "
                + ssos.getOrderRequirements().getMentalHealthTreatmentRequirement().getSelected());
        if (ssos.getOrderRequirements().getMentalHealthTreatmentRequirement().getSelected()) {
            log.debug("$$$ Treatment option "
                    + ssos.getOrderRequirements().getMentalHealthTreatmentRequirement().getTreatmentOption()
                            .getSelected());
            if (ssos.getOrderRequirements().getMentalHealthTreatmentRequirement().getTreatmentOption().getSelected()) {
                log.debug("$$$ Location "
                        + ssos.getOrderRequirements().getMentalHealthTreatmentRequirement().getTreatmentOption()
                                .getTreatmentLocation().getSite());
                if (isEmptyString(ssos.getOrderRequirements().getMentalHealthTreatmentRequirement().getTreatmentOption()
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
     * @param ssos
     *            the community order sctructure
     * @param helper
     *            the helper
     */
    private static void validateForeignTravelProhibitionRequirement(SuspendedSentenceOrderStructure ssos, ValidationHelper helper) {
        log.debug("$$$ validateForeignTravelProhibitionRequirement() ");

        if (ssos.getOrderRequirements().getForeignTravelProhibitionRequirement().getSelected()
                && isEmptyString(ssos.getOrderRequirements().getForeignTravelProhibitionRequirement().getProhibitedFrom())) {
            log.debug(PROBLEM_ADDED);
            helper.addLogicalProblem(FOREIGN_TRAVEL_PROHIBITION_MESSAGE, null);
        }
        
        if (ssos.getOrderRequirements().getForeignTravelProhibitionRequirement().getSelected()
                && !ssos.getOrderRequirements().getForeignTravelProhibitionRequirement().getFromToOption().getSelected()
                && isEmptyString(ssos.getOrderRequirements().getForeignTravelProhibitionRequirement().getDays())) {
        
            log.debug(PROBLEM_ADDED);
            helper.addLogicalProblem(FOREIGN_TRAVEL_PROHIBITION_DAYS_MESSAGE, null);
        }
        
        if (ssos.getOrderRequirements().getForeignTravelProhibitionRequirement().getSelected()
                && ssos.getOrderRequirements().getForeignTravelProhibitionRequirement().getFromToOption().getSelected()) {
            
            Calendar fromDate = ssos.getOrderRequirements().getForeignTravelProhibitionRequirement().getFromToOption().getFromDate().toCalendar();
            Calendar toDate = ssos.getOrderRequirements().getForeignTravelProhibitionRequirement().getFromToOption().getToDate().toCalendar();

            if (fromDate.after(toDate)) {
                log.debug(PROBLEM_ADDED);
                helper.addLogicalProblem(FOREIGN_TRAVEL_PROHIBITION_DATE_MESSAGE, null);
            }
        }
        
        if (ssos.getOrderRequirements().getForeignTravelProhibitionRequirement().getSelected()
                && ssos.getOrderRequirements().getForeignTravelProhibitionRequirement().getExceptionOption().getSelected()
                && isEmptyString(ssos.getOrderRequirements().getForeignTravelProhibitionRequirement().getExceptionOption().getException())){
            
                log.debug(PROBLEM_ADDED);
                helper.addLogicalProblem(FOREIGN_TRAVEL_PROHIBITION_EXCEPTION_MESSAGE, null);
        
        }
    }

    /**
     * Check that the Rehabilitation Activity details have been entered if option selected
     * 
     * @param ssos
     *            the community order sctructure
     * @param helper
     *            the helper
     */
    private static void validateRehabilitationActivityRequirement(SuspendedSentenceOrderStructure ssos, ValidationHelper helper) {
        log.debug("$$$ validateRehabilitationActivityRequirement() ");

        if (ssos.getOrderRequirements().getRehabilitationActivityRequirement().getSelected()
                && isEmptyString(ssos.getOrderRequirements().getRehabilitationActivityRequirement().getDays())) {
            log.debug(PROBLEM_ADDED);
            helper.addLogicalProblem(REHABILITATION_ACTIVITY_DAYS_EXCEPTION_MESSAGE, null);
        }
                
    }

    private static void validateTrailMonitoringRequirement(SuspendedSentenceOrderStructure ssos, ValidationHelper helper) {
        log.debug("$$$ validateTrailMonitoringRequirement() ");

        if (ssos.getOrderRequirements().getTrailMonitoringRequirement().getSelected()
                && isEmptyString(ssos.getOrderRequirements().getTrailMonitoringRequirement().getDuration())) {
            log.debug(PROBLEM_ADDED);
            helper.addLogicalProblem(TRIAL_MONITORING_DURATION_EXCEPTION_MESSAGE, null);
        }           
    }
    
    private static void validateAlcoholAbstinenceRequirement(SuspendedSentenceOrderStructure ssos, ValidationHelper helper) {
        log.debug("$$$ validateAlcoholAbstinenceRequirement() ");

        if (ssos.getOrderRequirements().getAlcoholAbstinenceAndMonitoringRequirement().getSelected()
                && isEmptyString(ssos.getOrderRequirements().getAlcoholAbstinenceAndMonitoringRequirement().getDays())) {
            log.debug(PROBLEM_ADDED);
            helper.addLogicalProblem(ALCOHOL_ABSTINENCE_DAYS_EXCEPTION_MESSAGE, null);
        }           
    }
    
    
    
     private static void validateLocalJusticeArea(SuspendedSentenceOrderStructure ssos, ValidationHelper helper) {
        log.debug("$$$ validateLocalJusticeArea() ");
        if (ssos.getPettySessionalArea().getCourtHouseName() == null
                || ssos.getPettySessionalArea().getCourtHouseName().trim().equals("")) {
            log.debug(PROBLEM_ADDED);
            helper.addLogicalProblem(LOCAL_JUSTICE_AREA_EMPTY, null);
        }
    }
     
     /**
      * Validate the Additional Notes Option
      * 
      * @param ssos
      *            the suspended sentence order
      * @param helper
      *            the helper
      */
     private static void validateAdditionalNotes(SuspendedSentenceOrderStructure ssos, ValidationHelper helper) {
         AdditionalNotes notes = ssos.getAdditionalNotes();
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