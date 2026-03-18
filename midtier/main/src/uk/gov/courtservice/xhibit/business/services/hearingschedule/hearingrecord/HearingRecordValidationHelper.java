package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord;

//jdk
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
import uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant.SchedHearingDefendant;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.shlegrep.ShLegRep;
import uk.gov.courtservice.xhibit.business.entities.shlegrep.ShLegRepMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendant;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;

/**
 * <p>
 * Title: HearingRecordValidationHelper
 * </p>
 * <p>
 * Description: This is a generic class for validation checks within the
 * HearingRecord package.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 * 
 * @history
 * <p>
 * 2003/03/07 MH- added validateCaseTypes(Integer caseID). Commented out
 * validateMPHearingType since no longer required according to requirements.
 * </p>
 * 
 * <p>
 * 2003/06/17 MH - Added method validateDefendantDataWithBailStatus()
 * </p>
 * 
 * <p>
 * 2003/06/20 MH - Made sure that when the HearingRecordDateHelper is populated
 * we don't set the hours, minutes or seconds since this causes problems at
 * times. Also created new method getTransformedDate()
 * </p>
 * 
 */
public class HearingRecordValidationHelper {

    private static Logger log = CSServices.getLogger(HearingRecordValidationHelper.class);

    public HearingRecordValidationHelper() {
        // empty
    }


    /**
     * DefHearingRecord (BR)- If Start and End bail status entered: Valid
     * entries 'B'=bail, 'C'=custody, 'J'=in case or 'N'=not applicable
     * 
     * @param bailStatus
     *            String
     * @throws FormAException
     */
    public void validateBailStatus(String bailStatus) throws FormAException {
        log.debug("HearingRecordValidationHelper.validateBailStatus called");

        if (bailStatus != null) {
            // if the bailstatus is not one of the pre-defined then throw a
            // HrBailStatusException
            if (!(bailStatus.toString().equalsIgnoreCase(HearingRecordConstants.BAIL_STATUS)
                    || bailStatus.toString().equalsIgnoreCase(HearingRecordConstants.CUSTODY_STATUS)
                    || bailStatus.toString().equalsIgnoreCase(HearingRecordConstants.IN_CASE_STATUS) || bailStatus
                    .toString().equalsIgnoreCase(HearingRecordConstants.NOT_APPLICABLE_STATUS))) {
                log.debug("HearingRecordValidationHelper.validateBailStatus will throw HrBailStatusException");
                throw new FormAException(HearingRecordConstants.INVALID_BAIL_STATUS_EXC,
                        "The given bail status is not valid");
            }
        }
        log.debug("HearingRecordValidationHelper.validateBailStatus OK");
    }

    /**
     * Date of application - if preliminary hearing and an application for bail
     * made: Date of application must be within the dates of current hearing.
     * 
     * @param dateHelper
     *            HearingRecordDateHelper
     * @param dateOfApplication
     *            java.util.Date
     * @throws FormAException
     * 
     */
    public void validateApplicationDate(HearingRecordDateHelper dateHelper, Date dateOfApplication)
            throws FormAException {
        log.debug("HearingRecordValidationHelper.validateApplicationDate called");
        Date newDateOfApplication = null;

        // Set a new date of application so that they are all in the same Date
        // format.
        if (dateOfApplication != null && dateHelper.getFirstHearingDate() != null
                && dateHelper.getLastHearingDate() != null) {
            newDateOfApplication = getTransformedDate(new Date(dateOfApplication.getTime()));

            log.debug("new Date of application : " + newDateOfApplication.toString());
            log.debug("start date " + dateHelper.getFirstHearingDate().toString());
            log.debug("end date " + dateHelper.getLastHearingDate().toString());

            // check if the newDateOfApplication is within the date
            // boundaries
            if (newDateOfApplication.before(dateHelper.getFirstHearingDate())
                    || newDateOfApplication.after(dateHelper.getLastHearingDate())) {
                log.debug("HearingRecordValidationHelper.validateApplicationDate will throw "
                        + "HearingRecordApplicationDateException");
                throw new FormAException(HearingRecordConstants.INVALID_DATE_OF_APPLICATION_EXC,
						"The given date of application " + newDateOfApplication.toString()
								+ " is not valid, it must be between " + dateHelper.getFirstHearingDate() + " and "
								+ dateHelper.getLastHearingDate());
            }
        }
        log.debug("HearingRecordValidationHelper.validateApplicationDate OK");
    }

