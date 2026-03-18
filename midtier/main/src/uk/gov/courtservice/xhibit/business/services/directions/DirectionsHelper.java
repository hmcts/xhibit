package uk.gov.courtservice.xhibit.business.services.directions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_defendant.XhbDirectionsForDefendant;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_defendant.XhbDirectionsForDefendantBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_defendant.XhbDirectionsForDefendantBeanHelper2;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForDefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsValue;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.helpers.event.EventLevelHelper;
import uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: DirectionsHelper
 * </p>
 * <p>
 * Description: This helper class creates, updates and deletes single direction
 * entities
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Ian Hannaford
 * @version $Id: DirectionsHelper.java,v 1.4 2006/06/05 12:29:34 bzjrnl Exp $
 */
public class DirectionsHelper {
    private static final Logger log = CSServices.getLogger(DirectionsHelper.class);

    // court log events for Directions For Case
    public static final Integer P_AND_D_FORM = new Integer(40710);

    public static final Integer TRIAL_TIME_EST = new Integer(40711);

    public static final Integer DIRECTIONS = new Integer(40712);

    // court log events for Directions For Defendant
    public static final Integer IDENTIFIED = new Integer(40704);

    public static final Integer ARRAIGNED = new Integer(40705);

    public static final Integer BAIL_AND_CUSTODY = new Integer(40706);

    public static final Integer CERT_OF_ATTEND = new Integer(40707);

    public static final Integer FORM_B = new Integer(40708);

    private DirectionsHelper() {
        // no implementation required...
    }

    public static DirectionsValue findDirectionsByCaseId(Integer caseId) throws CourtLogBusinessException {    	
    	DirectionsValue result = new DirectionsValue();    	    	    	    	
    	DirectionsForCaseValue directionsForCaseValue = findDirectionsForCaseByCaseId(caseId);
    	if (directionsForCaseValue != null) {
    		result.setDirectionsForCaseValue(directionsForCaseValue);	
    	}    	
    	return result;
    }
    
    public static DirectionsForCaseValue findDirectionsForCaseByCaseId(Integer caseId) throws CourtLogBusinessException {    	
    	DirectionsForCaseValue result = new DirectionsForCaseValue();   	    	    
    	XhbDirectionsForCaseBasicValue directionsForCaseBasicValue = XhbDirectionsForCaseBeanHelper2.findByCaseIDValue(caseId);
    	if (directionsForCaseBasicValue != null) {
    		result.setDirectionsForCaseBasicValue(directionsForCaseBasicValue);	
    	}    	
    	return result;
    }

    
    
    /**
     * Call through to maintainer to create or update direction entities. As
     * there is no requirement to display previously entered directions for
     * defendant/case then we just overwrite the exsiting one, if there is one,
     * or create a new one. As this is the case the client will not be passing
     * an ID of the record to update as they do not call a get method prior to
     * updating the directions.
     * 
     * @param directionsValue
     *            a DirectionsValue object.
     * @throws CourtLogBusinessException
     */
    public static void saveDirections(DirectionsValue directionsValue) throws CourtLogBusinessException {
        log.debug("saveDirections(..) : entered");

        final DirectionsForCaseValue directionsForCaseValue = directionsValue.getDirectionsForCaseValue();

        Integer caseId;
        final ArrayList courtLogEvents = new ArrayList();

        // get the case id
        if (directionsForCaseValue != null) {
            caseId = directionsForCaseValue.getDirectionsForCaseBasicValue().getCaseId();

            // add / update DirectionsForCase
            if (log.isDebugEnabled()) {
                log.debug("Processing directions for case basic value: "
                        + directionsForCaseValue.getDirectionsForCaseBasicValue());
            }

            processDirectionsForCase(directionsForCaseValue);

            // get the directions for case court log events to process later
            // (X54905)
            courtLogEvents.addAll(Arrays.asList(directionsForCaseValue.getCourtLogCRUDValues()));
        } else {
            // get the case Id from the Directions For Defendant
            Collection dirsForDefs = directionsValue.getDirectionsForDefendantValue();
            if (dirsForDefs.size() > 0) {
                caseId = findCaseIdFromDirForDef((DirectionsForDefendantValue) dirsForDefs.iterator().next());
            } else {
                log.warn("Neither a DirectionsForCase nor DirectionForDefendant "
                        + "objcets were passed to the saveDirections() method. " + "Nothing to do, returning...");
                return;
            }
        }

        // add / update any DirectionsForDefendant
        courtLogEvents.addAll(processDirectionsForDefendant(directionsValue.getDirectionsForDefendantValue(), caseId));

        // create / update the court log events
        new DirectionsEventHelper().orderAndCreateUpdateCourtLogEvents(courtLogEvents);

        log.debug("saveDirections() finished.");
    }

