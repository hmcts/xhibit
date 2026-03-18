package uk.gov.courtservice.xhibit.xmlbinding.orders;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.AdditionalNotes;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.CommunityOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.YouthRehabilitationOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.ConcurrentType;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.OrderXMLHelper.ValidationHelper;

/**
 * <p>
 * Title: Helper class that populates the Castor bound java-XML object for the
 * XHIBITYouthRehabilitationOrderStructure
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Validates the XHIBITYouthRehabilitationOrderStructure 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Brian Hingston
 * @version 1.0
 */
//
// NOTE:
// when organising the imports in this class, please ensure the inner class
// OrderXMLHelper.ValidationHelper is referenced using '.' rather than '$'
// whilst legal, Jikes advises against it and fails to compile. JP.
//
public class XhibitYROrderValidationHelper {

    // set up logger
    private static final Logger log = CSServices.getLogger(XhibitYROrderValidationHelper.class);

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

    private static final String LOCAL_AUTHORITY_RESIDENCE_AUTHORITY_MESSAGE = "ORDER_Local_Authority_authority_empty";
    
    private static final String FOSTERING_AUTHORITY_MESSAGE = "ORDER_Fostering_authority_empty";
    
    private static final String DRUG_REHAB_DIRECTOR_MESSAGE = "ORDER_Drug_rehab_director_empty";

    private static final String DRUG_TESTING_REQUIREMENTS_MESSAGE = "ORDER_Drug_testing_requirements_empty";

    private static final String DRUG_REHAB_LOCATION_MESSAGE = "ORDER_Drug_rehab_location_empty";

    private static final String INTOXICATING_SUBSTANCE_DIRECTOR_MESSAGE ="ORDER_Intoxicating_substance_director_empty";
    
    private static final String INTOXICATING_SUBSTANCE_LOCATION_MESSAGE ="ORDER_Intoxicating_substance_location_empty";
    
    private static final String MENTAL_TREAT_LOCATION_MESSAGE = "ORDER_Mental_treat_location_empty";

    private static final String ACTIVITY_OPTIONS_MESSAGE = "ORDER_Activity_options_empty";
    
    private static final String EDUCATION_AUTHORITY_MESSAGE = "ORDER_Education_authority_empty";
    
    private static final String EDUCATION_ARRANGEDBY_MESSAGE = "ORDER_Education_arrangedby_empty";
    
    private static final String LOCAL_JUSTICE_AREA_EMPTY = "ORDER_Local_Justice_Area_Empty";
    
    private static final String ADDITIONAL_NOTES_MESSAGE = "ORDER_Additional_Notes_incorrect";
    

    
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
    public static void logicalValidateYouthRehabilitationOrder(YouthRehabilitationOrderStructure yros, String typeCode,
            OrderXMLHelper.ValidationHelper helper) {
        log.debug("$$$ XhibitYouthRehabilitationOrderHelper.logicalValidateYouthRehabilitationOrder");
        validateOrderEndDate(yros, helper);
        validateResponsibleOfficers(yros, helper);
        validateUnpaidWorkRequirement(yros, helper);
        validateActivityRequirement(yros, helper);
        validateProgrammeRequirement(yros, helper);
        validateProhibitedActivityRequirement(yros, helper);
        validateExclusionRequirement(yros, helper);
        validateResidenceRequirement(yros, helper);
        validateLocalAuthorityResidenceRequirement(yros, helper);
        validateFosteringRequirement(yros, helper);
        validateDrugRehabRequirement(yros, helper);
        validateDrugTestingRequirement(yros, helper);
        validateIntoxicatingSubstanceRequirement(yros, helper);
        validateMentalHealthTreatmentRequirement(yros, helper);
        validateEducationRequirement(yros, helper);
        validateLocalJusticeArea(yros, helper);
        validateAdditionalNotes(yros, helper);
    }

    

