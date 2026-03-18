package uk.gov.courtservice.xhibit.courtlog.directionsbycase;

import java.util.Map;
import java.util.ResourceBundle;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBeanHelper2;
import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.helpers.xml.CourtLogXmlHelper;
import uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow;
import uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: DirectionsByCaseSubscriber
 * </p>
 * <p>
 * Description: This is a category subscriber used for handling direction for
 * case events
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bal Bhamra
 * @version $Id: DirectionsByCaseSubscriber.java,v 1.4 2005/03/03 10:58:53
 *          sz0t7n Exp $
 */
public class DirectionsByCaseSubscriber extends Subscriber {
    private static final int P_AND_D_FORM = 40710;

    private static final int TRIAL_TIME_EST = 40711;

    private static final int DIRECTIONS = 40712;

    private final ResourceBundle _directiondDBKeys;

    public DirectionsByCaseSubscriber() {
        _directiondDBKeys = CSServices.getConfigServices().getBundle("XHIBITDirectionsResources");
    }

    /**
     * Post-creation logic for Directions By Case events
     * 
     * @see uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber
     *      #postCreate(uk.gov.courtservice.xhibit.courtlog.OperationContext)
     */
    public void postDelete(OperationContext context) {
        log.debug("Start - post create for direction by case subscriber");
        CourtLogCRUDValue crudValue = context.getCrudValue();

        log.debug("postDelete: entered with CourtLogCRUDValue " + crudValue);

        // check if this is the latest version of the event

        if (!CourtLogWorkFlow.hasMoreEvents(crudValue.getCaseId(), crudValue.getEventType(), crudValue.getEntryDate())) {
            log.debug("This is the latest event of this type");
            // check for an earlier event
            XhbCourtLogEntryBasicValue cle = CourtLogWorkFlow.getLastEventOfType(crudValue.getCaseId(), crudValue
                    .getEventType());

            if (cle == null) {
                log.debug("No earlier event exists so remove from db");
                // no earlier event so remove details from the db
                removeUpdateEventDetails(crudValue, true);
            } else {
                log.debug("An earlier event has been found so update the db with this event");
                // earlier event found so replace the xml with the earlier
                // xml and update the db from this
                Map properties = CourtLogXmlHelper.getPropertySet(cle.getLogEntryXml());
                crudValue.setPropertyMap(properties);
                removeUpdateEventDetails(crudValue, false);
            }
        } else {
            log.debug("This is not the latest event of type " + crudValue.getEventType() + " on case "
                    + crudValue.getCaseId() + " so not updating the db tables.");
        }

        log.debug("postDelete: exited");
        log.debug("End - post create for directions by cas subscriber");
    }

    private void removeUpdateEventDetails(CourtLogCRUDValue crudValue, boolean isRemove) {
        switch (crudValue.getEventType().intValue()) {
        case P_AND_D_FORM:
            removeUpdatePandD(crudValue, isRemove);
            break;
        case TRIAL_TIME_EST:
            removeUpdateTrialTimeEst(crudValue, isRemove);
            break;
        case DIRECTIONS:
            removeUpdateDirections(crudValue, isRemove);
            break;
        }
    }

    private void removeUpdatePandD(CourtLogCRUDValue clvv, boolean isRemove) {
        XhbDirectionsForCaseBasicValue dirForCaseBV = DirectionsByCaseHelper.getDirForCaseBV(clvv.getCaseId());
        if (isRemove) {
            dirForCaseBV.setHasPanddForm(null);
        } else {
            // get the new value from the court log event
            String logEntry = CourtLogXmlHelper.getXML(clvv);

            // find the value of the p & d xpath
            String value = DirectionsByCaseHelper.getValueFromXml("panddXpath", logEntry);

            if (value.equals(DirectionsByCaseHelper.getPropertyFromXml("hasPandD"))) {
                dirForCaseBV.setHasPanddForm(_directiondDBKeys.getString("E40710_Form_Handed_In_DB"));
            } else {
                dirForCaseBV.setHasPanddForm(_directiondDBKeys.getString("E40710_Form_Not_Handed_In_DB"));
            }
        }

        XhbDirectionsForCaseBeanHelper2.update(dirForCaseBV);
    }

    private void removeUpdateTrialTimeEst(CourtLogCRUDValue clvv, boolean isRemove) {
        XhbDirectionsForCaseBasicValue dirForCaseBV = DirectionsByCaseHelper.getDirForCaseBV(clvv.getCaseId());

        if (isRemove) {
            dirForCaseBV.setTrialTimeEstimate(null);
            dirForCaseBV.setTrialTimeUnit(null);
        } else {
            // get the new value from the court log event
            String logEntry = CourtLogXmlHelper.getXML(clvv);

            // find the value of the trial time estimate xpath
            String value = DirectionsByCaseHelper.getValueFromXml("timeEstXpath", logEntry);

            dirForCaseBV.setTrialTimeEstimate(new Float(value));

            // find the value of the trial time estimate units xpath
            String valueUnits = DirectionsByCaseHelper.getValueFromXml("timeEstUnitsXpath", logEntry);

            if (valueUnits.equals(DirectionsByCaseHelper.getPropertyFromXml("trialTimeDays"))) {
                dirForCaseBV.setTrialTimeUnit(new Integer(_directiondDBKeys.getString("E40711_Days_DB")));
            } else if (valueUnits.equals(DirectionsByCaseHelper.getPropertyFromXml("trialTimeWeeks"))) {
                dirForCaseBV.setTrialTimeUnit(new Integer(_directiondDBKeys.getString("E40711_Weeks_DB")));
            } else // must be months
            {
                dirForCaseBV.setTrialTimeUnit(new Integer(_directiondDBKeys.getString("E40711_Months_DB")));
            }
        }

        XhbDirectionsForCaseBeanHelper2.update(dirForCaseBV);
    }

    private void removeUpdateDirections(CourtLogCRUDValue clvv, boolean isRemove) {
        log.debug("removeUpdateDirections: Start ");
        XhbDirectionsForCaseBasicValue dirForCaseBV = DirectionsByCaseHelper.getDirForCaseBV(clvv.getCaseId());
        log.debug("removeUpdateDirections: isRemove -  " + isRemove);
        if (isRemove) {
            log.debug("removeUpdateDirections: setDirectionsText(null)");
            dirForCaseBV.setDirectionsText(null);
        } else {
            log.debug("removeUpdateDirections: Setting new value to DirectionsText");
            // get the new value from the court log event
            String logEntry = CourtLogXmlHelper.getXML(clvv);

            // find the value of the directions xpath
            String value = DirectionsByCaseHelper.getValueFromXml("directionsXpath", logEntry);

            // set the directions text with the new value
            log.debug("removeUpdateDirections: value = " + value);
            dirForCaseBV.setDirectionsText(value);
        }

        log.debug("dirForCaseBV.getDirectionsText(): " + dirForCaseBV.getDirectionsText());
        log.debug("removeUpdateDirections: XhbDirectionsForCaseBeanHelper.update");
        XhbDirectionsForCaseBeanHelper2.update(dirForCaseBV);
    }
}