    private static Collection processDirectionsForDefendant(Collection dirsForDefs, Integer caseId) {
        log.debug("*** Size of DirectionsForDefendant Collection = " + dirsForDefs.size() + " ***");

        final Iterator dirsForDefIter = dirsForDefs.iterator();
        final ArrayList courtLogEvents = new ArrayList();

        // iterate through the direction for defendant.
        while (dirsForDefIter.hasNext()) {
            DirectionsForDefendantValue dirForDefValue = (DirectionsForDefendantValue) dirsForDefIter.next();
            XhbDirectionsForDefendantBasicValue dirForDefBasicValue = dirForDefValue
                    .getDirectionsForDefendantBasicValue();

            Collection directionForDefCol = XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(
                    dirForDefBasicValue.getDefendantOnCaseId()).getXhbDirectionsForDefendants();
            if (directionForDefCol.isEmpty()) {
                log.debug("*** creating a new directions for defendant row with VO [" + dirForDefBasicValue + "] ***");
                XhbDirectionsForDefendantBeanHelper2.create(dirForDefBasicValue);
            } else {
                XhbDirectionsForDefendant directionsForDefendant = (XhbDirectionsForDefendant) directionForDefCol
                        .iterator().next();

                // a record already exsits for this defendant, so update it
                updateExistingDirectionsForDefendant(dirForDefBasicValue, directionsForDefendant, caseId);
            }

            addDefendantOnCaseIdsToCourtLogEvents(dirForDefValue, dirForDefBasicValue);
            // get the directions for defendant court log events to process
            // later (X54905)
            courtLogEvents.addAll(Arrays.asList(dirForDefValue.getCourtLogCRUDValues()));
        }

        return courtLogEvents;
    }

    private static void addDefendantOnCaseIdsToCourtLogEvents(DirectionsForDefendantValue dirForDefValue,
            XhbDirectionsForDefendantBasicValue dirForDefBasicValue) {
        CourtLogCRUDValue[] dirForDefCRUDValues = dirForDefValue.getCourtLogCRUDValues();

        for (int i = 0; i < dirForDefCRUDValues.length; i++) {
            // this is a DEFENDANT level event so defendantOnCaseId required
            // for the CJSE event
            EventLevelHelper.addCjseCourtLogParameters(dirForDefCRUDValues[i], dirForDefBasicValue
                    .getDefendantOnCaseId(), null);
        }
    }

