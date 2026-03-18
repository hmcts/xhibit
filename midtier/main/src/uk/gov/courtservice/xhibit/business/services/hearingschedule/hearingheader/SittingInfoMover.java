package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingheader;

/**
 * <p>Title: SittingInfoMover</p>
 * <p>Description: Uses the ScheduledHearing and Sitting to create
 * ScheduledHearingAttendee and SHJustice where necessary</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Paul Morris
 * @version 1.0
 */
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Date;

import javax.ejb.EJBException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.ErrorHandler;
import uk.gov.courtservice.xhibit.business.entities.refjustice.RefJusticeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendee;
import uk.gov.courtservice.xhibit.business.entities.schedhearingattendee.SchedHearingAttendeeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.shjustice.ShJustice;
import uk.gov.courtservice.xhibit.business.entities.shjustice.ShJusticeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.sitting.Sitting;
import uk.gov.courtservice.xhibit.business.entities.sitting.SittingMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.SHJusticeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SchedHearingAttendeeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;

public class SittingInfoMover {
    private static final String ATTENDEE_TYPE_JUDGE = "J";

    private static final String ATTENDEE_TYPE_JUSTICE_OF_THE_PEACE = "JP";
    
    private static final String AND_WITH = "\nand with s = ";
    
    private static final String END_METHOD = "end method: hasSittingInfoBeenMoved(ScheduledHearing)";

    private static Logger logger = CSServices.getLogger(SittingInfoMover.class);

    private static ErrorHandler errorHandler = CSServices.getDefaultErrorHandler();

    private RefJusticeMaintainer refJusticeMaintainer;

    private SchedHearingAttendeeMaintainer scheduledHearingAttendeeMaintainer;

    private ScheduledHearingMaintainer scheduledHearingMaintainer;

    private ShJusticeMaintainer shJusticeMaintainer;

    private SittingMaintainer sittingMaintainer;

    private String justice1Name;

    private String justice2Name;

    private String justice3Name;

    private String justice4Name;

    {
        logger.debug("start method: initializer");

        refJusticeMaintainer = new RefJusticeMaintainer();
        scheduledHearingAttendeeMaintainer = new SchedHearingAttendeeMaintainer();
        scheduledHearingMaintainer = new ScheduledHearingMaintainer();
        sittingMaintainer = new SittingMaintainer();
        shJusticeMaintainer = new ShJusticeMaintainer();

        logger.debug("end method: initializer");
    }

    /**
     * Moves the data from a scheduled hearing over to the
     * ScheduledHearingAttendee and SHJustice records.
     * 
     * @param shID
     *            the scheduled hearing id
     */
    public void moveSittingInfo(Integer shID, String userDisplayName) {
        logger.debug("start method: moveSittingInfo(shID) with shID = " + shID);

        try {
            ScheduledHearing scheduledHearing = scheduledHearingMaintainer.findByPK(shID);
            if (!hasSittingInfoBeenMoved(scheduledHearing, userDisplayName)) {
                Sitting sitting = sittingMaintainer.findByPK(scheduledHearing.getSittingId());

                setupJusticeNames(sitting);
                moveData(scheduledHearing, sitting, userDisplayName);
            }
        } catch (ObjectNotFoundException onfe) {
            errorHandler.handleError(onfe, SittingInfoMover.class);
            throw new EJBException(onfe);
        }

        logger.debug("end method: moveSittingInfo(shID) with shID = " + shID);
    }