    /**
     * This will populate a HearingRecordHelper with the first and the last date
     * for a hearing as they stand of the time of the method call. This data
     * will not be stored in the hearing table until the end of the hearing but
     * will be calculated every time needed. The firstDate is the first
     * scheduled hearing and the last date is the date of the last scheduled
     * hearing.
     * 
     * @param hearingID
     *            Integer
     * @return HearingRecordDateHelper
     * @throws HearingRecordException
     */
    public HearingRecordDateHelper setFirstAndLastHearingDates(Integer hearingID) throws HearingRecordException {
        log.debug("HearingRecordValidationHelper.setFirstAndLastHearingDates called");

        HearingRecordDateHelper dateHelper = new HearingRecordDateHelper();

        Collection collScheduledHearings = null;
        ArrayList schedHearings = null;
        ScheduledHearing scheduledHearing = null;

        Date firstDate = null;
        Date lastDate = null;
        Date originalDate = null;

        try {
            // get a scheduledhearing maintainer and get scheduledhearings
            // for
            // the hearingID passed in.
            ScheduledHearingMaintainer scheduledHearingMaintainer = new ScheduledHearingMaintainer();
            collScheduledHearings = scheduledHearingMaintainer.findByHearingId(hearingID);
            schedHearings = (ArrayList) collScheduledHearings;

            // get the first and last date.
            for (int i = 0; i < schedHearings.size(); i++) {
                scheduledHearing = (ScheduledHearing) schedHearings.get(i);

                log.debug("Get scheduledHearing with id :" + scheduledHearing.getScheduledHearingId());

                // set the original time - use notbefore when/if original is not
                // populated.
                if (scheduledHearing.getOriginalTime() != null) {
                    // set the original time
                    originalDate = new Date(scheduledHearing.getOriginalTime().getTime());
                } else if (scheduledHearing.getNotBeforeTime() != null) {
                    // set the original time to be notbefore when the
                    // original time has not been populated.
                    originalDate = new Date(scheduledHearing.getNotBeforeTime().getTime());
                }

                // set the first and last date
                if (originalDate != null) {
                    log.debug("BEFORE - originalDate : " + originalDate + ", firstDate : " + firstDate
                            + ", lastDate : " + lastDate);

                    if (firstDate != null && lastDate != null) {
                        if (firstDate.after(originalDate)) {
                            log.debug("Set the firstDate for scheduledHearing with id : "
                                    + scheduledHearing.getScheduledHearingId());
                            firstDate = originalDate;
                        }
                        if (lastDate.before(originalDate)) {
                            log.debug("Set the firstDate for scheduledHearing with id : "
                                    + scheduledHearing.getScheduledHearingId());
                            lastDate = originalDate;
                        }
                    }// if(firstDate != null && lastDate != null)

                    // set the first and last date for the first value only
                    // once!
                    if (firstDate == null) {
                        firstDate = originalDate;
                    }
                    if (lastDate == null) {
                        lastDate = originalDate;
                    }
                }// if(scheduledHearing.getOriginalTime() != null)

                log.debug("After - originalDate : " + originalDate + ", firstDate : " + firstDate + ", lastDate : "
                        + lastDate);
            }// for loop
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            HearingRecordException hex = new HearingRecordException(HearingRecordConstants.SCHED_HEARING_NOT_FOUND, ex
                    .getMessage(), ex);
            throw hex;
        }

        // Re-set the dates without hours, minutes and seconds.
        log.debug("First date before transforming it: " + firstDate);
        firstDate = this.getTransformedDate(firstDate);
        log.debug("First date after transforming it: " + firstDate);

        log.debug("Last date before transforming it: " + lastDate);
        lastDate = this.getTransformedDate(lastDate);
        log.debug("Last date after transforming it: " + lastDate);

        dateHelper.setFirstHearingDate(firstDate);
        dateHelper.setLastHearingDate(lastDate);

        log.debug("HearingRecordValidationHelper.setFirstAndLastHearingDates OK");
        return dateHelper;
    }

    /**
     * This will take in a Date and return the same date but without the hours,
     * minutes and seconds set.
     * 
     * @param inDate -
     *            Date
     * @return Date
     */
    private Date getTransformedDate(Date inDate) {
        Calendar tmpCal = Calendar.getInstance();
        if (inDate != null) {
            tmpCal.setTime(inDate);
            tmpCal.set(tmpCal.get(Calendar.YEAR), tmpCal.get(Calendar.MONTH), tmpCal.get(Calendar.DATE), 0, 0, 0);
        }
        return tmpCal.getTime();
    }