    private static void updateExistingDirectionsForDefendant(XhbDirectionsForDefendantBasicValue dirForDefBasicValue,
            XhbDirectionsForDefendant directionsForDefendant, Integer caseId) {
        // get the VO from the local interface
        XhbDirectionsForDefendantBasicValue returnedDirForDefBVO = directionsForDefendant.getData();

        // update the returned BVO with the new values
        // Check to ensure that the value being passed in is not null before
        // updating the returnedDirectionsForCaseBVO so as not to overwrite
        // existing values with null.

        // ST: Now that editing of Directions has been introduced added check to
        // make sure that the Directions we are updating are the latest in the
        // database. Covers the unlikely event that the user sets the date back
        // on the screen to record earlier Directions. In this case we will
        // record the court log event but don't want to overwirte more recent
        // Directions in the database
        if (dirForDefBasicValue.getIsIdentified() != null
                && isLatestEvent(IDENTIFIED, caseId, dirForDefBasicValue.getDateTime())) {
            returnedDirForDefBVO.setIsIdentified(dirForDefBasicValue.getIsIdentified());
        }

        if (dirForDefBasicValue.getArraigned() != null
                && isLatestEvent(ARRAIGNED, caseId, dirForDefBasicValue.getDateTime())) {
            returnedDirForDefBVO.setArraigned(dirForDefBasicValue.getArraigned());
        }

        if (dirForDefBasicValue.getBailStatus() != null
                && isLatestEvent(BAIL_AND_CUSTODY, caseId, dirForDefBasicValue.getDateTime())) {
            returnedDirForDefBVO.setBailStatus(dirForDefBasicValue.getBailStatus());
            returnedDirForDefBVO.setNewBailConditions(dirForDefBasicValue.getNewBailConditions());
        }

        if (dirForDefBasicValue.getCertAttendance() != null
                && isLatestEvent(CERT_OF_ATTEND, caseId, dirForDefBasicValue.getDateTime())) {
            returnedDirForDefBVO.setCertAttendance(dirForDefBasicValue.getCertAttendance());
        }

        if (dirForDefBasicValue.getFiledFormB() != null
                && isLatestEvent(FORM_B, caseId, dirForDefBasicValue.getDateTime())) {
            returnedDirForDefBVO.setFiledFormB(dirForDefBasicValue.getFiledFormB());
            returnedDirForDefBVO.setToBeFiledBy(dirForDefBasicValue.getToBeFiledBy());
        }

        // check this date is after the existing date before updating freetext
        if (dirForDefBasicValue.getDateTime() != null
                && dirForDefBasicValue.getDateTime().after(returnedDirForDefBVO.getDateTime())) {
            returnedDirForDefBVO.setDateTime(dirForDefBasicValue.getDateTime());
            returnedDirForDefBVO.setFreetext(dirForDefBasicValue.getFreetext());
        }

        log.debug("*** updating a directions for defendant ***");
        XhbDirectionsForDefendantBeanHelper2.update(returnedDirForDefBVO);
    }

    private static void processDirectionsForCase(DirectionsForCaseValue dirsForCaseValue) {
        Integer caseId = dirsForCaseValue.getDirectionsForCaseBasicValue().getCaseId();

        // See if a record already exists for this case
        log.debug("*** Seeing if a record exists - findByCaseId using caseId = " + caseId + " ***");
        try {
            XhbDirectionsForCase dirsForCaseLocal = XhbDirectionsForCaseBeanHelper2.findByCaseID(caseId);
            log.debug("Found existing directions for case, processing new value: "
                    + dirsForCaseValue.getDirectionsForCaseBasicValue());
            updateExistingDirectionsForCase(dirsForCaseValue, dirsForCaseLocal, caseId);
        } catch (XhbDirectionsForCaseBeanNotFoundException e) {
            // no existing record found so create new
            createNewDirectionsForCase(dirsForCaseValue);
        }
    }

    private static void createNewDirectionsForCase(DirectionsForCaseValue dirsForCaseValue) {
        // get the basic value object from the client VO.
        XhbDirectionsForCaseBasicValue dirsForCaseBasicValue = dirsForCaseValue.getDirectionsForCaseBasicValue();
        // create
        log.debug("Creating a new DirectionForCase Entity");
        XhbDirectionsForCaseBeanHelper2.create(dirsForCaseBasicValue);
    }