    /**
     * Checks if the sitting info has been moved or not already
     * 
     * If the scheduled hearing start date has been set then this case has been
     * opened already and the sitting info moved.
     * 
     * If the scheduled hearing start date has not been set then the case has
     * not been opened, however if the hearing has been moved from another court
     * room the judge may have already been assigned, in this case we want to
     * set the start date, but not move the sitting info
     * 
     * @param shID
     *            the scheduled hearing id
     * @return whether or not the sitting info has already been moved
     */
    private boolean hasSittingInfoBeenMoved(ScheduledHearing scheduledHearing, String userDisplayName) {
        logger.debug("start method: hasSittingInfoBeenMoved(ScheduledHearing)");

        // if start date is not set then this is the first time we've opened
        // this
        // scheduled hearing
        if (scheduledHearing.getStartTime() == null) {
            // set the start time for the scheduled hearing
            setScheduledHearingStartTime(scheduledHearing, userDisplayName);

            // now check if we already have a judge - which could be
            // possible if the
            // case was moved from another court room
            Collection attendees = null;

            attendees = scheduledHearingAttendeeMaintainer.findByScheduledHearingId(scheduledHearing
                    .getScheduledHearingId());

            Iterator it = attendees.iterator();

            while (it.hasNext()) {
                SchedHearingAttendee sha = (SchedHearingAttendee) it.next();
                if (sha.getShJusticeId() != null || sha.getRefJudgeId() != null) {
                    logger.debug(END_METHOD);
                    // return sitting info has been moved as we don't want
                    // to
                    // overwrite the judge
                    return true;
                }

            }

            logger.debug(END_METHOD);
            // no startTime => sitting info not moved
            return false;
        }

        logger.debug(END_METHOD);
        // startTime set => sitting info has been moved
        return true;
    }

    /**
     * Calls the actual move data work methods
     * 
     * @param scheduledHearing
     *            the scheduled hearing
     * @param sitting
     *            the related sitting
     */
    private void moveData(ScheduledHearing scheduledHearing, Sitting sitting, String userDisplayName) {
        logger.debug("start method: moveData(scheduledHearing, sitting) with scheduledHearing = " + scheduledHearing
                + "\nand with sitting = " + sitting);

        moveJudgeData(scheduledHearing, sitting, userDisplayName);
        moveJusticeData(scheduledHearing, sitting, justice1Name, userDisplayName);
        moveJusticeData(scheduledHearing, sitting, justice2Name, userDisplayName);
        moveJusticeData(scheduledHearing, sitting, justice3Name, userDisplayName);
        moveJusticeData(scheduledHearing, sitting, justice4Name, userDisplayName);

        logger.debug("end method: moveData(scheduledHearing, sitting) with scheduledHearing = " + scheduledHearing
                + "\nand with sitting = " + sitting);
    }

    /**
     * Moves the judge data
     * 
     * @param sh
     *            the scheduled hearing
     * @param s
     *            the sitting
     */
    private void moveJudgeData(ScheduledHearing sh, Sitting s, String userDisplayName) {
        logger.debug("start method: moveJudgeData(sh, s) with sh = " + sh + AND_WITH + s);

        Integer refJudgeId = s.getRefJudgeId();

        // only create the record if there is a judge on the Sitting
        if (refJudgeId != null) {
            /** SchedHearingAttendee record for judge */
            SchedHearingAttendeeBasicValue judgeAttendee = new SchedHearingAttendeeBasicValue();
            judgeAttendee.setAttendeeType(ATTENDEE_TYPE_JUDGE);
            judgeAttendee.setRefJudgeID(refJudgeId);

            scheduledHearingAttendeeMaintainer
                    .create(judgeAttendee, sh, userDisplayName);
        }

        logger.debug("end method: moveJudgeData(sh, s) with sh = " + sh + AND_WITH + s);
    }