    /**
     * This method will validate that the case is of a valid type. The following
     * cases are valid for creating,viewing, updating, exporting a hearing
     * record: - Committal for Sentence - Trial - Criminal Appeal
     * 
     * If the case is of a valid type the returning boolean will be true
     * otherwise false.
     * 
     * @param caseID
     *            Integer
     * @return Boolean
     * @throws HearingRecordException
     */
    public Boolean validateCaseTypes(Integer caseID) throws HearingRecordException {
        log.debug("HearingRecordValidationHelper.validateCaseTypes(Integer " + caseID + ") called");

        Boolean isValidCaseType = new Boolean(false);
        String caseType = null;
        String caseSubType = null;

        try {
            // get the maintainer and the case.
            CaseMaintainer caseMaintainer = new CaseMaintainer();
            Case caze = caseMaintainer.findByPrimaryKey(caseID);
            caseType = caze.getCaseType();
            caseSubType = caze.getCaseSubType();
        } catch (ObjectNotFoundException ex) {
            // the case could not be found
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            HearingRecordException hex = new HearingRecordException(HearingRecordConstants.CASE_NOT_FOUND, ex
                    .getMessage(), ex);
            throw hex;
        }

        if (caseType != null) {
            log.debug(">>>>> case type : " + caseType + "<<<<<<<");
            log.debug(">>>>> case type : " + caseSubType + "<<<<<<<");

            // ok for com for sentence and trial
            if ((caseType.equalsIgnoreCase(HearingRecordConstants.CASE_TYPE_COM_FOR_SENT))
                    || (caseType.equalsIgnoreCase(HearingRecordConstants.CASE_TYPE_TRIAL))) {
                isValidCaseType = new Boolean(true);
            }
            // not ok for U-cases
            else if (caseType.equalsIgnoreCase(HearingRecordConstants.CASE_TYPE_U_CASE)) {
                isValidCaseType = new Boolean(false);
            }
            // not ok for B-cases
            else if (caseType.equalsIgnoreCase(HearingRecordConstants.CASE_TYPE_BAIL)) {
                isValidCaseType = new Boolean(false);
            }
            // test what type of appeal
            else if (caseType.equalsIgnoreCase(HearingRecordConstants.CASE_TYPE_APPEAL)) {
                // not ok for misc appeal.
                if (caseSubType != null) {
                    if (caseSubType.equalsIgnoreCase(HearingRecordConstants.CASE_SUB_TYPE_MISC_APPEAL)) {
                        isValidCaseType = new Boolean(false);
                    } else {
                        isValidCaseType = new Boolean(true);
                    }
                }
                // ok for any other type of appeal
                else {
                    isValidCaseType = new Boolean(true);
                }
            }
            // not ok for any other type of case
            else {
                isValidCaseType = new Boolean(false);
            }
        }

        log.debug("HearingRecordValidationHelper.validateCaseTypes(Integer " + caseID + ") finished");
        return isValidCaseType;
    }

    /**
     * DEFECT: 53543, 54314 This method will validate the bailStatus for a
     * defendant. If the case type is Trial (T).
     * 
     * If the defendant is company the bail status (if given) must be "Not
     * applicable". If the defendant is not a company the bail status (if given)
     * must be of any other value except from "Not applicable".
     * 
     * Case Type Defendant Type Valid Values in B/C Status Start Hrg
     * ================ ==========================
     * ========================================= Trial Case (T) Male/Female
     * (M/F) Bail/Custody/Jail (B/C/J) Trial Case (T) Company (C) Not Applicable
     * (N) Sentence Case (S) Male/Female/Company (M/F/C) Bail/Custody/Jail/Not
     * Applicable (B/C/J/N) Appeal Case (A) Male/Female/Company (M/F/C)
     * Bail/Custody/Jail/Not Applicable (B/C/J/N)
     * 
     * @param defOnCaseId
     *            Integer
     * @param bailStatus
     *            String
     * @return boolean - true if validation ok, false if validation failed.
     * @throws FormAException
     */
    public boolean validateDefendantDataWithBailStatus(Integer defOnCaseId, String bailStatus, String caseType)
            throws FormAException {
        log.debug("HearingRecordValidationHelper.validateDefendantDataWithBailStatus() called");

        boolean isValid = false;
        Boolean isDefendantCompany = null;

        if (defOnCaseId != null && bailStatus != null && caseType != null) {
            // the validation is only valid for certain case types. If it
            // isn't of
            // a case type that needs validating then just return true.
            if (!caseType.equalsIgnoreCase(HearingRecordConstants.CASE_TYPE_TRIAL)) {
                isValid = true;
                return isValid;
            }

            // Get the gender status - if the defendant is a company or not.
            isDefendantCompany = getDefendantCompanyFlag(defOnCaseId);

            // set the flag that will be returned for one of all possible
            // scenarios.
            if (isDefendantCompany != null) {
                // The defendant is a company and bailstatus is other than
                // NotApplicable - set false
                if (isDefendantCompany.booleanValue()
                        && !bailStatus.equalsIgnoreCase(HearingRecordConstants.NOT_APPLICABLE_STATUS)) {
                    log.debug("Company = true, and bail status is not notApplicable, will throw an exception");
                    throw new FormAException(HearingRecordConstants.INVALID_BAIL_STATUS_DEFENDANT_IS_COMPANY,
                            "Defendant is of type company and the bail flag is not NotApplicable");
                }
                // The defendant is not a company and bailstatus is
                // NotApplicable - set false
                else if ((!isDefendantCompany.booleanValue())
                        && bailStatus.equalsIgnoreCase(HearingRecordConstants.NOT_APPLICABLE_STATUS)) {
                    log.debug("Company = false, and bail status is notApplicable, will throw an exception");
                    throw new FormAException(
                            HearingRecordConstants.INVALID_BAIL_STATUS_DEFENDANT_IS_NOT_COMPANY,
                            "Defendant is not of type company and the bail flag is NotApplicable");
                } else {
                    isValid = true;
                }
            } else {
                isValid = true;
            }
        }
        // The bail status might not always be entered by the user therefore
        // need to cater for when this is null. The boolean to be returned
        // should
        // be set to true.
        else if (bailStatus == null || bailStatus.equals("")) {
            isValid = true;
        }

        log.debug("HearingRecordValidationHelper.validateDefendantDataWithBailStatus() finished");
        return isValid;
    }