    private static void updateExistingDirectionsForCase(DirectionsForCaseValue dirsForCaseValue,
            XhbDirectionsForCase dirsForCaseLocal, Integer caseId) {
        log.debug("updateExistingDirectionsForCase() with new value: "
                + dirsForCaseValue.getDirectionsForCaseBasicValue());

        // get the basic value object from the client VO.
        XhbDirectionsForCaseBasicValue dirsForCaseBasicValue = dirsForCaseValue.getDirectionsForCaseBasicValue();

        // get the VO from the local interface
        XhbDirectionsForCaseBasicValue returnedDirectionsForCaseBVO = dirsForCaseLocal.getData();

        // update the value, with the new ones
        // RL: For Bug X53287 - Added checks to ensure that the value being
        // passed in is not null before updating the
        // returnedDirectionsForCaseBVO
        // so as not to overwrite existing values with null.

        // ST: Now that editing of Directions has been introduced added check to
        // make sure that the Directions we are updating are the latest in the
        // database. Covers the unlikely event that the user sets the date back
        // on the screen to record earlier Directions. In this case we will
        // record the court log event but don't want to overwirte more recent
        // Directions in the database
        if (dirsForCaseBasicValue.getHasPanddForm() != null
                && isLatestEvent(P_AND_D_FORM, caseId, dirsForCaseBasicValue.getDateTime())) {
            returnedDirectionsForCaseBVO.setHasPanddForm(dirsForCaseBasicValue.getHasPanddForm());
        }

        if (dirsForCaseBasicValue.getTrialTimeEstimate() != null
                && isLatestEvent(TRIAL_TIME_EST, caseId, dirsForCaseBasicValue.getDateTime())) {
            returnedDirectionsForCaseBVO.setTrialTimeEstimate(dirsForCaseBasicValue.getTrialTimeEstimate());
            returnedDirectionsForCaseBVO.setTrialTimeUnit(dirsForCaseBasicValue.getTrialTimeUnit());
        }

        if (dirsForCaseBasicValue.getDirectionsText() != null
                && isLatestEvent(DIRECTIONS, caseId, dirsForCaseBasicValue.getDateTime())) {
            returnedDirectionsForCaseBVO.setDirectionsText(dirsForCaseBasicValue.getDirectionsText());
        }

        // check this date is after the existing date before updating freetext
        if (dirsForCaseBasicValue.getDateTime() != null
                && (returnedDirectionsForCaseBVO.getDateTime() == null || dirsForCaseBasicValue.getDateTime().after(returnedDirectionsForCaseBVO.getDateTime()))) {
            returnedDirectionsForCaseBVO.setDateTime(dirsForCaseBasicValue.getDateTime());
            returnedDirectionsForCaseBVO.setFreetext(dirsForCaseBasicValue.getFreetext());
        }

        // update with the new values
        XhbDirectionsForCaseBeanHelper2.update(returnedDirectionsForCaseBVO);
    }

    /**
     * Determines if this is the latest event of this type in the db
     * 
     * @param eventType
     *            The event type to check
     * @param caseId
     * @param eventDate
     *            The date of the event we have
     * @return true if the event we have is the latest, false if there is a more
     *         recent event in the db
     */
    private static boolean isLatestEvent(Integer eventType, Integer caseId, Date eventDate) {
        log.debug("isLatestEvent(Integer eventType,Calendar eventDate) start " + " with eventType: " + eventType
                + ", eventDate : " + eventDate);

        return !CourtLogWorkFlow.hasMoreEvents(caseId, eventType, eventDate);
    }

    private static boolean isLatestEvent(Integer eventType, Integer caseId, Calendar eventDate) {
        return isLatestEvent(eventType, caseId, eventDate.getTime());
    }

    private static Integer findCaseIdFromDirForDef(DirectionsForDefendantValue dirForDefValue) {
        Integer defOnCaseId = dirForDefValue.getDirectionsForDefendantBasicValue().getDefendantOnCaseId();

        return XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(defOnCaseId).getCaseId();
    }
}
