package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: Interface defining the contract that classes building message elements
 * must support.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Classes implementing this interface must provide a no arguments constructor
 * and be stateless from the point of view of individual messages. There is one
 * special case and that is the StringMessageElement which is directly
 * constructed without reflection and so can have a constructor with an
 * argument.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Eds
 * </p>
 * 
 * @author Bob Boothby, Sarah Tong
 * @version $Id: MessageElement.java,v 1.2 2006/05/31 14:21:06 bzjrnl Exp $
 */
public interface MessageElement {
    public String getElement(CourtLogSubscriptionValue value, XhbCase theCase);
}