    /**
     * Method to return a Boolean. The boolean value will be true if the
     * defendant is a company and false if the defendant is male/female. If the
     * data is not set it will return null.
     * 
     * @param defOnCaseId
     *            Integer
     * @return Boolean true if company, false if femle/male else null.
     */
    private Boolean getDefendantCompanyFlag(Integer defOnCaseId) {
        log.debug("Will try to find the DefendantOnCase with id : " + defOnCaseId);
        XhbDefendant defendant = XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(defOnCaseId).getXhbDefendant();
        
        if (defendant != null) {
            log.debug("Found the defendant with id : " + defendant.getDefendantId());
            log.debug("The flag isCompany from the defendant record is : " + (defendant.getGender().intValue() == 0));
        }
        
        if (defendant != null && defendant.getGender() != null) {
            return new Boolean(defendant.getGender().intValue() == 0);
        }
        return null;
    }

    /**
     * Finds representation for the hearing where the role is defence and the
     * type is barrister. Ensures that the category has been set.
     * 
     * @param hearing
     * @throws HearingRecordException
     */
    public void validateHearingRepresentation(Hearing hearing, Integer defendantOnCaseId) throws HearingRecordException {
        log.debug("validateHearingRepresentation(Hearing hearing) start");
        ShLegRepMaintainer shLegRepMaintainer = new ShLegRepMaintainer();
        Collection schedHearings = hearing.getScheduledHearings();
        Iterator schedHearingsIt = schedHearings.iterator();

        while (schedHearingsIt.hasNext()) {
            ScheduledHearing schedHearing = (ScheduledHearing) schedHearingsIt.next();
            Collection shLegReps = shLegRepMaintainer.findBySHIdRoleAndSolFirmOrRefLegRep(schedHearing
                    .getScheduledHearingId(), HearingRecordConstants.LEGAL_ROLE_DEFENCE,
                    HearingRecordConstants.BARRISTER);
            
            Iterator shLegRepsIt = shLegReps.iterator();
            while (shLegRepsIt.hasNext()) {
                ShLegRep shLegRep = (ShLegRep) shLegRepsIt.next();

                SchedHearingDefendant schedHearingDefendant =
                    shLegRep.getSchedHearingDefendant();
                
                if (schedHearingDefendant == null) {
                    continue;
                }
                else if(!schedHearingDefendant.getDefOnCaseID().equals(defendantOnCaseId)){
                	continue;
                }
                
                XhbDefendantOnCase doc = 
                    XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(
                            schedHearingDefendant.getDefOnCaseID());
                
                if (doc == null) {
                    continue;
                }
                
                final boolean nonObsoleteDefendant = 
                    (doc.getObsInd() == null || !doc.getObsInd().equals("Y"));
                
                // Check that the defence category has been set.
                if (nonObsoleteDefendant
                        && shLegRep.getRefDefenceCategoryId() == null) {
                    
                    throw new HearingRecordException(HearingRecordConstants.CATEGORY_REQUIRED,
                            "Category not set for shLegRep id : " + shLegRep.getShLegRepId()
                                    + " category must be set where the defence representation is a Barrister");
                }
            }
        }
        
        log.debug("validateHearingRepresentation(Hearing hearing) end");
    }

}