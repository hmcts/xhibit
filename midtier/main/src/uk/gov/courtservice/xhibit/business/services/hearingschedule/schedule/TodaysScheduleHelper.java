package uk.gov.courtservice.xhibit.business.services.hearingschedule.schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendant.Defendant;
import uk.gov.courtservice.xhibit.business.entities.defendant.DefendantMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
import uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendee;
import uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant.SchedHearingDefendant;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.sitting.Sitting;
import uk.gov.courtservice.xhibit.business.entities.sitting.SittingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;

/**
 * <p>
 * Title: TodaysScheduleHelper
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Babad
 * @version $Id: TodaysScheduleHelper.java,v 1.21 2009/01/12 17:40:06 hewittm Exp $
 * 
 * <Change History/>
 * 
 * <P>
 * 19/02/03 - JB - Created.
 * </P>
 * <P>
 * 25/02/03 - JB - Changed to use latest sysadmin VOs.
 * </P>
 * Fills in isFloating on the SchedHearing VO.
 * </P>
 * <P>
 * 26/02/03 - JB - Change to strip time from the date passed in to
 * getTodaysSchedule.
 * </P>
 * <P>
 * 26/02/03 - JB - Now using getId
 * </P>
 * <P>
 * 03/03/03 - JB - Factored out common code that creates ScheduleHearingValue
 * </P>
 * Insert null - if the judge ID is null (rather than throw exception)
 * </P>
 * <P>
 * 04/03/03 - JB - Fix to handle VOs from bisref controller
 * </P>
 * <P>
 * 12/03/03 - JB - Now put the defsOnCase into the SchedHearingValue
 * </P>
 */
public class TodaysScheduleHelper {
    private static final Logger log = CSServices.getLogger(TodaysScheduleHelper.class);

    private CaseMaintainer caseMaintainer;

    private DefendantMaintainer defendantMaintainer;

    private DefendantOnCaseMaintainer defendantOnCaseMaintainer;

    private SittingMaintainer sittingMaintainer;

    private ScheduledHearingMaintainer scheduledHearingMaintainer;

    private ScheduleHelper scheduleHelper;

    /**
     * Default constructor that instantiate necessary maintainers and helpers.
     */
    public TodaysScheduleHelper() {
        caseMaintainer = new CaseMaintainer();
        defendantMaintainer = new DefendantMaintainer();
        defendantOnCaseMaintainer = new DefendantOnCaseMaintainer();
        sittingMaintainer = new SittingMaintainer();
        scheduledHearingMaintainer = new ScheduledHearingMaintainer();
        scheduleHelper = new ScheduleHelper();
    }

    /**
     * Method to get an array of ScheduledHearingValues.
     * 
     * @param scheduledHearingIds
     *            Integer[]
     * @return ScheduledHearingValue[]
     * @throws HearingScheduleException
     */
    public ScheduledHearingValue[] getScheduledHearings(Integer[] scheduledHearingIds) throws HearingScheduleException {
        ScheduledHearingValue[] schedHearingValues = null;

        try {
            int size = scheduledHearingIds.length;
            // Instantiate the Array to hold the ScheduledHearingValues
            schedHearingValues = new ScheduledHearingValue[size];

            for (int i = 0; i < size; i++) {
                ScheduledHearing sh = scheduledHearingMaintainer.findByPK(scheduledHearingIds[i]);

                // Get Sitting...
                Sitting s1 = sh.getSitting();
                SittingBasicValue sbv = sittingMaintainer.getSittingBasicValue(s1);

                // Create ScheduledHearingValue...
                ScheduledHearingValue shcv = getSchedHearingValue(sh, sbv);

                schedHearingValues[i] = shcv;
            }
        } catch (ObjectNotFoundException onfe) {
            CSServices.getDefaultErrorHandler().handleError(onfe, getClass(), onfe.toString());
            throw new HearingScheduleException("", onfe.toString(), onfe);
        }

        return schedHearingValues;
    }

    // --------------------------------- Private Methods
    // -----------------------

