package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

// jdk
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: EventDateTimeMessageElement
 * </p>
 * <p>
 * Description: Retrives the court log event date message element
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: EventDateTimeMessageElement.java,v 1.1 2004/04/20 14:40:57
 *          pznwc5 Exp $
 */

public class EventDateTimeMessageElement implements MessageElement {
    private static final Logger log = CSServices.getLogger(DefendantNameMessageElement.class);

    public EventDateTimeMessageElement() {
    }

    /**
     * Retrives the date and time of the court log event
     * 
     * @param value
     *            Contains a <code>CourtLogViewValue</code> which contains the
     *            entryDate attribute holding the date and time of the event.
     * @param theCase
     *            not used
     * @return The date and time of the event
     */
    public String getElement(CourtLogSubscriptionValue value, XhbCase theCase) {
        log.debug("getElement() start: CourtLogSubscriptionValue " + value + "XhbCase " + theCase);

        // check parameters we're going to use
        if (value == null) {
            throw new IllegalArgumentException("A CourtLogSubscriptionValue instance "
                    + "must be passed to the getElement() method");
        }
        if (value.getCourtLogViewValue() == null) {
            throw new IllegalArgumentException("A CourtLogSubscriptionValue instance containing a  "
                    + "CourtLogViewValue must be " + "passed to the getElement() method");
        }
        if (value.getCourtLogViewValue().getEntryDate() == null) {
            throw new IllegalArgumentException(", containing a CourtLogViewValue "
                    + "object with a logEntry must be provided to the getElement() method for court log event type: "
                    + value.getCourtLogViewValue().getEventType());
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
        log.debug("Returning EventDateTimeMessageElement "
                + formatter.format(value.getCourtLogViewValue().getEntryDate()));
        return formatter.format(value.getCourtLogViewValue().getEntryDate());
    }
}