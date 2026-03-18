package uk.gov.courtservice.xhibit.courtlog.helpers;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoom;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSite;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearing;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * Creates a subscription value from a view value
 * 
 * @author pznwc5
 */
public class SubscriptionValueAssembler {
    private SubscriptionValueAssembler() {
        // private constructor to prevent external instantiation
    }

    /**
     * Creates a subscription value from a view value
     * 
     * @return Subscription value
     */
    public static CourtLogSubscriptionValue getSubsciptionValue(CourtLogViewValue viewVal) {
        CourtLogSubscriptionValue subVal = new CourtLogSubscriptionValue(viewVal);

        XhbScheduledHearing schedHearing = LookupHelper.getXhbScheduledHearing(viewVal);

        if (schedHearing != null) {
            XhbCourtRoom courtRoom = schedHearing.getXhbSitting().getXhbCourtRoom();
            XhbCourtSite courtSite = schedHearing.getXhbSitting().getXhbCourtSite();

            subVal.setCourtSiteId(courtRoom.getCourtSiteId());
            subVal.setCourtRoomId(courtRoom.getCourtRoomId());
            if (courtSite != null) {
            	subVal.setCourtSiteName(courtSite.getCourtSiteName());
            }

            subVal.setHearingId(schedHearing.getScheduledHearingId());
            subVal.setCourtURN(courtRoom.getUrn());
        }

        return subVal;
    }
}