    /**
     * Get a ScheduledHearingValue from the entity ScheduledHearing and
     * SittingBasicValue
     * 
     * @param sh
     *            ScheduledHearing
     * @param sbv
     *            SittingBasicValue
     * @return ScheduledHearingValue
     * @throws HearingScheduleException
     * @throws ObjectNotFoundException
     */
    private ScheduledHearingValue getSchedHearingValue(ScheduledHearing sh, SittingBasicValue sbv)
            throws HearingScheduleException, ObjectNotFoundException {
        String methodName = "getSchedHearingValue() - ";
        log.debug(methodName + "called :: sh Id:" + sh.getScheduledHearingId() + "Sitting Basic Value: " + sbv);

        String judgeName = null;

        ScheduledHearingBasicValue scheduledHearingBasicValue = scheduledHearingMaintainer
                .getScheduledHearingBasicValue(sh);
        if (log.isDebugEnabled())
            log.debug(methodName + "Scheduled Hearing Id: " + scheduledHearingBasicValue.getId());

        CourtRoomBasicValue crbv = scheduleHelper.getCourtRoom(sbv.getCourtRoomID());
        if (log.isDebugEnabled())
            log.debug(methodName + "Court Room Details : " + crbv);

        // Find Judge... try SHJudge first...otherwise get from sitting
        Integer shJudgeId = getSHJudgeId(sh);
        if (shJudgeId == null)
            shJudgeId = sbv.getRefJudgeID();
        if (shJudgeId != null)
            judgeName = scheduleHelper.getJudgeName(shJudgeId);
        if (log.isDebugEnabled())
            log.debug(methodName + "Judge: " + judgeName);

        // String courtRoomName = crbv.getCourtRoomName();
        // CMR to get Hearing....
        Hearing hearing = sh.getHearing();

        // RHT Value
        RefHearingTypeBasicValue rhtv = scheduleHelper.getRefHearingInfo(hearing.getRefHearingTypeId());
        if (log.isDebugEnabled())
            log.debug(methodName + "RefHearingTypeBasicValue: " + rhtv);

        // Case...
        Case myCase = caseMaintainer.findByPrimaryKey(hearing.getCaseId());
        CaseBasicValue cbv = caseMaintainer.getCaseBasicValue(myCase);
        if (log.isDebugEnabled())
            log.debug(methodName + "CaseBasicValue: " + cbv);

        // need the listed defendants only
        Collection shDefs = sh.getSchedHearingDefendant();
        Iterator shDefsIt = shDefs.iterator();
        ArrayList<DefendantBasicValue> defsOnCase = 
            new ArrayList<DefendantBasicValue>();
        ArrayList<DefendantOnCaseBasicValue> defOnCaseBasicValues = 
            new ArrayList<DefendantOnCaseBasicValue>();

        // create a collection of defendant basic values and a collection of
        // defendant on case basic values containing LISTED defendants for this
        // sh
        while (shDefsIt.hasNext()) {
            SchedHearingDefendant shDef = (SchedHearingDefendant) shDefsIt.next();

            // cmr with defendant on case not set up, when new entity layer
            // is
            // incorporated the cmr can be used and this look up simplified
            Integer defOncaseId = shDef.getDefOnCaseID();
            
            DefendantOnCase doc = defendantOnCaseMaintainer.findByPrimaryKey(defOncaseId);
            
            // PR5673
            try {
                // findByDefendantAndCase only returns values that are not obsolete.
                // Use this to check that the defendant has not had obs_ind set to "Y".
                XhbDefendantOnCaseBeanHelper2.findByDefendantAndCase(
                        doc.getDefendantId(), doc.getCaseId());
            } catch (XhbDefendantOnCaseBeanNotFoundException notFound) {
                if (log.isDebugEnabled()) {
                    log.debug(methodName + "defendant id " + doc.getDefendantId() + " is obsolete"); 
                }
                continue;
            }
            
            DefendantOnCaseBasicValue docBV = defendantOnCaseMaintainer.getDefendantOnCaseBasicValue(doc);
            Defendant defendant = doc.getDefendant();
            DefendantBasicValue defBV = defendantMaintainer.getDefendantBasicValue(defendant);
            defsOnCase.add(defBV);
            defOnCaseBasicValues.add(docBV);
        }

        // Build the VO
        ScheduledHearingValue shcv = new ScheduledHearingValue();
        // Sched Hearing...
        shcv.setScheduledHearingBasicValue(scheduledHearingBasicValue);
        // shcv.setScheduledHearingId(scheduledHearingBasicValue.getId());
        // Case Basic VO
        shcv.setCaseBasicValue(cbv);
        
        if (log.isDebugEnabled())
            log.debug(methodName + "Defendants on case = " + defsOnCase.size());
        
        // Defendant Info...
        shcv.setDefendantsOnCase(defsOnCase);

        // Def on Case Info...
        shcv.setDefendantOnCaseBasicValues(defOnCaseBasicValues);

        // Hearing type info...
        shcv.setRefHearingTypeBasicValue(rhtv);
        // shcv.setHearingType(rhtv.getHearingTypeCode());
        // shcv.setHearingTypeDesc(rhtv.getHearingTypeDesc());

        // Judge name
        shcv.setJudge(judgeName);
        // Judge id
        shcv.setRefJudgeId(shJudgeId);
        // Court Room Info
        shcv.setCourtRoomValue(crbv);

        // Not before time...
        // shcv.setNotBeforeTime(scheduledHearingBasicValue.getNotBeforeTime());
        // Sequence No.
        // shcv.setSequenceNo(scheduledHearingBasicValue.getSequenceNo());
        // CourtRoomId
        // shcv.setCourtRoomId(sbv.getCourtRoomID());
        // Sitting SequenceNo.
        shcv.setSittingSequenceNo(sbv.getSittingSequenceNo());
        // Floating...
        Boolean isFloating = null;
        if (sbv.getIsFloating() != null) {
            isFloating = sbv.getIsFloating().equals("1") ? new Boolean(true) : new Boolean(false);
        }
        shcv.setIsFloating(isFloating);

        // Hearing List Start Date added for bug 54633
        Calendar hearingListStartDate = Calendar.getInstance();
        hearingListStartDate.setTime(sh.getSitting().getHearingList().getStartDate());
        shcv.setHearingListStartDate(hearingListStartDate);

        if (log.isDebugEnabled())
            log.debug(methodName + "Exited OK :: SchedHearingValue: " + shcv);
        return shcv;
    }

    /**
     * Get the Judge Id from the Scheduled Hearing.
     * 
     * @param scheduledHearing
     *            ScheduledHearing
     * @return Integer Sched Hearing judgeId
     */
    private Integer getSHJudgeId(ScheduledHearing scheduledHearing) {
        String methodName = "getSHJudgeId() - ";
        log.debug(methodName + "called :: Scheduled Hearing Id: " + scheduledHearing.getScheduledHearingId());

        Collection shAttendees = scheduledHearing.getScheduledHearingAttendee();
        Integer judgeId = null;
        Iterator it = shAttendees.iterator();
        while (it.hasNext()) {
            SchedHearingAttendee sha = (SchedHearingAttendee) it.next();
            if (sha.getAttendeeType().equals("J")) {
                judgeId = sha.getRefJudgeId();
                break;
            }
        }

        log.debug(methodName + "Exited OK :: judgeId: " + judgeId);
        return judgeId;
    }
}