    /**
     * Check that the Order End Date is not greater than 3 years after the order
     * date
     * 
     * @param yros
     *            the youth rehabilitation order sctructure
     * @param helper
     *            the helper
     */
    private static void validateOrderEndDate(YouthRehabilitationOrderStructure yros, ValidationHelper helper) {
        log.debug("$$$ validateOrderEndDate() " + yros.getOrderHeader().getDefendant().getCharges().getChargeCount());
        Calendar today = Calendar.getInstance();
        Calendar compDate = yros.getCompletionDate().toCalendar();

        int age = compDate.get(Calendar.YEAR) - today.get(Calendar.YEAR);
        compDate.set(Calendar.YEAR, today.get(Calendar.YEAR));
        if (today.after(compDate)) {
            age--;
        }

        if (age >= 3) {
            log.debug("*************** PROBLEM ADDED");
            helper.addLogicalProblem(END_DATE_3_YEARS_MESSAGE, null);
        }
    }

    /**
     * Check that the Responsible Officers 1 & 2 have been populated
     * 
     * @param yros
     *            the youth rehabilitation order sctructure
     * @param helper
     *            the helper
     */
    private static void validateResponsibleOfficers(YouthRehabilitationOrderStructure yros, ValidationHelper helper) {
        log.debug("$$$ validateResponsibleOfficer1() " + yros.getResponsibleOfficer1());
        if (isEmptyString(yros.getResponsibleOfficer1())) {
            log.debug("*************** PROBLEM ADDED");
            helper.addLogicalProblem(RESP_OFFICER_1_MESSAGE, null);
        }

        log.debug("$$$ validateResponsibleOfficer2() " + yros.getResponsibleOfficer2());
        if (isEmptyString(yros.getResponsibleOfficer2())) {
            log.debug("*************** PROBLEM ADDED");
            helper.addLogicalProblem(RESP_OFFICER_2_MESSAGE, null);
        }
    }

    /**
     * Check that the Unpaid work details have been entered if option selected
     * 
     * @param yros
     *            the youth rehabilitation order sctructure
     * @param helper
     *            the helper
     */
    private static void validateUnpaidWorkRequirement(YouthRehabilitationOrderStructure yros, ValidationHelper helper) {
        log.debug("$$$ validateUnpaidWorkRequirement() ");
        int hours = yros.getYouthRehabilitationOrderRequirements().getUnpaidWorkRequirement().getHours();
        if (yros.getYouthRehabilitationOrderRequirements().getUnpaidWorkRequirement().getSelected() &&  hours > 300) {
            log.debug("*************** PROBLEM ADDED");
            helper.addLogicalProblem(UNPAID_WORK_HOURS_MESSAGE, null);
        }

        if (yros.getYouthRehabilitationOrderRequirements().getUnpaidWorkRequirement().getSelected()
                && yros.getYouthRehabilitationOrderRequirements().getUnpaidWorkRequirement().getConcurrent().getType() != ConcurrentType.NONE_TYPE
                && isEmptyString(yros.getYouthRehabilitationOrderRequirements().getUnpaidWorkRequirement().getWorkDetails())) {
            log.debug("*************** PROBLEM ADDED");
            helper.addLogicalProblem(UNPAID_WORK_DETAIL_MESSAGE, null);
        }
    }

