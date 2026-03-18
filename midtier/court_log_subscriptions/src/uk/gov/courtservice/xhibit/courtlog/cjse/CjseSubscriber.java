package uk.gov.courtservice.xhibit.courtlog.cjse;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage.LongAdjournDateMessageElement;
import uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * @author pznwc5
 * 
 * The class performs CJSE related business logic
 */
public class CjseSubscriber extends Subscriber {

    /** Logger */
    private static Logger LOG = Logger.getLogger(CjseSubscriber.class);

    /** CJSE Helper * */
    private CjseClHelper hlpr = new CjseClHelper();

    private static final Integer _longAdjournEventId = new Integer(30200);
    private static final Integer _specialMeasuresEventId = new Integer(20934);
    private static final String _DEFAULT_STRING = "xxx";
    
    
    /**
     * Do post create processing for CJSE
     */
    public void postCreate(OperationContext ctx) {
        CourtLogSubscriptionValue subVals[] = ctx.getNewSubscriptionValues();
        for (int i = 0; i < subVals.length; i++) {
            try {
                hlpr.transformClEvent(subVals[i]);
            } catch (Throwable e) {
                // We don't want this to abort the court log
                LOG.fatal(e.getMessage(), e);
            }
        }
    }

    
    
    /**
     * Do post update processing for CJSE Functionality added for CCN0227 to
     * create event message for Long Adjourn.
     */
    public void postUpdate(OperationContext ctx) {
        CourtLogSubscriptionValue subVals[] = ctx.getNewSubscriptionValues();

        /*
         * TODO Make strategic. Create reference table for all CJSE events that
         * need update functionality and take specific event functionality out
         * of the Subscriber code. As the updates are only for Long Adjourn this
         * code is ok for now.
         */
        for (CourtLogSubscriptionValue subVal : subVals) {
            try {
                Integer eventType = subVal.getCourtLogViewValue().getEventType();
                if (eventType.equals(_longAdjournEventId)) {
                    longAdjournmentUpdate(ctx, subVal);
                } else if (eventType.equals(_specialMeasuresEventId)) {
                    specialMeasuresUpdate(subVal);
                }
            } catch (Throwable e) {
                // We don't want this to abort the court log
                LOG.fatal(e.getMessage(), e);
            }
        }
    }
    
    
    private void specialMeasuresUpdate(CourtLogSubscriptionValue clsv) {
        LOG.debug("CJSE post update for event: " + clsv.getCourtLogViewValue().getEventType());
        hlpr.transformClEvent(clsv);
    }
    
    
    private void longAdjournmentUpdate(OperationContext ctx, CourtLogSubscriptionValue clsv) {
        LOG.debug("CJSE post update for event: " + clsv.getCourtLogViewValue().getEventType());
        
        String originalLogEntry = ctx.getOriginalBasicValue().getLogEntryXml();
        String newLogEntry = clsv.getCourtLogViewValue().getLogEntry();
        LongAdjournDateMessageElement element = new LongAdjournDateMessageElement();

        String oldDate = _DEFAULT_STRING;
        String newDate = _DEFAULT_STRING;
        try {
            oldDate = element.getElement(originalLogEntry);
        } catch (CSUnrecoverableException e) {
            // Do nothing, XPATH unavailable.
            LOG.warn(e.getMessage());
        }
        try {
            newDate = element.getElement(newLogEntry);
        } catch (CSUnrecoverableException e) {
            // Do nothing, XPATH unavailable.
            LOG.warn(e.getMessage());
        }

        
        // Find value of the old and LAO_type element within the XML
        // log structure.
        String xpathForType = "/event/E30200_Long_Adjourn_Options/E30200_LAO_Type/text()";
        String oldType = _DEFAULT_STRING;
        String newType = _DEFAULT_STRING;
        try {
            oldType = CSServices.getXMLServices()
                    .getXpathValueFromXmlString(originalLogEntry, xpathForType);
        } catch (CSUnrecoverableException e) {
            // Do nothing, XPATH unavailable.
            LOG.warn(e.getMessage());
        }
        try {
            newType = CSServices.getXMLServices().getXpathValueFromXmlString(newLogEntry, xpathForType);
        } catch (CSUnrecoverableException e) {
            // Do nothing, XPATH unavailable.
            LOG.warn(e.getMessage());
        }

        
        // Find value of the old and new PSR_required element within
        // the XML log structure.
        String xpathForPSR = "/event/E30200_Long_Adjourn_Options/E30200_LAO_PSR_Required/text()";
        String oldPSR = _DEFAULT_STRING;
        String newPSR = _DEFAULT_STRING;
        try {
            oldPSR = CSServices.getXMLServices().getXpathValueFromXmlString(originalLogEntry, xpathForPSR);
        } catch (CSUnrecoverableException e) {
            // Do nothing, XPATH unavailable.
            LOG.warn(e.getMessage());
        }
        try {
            newPSR = CSServices.getXMLServices().getXpathValueFromXmlString(newLogEntry, xpathForPSR);
        } catch (CSUnrecoverableException e) {
            // Do nothing, XPATH unavailable.
            LOG.warn(e.getMessage());
        }

        
        // TEST if the CJSE event has changed type, date or PSR
        // required.
        LOG.debug("CJSE Long Adjourn Update params: oldDate= " + oldDate + " newDate= " + newDate
                + " oldType= " + oldType + "newType= " + newType + " oldPSR= " + oldPSR + " newPSR= "
                + newPSR);
        if (!oldDate.equals(newDate) || !oldType.equals(newType) || !oldPSR.equals(newPSR)) {
            LOG.debug("CJSE post update, Long Adjourn date difference.");
            clsv.getCourtLogViewValue().getLogEntry();
            if (!oldDate.equals(newDate)) {
                clsv.getCourtLogViewValue().setDateAmended(true);
            }
            hlpr.transformClEvent(clsv);
        }
    }
}
