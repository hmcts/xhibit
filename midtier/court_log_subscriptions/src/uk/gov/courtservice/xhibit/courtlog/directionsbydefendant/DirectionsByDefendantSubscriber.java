package uk.gov.courtservice.xhibit.courtlog.directionsbydefendant;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntry;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_defendant.XhbDirectionsForDefendantBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_defendant.XhbDirectionsForDefendantBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_defendant.XhbDirectionsForDefendantBeanNotFoundException;
import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.helpers.xml.CourtLogXmlHelper;
import uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow;
import uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: DirectionsByDefendantSubscriber
 * </p>
 * <p>
 * Description: Performs subscriber processing for directions by defendant
 * events.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Stephen Tully
 * @version $Revision: 1.6 $
 */
public class DirectionsByDefendantSubscriber extends Subscriber {
    private static final Logger log = CSServices.getLogger(DirectionsByDefendantSubscriber.class);

    // court log events for Directions For Defendant
    private static final int IDENTIFIED = 40704;

    private static final int ARRAIGNED = 40705;

    private static final int BAIL_AND_CUSTODY = 40706;

    private static final int CERT_OF_ATTEND = 40707;

    private static final int FORM_B = 40708;

    private ResourceBundle directiondDBKeys;

    /**
     * Default constructor
     */
    public DirectionsByDefendantSubscriber() {
        directiondDBKeys = CSServices.getConfigServices().getBundle("XHIBITDirectionsResources");
    }

    /**
     * Do post delete processing for directions for defendant court log events.
     * 
     * @param cntxt
     */
    public void postDelete(OperationContext cntxt) {
        String methodName = "postDelete:";

        CourtLogCRUDValue crudValue = cntxt.getCrudValue();

        log.debug(methodName + " entered with CourtLogCRUDValue " + crudValue);

        // check if this is the latest version of the event
        if (CourtLogWorkFlow.hasMoreEvents(crudValue.getCaseId(), crudValue.getEventType(), crudValue.getEntryDate())) {
            log.debug("This is not the latest event of type " + crudValue.getEventType() + " on case "
                    + crudValue.getCaseId() + " so not updating the db tables.");
        } else {
            // check for an earlier event
            XhbCourtLogEntryBasicValue cle = null;
            Vector cleVector = new Vector(XhbCourtLogEntryBeanHelper2.findByCaseIdEventTypeDate(crudValue.getCaseId(),
                    crudValue.getEventType(), new Timestamp(0)));

            if (cleVector != null && cleVector.size() > 0) {
                cle = ((XhbCourtLogEntry) cleVector.lastElement()).getData();
            }

            if (cle == null) {
                // no earlier event so remove details from the db
                removeUpdateEventDetails(crudValue, true);
            } else {
                // earlier event found so replace the xml with the earlier
                // xml and update the db from this
                Map properties = CourtLogXmlHelper.getPropertySet(cle.getLogEntryXml());
                crudValue.setPropertyMap(properties);
                removeUpdateEventDetails(crudValue, false);
            }
        }

        log.debug(methodName + " exited");
    }

    /**
     * Process the directions for defendant court log event depending on the
     * event type code.
     * 
     * @param crudValue -
     *            the court log event to be processed
     * @param isRemove -
     *            true indicates that the value is to be initialised
     */
    private void removeUpdateEventDetails(CourtLogCRUDValue crudValue, boolean isRemove) {
        try {
            switch (crudValue.getEventType().intValue()) {
            case IDENTIFIED:
                removeUpdateIdentified(crudValue, isRemove);
                break;
            case ARRAIGNED:
                removeUpdateArraigned(crudValue, isRemove);
                break;
            case BAIL_AND_CUSTODY:
                removeUpdateBailAndCustody(crudValue, isRemove);
                break;
            case CERT_OF_ATTEND:
                removeUpdateCertOfAttend(crudValue, isRemove);
                break;
            case FORM_B:
                removeUpdateFormB(crudValue, isRemove);
                break;
            }
        } catch (XhbDirectionsForDefendantBeanNotFoundException e) {
            // It is entirely possible that the Directions For Defendant
            // record
            // doesn't exist - for example, if the court log event was
            // created
            // via the Preliminary Hearing screen as this does not cause a
            // directions
            // record to be created.
            // If this is the case, then swallow the error and continue.
            log.debug("No DirectionsForDefendant record found... continuing...");
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
        }
    }

