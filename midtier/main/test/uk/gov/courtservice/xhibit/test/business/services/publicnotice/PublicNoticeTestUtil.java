package uk.gov.courtservice.xhibit.test.business.services.publicnotice;

import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Pat Fox
 * @version 1.0
 */

public class PublicNoticeTestUtil {

  public PublicNoticeTestUtil() {
  }

  public static CourtLogSubscriptionValue createSubscriptionValue(int courtRoomId,
     int eventType) {
   Integer l_courtRoomId = new Integer(courtRoomId);
   //set evnetType to id that doesn't cause status change
   Integer l_eventType = new Integer(eventType);
   CourtLogViewValue l_courtLogViewValue = new CourtLogViewValue();
   l_courtLogViewValue.setEventType(l_eventType);

   CourtLogSubscriptionValue l_courtLogSubscriptionValue =
       new CourtLogSubscriptionValue();

   l_courtLogSubscriptionValue.setCourtLogViewValue(l_courtLogViewValue);
   l_courtLogSubscriptionValue.setCourtRoomId(l_courtRoomId);
   return l_courtLogSubscriptionValue;
  }
}