    /**
     * Moves a justice data This method may be called and no rows added. This is
     * because, not all 4 justice items have to exist in a sitting.
     * 
     * @param sh
     *            the scheduled hearing
     * @param s
     *            the sitting
     * @param name
     *            the name of the justice
     */
    private void moveJusticeData(ScheduledHearing sh, Sitting s, String name, String userDisplayName) {
        logger.debug("start method: moveJusticeData(sh, s, name) with sh = " + sh + AND_WITH + s
                + "\nand with name = " + name);

        // only move if there is a justice on the Sitting
        if (name != null) {
            /** SHJustice record */
            SHJusticeBasicValue justice = new SHJusticeBasicValue();
            justice.setHearingID(sh.getHearingId());
            justice.setJusticeName(name);
            ShJustice shJustice = (ShJustice) shJusticeMaintainer.create(justice, userDisplayName);

            /** SchedHearingAttendee record for justice */
            SchedHearingAttendeeBasicValue justiceAttendee = new SchedHearingAttendeeBasicValue();
            justiceAttendee.setAttendeeType(ATTENDEE_TYPE_JUSTICE_OF_THE_PEACE);
            justiceAttendee.setSheduledHearingID(sh.getScheduledHearingId());

            /** create attendee record */
            SchedHearingAttendee schedHearingAttendee = (SchedHearingAttendee) scheduledHearingAttendeeMaintainer
                    .create(justiceAttendee, sh, userDisplayName);

            /** justice links */
            schedHearingAttendee.setShJustice(shJustice);

        }

        logger.debug("end method: moveJusticeData(sh, s, name) with sh = " + sh + AND_WITH + s
                + "\nand with name = " + name);
    }

    /**
     * Uses the sitting class to determine the justice names via the ref justice
     * id or just using the name
     * 
     * @param sitting
     */
    private void setupJusticeNames(Sitting sitting) {
        logger.debug("start method: setupJusticeNames(sitting) with sitting = " + sitting);

        justice1Name = getJusticeName(sitting.getRefJustice1Id(), sitting.getJusticeName1());
        justice2Name = getJusticeName(sitting.getRefJustice2Id(), sitting.getJusticeName2());
        justice3Name = getJusticeName(sitting.getRefJustice3Id(), sitting.getJusticeName3());
        justice4Name = getJusticeName(sitting.getRefJustice4Id(), sitting.getJusticeName4());

        logger.debug("end method: setupJusticeNames(sitting) with sitting = " + sitting);
    }

    /**
     * Gets a justice name with the help of the RefJustice table
     * 
     * If a justice id is given, the lookup is made, otherwise a name may exist
     * on the sitting, which will be used instead
     * 
     * @param id
     * @param name
     * @return the resulting name to be used
     */
    private String getJusticeName(Integer id, String name) {
        logger.debug("start method: getJusticeName(id, name) with id = " + id + " and name = " + name);

        String result;

        if (id != null) {
            try {
                result = refJusticeMaintainer.findByPrimaryKey(id).getJusticeName();
            } catch (ObjectNotFoundException onfe) {
                errorHandler.handleError(onfe, SittingInfoMover.class);
                throw new EJBException(onfe);
            }
        } else {
            result = name;
        }

        if (result != null && result.trim().length() == 0) {
            result = null;
        }

        logger.debug("end method: getJusticeName(id, name) with id = " + id + " and name = " + name);

        return result;
    }

    private void setScheduledHearingStartTime(ScheduledHearing scheduledHearing, String userDisplayName) {
        ScheduledHearingBasicValue shBV = this.scheduledHearingMaintainer
                .getScheduledHearingBasicValue(scheduledHearing);
        try {
        	
        	Date notBefore = getDateWithDefaultTime(shBV.getNotBeforeTime());
        	
        	Date today = getDateWithDefaultTime(Calendar.getInstance().getTime());
        	
        	//CTX-4661 - only set the time if today is the date of the hearing - i.e. the not before date
        	if(notBefore.equals(today)){
	            shBV.setStartTime(Calendar.getInstance().getTime());
	            scheduledHearingMaintainer.update(shBV, userDisplayName);
        	}
        } catch (ObjectNotFoundException ex) {
            errorHandler.handleError(ex, SittingInfoMover.class);
            throw new EJBException(ex);
        }
    }
    
      /**
     * Get a date for the specified date with the time set to 00:00:00.000
     * 
     * @param date
     *            the date
     * @return the new calendar
     */
    public Date getDateWithDefaultTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

}