    /**
     * Remove or update defendant identified events
     * 
     * @param crudValue
     * @param isRemove
     */
    private void removeUpdateIdentified(CourtLogCRUDValue crudValue, boolean isRemove) {
        XhbDirectionsForDefendantBasicValue dirForDefBV = DirectionsByDefendantHelper.getDirForDefBV(crudValue
                .getDefendantOnCaseId());

        if (isRemove) {
            dirForDefBV.setIsIdentified(null);
        } else {
            // get the new value from the court log event
            String logEntry = CourtLogXmlHelper.getXML(crudValue);

            // find the value of the identified xpath
            String value = DirectionsByDefendantHelper.getValueFromXml("identifiedXpath", logEntry);

            if (value.equals(DirectionsByDefendantHelper.getPropertyFromXml("isIdentified"))) {
                dirForDefBV.setIsIdentified(directiondDBKeys.getString("E40704_Defendant_Identified_DB"));
            } else {
                dirForDefBV.setIsIdentified(directiondDBKeys.getString("E40704_No_Reply_DB"));
            }
        }

        XhbDirectionsForDefendantBeanHelper2.update(dirForDefBV);
    }

    /**
     * Remove or update defendant arraigned events
     * 
     * @param crudValue
     * @param isRemove
     */
    private void removeUpdateArraigned(CourtLogCRUDValue crudValue, boolean isRemove) {
        XhbDirectionsForDefendantBasicValue dirForDefBV = DirectionsByDefendantHelper.getDirForDefBV(crudValue
                .getDefendantOnCaseId());

        if (isRemove) {
            dirForDefBV.setArraigned(null);
        } else {
            // get the new value from the court log event
            String logEntry = CourtLogXmlHelper.getXML(crudValue);

            // find the value of the arraigned xpath
            String value = DirectionsByDefendantHelper.getValueFromXml("arraignedXpath", logEntry);

            if (value.equalsIgnoreCase("true")) {
                dirForDefBV.setArraigned(directiondDBKeys.getString("Arraigned_DB"));
            } else {
                dirForDefBV.setArraigned(directiondDBKeys.getString("NotArraigned_DB"));
            }
        }

        XhbDirectionsForDefendantBeanHelper2.update(dirForDefBV);
    }

    /**
     * Remove or update defendant bail and custody events
     * 
     * @param crudValue
     * @param isRemove
     */
    private void removeUpdateBailAndCustody(CourtLogCRUDValue crudValue, boolean isRemove) {
        XhbDirectionsForDefendantBasicValue dirForDefBV = DirectionsByDefendantHelper.getDirForDefBV(crudValue
                .getDefendantOnCaseId());

        if (isRemove) {
            dirForDefBV.setBailStatus(null);
            dirForDefBV.setNewBailConditions(null);
        } else {
            // get the new value from the court log event
            String logEntry = CourtLogXmlHelper.getXML(crudValue);

            // find the value of the bail status xpath
            String value = DirectionsByDefendantHelper.getValueFromXml("bailStatusXpath", logEntry);

            if (value.equals(DirectionsByDefendantHelper.getPropertyFromXml("ric"))) {
                dirForDefBV.setBailStatus(directiondDBKeys.getString("E40706_RIC_DB"));
                dirForDefBV.setNewBailConditions(null);
            } else if (value.equals(DirectionsByDefendantHelper.getPropertyFromXml("bailAsBefore"))) {
                dirForDefBV.setBailStatus(directiondDBKeys.getString("E40706_Bail_As_Before_DB"));
                dirForDefBV.setNewBailConditions(null);
            } else { // must be varied
                dirForDefBV.setBailStatus(directiondDBKeys.getString("E40706_Bail_Varied_DB"));

                // get the new bail conditions
                String valueConditions = DirectionsByDefendantHelper.getValueFromXml("bailConditionsXpath", logEntry);
                dirForDefBV.setNewBailConditions(valueConditions);
            }
        }

        XhbDirectionsForDefendantBeanHelper2.update(dirForDefBV);
    }

