package uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel;

//jdk
import org.apache.log4j.Logger;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.EventParameters;

/**
 * Description: This classgenerates and populates the EventLevelPopulator.  This populate is used to send a message to CJSE & DARTS to identify a 
 *              defendant/appelant who has a nationality and deportation reason assigned to them. 
 * @author davieskl
 * @version 1.0
 */
public class DeportationCjseEventLevelPopulator implements CjseEventLevelPopulator{

    private static final Logger log = CSServices.getLogger(DeportationCjseEventLevelPopulator.class);
    
    /**
     * Description: This method populates the event parameters and generates theEventLevelandIdentifier.
     *       
     *@param EventParameters eventParameters
     *@param XhbCase theCase
     *@param CourtLogSubscriptionValue eventInfo
     *@return EventLevelAndIdentifier
     */
    public EventLevelAndIdentifier populate(EventParameters eventParameters, XhbCase theCase, CourtLogSubscriptionValue eventInfo) {
        
        //log start of method and check params
        logStartAndCheckParams(eventParameters, theCase, eventInfo);
        
        Integer defendantOnCaseId = eventInfo.getDefendantOnCaseId();
        XhbDefendantOnCase defendantOnCase = XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(defendantOnCaseId);
        
        DeportationPopulatorHelper deportationHelper = new DeportationPopulatorHelper();
        
        eventParameters.setDeportationReason(deportationHelper.getDeportationReason(defendantOnCase));
        eventParameters.setDefendantNationality(deportationHelper.getNationality(defendantOnCase));
        eventParameters.setIsUrgent(false);

        //populate the crn parameters via helper
        CrnPopulatorHelper2 crnHelper2 = new CrnPopulatorHelper2(defendantOnCase);
        eventParameters.setASNs(crnHelper2.getAsns());
        eventParameters.setCRNIDs(crnHelper2.getCrns());
        
        //find the event identifier, use CRN if available, otherwise null
        // there will be zero or one CRNs for a defendant on offence
        String eventIdentifier = null;
        if (eventParameters.getCRNIDs() != null && eventParameters.getCRNIDs().getCRNIDCount() == 1) {
            eventIdentifier = eventParameters.getCRNIDs().getCRNID(0);
        }
         
        EventLevelAndIdentifier levelAndIdentifier = new EventLevelAndIdentifier(CjseEventLevelPopulatorFactory
                .getInstance().getEventLevelForDescription(CjseEventLevelPopulatorFactory.CRN_EVENT_LEVEL),
                eventIdentifier);
        
        return levelAndIdentifier;
    }

    // ---------------------------- Private Methods
    // -----------------------------//
    private void logStartAndCheckParams(EventParameters eventParameters, XhbCase theCase,
            CourtLogSubscriptionValue eventInfo) throws IllegalArgumentException {
        log.debug("populate() start");

        // check parameters
        if (eventInfo == null) {
            throw new IllegalArgumentException("A CourtLogSubscriptionValue instance "
                    + "must be passed to the populate() method");
        }
        if (eventInfo.getCourtLogViewValue() == null) {
            throw new IllegalArgumentException("A CourtLogSubscriptionValue instance containing a  "
                    + "CourtLogViewValue must be " + "passed to the populate() method");
        }
        if (eventParameters == null) {
            throw new IllegalArgumentException("An EventParameters instance must be passed to the "
                    + "populate() method for court log event type: " + eventInfo.getCourtLogViewValue().getEventType());
        }
        if (theCase == null) {
            throw new IllegalArgumentException("An XhbCase instance must be passed to the "
                    + "populate() method for court log event type: " + eventInfo.getCourtLogViewValue().getEventType());
        }
    }
}