    /**
     * Check that the Activity Requirement details have been entered if option
     * selected
     * 
     * @param yros
     *            the youth rehabilitation order sctructure
     * @param helper
     *            the helper
     */
    private static void validateActivityRequirement(YouthRehabilitationOrderStructure yros, ValidationHelper helper) {
        log.debug("$$$ validateActivityRequirement() ");
        if (yros.getYouthRehabilitationOrderRequirements().getActivityRequirement().getSelected()) {
            if (!yros.getYouthRehabilitationOrderRequirements().getActivityRequirement().getPresentDetails().getSelected()
                    && !yros.getYouthRehabilitationOrderRequirements().getActivityRequirement().getActivityDetails().getSelected()
                    && !yros.getYouthRehabilitationOrderRequirements().getActivityRequirement().getAdditonalRequirements().getSelected()) {
                log.debug("*************** PROBLEM ADDED - person");
                helper.addLogicalProblem(ACTIVITY_OPTIONS_MESSAGE, null);
            }

            if (yros.getYouthRehabilitationOrderRequirements().getActivityRequirement().getPresentDetails().getSelected()) {
                log.debug("*****************PresentDetails Selected");
                if (isEmptyString(yros.getYouthRehabilitationOrderRequirements().getActivityRequirement().getPresentDetails().getPerson())) {
                    log.debug("*************** PROBLEM ADDED - person");
                    helper.addLogicalProblem(ACTIVITY_PERSON_MESSAGE, null);
                }
                if (isEmptyString(yros.getYouthRehabilitationOrderRequirements().getActivityRequirement().getPresentDetails().getPlace()
                        .getSite())) {
                    log.debug("*************** PROBLEM ADDED - place");
                    helper.addLogicalProblem(ACTIVITY_PLACE_MESSAGE, null);
                }

            }
            if (yros.getYouthRehabilitationOrderRequirements().getActivityRequirement().getActivityDetails().getSelected()) {
                log.debug("*****************ActivityDetails Selected");
                if (isEmptyString(yros.getYouthRehabilitationOrderRequirements().getActivityRequirement().getActivityDetails()
                        .getActivity())) {
                    log.debug("*************** PROBLEM ADDED");
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
     *            the youth rehabilitation order sctructure
     * @param helper
     *            the helper
     */
    private static void validateProgrammeRequirement(YouthRehabilitationOrderStructure yros, ValidationHelper helper) {
        log.debug("$$$ validateProgrammeRequirement() ");
        if (yros.getYouthRehabilitationOrderRequirements().getProgrammeRequirement().getSelected()) {
            log.debug("*****************Programme Selected");
            if (isEmptyString(yros.getYouthRehabilitationOrderRequirements().getProgrammeRequirement().getProgramme())) {
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(PROGRAMME_PROGRAMME_MESSAGE, null);
            }
            log.debug("*****************Place Selected");
            if (isEmptyString(yros.getYouthRehabilitationOrderRequirements().getProgrammeRequirement().getPlace().getSite())) {
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(PROGRAMME_LOCATION_MESSAGE, null);
            }
        }
    }

    /**
     * Check that the Prohibited ACtivity Requirement details have been entered
     * if option selected
     * 
     * @param yros
     *            the youth rehabilitation order sctructure
     * @param helper
     *            the helper
     */
    private static void validateProhibitedActivityRequirement(YouthRehabilitationOrderStructure yros, ValidationHelper helper) {
        log.debug("$$$ validateProhibitedActivityRequirement() ");
        if (yros.getYouthRehabilitationOrderRequirements().getProhibitedActivityRequirement().getSelected()) {
            if (isEmptyString(yros.getYouthRehabilitationOrderRequirements().getProhibitedActivityRequirement().getActivity())) {
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(PROHIB_ACTIVITY_MESSAGE, null);
            }
        }
    }

    /**
     * Check that the Exclusion Requirement details have been entered if option
     * selected
     * 
     * @param cos
     *            the youth rehabilitation order sctructure
     * @param helper
     *            the helper
     */
    private static void validateExclusionRequirement(YouthRehabilitationOrderStructure yros, ValidationHelper helper) {
        log.debug("$$$ validateExclusionRequirement() ");
        if (yros.getYouthRehabilitationOrderRequirements().getExclusionRequirement().getSelected()) {
            log.debug("*****************Place Selected");
            if (isEmptyString(yros.getYouthRehabilitationOrderRequirements().getExclusionRequirement().getPlace().getSite())) {
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(EXCLUSION_PLACE_MESSAGE, null);
            }
            if (yros.getYouthRehabilitationOrderRequirements().getExclusionRequirement().getBetweenPeriod().getSelected()) {
                log.debug("*****************BetweenPeriod Selected");
                if (isEmptyString(yros.getYouthRehabilitationOrderRequirements().getExclusionRequirement().getBetweenPeriod()
                        .getApplicablePeriod())) {
                    log.debug("*************** PROBLEM ADDED");
                    helper.addLogicalProblem(EXCLUSION_PERIOD_MESSAGE, null);
                }

            }
        }
    }

    /**
     * Check that the Residence Requirement details have been entered if option
     * selected
     * 
     * @param yros
     *            the youth rehabilitation order sctructure
     * @param helper
     *            the helper
     */
    private static void validateResidenceRequirement(YouthRehabilitationOrderStructure yros, ValidationHelper helper) {
        log.debug("$$$ validateResidenceRequirement() ");
        if (yros.getYouthRehabilitationOrderRequirements().getResidenceRequirement().getSelected()) {
            log.debug("*****************Hostel Selected");
            if (isEmptyString(yros.getYouthRehabilitationOrderRequirements().getResidenceRequirement().getHostel().getSite())) {
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(RESIDENCE_HOSTEL_MESSAGE, null);
            }
            if (yros.getYouthRehabilitationOrderRequirements().getResidenceRequirement().getPlaceOption().getSelected()) {
                log.debug("*****************PlaceOption Selected");
                if (isEmptyString(yros.getYouthRehabilitationOrderRequirements().getResidenceRequirement().getPlaceOption().getPlace()
                        .getSite())) {
                    log.debug("*************** PROBLEM ADDED");
                    helper.addLogicalProblem(RESIDENCE_OTHERPLACE_MESSAGE, null);
                }
            }
        }
    }
    
    /**
     * Check that the Local Authority Residence Requirement details have been entered if option
     * selected
     * 
     * @param yros
     *            the youth rehabilitation order sctructure
     * @param helper
     *            the helper
     */
    private static void validateLocalAuthorityResidenceRequirement(YouthRehabilitationOrderStructure yros, ValidationHelper helper) {
        log.debug("$$$ validateResidenceRequirement() ");
        if (yros.getYouthRehabilitationOrderRequirements().getLocalAuthorityResidenceRequirement().getSelected()) {
            log.debug("*****************LocalAuthority Selected");
            if (isEmptyString(yros.getYouthRehabilitationOrderRequirements().getLocalAuthorityResidenceRequirement().getLocalAuthority())) {
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(LOCAL_AUTHORITY_RESIDENCE_AUTHORITY_MESSAGE, null);
            }
            
        }
    }

    /**
     * Check that the Fostering Requirement details have been entered if option
     * selected
     * 
     * @param yros
     *            the youth rehabilitation order sctructure
     * @param helper
     *            the helper
     */
    private static void validateFosteringRequirement(YouthRehabilitationOrderStructure yros, ValidationHelper helper) {
        log.debug("$$$ validateResidenceRequirement() ");
        if (yros.getYouthRehabilitationOrderRequirements().getFosteringRequirement().getSelected()) {
            log.debug("*****************Fostering Selected");
            if (isEmptyString(yros.getYouthRehabilitationOrderRequirements().getFosteringRequirement().getLocalAuthority())) {
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(FOSTERING_AUTHORITY_MESSAGE, null);
            }
            
        }
    }
    
    /**
     * Check that the Drug Rehab Requirement details have been entered if option
     * selected
     * 
     * @param yros
     *            the youth rehabilitation order sctructure
     * @param helper
     *            the helper
     */
    private static void validateDrugRehabRequirement(YouthRehabilitationOrderStructure yros, ValidationHelper helper) {
        log.debug("$$$ validateDrugRehabRequirement() "
                + yros.getYouthRehabilitationOrderRequirements().getDrugRehabilitationRequirement().getSelected());
        if (yros.getYouthRehabilitationOrderRequirements().getDrugRehabilitationRequirement().getSelected()) {
            log.debug("$$$ Director "
                    + yros.getYouthRehabilitationOrderRequirements().getDrugRehabilitationRequirement().getTreatmentDirector());
            if (isEmptyString(yros.getYouthRehabilitationOrderRequirements().getDrugRehabilitationRequirement().getTreatmentDirector())) {
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(DRUG_REHAB_DIRECTOR_MESSAGE, null);
            }

            log.debug("$$$ validateDrugRehabRequirement() "
                    + yros.getYouthRehabilitationOrderRequirements().getDrugRehabilitationRequirement().getTreatmentOption().getSelected());
            if (yros.getYouthRehabilitationOrderRequirements().getDrugRehabilitationRequirement().getTreatmentOption().getSelected()) {
                log.debug("$$$ Clinic "
                        + yros.getYouthRehabilitationOrderRequirements().getDrugRehabilitationRequirement().getTreatmentOption()
                                .getTreatmentLocation().getSite());
                if (isEmptyString(yros.getYouthRehabilitationOrderRequirements().getDrugRehabilitationRequirement().getTreatmentOption()
                        .getTreatmentLocation().getSite())) {
                    log.debug("*************** PROBLEM ADDED");
                    helper.addLogicalProblem(DRUG_REHAB_LOCATION_MESSAGE, null);
                }

            }
        }
    }
    
    /**
     * Check that the Drug Testing Requirement details have been entered if option
     * selected
     * 
     * @param yros
     *            the youth rehabilitation order sctructure
     * @param helper
     *            the helper
     */
    private static void validateDrugTestingRequirement(YouthRehabilitationOrderStructure yros, ValidationHelper helper) {
        log.debug("$$$ validateDrugTestingRequirement() "
                + yros.getYouthRehabilitationOrderRequirements().getDrugTestingRequirement().getSelected());
        if (yros.getYouthRehabilitationOrderRequirements().getDrugTestingRequirement().getSelected()) {
            log.debug("$$$ requirements "
                    + yros.getYouthRehabilitationOrderRequirements().getDrugTestingRequirement().getRequirements());
            if (isEmptyString(yros.getYouthRehabilitationOrderRequirements().getDrugTestingRequirement().getRequirements()) &&
                    yros.getYouthRehabilitationOrderRequirements().getDrugTestingRequirement().getTestingType().toString() =="Requirements") {
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(DRUG_TESTING_REQUIREMENTS_MESSAGE, null);
            }

        }
    }
    


    /**
     * Check that the Intoxicating Substance Requirement details have been entered if
     * option selected
     * 
     * @param yros
     *            the youth rehabilitation order sctructure
     * @param helper
     *            the helper
     */
    private static void validateIntoxicatingSubstanceRequirement(YouthRehabilitationOrderStructure yros, ValidationHelper helper) {
        log.debug("$$$ validateIntoxicatingSubstanceRequirement() "
            + yros.getYouthRehabilitationOrderRequirements().getIntoxicatingSubstanceRequirement().getSelected());
        if (yros.getYouthRehabilitationOrderRequirements().getIntoxicatingSubstanceRequirement().getSelected()) {
            log.debug("$$$ Director "
                + yros.getYouthRehabilitationOrderRequirements().getIntoxicatingSubstanceRequirement().getTreatmentDirector());
            if (isEmptyString(yros.getYouthRehabilitationOrderRequirements().getIntoxicatingSubstanceRequirement().getTreatmentDirector())) {
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(INTOXICATING_SUBSTANCE_DIRECTOR_MESSAGE, null);
            }

            log.debug("$$$ Treatment option "
                + yros.getYouthRehabilitationOrderRequirements().getIntoxicatingSubstanceRequirement().getTreatmentOption().getSelected());
            if (yros.getYouthRehabilitationOrderRequirements().getIntoxicatingSubstanceRequirement().getTreatmentOption().getSelected()) {
                log.debug("$$$ Location "
                    + yros.getYouthRehabilitationOrderRequirements().getIntoxicatingSubstanceRequirement().getTreatmentOption()
                            .getTreatmentLocation().getSite());
                if (isEmptyString(yros.getYouthRehabilitationOrderRequirements().getIntoxicatingSubstanceRequirement().getTreatmentOption()
                    .getTreatmentLocation().getSite())) {
                    log.debug("*************** PROBLEM ADDED");
                    helper.addLogicalProblem(INTOXICATING_SUBSTANCE_LOCATION_MESSAGE, null);
                }

            }
        }
    }

    
    /**
     * Check that the Mental Health Treatment Requirement details have been
     * entered if option selected
     * 
     * @param yros
     *            the youth rehabilitation order sctructure
     * @param helper
     *            the helper
     */
    private static void validateMentalHealthTreatmentRequirement(YouthRehabilitationOrderStructure yros, ValidationHelper helper) {
        log.debug("$$$ validateMentalHealthTreatmentRequirement() "
                + yros.getYouthRehabilitationOrderRequirements().getMentalHealthTreatmentRequirement().getSelected());
        if (yros.getYouthRehabilitationOrderRequirements().getMentalHealthTreatmentRequirement().getSelected()) {
            log.debug("$$$ Treatment option "
                    + yros.getYouthRehabilitationOrderRequirements().getMentalHealthTreatmentRequirement().getTreatmentOption()
                            .getSelected());
            if (yros.getYouthRehabilitationOrderRequirements().getMentalHealthTreatmentRequirement().getTreatmentOption().getSelected()) {
                log.debug("$$$ Location "
                        + yros.getYouthRehabilitationOrderRequirements().getMentalHealthTreatmentRequirement().getTreatmentOption()
                                .getTreatmentLocation().getSite());
                if (isEmptyString(yros.getYouthRehabilitationOrderRequirements().getMentalHealthTreatmentRequirement().getTreatmentOption()
                        .getTreatmentLocation().getSite())) {
                    log.debug("*************** PROBLEM ADDED");
                    helper.addLogicalProblem(MENTAL_TREAT_LOCATION_MESSAGE, null);
                }
            }
        }
    }
    
    /**
     * Check that the Education Requirement details have been entered if option
     * selected
     * 
     * @param yros
     *            the youth rehabilitation order sctructure
     * @param helper
     *            the helper
     */
    private static void validateEducationRequirement(YouthRehabilitationOrderStructure yros, ValidationHelper helper) {
        log.debug("$$$ validateResidenceRequirement() ");
        if (yros.getYouthRehabilitationOrderRequirements().getEducationRequirement().getSelected()) {
            log.debug("*****************Local Authority Selected");
            if (isEmptyString(yros.getYouthRehabilitationOrderRequirements().getEducationRequirement().getLocalAuthority())) {
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(EDUCATION_AUTHORITY_MESSAGE, null);
            }
            log.debug("*****************Arranged By Selected");
            if (isEmptyString(yros.getYouthRehabilitationOrderRequirements().getEducationRequirement().getArrangedBy())) {
                log.debug("*************** PROBLEM ADDED");
                helper.addLogicalProblem(EDUCATION_ARRANGEDBY_MESSAGE, null);
            }
        }
    }
    
    
    
    
    
    private static void validateLocalJusticeArea(YouthRehabilitationOrderStructure yros, ValidationHelper helper) {
        log.debug("$$$ validateLocalJusticeArea() ");
        if (yros.getPettySessionalArea().getCourtHouseName() == null
                || yros.getPettySessionalArea().getCourtHouseName().trim().equals("")) {
            log.debug("*************** PROBLEM ADDED");
            helper.addLogicalProblem(LOCAL_JUSTICE_AREA_EMPTY, null);
        }
    }
    
    /**
     * Validate the Additional Notes Option
     * 
     * @param yros
     *            the Youth Rehabilitation order
     * @param helper
     *            the helper
     */
    private static void validateAdditionalNotes(YouthRehabilitationOrderStructure yros, ValidationHelper helper) {
        AdditionalNotes notes = yros.getAdditionalNotes();
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