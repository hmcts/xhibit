package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

// j2ee
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoom;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBeanHelper2;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: CourtIdentificationMessageElement
 * </p>
 * <p>
 * Description: This class retrieves the court identification string comprised
 * of <courtlocation>-<court-room> where <courtlocation> is the
 * xhb_court.short_name if present, xhb_court.court_name ortherwise.
 * <court-room> is xhb_court.court_room_name. Note this is a CRN level message
 * element, a defendantOnOffenceId must be provided.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: CourtIdentificationMessageElement.java,v 1.3 2005/03/02
 *          16:22:13 sz0t7n Exp $
 */
public class CourtIdentificationMessageElement implements MessageElement {
    private static final Logger log = CSServices.getLogger(CourtIdentificationMessageElement.class);

    private static final String UNIDENTIFIED_COURT_ROOM = "U";

    public CourtIdentificationMessageElement() {
    }

    /**
     * Returns the court identification string for the court and court room
     * identified in the <code>CourtLogSubscriptionValue</code>
     * 
     * @param value
     *            Used to determine which court room we build the String for
     * @param theCase
     *            Used to determine which court we build the String for
     * @return The court identification string
     */
    public String getElement(CourtLogSubscriptionValue value, XhbCase theCase) {
        log.debug("getElement() start: CourtLogSubscriptionValue " + value + "XhbCase " + theCase);

        boolean inCourt = true;

        // check parameters we're going to use
        if (theCase == null) {
            throw new IllegalArgumentException("An XhbCase reference must be provided to the getElement() method.");
        }
        if (value == null) {
            throw new IllegalArgumentException("A CourtLogSubscriptionValue instance "
                    + "must be passed to the getElement() method");
        }
        if (value.getCourtLogViewValue() == null) {
            throw new IllegalArgumentException("A CourtLogSubscriptionValue instance containing a  "
                    + "CourtLogViewValue must be " + "passed to the getElement() method");
        }
        if (value.getCourtRoomId() == null) {
            // no court room, this event must have been created out of court
            inCourt = false;
        }

        boolean isMultiSiteCourt = theCase.getXhbCourt().getXhbCourtSites().size() > 1;
        StringBuffer courtIdentification = new StringBuffer();

        // retrive the <courtlocation> part: short name if present, court name
        // otherwise
        if (theCase.getXhbCourt().getShortName() != null) {
            courtIdentification.append(theCase.getXhbCourt().getShortName());
        } else {
            // the event was recorded out of court so set to unidentified
            courtIdentification.append(theCase.getXhbCourt().getCourtName());
        }

        // find the court and add the <court-room> part
        if (inCourt) {
            XhbCourtRoom courtRoom = XhbCourtRoomBeanHelper2.findByPrimaryKey(value.getCourtRoomId());

            courtIdentification.append("-" + courtRoom.getCourtRoomName());

            if (isMultiSiteCourt) {
                courtIdentification.append(" (" + courtRoom.getXhbCourtSite().getShortName() + ")");
            }
        } else {
            courtIdentification.append("-" + UNIDENTIFIED_COURT_ROOM);
        }

        log.debug("Returning CourtIdentificationMessageElement " + courtIdentification.toString());

        return courtIdentification.toString();
    }
}