    /**
     * Remove or update defendant certificate of attendance events
     * 
     * @param crudValue
     * @param isRemove
     */
    private void removeUpdateCertOfAttend(CourtLogCRUDValue crudValue, boolean isRemove) {
        XhbDirectionsForDefendantBasicValue dirForDefBV = DirectionsByDefendantHelper.getDirForDefBV(crudValue
                .getDefendantOnCaseId());

        if (isRemove) {
            dirForDefBV.setCertAttendance(null);
        } else {
            // get the new value from the court log event
            String logEntry = CourtLogXmlHelper.getXML(crudValue);

            // find the value of the bail status xpath
            String value = DirectionsByDefendantHelper.getValueFromXml("certOfAttendXpath", logEntry);

            if (value.equals(DirectionsByDefendantHelper.getPropertyFromXml("granted"))) {
                dirForDefBV.setCertAttendance(directiondDBKeys.getString("E40707_Granted_DB"));
            } else if (value.equals(DirectionsByDefendantHelper.getPropertyFromXml("refused"))) {
                dirForDefBV.setCertAttendance(directiondDBKeys.getString("E40707_Refused_DB"));
            } else { // must be not requested
                dirForDefBV.setCertAttendance(directiondDBKeys.getString("E40707_Not_Requested_DB"));
            }
        }

        XhbDirectionsForDefendantBeanHelper2.update(dirForDefBV);
    }

    /**
     * Remove or update defendant form B events
     * 
     * @param crudValue
     * @param isRemove
     */
    private void removeUpdateFormB(CourtLogCRUDValue crudValue, boolean isRemove) {
        XhbDirectionsForDefendantBasicValue dirForDefBV = DirectionsByDefendantHelper.getDirForDefBV(crudValue
                .getDefendantOnCaseId());

        if (isRemove) {
            dirForDefBV.setFiledFormB(null);
            dirForDefBV.setToBeFiledBy(null);
        } else {
            // get the new value from the court log event
            String logEntry = CourtLogXmlHelper.getXML(crudValue);

            // find the value of the bail status xpath
            String value = DirectionsByDefendantHelper.getValueFromXml("formBXpath", logEntry);

            if (value.equals(DirectionsByDefendantHelper.getPropertyFromXml("filed"))) {
                dirForDefBV.setFiledFormB(directiondDBKeys.getString("E40708_Filed_DB"));
                dirForDefBV.setToBeFiledBy(null);
            } else { // must be to be filed
                dirForDefBV.setFiledFormB(directiondDBKeys.getString("E40708_To_Be_Filed_DB"));

                // get the 'To be filed by' date
                String valueDate = DirectionsByDefendantHelper.getValueFromXml("formBDateXpath", logEntry);

                try {
                    dirForDefBV.setToBeFiledBy(XDateFormat.parseAsDate(valueDate));
                } catch (ParseException ex) {
                    CSServices.getDefaultErrorHandler().handleError(ex, getClass());
                    // this is an unexpected error as this String has
                    // previously been
                    // entered into the db as a date, should parse fine now
                    // too
                    throw new EJBException("Could not parse String " + valueDate + " as a date.", ex);
                }
            }
        }

        XhbDirectionsForDefendantBeanHelper2.update(dirForDefBV);
    }
}
